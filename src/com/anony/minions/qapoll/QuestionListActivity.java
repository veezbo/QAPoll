package com.anony.minions.qapoll;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.ListView;

import com.anony.minions.qapoll.adapters.QuestionListAdapter;
import com.anony.minions.qapoll.data.Question;
import com.anony.minions.qapoll.data.User;
import com.anony.minions.qapoll.data.Student;

public class QuestionListActivity extends Activity {
	QuestionListAdapter adapter;
	User user;
	boolean isStudent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qustion_list);
		Intent i=getIntent();
	    //identi=i.getStringExtra("Identity");
		user=(User)getIntent().getExtras().getParcelable("User");
		if(user instanceof Student){
			isStudent=true;
			
		}else{
			isStudent=false;
		}
		Question[] qs=new Question[3];// TODO  pulling the list
		adapter=new QuestionListAdapter(this, qs );
		ListView ls=(ListView)findViewById(R.id.question_list);
		ls.setAdapter(adapter);
		
		if(!isStudent){
			
			ls.setOnLongClickListener(new OnLongClickListener(){

				@Override
				public boolean onLongClick(View v) {
					//TODO delete box
					new AlertDialog.Builder(QuestionListActivity.this)
				    .setTitle("Delete This Question")
				    .setMessage("Are you sure you want to delete this question?")
				    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				            // continue with delete
				        	   //TODO delete from the values.
				        	adapter.deleteQuestion(which);
				        	dialog.dismiss();
				        }
				     })
				    .setNegativeButton("No", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				            dialog.dismiss();
				        }
				     })
				     .show();
					return false;
				}
				
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.qustion_list, menu);
		if(isStudent){
			MenuItem item = menu.findItem(R.id.start_quiz);
			item.setVisible(false);
		}else{
			MenuItem item=menu.findItem(R.id.post_question);
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

	private void startQuiz() {
		// TODO Auto-generated method stub
		
	}

	private void postQuestion() {
		// TODO Auto-generated method stub
		
	}

}
