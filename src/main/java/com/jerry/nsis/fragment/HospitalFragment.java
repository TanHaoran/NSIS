package com.jerry.nsis.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.utils.L;

/**
 * Created by Jerry on 2016/4/11.
 */
public class HospitalFragment extends Fragment {


    private View mView;

    private TextView mName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_hospital, container, false);
        mName = (TextView) mView.findViewById(R.id.tv_hosname);
        return mView;
    }

    public void setHospitalName(String name) {
        mName.setText(name);
    }

}
