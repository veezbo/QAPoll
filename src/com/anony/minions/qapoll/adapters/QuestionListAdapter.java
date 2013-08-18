package com.anony.minions.qapoll.adapters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.anony.minions.qapoll.QuestionListActivity;
import com.anony.minions.qapoll.R;
import com.anony.minions.qapoll.data.Question;

public class QuestionListAdapter extends BaseAdapter {
	private final Context context;
	private final ArrayList<Question> values;
	private int id;
	public static final String TAG = QuestionListAdapter.class.getSimpleName();
	
	public QuestionListAdapter(Context context, Question[] values) {
		super();
		this.context = context;
		id = ((QuestionListActivity) context).id.hashCode();
		this.values = new ArrayList<Question>(Arrays.asList(values));
		Collections.sort(this.values, new QuestionComparator());
	}

	public void deleteQuestion(int position) {
		values.remove(position);
		notifyDataSetChanged();
	}

	public void addQuestion(Question question) {
		for(Question q : values) {
			if(question.getId()==q.getId()) {
				Log.d(TAG,"Question already exists, id: "+q.getId()+", text: "+q.getText());
				if(question.getVotes()!=q.getVotes()) {
					Log.d(TAG,"Updating number of upvotes for id "+question.getId());
					q.setVotes(question.getVotes());
					Collections.sort(values, new QuestionComparator());
					notifyDataSetChanged();
				}
				return;
			}
		}
		Log.d(TAG,"adding question id: "+question.getId());
		values.add(question);
		// sort it
		Collections.sort(values, new QuestionComparator());
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.rowlayout, parent, false);

		}

		Question q = values.get(position);

		if (this.id == q.getId()) {
			rowView.setBackgroundColor(Color.parseColor("#1A000000"));
		} else {
			rowView.setBackgroundColor(Color.WHITE);
		}
		TextView numOfVotes = (TextView) rowView
				.findViewById(R.id.numer_of_votes);
		TextView rank = (TextView) rowView.findViewById(R.id.question_rank);
		TextView title = (TextView) rowView.findViewById(R.id.question_title);
		TextView preview = (TextView) rowView
				.findViewById(R.id.QuestionPreview);
		CheckBox upvote = (CheckBox) rowView.findViewById(R.id.upvote_box);
		upvote.setTag(position);
		upvote.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				if (arg0.getTag() instanceof Integer) {
					int pos = (Integer) arg0.getTag();
					Question q = values.get(pos);
					if (isChecked) {
						q.incrVote();
					} else {
						q.decVote();

					}
					// q.toggleChecked();
					Collections.sort(values, new QuestionComparator());
					QuestionListAdapter.this.notifyDataSetChanged();
				}
			}
		});

		numOfVotes.setText("" + q.getVotes());
		rank.setText("" + (position + 1));
		title.setText(q.getTitle());
		preview.setText(q.getText());

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
	public ArrayList<Question> getAllQuestions() {
		return values;
	}
	public class QuestionComparator implements Comparator<Question> {

		@Override
		public int compare(Question lhs, Question rhs) {
			if (lhs.getVotes() == rhs.getVotes()) {
				return 0;
			} else if (lhs.getVotes() < rhs.getVotes()) {
				return 1;
			} else {
				return -1;
			}
		}
	}
}