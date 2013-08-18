package com.anony.minions.qapoll.data;

public class Question extends QAObject {
	long id;
	int votes;
	
	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}

	@Override
	public QAObjectType getType() {
		return QAObjectType.Question;
	}
	
	public long getId(){
		return id;
	}

	public void setId(long newid){
		id=newid;
	}
}
