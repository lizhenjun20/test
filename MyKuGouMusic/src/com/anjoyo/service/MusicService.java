package com.anjoyo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.anjoyo.bean.MusicQueue;
import com.anjoyo.bean.PlayQueueInfo;
import com.anjoyo.db.MusicDbHelperDao;
import com.anjoyo.fragment.FragmentLocalBase.FragmentReceiver;
import com.anjoyo.mykugoumusic.R;
import com.anjoyo.tools.Constant;
import com.anjoyo.tools.MusicScaner;
import com.anjoyo.tools.MusicScaner.OnCompleteListener;
import com.google.gson.Gson;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;

public class MusicService extends Service {
	private MediaPlayer mPlayer;
	public static final int PLAY_MODE_NORMAL = 0;  
	/**单曲循环**/
	public static final int PLAY_MODE_REPEAT_ONE = 1; 
	/**列表循环**/
	public static final int PLAY_MODE_REPEAT_ALL= 2; 
	/**随机播放**/
	public static final int PLAY_MODE_RANDOM= 3;
	private int mCurPlayMode = PLAY_MODE_NORMAL;
	private List<MusicQueue> mMusicQueues=new ArrayList<MusicQueue>();
	private MusicDbHelperDao dbHelper;
	
	private int curPlayPos=-1;
	private String curQeueTag="";
	public class MusicBind extends Binder{
		public MusicService getWelcomService(){
			return MusicService.this;
		}
	}
	private MusicBind musicBind=new MusicBind();
	@Override
	public IBinder onBind(Intent intent) {

		return musicBind;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		mPlayer=new MediaPlayer();
		dbHelper=new MusicDbHelperDao(this);
		registerReceiver();
	}
	private void registerReceiver(){
		BroadcastReceiver receiver=new MusicReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction(MusicReceiver.ACTION_LOCAL_LISTE);
		filter.addAction(MusicReceiver.ACTION_PLAY_OR_PAUSE);
		filter.addAction(MusicReceiver.ACTION_ADDTO_QUEUE);
		filter.addAction(MusicReceiver.ACTION_PLAY_NEXT);
		filter.addAction(MusicReceiver.ACTION_PLAY_EXIT);
		filter.addAction(MusicReceiver.ACTION_PLAY_PRE);
		registerReceiver(receiver,filter);
		
		
	}
	public void playLogin(){
		mPlayer.reset();
		try {
			mPlayer.setDataSource(this, Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.login));
			mPlayer.prepare();
			mPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void startScan() {
		MusicScaner scaner=new MusicScaner(this);
		scaner.setOnCompleteListener(new OnCompleteListener() {
			
			@Override
			public void OnComplete(int count) {
				inintQueueLocalMusic();
				
			}
		});
		scaner.startScan();
	}
	/****
	 * 初始化本地音乐列表
	 */
	public void inintQueueLocalMusic() {
		mMusicQueues.clear();
		mMusicQueues.addAll(dbHelper.getLocalQueue());
		for (MusicQueue music:mMusicQueues) {
			System.out.println("列表"+music.getName());
			
		}
		curQeueTag=Constant.TAG_QUEUE_LOCAL;
	}
	public class MusicReceiver extends BroadcastReceiver{
		public static final String ACTION_LOCAL_LISTE="com.anjoyo.service.ACTION_LOCAL_LISTE";
		public static final String ACTION_PLAY_OR_PAUSE="com.anjoyo.service.ACTION_PLAY_OR_PAUSE";
		public static final String ACTION_ADDTO_QUEUE="com.anjoyo.service.ACTION_ADDTO_QUEUE";
		public static final String ACTION_PLAY_NEXT="com.anjoyo.service.ACTION_PLAY_NEXT";
		public static final String ACTION_PLAY_EXIT="com.anjoyo.service.ACTION_PLAY_EXIT";
		public static final String ACTION_PLAY_PRE="com.anjoyo.service.ACTION_PLAY_PRE";
		@Override
		public void onReceive(Context context, Intent intent) {
			String action=intent.getAction();
			if (ACTION_LOCAL_LISTE.equals(action)) {
				int position=intent.getIntExtra(Constant.KEY_POSITION, 0);
				playEntrence(position);
				
			}else if(ACTION_PLAY_OR_PAUSE.equals(action)){
				playOrPause();
				
			}else if(ACTION_ADDTO_QUEUE.equals(action)){
				MusicQueue music = intent.getParcelableExtra(Constant.KEY_MUSIC);
				addToQueue(music);
				
			}else if(ACTION_PLAY_NEXT.equals(action)){
				boolean byUser = false;
				playNext(true);
			}else if(ACTION_PLAY_EXIT.equals(action)){
				exit();
				
			}else if (ACTION_PLAY_PRE.equals(action)) {
				playPre();
				
			}
			
		}
		
	}
	public void playEntrence(int position) {
		if (curPlayPos!=position) {
			play(position);
			
		}
	}
	public void playPre() {
		int position=getPrePosition();
		if (position==-1) {
			return;
			
		}
		play(position);
		
	}
	private int getPrePosition() {
		if (mMusicQueues.size()==0) {
			return -1;

		}
		int position=-1;
		if (mCurPlayMode==PLAY_MODE_RANDOM) {
			position=(int) (Math.random()*mMusicQueues.size());
		}else {
			position=curPlayPos-1;
			if (position==-1) {
				position=mMusicQueues.size()-1;
				
			}
		}
		return position;
	}
	public void exit() {
		
		savePlayQueue();
		stop();
		stopSelf();
		android.os.Process.killProcess(android.os.Process.myPid());
		
	}
	/***
	 * 播放下一首
	 */
	public void playNext(boolean byUser) {
		int position=getNextPosition(byUser);
		if (position==-1) {
			return;
			
		}
		play(position);
		
	}
	private int getNextPosition(boolean byUser) {
		int position=-1;
		if (byUser) {
			if (mMusicQueues.size()==0) {
				inintQueueLocalMusic();
				if (mMusicQueues.size()==0) {
					return -1;
					
				}else {
					if(mCurPlayMode == PLAY_MODE_RANDOM){
						return (int) (Math.random()*mMusicQueues.size());
					}else{
						return 0;
					}
				}
				
			}
			switch (mCurPlayMode) {
			case PLAY_MODE_RANDOM:
				position = (int) (Math.random()*mMusicQueues.size());
				break;

			default:
				position = curPlayPos + 1;
				if(position == mMusicQueues.size()){
					position = 0;
				}
				break;
			}
			
		}
		return position;
	}
	public void addToQueue(MusicQueue music) {
		int position=getAddPosition();
		music.setIndex(position);
		mMusicQueues.add(position, music);
		if (mMusicQueues.size()==1) {
			play(position);
			
		}
		curQeueTag=Constant.TAG_QUEUE_ADDED;
		
	}
	private int getAddPosition() {
		int position=curPlayPos+1;
		for (int i = curPlayPos+1; i < mMusicQueues.size(); i++) {
			if (mMusicQueues.get(i).isAdd()) {
				position++;
			}else{
				mMusicQueues.get(i).setIndex(mMusicQueues.get(i).getIndex()+1);
			}
			
		}
		return position;
	}
	private void play(int position) {
		mPlayer.reset();
		try {
			mPlayer.setDataSource(mMusicQueues.get(position).getPath());
			mPlayer.prepare();
			mPlayer.start();
			curPlayPos=position;
			Intent intent=new Intent(FragmentReceiver.ACTION_PLAY);
			intent.putExtra(Constant.KEY_MUSIC, getCurMusic());
			sendBroadcast(intent);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public String getCurPlayPath(){
		if (curPlayPos==-1) {
			return "";
			
		}
		return mMusicQueues.get(curPlayPos).getPath();
	}
	/****
	 * 播放或暂停
	 */
	private void playOrPause(){
		if (mPlayer.isPlaying()) {
			pause();
			
		}else{
			start();
		}
		
	}
	private void stop(){
		mPlayer.stop();
		mPlayer.release();
		mPlayer=null;
	}
	private void start() {
		mPlayer.start();
		Intent intent=new Intent(FragmentReceiver.ACTION_PLAY);
		intent.putExtra(Constant.KEY_MUSIC, getCurMusic());
		sendBroadcast(intent);
	}
	/***
	 * 暂停
	 */
	private void pause() {
		mPlayer.pause();
		Intent intent=new Intent(FragmentReceiver.ACTION_PAUSE);
		intent.putExtra(Constant.KEY_MUSIC, getCurMusic());
		sendBroadcast(intent);
	}
	private MusicQueue getCurMusic() {
		return mMusicQueues.get(curPlayPos);
	}
	public String getCurQueueTag() {
		// TODO Auto-generated method stub
		return curQeueTag;
	}
	private void savePlayQueue(){
		PlayQueueInfo info=new PlayQueueInfo();
		info.setCurMusicPosition(mPlayer.getCurrentPosition());
		info.setCurPlayPos(curPlayPos);
		info.setMusics(mMusicQueues);
		info.setCurQueueTag(curQeueTag);
		Gson gson=new Gson();
		String json = gson.toJson(info);
		SharedPreferences preferences=getSharedPreferences(Constant.SHARED_MUSIC, MODE_PRIVATE);
		preferences.edit().putString(Constant.SHARED_QUEUE, json).commit();
		
	}

}
