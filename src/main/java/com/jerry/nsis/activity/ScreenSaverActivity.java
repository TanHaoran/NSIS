package com.jerry.nsis.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.nsis.R;
import com.jerry.nsis.utils.DateUtil;
import com.jerry.nsis.utils.L;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Random;

@ContentView(R.layout.activity_screen_saver)
public class ScreenSaverActivity extends Activity {


    // 右上角更新时间的常量
    private static final int UPDATE_TIME = 0;

    @ViewInject(R.id.rl_layout)
    private RelativeLayout mLayout;
    @ViewInject(R.id.tv_date)
    private TextView mDate;
    @ViewInject(R.id.tv_time)
    private TextView mTime;

    // 切换背景图案的市场（秒）
    private static final int CHANGE_BG_TIME = 30;
    private int count = 0;

    private int bgIndex = 0;

    private int[] bgResources = {R.drawable.screensaver02, R.drawable.screensaver03, R.drawable.screensaver04,
            R.drawable.screensaver05, R.drawable.screensaver06, R.drawable.screensaver07};


    // 更新左侧界面信息的常量
    private static final int UPDATE_DATA = 100;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                // 更新时间
                case UPDATE_TIME:
                    count++;
                    mTime.setText(DateUtil.getTime());
                    mDate.setText(DateUtil.getYMD());
                    // 每过CHANGE_BG_TIME秒，切换一次背景图
                    if (count == CHANGE_BG_TIME) {
                        bgIndex++;
                        if (bgIndex > bgResources.length - 1) {
                            bgIndex = 0;
                        }
                        mLayout.setBackgroundResource(bgResources[bgIndex]);
                        count = 0;
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NsisApplication.getInstance().addActivity(this);
        startTimeThread();
    }

    /**
     * 开始时间显示组件
     */
    private void startTimeThread() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            try {
                                Thread.sleep(1000);
                                Message msg = mHandler.obtainMessage();
                                msg.what = UPDATE_TIME;
                                msg.sendToTarget();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return super.onTouchEvent(event);
    }
}
