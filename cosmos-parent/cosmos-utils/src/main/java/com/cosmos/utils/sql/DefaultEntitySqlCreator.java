package com.cosmos.utils.sql;

import java.util.ArrayList;
import java.util.Arrays;

import com.cosmos.utils.reflect.MemberReflectUtils;
import com.cosmos.utils.reflect.ReflectException;
import com.cosmos.utils.sql.annotation.EntityBody;
import com.cosmos.utils.sql.annotation.EntityMember;
import com.cosmos.utils.text.StringUtils;

public class DefaultEntitySqlCreator implements EntitySqlCreator{
	
	private static String insertSqlBase = "INSERT INTO %s (%s) VALUES (%s)";
	
	private static String updateSqlBase = "UPDATE %s SET %s WHERE %s";
	
	private static String deleteSqlBase = "DELETE FROM %s WHERE %s";
	
	public SqlContext getInsertSqlByEntity(Object bean) throws ReflectException{
		SqlContext result = null;
		if(bean != null){
			String tableName;
			ArrayList<String> columns;
			ArrayList<Object> args;
			Class<?> clazz = bean.getClass();
			EntityBody body = clazz.getAnnotation(EntityBody.class);
			if(body == null){
				tableName = clazz.getSimpleName();
			} else {
				tableName = body.tableName();
			}
			MemberReflectUtils<Object> member = new MemberReflectUtils<Object>(bean,true,false);
			String[] fields = member.getAllFieldNames();
			int length = fields.length;
			columns = new ArrayList<String>(length);
			args = new ArrayList<Object>(length);
			for (int i=0;i<length;i++) {
				String name = fields[i];
				EntityMember fa = member.getFiledAnnotation(name, EntityMember.class);
				Object fieldValue = member.getFieldValue(name);
				if(fa == null || fa.include()){
					columns.add(fa == null ? name : fa.columnName());
					args.add(fieldValue);
				}
			}
			String[] zw = new String[length];
			Arrays.fill(zw, "?");
			String sql = String.format(insertSqlBase, tableName,StringUtils.join(columns.toArray(new String[0]), ","),StringUtils.join(zw, ","));
			result = new SqlContext();
			result.setSql(sql);
			result.setArguments(args.toArray());
		}
		return result;
	}
	
	public SqlContext getUpdateSqlByEntity(Object bean) throws ReflectException{
		SqlContext result = null;
		if(bean != null){
			String tableName;
			ArrayList<String> columns;
			ArrayList<String> pks;
			ArrayList<Object> args;
			ArrayList<Object> pkArgs;
			Class<?> clazz = bean.getClass();
			EntityBody body = clazz.getAnnotation(EntityBody.class);
			if(body == null){
				tableName = clazz.getSimpleName();
			} else {
				tableName = body.tableName();
			}
			MemberReflectUtils<Object> member = new MemberReflectUtils<Object>(bean,true,false);
			String[] fields = member.getAllFieldNames();
			int length = fields.length;
			columns = new ArrayList<String>(length);
			args = new ArrayList<Object>(length);
			pks = new ArrayList<String>(5);
			pkArgs = new ArrayList<Object>(5);
			for (int i=0;i<length;i++) {
				String name = fields[i];
				EntityMember fa = member.getFiledAnnotation(name, EntityMember.class);
				if(fa != null && !fa.include()){
					continue;
				}
				Object fieldValue = member.getFieldValue(name);
				if(fa != null && fa.isPrimaryKey()){
					pks.add((fa.columnName() == null ? name : fa.columnName()) +"=?");
					pkArgs.add(fieldValue);
				} else {
					columns.add((fa == null ? name : fa.columnName()) + "=?");
					args.add(fieldValue);
				}
			}
			if(pks.size() == 0){
				pks.add("1 = 1");
			}
			String sql = String.format(updateSqlBase, tableName,StringUtils.join(columns.toArray(new String[0]), ","),StringUtils.join(pks.toArray(new String[0]), " AND "));
			args.addAll(pkArgs);
			result = new SqlContext();
			result.setSql(sql);
			result.setArguments(args.toArray());
			for (Object string : args.toArray()) {
				System.out.println(string);
			}
		}
		return result;
	}
	
	public SqlContext getDeleteSqlByEntity(Object bean) throws ReflectException{
		SqlContext result = null;
		if(bean != null){
			String tableName;
			ArrayList<String> pks;
			ArrayList<Object> pkArgs;
			Class<?> clazz = bean.getClass();
			EntityBody body = clazz.getAnnotation(EntityBody.class);
			if(body == null){
				tableName = clazz.getSimpleName();
			} else {
				tableName = body.tableName();
			}
			MemberReflectUtils<Object> member = new MemberReflectUtils<Object>(bean,true,false);
			String[] fields = member.getAllFieldNames();
			int length = fields.length;
			pks = new ArrayList<String>(5);
			pkArgs = new ArrayList<Object>(5);
			for (int i=0;i<length;i++) {
				String name = fields[i];
				EntityMember fa = member.getFiledAnnotation(name, EntityMember.class);
				if(fa != null && !fa.include()){
					continue;
				}
				Object fieldValue = member.getFieldValue(name);
				if(fa != null && fa.isPrimaryKey()){
					pks.add((fa.columnName() == null ? name : fa.columnName()) +"=?");
					pkArgs.add(fieldValue);
				}
			}
			if(pks.size() == 0){
				pks.add("1 = 1");
			}
			String sql = String.format(deleteSqlBase, tableName,StringUtils.join(pks.toArray(new String[0]), " AND "));
			result = new SqlContext();
			result.setSql(sql);
			result.setArguments(pkArgs.toArray());
		}
		return result;
	}
	
}
