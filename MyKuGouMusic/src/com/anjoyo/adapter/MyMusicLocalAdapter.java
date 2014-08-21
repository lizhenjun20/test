package com.anjoyo.adapter;


import com.anjoyo.bean.MusicQueue;
import com.anjoyo.db.MusicDbHelper.LocalMusicColumns;
import com.anjoyo.mykugoumusic.R;
import com.anjoyo.service.MusicService.MusicReceiver;
import com.anjoyo.tools.Constant;

import android.animation.Animator.AnimatorListener;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyMusicLocalAdapter extends BaseAdapter {
	private Cursor cursor;
	private LayoutInflater inflater;
	private AlphabetIndexer indexer;
	private Context context;
	/**
	 * @param cursor
	 */
	public MyMusicLocalAdapter(Context context,Cursor cursor, AlphabetIndexer indexer) {
		super();
		this.cursor = cursor;
		inflater=LayoutInflater.from(context);
		this.indexer=indexer;
		this.context=context;
		endX=Constant.screenWidth/2;
		endY=Constant.screenHeight-150;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cursor.getCount();
	}

	@Override
	public Cursor getItem(int position) {
		cursor.moveToPosition(position);
		return cursor;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder=null;
		if (convertView==null) {
			convertView=inflater.inflate(R.layout.item_local_music, null);
			holder=new ViewHolder();
			convertView = inflater.inflate(R.layout.item_local_music, null);
			holder = new ViewHolder();
			holder.tipView = convertView.findViewById(R.id.tipView_item_local_music);
			holder.tvLetter = (TextView) convertView.findViewById(R.id.tvLetter_item_local_music);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvMusicName_item_local_music);
			holder.btnMore = convertView.findViewById(R.id.btnMore_item_local_music);
			holder.btnAddToQueue = convertView.findViewById(R.id.btnAddToQueue_item_local_music);
			holder.layoutMore = convertView.findViewById(R.id.layoutMore);
			holder.btnSetring = convertView.findViewById(R.id.btnSetring_item_local_music);
			holder.btnAddTo = convertView.findViewById(R.id.btnAddTo_item_local_music);
			holder.btnInfo = convertView.findViewById(R.id.btnInfo_item_local_music);
			holder.btnDelete = convertView.findViewById(R.id.btnDelete_item_local_music);
			holder.ivCheck = convertView.findViewById(R.id.ivCheck_item_local_music);
			convertView.setTag(holder);

		}else{
			holder= (ViewHolder) convertView.getTag();
		}
		cursor.moveToPosition(position);
		String artist = cursor.getString(cursor.getColumnIndex(LocalMusicColumns.ARTIST));
		String musicName = cursor.getString(cursor.getColumnIndex(LocalMusicColumns.MUSIC_NAME));;
		holder.tvTitle.setText(artist+" - "+musicName);

		int section = indexer.getSectionForPosition(position);
		int pos = indexer.getPositionForSection(section);
		if (pos==position) {
			holder.tvLetter.setVisibility(View.VISIBLE);
			String letter = cursor.getString(cursor.getColumnIndex(LocalMusicColumns.LETTER));
			holder.tvLetter.setText(letter);
			if ("其".equals(letter)) {
				letter = "#";
			}
		}else{
			holder.tvLetter.setVisibility(View.GONE);
		}

		holder.btnAddToQueue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int[] location = new int[2];
				v.getLocationInWindow(location);
				startX = location[0];
				startY = location[1]-Constant.heightStatusBar;
				a = (endY-startY)/(Math.pow(endX-offset, 2)-Math.pow(startX-offset, 2));
				b = startY-Math.pow(startX-offset, 2)*a;
	
				showFloatAddToQueue(startX,startY);
				
				cursor.moveToPosition(position);
				MusicQueue music=new MusicQueue();
				music.setAdd(true);
				music.setArtist(cursor.getString(cursor.getColumnIndex(LocalMusicColumns.ARTIST)));
				music.setDuration(cursor.getInt(cursor.getColumnIndex(LocalMusicColumns.DURATION)));
				music.setMp3Size(cursor.getInt(cursor.getColumnIndex(LocalMusicColumns.MP3_SIZE)));
				music.setName(cursor.getString(cursor.getColumnIndex(LocalMusicColumns.MUSIC_NAME)));
				music.setPath(cursor.getString(cursor.getColumnIndex(LocalMusicColumns.PATH)));
				music.setSource(Constant.TAG_QUEUE_LOCAL);
				music.setType(0);
				Intent intent=new Intent(MusicReceiver.ACTION_ADDTO_QUEUE);
				intent.putExtra(Constant.KEY_MUSIC, music);
				context.sendBroadcast(intent);
			}
		});

		if (cursor.getString(cursor.getColumnIndex(LocalMusicColumns.PATH)).equals(curPlayPath)) {
			holder.tipView.setVisibility(View.VISIBLE);
			switch (mPlayState) {
			case PLAY_STATE_PAUSE:
				holder.tipView.setBackgroundColor(
						context.getResources().getColor(R.color.tip_pause));
				break;
			case PLAY_STATE_PLAYING:
				holder.tipView.setBackgroundColor(
						context.getResources().getColor(R.color.tip_playing));
				break;
			}
			
		}else{
			holder.tipView.setVisibility(View.GONE);
		}
		return convertView;
	}
	private WindowManager mWindowManager;
	private LayoutParams mLayoutParams;
	private ImageView mImageView;
	private ValueAnimator mAnimator;
	private int endX,endY;
	private int startX,startY;
	private double a,b;
	private int offset=80;
	protected void showFloatAddToQueue(int startX,int startY) {
		if (mWindowManager==null) {
			mWindowManager=(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			mLayoutParams=new LayoutParams();
			mLayoutParams.width=LayoutParams.WRAP_CONTENT;
			mLayoutParams.height=LayoutParams.WRAP_CONTENT;
			mLayoutParams.x=startX;
			mLayoutParams.y=startY;
			mLayoutParams.gravity=Gravity.TOP|Gravity.LEFT;
			mLayoutParams.format = PixelFormat.TRANSLUCENT;
			mLayoutParams.flags=LayoutParams.FLAG_NOT_FOCUSABLE
					| LayoutParams.FLAG_NOT_TOUCHABLE;
			mImageView=new ImageView(context);
			mImageView.setImageResource(R.drawable.insert_play_note);
			mWindowManager.addView(mImageView, mLayoutParams);
			mAnimator=ValueAnimator.ofInt(startX,endX);
			mAnimator.setDuration(500);
		    mAnimator.addUpdateListener(updateListener);
		    
			mAnimator.addListener(addListener);
		    mAnimator.start();
		}else{
			mLayoutParams.y=startY;
			mWindowManager.updateViewLayout(mImageView, mLayoutParams);
			if (mAnimator.isRunning()) {
				mAnimator.cancel();
				isCancel=true;
				
			}
			mAnimator.start();
		}
	}
	private Boolean isCancel=false;
	private AnimatorListener addListener=new AnimatorListener() {
		
		@Override
		public void onAnimationStart(Animator animation) {
			
		}
		
		@Override
		public void onAnimationRepeat(Animator animation) {
			
		}
		
		@Override
		public void onAnimationEnd(Animator animation) {
			if (!isCancel) {
				Toast.makeText(context, "已添加至播放队列", Toast.LENGTH_SHORT).show();
				mImageView.setAlpha(0f);
			}
			isCancel=false;
		}
		
		@Override
		public void onAnimationCancel(Animator animation) {
			
		}
	};
	private AnimatorUpdateListener updateListener=new AnimatorUpdateListener() {
		
		@Override
		public void onAnimationUpdate(ValueAnimator animation) {
			int curX= (Integer) animation.getAnimatedValue();
			int curY=(int) (a*Math.pow(curX-offset, 2)+b);
			mLayoutParams.x=curX;
			mLayoutParams.y=curY;
			mImageView.setRotation(animation.getAnimatedFraction()*720);
			mImageView.setAlpha(1-animation.getAnimatedFraction()*0.5f);
			mWindowManager.updateViewLayout(mImageView, mLayoutParams);
			
		}
	};
	class ViewHolder{
		View tipView;
		TextView tvLetter;
		TextView tvTitle;
		View btnMore;
		View layoutMore;
		View btnAddToQueue;
		View btnSetring;
		View btnAddTo;
		View btnInfo;
		View btnDelete;
		View ivCheck;
	}
	private String curPlayPath;
	public static final int PLAY_STATE_PAUSE = 1;
	public static final int PLAY_STATE_PLAYING = 2;
	private int mPlayState;
	public void updataPath(String path,int mPlayState){
		this.mPlayState=mPlayState;
		this.curPlayPath=path;
		notifyDataSetChanged();
	}
}
