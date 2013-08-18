package com.anony.minions.qapoll;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

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

		Button instructor = (Button) findViewById(R.id.instructor_login);
		instructor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent i = new Intent(LoginActivity.this,
								QuestionListActivity.class);
						startActivity(i);
					}
				};
				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};

				AlertDialog ad = new AlertDialog.Builder(LoginActivity.this)
						.setView(
								getLayoutInflater().inflate(
										R.layout.create_room, null))
						.setPositiveButton("Create", positiveListener)
						.setNegativeButton("Cancel", negativeListener).create();

				ad.show();
			}
		});

		Button student = (Button) findViewById(R.id.student_login);
		student.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (!validateUser()) {
							// message user info invalid
						}
						if (!validateRoom()) {
							// message room invalid
						}

						// connection ready to go
						Intent i = new Intent(LoginActivity.this,
								QuestionListActivity.class);
						startActivity(i);
					}
				};
				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};

				AlertDialog ad = new AlertDialog.Builder(LoginActivity.this)
						.setView(
								getLayoutInflater().inflate(R.layout.join_room,
										null))
						.setPositiveButton("Login", positiveListener)
						.setNegativeButton("Cancel", negativeListener).create();

				ad.show();
			}
		});
	}

	public void bindChordService() {
		if (mChordService == null) {
			Intent intent = new Intent(Constants.BIND_SERVICE);
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
		Intent intent = new Intent(Constants.START_SERVICE);
		startService(intent);
	}

	private void stopService() {
		Intent intent = new Intent(Constants.STOP_SERVICE);
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

	private boolean validateUser() {
		return false;
	}

	private boolean validateRoom() {
		return false;
	}
}
