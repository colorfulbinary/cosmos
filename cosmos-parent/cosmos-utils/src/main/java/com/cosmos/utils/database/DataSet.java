package com.cosmos.utils.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
/*
 * Name      : DataSet
 * Creator   : rouies
 * Function  : 一个数据集，用于保存一组数据，并对这些数据进行一些提取或插入操作
 * Date      : 2016-1-18
 */
public class DataSet {
	
	public class Pager{
		
		private int pageNumber;
		
		private int pageSize;
		
		private int pageCount;
		
		public int getPageNumber() {
			return pageNumber;
		}
		
		public int getPageSize() {
			return pageSize;
		}
		
		public int getPageCount() {
			return pageCount;
		}
	}
	
	private Pager pager;
	
	public void setPager(int pageNumber,int pageSize,int pageCount){
		this.pager = new Pager();
		this.pager.pageNumber = pageNumber;
		this.pager.pageSize = pageSize;
		this.pager.pageCount = pageCount;
	}
	
	public Pager getPager(){
		return this.pager;
	}
	
	//存储列信息
	private String[] columns = new String[0];

	//用于存放数据的二维表格容器
	private List<Object[]> data = new ArrayList<Object[]>(15);
	
	private String tableName;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/*
	 * MethodName : setColumns
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 设置该数据集的列信息
	 * Arguments  : String[] colums    -> 列集合信息
	 * Return     : void
	 */
	public void setColumns(String[] columns) {
		this.columns = columns;
	}
	
	/*
	 * MethodName : getColumn
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 获取指定索引列的名称
	 * Arguments  : int index -> 要获取列的索引号
	 * Return     : String
	 */
	public String getColumn(int index) {
		String result = null;
		if(index >= 0 && index < columns.length){
			result = columns[index];
		} 
		return result;
	}
	
	/*
	 * MethodName : getColumnCount
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 获取列的总数
	 * Return     : int
	 */
	public int getColumnCount(){
		return this.columns.length;
	}
	
	/*
	 * MethodName : getRowCount
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 获取行的总数
	 * Return     : int
	 */
	public int getRowCount(){
		return this.data.size();
	}
	
	
	/*
	 * MethodName : appendRow
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 追加一行数据
	 * Arguments  : Object[] data -> 要添加的行的信息
	 * Return     : void
	 */
	public void appendRow(Object[] data) throws DataSetException{
		if(data.length == columns.length){
			this.data.add(data);
		} else {
			throw new DataSetException("添加行的列数与实际列数不匹配!");
		}
	}
	
	/*
	 * MethodName : get
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 获取指定行列中单元格中的数据
	 * Arguments  : int row    -> 行号
	 * 				int column -> 列号
	 * Return     : Object
	 */
	public Object get(int row,int column) throws DataSetException{
		if(row >= 0 && row < this.data.size() && column >=0 && column < this.columns.length){
			return this.data.get(row)[column];
		} else {
			throw new DataSetException("索引越界!");
		}
		
	}
	
	/*
	 * MethodName : set
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 设置指定行列中单元格中的数据
	 * Arguments  : int row    -> 行号
	 * 				int column -> 列号
	 * Return     : void
	 */
	public void set(int row,int column,Object value) throws DataSetException{
		if(row >= 0 && row < this.data.size() && column >=0 && column < this.columns.length){
			this.data.get(row)[column] = value;
		} else {
			throw new DataSetException("索引越界!");
		}
	}
	
	/*
	 * MethodName : removeRow
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 移除指定行号的行内容
	 * Arguments  : int row    -> 行号
	 * Return     : void
	 */
	public void removeRow(int row){
		this.data.remove(row);
	}
	
	/*
	 * MethodName : iterator
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 获取所有数据内容的迭代器
	 * Return     : Iterator<Object[]>
	 */
	public Iterator<Object[]> iterator(){
		return new DataSetIterator(this.data);
	}
	
	/*
	 * MethodName : toArray
	 * MethodType : instance
	 * Creator    : rouies
	 * Function   : 将所有数据按照二维数组方式返回
	 * Return     : Object[][]
	 */
	public Object[][] toArray(){
		Object[][] result = new Object[this.data.size()][];
		for (int i = 0,len = data.size(); i < len; i++) {
			result[i] = this.data.get(i);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Object, Object>[] toMapArray(){
		Map<Object, Object>[] result = new Map[this.data.size()];
		for (int i = 0,len = data.size(); i < len; i++) {
			Map<Object, Object> item = new HashMap<Object, Object>();
			for(int j=0,jlen = this.columns.length;j < jlen ;j++){
				item.put(this.getColumn(j), this.data.get(i)[j]);
			}
			result[i] = item;
		}
		return result;
	}
	
	/*
	 * Name      : DataSetIterator
	 * Creator   : rouies
	 * Function  : 内部类 DataSet迭代器实现
	 * Date      : 2016-1-18
	 */
	private class DataSetIterator implements Iterator<Object[]>{
		
		private int index = 0;
		
		private List<Object[]> data = null;
		
		public DataSetIterator(List<Object[]> data){
			this.data = data;
		}
		
		public boolean hasNext() {
			return index < this.data.size();
		}

		public Object[] next() {
			if(index < this.data.size()){
				return this.data.get(index++);
			} else {
				return null;
			}
		}
		
	}
	
}
