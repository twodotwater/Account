package com.lzhn.utils.os;

import java.util.ArrayList;

import android.app.Application;
import android.support.v4.app.FragmentActivity;

public class CatchExcepApplication_v4 extends Application {

	ArrayList<FragmentActivity> list = new ArrayList<FragmentActivity>();

	public void init(Class<?> cls) {
		// 设置该CrashHandler为程序的默认处理器
		UnCEHandler_v4 catchExcep = new UnCEHandler_v4(this, cls);
		// 设置自定义的异常处理handler
		Thread.setDefaultUncaughtExceptionHandler(catchExcep);
	}

	/**
	 * FragmentActivity关闭时，删除FragmentActivity列表中的FragmentActivity对象
	 */
	public void removeActivity(FragmentActivity a) {
		list.remove(a);
	}

	/**
	 * 向Activity列表中添加Activity对象
	 */
	public void addActivity(FragmentActivity a) {
		list.add(a);
	}

	/**
	 * 关闭Activity列表中的所有Activity
	 */
	public void finishActivity() {
		for (FragmentActivity activity : list) {
			if (null != activity) {
				activity.finish();
			}
		}
		// 杀死该应用进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
