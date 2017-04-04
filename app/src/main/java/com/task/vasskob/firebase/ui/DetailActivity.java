package com.task.vasskob.firebase.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.ui.pageadapter.LandscapeFragmentPageAdapter;
import com.task.vasskob.firebase.ui.pageadapter.PortraitFragmentPageAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity {

    public static final String SESSION_ID = "sessionId";
    public String sessionId;

    @Bind(R.id.container)
    ViewPager mViewPager;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        int orientation = this.getResources().getConfiguration().orientation;
        FragmentPagerAdapter mPagerAdapter;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mPagerAdapter = new PortraitFragmentPageAdapter(getSupportFragmentManager(), this);
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            mPagerAdapter = new LandscapeFragmentPageAdapter(getSupportFragmentManager(), this);
            tabLayout.setVisibility(View.GONE);
        }

        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        Intent intent=getIntent();
        sessionId=intent.getExtras().getString(SESSION_ID);
    }
}

