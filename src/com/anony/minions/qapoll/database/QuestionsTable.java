package com.anony.minions.qapoll.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.anony.minions.qapoll.QAPollContextManager;

public class QuestionsTable extends BaseDataSource {

	// Database table
	public static final String TABLE_NAME = "Topic";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_BODY = "Body";
	public static final String COLUMN_TIME_STAMP = "TimeStamp";
	public static final String COLUMN_OWNER = "Owner";
	public static final String[] ALL_COLUMNS = new String[] { COLUMN_ID,
			COLUMN_BODY, COLUMN_TIME_STAMP, COLUMN_OWNER, };

	// Database creation SQL statement
	public static final String DATABASE_CREATE = "create table if not exists " + TABLE_NAME + " ( " + 
			COLUMN_ID + " " + "TEXT PRIMARY KEY" + " , " + 
			COLUMN_BODY + " " + "TEXT" + " , " + 
			COLUMN_TIME_STAMP + " "	+ "TEXT" + " , " + 
			COLUMN_OWNER + " " + "TEXT" + " );";

	static QuestionsTable instance = null;

	public static QuestionsTable getInstance() {
		if (instance == null)
			instance = new QuestionsTable(QAPollContextManager.getContext());
		return instance;
	}

	private QuestionsTable(Context context) {
		super(context);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDowngrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		// TODO Auto-generated method stub

	}

}
