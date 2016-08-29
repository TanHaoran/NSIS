package com.jerry.nsis.utils;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * @description ActivityUtil.java
 * @date 2015年9月14日 下午2:39:53
 * @version 1.0
 * @Company Buzzlysoft
 * @author Jerry Tan
 * @email thrforever@126.com
 * @remark
 */
public class ActivityUtil {

	private ActivityUtil() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * 获取当前正在运行的Activity类名
	 * 
	 * @return
	 */
	public static String getCurrentActivity(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTasks = manager.getRunningTasks(1);
		RunningTaskInfo info = runningTasks.get(0);
		ComponentName component = info.topActivity;
		L.i(component.getClassName());
		L.i(component.getShortClassName());
		return component.getShortClassName();
	}

	public static void makeActivity2Dialog(Activity activity) {
		WindowManager manager = activity.getWindowManager();
		Display display = manager.getDefaultDisplay(); // 为获取屏幕宽、高
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes(); // 获取对话框当前的参值
		lp.height = (int) (display.getHeight() * 0.75); // 高度设置为屏幕的1.0
		lp.width = (int) (display.getWidth() * 0.8); // 宽度设置为屏幕的0.8
		lp.alpha = 1.0f; // 设置本身透明度
		lp.dimAmount = 0.6f; // 设置黑暗度
		activity.getWindow().setAttributes(lp); // 设置生效
		activity.getWindow().setGravity(Gravity.CENTER); // 设置靠右对齐
	}

}
