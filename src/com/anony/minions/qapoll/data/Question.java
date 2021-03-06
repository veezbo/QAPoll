package com.anony.minions.qapoll.data;

import java.util.StringTokenizer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Question extends QAObject {
	static int incr = 0;
	@JsonProperty("id")
	int id;
	@JsonProperty("votes")
	int votes;
	@JsonProperty("title")
	String title;
	@JsonProperty("text")
	private String text;

	private String ownerId;
	private boolean isChecked;

	public Question(String title, String text, String userid) {

		this.id = incr++;
		ownerId = userid;
		votes = 0;
		this.title = title;
		this.text = text;
	}

	public Question(String title, String text, String userId, int qid) {
		this.id = qid;
		ownerId = userId;
		votes = 0;
		this.title = title;
		this.text = text;
		incr++;
	}

	public void setOwnerId(String userId) {
		ownerId = userId;
	}

	public String getOwnerId() {
		return ownerId;
	}

	@JsonProperty("votes")
	public int getVotes() {
		return votes;
	}

	@JsonProperty("votes")
	public void setVotes(int votes) {
		this.votes = votes;
	}

	public void incrVote() {
		this.votes++;
	}

	@Override
	public QAObjectType getType() {
		return QAObjectType.Question;
	}

	@JsonProperty("id")
	public int getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(int newid) {
		id = newid;
	}

	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	@JsonProperty("title")
	public void setTitle(String newTitle) {
		title = newTitle;
	}

	@JsonProperty("text")
	public String getText() {
		return text;
	}

	@JsonProperty("text")
	public void setText(String text) {
		this.text = text;
	}

	public static final String QUESTION_SEPARATOR = "\r\n";

	public static String toString(Question q) {
		StringBuilder sb = new StringBuilder();
		sb.append(q.id + QUESTION_SEPARATOR);
		sb.append(q.ownerId + QUESTION_SEPARATOR);
		sb.append(q.votes + QUESTION_SEPARATOR);
		sb.append(q.title + QUESTION_SEPARATOR);
		sb.append(q.text + QUESTION_SEPARATOR);
		return sb.toString();
	}

	public static Question fromString(String s) {
		StringTokenizer t = new StringTokenizer(s, QUESTION_SEPARATOR);
		int qid = Integer.parseInt(t.nextToken());
		String oId = t.nextToken();
		int votes = Integer.parseInt(t.nextToken());
		String title = t.nextToken();
		String text = t.nextToken();

		Question q = new Question(title, text, oId, qid);
		q.setVotes(votes);
		return q;
	}

	public void decVote() {
		this.votes--;
	}

	@Override
	public boolean equals(Object o) {
		if (o != null && o instanceof Question) {
			return ((Question) o).getId() == getId();
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getId();
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}
