package com.anjoyo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.anjoyo.bean.MusicQueue;
import com.anjoyo.mykugoumusic.R;
import com.anjoyo.service.MusicService.MusicReceiver;

public class PlayingFragment extends FragmentLocalBase{
	private View mContentView;
	private ImageButton mModeBtn,mNextBtn,mPreBtn,mPlayBtn,mMoreBtn;
	private TextView mMusicNameTv,mArtistTv;
	private View mPlayQueue;
	private PopupWindow mPlayListPop;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView=inflater.inflate(R.layout.fragment_playing, null);
		return mContentView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerReceiver();
		inintViews();
	}

	private void inintViews() {
		mMusicNameTv = (TextView) mContentView.findViewById(R.id.tvMusicName_title_playing);
		mArtistTv = (TextView) mContentView.findViewById(R.id.tvArtist_title_playing);
		mModeBtn = (ImageButton) mContentView.findViewById(R.id.btnMode_playing_bottom);
		mPreBtn = (ImageButton) mContentView.findViewById(R.id.btnPre_playing_bottom);
		mPlayBtn = (ImageButton) mContentView.findViewById(R.id.btnPlay_playing_bottom);
		mNextBtn = (ImageButton) mContentView.findViewById(R.id.btnNext_playing_bottom);
		mMoreBtn = (ImageButton) mContentView.findViewById(R.id.btnMore_playing_bottom);
		mPlayQueue=mContentView.findViewById(R.id.btnPlayList_title_playing);
		mModeBtn.setOnClickListener(onClickListener);
		mPreBtn.setOnClickListener(onClickListener);
		mPlayBtn.setOnClickListener(onClickListener);
		mNextBtn.setOnClickListener(onClickListener);
		mMoreBtn.setOnClickListener(onClickListener);
		mPlayQueue.setOnClickListener(onClickListener);
	}
	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v==mPreBtn) {
				getActivity().sendBroadcast(new Intent(MusicReceiver.ACTION_PLAY_PRE));
			}else if (v==mModeBtn) {
				
			}else if (v==mPlayBtn) {
				getActivity().sendBroadcast(new Intent(MusicReceiver.ACTION_PLAY_OR_PAUSE));
				
			}else if (v==mMoreBtn) {
				
			}else if (v==mNextBtn) {
				getActivity().sendBroadcast(new Intent(MusicReceiver.ACTION_PLAY_NEXT));
			}else if (v==mPlayQueue) {
				showPopWindow(v);
			}
			
		}
	};
	@Override
	public void onPause(MusicQueue music) {
		mPlayBtn.setImageResource(R.drawable.selector_btn_play_playing_bottom);
		
	}

	
	protected void showPopWindow(View v) {
		initPopWindow();
		
	}
	private void initPopWindow() {
		View popView=LayoutInflater.from(getActivity()).inflate(R.layout.play_queue, null);
		mPlayListPop=new PopupWindow(popView, LayoutParams.WRAP_CONTENT,  LayoutParams.WRAP_CONTENT);
	}
	@Override
	public void onPlay(MusicQueue music) {
		mPlayBtn.setImageResource(R.drawable.selector_btn_pause_playing_bottom);
		mMusicNameTv.setText(music.getName());
		mArtistTv.setText(music.getArtist());
	}

}
