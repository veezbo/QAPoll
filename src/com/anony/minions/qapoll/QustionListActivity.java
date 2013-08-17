package com.anony.minions.qapoll;

import com.anony.minions.qapoll.adapters.QuestionListAdapter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class QustionListActivity extends Activity {
	QuestionListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qustion_list);
		//adapter=new QuestionListAdapter();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qustion_list, menu);
		return true;
	}

}
