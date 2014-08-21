package com.anjoyo.fragment;

import com.anjoyo.activity.MainActivity;
import com.anjoyo.mykugoumusic.R;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class MainFragment extends Fragment {
	private View mContentView;
	private View mLocalMusic;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView=inflater.inflate(R.layout.fragment_main, null);
		return mContentView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mLocalMusic=mContentView.findViewById(R.id.layoutGotoLocalMusic_fragment_main);
		initViews();


	}
	private void initViews() {
		mLocalMusic.setOnClickListener(onClickListener);

	}
	private OnClickListener onClickListener=new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v==mLocalMusic) {
				gotoLocalMusic();
			}
			

		}
	};
	private void gotoLocalMusic() {
		MainActivity mainActivity=(MainActivity) getActivity();
		mainActivity.gotoLocalMusicMain();

	}

}
