package com.zzha.utils.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> fragments;
	private int mCount = 0;

	public void setFragments(ArrayList<Fragment> fragments) {
		this.fragments = fragments;
	}

	public ViewPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public android.support.v4.app.Fragment getItem(int position) {
		if (fragments != null && fragments.size() > 0) {
			return fragments.get(position % fragments.size());
		}
		return null;
	}

	@Override
	public int getCount() {
		if (fragments != null && fragments.size() > 0) {
			return fragments.size();
		}
		return 0;
	}

}
