package com.anony.minions.qapoll.data;

public class Question extends QAObject {
	long id;
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
