package com.anjoyo.activity;

import java.util.ArrayList;
import java.util.List;

import com.anjoyo.app.MusicApplication;
import com.anjoyo.app.MusicApplication.OnServiceConnectedListener;
import com.anjoyo.mykugoumusic.R;
import com.anjoyo.viewpager.GuideContoler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class GuideActivity extends Activity {
	private SharedPreferences preferences;
	private MusicApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		preferences=getSharedPreferences("configure", MODE_PRIVATE);
		
		if (isFirstRun()) {
			initViewPager();
			app = (MusicApplication) getApplication();
			app.setOnServiceConnectedListener(onServiceConnectedListener);
		}else{
			gotoWelcom();
		}
	}
	OnServiceConnectedListener onServiceConnectedListener=new OnServiceConnectedListener() {
		
		@Override
		public void onServiceConnected() {
			app.getMusicService().startScan();
			
		}
	};
	private boolean isFirstRun() {
		boolean isFirstRun = preferences.getBoolean("isFirstRun", true);
		if (isFirstRun) {
			 preferences.edit().putBoolean("isFirstRun", false).commit();
		}
		return isFirstRun;
	}
	private void initViewPager(){
		GuideContoler contoler=new GuideContoler(this);
		List<View> views=new ArrayList<View>();
		LayoutInflater inflater=LayoutInflater.from(this);
		views.add(inflater.inflate(R.layout.guide_1, null));
		views.add(inflater.inflate(R.layout.guide_2, null));
		views.add(inflater.inflate(R.layout.guide_3, null));
		contoler.init(views);
		views.get(views.size()-1).findViewById(R.id.btnEnter_guide).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				gotoWelcom();

			}
		});

	}
	protected void gotoWelcom() {
		startActivity(new Intent(this,WelcomActivity.class));
		finish();
	}
}
