package com.jerry.nsis.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jerry.nsis.R;

/**
 * Created by Jerry on 2016/4/11.
 */
public class WeChatFragment extends Fragment {


    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_wechat, container, false);
        return mView;
    }
}
