package com.rhomobile.rhodes.camera;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.rhomobile.rhodes.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FileList extends Activity implements OnClickListener{

	public final static String BASE_CAMERA_DIR = "/sdcard/dcim/Camera/";
	
	private Button okButton;
	private Button cancelButton;
	private TextView lookIn;
	private List<String> items = null;
	private String selectedFilePath = "";
	private ImageView imagePreview;
	private ListView filesList;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		setContentView(R.layout.directory_list);

		imagePreview = (ImageView) findViewById(R.id.preview);

		filesList = (ListView) findViewById(R.id.filesList);
			
		fill(new File(BASE_CAMERA_DIR).listFiles());

		okButton = (Button) findViewById(R.id.okButton);
		cancelButton = (Button) findViewById(R.id.cancelButton);

		lookIn = (TextView) findViewById(R.id.lookIn);

		lookIn.setText("Look In: " + BASE_CAMERA_DIR);

		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);
		
		filesList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				int selectionRowID = position;
				try {
					File file = new File(BASE_CAMERA_DIR + items.get(selectionRowID));
					selectedFilePath = file.getAbsolutePath();

					Bitmap bm;
					bm = Bitmap.createScaledBitmap(BitmapFactory
							.decodeFile(file.getAbsolutePath()), 176, 144, true);
					imagePreview.setImageBitmap(bm);
				} catch (Exception e) {
					Log.e("FileList", e.getMessage());
				}
			}
			
		});
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.okButton:
			com.rhomobile.rhodes.camera.Camera.callCallback(this.selectedFilePath);
			finish();
			break;
		case R.id.cancelButton:
			selectedFilePath = "";
			com.rhomobile.rhodes.camera.Camera.callCallback(this.selectedFilePath);
			finish();
			break;
		}
	}
	
	private void fill(File[] files) {
		items = new ArrayList<String>();
		for (File file : files) {
			if (!file.isDirectory()) {
				
				boolean skip = false;
				
				if ( file.getName().indexOf(".png") == -1 && file.getName().indexOf(".jpg") == -1 )
					skip = true;
				
				if ( !skip )
					items.add(file.getName());
			}
		}
		ArrayAdapter<String> fileList = new ArrayAdapter<String>(this,
				R.layout.file_row, items);
		
		filesList.setAdapter(fileList);
	}

	public String getSelectedFilePath() {
		return selectedFilePath;
	}
}