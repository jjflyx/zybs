package com.hits.modules.cgtable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.util.StringUtil;
import com.hits.modules.cgtable.bean.FormField;
import com.hits.modules.cgtable.bean.FormTable;
import com.hits.modules.cgtable.util.CommonStaticUtil;
import com.hits.modules.cgtable.util.DbTableOracle;
import com.hits.modules.cgtable.util.GenEntityUtil;
import com.hits.modules.form.bean.Form_field;
import com.hits.modules.form.bean.Form_table;
import com.hits.util.EmptyUtils;

/**
 * 
 *  #(c) IFlytek cform <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: 创建表
 * 
 *  <br/>创建说明: 2016年2月1日 上午9:50:55 (☆笑死宝宝了☆)  创建文件<br/>
 * 
 *  修改历史:<br/>
 *
 */
@IocBean
@At("/private/createtable")
@Filters({ @By(type = GlobalsFilter.class) })
public class CreateTableAction extends BaseAction {
	
	@Inject
	protected Dao dao;
	
	/**
	 * 功能描述:生成数据库表，创建实体
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月3日 上午9:13:33
	 * 
	 * @param session
	 * @param req
	 * @return
	 */
	@At
	@Ok("raw")
	public boolean createBean(HttpSession session,final HttpServletRequest req,@Param("ids") final String ids) {
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		try {
			Trans.exec(new Atom() {
                public void run() {
                	//获取需要创建的数据信息
                	String[] idArray = StringUtil.null2String(ids).split(",");
                	for (int i = 0; i < idArray.length; i++) {
                		//form_def表的id
                		String id=StringUtil.null2String(idArray[i]);
                		
                		//获取form_table表信息
                		List<Form_table> tableList=daoCtl.list(dao, Form_table.class, Sqls.create("select * from form_table where formdefid="+id));
                		if (tableList.size()>0) {
							for (Form_table table :tableList) {
								//判断是否已经生成实体，已生成的不在执行实体生成和表创建
//								Sql checksql=Sqls.create("select count(*) from FORM_DEF where isgen=1 and defid=@defid");
//								checksql.params().set("defid", table.getFormdefid());
//								int count=daoCtl.getIntRowValue(dao, checksql);
//								if (count>=1) {
//									continue;
//								}
								
								String tableName=CommonStaticUtil.TABLE_NAME_SUB+table.getTablekey();
								
								//判断当前表在数据库中是否有生成 ，如果有删除此表重新建立
								boolean is_create=dao.exists(tableName);
								//封装创建数据库数据
								FormTable formTable=new FormTable();
								formTable.setTableName(tableName);
								FormTable modTable=new FormTable();
								modTable.setTableName(tableName);
								String sql="";
								String ssql="select column_name,data_type||'_'||data_length  from user_tab_cols where table_name='"+tableName.toUpperCase()+"' ";
								//查询字段信息
								List<Form_field> fieldList=daoCtl.list(dao, Form_field.class, Sqls.create("select * from form_field where status=0 and tableid="+table.getTableid()+" order by fieldid"));
								List<FormField> formFieldList=new ArrayList<FormField>();
								if (is_create) {//表已创建
									if (fieldList.size()>0) {
										List<FormField> formFieldList2=new ArrayList<FormField>();
										Hashtable<String, String> colMap=daoCtl.getHTable(dao, Sqls.create(ssql));
										Set<String> colSet=colMap.keySet();
										for (Form_field field :fieldList) {
											if(!colSet.contains(field.getFieldname().toUpperCase())){//字段未创建
												FormField formField=new FormField();
												formField.setFieldName(field.getFieldname());
												formField.setFieldSize(field.getFieldsize());
												formField.setFieldType(field.getFieldtype());
												formField.setIsPrimary(field.getIsprimary());
												formField.setIsRequired(field.getIsrequired());
												formField.setForeignkey(field.getForeignkey());
												formField.setForeigntable(field.getForeigntable());
												formFieldList.add(formField);
											}else{
												String[] typeAndlen=colMap.get(field.getFieldname().toUpperCase()).split("_");
												if(typeAndlen[0].startsWith(field.getFieldtype().toUpperCase())&&field.getFieldsize()>StringUtil.StringToInt(typeAndlen[1])){
													//字段长度增加
													FormField formField=new FormField();
													formField.setFieldName(field.getFieldname());
													formField.setFieldSize(field.getFieldsize());
													formField.setFieldType(field.getFieldtype());
													formField.setIsPrimary(field.getIsprimary());
													formField.setIsRequired(field.getIsrequired());
													formField.setForeignkey(field.getForeignkey());
													formField.setForeigntable(field.getForeigntable());
													formFieldList2.add(formField);
												}
											}
										}
										modTable.setColumns(formFieldList2);
										String sql2=DbTableOracle.getAlterTableForModify(modTable);
										if(EmptyUtils.isNotEmpty(sql2)){
											dao.execute(Sqls.create(sql2));
										}
									}
									formTable.setColumns(formFieldList);
									sql=DbTableOracle.getAlterTableForAdd(formTable);
								}else{
									if (fieldList.size()>0) {
										for (Form_field field :fieldList) {
											FormField formField=new FormField();
											formField.setFieldName(field.getFieldname());
											formField.setFieldSize(field.getFieldsize());
											formField.setFieldType(field.getFieldtype());
											formField.setIsPrimary(field.getIsprimary());
											formField.setIsRequired(field.getIsrequired());
											formField.setForeignkey(field.getForeignkey());
											formField.setForeigntable(field.getForeigntable());
											
											formFieldList.add(formField);
										}
									}
									formTable.setColumns(formFieldList);
									sql=DbTableOracle.getCreateTableSql(formTable);
								}
								if(EmptyUtils.isNotEmpty(sql)){
									dao.execute(Sqls.create(sql));
								}
								
								//已生成实体并发布
								Sql defSql=Sqls.create("update FORM_DEF set isgen=1,status=1 where defid=@defid");
								defSql.params().set("defid", table.getFormdefid());
								dao.execute(defSql);
								
								//生成实体类 
								
								/*String dir=req.getSession().getServletContext().getRealPath("/");
								
								dir=dir.substring(0, dir.lastIndexOf("\\"))+"\\src"+"\\";
								
								GenEntityUtil.CreateEntiy(formTable,dir);
								
								//更改表单状态为以生成实体
								Sql defSql=Sqls.create("update FORM_DEF set status=1,isgen=1 where defid=@defid");
								defSql.params().set("defid", table.getFormdefid());
								dao.execute(defSql);*/
								
								re.set(true);
							}
						}
                	}
                }
			});
		} catch (Exception e) {
			e.printStackTrace();
			re.set(false);
		}
		
		return re.get();
	}
	
	

}
