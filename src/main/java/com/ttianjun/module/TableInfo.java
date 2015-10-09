package com.ttianjun.module;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class TableInfo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static TableInfo dao = new TableInfo();
	public List<Record> getTableInfo(String tableName){
		String sql="select  column_name as name, column_comment as comment from information_schema.columns where table_schema ='web'  and table_name = ? ";
		return Db.find(sql, tableName);
	}
	
}
