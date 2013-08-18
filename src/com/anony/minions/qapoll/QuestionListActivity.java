package com.anony.minions.qapoll;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.anony.minions.qapoll.adapters.QuestionListAdapter;
import com.anony.minions.qapoll.adapters.VoteChangeListener;
import com.anony.minions.qapoll.data.Question;
import com.anony.minions.qapoll.service.ChordApiService;
import com.anony.minions.qapoll.service.ChordApiService.IChordServiceListener;
import com.immersion.uhl.Launcher;
import com.samsung.chord.IChordChannel;

public class QuestionListActivity extends Activity {

	public static final String TAG = QuestionListActivity.class.getSimpleName();

	QuestionListAdapter adapter;

	public String userId;
	private String instructorNodeId;
	private String room;
	boolean isStudent;

	/**
	 * Map<UserId, QuizAnswer>
	 */
	Map<String, String> quizAnswer = new HashMap<String, String>();

	private ChordApiService mChordService = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qustion_list);

		Intent i = getIntent();
		String identity = i.getStringExtra(Constants.USER);
		if (identity.equals(Constants.STUDENT)) {
			isStudent = true;
		}
		userId = i.getStringExtra(Constants.ID);
		room = i.getStringExtra(Constants.ROOM);
		Log.i("identity", identity);

		Question[] qs = new Question[] {};// TODO pulling the list

		adapter = new QuestionListAdapter(userId, qs, new VoteChangeListener() {

			@Override
			public void onVoteChanged(Question question, boolean up) {
				if (mChordService != null) {
					mChordService.sendDataToAll(room, ("voteupdate: id:"
							+ question.getId() + " :vote:" + question
							.getVotes()).getBytes());
				}
			}
		});

		mChordService = QAPollChordManager
				.getInstance(new ChordServiceListener());

		IChordChannel channel = mChordService.joinChannel(room);
		if (channel == null) {
			CharSequence text = "join failed";
			Toast.makeText(QAPollContextManager.getContext(), text,
					Toast.LENGTH_SHORT).show();
		}

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
						final int position, long itemid) {
					Log.d("delete question", "long press");
					if (adapter.getItem(position).getOwnerId().equals(userId)) {
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
					} else {
						Toast.makeText(QuestionListActivity.this,
								"Not able to delete", Toast.LENGTH_SHORT)
								.show();
						return false;
					}
				}

			});
		}

	}

	@Override
	public void onBackPressed() {
		mChordService.leaveChannel();
		super.onBackPressed();
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
			// showQuiz("1\n2,3,4\n4\n");
			return true;
		case R.id.start_quiz:
			startQuiz();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void startQuiz() {
		String files[];
		try {
			files = getResources().getAssets().list("");
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}

		int count = 0;
		ArrayList<String> txtFileNames = new ArrayList<String>();
		for (String currFile : files) {
			if (currFile.endsWith(".txt")) {
				count++;
				txtFileNames.add(currFile);
			}
		}

		if (count == 0) {
			// no quizzes exist
			return;
		}

		// final String[] filenames=new String[] {"Turing Machine Quiz",
		// "Graph Quiz", "Greedy Algo Quiz"};
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Select A Quiz");
		final CharSequence[] filenames = (CharSequence[]) (txtFileNames
				.toArray(new String[txtFileNames.size()]));
		// builder.setI
		builder.setItems(filenames, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				Toast.makeText(getApplicationContext(), filenames[item],
						Toast.LENGTH_SHORT).show();
				// File file=new File(f.getAbsolutePath()+"/"+filenames[item]);
				// Log.i("file path", file.getPath());
				// Read text from file
				StringBuilder text = new StringBuilder();
				try {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(getAssets().open(
									filenames[item].toString()), "UTF-8"));
					String line;

					while ((line = br.readLine()) != null) {
						text.append(line);
						text.append('\n');
					}
					final String textToString = text.toString();
					new AlertDialog.Builder(QuestionListActivity.this)
							.setTitle("Quiz")
							.setMessage(text)
							.setPositiveButton("Send",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											quizAnswer.clear();
											mChordService
													.sendDataToAll(
															room,
															("quiz: text:"
																	+ textToString
																	+ " :duration:" + 10 * 1000)
																	.getBytes());
											Toast.makeText(
													getApplicationContext(),
													"broadcast to student",
													Toast.LENGTH_SHORT).show();
										}
									})
							.setNegativeButton("Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO broadcast
											Toast.makeText(
													getApplicationContext(),
													"cancel",
													Toast.LENGTH_SHORT).show();
											dialog.dismiss();
										}
									}).show();
				} catch (IOException e) {
					// You'll need to add proper error handling here
				}

			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

	private interface QuizeTimeUpListener {
		public void onQuizTimedUp();
	}

	private class VibrateTransmissionTask extends
			AsyncTask<Long, Integer, Boolean> {

		QuizeTimeUpListener mListener = null;

		VibrateTransmissionTask(QuizeTimeUpListener listener) {
			mListener = listener;
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Boolean doInBackground(Long... params) {
			try {
				Thread.sleep(params[0]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (mListener != null)
				mListener.onQuizTimedUp();
		}

	}

	public void showQuiz(String quiz, long duration) {
		String[] lines = quiz.split("\n");
		if (lines.length < 1) {
			Log.e("exception", "invalid quiz question");
			return;
		}

		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.quiz_popup_layout);
		dialog.setTitle("QUIZ");
		TextView question = (TextView) dialog.findViewById(R.id.quiz_question);
		final RadioGroup choices = (RadioGroup) dialog
				.findViewById(R.id.quiz_choices);
		question.setText(lines[0]);
		String[] multiples = lines[1].split(",");
		for (String s : multiples) {
			RadioButton button = new RadioButton(this);
			button.setText(s);
			choices.addView(button);
		}

		Button submit = (Button) dialog.findViewById(R.id.submitQuizButton);
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int id = choices.getCheckedRadioButtonId();

				if (id == -1) {

				} else {
					RadioButton selected = (RadioButton) choices
							.findViewById(id);
					String answer = selected.getText().toString();
					if (mChordService != null) {
						mChordService.sendData(room, ("useranswer: userid:"
								+ userId + " :answer:" + answer).getBytes(),
								instructorNodeId);
						HapticLauncherManager.getInstance().stop();
						dialog.dismiss();
					}
				}

			}

		});
		dialog.show();

		HapticLauncherManager.getInstance().play(Launcher.ENGINE4_100);
		new VibrateTransmissionTask(new QuizeTimeUpListener() {

			@Override
			public void onQuizTimedUp() {
				HapticLauncherManager.getInstance().stop();
				dialog.dismiss();
			}
		}).execute(duration);
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
							Question question = new Question(t, q, userId);
							adapter.addQuestion(question);
							if (mChordService != null && q != null) {

								// OutputStream os = new
								// ByteArrayOutputStream();
								// ObjectMapper mapper = new ObjectMapper();
								// try {
								// mapper.writeValue(os, question);
								// } catch (JsonGenerationException e) {
								// // TODO Auto-generated catch block
								// e.printStackTrace();
								// } catch (JsonMappingException e) {
								// // TODO Auto-generated catch block
								// e.printStackTrace();
								// } catch (IOException e) {
								// // TODO Auto-generated catch block
								// e.printStackTrace();
								// }
								// JSONObject mJson = null;
								// try {
								// mJson = new JSONObject(os.toString());
								// } catch (JSONException e) {
								// e.printStackTrace();
								// }

								mChordService.sendDataToAll(room, Question
										.toString(question).getBytes());
							}
							ad.dismiss();
						}
					}
				});
	}

	private class ChordServiceListener implements IChordServiceListener {

		@Override
		public void onReceiveMessage(String node, String channel, String message) {

			// ObjectMapper mapper = new ObjectMapper();
			// Question question = null;
			// try {
			// question = mapper.readValue(message.toString(), Question.class);
			// } catch (JsonParseException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (JsonMappingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// if (question != null) {
			// Toast.makeText(QuestionListActivity.this,
			// "Channel : " + channel + " message : " + message,
			// Toast.LENGTH_LONG).show();
			if (message.contains("useranswer:") && !isStudent) {
				String userId = "";
				String userAnswer = "";
				String[] tokens = message.split(" ");
				for (String token : tokens) {
					if (token.contains("id:")) {
						userId = token.substring(token.indexOf("id:")
								+ "id:".length());
					} else if (token.contains(":answer:")) {
						userAnswer = token.substring(token.indexOf(":answer:")
								+ ":answer:".length());
					}
					quizAnswer.put(userId, userAnswer);
				}
			} else if (message.contains("voteupdate:")) {
				int questionId = -1;
				int voteCount = 0;
				String[] tokens = message.split(" ");
				for (String token : tokens) {
					if (token.contains("id:")) {
						questionId = Integer.parseInt(token.substring(token
								.indexOf("id:") + "id:".length()));
					} else if (token.contains(":vote:")) {
						voteCount = Integer.parseInt(token.substring(token
								.indexOf(":vote:") + ":vote:".length()));
					}
				}
				if (adapter != null) {
					adapter.updateQuestionVotes(questionId, voteCount);
				}
			} else if (message.contains("instructorId:")) {
				long instructorJoinTime = 0;
				String[] tokens = message.split(" ");
				for (String token : tokens) {
					if (token.contains("instructorId:")) {
						instructorNodeId = token.substring(token
								.indexOf("instructorId:")
								+ "instructorId:".length());
					} else if (token.contains(":time:")) {
						instructorJoinTime = Long.parseLong(token
								.substring(token.indexOf(":time:")
										+ ":time:".length()));
					}
				}
				if (instructorJoinTime < System.currentTimeMillis()) {
					instructorNodeId = message.substring(message
							.indexOf("instructorId:")
							+ "instructorId:".length());
					if (!isStudent) {
						mChordService.leaveChannel();
						finish();
					}
				}
			} else if (message.contains("quiz:")) {
				String quizString = message.substring(message.indexOf("text:")
						+ "text:".length(), message.indexOf(":duration:"));
				long quizDuration = Long.parseLong(message.substring(message
						.indexOf(":duration:") + ":duration:".length()));
				showQuiz(quizString, quizDuration);
			} else {
				Question question = Question.fromString(message);
				Log.d(TAG, "Receiving question with id " + question.getId());
				Toast.makeText(getApplicationContext(),
						"Receiving question with id " + question.getId(),
						Toast.LENGTH_LONG).show();
				adapter.addQuestion(question);
			}
			// }
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
			Log.d(TAG, "onNodeEvent");
			if (channel.equals(room) && !isStudent) {
				String nodeName = mChordService.getNodeName();
				mChordService.sendData(room, ("instructorId: " + nodeName
						+ " :time:" + System.currentTimeMillis()).getBytes(),
						node);

			}
			if (bJoined && channel.equals(room) && !isStudent) {
				Toast.makeText(
						getApplicationContext(),
						"channel: " + channel + ", new node: " + node
								+ ", current node: "
								+ mChordService.getNodeName(),
						Toast.LENGTH_LONG).show();
				Log.d(TAG, "onNodeEvent joined");
				for (Question q : adapter.getAllQuestions()) {
					Log.d(TAG,
							"sending question id: " + q.getId() + ", title: "
									+ q.getTitle() + ", text: " + q.getText()
									+ ", votes: " + q.getVotes());
					mChordService.sendData(room, Question.toString(q)
							.getBytes(), node);
				}
			}
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
