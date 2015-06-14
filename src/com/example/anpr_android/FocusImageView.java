package com.example.anpr_android;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FocusImageView extends ImageView {
	public final static String TAG = "FocusImageView";
	private static final int NO_ID = -1;
	private int focusImg_ = NO_ID;
	private int focusSucceedImg_ = NO_ID;
	private int focusSFailedImg_ = NO_ID;
	private Animation amimation_;
	private Handler handler_;

	public FocusImageView(Context context) {
		super(context);
		amimation_=AnimationUtils.loadAnimation(getContext(), R.anim.focusview_show);
		setVisibility(View.GONE);
		handler_=new Handler();
	}

	public FocusImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		amimation_=AnimationUtils.loadAnimation(getContext(), R.anim.focusview_show);
		handler_=new Handler();
	     
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FocusImageView);
		focusImg_ = a.getResourceId(R.styleable.FocusImageView_focus_focusing_id, NO_ID);
		focusSucceedImg_=a.getResourceId(R.styleable.FocusImageView_focus_success_id, NO_ID);
		focusSFailedImg_=a.getResourceId(R.styleable.FocusImageView_focus_fail_id, NO_ID);
		a.recycle();

		if (focusImg_==NO_ID||focusSucceedImg_==NO_ID||focusSFailedImg_==NO_ID) 
			throw new RuntimeException("Animation is null");
	}


	public void startFocus(Point point){
		if (focusImg_==NO_ID||focusSucceedImg_==NO_ID||focusSFailedImg_==NO_ID) 
			throw new RuntimeException("focus image is null");

		//RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)getLayoutParams();
		//params.topMargin=point.y-getHeight()/2;
		//params.leftMargin=point.x-getWidth()/2;
		//setLayoutParams(params);	

		setImageResource(focusImg_);
		setVisibility(View.VISIBLE);
		startAnimation(amimation_);	

		handler_.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setVisibility(View.GONE);
			}
		},500);
	}
	
	public void onFocusSuccess(){
		
		setVisibility(View.VISIBLE);
		
		setImageResource(focusSucceedImg_);
		handler_.removeCallbacks(null, null);
		handler_.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setVisibility(View.GONE);
			}
		},500);
		
	}
	
	
	public void onFocusFailed(){
		setImageResource(focusSFailedImg_);

		handler_.removeCallbacks(null, null);
		handler_.postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				setVisibility(View.GONE);
			}
		},1000);
	}


	public void setFocusImg(int focus) {
		this.focusImg_ = focus;
	}


	public void setFocusSucceedImg(int focusSucceed) {
		this.focusSucceedImg_ = focusSucceed;
	}
}
