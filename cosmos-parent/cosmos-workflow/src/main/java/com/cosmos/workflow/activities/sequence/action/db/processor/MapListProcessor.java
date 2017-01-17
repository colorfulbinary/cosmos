package com.cosmos.workflow.activities.sequence.action.db.processor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cosmos.utils.database.IResultProcessor;

public class MapListProcessor implements IResultProcessor<Map<Object, Object>[]>{
	public Map<Object, Object>[] process(ResultSet resultSet)
			throws SQLException {
		ResultSetMetaData metaData = resultSet.getMetaData();
		String[] columns = new String[metaData.getColumnCount()];
		for (int i = 0,len = columns.length; i < len; i++) {
			columns[i] = metaData.getColumnName(i+1);
		}
		List<Map<String, Object>> resList = new ArrayList<Map<String, Object>>(20);
		while(resultSet.next()){
			Map<String, Object> item = new HashMap<String, Object>(16);
			for (int i = 0,len = columns.length; i < len; i++) {
				Object value = resultSet.getObject(columns[i]);
				item.put(columns[i], value);
			}
			resList.add(item);
		}
		@SuppressWarnings("unchecked")
		Map<Object,Object>[] result = resList.toArray(new Map[0]);
		return result;
	}
}

