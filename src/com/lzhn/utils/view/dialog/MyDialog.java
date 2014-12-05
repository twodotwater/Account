package com.lzhn.utils.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzhn.account.R;

/**
 * 对话框
 * 
 * @author lzhn
 * 
 */
public class MyDialog extends Dialog implements
		android.view.View.OnClickListener {
	private static final String TAG = "MyDialog";
	public static final int MODE_MESSAGE = 1;
	public static final int MODE_INPUT = 2;
	public static final int MODE_VIEW = 3;
	private Context context;
	/** 对话框的布局view */
	private View view;
	/** 显示contentView中的子View */
	private View childView;
	private TextView tv_title;
	private TextView tv_message;
	private EditText et_input;
	private Button btn_OK;
	private Button btn_NO;
	private TextView tv_gap;
	/** 对话框中间显示内容的View */
	private LinearLayout contentView;
	private OnBtnClickListener onBtnClickListener;

	private MyDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		view = LayoutInflater.from(context).inflate(R.layout.mydialog, null);
		initViews(view);
	}

	private MyDialog(Context context, int theme, int mode) {
		this(context, theme);
		switch (mode) {
		case MODE_MESSAGE:
			tv_message.setVisibility(View.VISIBLE);
			et_input.setVisibility(View.GONE);
			break;
		case MODE_INPUT:
			tv_message.setVisibility(View.GONE);
			et_input.setVisibility(View.VISIBLE);
			break;
		case MODE_VIEW:
			tv_message.setVisibility(View.GONE);
			et_input.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	/**
	 * 生成一个对话框
	 * 
	 * @param context
	 * @param mode
	 *            对话框模式：MODE_MESSAGE、MODE_INPUT、MODE_VIEW
	 * @return
	 */
	public static MyDialog newInstance(Context context, int mode) {
		return new MyDialog(context, android.R.style.Theme_Light_NoTitleBar,
				mode);
	}

	private void initViews(View view2) {
		tv_title = (TextView) view2.findViewById(R.id.tv_title);
		tv_message = (TextView) view2.findViewById(R.id.tv_message);
		et_input = (EditText) view2.findViewById(R.id.et_input);
		btn_OK = (Button) view2.findViewById(R.id.btn_OK);
		btn_NO = (Button) view2.findViewById(R.id.btn_NO);
		tv_gap = (TextView) view2.findViewById(R.id.tv_gap);
		contentView = (LinearLayout) view2.findViewById(R.id.contentView);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(view);
		initSize();
		setListeners();
	}

	private MyDialog setListeners() {
		btn_OK.setOnClickListener(this);
		btn_NO.setOnClickListener(this);
		return this;
	}

	private void initSize() {
		Window window = getWindow();
		// window.setGravity(Gravity.TOP | Gravity.LEFT);
		WindowManager.LayoutParams lp = window.getAttributes();
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);
		boolean isLandscape = lp.screenOrientation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		int minHeight = isLandscape ? metrics.heightPixels / 2
				: metrics.heightPixels / 3;
		int minWidth = isLandscape ? metrics.widthPixels / 2
				: metrics.widthPixels / 3 * 2;
		view.setMinimumHeight(minHeight);
		view.setMinimumWidth(minWidth);
		view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		lp.width = view.getMeasuredWidth();
		lp.height = view.getMeasuredHeight();
		lp.alpha = 1f;
		window.setAttributes(lp);
	}

	public MyDialog setOnBtnClickListener(OnBtnClickListener onBtnClickListener) {
		this.onBtnClickListener = onBtnClickListener;
		return this;
	}

	public MyDialog setBackgroundDrawable(int id) {
		if (id != 0)
			view.setBackground(context.getResources().getDrawable(id));
		return this;
	}

	public MyDialog setBackgroundColor(int color) {
		view.setBackgroundColor(color);
		return this;
	}

	public MyDialog setTitle(String title) {
		tv_title.setText(title);
		return this;
	}

	public MyDialog setMessage(String msg) {
		tv_message.setText(msg);
		return this;
	}

	public MyDialog setPositiveText(String positiveText) {
		btn_OK.setText(positiveText);
		return this;
	}

	public MyDialog setNegativeText(String negativeText) {
		btn_NO.setText(negativeText);
		return this;
	}

	public MyDialog setInputText(String inputText) {
		et_input.setText(inputText);
		return this;
	}

	public String getInputText() {
		return et_input.getText().toString().trim();
	}

	public MyDialog hidePositiveButton() {
		btn_OK.setVisibility(View.GONE);
		tv_gap.setVisibility(View.GONE);
		return this;
	}

	private MyDialog addView(View childView) {
		contentView.removeAllViews();
		contentView.addView(childView);
		initSize();
		this.childView = childView;
		return this;
	}

	public View getChildView() {
		return childView;
	}

	/**
	 * 指定对话框显示的子View
	 * 
	 * @param childView
	 * @return
	 */
	public MyDialog setChildView(View childView) {
		addView(childView);
		setContentView(view);
		return this;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_OK:
			if (onBtnClickListener != null) {
				onBtnClickListener.onOKClick(this);
			}
			break;
		case R.id.btn_NO:
			if (onBtnClickListener != null)
				onBtnClickListener.onNOClick(this);
			this.dismiss();
			break;

		default:
			break;
		}
	}

	public interface OnBtnClickListener {
		void onOKClick(MyDialog dialog);

		void onNOClick(MyDialog dialog);
	}

}
