package com.cosmos.workflow.test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import com.cosmos.utils.database.CommonDBAccess;

public class OracleDBAccess extends CommonDBAccess{

	private Connection connection;
	
	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public Connection getConnection() throws SQLException {
		if(this.connection == null){
			this.connection = DriverManager.getConnection("jdbc:oracle:thin:@192.168.234.1:1521:orcl", "cosmos", "123");
		}
		return this.connection;
	}

	@Override
	public void closeConnection() throws SQLException {
		this.connection.close();
	}

	@Override
	public void commit() throws SQLException {
		this.connection.commit();
	}

	@Override
	public void rollback() throws SQLException {
		this.connection.rollback();
	}

	@Override
	public Object getIdentity(Object flag) throws SQLException {
		return UUID.randomUUID().toString();
	}
	
	@Override
	protected Object parseQueryResuleType(Object instance) {
		Object result = instance;
		if(instance instanceof BigDecimal){
			BigDecimal bd = (BigDecimal) instance;
			if(bd.scale() == 0){
				if(bd.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) != 1){
					result = bd.intValue();
				} else if(bd.compareTo(BigDecimal.valueOf(Long.MAX_VALUE)) != 1){
					result = bd.longValue();
				}
			} else {
				result = bd.doubleValue();
			}
			
		}
		return result;
	}

}