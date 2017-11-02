package com.hits.modules.form;


import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
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
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.StringUtil;
import com.hits.modules.com.YWCL;
import com.hits.modules.com.comUtil;
import com.hits.modules.form.bean.Form_def;
import com.hits.modules.form.bean.Form_field;
import com.hits.modules.form.bean.Form_query;
import com.hits.modules.form.bean.Form_table;
import com.hits.modules.form.bean.ParseReult;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.DateUtil;
import com.hits.util.Decode64Util;
import com.hits.util.EmptyUtils;

/**
 * 
 *  #(c) IFlytek cform <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: 表单管理类
 * 
 *  修改历史:<br/>
 *
 */
@IocBean
@At("/private/form")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class FormAction extends BaseAction {
	public static Hashtable<String, Form_def> formdefMap = new Hashtable<String, Form_def>();//下一步时自定义表单信息存放在内存中
	public static String TABLE_PRE_NAME = "WF_";
	
	@Inject
	protected Dao dao;

	@At
	@Ok("->:/private/form/formDefList.html")
	public void tolist(HttpSession session,HttpServletRequest req) {
		req.setAttribute("csvalueList", comUtil.sxxwtypeList);
	}
	
	/**
	 * 功能描述:表单管理新增页面
	 *
	 *
	 */
	@At
	@Ok("->:/private/form/formDesign.html")
	public void toadd(HttpServletRequest req,@Param("formdesid") String formdesid
			,@Param("tableName") String tableName,@Param("tableKey") String tableKey) {
		try {
			if(EmptyUtils.isNotEmpty(formdesid)){
				Form_def formdef=formdefMap.get(formdesid);
				req.setAttribute("def", formdef);
				req.setAttribute("formdesid", formdesid);
				req.setAttribute("tableName", tableName);
				req.setAttribute("tableKey", tableKey);
			}
			req.setAttribute("csvalueList", comUtil.sxxwtypeList);
			req.setAttribute("ywMap", comUtil.formywMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@At
	@Ok("->:/private/form/userselector.html")
	public void userselector(HttpServletRequest req,@Param("singleSelect") int singleSelect) {
		try {
			if(singleSelect==1){
				req.setAttribute("singleSelect", "true");
			}else{
				req.setAttribute("singleSelect", "false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 功能描述:表单管理新增页面
	 *
	 *
	 */
	@At
	@Ok("->:/private/form/formTableDesign.html")
	public void toTableDedign(HttpServletRequest req,@Param("xyml") String xyml,@Param("ywtype") String ywtype,
			@Param("formtitle") String formtitle,@Param("formdes") String formdes,@Param("table_type") String table_type,
			@Param("formtype") int formtype,@Param("formhtml") String formhtml,@Param("formdesid") String formdesid
			,@Param("tableName") String tableName,@Param("tableKey") String tableKey) {
		try {
			formhtml=Decode64Util.Decrypt(formhtml);
			Map params = new HashMap();
			params.put("defHtml", formhtml);
			params.put("formtype", formtype);
			params.put("tableName", tableName);
			params.put("tableKey", tableKey);
			
			Form_def formdef=new Form_def();
			formdef.setFormtitle(formtitle);
			formdef.setFormdes(formdes);
			formdef.setFormtype(formtype);
			formdef.setFormhtml(formhtml);
			formdef.setXyml(xyml);
			formdef.setYwtype(ywtype);
			formdef.setTable_type(table_type);
			ParseReult result=null;
		    if(EmptyUtils.isEmpty(formdesid)){//新增
		    	result = FormUtil.parseHtmlNoTable(params);
		    	formdesid=YWCL.getVifCode();
		    }else{//修改
		    	String sql="select tableKey||'_'||fieldname,location from form_field,form_table where form_field.tableid=form_table.tableid and formdefid='"+formdesid+"'";
		    	Hashtable<String, String> pxmap=daoCtl.getHTable(dao, Sqls.create(sql));
		    	params.put("pxmap", pxmap);
		    	result = FormUtil.parseHtmlNoTable(params);
		    	Form_table table=result.getFormTable();
		    	sql="select tableid from form_table where ismain=1 and formdefid='"+formdesid+"'";
		    	int tableid=daoCtl.getIntRowValue(dao, Sqls.create(sql));
		    	sql="select * from form_field where isdesignshow=0 and tableid='"+tableid+"' order by fieldid";
		    	List<Form_field> fields=daoCtl.list(dao, Form_field.class, Sqls.create(sql));
		    	table.addFields(fields);
		    	List<Form_table> subtables=table.getSubTableList();
		    	for (Form_table subtable : subtables) {
		    		sql="select tableid from form_table where tablekey='"+subtable.getTablekey()+"' and ismain=0 and formdefid='"+formdesid+"'";
			    	tableid=daoCtl.getIntRowValue(dao, Sqls.create(sql));
			    	sql="select * from form_field where isdesignshow=0 and tableid='"+tableid+"' order by fieldid";
			    	List<Form_field> tfields=daoCtl.list(dao, Form_field.class, Sqls.create(sql));
			    	subtable.addFields(tfields);
				}
		    }
		    formdefMap.put(formdesid, formdef);
		    req.setAttribute("formdesid", formdesid);
		    req.setAttribute("result", result);
		    req.setAttribute("formtype", formtype);
		    req.setAttribute("tableName", tableName);
			req.setAttribute("tableKey", tableKey);
			req.setAttribute("xyml", xyml);
			req.setAttribute("table_type", table_type);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 功能描述:表单管理新增页面
	 *
	 *
	 */
	@At
	@Ok("raw")
	public boolean saveForm(HttpSession session,HttpServletRequest req,@Param("formdesid") final String formdesid,
			@Param("subobjs") String subobjs,@Param("objs") String objs,@Param("fb") final int fb,@Param("status") final int status) {
		boolean bol=false;
		try {
			objs=Decode64Util.Decrypt(objs);
			subobjs=Decode64Util.Decrypt(subobjs);
			String sql="select * from form_def where defid='"+formdesid+"'";
			Form_def olddef=daoCtl.detailBySql(dao, Form_def.class, Sqls.create(sql));
			Form_def formdef=formdefMap.get(formdesid);
			Sys_user user = (Sys_user) session.getAttribute("userSession");
			if(EmptyUtils.isNotEmpty(olddef)){
				//update
				olddef.setFormtitle(formdef.getFormtitle());
				olddef.setFormdes(formdef.getFormdes());
				olddef.setFormtype(formdef.getFormtype());
				olddef.setFormhtml(formdef.getFormhtml());
				olddef.setStatus(status);
				olddef.setYwtype(formdef.getYwtype());
				olddef.setXyml(formdef.getXyml());
				olddef.setTable_type(formdef.getTable_type());
				bol=update(user, olddef, objs, subobjs);
			}else{
				formdef.setStatus(status);
				bol=add(user, formdef, objs, subobjs);
			}
			if(bol){
				formdefMap.remove(formdesid);
				comUtil.setHtTable(dao, daoCtl, "1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bol;
	}
	public boolean add(final Sys_user user,final Form_def formdef,final String objs,final String subobjs) {
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		re.set(false);
		Trans.exec(new Atom() {
			@Override
			public void run() {
				String curtime=DateUtil.getCurDateTime();
				formdef.setVersion(1);
				formdef.setCreater(user.getLoginname());
				formdef.setCreatetime(curtime);
				dao.insert(formdef);
				//增加主表
				JSONObject ss=JSONObject.fromObject(objs);
				String tablename=StringUtil.null2String(ss.get("tableName"));
				String tablekey=StringUtil.null2String(ss.get("tableKey"));
				JSONArray sobj=(JSONArray)ss.get("rows");
				List<Form_field> fields=JSONArray.toList(sobj, Form_field.class);
				Form_table formTable = new Form_table();
				formTable.setTablename(tablename);
				formTable.setTablekey(tablekey);
				formTable.setCatetetime(curtime);
				formTable.setCreater(user.getLoginname());
				formTable.setFormdefid(formdef.getDefid());
				formTable.setForm_type(formdef.getTable_type());
				formTable.setIsmain(1);
				dao.insert(formTable);
				int i=0;
				for (Form_field formField : fields) {
					formField.setTableid(formTable.getTableid());
					formField.setLocation(i++);
					if(EmptyUtils.isNotEmpty(formField.getFieldname())){
						dao.insert(formField);
					}
				}
				//增加子表
				JSONArray subArray=JSONArray.fromObject(subobjs);
				for (Object object : subArray) {
					JSONObject subs=(JSONObject) object;
					tablename=StringUtil.null2String(subs.get("tableName"));
					tablekey=StringUtil.null2String(subs.get("tableKey"));
					sobj=(JSONArray)subs.get("rows");
					List<Form_field> tmpfields=JSONArray.toList(sobj, Form_field.class);
					Form_table tmptable = new Form_table();
					tmptable.setTablename(tablename);
					tmptable.setTablekey(tablekey);
					tmptable.setCatetetime(curtime);
					tmptable.setCreater(user.getLoginname());
					tmptable.setFormdefid(formdef.getDefid());
					tmptable.setForm_type(formdef.getTable_type());
					dao.insert(tmptable);
					i=0;
					for (Form_field formField : tmpfields) {
						formField.setTableid(tmptable.getTableid());
						formField.setLocation(i++);
						if(EmptyUtils.isNotEmpty(formField.getFieldname())){
							dao.insert(formField);
						}
					}
				}
				comUtil.formHtmlMap.put(formdef.getDefid()+"_add", FormUtil.generateHtml(formdef.getFormhtml(),"add"));
				comUtil.formHtmlMap.put(formdef.getDefid()+"_update", FormUtil.generateHtml(formdef.getFormhtml(),"update"));
				comUtil.formHtmlMap.put(formdef.getDefid()+"_makeup", FormUtil.generateHtml(formdef.getFormhtml(),"makeup"));
				comUtil.formHtmlMap.put(formdef.getDefid()+"_view", FormUtil.generateHtml(formdef.getFormhtml(),"view"));
				re.set(true);
			}
		});
		return re.get();
	}
	public boolean update(final Sys_user user,final Form_def formdef,final String objs,final String subobjs) {
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		re.set(false);
		Trans.exec(new Atom() {
			@Override
			public void run() {
				dao.update(formdef);
				//更新主表
				JSONObject ss=JSONObject.fromObject(objs);
				String tablename=StringUtil.null2String(ss.get("tableName"));
				String tablekey=StringUtil.null2String(ss.get("tableKey"));
				String oldtablekey=StringUtil.null2String(ss.get("tableKey"));
				JSONArray sobj=(JSONArray)ss.get("rows");
				List<Form_field> fields=JSONArray.toList(sobj, Form_field.class);
				Form_table formTable = dao.fetch(Form_table.class, Cnd.where("formdefid", "=", formdef.getDefid()).and("ismain", "=", "1").and("tablekey", "=", oldtablekey));
				formTable.setTablename(tablename);
				formTable.setTablekey(tablekey);
				formTable.setCreater(user.getLoginname());
				formTable.setFormdefid(formdef.getDefid());
				formTable.setForm_type(formdef.getTable_type());
				dao.update(formTable);
				//设置所有字段的状态为删除
				Sql updatesql=Sqls.create("update form_field set status=1 where tableid=@tableid");
				updatesql.params().set("tableid", formTable.getTableid());
				dao.execute(updatesql);
				int i=0;
				for (Form_field formField : fields) {
					Form_field oldfield = dao.fetch(Form_field.class, Cnd.where("tableid", "=", formTable.getTableid()).and("fieldname", "=", formField.getFieldname()));
					formField.setLocation(i++);
					formField.setTableid(formTable.getTableid());
					if(EmptyUtils.isEmpty(oldfield)){
						if(EmptyUtils.isNotEmpty(formField.getFieldname())){
							dao.insert(formField);
						}
					}else{
						formField.setFieldid(oldfield.getFieldid());
						dao.update(formField);
					}
				}
				//更新子表
				JSONArray subArray=JSONArray.fromObject(subobjs);
				for (Object object : subArray) {
					JSONObject subs=(JSONObject) object;
					tablename=StringUtil.null2String(subs.get("tableName"));
					tablekey=StringUtil.null2String(subs.get("tableKey"));
					oldtablekey=StringUtil.null2String(subs.get("tableKey"));
					sobj=(JSONArray)subs.get("rows");
					List<Form_field> tmpfields=JSONArray.toList(sobj, Form_field.class);
					Form_table tmptable = dao.fetch(Form_table.class, Cnd.where("formdefid", "=", formdef.getDefid()).and("ismain", "=", "0").and("tablekey", "=", oldtablekey));
					if(EmptyUtils.isNotEmpty(tmptable)){
						tmptable.setTablename(tablename);
						tmptable.setTablekey(tablekey);
						tmptable.setCreater(user.getLoginname());
						tmptable.setFormdefid(formdef.getDefid());
						tmptable.setForm_type(formdef.getTable_type());
						dao.update(tmptable);
					}else{
						tmptable=new Form_table();
						tmptable.setTablename(tablename);
						tmptable.setTablekey(tablekey);
						tmptable.setCreater(user.getLoginname());
						tmptable.setFormdefid(formdef.getDefid());
						tmptable.setForm_type(formdef.getTable_type());
						dao.insert(tmptable);
					}
					//设置所有字段的状态为删除
					updatesql.params().set("tableid", tmptable.getTableid());
					dao.execute(updatesql);
					i=0;
					for (Form_field formField : tmpfields) {
						Form_field oldfield = dao.fetch(Form_field.class, Cnd.where("tableid", "=", tmptable.getTableid()).and("fieldname", "=", formField.getFieldname()));
						formField.setLocation(i++);
						formField.setTableid(tmptable.getTableid());
						if(EmptyUtils.isEmpty(oldfield)){
							if(EmptyUtils.isNotEmpty(formField.getFieldname())){
								dao.insert(formField);
							}
						}else{
							formField.setFieldid(oldfield.getFieldid());
							dao.update(formField);
						}
					}
				}
				comUtil.formHtmlMap.put(formdef.getDefid()+"_add", FormUtil.generateHtml(formdef.getFormhtml(),"add"));
				comUtil.formHtmlMap.put(formdef.getDefid()+"_update", FormUtil.generateHtml(formdef.getFormhtml(),"update"));
				comUtil.formHtmlMap.put(formdef.getDefid()+"_makeup", FormUtil.generateHtml(formdef.getFormhtml(),"makeup"));
				comUtil.formHtmlMap.put(formdef.getDefid()+"_view", FormUtil.generateHtml(formdef.getFormhtml(),"view"));
				re.set(true);
			}
		});
		return re.get();
	}
	/**
	 * 功能描述:表单管理新增页面
	 *
	 *
	 */
	@At
	@Ok("->:/private/form/formDesign.html")
	public void toupdate(HttpServletRequest req,@Param("id") String id) {
		try {
			if(EmptyUtils.isNotEmpty(id)){
				Form_def formdef=daoCtl.detailByName(dao, Form_def.class, "defid", id);
				req.setAttribute("def", formdef);
				req.setAttribute("formdesid", formdef.getDefid());
				String sql="select * from form_table where ismain=1 and formdefid="+id+" order by tableid desc";
				Form_table table=daoCtl.detailBySql(dao, Form_table.class, Sqls.create(sql));
				if (EmptyUtils.isNotEmpty(table)) {
					req.setAttribute("tableName", table.getTablename());
					req.setAttribute("tableKey", table.getTablekey());
					if(formdef.getIsgen()==1){//生成实体后表名及现有字段不能更改
						req.setAttribute("onlyread", 1);
					}
				}
				
			}
			req.setAttribute("csvalueList", comUtil.sxxwtypeList);
			req.setAttribute("ywMap", comUtil.formywMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@At
	public boolean deleteIds(@Param("ids") final String ids) {
		try {
			final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
			re.set(false);
			Trans.exec(new Atom() {
				@Override
				public void run() {
					String[] id = StringUtil.null2String(ids).split(",");
					String sqlstr="select tableid from form_table where formdefid in "+StringUtil.getIdsplit(id)+"";
					List<String> tableid=daoCtl.getStrRowValues(dao, Sqls.create(sqlstr));
					sqlstr="delete from form_field where tableid in "+StringUtil.getIdsplit(tableid)+"";
					Sql sql1=Sqls.create(sqlstr);
					sqlstr="delete from form_table where formdefid in "+StringUtil.getIdsplit(id)+"";
					Sql sql2=Sqls.create(sqlstr);
					sqlstr="delete from form_def where defid in "+StringUtil.getIdsplit(id)+"";
					Sql sql3=Sqls.create(sqlstr);
					dao.execute(new Sql[]{sql1,sql2,sql3});
					sqlstr="select t.tablekey from form_table t,form_def d where t.formdefid=d.defid and d.isgen=1 and d.defid in "+StringUtil.getIdsplit(id)+"";
					List<String> tablekeys=daoCtl.getStrRowValues(dao, Sqls.create(sqlstr));
					for (String tablekey : tablekeys) {
						dao.drop(TABLE_PRE_NAME+tablekey);
					}
					for (String tmp : id) {
						comUtil.formHtmlMap.remove(tmp+"_add");
						comUtil.formHtmlMap.remove(tmp+"_update");
						comUtil.formHtmlMap.remove(tmp+"_makeup");
						comUtil.formHtmlMap.remove(tmp+"_view");
					}
					re.set(true);
				}
			});
			return re.get();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	@At
	public String checktable(@Param("id") String id,@Param("tablekeys") String tablekeys,@Param("xyml") String xyml) {
		try {
			String[] tablekey = StringUtil.null2String(tablekeys).split(",");
			String sqlstr="select wm_concat(to_char(tablekey)) from form_table where formdefid not in (select defid from form_def where xyml='"+xyml+"' or defid='"+id+"') and tablekey in "+StringUtil.getStrsplit(tablekey);
			return daoCtl.getStrRowValue(dao, Sqls.create(sqlstr));
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	@At
	@Ok("raw")
	public JSONObject list(@Param("page") int curPage, @Param("rows") int pageSize,@Param("xyml") String xyml,
			@Param("formtitle") String formtitle,@Param("formdes") String formdes,@Param("SelisGen") String SelisGen){
		Criteria cri = Cnd.cri();
		if(EmptyUtils.isNotEmpty(formtitle)){
			cri.where().and("formtitle","like",formtitle);
		}
		if(EmptyUtils.isNotEmpty(formdes)){
			cri.where().and("formdes","like",formdes);
		}
		if(EmptyUtils.isNotEmpty(SelisGen)){
			cri.where().and("isGen","=",SelisGen);
		}
		if(EmptyUtils.isNotEmpty(xyml)){
			cri.where().and("xyml","like",xyml+"%");
		}
		cri.getOrderBy().desc("defid");
		JSONObject jobj= daoCtl.listPageJson(dao, Form_def.class, curPage, pageSize, cri);
		return jobj;
	}
	/**
	 * 功能描述:表单管理新增页面
	 *
	 *
	 */
	@At
	@Ok("->:/private/form/preview.html")
	public void preview(HttpServletRequest req,@Param("id") int formdefid,@Param("mid") String mid
			,@Param("formhtml") String formhtml) {
		try {
			if(EmptyUtils.isEmpty(formhtml)){
				if(formdefid>0){
					Form_def olddef=daoCtl.detailByName(dao, Form_def.class, "defid",formdefid);
					formhtml=olddef.getFormhtml();
				}else{
					Form_def olddef=formdefMap.get(mid);
					formhtml=olddef.getFormhtml();
				}
			}
			String restr=FormUtil.generateHtml(formhtml,"").replaceAll("<body>", "").replaceAll("</body>", "");
			req.setAttribute("body", restr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 功能描述:表单管理新增页面
	 *
	 *
	 */
	@At
	@Ok("->:/private/form/queryDesign.html")
	public void toquery(HttpServletRequest req,@Param("id") String id) {
		try {
			if(EmptyUtils.isNotEmpty(id)){
				req.setAttribute("formdefid", id);
				String sqlstr="select * from form_query where formdefid="+id;
				Form_query query=daoCtl.detailBySql(dao, Form_query.class, Sqls.create(sqlstr));
				if(EmptyUtils.isEmpty(query)){
					Form_def formdef=daoCtl.detailByName(dao, Form_def.class, "defid", id);
					req.setAttribute("queryhtml", FormUtil.generateQueryHtml(formdef.getFormhtml()));
				}else{
					req.setAttribute("id", query.getId());
					req.setAttribute("queryhtml",query.getQueryhtml());
					req.setAttribute("query",query);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 功能描述:表单管理新增页面
	 *
	 *
	 */
	@At
	@Ok("raw")
	public boolean saveQuery(HttpSession session,HttpServletRequest req,@Param("..") final Form_query query) {
		boolean bol=false;
		try {
			if(EmptyUtils.isNotEmpty(query.getQueryhtml())){
				query.setQueryhtml(Decode64Util.Decrypt(query.getQueryhtml()));
			}
			if(EmptyUtils.isNotEmpty(query.getId())&&query.getId()!=0){
				bol=daoCtl.update(dao, query);
			}else{
				bol=daoCtl.add(dao, query);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bol;
	}
	@At
	@Ok("->:/private/form/viewDesign.html")
	public void toview(HttpServletRequest req,@Param("id") String id) {
		try {
			System.out.println("kdkdfkldfkldfkldf");
			if(EmptyUtils.isNotEmpty(id)){
				req.setAttribute("formdefid", id);
				Form_def formdef=daoCtl.detailByName(dao, Form_def.class, "defid", id);
				if(EmptyUtils.isEmpty(formdef.getViewhtml())){
					req.setAttribute("viewhtml", formdef.getFormhtml());
				}else{
					req.setAttribute("viewhtml", formdef.getViewhtml());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 功能描述:表单管理新增页面
	 */
	@At
	@Ok("raw")
	public boolean saveView(HttpSession session,HttpServletRequest req,@Param("formdefid") String formdefid,@Param("viewhtml") String viewhtml) {
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		final Form_def formdef=daoCtl.detailByName(dao, Form_def.class, "defid", formdefid);
		formdef.setViewhtml(viewhtml);
		re.set(false);
		try {
			Trans.exec(new Atom() {
				@Override
				public void run() {
					dao.update(formdef);
					comUtil.formHtmlMap.put(formdef.getDefid()+"_view", FormUtil.generateHtml(formdef.getViewhtml(),"view"));
					re.set(true);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return re.get();
	}
}