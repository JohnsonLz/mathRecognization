package com.example.anpr_android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.example.anpr_android.CameraContainer.TakePictureListener;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener, TakePictureListener {
	
	private String saveRoot_;
	private CameraContainer container_;
	private ImageButton cameraShutterButton_;
	private anpr recognizer_;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		new DataTranThread().start();

		requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
							WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.camera);
		container_ = (CameraContainer)findViewById(R.id.container);
		cameraShutterButton_ = (ImageButton)findViewById(R.id.btn_shutter_camera);

		
		cameraShutterButton_.setOnClickListener(this);

		saveRoot_ = "test";
		container_.setRootPath(saveRoot_);
		//DataTransport();
		//anpr app = new anpr();
		//TextView tv = new TextView(this);
		//String path ="/data/data/com.example.anpr_android/data/test4.jpg";
		//tv.setText( app.Recognize_result(path));

		//setContentView(tv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()) {
			case R.id.btn_shutter_camera:
				cameraShutterButton_.setClickable(false);
				container_.takePicture(this);
				break;
			default:
				break;
		}
	}

	@Override 
	public void onTakePictureEnd() {
		cameraShutterButton_.setClickable(true);
		Intent intent = new Intent();
		intent.setClass(MainActivity.this, RecognizeActivity.class);
		Bundle bundle = new Bundle();  
		bundle.putString("path","wait");
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onSaveEnd(String path,int rotation){
		String result = recognizer_.Recognize_result(path,rotation);
		final Handler handler  = RecognizeActivity.getHandler();
		Message msg = Message.obtain();
		msg.obj=result;
		handler.sendMessage(msg);
		finish();
	}

	@Override 
	protected void onResume() {
		super.onResume();
	}

	public void DataTransport() {
		
		String DestinationDir = "/data/data/com.example.anpr_android/data";
		File dir = new File(DestinationDir);
		if(!dir.exists()) {
			dir.mkdir();
		}

		File file = new File(dir, "SVM_DATA.xml");
		try {
			if(!file.exists()) {
				file.createNewFile();
			}
			AssetManager a = getAssets();
			InputStream is = a.open("SVM_DATA.xml");
			FileOutputStream fos  = new FileOutputStream(file);
			byte[] buffere = new byte[is.available()];
			is.read(buffere);
			fos.write(buffere);
			is.close();
			fos.close();

		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	
	}
	
	private class DataTranThread extends Thread {
		public void run() {
			DataTransport();
			recognizer_ = new anpr();
			recognizer_.CreatSVM();
		}
	}
	
	static {
		System.loadLibrary("opencv_core");
		System.loadLibrary("opencv_imgproc");
		System.loadLibrary("opencv_highgui");
		System.loadLibrary("opencv_ml");
		System.loadLibrary("vet");
		System.loadLibrary("contiour");
		System.loadLibrary("processor");
		System.loadLibrary("classify");
		System.loadLibrary("caculator");
		System.loadLibrary("anpr");

	}

}
