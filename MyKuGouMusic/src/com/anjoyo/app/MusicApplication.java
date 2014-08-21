package com.anjoyo.app;

import com.anjoyo.service.MusicService;
import com.anjoyo.service.MusicService.MusicBind;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MusicApplication extends Application {
	private MusicService mWelcomService;

	public MusicService getMusicService() {
		return mWelcomService;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		startWelcomeService();
	}
	private void startWelcomeService() {
		Intent service=new Intent(this, MusicService.class);
		startService(service);
		bindService(service, conn, BIND_AUTO_CREATE);

	}
	ServiceConnection conn=new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			MusicBind binder=(MusicBind) service;
			mWelcomService=binder.getWelcomService();
			if (onServiceConnectedListener!=null) {
				onServiceConnectedListener.onServiceConnected();				
			}
		}
	};
	private OnServiceConnectedListener onServiceConnectedListener;
	
	public void setOnServiceConnectedListener(OnServiceConnectedListener onServiceConnectedListener) {
		this.onServiceConnectedListener = onServiceConnectedListener;
	}

	public interface OnServiceConnectedListener{
		void onServiceConnected();
	}
}
