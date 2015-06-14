package com.example.anpr_android;

import java.util.ArrayList;
import java.util.List;

import com.example.anpr_android.CameraContainer.TakePictureListener;

import android.R.integer;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.Area;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;


public class CameraView extends SurfaceView {

	private Camera camera_;
	private int orientation_ = 0;
	private Camera.Parameters parameters_;
	
	public CameraView(Context context) {
		super(context);
		getHolder().addCallback(callback_);
		opencamera_();
	};

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
		getHolder().addCallback(callback_);
		opencamera_();
	};

	private SurfaceHolder.Callback callback_ = new SurfaceHolder.Callback() {

		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			try {
				if(camera_ == null ){
					opencamera_();
				}
				camera_.setPreviewDisplay(getHolder());
			}catch (Exception e) {
				Toast.makeText(getContext(), "setPreviewDisplay error", Toast.LENGTH_SHORT).show();
				Log.e("cameraView", e.getMessage());
			}
			camera_.startPreview();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
			updateCameraOrientation_();
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {

			if(camera_ != null) {
				camera_.stopPreview();
				camera_.release();
				camera_= null;
			}
		}
		
	};

	private boolean opencamera_() {
		if(camera_ != null) {
			camera_.stopPreview();
			camera_.release();
			camera_ = null;
		}
		
		try {
			camera_ = Camera.open();
		}catch(Exception e) {
			camera_= null;
			Toast.makeText(getContext(), "open Camera error", Toast.LENGTH_SHORT).show();
			Log.e("cameraView", e.getMessage());
			return false;
		}
		setCameraParameters_();
		return true;
	}

	protected void onFocus(Point point,AutoFocusCallback callback){
		Camera.Parameters parameters=camera_.getParameters();
		if (parameters.getMaxNumFocusAreas()<=0) {
			camera_.autoFocus(callback);
			return;
		}
		List<Area> areas=new ArrayList<Camera.Area>();
		int left=point.x-300;
		int top=point.y-300;
		int right=point.x+300;
		int bottom=point.y+300;
		left=left<-1000?-1000:left;
		top=top<-1000?-1000:top;
		right=right>1000?1000:right;
		bottom=bottom>1000?1000:bottom;
		areas.add(new Area(new Rect(left,top,right,bottom), 100));
		parameters.setFocusAreas(areas);
		try {
			camera_.setParameters(parameters);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		camera_.autoFocus(callback);
	}

	public void takePicture(PictureCallback callback, TakePictureListener listener) {
		camera_.takePicture(null, null, callback);
	}
	
	private void setCameraParameters_() {
		Camera.Parameters parameters = camera_.getParameters();

		List<Camera.Size>sizelist = parameters.getSupportedPreviewSizes();
		if(sizelist.size()>0) {
			Size cameraSize = sizelist.get(0);
			parameters.setPreviewSize(cameraSize.width, cameraSize.height);
		}

		sizelist = parameters.getSupportedPictureSizes();
		if(sizelist.size()>0) {
			Size cameraSize = sizelist.get(0);
			for(Size size : sizelist) {
				if(size.width*size.height<100*10000) {
					cameraSize = size;
					break;
				}
			}
			parameters.setPictureSize(cameraSize.width, cameraSize.height);
		}
		parameters.setPictureFormat(ImageFormat.JPEG);
		parameters.setJpegQuality(100);
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		camera_.setParameters(parameters);
		startOrientationChangeListerner_();
	}


	private void startOrientationChangeListerner_() {
		OrientationEventListener mOrEventListener = new OrientationEventListener(getContext()) {

			@Override
			public void onOrientationChanged(int rotation) {
				if (((rotation >= 0) && (rotation <= 45)) || (rotation > 315)) {
					rotation=0;
				} else if ((rotation > 45) && (rotation <= 135))  {
					rotation=90;
				}
				else if ((rotation > 135) && (rotation <= 225)) {
					rotation=180;
				} 
				else if((rotation > 225) && (rotation <= 315)) { 
					rotation=270;
				}else {
					rotation=0;
				}
				if(rotation==orientation_)
					return;
				orientation_=rotation;
				updateCameraOrientation_();
			}  
		};  
		mOrEventListener.enable();
	}

	private void updateCameraOrientation_() {
		if(camera_ != null) {
			Camera.Parameters parameters = camera_.getParameters();
			int rotation  = 90+orientation_==360?0:90+orientation_;
			
			parameters.setRotation(rotation);
			camera_.setDisplayOrientation(90);
			try {
				camera_.setParameters(parameters);
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	}
	
	public int Rotation() {
		return orientation_;
	}

}
