package com.anony.minions.qapoll.adapters;

import com.anony.minions.qapoll.data.Question;

public interface VoteChangeListener {
	public void onVoteChanged(Question question, boolean up);
}
