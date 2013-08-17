package com.anony.minions.qapoll;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.anony.minions.qapoll.service.ChordApiService;
import com.anony.minions.qapoll.service.ChordApiService.ChordServiceBinder;
import com.anony.minions.qapoll.service.ChordApiService.IChordServiceListener;

public class LoginActivity extends Activity {

	public static final String TAG = LoginActivity.class.getSimpleName();

	// **********************************************************************
	// Using Service
	// **********************************************************************
	private ChordApiService mChordService = null;

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ChordServiceBinder binder = (ChordServiceBinder) service;
			mChordService = binder.getService();
			try {
				mChordService.initialize(new ChordServiceListener());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mChordService = null;
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		startService();
		bindChordService();

	}

	public void bindChordService() {
		if (mChordService == null) {
			Intent intent = new Intent(
					"com.samsung.chord.samples.apidemo.service.ChordApiDemoService.SERVICE_BIND");
			bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		}
	}

	private void unbindChordService() {
		if (null != mChordService) {
			unbindService(mConnection);
		}
		mChordService = null;
	}

	private void startService() {
		Intent intent = new Intent(
				"com.samsung.chord.samples.apidemo.service.ChordApiDemoService.SERVICE_START");
		startService(intent);
	}

	private void stopService() {
		Intent intent = new Intent(
				"com.samsung.chord.samples.apidemo.service.ChordApiDemoService.SERVICE_STOP");
		stopService(intent);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindChordService();
		stopService();
	}

	private class ChordServiceListener implements IChordServiceListener {

		@Override
		public void onReceiveMessage(String node, String channel, String message) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFileWillReceive(String node, String channel,
				String fileName, String exchangeId) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFileProgress(boolean bSend, String node, String channel,
				int progress, String exchangeId) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFileCompleted(int reason, String node, String channel,
				String exchangeId, String fileName) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNodeEvent(String node, String channel, boolean bJoined) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNetworkDisconnected() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onUpdateNodeInfo(String nodeName, String ipAddress) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onConnectivityChanged() {
			// TODO Auto-generated method stub

		}

	}
}
