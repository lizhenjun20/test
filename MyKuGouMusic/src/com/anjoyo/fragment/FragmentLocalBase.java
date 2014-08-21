package com.anjoyo.fragment;
import com.anjoyo.bean.MusicQueue;
import com.anjoyo.mykugoumusic.R;
import com.anjoyo.tools.Constant;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public abstract class FragmentLocalBase extends Fragment {
	private View mBackBtn,mMenuBtn,mSearchBtn;
	private TextView mtitleTv;
	private BroadcastReceiver receiver;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	protected void registerReceiver(){
		receiver = new FragmentReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(FragmentReceiver.ACTION_PAUSE);
		filter.addAction(FragmentReceiver.ACTION_PLAY);
		filter.addAction(FragmentReceiver.ACTION_PROGRESS_CHANGE);
		getActivity().registerReceiver(receiver , filter );
		
	}
	public abstract void onPause(MusicQueue music);
	public abstract void onPlay(MusicQueue music);
	public void setupBack(){
		mBackBtn = getView().findViewById(R.id.btnBack_title_commom);
		mBackBtn.setVisibility(View.VISIBLE);
		mBackBtn.setOnClickListener(clickListener);
	}
	public void setupMenu(){
		mMenuBtn = getView().findViewById(R.id.btnMenu_title_commom);
		mMenuBtn.setVisibility(View.VISIBLE);
		mMenuBtn.setOnClickListener(clickListener);
	}
	public void setupTitle(String title){
		mtitleTv = (TextView) getView().findViewById(R.id.tvTitle_title_common);
		mtitleTv.setVisibility(View.VISIBLE);
		mtitleTv.setText(title);
	}
	OnClickListener clickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	public class FragmentReceiver extends BroadcastReceiver{
		/**如果Service暂停播放了，Service应该发这个Action对应的广播**/
		public static final String ACTION_PAUSE = "com.anjoyo.anjoyomusicplayer.fragment.FragmentReceiver.ACTION_PAUSE";
		/**如果Service开始播放了，Service应该发这个Action对应的广播**/
		public static final String ACTION_PLAY = "com.anjoyo.anjoyomusicplayer.fragment.FragmentReceiver.ACTION_PLAY";
		/**如果Service中国播放进度改变了，Service应该发这个Action对应的广播**/
		public static final String ACTION_PROGRESS_CHANGE = "com.anjoyo.anjoyomusicplayer.fragment.FragmentReceiver.ACTION_PROGRESS_CHANGE";
		
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(ACTION_PAUSE.equals(action)){
				MusicQueue music = intent.getParcelableExtra(Constant.KEY_MUSIC);
				onPause(music);
			}else if(ACTION_PLAY.equals(action)){
				MusicQueue music = intent.getParcelableExtra(Constant.KEY_MUSIC);
				onPlay(music);
			}
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(receiver != null){
			getActivity().unregisterReceiver(receiver);
		}
	}
}
