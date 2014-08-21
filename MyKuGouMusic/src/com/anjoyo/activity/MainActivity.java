package com.anjoyo.activity;

import java.util.ArrayList;
import java.util.List;

import com.anjoyo.adapter.ViewPagerAdapter;
import com.anjoyo.fragment.LocalMusicFragment;
import com.anjoyo.fragment.MainFragment;
import com.anjoyo.fragment.MoreFragment;
import com.anjoyo.mykugoumusic.R;
import com.anjoyo.view.MyViewPager;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;

public class MainActivity extends Activity {
	private MyViewPager mViewPager;
	private List<View> mViews;
	private ViewPagerAdapter mPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViewPager();

	}

	private void initViewPager() {
		mViewPager=(MyViewPager) findViewById(R.id.viewPager_main);

		FrameLayout containerMore=new FrameLayout(this);
		containerMore.setId(1);

		FrameLayout containerMain=new FrameLayout(this);
		containerMain.setId(2);

		mViews=new ArrayList<View>();
		mViews.add(containerMore);
		mViews.add(containerMain);

		mPagerAdapter = new ViewPagerAdapter(mViews);
		mViewPager.setAdapter(mPagerAdapter);

		mViewPager.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
	}

	private OnGlobalLayoutListener onGlobalLayoutListener=new OnGlobalLayoutListener() {
		private Boolean isFirst=true;

		@Override
		public void onGlobalLayout() {
			if (isFirst) {
				FragmentManager manager = getFragmentManager();
				FragmentTransaction ft = manager.beginTransaction();
				ft.add(1, new MoreFragment(), "MoreFragment");
				ft.add(2, new MainFragment(), "MainFragment");
				ft.commit();
				isFirst=false;
			}


		}
	};

	public void gotoLocalMusicMain() {
		if (mViews.size()==2) {
			FrameLayout containerAdd=new FrameLayout(this);
			containerAdd.setId(3);
			mViews.add(containerAdd);
			mPagerAdapter.notifyDataSetChanged();
			
			FragmentManager manager = getFragmentManager();
			FragmentTransaction ft = manager.beginTransaction();
			System.out.println("wo zhidao  ........");
            System.out.println("大小"+mViews.size());
			ft.add(3, new LocalMusicFragment(), "LocalMusicFragment");
			ft.commit();
			mViewPager.setCurrentItem(2);

		}else{
			mViewPager.setCurrentItem(2);
		}


	}
	@Override
	public void onBackPressed() {
		if (getFragmentManager().popBackStackImmediate()) {
			return;
			
		}
		if (mViewPager.getCurrentItem()!=1) {
			mViewPager.setCurrentItem(1);

		}else{
			Intent intent=new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
		}

	}
}
