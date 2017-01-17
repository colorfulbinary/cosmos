package com.cosmos.utils.database;

import java.sql.SQLException;
import java.util.Map;

/*
 * Name      : IDBAccess
 * Creator   : rouies
 * Function  : 进阶版数据库访问操作功能声明
 * Date      : 2016-1-18
 */
public interface IDBAccess extends IBaseDBAcccess {
	
	/*
	 * MethodName : executeQuery
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 执行sql查询命令，并将结果查询结果封装到DataSet对象中
	 * Arguments  : String sql    -> 要执行的查询命令
	 * 			  : CommonDBPager pager -> 分页器实例
	 *              Object...pars -> 查询命令中使用的参数
	 * Return     : DataSet
	 */
	public DataSet executeQuery(CommonDBPager pager,int pageNumber,int pageSize,String sql, Object...pars) throws SQLException;
	
	/*
	 * MethodName : executeQuery
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 执行sql查询命令，并将结果查询结果封装到DataSet对象中
	 * Arguments  : String sql    -> 要执行的查询命令
	 *              Object...pars -> 查询命令中使用的参数
	 * Return     : DataSet
	 */
	public DataSet executeQuery(String sql,Object...pars) throws SQLException;
	
	/*
	 * MethodName : get
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 执行sql查询命令，并获取第一行数据作为Object数组
	 * Arguments  : String sql    -> 要执行的查询命令
	 *              Object...pars -> 查询命令中使用的参数
	 * Return     : Object[]
	 */
	public Object[] get(String sql,Object...pars) throws SQLException;
	
	
	/*
	 * MethodName : insert
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 批量插入数据
	 * Arguments  : String sql -> 要执行的插入命令
	 *              DataSet ds -> 要插入数据的内容
	 * Return     : void
	 */
	public void insert(String sql,DataSet ds) throws SQLException;
	
	
	/*
	 * MethodName : insert
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 根据map字段对应插入数据库
	 * Arguments  : Map<Object, Object> data -> 数据集
	 *              String tableName -> 表名
	 * Return     : void
	 */
	public void insert(Map<Object, Object> data,String tableName) throws SQLException;
	
	
	/*
	 * MethodName : update
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 根据map字段对应更新数据库
	 * Arguments  : Map<Object, Object> data -> 数据集
	 *              String tableName -> 表名
	 *              String...pkName ->  主键
	 * Return     : void
	 */
	public void update(Map<Object, Object> data,String tableName,String...pkName) throws SQLException;
	
	
	/*
	 * MethodName : delete
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 根据map字段对应删除数据库
	 * Arguments  : Map<Object, Object> data -> 数据集
	 *              String tableName -> 表名
	 *              String...pkName ->  主键
	 * Return     : void
	 */
	public void delete(Map<Object, Object> data,String tableName,String...pkName) throws SQLException;
	
}
