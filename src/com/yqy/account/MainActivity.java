package com.yqy.account;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.ShareActionProvider;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lzhn.account.R;
import com.lzhn.utils.adapter.CustomBaseAdapter;
import com.lzhn.utils.adapter.CustomBaseAdapter.OnGetViewListener;
import com.lzhn.utils.adapter.ViewHolder;
import com.lzhn.utils.os.BaseActivity_v4;
import com.lzhn.utils.print.LogUtils;
import com.lzhn.utils.share.ShareUtils;
import com.lzhn.utils.share.WXUtils;
import com.lzhn.utils.view.dialog.DialogUtils;
import com.lzhn.utils.view.dialog.MyDialog;
import com.lzhn.utils.view.dialog.MyDialog.OnBtnClickListener;
import com.yqy.account.common.Constant;
import com.yqy.account.common.DataBuffer;
import com.yqy.account.common.FileManager;
import com.yqy.account.common.Person;
import com.yqy.account.db.DBManager;

public class MainActivity extends BaseActivity_v4 {

	private static final String TAG = "MainActivity";
	private Spinner spinner_name;
	private EditText editText_money;
	private LinearLayout linearLayout_addMoney;
	private TextView textView_addDivider;
	private LinearLayout linearLayout_addPerson;
	private ListView listView_content;
	private LinearLayout linearLayout_result;
	private TextView textView_totalMoney;
	private TextView textView_averageMoney;
	private LinearLayout linearLayout_calculate;

	private CustomBaseAdapter<Person> adapter;

	private long previousPressTime = 0;

	private ShareActionProvider shareActionProvider;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);

		MenuItem shareMenu = (MenuItem) menu.findItem(R.id.action_share);
		shareActionProvider = (ShareActionProvider) shareMenu
				.getActionProvider();
		shareActionProvider.setShareIntent(ShareUtils.getShareTextIntent(null));

		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		searchView.setQueryHint(Constant.STRING_QUERY_HINT);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO 提交搜索
				AccountFragment accountFragment = (AccountFragment) getSupportFragmentManager()
						.findFragmentByTag("accountFragment");
				boolean hasAddToBackStack = accountFragment != null;
				LogUtils.printExcp(TAG, hasAddToBackStack);
				if (!hasAddToBackStack) {
					FragmentTransaction transaction = getSupportFragmentManager()
							.beginTransaction();
					accountFragment = new AccountFragment();
					transaction.addToBackStack("accountFragment");
					transaction.add(R.id.frameLayout_main, accountFragment,
							"accountFragment");
					transaction.commit();
				}
				accountFragment.getHandler()
						.obtainMessage(Constant.WHAT_QUERY_STRING, query)
						.sendToTarget();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO 搜索框输入改变
				return false;
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// LogUtils.printExcp(TAG, item.getItemId());
		switch (item.getItemId()) {
		case R.id.action_share:
			Toast.makeText(this, "on share", 0).show();
			if (shareActionProvider != null)
				shareActionProvider.setShareIntent(ShareUtils
						.getShareTextIntent("修改后的文本"));
			break;
		case R.id.action_wx:
			// WXUtils.launchWX();
			WXUtils.sendTextToWX("微信分享");
			Toast.makeText(this, "微信分享！", 0).show();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setContentViewLayout() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void initSomeWork() {
		super.initSomeWork();
		WXUtils.registerToWX(this);
		adapter = new CustomBaseAdapter<Person>(this,
				DataBuffer.getListPerson(), R.layout.listview_content_item);
	}

	@Override
	public void initComponent() {
		spinner_name = (Spinner) findViewById(R.id.spinner_name);
		editText_money = (EditText) findViewById(R.id.editText_money);
		linearLayout_addMoney = (LinearLayout) findViewById(R.id.linearLayout_addMoney);
		textView_addDivider = (TextView) findViewById(R.id.textView_addDivider);
		linearLayout_addPerson = (LinearLayout) findViewById(R.id.linearLayout_addPerson);
		listView_content = (ListView) findViewById(R.id.listView_content);
		linearLayout_result = (LinearLayout) findViewById(R.id.linearLayout_result);
		textView_totalMoney = (TextView) findViewById(R.id.textView_totalMoney);
		textView_averageMoney = (TextView) findViewById(R.id.textView_averageMoney);
		linearLayout_calculate = (LinearLayout) findViewById(R.id.linearLayout_calculate);

		// 初始状态
		hideAllAddView();
	}

	@Override
	public void setListeners() {
		spinner_name.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					hideAllAddView();
					return;
				}
				String name = parent.getItemAtPosition(position).toString();
				// 新的人员名字
				Person person = Person.getPersonByName(
						DataBuffer.getListPerson(), name);
				if (person == null) {
					person = new Person(name);
					hideAddMoneyView();
				} else {
					showAddMoneyView();
				}
				DataBuffer.setCurrentPerson(person);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		listView_content.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Person p = (Person) parent.getItemAtPosition(position);

				showPersonDetailsDialog(p);
			}
		});
		linearLayout_addMoney.setOnClickListener(this);
		linearLayout_addPerson.setOnClickListener(this);
		linearLayout_calculate.setOnClickListener(this);
		linearLayout_result.setOnClickListener(this);

		adapter.setGetViewListener(new OnGetViewListener<Person>() {

			@Override
			public void onGetView(List<Person> items, int position,
					View convertView) {
				TextView textView_name = ViewHolder.get(convertView,
						R.id.textView_name);
				TextView textView_money = ViewHolder.get(convertView,
						R.id.textView_money);
				TextView textView_totalMoney = ViewHolder.get(convertView,
						R.id.textView_tMoney);
				TextView textView_diffMoney = ViewHolder.get(convertView,
						R.id.textView_diffMoney);

				Person person = items.get(position);

				textView_name.setText(person.getName());
				textView_money.setText(Person.parseMoneyToString(person));
				textView_totalMoney.setText(Math.round(person.getTotalMoney())
						+ "元");
				textView_diffMoney.setText(Math.round(person.getDiffMoney())
						+ "元");
			}
		});

		listView_content.setAdapter(adapter);
	}

	/**
	 * 显示个人信息对话框
	 * 
	 * @param p
	 */
	public void showPersonDetailsDialog(final Person p) {

		MyDialog dialog = MyDialog.newInstance(MainActivity.this,
				MyDialog.MODE_MESSAGE);
		dialog.setTitle("个人支付信息").setMessage(Person.getPersonDetails(p))
				.setPositiveText("知道了").setNegativeText("删除");
		dialog.setOnBtnClickListener(new OnBtnClickListener() {

			@Override
			public void onOKClick(MyDialog dialog) {
				dialog.dismiss();
			}

			@Override
			public void onNOClick(MyDialog dialog) {
				DataBuffer.removePerson(p);
				calculateWork();
				if (spinner_name.getSelectedItem().toString()
						.equals(p.getName())) {
					hideAddMoneyView();
				}
				if (DataBuffer.getCurrentPerson().equals(p)) {
					DataBuffer.setCurrentPerson(new Person(p.getName()));
				}
				Toast.makeText(MainActivity.this, "已删除此人支付信息！", 0).show();
			}
		}).show();
	}

	@Override
	public void onClickComponent(View v) {
		switch (v.getId()) {
		case R.id.linearLayout_addMoney:
			// 添加金额
			addMoney(false);

			break;
		case R.id.linearLayout_addPerson:
			// 添加新人
			DataBuffer.addPerson(DataBuffer.getCurrentPerson());
			Toast.makeText(this, "已添加新人！", Toast.LENGTH_SHORT).show();
			addMoney(true);
			showAddMoneyView();
			break;
		case R.id.linearLayout_calculate:
			calculateWork();
			break;
		case R.id.linearLayout_result:
			showSaveDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * 存储数据对话框
	 */
	public void showSaveDialog() {
		if (DataBuffer.getListPerson() == null
				|| DataBuffer.getListPerson().size() <= 0) {
			return;
		}
		// 双击两次弹出对话框
		if (System.currentTimeMillis() - previousPressTime < 300) {
			DialogUtils.showMessageDialog(this, "确定存储本次数据？",
					new OnBtnClickListener() {

						@Override
						public void onOKClick(MyDialog dialog) {
							// TODO 存储数据库
							if (DataBuffer.getListPerson() != null
									&& DataBuffer.getListPerson().size() > 0) {
								if (DBManager.getInstance(MainActivity.this)
										.insertAccount()) {

									Toast.makeText(MainActivity.this, "已存储数据！",
											0).show();

								} else {
									Toast.makeText(MainActivity.this,
											"存储数据失败！", 0).show();

								}

								// 保存文件
								String fileName = FileManager.saveFile();
								if (fileName != null) {
									Toast.makeText(MainActivity.this,
											"已保存文件！\n" + fileName, 0).show();
								} else {
									Toast.makeText(MainActivity.this,
											"文件保存失败！", 0).show();
								}
							} else {
								Toast.makeText(MainActivity.this, "暂无可存储的数据！",
										0).show();
							}
							dialog.dismiss();
						}

						@Override
						public void onNOClick(MyDialog dialog) {
						}
					});
		}
		previousPressTime = System.currentTimeMillis();
	}

	/**
	 * 为人员添加金额
	 * 
	 * @param isNewPerson
	 *            true：新成员；false：已存在的人员
	 */
	public void addMoney(boolean isNewPerson) {
		boolean isInputCorrect = true;
		List<Float> moneys = new ArrayList<Float>();
		String moneyString = editText_money.getText().toString().trim();
		String regularExpression = "[\\+\\-]?\\d*\\.?\\d+";// 分割输入金额
		Pattern pattern = Pattern.compile(regularExpression);
		Matcher matcher = pattern.matcher(moneyString);
		try {
			// LogUtils.printExcp(TAG, moneyString);
			while (matcher.find()) {
				String str = matcher.group();
				if (!str.trim().isEmpty()) {
					moneys.add(Float.parseFloat(str));
				}
			}

		} catch (Exception e) {
			isInputCorrect = false;
			editText_money.requestFocus();
			Toast.makeText(this, "输入的金额格式有误！", Toast.LENGTH_SHORT).show();
		}
		// 正常运行
		if (isInputCorrect) {
			editText_money.setText("");
			DataBuffer.getCurrentPerson().addMoney(moneys);
			Toast.makeText(this, "已添加新的金额！", Toast.LENGTH_SHORT).show();
		}

		// 输入金额正确、或者添加新人员；则计算平均值
		if (isNewPerson || isInputCorrect) {
			calculateWork();
		}
	}

	/**
	 * 完成计算工作
	 */
	private void calculateWork() {
		float totalMoney = 0f;// 家庭总金额
		float averageMoney = 0f;// 家庭平均额
		List<Person> listPerson = DataBuffer.getListPerson();
		// 计算总金额
		for (Person p : listPerson) {
			totalMoney += p.getTotalMoney();
		}
		// 计算平均值
		if (listPerson.size() != 0) {
			averageMoney = totalMoney / listPerson.size();
		}
		// 计算个人差额
		for (Person p : listPerson) {
			p.setDiffMoney(Math.round(p.getTotalMoney() - averageMoney));
		}
		// 刷新显示
		textView_totalMoney.setText(Math.round(totalMoney) + "元");
		textView_averageMoney.setText(Math.round(averageMoney) + "元");
		adapter.refresh(DataBuffer.getListPerson());
		// 缓存记录
		DataBuffer.setTotalMoney(totalMoney);
		DataBuffer.setAverageMoney(averageMoney);
	}

	@Override
	public void doSomeWork() {
		super.doSomeWork();

	}

	public void hideAllAddView() {
		linearLayout_addMoney.setVisibility(View.GONE);
		linearLayout_addPerson.setVisibility(View.GONE);
	}

	public void hideAddMoneyView() {
		linearLayout_addMoney.setVisibility(View.GONE);
		linearLayout_addPerson.setVisibility(View.VISIBLE);
	}

	public void showAddMoneyView() {
		linearLayout_addMoney.setVisibility(View.VISIBLE);
		linearLayout_addPerson.setVisibility(View.GONE);
	}

}
