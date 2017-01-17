package com.cosmos.utils.database;

public abstract class CommonDBPager {
	
	public abstract String getPageRecordSql(String sql,int pageNumber,int pageSize);
	
	public String getRecoreCountSql(String sql) {
		StringBuilder result = new StringBuilder(sql.toUpperCase());
		int length = result.length();
		int index = 0;
		while(index < length){
			char ch = result.charAt(index);
			if(ch == '('){
				index = result.indexOf(")", index) + 1;
				continue;
			} else if(ch == ' '){
				if(index + 6 < length){
					String substring = result.substring(index + 1, index + 5);
					if(substring.equals("FROM ") || substring.equals("FROM(")){
						result.delete(0, index+1);
						break;
					}
				} else {
					break;
				}
			} else if(ch == 'F'){
				if(index + 5 < length){
					String substring = result.substring(index, index + 5);
					if(substring.equals("FROM ") || substring.equals("FROM(")){
						result.delete(0, index);
						break;
					}
				} else {
					break;
				}
			}
			index++;
		}
		result.insert(0, "SELECT COUNT(*) "); 
		return result.toString();
	}
}
