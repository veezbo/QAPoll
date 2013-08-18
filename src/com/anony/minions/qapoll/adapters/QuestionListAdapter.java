package com.anony.minions.qapoll.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anony.minions.qapoll.R;
import com.anony.minions.qapoll.data.Question;

public class QuestionListAdapter extends BaseAdapter {
	private final Context context;
	private final ArrayList<Question> values;

	public QuestionListAdapter(Context context, Question[] values) {
		super();
		this.context = context;
		this.values = new ArrayList<Question>(Arrays.asList(values));
		Collections.sort(this.values, new QuestionComparator());
	}

	public void deleteQuestion(int position) {
		values.remove(position);
		notifyDataSetChanged();
	}
	public void addQuestion(Question q){
		values.add(q);
		//sort it
		Collections.sort(values, new QuestionComparator());
		notifyDataSetChanged();
	}

	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = convertView;
		if (rowView == null) {
			rowView=inflater.inflate(R.layout.rowlayout, parent, false);

		}
		final Question q=values.get(position);
		TextView numOfVotes = (TextView) rowView
				.findViewById(R.id.numer_of_votes);
		TextView rank = (TextView) rowView.findViewById(R.id.question_rank);
		TextView title = (TextView) rowView.findViewById(R.id.question_title);
		TextView preview = (TextView) rowView.findViewById(R.id.QuestionPreview);
		ImageView upvote=(ImageView)rowView.findViewById(R.id.imageButton1);
		upvote.setContentDescription(""+position);
		upvote.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View view) {
				//Toast.makeText(context, "ImageView clicked for the row = "+view.getContentDescription(), Toast.LENGTH_SHORT).show();
	
				
				q.incrVote();
				Collections.sort(values, new QuestionComparator());
				QuestionListAdapter.this.notifyDataSetChanged();
				
			}
			
		});
		//TODO hardcode for now
		numOfVotes.setText(""+q.getVotes());
		rank.setText(""+(position+1));
		title.setText("What is UDP???");
		preview.setText("More Detailed Explanation");

		return rowView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return values.size();
	}

	@Override
	public Question getItem(int position) {
		// TODO Auto-generated method stub
		return values.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO change it to real id later
		return values.get(position).getId();
	}
	public class QuestionComparator implements Comparator<Question> {
	  

		@Override
		public int compare(Question lhs, Question rhs) {
			if(lhs.getVotes() == rhs.getVotes()){
			return 0;
			}else if(lhs.getVotes() < rhs.getVotes()){
				return 1;
			}else{
				return -1;
			}
		}
	}
}