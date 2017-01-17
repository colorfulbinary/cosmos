package com.cosmos.utils.sql;

import com.cosmos.utils.reflect.ReflectException;

public interface EntitySqlCreator {
	
	public static class SqlContext{
		private String sql;
		private Object[] arguments;
		public String getSql() {
			return sql;
		}
		public void setSql(String sql) {
			this.sql = sql;
		}
		public Object[] getArguments() {
			return arguments;
		}
		public void setArguments(Object[] arguments) {
			this.arguments = arguments;
		}
		
	}
	
	public SqlContext getInsertSqlByEntity(Object bean) throws ReflectException;
	
	public SqlContext getUpdateSqlByEntity(Object bean) throws ReflectException;
	
	public SqlContext getDeleteSqlByEntity(Object bean) throws ReflectException;
}
