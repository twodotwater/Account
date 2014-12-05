package com.lzhn.utils.os;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

/**
 * 抽象的Activity基类，声明常用方法，实现点击接口
 * 
 * @author zzha
 * 
 */
public abstract class BaseActivity extends Activity implements OnClickListener {
	private static final String TAG = "BaseActivity";
	private CatchExcepApplication application;
	private boolean isFullScreen;

	/**
	 * 整理收集当前Activity
	 */
	private void collectCurrentActivity() {
		application = (CatchExcepApplication) getApplication();
		application.addActivity(this);
	}

	/**
	 * 
	 * @param cls
	 *            重启后首次加载的activity ~ RTTI
	 */
	public void initUnCatchExceptionHandler(Class cls) {
		application.init(cls);
	}

	/**
	 * 指定页面的布局id
	 */
	public abstract void setContentViewLayout();

	/**
	 * 初始化组件
	 */
	public abstract void initComponent();

	/**
	 * 为组件添加监听器
	 */
	public abstract void setListeners();

	/**
	 * 组件点击事件
	 * 
	 * @param v
	 */
	public abstract void onClickComponent(View v);

	/**
	 * 初始设置是否全屏：默认不全屏
	 */
	public void initIsFullScreen() {
		setIsFullScreen(false);
	}

	public void setIsFullScreen(boolean isFullScreen) {
		this.isFullScreen = isFullScreen;
	}

	public boolean getIsFullScreen() {
		return isFullScreen;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "--> onCreate");
		super.onCreate(savedInstanceState);
		initIsFullScreen();
		if (isFullScreen) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		setContentViewLayout();
		collectCurrentActivity();
		initSomeWork();
		initComponent();
		setListeners();
		doSomeWork();
	}

	/**
	 * 预留方法：在onCreate()方法中完成一些可选工作--组件初始化之前
	 */
	public void initSomeWork() {
		return;
	}

	/**
	 * 预留方法：在onCreate()方法中完成一些可选工作--组件初始化之后
	 */
	public void doSomeWork() {
		return;
	}

	@Override
	public void onClick(View v) {
		onClickComponent(v);
	}

	/**
	 * 初始化组件
	 * 
	 * @param view
	 * @param resId
	 */
	@SuppressWarnings("unchecked")
	public <T> void initViewById(T view, int resId) {
		view = (T) findViewById(resId);
	}

	/**
	 * 横屏显示
	 */
	public void forceLandscape() {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}

	@Override
	protected void onResume() {
		Log.i(TAG, "--> onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.i(TAG, "--> onPause");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		Log.i(TAG, "--> onRestart");
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		Log.i(TAG, "--> onDestroy");
		super.onDestroy();
	}
}
