package com.jerry.nsis.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.jerry.nsis.R;
import com.jerry.nsis.common.ServiceConstant;
import com.jerry.nsis.utils.HttpGetUtil;
import com.jerry.nsis.utils.JsonUtil;
import com.jerry.nsis.utils.L;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends Activity {

    private String serialNumber;

    private String hos;
    private String office;
    private String serial;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);
        loadDeviceInfo();
        loadImportantData();
        checkRegister();
    }

    private void checkRegister() {
        if (hos != null && office != null && serial != null && code != null) {
            checkCodeCorrect(code, serialNumber);
        } else {
            Intent intent = new Intent();
            intent.setClass(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }



    /**
     * 读取本地是否保存了重要的信息
     */
    private void loadImportantData() {
        SharedPreferences sp = getSharedPreferences(RegisterActivity.SETTING_NAME, MODE_PRIVATE);
        ServiceConstant.SERVICE_IP = sp.getString("ip", "http://192.168.0.100");
        hos = sp.getString("hospital", null);
        office = sp.getString("office_name", null);
        serial = sp.getString("serial", null);
        code = sp.getString("code", null);
    }

    /**
     * 获取设备唯一序列号
     *
     * @return
     */
    private void loadDeviceInfo() {
        serialNumber = android.os.Build.SERIAL;
    }

    /**
     * 检测激活码是否正确
     */
    private void checkCodeCorrect(final String code, String serialNumbe) {
        String url = ServiceConstant.SERVICE_IP + ServiceConstant.SERVICE +
                ServiceConstant.VERITY_CHECK_CODE + "?serialnumber=" +
                code + "&harddiskinfo=" + serialNumber;
        HttpGetUtil getUtil = new HttpGetUtil() {
            @Override
            public void success(String json) {
                String result = JsonUtil.getRegisterInfoJson(json);
                L.i("返回值：" + result);
                Intent intent = new Intent();
                if (result != null && result.equals("True")) {
                    intent.setClass(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    intent.setClass(WelcomeActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }


        };
        getUtil.doGet(this, url, false, "检测激活码");
    }


}
