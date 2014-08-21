package com.anjoyo.tools;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class Constant {
	public static final String KEY_MUSIC="music";
	public static final String KEY_MUSIC_PATH = "music_path";
	public static final String KEY_MUSIC_NAME = "music_name";
	public static final String KEY_ARTIST = "artist";
	public static final String KEY_PROGRESS_CURRENT = "progress";
	public static final String KEY_PROGRESS_MAX = "max";
	public static final String KEY_POSITION = "position";
	
	public static final String TAG_QUEUE_LOCAL="本地音乐";
	public static final String TAG_QUEUE_NETWORK="网络音乐";
	public static final String TAG_QUEUE_ADDED="有插队";
	public static final String TAG_QUEUE_DEFALT="";
	
	public static final String SHARED_MUSIC="Music";
	public static final String SHARED_QUEUE="play_queue";
	
	
	public static int screenWidth;
	public static int screenHeight;
	public static int heightStatusBar;
	public static int bgViewWidth;
	public static int bgViewScrollWidth;
	public static void init(Activity a){
		if(screenHeight >0){
			return;
		}
		
		DisplayMetrics dm = new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		heightStatusBar = getStatusBarHeight(a);
		bgViewWidth = (int) (screenWidth * 1.5);
		bgViewScrollWidth = bgViewWidth - screenWidth;
	}
	
	public static int getStatusBarHeight(Context context){ 
		Class<?> c = null; 
		Object obj = null; 
		java.lang.reflect.Field field = null; 
		int x = 0; 
		int statusBarHeight = 0; 
		try { 
			c = Class.forName("com.android.internal.R$dimen"); 
			obj = c.newInstance(); 
			field = c.getField("status_bar_height"); 
			x = Integer.parseInt(field.get(obj).toString()); 
			statusBarHeight = context.getResources().getDimensionPixelSize(x); 
			return statusBarHeight; 
		}catch (Exception e){ 
			e.printStackTrace(); 
		} 
		return statusBarHeight; 
	} 
}
