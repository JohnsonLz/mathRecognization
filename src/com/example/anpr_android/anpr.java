package com.example.anpr_android;

public class anpr {

	public native void CreatSVM();
	public native String Recognize_result(String path, int angle);

}
