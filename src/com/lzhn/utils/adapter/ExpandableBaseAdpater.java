package com.lzhn.utils.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class ExpandableBaseAdpater<TG, TC> extends BaseExpandableListAdapter {
	private Context context;
	private List<TG> groupItems;
	private List<List<TC>> childItems;
	private int groupViewResourceId;
	private int childViewResourceId;
	private OnGetViewListener<TG, TC> onGetViewListener;

	public ExpandableBaseAdpater(Context context) {
		super();
		this.context = context;
	}

	public ExpandableBaseAdpater(Context context, int groupViewResourceId,
			int childViewResourceId) {
		super();
		this.context = context;
		this.groupViewResourceId = groupViewResourceId;
		this.childViewResourceId = childViewResourceId;
	}

	public ExpandableBaseAdpater(Context context, List<TG> groupItems,
			List<List<TC>> childItems, int groupViewResourceId,
			int childViewResourceId) {
		super();
		this.context = context;
		this.groupItems = groupItems;
		this.childItems = childItems;
		this.groupViewResourceId = groupViewResourceId;
		this.childViewResourceId = childViewResourceId;
	}

	public List<TG> getGroupItems() {
		return groupItems;
	}

	public void setGroupItems(List<TG> groupItems) {
		this.groupItems = groupItems;
	}

	public List<List<TC>> getChildItems() {
		return childItems;
	}

	public void setChildItems(List<List<TC>> childItems) {
		this.childItems = childItems;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void setGroupViewResourceId(int groupViewResourceId) {
		this.groupViewResourceId = groupViewResourceId;
	}

	public void setChildViewResourceId(int childViewResourceId) {
		this.childViewResourceId = childViewResourceId;
	}

	public void setOnGetViewListener(OnGetViewListener<TG, TC> onGetViewListener) {
		this.onGetViewListener = onGetViewListener;
	}

	public void refresh(List<TG> gItems, List<List<TC>> cItems) {
		if (gItems != null) {
			this.groupItems = gItems;
		}
		if (cItems != null) {
			this.childItems = cItems;
		}
		setGroupItems(groupItems);
		setChildItems(childItems);
		notifyDataSetChanged();
	}

	@Override
	public int getGroupCount() {
		return groupItems.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childItems.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupItems.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childItems.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		convertView = initConvertView(convertView, groupViewResourceId, parent);
		onGetViewListener.onGetGroupView(convertView, groupItems,
				groupPosition, isExpanded);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		convertView = initConvertView(convertView, childViewResourceId, parent);
		onGetViewListener.onGetChildView(convertView, childItems,
				groupPosition, childPosition, isLastChild);
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	public View initConvertView(View convertView, int resourceId,
			ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(resourceId,
					parent, false);
		}
		return convertView;
	}

	public interface OnGetViewListener<TG, TC> {
		public void onGetGroupView(View convertView, List<TG> groupItems,
				int groupPosition, boolean isExpanded);

		public void onGetChildView(View convertView, List<List<TC>> childItems,
				int groupPosition, int childPosition, boolean isLastChild);
	}
}
