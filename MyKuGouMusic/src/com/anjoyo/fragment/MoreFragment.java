package com.anjoyo.fragment;

import com.anjoyo.mykugoumusic.R;
import com.anjoyo.service.MusicService.MusicReceiver;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

@SuppressLint("NewApi")
public class MoreFragment extends Fragment {
	private View mContentView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView=inflater.inflate(R.layout.fragment_more, null);

		return mContentView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mContentView.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().sendBroadcast(new Intent(MusicReceiver.ACTION_PLAY_EXIT));
				
			}
		});
	}
}
