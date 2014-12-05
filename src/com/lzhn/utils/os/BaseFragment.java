package com.lzhn.utils.os;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * 自定义fragment基类。--实现点击接口
 * 
 * @author zzha
 * 
 */
public class BaseFragment extends Fragment implements OnClickListener {

	private int resource;
	private Context context;

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.context = getActivity();
		onGetArguments(getArguments());
	}

	/**
	 * 获取传递的参数值
	 * 
	 * @param arguments
	 */
	public void onGetArguments(Bundle arguments) {
		return;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflateLayout(inflater, container, false);
		initComponent(view);
		setListeners();
		doSomeWork();
		return view;
	}

	/**
	 * 为组件添加监听器
	 */
	public void setListeners() {
		return;
	}

	/**
	 * 导入布局文件
	 * 
	 * @param inflater
	 * @param container
	 * @param b
	 * @return
	 */
	public View inflateLayout(LayoutInflater inflater, ViewGroup container,
			boolean b) {
		return null;
	}

	/**
	 * 预留方法：在onCreateView()方法中完成的其他工作
	 */
	public void doSomeWork() {

	}

	/**
	 * 初始化组件
	 * 
	 * @param view
	 *            包含组件的view
	 */
	public void initComponent(View view) {
		return;
	}

	@Override
	public void onClick(View v) {
		onClickComponent(v);
	}

	/**
	 * 组件点击处理方法
	 * 
	 * @param v
	 */
	public void onClickComponent(View v) {
		return;
	}

	/**
	 * 初始化组件
	 * 
	 * @param viewGroup
	 * @param view
	 * @param resId
	 */
	public <T> void initViewById(View viewGroup, T view, int resId) {
		view = (T) viewGroup.findViewById(resId);
	}
}
