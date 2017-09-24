package com.hits.modules.cgtable.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *  #(c) IFlytek cform <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: 表实体类
 * 
 *  <br/>创建说明: 2016年2月1日 下午3:43:27 (☆笑死宝宝了☆)  创建文件<br/>
 * 
 *  修改历史:<br/>
 *
 */
public class FormTable {
	
	private String tableName;// 表名
	
	private List<FormField> columns =new ArrayList<FormField>(); //字段列表

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<FormField> getColumns() {
		return columns;
	}

	public void setColumns(List<FormField> columns) {
		this.columns = columns;
	}
	
	
	
	

}
