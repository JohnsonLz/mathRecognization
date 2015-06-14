package com.example.anpr_android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class RecognizeActivity extends Activity {
	
	private TextView tv_;
	private String path_;
	private static Handler handler_;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recognize);
		 Intent intent=getIntent();  
	     Bundle bundle=intent.getExtras();  
	     path_=bundle.getString("path");  
	    
	     tv_ = (TextView)findViewById(R.id.recognize_text);
	     tv_.setText(path_);

	     handler_ = new Handler() {
	    	 public void handleMessage(Message msg) {
	    		 String result = (String)msg.obj;
	    		 tv_.setText(result);
	    	 }
	     };
	
	}

	public static Handler getHandler() {
		// TODO Auto-generated method stub
		return handler_;
	}
	
}
