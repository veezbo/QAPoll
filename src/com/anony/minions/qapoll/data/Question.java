package com.anony.minions.qapoll.data;

public class Question extends QAObject {
	static int incr=0; 
	int id;
	int votes;
	public Question(){
		id=incr++;
		votes=0;
	}
	public Question(int votestest){
		votes=votestest;
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
}
