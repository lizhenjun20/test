package com.anjoyo.fragment;

import com.anjoyo.adapter.MyMusicLocalAdapter;
import com.anjoyo.app.MusicApplication;
import com.anjoyo.bean.MusicQueue;
import com.anjoyo.db.MusicDbHelper;
import com.anjoyo.db.MusicDbHelper.LocalMusicColumns;
import com.anjoyo.mykugoumusic.R;
import com.anjoyo.service.MusicService.MusicReceiver;
import com.anjoyo.tools.Constant;
import com.anjoyo.view.AlphListview;
import com.anjoyo.view.LetterView;
import com.anjoyo.view.LetterView.OnLetterChangeListener;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AlphabetIndexer;
import android.widget.ImageView;

public class LocalMusicFragment extends FragmentLocalBase{
	private AlphListview mListview;
	private LetterView mLetterView;
	private View mContentView;
	private MusicDbHelper dbHelper;
	private AlphabetIndexer indexer;
	private String mAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ其";
	private View mLoadingView;

	/**悬浮窗口相关的***/
	private WindowManager mWindowManager;
	private LayoutParams mParams;
	private ImageView mImageView;

	private MusicApplication app;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContentView=inflater.inflate(R.layout.fragemnt_localmusic, null);
		System.out.println("sjhsjfsbsjfs");
		return mContentView;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		registerReceiver();
		setupBack();
		setupMenu();
		setupTitle("本地音乐");
		dbHelper=new MusicDbHelper(getActivity());
		initViews();
		handler.sendEmptyMessageDelayed(MSG_SHOW_LIST, 1500);

		mWindowManager=(WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		app=(MusicApplication) getActivity().getApplication();
	}

	private void initViews() {
		mListview=(AlphListview) mContentView.findViewById(R.id.alphlistview);
		mLetterView = (LetterView) mContentView.findViewById(R.id.letterView_fragment_local_music);
		mLoadingView=mContentView.findViewById(R.id.loadingView);		
		mLetterView.setLetterChangeListener(letterChangeListener);
		mListview.setOnItemClickListener(onItemClickListener);


	}
	private OnItemClickListener onItemClickListener=new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			cursor.moveToPosition(position);
			if (cursor.getString(cursor.getColumnIndex(LocalMusicColumns.PATH)).equals(app.getMusicService().getCurPlayPath())) {
				getActivity().sendBroadcast(new Intent(MusicReceiver.ACTION_PLAY_OR_PAUSE));

			}else{
				playPosition=position;
				View btnAddToQueue = view.findViewById(R.id.btnAddToQueue_item_local_music);
				int[] positions=new int[2];
				btnAddToQueue.getLocationInWindow(positions);
				showFloatMusic(positions[0],positions[1]);
				handler.sendMessageDelayed(handler.obtainMessage(MSG_MOVE_FLOAT, positions[1], Constant.screenHeight-Constant.heightStatusBar-50), 300);


				
			}
		}
	};
	private int playPosition;
	private OnScrollListener onScrollListener=new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			int section = indexer.getSectionForPosition(firstVisibleItem);	
			mLetterView.setSelectedIndex(section);
		}
	};
	private OnLetterChangeListener letterChangeListener=new OnLetterChangeListener() {

		@Override
		public void onLetterChange(int selectedIndex) {
			mListview.setSelection(indexer.getPositionForSection(selectedIndex));

		}
	};
	private void setListAdapter(){
		cursor = dbHelper.getWritableDatabase().query(MusicDbHelper.LOCAL_MUSIC_TABLE, null, null, null, null, null, LocalMusicColumns.LETTER);		
		indexer=new AlphabetIndexer(cursor, cursor.getColumnIndex(LocalMusicColumns.LETTER), mAlphabet);
		mListAdapter = new MyMusicLocalAdapter(getActivity(), cursor, indexer);
		mListview.setAdapter(mListAdapter);
		mListview.setOnScrollListener(onScrollListener);

	}
	public static final int MSG_SHOW_LIST = 1;
	public static final int MSG_MOVE_FLOAT=2;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_SHOW_LIST:
				mLoadingView.setVisibility(View.GONE);
				mLetterView.setVisibility(View.VISIBLE);
				setListAdapter();
				break;

			case MSG_MOVE_FLOAT:
				startAnimFloat(msg.arg1, msg.arg2);
				break;
			}
		};

	};

	private void showFloatMusic(int x, int y){
		if (mParams==null) {
			mParams=new LayoutParams();
			mParams.width=LayoutParams.WRAP_CONTENT;
			mParams.height=LayoutParams.WRAP_CONTENT;
			mParams.x=x;
			mParams.y=y;
			mParams.gravity=Gravity.TOP|Gravity.LEFT;
			mParams.format = PixelFormat.TRANSLUCENT;
			mParams.flags=LayoutParams.FLAG_NOT_FOCUSABLE
					| LayoutParams.FLAG_NOT_TOUCHABLE;


			mImageView=new ImageView(getActivity());
			mImageView.setImageResource(R.drawable.music_note);
			mWindowManager.addView(mImageView, mParams);


		}else{
			mParams.y=y;
			if (mAnimator.isRunning()) {
				isCancel=true;
				mAnimator.cancel();
			}
			mImageView.setAlpha(1f);
			mWindowManager.updateViewLayout(mImageView, mParams);
		}

	}
	private ValueAnimator mAnimator;
	private Cursor cursor;
	private boolean isCancel;
	private MyMusicLocalAdapter mListAdapter;
	private void startAnimFloat(final int startY,final int endY){
		mAnimator=ValueAnimator.ofInt(startY,endY);
		mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
		mAnimator.setDuration(900);
		mAnimator.start();
		mAnimator.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int curY = (Integer) animation.getAnimatedValue();
				mParams.y=curY;
				mWindowManager.updateViewLayout(mImageView, mParams);
				mImageView.setAlpha(1f - (mParams.y-startY)*1f/(endY-startY));

			}
		});
		mAnimator.addListener(new AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				if (!isCancel) {
					if (!Constant.TAG_QUEUE_LOCAL.equals(app.getMusicService().getCurQueueTag())) {
						app.getMusicService().inintQueueLocalMusic();
					}
					Intent intent=new Intent(MusicReceiver.ACTION_LOCAL_LISTE);
					intent.putExtra(Constant.KEY_POSITION, playPosition);
					getActivity().sendBroadcast(intent);
				}
				isCancel=false;
				
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub

			}
		});
	}
	@Override
	public void onPause(MusicQueue music) {
		mListAdapter.updataPath(music.getPath(),MyMusicLocalAdapter.PLAY_STATE_PAUSE);
	}
	@Override
	public void onPlay(MusicQueue music) {
		mListAdapter.updataPath(music.getPath(),MyMusicLocalAdapter.PLAY_STATE_PLAYING);
	}
}
