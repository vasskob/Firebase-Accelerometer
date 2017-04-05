package com.task.vasskob.firebase;

import android.os.Parcel;
import android.os.Parcelable;

public class SessionOptions implements Parcelable {
    private String uid;
    private int interval;
    private int duration;

    public String getUid() {
        return uid;
    }

    public int getInterval() {
        return interval;
    }

    public int getDuration() {
        return duration;
    }

    public SessionOptions(String uid, int interval, int duration) {
        this.uid = uid;
        this.interval = interval;
        this.duration = duration;
    }

    private SessionOptions(Parcel in) {
        uid = in.readString();
        interval = in.readInt();
        duration = in.readInt();
    }

    public static final Creator<SessionOptions> CREATOR = new Creator<SessionOptions>() {
        @Override
        public SessionOptions createFromParcel(Parcel in) {
            return new SessionOptions(in);
        }

        @Override
        public SessionOptions[] newArray(int size) {
            return new SessionOptions[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeInt(this.interval);
        dest.writeInt(this.duration);

    }
}
