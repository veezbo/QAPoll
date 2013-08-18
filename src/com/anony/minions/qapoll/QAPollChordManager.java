package com.anony.minions.qapoll;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.anony.minions.qapoll.service.ChordApiService;
import com.anony.minions.qapoll.service.ChordApiService.ChordServiceBinder;
import com.anony.minions.qapoll.service.ChordApiService.IChordServiceListener;
import com.samsung.chord.ChordManager;

public class QAPollChordManager {

	private static ChordApiService mInstance;

	private static ServiceConnection mConnection = null;

	public static ChordApiService getInstance(
			final IChordServiceListener listener) {

		if (mInstance != null) {
			mInstance.setListener(listener);
			return mInstance;
		} else {
			mConnection = new ServiceConnection() {

				@Override
				public void onServiceConnected(ComponentName name,
						IBinder service) {
					ChordServiceBinder binder = (ChordServiceBinder) service;
					mInstance = binder.getService();
					try {
						mInstance.initialize(listener);
						int nError = mInstance
								.start(ChordManager.INTERFACE_TYPE_WIFI);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onServiceDisconnected(ComponentName name) {
					mInstance = null;
				}
			};

			startService();
			bindChordService();
		}
		return mInstance;

	}

	public static void bindChordService() {
		if (mInstance == null) {
			Intent intent = new Intent(Constants.BIND_SERVICE);
			QAPollContextManager.getContext().bindService(intent, mConnection,
					Context.BIND_AUTO_CREATE);
		}
	}

	private static void unbindChordService() {
		if (null != mInstance) {
			QAPollContextManager.getContext().unbindService(mConnection);
		}
		mInstance = null;
	}

	private static void startService() {
		Intent intent = new Intent(Constants.START_SERVICE);
		QAPollContextManager.getContext().startService(intent);
	}

	private static void stopService() {
		Intent intent = new Intent(Constants.STOP_SERVICE);
		QAPollContextManager.getContext().stopService(intent);
	}
}
