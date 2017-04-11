
package com.task.vasskob.firebase.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationManagerCompat;
import android.view.Menu;
import android.view.MenuItem;

import com.task.vasskob.firebase.Constants;
import com.task.vasskob.firebase.R;
import com.task.vasskob.firebase.SessionOptions;
import com.task.vasskob.firebase.database.FirebaseOperations;
import com.task.vasskob.firebase.event.ServiceIsRunningEvent;
import com.task.vasskob.firebase.service.AccelerometerService;
import com.task.vasskob.firebase.ui.fragment.SessionListFragment;
import com.task.vasskob.firebase.ui.fragment.TimePickerFragment;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity {

    public static final String SESSION_LIST_FRAGMENT = "sessionListFragment";
    public int interval = Constants.DEFAULT_INTERVAL;
    public int duration = Constants.DEFAULT_DURATION;
    private boolean isRunning = false;

    @Bind(R.id.fab_run_service)
    public FloatingActionButton fab;
    private SessionListFragment sessionListFragment;

    @OnClick(R.id.fab_run_service)
    public void onClick() {
        if (!isRunning) {
            Intent intent = new Intent(MainActivity.this, AccelerometerService.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.OPTIONS_KEY, new SessionOptions(getUid(), interval, duration));
            intent.putExtra(Constants.OPTIONS_KEY, bundle);
            startService(intent);
            fabIsOn();
        } else {
            MainActivity.this.stopService(new Intent(MainActivity.this,
                    AccelerometerService.class));
            fabIsOff();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            sessionListFragment = (SessionListFragment) getSupportFragmentManager().
                    getFragment(savedInstanceState, SESSION_LIST_FRAGMENT);
            // fragment is already added, no need to do this again
        } else {
            sessionListFragment = SessionListFragment.newInstance(getUid());

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, sessionListFragment).commit();
        }


        EventBus myEventBus = EventBus.getDefault();
        myEventBus.register(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        getSupportFragmentManager().putFragment(outState, SESSION_LIST_FRAGMENT, sessionListFragment);
    }

    private void fabIsOn() {
        fab.setSelected(true);
        isRunning = true;
    }

    private void fabIsOff() {
        fab.setSelected(false);
        isRunning = false;
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(getApplicationContext());
        notificationManager.cancelAll();
    }

    public void onEvent(ServiceIsRunningEvent event) {
        if (event.isRunning()) {
            fabIsOn();
        } else {
            fabIsOff();
        }
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
                TimePickerFragment dialogFragment = TimePickerFragment.newInstance(getUid(), interval, duration);
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
                duration = (int) TimeUnit.MINUTES.toSeconds(1);
                break;
            case R.id.duration_2m:
                duration = (int) TimeUnit.MINUTES.toSeconds(2);
                break;
            case R.id.duration_5m:
                duration = (int) TimeUnit.MINUTES.toSeconds(5);
                break;
            case R.id.duration_10m:
                duration = (int) TimeUnit.MINUTES.toSeconds(10);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        fabIsOff();
    }

}
