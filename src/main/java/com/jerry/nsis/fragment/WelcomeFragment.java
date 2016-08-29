package com.jerry.nsis.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jerry.nsis.R;

/**
 * Created by Jerry on 2016/4/11.
 */
public class WelcomeFragment extends Fragment {

    private ViewPager mViewPager;


    private View mView;

    public WelcomeFragment() {
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_welcome, container, false);

        return mView;
    }
}