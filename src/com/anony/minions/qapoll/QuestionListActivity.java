package com.anony.minions.qapoll;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.anony.minions.qapoll.adapters.QuestionListAdapter;
import com.anony.minions.qapoll.data.Instructor;
import com.anony.minions.qapoll.data.Question;
import com.anony.minions.qapoll.data.User;
import com.anony.minions.qapoll.service.ChordApiService;
import com.anony.minions.qapoll.service.ChordApiService.ChordServiceBinder;
import com.anony.minions.qapoll.service.ChordApiService.IChordServiceListener;
import com.samsung.chord.ChordManager;
import com.samsung.chord.IChordChannel;

public class QuestionListActivity extends Activity {

	public static final String TAG = QuestionListActivity.class.getSimpleName();

	QuestionListAdapter adapter;
	private String id, room;
	boolean isStudent;

	private ChordApiService mChordService = null;

	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			ChordServiceBinder binder = (ChordServiceBinder) service;
			mChordService = binder.getService();
			try {
				mChordService.initialize(new ChordServiceListener());
				int nError = mChordService
						.start(ChordManager.INTERFACE_TYPE_WIFI);
				if (nError == 0)
					Log.d(TAG, "HI");
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
		setContentView(R.layout.activity_qustion_list);

		Intent i = getIntent();
		String identity = i.getStringExtra(Constants.USER);
		if (identity.equals(Constants.STUDENT)) {
			isStudent = true;
			id = i.getStringExtra(Constants.ID);
		}
		room = i.getStringExtra(Constants.ROOM);
		Log.i("identity", identity);

		startService();
		bindChordService();

		Question[] qs = new Question[] { new Question(3), new Question(4),
				new Question(5) };// TODO pulling the list

		adapter = new QuestionListAdapter(this, qs);

		ListView ls = (ListView) findViewById(R.id.question_list);
		ls.setAdapter(adapter);

		if (!isStudent) {

			ls.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int position, long id) {
					// TODO Auto-generated method stub
					Log.d("delete question", "long press");
					new AlertDialog.Builder(QuestionListActivity.this)
							.setTitle("Delete This Question")
							.setMessage(
									"Are you sure you want to delete this question?")
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// continue with delete
											// TODO delete from the values.
											adapter.deleteQuestion(position);
										}
									})
							.setNegativeButton("No",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();
					return false;
				}

			});
		} else {
			ls.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int position, long id) {
					Log.d("delete question", "long press");
					if ((int)id == QuestionListActivity.this.id.hashCode()) {
						new AlertDialog.Builder(QuestionListActivity.this)
								.setTitle("Delete This Question")
								.setMessage(
										"Are you sure you want to delete this question?")
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												// continue with delete
												// TODO delete from the values.
												adapter.deleteQuestion(position);
											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.dismiss();
											}
										}).show();
						return true;
					}else{
						Toast.makeText(QuestionListActivity.this,"Not able to delete" , Toast.LENGTH_SHORT).show();
					    return false;
					}
				}

			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qustion_list, menu);
		if (isStudent) {
			MenuItem item = menu.findItem(R.id.start_quiz);
			item.setVisible(false);
		} else {
			MenuItem item = menu.findItem(R.id.post_question);
			item.setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.post_question:
			postQuestion();
			return true;
		case R.id.start_quiz:
			startQuiz();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbindChordService();
		stopService();
	}

	private void startQuiz() {
		// TODO Auto-generated method stub
		Intent i = getIntent();
		String roomName = i.getExtras().getString("room");

		if (roomName != null) {
			Log.d(TAG, "roomName:" + roomName);
			IChordChannel channel = mChordService.joinChannel(roomName);
			if (channel != null) {
				Log.d(TAG, "Non null channel");
				String hello = "hello";
				mChordService.sendDataToAll(roomName, hello.getBytes());
			} else {
				CharSequence text = "null channel, no join";
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Log.e(TAG, "no room name");
		}
	}

	private void postQuestion() {
		final View viewInstructor = getLayoutInflater().inflate(
				R.layout.create_question, null);

		DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		};
		DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};

		final AlertDialog ad = new AlertDialog.Builder(
				QuestionListActivity.this).setView(viewInstructor)
				.setPositiveButton("Post Question", positiveListener)
				.setNegativeButton("Cancel", negativeListener).create();

		ad.show();

		ad.getButton(Dialog.BUTTON_POSITIVE).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (viewInstructor != null) {
							EditText newTitle = (EditText) viewInstructor
									.findViewById(R.id.add_question_title);
							String t = newTitle.getText().toString();

							EditText newQuestion = (EditText) viewInstructor
									.findViewById(R.id.add_question);
							String q = newQuestion.getText().toString();

							if (q.length() * t.length() == 0) {
								String message = getResources()
										.getString(
												R.string.st_alert_please_enter_question);
								Toast toast = Toast.makeText(
										QuestionListActivity.this, message,
										Toast.LENGTH_SHORT);
								toast.show();
								return;
							}
							// question is ready for posting
							Question question = new Question(t, q);
							adapter.addQuestion(question);
							ad.dismiss();
						}
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

	private class ChordServiceListener implements IChordServiceListener {

		@Override
		public void onReceiveMessage(String node, String channel, String message) {
			// TODO Auto-generated method stub
			Toast.makeText(QuestionListActivity.this,
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
