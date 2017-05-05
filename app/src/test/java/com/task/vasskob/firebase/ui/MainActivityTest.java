package com.task.vasskob.firebase.ui;

import android.app.Activity;
import android.view.Menu;

import com.task.vasskob.firebase.BuildConfig;
import com.task.vasskob.firebase.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, packageName="com.task.vasskob.firebaseAccelerometer")
public class MainActivityTest {
    @Test
    public void onCreate_shouldInflateTheMenu() throws Exception {
        Activity activity = Robolectric.setupActivity(MainActivity.class);

        final Menu menu = shadowOf(activity).getOptionsMenu();
        assertThat(menu.findItem(R.id.action_interval).getTitle()).isEqualTo("Set interval");
        assertThat(menu.findItem(R.id.start_recording_time).getTitle()).isEqualTo("Start in time");
        assertThat(menu.findItem(R.id.recording_duration).getTitle()).isEqualTo("Set recording duration");
        assertThat(menu.findItem(R.id.clean_db).getTitle()).isEqualTo("Clean DB");
        assertThat(menu.findItem(R.id.action_logout).getTitle()).isEqualTo("Log out");
        assertThat(menu.findItem(R.id.interval_1s).getTitle()).isEqualTo("1s");
        assertThat(menu.findItem(R.id.interval_2s).getTitle()).isEqualTo("2s");
        assertThat(menu.findItem(R.id.interval_5s).getTitle()).isEqualTo("5s");
        assertThat(menu.findItem(R.id.interval_10s).getTitle()).isEqualTo("10s");
        assertThat(menu.findItem(R.id.duration_1m).getTitle()).isEqualTo("1m");
        assertThat(menu.findItem(R.id.duration_2m).getTitle()).isEqualTo("2m");
        assertThat(menu.findItem(R.id.duration_5m).getTitle()).isEqualTo("5m");
        assertThat(menu.findItem(R.id.duration_10m).getTitle()).isEqualTo("10m");

    }

}