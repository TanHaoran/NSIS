package com.jerry.nsis.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;

import com.lidroid.xutils.ViewUtils;

/**
 * @author Jerry Tan
 * @version 1.0
 * @description 管理NSIS工具类
 * @date 2015年8月25日 上午10:09:27
 * @Company Buzzlysoft
 * @email thrforever@126.com
 * @remark
 */
public class NsisApplication extends Application {

    private List<Activity> activityList = new ArrayList<Activity>();
    private static NsisApplication instance;

    private NsisApplication() {
    }

    /**
     * 单例模式中获取唯一的MyApplication实例
     *
     * @return
     */
    public static NsisApplication getInstance() {
        if (null == instance) {
            instance = new NsisApplication();
        }
        return instance;
    }

    /**
     * 添加Activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
        // 设置强制横屏
        if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        // 设置没有标题栏
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 设置全屏
        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 添加ViewUtil的设置
        ViewUtils.inject(activity);
    }

    /**
     * 移除一个Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 遍历所有Activity
     */
    public void finishAllActivity() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
    }

    /**
     * 遍历所有Activity并finish
     */
    public void finishApplication() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
        System.exit(0);
    }
}
