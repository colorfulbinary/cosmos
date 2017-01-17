package com.cosmos.utils.database;

import java.sql.SQLException;

/*
 * Name      : IBaseDBAcccess
 * Creator   : rouies
 * Function  : 基础数据库访问操作功能声明
 * Date      : 2016-1-18
 */
public interface IBaseDBAcccess {
	
	/*
	 * MethodName : executeNoQuery
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 执行sql非查询命令，并返回影响行数
	 * Arguments  : String sql    -> 要执行的查询命令
	 *              Object...pars -> 命令中使用的参数
	 * Return     : int
	 */
	public int executeNoQuery(String sql,Object...pars) throws SQLException;
	
	
	/*
	 * MethodName : executeQuery
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 执行sql查询命令，并将指定的处理结果返回
	 * Arguments  : IResultProcessor<T> processor -> 结果处理的实现类
	 * 				String sql   				  -> 要执行的查询命令
	 *              Object...pars 				  -> 命令中使用的参数
	 * Return     : T
	 */
	public <T> T executeQuery(IResultProcessor<T> processor,String sql,Object...pars) throws SQLException;
	
}
