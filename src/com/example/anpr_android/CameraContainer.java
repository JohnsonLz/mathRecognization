package com.example.anpr_android;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import com.example.anpr_android.R;

import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;

public class CameraContainer extends RelativeLayout  {

	private CameraView cameraView_;
	private FocusImageView focusImageView_;
	private String savePath_;
	private TakePictureListener listener_;
	private DataHandler dataHandler_;
	private Handler handler_;
	private FocusThread_ ft_;
	public boolean focusThread_;

	public CameraContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView_(context);
		handler_ = new Handler();
	}

	private void initView_(Context context) {
		inflate(context, R.layout.cameracontainer, this);
		cameraView_=(CameraView)findViewById(R.id.cameraView);
		focusThread_ = true;
		ft_ = new FocusThread_();
		ft_.start();
		focusImageView_ = (FocusImageView)findViewById(R.id.focusImageView);

	}

	public void setRootPath(String rootPath) {
		this.savePath_ = rootPath;
	}

	public void takePicture() {
		takePicture(pictureCallback_, listener_);
	}

	public void takePicture(TakePictureListener listener) {
		this.listener_ = listener;
		takePicture(pictureCallback_, listener_);
	}

	public void takePicture(PictureCallback callback, TakePictureListener listener) {
		focusImageView_.startFocus(new Point(getWidth(),getHeight()));
		focusThread_=false;
		cameraView_.takePicture(callback, listener);
	}
	
	private final PictureCallback pictureCallback_ = new PictureCallback() {
		
		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			if(savePath_ == null) throw new RuntimeException("savePath is null");
			if(dataHandler_ == null) dataHandler_ = new DataHandler();
			int rotation = cameraView_.Rotation();
			dataHandler_.setMaxSize(1500);
			if(listener_ != null) listener_.onTakePictureEnd();
			
			SaveThread_ st = new SaveThread_(data,rotation);
			st.start();
		}
	};

	private final AutoFocusCallback autoFocusCallback_=new AutoFocusCallback() {

		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			
			if (success) {
				focusImageView_.onFocusSuccess();
			}else {
				focusImageView_.onFocusFailed();

			}
		}
	};
	
	private class FocusThread_ extends Thread {
				
		public void run() {
			while(focusThread_) {
					cameraView_.onFocus(new Point(getWidth(),getHeight()), autoFocusCallback_);
					try {
						sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}		
	}
	
	private class SaveThread_ extends Thread {
		
		private byte[] data_;
		private int rotation_;
		
		public SaveThread_(byte[] data,int rotation) {
			data_ = data;
			rotation_ =rotation;
		}
		public void run() {
			String path = dataHandler_.Path();
			dataHandler_.save(data_);
			if(listener_!=null) listener_.onSaveEnd(path,rotation_+90);
		}
	}

	private final class DataHandler {

		private String imageFolder_;
		private int maxSize_ = 1024;
		private String imagePath_;

		public DataHandler() {
			imageFolder_ = FileOperateUtil.getFolderPath(getContext(),FileOperateUtil.TYPE_IMAGE, savePath_);
			File folder = new File(imageFolder_);
			if(!folder.exists()) {
				folder.mkdirs();
			}
		}

		public Bitmap save(byte[] data) {
			if(data!=null) {
				Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);
				Log.i("imagepath : %s \n", imagePath_);
				File file = new File(imagePath_);
				try {
					FileOutputStream fos = new FileOutputStream(file);
					ByteArrayOutputStream bos = compress(bm);
					fos.write(bos.toByteArray());
					fos.flush();
					fos.close();
					return bm;
				}catch(Exception e) {
					Log.e("cameraContainer", e.toString());
					Toast.makeText(getContext(), "save image error", Toast.LENGTH_SHORT).show();
				}
			}else {
				Toast.makeText(getContext(), "DataHandler is null", Toast.LENGTH_SHORT).show();
			}
			return null;
		}
		
		public ByteArrayOutputStream compress(Bitmap bitmap) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
			int options = 99;
			while( baos.toByteArray().length / 1024 > maxSize_) {
				options -= 3;
				if(options<0) {
					break;
				}
				Log.i("cameraContainer", baos.toByteArray().length / 1024+"");
				baos.reset();
				bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
			}
			Log.i("cameraContainer","compress over");
			return baos;
		}

		public void setMaxSize(int maxSize) {
			this.maxSize_ = maxSize;
		}
		
		public String Path() {
			String imgName = FileOperateUtil.createFileNmae(".jpg");
			imagePath_ = imageFolder_+File.separator+imgName;
			return imagePath_;
		}
	}

	public static interface TakePictureListener {

		public void onTakePictureEnd();
		public void onSaveEnd(String path,int rotation);
	}



}
