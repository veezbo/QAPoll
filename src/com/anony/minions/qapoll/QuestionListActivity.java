package com.anony.minions.qapoll;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.anony.minions.qapoll.adapters.QuestionListAdapter;
import com.anony.minions.qapoll.data.Question;

public class QuestionListActivity extends Activity {
	QuestionListAdapter adapter;
	String id;
	boolean isStudent=false;
	String room;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qustion_list);
		Intent i=getIntent();
		String identity=i.getStringExtra(Constants.USER);
		if(identity.equals(Constants.STUDENT)){
			isStudent=true;
			id=i.getStringExtra(Constants.ID);
		}
		room=i.getStringExtra(Constants.ROOM);
		Log.i("identity", identity);
	    //identi=i.getStringExtra("Identity");
		/*user=(User)getIntent().getExtras().getParcelable("User");
		if(user instanceof Student){
			isStudent=true;
			
		}else{
			isStudent=false;
		}*/
		
		Question[] qs=new Question[]{new Question(3), new Question(4), new Question(5) };// TODO  pulling the list

		adapter=new QuestionListAdapter(this, qs );
	    ListView ls=(ListView)findViewById(R.id.question_list);
		ls.setAdapter(adapter);
		
		if(!isStudent){
			
			ls.setOnItemLongClickListener(new OnItemLongClickListener(){

				

				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
						final int position, long id) {
					Log.d("delete question", "long press");
					new AlertDialog.Builder(QuestionListActivity.this)
				    .setTitle("Delete This Question")
				    .setMessage("Are you sure you want to delete this question?")
				    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) { 
				            // continue with delete
				        	   //TODO delete from the values.
				        adapter.deleteQuestion(position);
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
