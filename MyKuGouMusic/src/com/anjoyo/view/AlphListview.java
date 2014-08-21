package com.anjoyo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ListView;

public class AlphListview extends ListView {

	public AlphListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		super.onVisibilityChanged(changedView, visibility);
		
		if(visibility == View.VISIBLE){
			startAnimation(getAnim());
		}
	}
	private AlphaAnimation anim;
	private AlphaAnimation getAnim(){
		if(anim == null){
			anim = new AlphaAnimation(0, 1);
			anim.setDuration(700);
			anim.setFillAfter(true);
		}
		return anim;
	}
	
}
