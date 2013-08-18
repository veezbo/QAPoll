package com.anony.minions.qapoll.data;

public class Question extends QAObject {
	static int incr=0; 
	int id;
	int votes;
	String title;
	String text;
	private User owner;
	public Question() {}
	public Question(String title, String text){
		id=incr++;
		votes=0;
		this.title = title;
		this.text = text;
	}
	public Question(int votestest){
		id = incr++;
		votes=votestest;
		this.title = "sampleTitle";
		this.text = "sampleText";
	}
	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
	
	public void incrVote(){
		this.votes++;
	}

	@Override
	public QAObjectType getType() {
		return QAObjectType.Question;
	}
	
	public int getId(){
		return id;
	}

	public void setId(int newid){
		id=newid;
	}
	public String getText() {
		return text;
	}
	public String getTitle() {
		return title;
	}
	public User getOwner() {
		return owner;
	}
}
