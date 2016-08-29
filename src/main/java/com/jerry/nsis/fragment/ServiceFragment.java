package com.jerry.nsis.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jerry.nsis.R;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;

/**
 * Created by Jerry on 2016/4/11.
 */
public class ServiceFragment extends Fragment {


    private View mView;

    private EditText mService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_service, container, false);
        mService = (EditText)mView.findViewById(R.id.et_service);
        return mView;
    }

    /**
     * 获取编辑框的服务地址
     * @return
     */
    public String getServiceIp() {
        return mService.getText().toString();
    }


}
