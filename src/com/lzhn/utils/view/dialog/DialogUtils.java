package com.lzhn.utils.view.dialog;

import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.View;

import com.lzhn.utils.view.dialog.MyDialog.OnBtnClickListener;

/** 提示对话框 */
public class DialogUtils {

	private static final String TAG = "DialogUtils";
	// private static final String TITLE = "title";
	// private static final String MESSAGE = "message";
	// private Context context;
	// private String title;
	// private String message;
	// private OnClickListener listener;
	private String positive = "确定";
	private String negative = "取消";
	private View view = null;
	private static AlertDialog.Builder builder;

	public DialogUtils() {
		super();
	}

	public View getView() {
		return view;
	}

	public String getPositive() {
		return positive;
	}

	public void setPositive(String positive) {
		this.positive = positive;
	}

	public String getNegative() {
		return negative;
	}

	public void setNegative(String negative) {
		this.negative = negative;
	}

	/**
	 * 显示对话框
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param message
	 *            对话框信息
	 * @param listener
	 *            按钮点击监听器
	 * @param view2
	 *            自定义显示
	 */
	private void showDialog(Context context, CharSequence title,
			CharSequence message, OnClickListener listener, View view2) {
		builder = new Builder(context);
		if (title == null) {
			title = "提示";
		}
		builder.setCancelable(false).setTitle(title)
				.setPositiveButton(positive, listener)
				.setNegativeButton(negative, listener);
		if (message != null) {
			builder.setMessage(message);
		}
		view = view2;
		if (view != null) {
			builder.setView(view);
		}
		builder.create().show();
	}

	/**
	 * 取消对话框
	 * 
	 * @param dialog
	 * @param dismiss
	 *            true:取消；false:不取消
	 */
	private static void dismissDialog(DialogInterface dialog, boolean dismiss) {
		try {
			Field field = dialog.getClass().getSuperclass()
					.getDeclaredField("mShowing");
			field.setAccessible(true);
			// 设置mShowing值，欺骗android系统
			field.set(dialog, dismiss);
		} catch (Exception e) {
			Log.e(TAG, e.getLocalizedMessage());
		}
	}

	/**
	 * 显示提示信息对话框
	 * 
	 * @param context
	 * @param msg
	 *            提示信息
	 * @param onBtnClickListener
	 */
	public static void showMessageDialog(Context context, String msg,
			OnBtnClickListener onBtnClickListener) {
		MyDialog dialog = MyDialog.newInstance(context, MyDialog.MODE_MESSAGE);
		dialog.setMessage(msg).setOnBtnClickListener(onBtnClickListener).show();
	}

	/**
	 * 显示文本输入框
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param inputText
	 *            输入框初始显示文本
	 * @param onBtnClickListener
	 */
	public static void showInputDialog(Context context, String title,
			String inputText, OnBtnClickListener onBtnClickListener) {
		MyDialog dialog = MyDialog.newInstance(context, MyDialog.MODE_INPUT);
		if (title != null) {
			dialog.setTitle(title);
		}
		dialog.setInputText(inputText)
				.setOnBtnClickListener(onBtnClickListener).show();

	}

	/**
	 * 显示自定义布局view的对话框
	 * 
	 * @param context
	 * @param title
	 *            标题
	 * @param positiveText
	 *            确认按钮显示文字
	 * @param childView
	 *            添加到对话框的自定义view
	 * @param onBtnClickListener
	 */
	public static void showViewDialog(Context context, String title,
			String positiveText, View childView,
			OnBtnClickListener onBtnClickListener) {
		MyDialog dialog = MyDialog.newInstance(context, MyDialog.MODE_VIEW);
		if (title != null) {
			dialog.setTitle(title);
		}
		if (positiveText != null) {
			dialog.setPositiveText(positiveText);
		}
		dialog.setChildView(childView)
				.setOnBtnClickListener(onBtnClickListener).show();
	}
}
