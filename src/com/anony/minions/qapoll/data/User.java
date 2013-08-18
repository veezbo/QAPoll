package com.anony.minions.qapoll.data;

public abstract class User {

	private String _id;
	

	public String getId() {
		return this._id;
	}

	public void setId(String newId) {
		this._id = newId;
	}

	public abstract boolean hasPermission(Permission permission);
}
