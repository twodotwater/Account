package com.lzhn.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "DatabaseHelper";
	public static final String DATABASENAME = "DB_ZZHA";
	public static final int VERSION = 1;
	private String createTableSql = null;

	private DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	private DatabaseHelper(Context context, String name, CursorFactory factory,
			int version, String createTableSql) {
		this(context, name, factory, version);
		setCreateTableSql(createTableSql);
	}

	public static DatabaseHelper newInstance(Context context,
			String createTableSql) {
		return newInstance(context, DATABASENAME, createTableSql);
	}

	public static DatabaseHelper newInstance(Context context,
			String databaseName, String createTableSql) {
		return newInstance(context, databaseName, VERSION, createTableSql);
	}

	public static DatabaseHelper newInstance(Context context,
			String databaseName, int version, String createTableSql) {
		return new DatabaseHelper(context, databaseName, null, version,
				createTableSql);
	}

	public void setCreateTableSql(String createTableSql) {
		this.createTableSql = createTableSql;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		if (createTableSql != null)
			db.execSQL(createTableSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
