package com.anony.minions.qapoll;

import com.anony.minions.qapoll.data.Instructor;
import com.anony.minions.qapoll.data.Student;
import com.anony.minions.qapoll.data.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.Toast;
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
		instructor.setOnClickListener( new OnClickListener() {

			View viewInstructor = getLayoutInflater().inflate(R.layout.create_room, null);

			@Override
			public void onClick(View arg0) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// overridden later
					}
				};
				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};

				final AlertDialog ad = new AlertDialog.Builder( LoginActivity.this )
				.setView(viewInstructor)
				.setPositiveButton( "Create", positiveListener )
				.setNegativeButton( "Cancel", negativeListener )
				.create();

				ad.show();

				ad.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener( new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(viewInstructor != null) {
							EditText room = (EditText) viewInstructor.findViewById(R.id.instructor_new_room);
							String r = room.getText().toString();

							if( r.length() == 0 ) {
								String message = "Please enter a room name.";
								Toast t = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT);
								t.show();
								return;
							}
							if( !roomAvailable(r) ) {
								String message = "Room name taken. Please choose another.";
								Toast t = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT);
								t.show();
								return;
							}
							if( !validateUser() ) {
								// message user info invalid
								return;
							}
							if( !validateRoom() ) {
								// message room invalid
								return;
							}

							// connection is ready to go
							ad.dismiss();
							User newUser = new Instructor();
							newUser.setId("instructor");

							Intent i = new Intent( LoginActivity.this, QuestionListActivity.class );
							i.putExtra("user", "instructor");
							i.putExtra("room", r);
							startActivity(i);
						}
					}
				});
			}
		} );

		Button student = (Button) findViewById(R.id.student_login);
		student.setOnClickListener( new OnClickListener() {

			View viewStudent = getLayoutInflater().inflate(R.layout.join_room, null);

			@Override
			public void onClick(View arg0) {
				DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// overridden later
					}
				};
				DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				};

				final AlertDialog ad = new AlertDialog.Builder( LoginActivity.this )
				.setView(viewStudent)
				.setPositiveButton( "Login", positiveListener )
				.setNegativeButton( "Cancel", negativeListener )
				.create();

				ad.show();

				ad.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener( new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if( viewStudent != null ) {
							EditText user = (EditText) viewStudent.findViewById(R.id.username);
							EditText pass = (EditText) viewStudent.findViewById(R.id.password);
							EditText room = (EditText) viewStudent.findViewById(R.id.room);

							String u = user.getText().toString();
							String p = pass.getText().toString();
							String r = room.getText().toString();

							if( !formsFilled( u, p, r ) ) {
								String message = "Please complete each field before continuing.";
								Toast t = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT);
								t.show();
								return;
							}
							if( !validateUser() ) {
								// message user info invalid
								return;
							}
							if( !validateRoom() ) {
								// message room invalid
								return;
							}

							// connection is ready to go
							ad.dismiss();
							User newUser = new Student();
							newUser.setId(u);

							Intent i = new Intent( LoginActivity.this, QuestionListActivity.class );
							i.putExtra("user", "student");
							i.putExtra("id", u);
							i.putExtra("room", r);
							startActivity(i);
						}
					}
				});

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
		return true;
	}

	private boolean validateRoom() {
		return true;
	}

	private boolean formsFilled( String user, String pass, String room ) {
		return !( user.length() * pass.length() * room.length() == 0 );
	}

	private boolean roomAvailable( String roomName ) {
		return true;
	}
}
