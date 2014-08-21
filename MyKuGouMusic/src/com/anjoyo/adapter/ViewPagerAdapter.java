package com.anjoyo.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {
	private List<View> views;



	public ViewPagerAdapter(List<View> views) {
		super();
		this.views = views;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0==arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		View view=views.get(position);
		container.removeView(view);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view=views.get(position);
		container.addView(view);
		return view;
	}

}
