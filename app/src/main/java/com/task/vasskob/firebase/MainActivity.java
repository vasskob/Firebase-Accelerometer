
package com.task.vasskob.firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.task.vasskob.firebase.fragment.SessionListFragment;
import com.task.vasskob.firebase.fragment.TimePickerFragment;
import com.task.vasskob.firebase.service.AccelerometerService;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    public int interval = Constants.DEFAULT_INTERVAL;
    public int duration = Constants.DEFAULT_DURATION;
    public String startTime;
    private BroadcastReceiver mReceiver;

// private static final String TAG = "MainActivity";


    @Bind(R.id.fab_run_service)
    public FloatingActionButton fab;
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SessionListFragment sessionListFragment= new SessionListFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, sessionListFragment).commit();

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
                    setFabColorAndIcon(R.color.colorRunService, R.drawable.ic_stop);
                    isRunning = true;
                } else {
                    MainActivity.this.stopService(new Intent(MainActivity.this,
                            AccelerometerService.class));
                    setFabColorAndIcon(R.color.colorAccent, R.drawable.ic_play);
                    isRunning = false;
                }
            }
        });
    }

    public void setFabColorAndIcon(int fabColor, int fabIcon) {
        fab.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, fabColor));
        fab.setImageResource(fabIcon);
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
                duration = Constants.MIN_TO_SEC;
                break;
            case R.id.duration_2m:
                duration = 2 * Constants.MIN_TO_SEC;
                break;
            case R.id.duration_5m:
                duration = 5 * Constants.MIN_TO_SEC;
                break;
            case R.id.duration_10m:
                duration = 10 * Constants.MIN_TO_SEC;
                break;
            case R.id.start_recording_time:
                TimePickerFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getFragmentManager(), getResources().getString(R.string.choose_time));
                break;
            case R.id.clean_db:
                FirebaseDatabase.getInstance().getReference().getRoot().child("users-sessions-coordinates").child(getUid()).removeValue();
                FirebaseDatabase.getInstance().getReference().getRoot().child("sessions").child(getUid()).removeValue();
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

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(
                "android.intent.action.MAIN");

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                    setFabColorAndIcon(R.color.colorAccent, R.drawable.ic_play);
                    isRunning = false;

            }
        };
        this.registerReceiver(mReceiver, intentFilter);
        Log.d("mReceiver", "MainActivity onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(this.mReceiver);
    }
}
