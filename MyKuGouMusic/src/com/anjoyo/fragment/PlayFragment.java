package com.anjoyo.fragment;

import com.anjoyo.bean.MusicQueue;
import com.anjoyo.mykugoumusic.R;
import com.anjoyo.service.MusicService.MusicReceiver;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class PlayFragment extends FragmentLocalBase {
	private View mContentView;
	private ImageButton mNextBtn,mPlayBtn;
	private TextView mMusicNameTv,mMusicArtistTv;
	private View goToPlaying;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView=inflater.inflate(R.layout.play_bottom, null);
		return mContentView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
        registerReceiver();
		initViews();
	}
	private void initViews() {
		mPlayBtn = (ImageButton) mContentView.findViewById(R.id.btnPlay_play_bottom);
		mNextBtn = (ImageButton) mContentView.findViewById(R.id.btnNext_play_bottom);
		mMusicNameTv = (TextView) mContentView.findViewById(R.id.tvMusicName_play_bottom);
		mMusicArtistTv = (TextView) mContentView.findViewById(R.id.tvIvavatar_play_bottom);
		goToPlaying=mContentView.findViewById(R.id.gotoPlaying);
		mNextBtn.setOnClickListener(onClickListener);
		mPlayBtn.setOnClickListener(onClickListener);
		goToPlaying.setOnClickListener(onClickListener);
	}
	private OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (v==mNextBtn) {
				getActivity().sendBroadcast(new Intent(MusicReceiver.ACTION_PLAY_NEXT));
				
			}else if (v==mPlayBtn) {
				getActivity().sendBroadcast(new Intent(MusicReceiver.ACTION_PLAY_OR_PAUSE));
			}else if (v==goToPlaying) {
				gotoPlaying();
				
			}
			
		}
	};
	@Override
	public void onPause(MusicQueue music) {
		mPlayBtn.setImageResource(R.drawable.selector_btn_play_play_bottom);
		
	}
	protected void gotoPlaying() {
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		ft.add(R.id.mContainerMain, new PlayingFragment(), "PlayingFragment");
		ft.addToBackStack("PlayingFragment");
		ft.commit();
		
	}
	@Override
	public void onPlay(MusicQueue music) {
		mPlayBtn.setImageResource(R.drawable.selector_btn_pause_play_bottom);
		mMusicArtistTv.setText(music.getArtist());
		mMusicNameTv.setText(music.getName());
		
	}

}
