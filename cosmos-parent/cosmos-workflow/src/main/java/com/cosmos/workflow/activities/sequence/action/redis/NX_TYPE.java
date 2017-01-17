package com.cosmos.workflow.activities.sequence.action.redis;

public enum NX_TYPE {
	NX("NX"),
	XX("XX"),
	NONE("NONE");
	private String contents;
	NX_TYPE(String contents){
		this.contents = contents;
	}
	public String toString(){
		return this.contents;
	}
	
	public byte[] toByteArray(){
		return this.contents.getBytes();
	}
}
