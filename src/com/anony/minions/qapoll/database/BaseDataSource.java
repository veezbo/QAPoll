package com.anony.minions.qapoll.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class BaseDataSource implements DataBaseCallBackListener {

	public BaseDataSource(Context context) {
	}

	protected synchronized <T> T read(DatabaseReadRequest<T> readRequest) {
		if (readRequest == null)
			return null;

		T result = null;

		SQLiteDatabase database = DatabaseHolder.getDatabase();
		if (database != null)
			result = readRequest.requestImplementation(database);

		return result;
	}

	protected synchronized <T> T write(DatabaseWriteRequest<T> writeRequest) {
		if (writeRequest == null)
			return null;

		T result = null;

		SQLiteDatabase database = DatabaseHolder.getDatabase();
		if (database != null)
			result = writeRequest.requestImplementation(database);

		return result;
	}
}
