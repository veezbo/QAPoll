package com.anony.minions.qapoll.data;

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

	
	private User owner;
	
	public Question() {
	}

	public Question(String title, String text, String id) {

		this.id = id.hashCode();
		votes = 0;
		this.title = title;
		this.text = text;
	}

	public Question(String title, String text) {
		id = incr++;
		votes = 0;
		this.title = title;
		this.setText(text);
	}

	public Question(int votestest) {
		id = incr++;
		votes = votestest;
		this.title = "sampleTitle";
		this.setText("sampleText");
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
	
	public final static String STRING_CONVERSION_INDICATOR = "QUESTION_TO_STRING";
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(STRING_CONVERSION_INDICATOR);
		return "";
	}
	public void decVote() {
		this.votes--;
	}
	
	
}
