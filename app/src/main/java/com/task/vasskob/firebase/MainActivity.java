
package com.task.vasskob.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.task.vasskob.firebase.fragment.ChartFragment;
import com.task.vasskob.firebase.fragment.ListFragment;
import com.task.vasskob.firebase.fragment.ResentDataFragment;
import com.task.vasskob.firebase.fragment.TimePickerFragment;
import com.task.vasskob.firebase.service.AccelerometerService;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private int interval;
    private int duration;
    public String startTime;

    // private static final String TAG = "MainActivity";

    @Bind(R.id.container)
    ViewPager mViewPager;

    @Bind(R.id.tabs)
    TabLayout tabLayout;

    @Bind(R.id.fab_run_service)
    FloatingActionButton fab;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Create the adapter that will return a fragment for each section
        FragmentPagerAdapter mPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            private final Fragment[] mFragments = new Fragment[]{
                    new ResentDataFragment(),
                    new ChartFragment(),
            };
            private final String[] mFragmentNames = new String[]{
                    getString(R.string.fragment_1),
                    getString(R.string.fragment_2),
            };

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
        };
        // Set up the ViewPager with the sections adapter.

        mViewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        // Button launches NewPostActivity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    Intent intent = new Intent(MainActivity.this, AccelerometerService.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.USER_ID, getUid());
                    bundle.putInt(Constants.INTERVAL_KEY, interval);
                    bundle.putString(Constants.START_TIME_KEY, startTime);
                    bundle.putInt(Constants.DURATION_KEY, duration);
                    intent.putExtra(Constants.OPTIONS_KEY, bundle);
                    startService(intent);
                    fab.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorRunService));
                    fab.setImageResource(R.drawable.ic_stop);
                    isRunning = true;
                } else {
                    MainActivity.this.stopService(new Intent(MainActivity.this,
                            AccelerometerService.class));
                    fab.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, R.color.colorAccent));
                    fab.setImageResource(R.drawable.ic_play);
                    isRunning = false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.interval_1s:
                interval = 1;
                break;
            case R.id.interval_2s:
                interval = 2;
                break;
            case R.id.interval_5s:
                interval = 5;
                break;
            case R.id.interval_10s:
                interval = 10;
                break;
            case R.id.duration_1m:
                duration = 1;
                break;
            case R.id.duration_2m:
                duration = 2;
                break;
            case R.id.duration_5m:
                duration = 5;
                break;
            case R.id.duration_10m:
                duration = 10;
                break;
            case R.id.start_recording_time:
                TimePickerFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getFragmentManager(), getResources().getString(R.string.choose_time));
                break;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                break;
            default:
                return false;
        }
        Log.d("NDA", " interval = " + interval + " duration = " + duration + " start Time = " + startTime);
        return super.onOptionsItemSelected(item);
    }

}
