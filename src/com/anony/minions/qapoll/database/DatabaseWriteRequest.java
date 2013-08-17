package com.anony.minions.qapoll.database;

import android.database.sqlite.SQLiteDatabase;

public interface DatabaseWriteRequest<T> {
	public T requestImplementation(SQLiteDatabase database);
}
