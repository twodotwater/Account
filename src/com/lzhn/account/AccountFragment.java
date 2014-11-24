package com.lzhn.account;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzhn.account.common.Account;
import com.lzhn.account.common.Constant;
import com.lzhn.account.common.Person;
import com.lzhn.account.db.DBManager;
import com.zzha.util.os.BaseFragment_v4;
import com.zzha.util.view.dialog.MyDialog;
import com.zzha.util.view.dialog.MyDialog.OnBtnClickListener;
import com.zzha.utils.adapter.ExpandableBaseAdpater;
import com.zzha.utils.adapter.ExpandableBaseAdpater.OnGetViewListener;
import com.zzha.utils.adapter.ViewHolder;

public class AccountFragment extends BaseFragment_v4 {

	private LinearLayout linearLayout_account;
	private TextView textView_empty;
	private ExpandableListView expandableListView_account;
	private ExpandableBaseAdpater<Account, Person> expandableBaseAdpater;
	private List<Account> listAccounts;
	private List<List<Person>> listPersons;
	private long previousTime = 0;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case Constant.WHAT_QUERY_STRING:
				// 查询历史数据
				String query = msg.obj.toString();
				listAccounts = DBManager.getInstance(getActivity())
						.queryAccounts(query);
				if (listAccounts != null && listAccounts.size() > 0) {
					listPersons.clear();
					for (Account account : listAccounts) {
						listPersons.add(DBManager.getInstance(getActivity())
								.queryPersons(account));
					}
				}
				refreshView();
				break;

			default:
				break;
			}
		};
	};

	public Handler getHandler() {
		return handler;
	}

	@Override
	public void onGetArguments(Bundle arguments) {

		super.onGetArguments(arguments);
		expandableBaseAdpater = new ExpandableBaseAdpater<Account, Person>(
				getActivity(), R.layout.expandablelistview_account_group,
				R.layout.expandablelistview_account_child);

		listAccounts = new ArrayList<Account>();
		listPersons = new ArrayList<List<Person>>();
		expandableBaseAdpater.setGroupItems(listAccounts);
		expandableBaseAdpater.setChildItems(listPersons);
	}

	@Override
	public void setListeners() {

		super.setListeners();
		linearLayout_account.setOnClickListener(this);
		expandableBaseAdpater
				.setOnGetViewListener(new OnGetViewListener<Account, Person>() {

					@Override
					public void onGetGroupView(View convertView,
							List<Account> groupItems, int groupPosition,
							boolean isExpanded) {
						TextView textView_time = ViewHolder.get(convertView,
								R.id.textView_time);
						TextView textView_totalMoney = ViewHolder.get(
								convertView, R.id.textView_totalMoney);
						TextView textView_averageMoney = ViewHolder.get(
								convertView, R.id.textView_averageMoney);

						Account account = groupItems.get(groupPosition);
						textView_time.setText(account.getTime());
						textView_totalMoney.setText(Math.round(account
								.getTotalMoney()) + " 元");
						textView_averageMoney.setText(Math.round(account
								.getTotalMoney() / account.getListName().size())
								+ " 元");
					}

					@Override
					public void onGetChildView(View convertView,
							List<List<Person>> childItems, int groupPosition,
							int childPosition, boolean isLastChild) {
						TextView textView_name = ViewHolder.get(convertView,
								R.id.textView_name);
						TextView textView_money = ViewHolder.get(convertView,
								R.id.textView_money);
						TextView textView_totalMoney = ViewHolder.get(
								convertView, R.id.textView_totalMoney);
						TextView textView_diffMoney = ViewHolder.get(
								convertView, R.id.textView_diffMoney);

						Account account = listAccounts.get(groupPosition);
						Person person = childItems.get(groupPosition).get(
								childPosition);
						textView_name.setText(person.getName());
						textView_money.setText(Person
								.parseMoneyToString(person));
						textView_totalMoney.setText(Math.round(person
								.getTotalMoney()) + " 元");
						textView_diffMoney.setText(Math.round(person
								.getTotalMoney()
								- (account.getTotalMoney() / account
										.getListName().size()))
								+ " 元");
					}

				});
		expandableListView_account
				.setOnGroupClickListener(new OnGroupClickListener() {

					@Override
					public boolean onGroupClick(ExpandableListView parent,
							View v, int groupPosition, long id) {
						if (System.currentTimeMillis() - previousTime < 300) {
							showDeleteAccountDialog(groupPosition);

						}
						previousTime = System.currentTimeMillis();

						return false;
					}
				});

		expandableListView_account
				.setOnChildClickListener(new OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
							View v, int groupPosition, int childPosition,
							long id) {
						// TODO 显示详细信息
						Person p = listPersons.get(groupPosition).get(
								childPosition);
						MyDialog dialog = MyDialog.newInstance(getActivity(),
								MyDialog.MODE_MESSAGE);
						dialog.setTitle("个人详细支付信息")
								.setBackgroundDrawable(
										R.drawable.dialog_background)
								.setMessage(Person.getPersonDetails(p))
								.hidePositiveButton().setNegativeText("知道了")
								.show();
						return false;
					}
				});
	}

	protected void showDeleteAccountDialog(final int groupPosition) {

		final Account account = listAccounts.get(groupPosition);

		MyDialog dialog = MyDialog.newInstance(getActivity(),
				MyDialog.MODE_MESSAGE);
		String msg = "确定删除此记录？\n\n日期：" + account.getTime();
		dialog.setMessage(msg)
				.setBackgroundDrawable(R.drawable.dialog_background)
				.setOnBtnClickListener(new OnBtnClickListener() {

					@Override
					public void onOKClick(MyDialog dialog) {
						dialog.dismiss();
						if (DBManager.getInstance(getActivity()).deleteAccount(
								account) > 0) {
							listAccounts.remove(groupPosition);
							listPersons.remove(groupPosition);
							refreshView();
							Toast.makeText(getActivity(),
									"已删除记录：" + account.getTime(), 0).show();
						}
					}

					@Override
					public void onNOClick(MyDialog dialog) {
					}
				}).show();
	}

	@Override
	public View inflateLayout(LayoutInflater inflater, ViewGroup container,
			boolean b) {

		return inflater.inflate(R.layout.fragment_account, container, b);
	}

	@Override
	public void doSomeWork() {

		super.doSomeWork();
		expandableListView_account.setAdapter(expandableBaseAdpater);

	}

	@Override
	public void initComponent(View view) {

		super.initComponent(view);
		linearLayout_account = (LinearLayout) view
				.findViewById(R.id.linearLayout_account);
		textView_empty = (TextView) view.findViewById(R.id.textView_empty);
		expandableListView_account = (ExpandableListView) view
				.findViewById(R.id.expandableListView_account);
	}

	private void refreshView() {
		expandableBaseAdpater.refresh(listAccounts, listPersons);
		if (listAccounts != null && listAccounts.size() > 0) {
			hideEmptyView();
		} else {
			showEmptyView();
		}
	}

	private void hideEmptyView() {
		textView_empty.setVisibility(View.GONE);
		expandableListView_account.setVisibility(View.VISIBLE);
	}

	private void showEmptyView() {
		textView_empty.setVisibility(View.VISIBLE);
		expandableListView_account.setVisibility(View.GONE);
	}
}
