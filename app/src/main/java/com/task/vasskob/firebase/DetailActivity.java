package com.task.vasskob.firebase;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.task.vasskob.firebase.fragment.ChartFragment;
import com.task.vasskob.firebase.model.Coordinates;
import com.task.vasskob.firebase.pageadapter.BaseFragmentPageAdapter;
import com.task.vasskob.firebase.pageadapter.LandscapeFragmentPageAdapter;
import com.task.vasskob.firebase.pageadapter.PortraitFragmentPageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity {

    public String sessionId;

    @Bind(R.id.container)
    ViewPager mViewPager;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

    List<Coordinates> coords = new ArrayList<Coordinates>();
    private BaseFragmentPageAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            mPagerAdapter = new PortraitFragmentPageAdapter(getSupportFragmentManager(), this);
            tabLayout.setVisibility(View.VISIBLE);
        } else {
            mPagerAdapter = new LandscapeFragmentPageAdapter(getSupportFragmentManager(), this);
            tabLayout.setVisibility(View.GONE);
        }

        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        Intent intent = getIntent();
        sessionId = intent.getExtras().getString("sessionId");
        coords.clear();
    }

    public void addCoordinates(Coordinates coordinates) {
        coords.add(coordinates);
        ChartFragment chartFragment = (ChartFragment) mPagerAdapter.getChartFragment();
        chartFragment.onDrawChart(coords);
    }
}

