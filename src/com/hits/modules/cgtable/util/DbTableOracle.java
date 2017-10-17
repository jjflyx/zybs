package com.hits.modules.cgtable.util;

import java.util.List;

import com.hits.modules.cgtable.bean.FormField;
import com.hits.modules.cgtable.bean.FormTable;
import com.hits.util.EmptyUtils;


/**
 * oracle的表工具类
 * oracle语句结尾不能使用 ;
 */
public class DbTableOracle  {
	
	/**
	 * 功能描述:获取创建表 sql
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月1日 下午4:06:28
	 * 
	 * @param table
	 * @return  create table ... 语句
	 */
	public  static String getCreateTableSql(FormTable table) {
		String sql="";
		if (EmptyUtils.isNotEmpty(table)) {
			StringBuffer sb=new StringBuffer("");
			sb.append("create table "+table.getTableName()+" (");
			
			List<FormField> fieldList=table.getColumns();
			if (fieldList.size()>0) {
				for (int i = 0; i < fieldList.size(); i++) {
					FormField field=fieldList.get(i);
//					System.out.println("字段名："+field.getFieldName()+" -- 类型："+field.getFieldType());
					//追加字段名
					sb.append(" "+field.getFieldName());
					//追加字段类型
					sb.append(" "+getTypeSql(field.getFieldType(),field.getFieldSize()));
					//追加字段是否为空
					sb.append(" "+getIsNullSql(field.getIsRequired()));
					//追加字段是否主键
					sb.append(" "+getPrimarySql(field.getIsPrimary()));
					//追加外键
					sb.append(" "+getForeignKeySql(field.getForeignkey(),field.getForeigntable()));
//					fksql=getForeignKeySql(field.getForeignkey(),field.getForeigntable());
					if ((i+1)<fieldList.size()) {
						sb.append(",");
					}
				}
				
			}
			sb.append(")");
			sql=sb.toString();
		}
		System.out.println("create Sql："+sql);
		return sql;
	}
	
	/**
	 * 功能描述:获取创建表 sql
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月1日 下午4:06:28
	 * 
	 * @param table
	 * @return  create table ... 语句
	 */
	public  static String getAlterTableForAdd(FormTable table) {
		String sql="";
		if (EmptyUtils.isNotEmpty(table)&&table.getColumns().size()>0) {
			StringBuffer sb=new StringBuffer("");
			sb.append("ALTER table "+table.getTableName()+" add (");
			
			List<FormField> fieldList=table.getColumns();
			for (int i = 0; i < fieldList.size(); i++) {
				FormField field=fieldList.get(i);
//					System.out.println("字段名："+field.getFieldName()+" -- 类型："+field.getFieldType());
				//追加字段名
				sb.append(" "+field.getFieldName());
				//追加字段类型
				sb.append(" "+getTypeSql(field.getFieldType(),field.getFieldSize()));
				//追加字段是否为空
				sb.append(" "+getIsNullSql(field.getIsRequired()));
				//追加字段是否主键
				sb.append(" "+getPrimarySql(field.getIsPrimary()));
				//追加外键
				sb.append(" "+getForeignKeySql(field.getForeignkey(),field.getForeigntable()));
//					fksql=getForeignKeySql(field.getForeignkey(),field.getForeigntable());
				if ((i+1)<fieldList.size()) {
					sb.append(",");
				}
			}
			sb.append(")");
			sql=sb.toString();
		}
		System.out.println("add Sql："+sql);
		return sql;
	}
	/**
	 * 功能描述:获取创建表 sql
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月1日 下午4:06:28
	 * 
	 * @param table
	 * @return  create table ... 语句
	 */
	public  static String getAlterTableForModify(FormTable table) {
		String sql="";
		if (EmptyUtils.isNotEmpty(table)&&table.getColumns().size()>0) {
			StringBuffer sb=new StringBuffer("");
			sb.append("ALTER table "+table.getTableName()+" MODIFY (");
			
			List<FormField> fieldList=table.getColumns();
			if (fieldList.size()>0) {
				for (int i = 0; i < fieldList.size(); i++) {
					FormField field=fieldList.get(i);
//					System.out.println("字段名："+field.getFieldName()+" -- 类型："+field.getFieldType());
					//追加字段名
					sb.append(" "+field.getFieldName());
					//追加字段类型
					sb.append(" "+getTypeSql(field.getFieldType(),field.getFieldSize()));
					//追加字段是否为空
					//sb.append(" "+getIsNullSql(field.getIsRequired()));
					if ((i+1)<fieldList.size()) {
						sb.append(",");
					}
				}
				
			}
			sb.append(")");
			sql=sb.toString();
		}
		System.out.println("MODIFY Sql："+sql);
		return sql;
	}
	
	
	/**
	 * 功能描述:拼接主键sql ( 0-否 ； 1-是)
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月1日 下午4:21:56
	 * 
	 * @return
	 */
	private static String getPrimarySql(int isPrimary) {
		String result ="";
		
		switch (isPrimary) {
		case 1:
			result=" primary key";
			break;

		default:
			result ="";
			break;
		}
		
		return result;
	}
	
	/**
	 * 功能描述:拼接外键sql 
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月1日 下午4:21:56
	 * 
	 * @return
	 */
	private static String getForeignKeySql(String foreignkey,String foreigntable) {
		String result ="";
		
		if (EmptyUtils.isNotEmpty(foreignkey)) {
//			result="constraint fk_couseid foreign key ("+foreignkey+") references t_couse ("+foreignkey+")  on delete cascade";
			result="  references "+CommonStaticUtil.TABLE_NAME_SUB+foreigntable+"("+foreignkey+") on delete cascade";
		}
		
		return result;
	}
	
	/**
	 * 功能描述:拼接字段类型和长度
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月1日 下午4:21:56
	 * 
	 * @param type
	 * @param size
	 * @return
	 */
	private static String getTypeSql(String type,int size) {
		String result ="";
		
		type=type.toUpperCase();
		
		
		if ("VARCHAR2".equals(type) || "VARCHAR".equals(type)) {
			result=" varchar2("+size+")";
		}else if("NUMBER".equals(type)){
			result=" NUMBER("+size+")";
		}else if("DATE".equals(type)){
//			result=" DATE";
			result=" varchar2(20)";//日起统一使用字符串
		}else if("DOUBLE".equals(type)){
			result=" DOUBLE";
		}else if("CLOB".equals(type)){
			result=" CLOB";
		}
		return result;
	}
	
	/**
	 * 功能描述:验证字段是否 可为空  （ 1-不为空  0-可为空）
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月1日 下午4:14:53
	 * 
	 * @return
	 */
	private static String getIsNullSql(int isRequired ){
		String sql=" ";
		switch (isRequired) {
		case 1:
			sql=" NOT NULL ";
			break;
		default:
			sql=" ";
			break;
		}
		return sql;
	}
}
