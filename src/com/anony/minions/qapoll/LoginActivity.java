package com.anony.minions.qapoll;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anony.minions.qapoll.data.Instructor;
import com.anony.minions.qapoll.data.Student;
import com.anony.minions.qapoll.data.User;
import com.anony.minions.qapoll.service.ChordApiService.IChordServiceListener;

public class LoginActivity extends Activity {

	public static final String TAG = LoginActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		QAPollChordManager.getInstance(new IChordServiceListener() {
			
			@Override
			public void onUpdateNodeInfo(String nodeName, String ipAddress) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onReceiveMessage(String node, String channel, String message) {
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
			public void onFileWillReceive(String node, String channel, String fileName,
					String exchangeId) {
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
			public void onConnectivityChanged() {
				// TODO Auto-generated method stub
				
			}
		});
		
		Button instructor = (Button) findViewById(R.id.instructor_login);
		instructor.setOnClickListener(new OnClickListener() {

			View viewInstructor = getLayoutInflater().inflate(
					R.layout.create_room, null);

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

				final AlertDialog ad = new AlertDialog.Builder(
						LoginActivity.this).setView(viewInstructor)
						.setPositiveButton("Create", positiveListener)
						.setNegativeButton("Cancel", negativeListener).create();

				ad.show();

				ad.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (viewInstructor != null) {
									EditText room = (EditText) viewInstructor
											.findViewById(R.id.instructor_new_room);
									String r = room.getText().toString();

									if( r.length() == 0 ) {
										String message = getResources().getString(R.string.in_alert_please_enter_room);
										Toast t = Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT);
										t.show();
										return;
									}
									if( !roomAvailable(r) ) {
										String message = getResources().getString(R.string.in_alert_room_taken);
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
									newUser.setId(Constants.INSTRUCTOR);

									Intent i = new Intent( LoginActivity.this, QuestionListActivity.class );
									i.putExtra(Constants.USER, Constants.INSTRUCTOR);
									i.putExtra(Constants.ROOM, r);
									startActivity(i);
								}
							}
						});
			}
		});

		Button student = (Button) findViewById(R.id.student_login);
		student.setOnClickListener(new OnClickListener() {

			View viewStudent = getLayoutInflater().inflate(R.layout.join_room,
					null);

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
				.setPositiveButton( getResources().getString(R.string.in_alert_login), positiveListener )
				.setNegativeButton( getResources().getString(R.string.alert_cancel), negativeListener )
				.create();

				ad.show();

				ad.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(
						new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								if (viewStudent != null) {
									EditText user = (EditText) viewStudent
											.findViewById(R.id.username);
									EditText pass = (EditText) viewStudent
											.findViewById(R.id.password);
									EditText room = (EditText) viewStudent
											.findViewById(R.id.room);

									String u = user.getText().toString();
									String p = pass.getText().toString();
									String r = room.getText().toString();

									if( !formsFilled( u, p, r ) ) {
										String message = getResources().getString(R.string.st_alert_complete_fields);
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
									i.putExtra(Constants.USER, Constants.STUDENT);
									i.putExtra(Constants.ID, u);
									i.putExtra(Constants.ROOM, r);
									startActivity(i);
								}
							}
						});

			}
		});
	}

	private class ChordServiceListener implements IChordServiceListener {

		@Override
		public void onReceiveMessage(String node, String channel, String message) {
			// TODO Auto-generated method stub
			Toast.makeText(LoginActivity.this,
					"Channel : " + channel + " message : " + message,
					Toast.LENGTH_LONG).show();
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
//			Toast.makeText(LoginActivity.this,
//					"Channel : " + channel + " node : " + node,
//					Toast.LENGTH_LONG).show();
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

	private boolean formsFilled(String user, String pass, String room) {
		return !(user.length() * pass.length() * room.length() == 0);
	}

	private boolean roomAvailable(String roomName) {
		return true;
	}
}
