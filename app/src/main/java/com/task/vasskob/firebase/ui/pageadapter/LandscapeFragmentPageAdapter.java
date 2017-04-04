package com.task.vasskob.firebase.ui.pageadapter;

import android.content.Context;
import android.support.v4.app.FragmentManager;

public class LandscapeFragmentPageAdapter extends BaseFragmentPageAdapter {
    public LandscapeFragmentPageAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public float getPageWidth(int position) {
        return 0.5f; // split view into 2 equal parts
    }
}
