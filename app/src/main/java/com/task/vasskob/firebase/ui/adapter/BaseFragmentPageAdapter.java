package com.task.vasskob.firebase.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.ui.fragment.ChartFragment;
import com.task.vasskob.firebase.ui.fragment.CoordinateListFragment;

abstract class BaseFragmentPageAdapter extends FragmentPagerAdapter {

    private final Fragment[] mFragments;
    private final String[] mFragmentNames;

    BaseFragmentPageAdapter(FragmentManager fm, Context context, String uId, String sId) {
        super(fm);
        mFragments = new Fragment[]{
                CoordinateListFragment.newInstance(uId, sId),
                ChartFragment.newInstance(uId,sId),
        };
        mFragmentNames = new String[]{
                context.getString(R.string.fragment_1),
                context.getString(R.string.fragment_2),
        };

    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentNames[position];
    }

}
