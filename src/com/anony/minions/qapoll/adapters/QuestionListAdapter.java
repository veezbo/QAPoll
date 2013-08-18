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
import android.widget.TextView;

import com.anony.minions.qapoll.QAPollContextManager;
import com.anony.minions.qapoll.R;
import com.anony.minions.qapoll.data.Question;

public class QuestionListAdapter extends BaseAdapter {
	private final ArrayList<Question> values;
	public static final String TAG = QuestionListAdapter.class.getSimpleName();
	private String id;
	VoteChangeListener mListener;

	public QuestionListAdapter(String userId, Question[] values,
			VoteChangeListener listener) {
		super();
		id = userId;
		this.values = new ArrayList<Question>(Arrays.asList(values));
		Collections.sort(this.values, new QuestionComparator());
		mListener = listener;
	}

	public void deleteQuestion(int position) {
		values.remove(position);
		notifyDataSetChanged();
	}

	public void updateQuestionVotes(int questionId, int newVotes) {
		for (Question q : values) {
			if (q.getId() == questionId) {
				q.setVotes(newVotes);
				Collections.sort(values, new QuestionComparator());
				notifyDataSetChanged();
			}
		}
	}

	public void addQuestion(Question question) {
		int index = values.indexOf(question);
		if (index != -1) {
			Question q = values.get(index);
			Log.d(TAG, "Question already exists, id: " + question.getId()
					+ ", text: " + q.getText());
			if (question.getVotes() != q.getVotes()) {
				Log.d(TAG,
						"Updating number of upvotes for id " + question.getId());
				q.setVotes(question.getVotes());
				Collections.sort(values, new QuestionComparator());
				notifyDataSetChanged();
			}
			return;
		}
		Log.d(TAG, "adding question id: " + question.getId());
		values.add(question);
		// sort it
		Collections.sort(values, new QuestionComparator());
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) QAPollContextManager
				.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = convertView;
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.rowlayout, parent, false);

		}

		Question q = values.get(position);

		if (this.id.equals(q.getOwnerId())) {
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
		final CheckBox upvote = (CheckBox) rowView
				.findViewById(R.id.upvote_box);
		upvote.setTag(position);
		upvote.setChecked(q.isChecked());
		upvote.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getTag() instanceof Integer) {
					int pos = (Integer) v.getTag();
					Question q = values.get(pos);
					if (q.isChecked()) {
						q.decVote();
						if (mListener != null)
							mListener.onVoteChanged(q, false);
					} else {
						q.incrVote();
						if (mListener != null)
							mListener.onVoteChanged(q, true);
					}

					q.setChecked(!q.isChecked());
					((CheckBox) v).setChecked(q.isChecked());
					// q.toggleChecked();
					Collections.sort(values, new QuestionComparator());
					// upvote.setTag(values.indexOf(q));
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