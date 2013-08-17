package com.anony.minions.qapoll.adapters;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.anony.minions.qapoll.R;
import com.anony.minions.qapoll.data.Question;

public class QuestionListAdapter extends ArrayAdapter<Question> {
	private final Context context;
	private final ArrayList<Question> values;

	public QuestionListAdapter(Context context, Question[] values) {
		super(context, R.layout.rowlayout, values);
		this.context = context;
		this.values = new ArrayList<Question>(Arrays.asList(values));
	}

	public void deleteQuestion(int position) {
		values.remove(position);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = convertView;
		if (rowView == null) {
			inflater.inflate(R.layout.rowlayout, parent, false);

		}
		TextView numOfVotes = (TextView) parent
				.findViewById(R.id.numer_of_votes);
		TextView rank = (TextView) parent.findViewById(R.id.question_rank);
		TextView title = (TextView) parent.findViewById(R.id.question_title);
		TextView preview = (TextView) parent.findViewById(R.id.QuestionPreview);
		//TODO hardcode for now
		numOfVotes.setText("100");
		rank.setText("1");
		title.setText("What is UDP???");
		preview.setText("More Detailed Explanation");

		return rowView;
	}
}