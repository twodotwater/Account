package com.zzha.util.os;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;

public class CatchExcepApplication extends Application {

	ArrayList<Activity> list = new ArrayList<Activity>();

	public void init(Class<?> cls) {
		// 设置该CrashHandler为程序的默认处理器
		UnCEHandler catchExcep = new UnCEHandler(this, cls);
		// 设置自定义的异常处理handler
		Thread.setDefaultUncaughtExceptionHandler(catchExcep);
	}

	/**
	 * Activity关闭时，删除Activity列表中的Activity对象
	 */
	public void removeActivity(Activity a) {
		list.remove(a);
	}

	/**
	 * 向Activity列表中添加Activity对象
	 */
	public void addActivity(Activity a) {
		list.add(a);
	}

	/**
	 * 关闭Activity列表中的所有Activity
	 */
	public void finishActivity() {
		for (Activity activity : list) {
			if (null != activity) {
				activity.finish();
			}
		}
		// 杀死该应用进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
