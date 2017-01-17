package com.cosmos.workflow.test;

import com.cosmos.utils.database.CommonDBPager;

public class OracleDBPager extends CommonDBPager{
	
	private static final String TEMP_TEBLE_NAME    = "PAGER_TEMP_TABLE_9527";
	
	private static final String TEMP_TEBLE_RN_NAME = "PAGER_TEMP_TABLE_RN_9527";
	
	@Override
	public String getPageRecordSql(String sql, int pageNumber, int pageSize) {
		StringBuilder result = new StringBuilder(1024);
		result.append("SELECT * FROM (SELECT ROWNUM ")
			.append(TEMP_TEBLE_RN_NAME).append(",").append(TEMP_TEBLE_NAME)
			.append(".* FROM (")
			.append(sql.toUpperCase())
			.append(") ").append(TEMP_TEBLE_NAME).append(" WHERE ROWNUM <= ")
			.append(pageNumber * pageSize)
			.append(") WHERE ").append(TEMP_TEBLE_RN_NAME)
			.append(" > ").append(pageSize * (pageNumber - 1));
		return result.toString();
	}
}