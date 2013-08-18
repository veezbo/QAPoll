package com.anony.minions.qapoll;

import com.anony.minions.qapoll.database.DatabaseHolder;
import com.anony.minions.qapoll.database.DatabaseInitListener;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

public class SpalshScreenActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.spalsh_screen);

		initializeData();
	}

	private void initializeData() {
		new InitDataTask().execute();
	}

	/**
	 * we should initialize all the stuff here.
	 */
	private class InitDataTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Boolean doInBackground(String... params) {
			try {

				QAPollContextManager.setContext(getApplicationContext());
				DatabaseHolder.init(new DatabaseInitListener() {

					@Override
					public void onInit() {
					}

					@Override
					public void onError() {
					}
				});

				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// not really doing anything w/ the return value
			return true;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (result == true) {
				// Start KhanFragmentActivity
				Intent ide = new Intent(SpalshScreenActivity.this,
						LoginActivity.class);
				startActivity(ide);
				// finish existing activity
				finish();
			}

		}

	}

}
