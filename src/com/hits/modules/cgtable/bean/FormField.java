package com.hits.modules.cgtable.bean;

/**
 * 
 *  #(c) IFlytek cform <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: 列 实体类
 * 
 *  <br/>创建说明: 2016年2月1日 下午3:43:53 (☆笑死宝宝了☆)  创建文件<br/>
 * 
 *  修改历史:<br/>
 *
 */
public class FormField {
	
	private  String fieldName; //字段名
	
	private  String fieldType; //字段类型
	
	private  int fieldSize; //字段长度
	
	private  int isPrimary; //是否是主键  0-否 ； 1-是 
	
	private  int isRequired; //是否为空  0-否 ； 1-是
	
	private  String foreignkey; //外键字段
	
	private  String foreigntable; //外键表名

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public int getFieldSize() {
		return fieldSize;
	}

	public void setFieldSize(int fieldSize) {
		this.fieldSize = fieldSize;
	}

	public int getIsPrimary() {
		return isPrimary;
	}

	public void setIsPrimary(int isPrimary) {
		this.isPrimary = isPrimary;
	}

	public int getIsRequired() {
		return isRequired;
	}

	public void setIsRequired(int isRequired) {
		this.isRequired = isRequired;
	}

	public String getForeignkey() {
		return foreignkey;
	}

	public void setForeignkey(String foreignkey) {
		this.foreignkey = foreignkey;
	}

	public String getForeigntable() {
		return foreigntable;
	}

	public void setForeigntable(String foreigntable) {
		this.foreigntable = foreigntable;
	}
	
	
	
	
	

}
