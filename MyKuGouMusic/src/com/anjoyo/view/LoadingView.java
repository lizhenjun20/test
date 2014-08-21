package com.anjoyo.view;

import com.anjoyo.mykugoumusic.R;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;

public class LoadingView extends View {
	private AnimationDrawable animationDrawable;

	public LoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundResource(R.anim.img_scanning);
		animationDrawable=(AnimationDrawable) getBackground();
		animationDrawable.start();
		
	}

}
