package com.cosmos.workflow.activities.sequence.action.db.processor;

import java.util.Map;

public class QueryResult {
	
	private int count;
	
	private Map<Object, Object>[] records;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Map<Object, Object>[] getRecords() {
		return records;
	}

	public void setRecords(Map<Object, Object>[] records) {
		this.records = records;
	}
}
