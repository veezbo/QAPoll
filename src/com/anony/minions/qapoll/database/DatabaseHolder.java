package com.anony.minions.qapoll.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.anony.minions.qapoll.QAPollContextManager;

public class DatabaseHolder {

	private static final String TAG = DatabaseHolder.class.getSimpleName();

	private static SQLiteDatabase database = null;
	private static QAPollSqliteHelper dbHelper = null;

	public static void init(DatabaseInitListener listener) {
		try {
			dbHelper = QAPollSqliteHelper.getInstance(QAPollContextManager
					.getContext());
			open(true);
		} catch (SQLiteException sqle) {
			Log.e(TAG, "SQLite Exception is: " + sqle.getMessage());
			if (listener != null)
				listener.onError();
		}
		if (listener != null)
			listener.onInit();
	}

	public static SQLiteDatabase getDatabase() {
		if (database == null)
			init(null);
		return database;
	}

	/**
	 * checks if the connection to the DB is already open
	 * 
	 * @return
	 */
	private static boolean isOpen() {
		if (database != null)
			return database.isOpen();
		return false;
	}

	/**
	 * opens a connection to the DB
	 */
	public static void open(boolean writable) {

		if (dbHelper != null)
			dbHelper.close();
		try {
			if (dbHelper != null)
				database = dbHelper.getWritableDatabase();
		} catch (SQLiteException sqle) {
			throw new Error("DatabaseHolder: open: Exception creating DB");
		}

	}

	/**
	 * closes the connection to the DB</br> should be called at the time that
	 * the app is being closed</br> commented out for now cause it was causing
	 * issues.</br> we should close the whole DB once for the whole application
	 * life cycle.</br> in one of the android's official examples they never
	 * close the db! some practices say you can close it in onDestroy
	 */
	public static void close() {
		if (isOpen() && dbHelper != null)
			dbHelper.close();
	}
}
