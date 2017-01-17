package com.cosmos.utils.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * Name      : CommonDBAccess
 * Creator   : rouies
 * Function  : 通用关系数据库操作类
 * Date      : 2016-1-18
 */
public abstract class CommonDBAccess implements IDBAccess{
	
	/*
	 * MethodName : getConnection
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 获取一个可用链接
	 * Return     : Connection
	 */
	public abstract Connection getConnection() throws SQLException;
	
	/*
	 * MethodName : closeConnection
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 关闭一个可用链接
	 * Return     : void
	 */
	public abstract void closeConnection() throws SQLException;
	
	
	/*
	 * MethodName : commit
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 数据库事务提交
	 * Return     : void
	 */
	public abstract void commit() throws SQLException;
	
	
	/*
	 * MethodName : rollback
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 数据库事务回滚
	 * Return     : void
	 */
	public abstract void rollback() throws SQLException;
	
	/*
	 * MethodName : getIdentity
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 获取一个主键
	 * Arguments  : Object flag    -> 标识，由实现类确定如何使用
	 * Return     : Object
	 */
	public abstract Object getIdentity(Object flag) throws SQLException;
	
	
	/***********************IBaseDBAccess实现 -begin**************************/
	
	private Object convert(Object par){
		Object result = null;
		if(par != null && par instanceof String){
			result = par.toString().replace("'", "''");
		} else {
			result = par;
		}
		return result;
	}
	
	protected Object parseQueryResuleType(Object instance){
		return instance;
	}
	
	protected Object parseModifyArgumentType(Object instance){
		return instance;
	}
	
	public int executeNoQuery(String sql, Object... pars)
			throws SQLException {
		int result = -1;
		PreparedStatement pstm = null;
		Connection connection = null;
		try {
			connection = this.getConnection();
			pstm = connection.prepareStatement(sql);
			for (int i = 1,len = pars.length; i <= len; i++) {
				Object arg = convert(pars[i-1]);
				arg = this.parseModifyArgumentType(arg);
				pstm.setObject(i, arg);
			}
			result = pstm.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			if(pstm != null){
				pstm.close();
			}
			pstm = null;
		}
		return result;
	}

	public <T> T executeQuery(IResultProcessor<T> processor, String sql,
			Object... pars) throws SQLException {
		T result = null;
		PreparedStatement pstm = null;
		ResultSet resultSet = null;
		Connection connection = null;
		try {
		    connection = this.getConnection();
			pstm = connection.prepareStatement(sql);
			for (int i = 1,len = pars.length; i <= len; i++) {
				Object arg = convert(pars[i-1]);
				arg = this.parseModifyArgumentType(arg);
				pstm.setObject(i, arg);
			}
			resultSet = pstm.executeQuery();
			result = processor.process(resultSet);
		} catch (SQLException e) {
			throw e;
		} finally {
			if(resultSet != null){
				resultSet.close();
			}
			if(pstm != null){
				pstm.close();
			}
			resultSet = null;
			pstm = null;
		}
		return result;
	}
	
	
	
	/***********************IBaseDBAccess实现 - end**************************/

	
	public class DataSetProcessor implements IResultProcessor<DataSet>{
		@Override
		public DataSet process(ResultSet resultSet) throws SQLException {
			DataSet set = new DataSet();
			ResultSetMetaData metaData = resultSet.getMetaData();
			String[] columns = new String[metaData.getColumnCount()];
			for (int i = 0,len = columns.length; i < len; i++) {
				columns[i] = metaData.getColumnName(i+1);
			}
			set.setColumns(columns);
			while(resultSet.next()){
				Object[] item = new Object[columns.length];
				for (int i = 0,len = item.length; i < len; i++) {
					item[i] = resultSet.getObject(i + 1);
					item[i] = CommonDBAccess.this.parseQueryResuleType(item[i]);
				}
				try {
					set.appendRow(item);
				} catch (DataSetException e) {
					e.printStackTrace();
				}
			}
			return set;
		}
		
	}
	/***********************IDBAccess实现 - begin**************************/
	public DataSet executeQuery(CommonDBPager pager,int pageNumber,int pageSize,String sql, Object...pars) throws SQLException {
		String targetSql = null;
		if(pager != null){
			targetSql = pager.getPageRecordSql(sql, pageNumber, pageSize);
		} else {
			targetSql = sql;
		}
		DataSet result = this.executeQuery(new DataSetProcessor(), targetSql, pars);
		if(pager != null){
			String countSql = pager.getRecoreCountSql(sql);
			Object count = this.get(countSql)[0];
			result.setPager(pageNumber, pageSize, (Integer)count);
		}
		return result;
	}
	
	public DataSet executeQuery(String sql, Object... pars) throws SQLException {
		return this.executeQuery(null, -1, -1,sql,pars);
	}
	

	public Object[] get(String sql, Object... pars) throws SQLException {
		Object[] result = this.executeQuery(new IResultProcessor<Object[]>() {
			public Object[] process(ResultSet resultSet)
					throws SQLException {
				Object[] item = null;
				if(resultSet.next()){
					item = new Object[resultSet.getMetaData().getColumnCount()];
					for (int i = 0,len = item.length; i < len; i++) {
						item[i] = resultSet.getObject(i + 1);
						item[i] = CommonDBAccess.this.parseQueryResuleType(item[i]);
					}
				}
				return item;
			}
		}, sql, pars);
		return result;
	}

	public void insert(String sql,DataSet ds) throws SQLException {
		PreparedStatement pstm = null;
		try {
			Connection connection = this.getConnection();
			pstm = connection.prepareStatement(sql);
			Iterator<Object[]> iterator = ds.iterator();
			while(iterator.hasNext()){
				Object[] rowData = iterator.next();
				for (int i = 1 , len = rowData.length; i <= len; i++) {
					Object arg = convert(rowData[i-1]);
					arg = this.parseModifyArgumentType(arg);
					pstm.setObject(i, arg);
				}
				pstm.addBatch();
			}
			pstm.executeBatch();
		} catch (SQLException e) {
			throw e;
		} finally {
			if(pstm!=null){
				pstm.close();
			}
			pstm = null;
		}
	}
	
	public void insert(Map<Object, Object> data,String tableName) throws SQLException{
		List<Object> pars = new ArrayList<Object>(data.size());
		StringBuilder sql = new StringBuilder("INSERT INTO ")
			.append(tableName).append("(");
		StringBuilder endSql = new StringBuilder(") VALUES (");
		Set<Object> keySet = data.keySet();
		boolean isFirst = true;
		for (Object key : keySet) {
			if(isFirst){
				isFirst = false;
			} else {
				sql.append(",");
				endSql.append(",");
			}
			sql.append(key);
			endSql.append("?");
			pars.add(data.get(key));
		}
		endSql.append(")");
		this.executeNoQuery(sql.append(endSql).toString(), pars.toArray());
	}
	
	
	public void update(Map<Object, Object> data,String tableName,String...pkName) throws SQLException{
		if(pkName.length < 1){
			throw new SQLException("更新失败，没有指定主键");
		}
		List<Object> pars = new ArrayList<Object>(data.size());
		List<Object> wherePars = new ArrayList<Object>(pkName.length);
		StringBuilder sql = new StringBuilder("UPDATE ")
			.append(tableName).append(" SET ");
		StringBuilder endSql = new StringBuilder(" WHERE 1=1");
		for (Object name : pkName) {
			endSql.append(" AND ").append(name).append(" = ?");
			wherePars.add(data.get(name));
			data.remove(name);
		}
		Set<Object> keySet = data.keySet();
		boolean isFirst = true;
		for (Object key : keySet) {
			if(isFirst){
				isFirst = false;
			} else {
				sql.append(",");
			}
			sql.append(key).append("= ?");
			pars.add(data.get(key));
		}
		pars.addAll(wherePars);
		this.executeNoQuery(sql.append(endSql).toString(), pars.toArray());
	}
	
	
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
	public void delete(Map<Object, Object> data,String tableName,String...pkName) throws SQLException{
		if(pkName.length < 1){
			throw new SQLException("删除失败，没有指定主键");
		}
		List<Object> wherePars = new ArrayList<Object>(pkName.length);
		StringBuilder sql = new StringBuilder("DELETE FROM ")
			.append(tableName).append(" WHERE 1=1 ");
		for (Object name : pkName) {
			sql.append("AND ").append(name).append(" = ? ");
			wherePars.add(data.get(name));
		}
		
		this.executeNoQuery(sql.toString(), wherePars.toArray());
	};
	/***********************IDBAccess实现 - end**************************/
}
