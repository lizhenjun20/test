package com.anjoyo.activity;


import com.anjoyo.app.MusicApplication;
import com.anjoyo.mykugoumusic.R;
import com.anjoyo.tools.Constant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Constant.init(this);
		setContentView(R.layout.activity_welcome);

		new Handler(){
			public void handleMessage(android.os.Message msg) {
				gotoMain();
			};
		}.sendEmptyMessageDelayed(0, 2500);
		MusicApplication app=(MusicApplication) getApplication();
		app.getMusicService().playLogin();
	}

	protected void gotoMain() {
		Intent intent=new Intent(this,MainActivity.class);
		startActivity(intent);		
	}



}
