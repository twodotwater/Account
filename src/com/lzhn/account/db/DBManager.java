package com.lzhn.account.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lzhn.account.common.Account;
import com.lzhn.account.common.DataBuffer;
import com.lzhn.account.common.Person;
import com.zzha.util.db.DatabaseHelper;
import com.zzha.utils.print.ConvertUtils;
import com.zzha.utils.print.LogUtils;

public class DBManager {
	private static final String TAG = "DBManager";

	private static final String TABLE_ACCOUNT = "Account";
	private static final String TABLE_PERSON = "Person";
	private static final String ID = "_id";
	private static final String TIME = "time";
	private static final String TOTAL_MONEY = "totalMoney";
	// private static final String AVERAGE_MONEY = "averageMoney";

	private static final String PERSON_NAME = "personName";
	private static final String PERSON_MONEY = "personMoney";
	// private static final String PERSON_TOTAL_MONEY = "personTotalMoney";
	// private static final String PERSON_DIFF_MONEY = "personDiffMoney";

	private static final String CREATE_ACCOUNT_SQL = "create table if not exists "
			+ TABLE_ACCOUNT
			+ "( "
			+ ID
			+ " integer primary key autoincrement , "
			+ TIME
			+ " varchar , "
			+ PERSON_NAME + " varchar , " + TOTAL_MONEY + " float )";

	private static final String CREATE_PERSON_SQL = "create table if not exists "
			+ TABLE_PERSON
			+ "( "
			+ ID
			+ " integer primary key autoincrement , "
			+ TIME
			+ " varchar , "
			+ PERSON_NAME + " varchar , " + PERSON_MONEY + " varchar )";
	private final DatabaseHelper helper;
	private static SQLiteDatabase database;
	private static DBManager dbManager;

	private DBManager(Context context) {
		helper = DatabaseHelper.newInstance(context, CREATE_ACCOUNT_SQL);
		database = helper.getWritableDatabase();
		database.execSQL(CREATE_PERSON_SQL);
	}

	public static DBManager getInstance(Context context) {
		if (dbManager == null) {
			dbManager = new DBManager(context);
		}
		return dbManager;
	}

	public boolean insertAccount() {
		String time = ConvertUtils.formatDate(ConvertUtils.PATTERN_5,
				System.currentTimeMillis());
		int count = 0;
		for (Person p : DataBuffer.getListPerson()) {
			ContentValues pValues = new ContentValues();
			pValues.put(TIME, time);
			pValues.put(PERSON_NAME, p.getName());
			pValues.put(PERSON_MONEY, Person.parseMoneyToString(p));
			database.insert(TABLE_PERSON, null, pValues);

			count++;
		}
		ContentValues values = new ContentValues();
		values.put(TIME, time);
		values.put(TOTAL_MONEY, DataBuffer.getTotalMoney());
		values.put(PERSON_NAME,
				Account.getNamesInList(DataBuffer.getListPerson()));
		database.insert(TABLE_ACCOUNT, null, values);
		return count == DataBuffer.getListPerson().size();
	}

	public Account parseCursorToAccount(Cursor cursor) {
		int id = cursor.getInt(cursor.getColumnIndex(ID));
		String time = cursor.getString(cursor.getColumnIndex(TIME));
		String nameString = cursor
				.getString(cursor.getColumnIndex(PERSON_NAME));
		float totalMoney = cursor.getFloat(cursor.getColumnIndex(TOTAL_MONEY));
		return new Account(time, Account.parseStringToNames(nameString),
				totalMoney);
	}

	public List<Account> queryAccounts() {
		String sql = "select * from " + TABLE_ACCOUNT;
		Cursor cursor = database.rawQuery(sql, null);
		List<Account> accounts = new ArrayList<Account>();
		while (cursor.moveToNext()) {
			accounts.add(parseCursorToAccount(cursor));
		}
		return accounts;
	}

	public List<Account> queryAccounts(String time) {
		String sql = "select * from " + TABLE_ACCOUNT + " where " + TIME
				+ " like '%" + time + "%'";
		LogUtils.printExcp(TAG, sql);
		Cursor cursor = database.rawQuery(sql, null);
		List<Account> accounts = new ArrayList<Account>();
		while (cursor.moveToNext()) {
			accounts.add(parseCursorToAccount(cursor));
		}
		return accounts;
	}

	public List<Person> queryPersons(Account account) {
		String sql = "select * from " + TABLE_PERSON + " where " + TIME
				+ " = '" + account.getTime() + "';";
		Cursor cursor = database.rawQuery(sql, null);
		List<Person> listPerson = new ArrayList<Person>();
		while (cursor.moveToNext()) {
			Person p = new Person(cursor.getString(cursor
					.getColumnIndex(PERSON_NAME)));
			p.setTime(account.getTime());
			String moneyString = cursor.getString(cursor
					.getColumnIndex(PERSON_MONEY));
			p.setMoney(Person.parseStringToMoney(moneyString));
			p.setDiffMoney(p.getTotalMoney() - account.getTotalMoney());
			listPerson.add(p);
		}
		return listPerson;
	}

	public int deleteAccount(Account account) {
		deletePerson(account.getTime());
		String whereClause = TIME + " = ?";
		String[] whereArgs = new String[] { account.getTime() };
		return database.delete(TABLE_ACCOUNT, whereClause, whereArgs);
	}

	private int deletePerson(String time) {
		String whereClause = TIME + " = ?";
		String[] whereArgs = new String[] { time };
		return database.delete(TABLE_PERSON, whereClause, whereArgs);
	}
}
