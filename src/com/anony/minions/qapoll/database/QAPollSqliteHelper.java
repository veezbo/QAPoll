package com.anony.minions.qapoll.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class QAPollSqliteHelper extends SQLiteOpenHelper {

	private static final String TAG = QAPollSqliteHelper.class.getSimpleName();

	private static final String DATABASE_NAME = "qapoll.db";
	private static String DATABASE_PATH = "";
	private static final int DATABASE_VERSION = 1;

	private static QAPollSqliteHelper mInstance = null;

	public static QAPollSqliteHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new QAPollSqliteHelper(context);
		}

		return mInstance;
	}

	private QAPollSqliteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		DATABASE_PATH = Environment.getDataDirectory() + "/data/"
				+ context.getPackageName() + "/databases/";
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(QuestionsTable.DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		QuestionsTable.getInstance()
				.onUpgrade(database, oldVersion, newVersion);
	}

	@Override
	public void onDowngrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		QuestionsTable.getInstance().onDowngrade(database, oldVersion,
				newVersion);
	}
}
