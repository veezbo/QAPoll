package com.anony.minions.qapoll.database;

import android.database.sqlite.SQLiteDatabase;

public interface DataBaseCallBackListener {

	public void onCreate(SQLiteDatabase database);

	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion);

	public void onDowngrade(SQLiteDatabase database, int oldVersion,
			int newVersion);

}
