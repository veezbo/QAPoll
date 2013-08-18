package com.anony.minions.qapoll;

import android.content.Context;

public class QAPollContextManager {

	private static Context mContext = null;

	/**
	 * 
	 * @return the application context
	 */
	public static Context getContext() {
		return mContext;
	}

	/**
	 * call this method on app startup and send application context </br> don't
	 * send activity since it might cause memory leak.
	 * 
	 * @param context
	 */
	public static void setContext(Context context) {
		mContext = context;
	}
}
