
package com.task.vasskob.firebase.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.task.vasskob.firebase.Constants;
import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.database.FirebaseOperations;
import com.task.vasskob.firebase.ui.fragment.SessionListFragment;
import com.task.vasskob.firebase.ui.fragment.TimePickerFragment;
import com.task.vasskob.firebase.service.AccelerometerService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    public int interval = Constants.DEFAULT_INTERVAL;
    public int duration = Constants.DEFAULT_DURATION;
    public String startTime;
    private BroadcastReceiver mReceiver;

    @Bind(R.id.fab_run_service)
    public FloatingActionButton fab;

    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        SessionListFragment sessionListFragment = new SessionListFragment();
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
                    fabIsOn();
                } else {
                    MainActivity.this.stopService(new Intent(MainActivity.this,
                            AccelerometerService.class));
                    fabIsOff();

                }
            }
        });
        registerBroadcast();
    }

    private void fabIsOn() {
        setFabColorAndIcon(R.color.colorRunService, R.drawable.ic_stop);
        isRunning = true;
    }

    private void fabIsOff() {
        setFabColorAndIcon(R.color.colorAccent, R.drawable.ic_play);
        isRunning = false;
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());
        notificationManager.cancelAll();
    }

    public void setFabColorAndIcon(int fabColor, int fabIcon) {
        fab.setBackgroundTintList(ContextCompat.getColorStateList(MainActivity.this, fabColor));
        fab.setImageResource(fabIcon);

    }

    private void registerBroadcast() {
        IntentFilter intentFilter = new IntentFilter(
                Constants.INTENT_ACTION_MAIN);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getExtras().getBoolean(Constants.SERVISE_IS_RUN)) {
                    fabIsOn();
                } else {
                    fabIsOff();
                }
            }
        };
        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.interval_1s:
            case R.id.interval_2s:
            case R.id.interval_5s:
            case R.id.interval_10s:
                setInterval(itemId);
                break;
            case R.id.duration_1m:
            case R.id.duration_2m:
            case R.id.duration_5m:
            case R.id.duration_10m:
                setDuration(itemId);
                break;
            case R.id.start_recording_time:
                TimePickerFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getFragmentManager(), getResources().getString(R.string.choose_time));
                break;
            case R.id.clean_db:
                FirebaseOperations.cleanDb(getUid());
            case R.id.action_logout:
                FirebaseOperations.logout();
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                break;
            default:
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setInterval(int id) {
        switch (id) {
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
        }
    }

    private void setDuration(int id) {
        switch (id) {
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
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(this.mReceiver);
        fabIsOff();
    }
}
