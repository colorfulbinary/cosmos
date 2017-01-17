package com.cosmos.utils.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * Name      : IResultProcessor<T>
 * Creator   : rouies
 * Function  : 用于数据库操作中处理resultset的命令模式封装接口
 * Date      : 2016-1-18
 */
public interface IResultProcessor<T> {
	
	/*
	 * MethodName : process
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 处理resultset结果，并将结果封装为对应的类型
	 * Arguments  : ResultSet resultSet -> 数据库结果集
	 * Return     : T
	 */
	public T process(ResultSet resultSet) throws SQLException;
}
