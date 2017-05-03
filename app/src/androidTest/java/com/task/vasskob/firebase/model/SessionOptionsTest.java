package com.task.vasskob.firebase.model;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class SessionOptionsTest {

    @Test
    public void testSessionOptionInParcel(){
        SessionOptions options=new SessionOptions("10",10,10);
        Parcel parcel=Parcel.obtain();
        options.writeToParcel(parcel, options.describeContents());
        parcel.setDataPosition(0);
        SessionOptions createdFromParcel=SessionOptions.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getUid(), is("10"));
        assertThat(createdFromParcel.getInterval(), is(10));
        assertThat(createdFromParcel.getDuration(), is(10));
    }
}