package com.anony.minions.qapoll.database;

import android.database.sqlite.SQLiteDatabase;

public interface DatabaseReadRequest<T> {
	public T requestImplementation(SQLiteDatabase database);
}
