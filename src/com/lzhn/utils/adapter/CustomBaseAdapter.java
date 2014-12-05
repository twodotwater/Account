package com.lzhn.utils.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CustomBaseAdapter<T> extends BaseAdapter {
	// 上下文对象
	private Context context;
	// item中的数据
	private List<T> items;
	// listview中item的布局文件id
	private int resource;
	// 自定义回调接口
	private OnGetViewListener<T> getViewListener;

	private CustomBaseAdapter() {
		super();
		// this.items = new ArrayList<T>();
	}

	public CustomBaseAdapter(Context context) {
		this();
		this.context = context;
	}

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param items
	 *            数据源
	 * @param resource
	 *            item布局文件id
	 */
	public CustomBaseAdapter(Context context, List<T> items, int resource) {
		this();
		this.context = context;
		this.items = items;
		this.resource = resource;
	}

	public List<T> getItems() {
		return items;
	}

	/**
	 * 设置数据源
	 * 
	 * @param items
	 */
	public void setItems(List<T> items) {
		this.items = items;
	}

	public int getResource() {
		return resource;
	}

	public void setResource(int resource) {
		this.resource = resource;
	}

	public void setGetViewListener(OnGetViewListener<T> getViewListener) {
		this.getViewListener = getViewListener;
	}

	/**
	 * 刷新数据列表
	 * 
	 * @param items
	 *            新的数据源
	 */
	public void refresh(List<T> items) {
		setItems(items);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(resource,
					parent, false);
		}
		getViewListener.onGetView(items, position, convertView);
		return convertView;
	}

	/**
	 * 回调接口，完成baseAdater的getView方法中的操作
	 * 
	 * @param <T>
	 */
	public interface OnGetViewListener<T> {
		/**
		 * 在baseAdater的getView方法中调用。 Note：viewholder优化
		 * 
		 * @param items
		 *            listview中item中的数据的集合
		 * @param position
		 *            item的位置
		 * @param convertView
		 *            item的视图view
		 */
		public void onGetView(List<T> items, int position, View convertView);
	}
}
