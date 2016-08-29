package com.jerry.nsis.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jerry.nsis.R;
import com.jerry.nsis.common.LoginInfo;
import com.jerry.nsis.common.ServiceConstant;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.activity_setting)
public class SettingActivity extends Activity {

    @ViewInject(R.id.btn)
    private Button mSave;
    @ViewInject(R.id.et_ip)
    private EditText mIP;
    @ViewInject(R.id.et_office)
    private EditText mOffice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);
        loadSetting();
    }

    @OnClick(R.id.btn)
    public void saveSetting(View v) {
        SharedPreferences sp = getSharedPreferences(RegisterActivity.SETTING_NAME, MODE_PRIVATE);
        String ip = mIP.getText().toString();
        String officeId = mOffice.getText().toString();
        LoginInfo.OFFICE_ID = officeId;
        ServiceConstant.SERVICE_IP = ip;
        sp.edit().putString("ip", ip).commit();
        sp.edit().putString("office_id", officeId).commit();
        finish();
    }

    public void loadSetting() {
        SharedPreferences sp = getSharedPreferences(RegisterActivity.SETTING_NAME, MODE_PRIVATE);
        String ip = sp.getString("ip", "http://192.168.0.100:");
        String officeId = sp.getString("office_id", "0000000128");
        LoginInfo.OFFICE_ID = officeId;
        ServiceConstant.SERVICE_IP = ip;
        mIP.setText(ip);
        mOffice.setText(officeId);
    }

}
