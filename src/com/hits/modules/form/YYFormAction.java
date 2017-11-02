package com.hits.modules.form;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
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
import com.hits.common.dao.ObjectCtl;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.SqlFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.DateUtil;
import com.hits.common.util.DecodeUtil;
import com.hits.modules.bean.File_info;
import com.hits.modules.cgtable.util.CommonStaticUtil;
import com.hits.modules.com.YWCL;
import com.hits.modules.com.comUtil;
import com.hits.modules.form.bean.Form_def;
import com.hits.modules.form.bean.Form_field;
import com.hits.modules.form.bean.Form_query;
import com.hits.modules.form.bean.Form_table;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;
import com.hits.util.StringUtil;
import com.hits.util.SysLogUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
@At("/private/formyy")
@Filters({ @By(type = GlobalsFilter.class), @By(type = SqlFilter.class),@By(type = UserLoginFilter.class) })
public class YYFormAction extends BaseAction {
	public static Hashtable<String, Form_def> formdefMap = new Hashtable<String, Form_def>();//下一步时自定义表单信息存放在内存中
	public static String TABLE_PRE_NAME = "WF_";
	public static String url="/private/formyy/topreview?defid=@defid&mainid=@mainid";

	@Inject
	protected Dao dao;

	@At("/tolist")
	@Ok("->:${obj}")
	public String tolist(HttpSession session,HttpServletRequest req,@Param("id") int id,@Param("type")int type) {
		String sqlstr="select * from form_table where ismain=1 and formdefid="+id;
		Form_table table=daoCtl.detailBySql(dao, Form_table.class, Sqls.create(sqlstr));
		req.setAttribute("table", table);
		sqlstr="select lower(fieldname) fieldname,fieldlabel from form_field where status=0 and islist=1 and tableid='"+table.getTableid()+"' order by location asc";
		List<Map> fieldlist=daoCtl.list(dao, Sqls.create(sqlstr));
		req.setAttribute("fieldlist", fieldlist);
		req.setAttribute("id", id);
		sqlstr="select ywtype from form_def where defid="+id;
		req.setAttribute("ywtype", daoCtl.getStrRowValue(dao, Sqls.create(sqlstr)));
		sqlstr="select lower(fieldname) fieldname from form_field where status=0 and isprimary=1 and tableid='"+table.getTableid()+"' order by location asc";
		req.setAttribute("primkey", daoCtl.getStrRowValue(dao, Sqls.create(sqlstr)));
		sqlstr="select * from form_query where formdefid='"+id+"'";
		Form_query query=daoCtl.detailBySql(dao, Form_query.class, Sqls.create(sqlstr));
		if (EmptyUtils.isNotEmpty(query)) {
			String queryhtml=FormUtil.getQueryHtml(query.getQueryhtml());
			req.setAttribute("queryhtml", queryhtml);
			req.setAttribute("query", query);
		}
		
		sqlstr="select fieldname from form_field where fieldlabel like '%完成'and status=0 and tableid="+table.getTableid();
		req.setAttribute("sfwc", daoCtl.getStrRowValue(dao, Sqls.create(sqlstr)));
		
		String reName = "";
		switch (type) {
			case 0 :
				reName = "/private/formyy/tableList.html";
				break;
			case 1 :
				reName = "/private/formyy/tableListForView.html";break;
			default:
				reName = "/private/formyy/tableList.html";break;
		}
		return reName;
	}

	@At
	@Ok("raw")
	public JSONObject list(HttpSession session,@Param("page") int curPage, @Param("rows") int pageSize,@Param("tableid") String tableid,@Param("tablename") String tablename,
			@Param("sort") String sort,@Param("order") String order,@Param("querySql") String querySql,HttpServletRequest req){
		JSONObject jobj=null;
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		try {
			String sqlstr="select fieldname from form_field where status=0 and isquery=1 and tableid="+tableid;
			List<String> filednames=daoCtl.getStrRowValues(dao, Sqls.create(sqlstr));
			sqlstr="select * from "+CommonStaticUtil.TABLE_NAME_SUB+tablename;
			querySql=StringUtil.null2String(querySql);
			if(EmptyUtils.isNotEmpty(querySql)){
				querySql=DecodeUtil.Decrypt(querySql);
			}
			for (String filedname : filednames) {
				String nameequal=req.getParameter(filedname+"/equal");
				String namelike=req.getParameter(filedname+"/like");
				String namelikeend=req.getParameter(filedname+"/likeend");
				String namegreatere=req.getParameter(filedname+"/greaterequal");
				String namelesse=req.getParameter(filedname+"/lessequal");//日期才有大于等于或小于等于的条件
				if(EmptyUtils.isNotEmpty(nameequal)&&!" ".equals(nameequal)){
					String[] values=nameequal.split(",");
					if(values.length==1){
						querySql+=filedname+"='"+nameequal+"' and ";
					}else{
						String tmpsql="(";
						for (String str : values) {
							tmpsql+=filedname+"='"+str+"' or ";
						}
						tmpsql=tmpsql.substring(0,tmpsql.lastIndexOf(" or "))+")";
						querySql+=tmpsql+" and ";
					}
				}else if(EmptyUtils.isNotEmpty(namelike)&&!" ".equals(namelike)){
					String[] values=namelike.split(",");
					if(values.length==1){
						querySql+=filedname+" like '%"+namelike+"%' and ";
					}else{
						String tmpsql="(";
						for (String str : values) {
							tmpsql+=filedname+" like '%"+str+"%' or ";
						}
						tmpsql=tmpsql.substring(0,tmpsql.lastIndexOf(" or "))+")";
						querySql+=tmpsql+" and ";
					}
				}else if(EmptyUtils.isNotEmpty(namelikeend)&&!" ".equals(namelikeend)){
					String[] values=namelikeend.split(",");
					if(values.length==1){
						querySql+=filedname+" like '"+namelikeend+"%' and ";
					}else{
						String tmpsql="(";
						for (String str : values) {
							tmpsql+=filedname+" like '"+str+"%' or ";
						}
						tmpsql=tmpsql.substring(0,tmpsql.lastIndexOf(" or "))+")";
						querySql+=tmpsql+" and ";
					}
				}else if(EmptyUtils.isNotEmpty(namegreatere)&&!" ".equals(namegreatere)){
					querySql+=filedname+" >= '"+namegreatere+" 00:00:00' and ";
				}else if(EmptyUtils.isNotEmpty(namelesse)&&!" ".equals(namelesse)){
					querySql+=filedname+" <= '"+namelesse+" 23:59:59' and ";
				}
			}
			
			String temsql="select count(1) from form_field where status=0 and fieldname='status' and tableid="+tableid;
			int i=daoCtl.getIntRowValue(dao, Sqls.create(temsql));
			if(i>0){
				querySql+=" (status is null or status!=-1) and ";
			}
			temsql="select count(1) from form_field where status=0 and fieldname='actor' and tableid="+tableid;
			i=daoCtl.getIntRowValue(dao, Sqls.create(temsql));
			if(i>0){
				querySql+=" actor='"+user.getLoginname()+"' and ";
			}
			if(EmptyUtils.isNotEmpty(querySql)){
				querySql=querySql.substring(0,querySql.lastIndexOf("and "));
				sqlstr=sqlstr+" where "+querySql;
			}
			temsql="select count(1) from form_field where status=0 and fieldname='add_time' and tableid="+tableid;
			i=daoCtl.getIntRowValue(dao, Sqls.create(temsql));
			if(i==0){
				jobj= daoCtl.listPageJsonSql(dao, Sqls.create(sqlstr), curPage, pageSize);
			}else{
				if(EmptyUtils.isNotEmpty(sort)&&EmptyUtils.isNotEmpty(order)){
					jobj= daoCtl.listPageJsonSql(dao, Sqls.create(sqlstr + " order by "+sort+" "+order+" "), curPage, pageSize);
				}else{
					jobj= daoCtl.listPageJsonSql(dao, Sqls.create(sqlstr + " order by add_time desc"), curPage, pageSize);
				}
			}
			System.out.println(sqlstr);
			JSONArray sobj=(JSONArray)jobj.get("rows");
			List<JSONObject> list=JSONArray.toList(sobj,JSONObject.class);
			sqlstr="select fieldname,controltype,options,loadselect from form_field where status=0 and controltype in (0,3,4,5,7,8) and tableid="+tableid;
			List<Map> controlList=daoCtl.list(dao, Sqls.create(sqlstr));
			if(list.size()>0){
				for (Map map : controlList) {
					int controltype=StringUtil.StringToInt(StringUtil.null2String(map.get("controltype")));
					String fieldname=StringUtil.null2String(map.get("fieldname"));
					for (JSONObject pageMap : list) {
						String value=StringUtil.null2String(pageMap.get(fieldname));
						if(controltype==3){//多选框
							String options=StringUtil.null2String(map.get("options"));
							Map<String,String> optionsMap=getOptionsMap(options, "#newline#", "/");
							String[] values=value.split(",");
							value="";
							for (String val : values) {
								value+=optionsMap.get(val)+";";
							}
							value=value.substring(0,value.lastIndexOf(";"));
						}else if(controltype==5||controltype==4){//下拉框或单选框
							String loadselect=StringUtil.null2String(map.get("loadselect"));
							String options=StringUtil.null2String(map.get("options"));
							if(EmptyUtils.isNotEmpty(loadselect)){
								String sql="select value,name from Cs_value where typeid = '"+loadselect+"'";
								if(controltype==5){//下拉框
									sql="select code,name from Cs_value where typeid = '"+loadselect+"'";
									Map<String,String> loadselectMap=daoCtl.getHTable(dao, Sqls.create(sql));
									sql="select code from Cs_value where value='"+value+"' and typeid = '"+loadselect+"'";
									String code=daoCtl.getStrRowValue(dao, Sqls.create(sql));
									int lg=code.length()/4;
									value="";
									for (int j = 1; j <= lg; j++) {
										value+=loadselectMap.get(code.substring(0,j*4))+"/";
									}
									int lg2=value.length()>0?value.length()-1:0;
									value=value.substring(0,lg2);
								}else{
									sql="select name from Cs_value where value='"+value+"' and typeid = '"+loadselect+"'";
									value=daoCtl.getStrRowValue(dao, Sqls.create(sql));
								}
							}else{
								Map<String,String> optionsMap=getOptionsMap(options, "#newline#", "/");
								value=optionsMap.get(value);
							}
						}else if(controltype==7||controltype==8){//信用主体
							pageMap.put("xyztid",value);
							String[] values=value.split(",");
							value="";
							for (String val : values) {
								value+=StringUtil.null2String(comUtil.xyztMap.get(val)).split("☆")[0]+";";
							}
							value=value.substring(0,value.lastIndexOf(";"));
						}else if(controltype==19){//用户所在机构
							value=comUtil.unitMap.get(value);
						}else if(controltype==0){//状态
							if(fieldname.equals("status")){
								value=comUtil.statusMap.get(value);
							}
						}
						if(EmptyUtils.isEmpty(value)){
							value=StringUtil.null2String(pageMap.get(fieldname));
						}
						pageMap.put(fieldname,value);
					}
				}
				jobj.element("rows", list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobj;
	}
	public static Map<String,String> getOptionsMap(String options,String sp1,String sp2){
		Map<String,String> map = new HashMap<String, String>();
		String[] ss=options.split(sp1);
		for (String str : ss) {
			String[] strs=str.split(sp2);
			if(strs.length==2){
				map.put(strs[1], strs[0]);
			}else{
				map.put(str, str);
			}
		}
		return map;
	}
	/*
	 * 批量删除
	 */
	@At
	public boolean deleteIds(@Param("tableid") String tableid,@Param("tablename") String tablename,@Param("primkey") String primkey,@Param("ids") String ids) {
		String[] id = StringUtil.null2String(ids).split(",");
		String sqlstr="delete from "+CommonStaticUtil.TABLE_NAME_SUB+tablename+" where "+primkey+" in "+StringUtil.getStrsplit(id);
		return true;
		//return daoCtl.exeUpdateBySql(dao, Sqls.create(sqlstr));
	}

	/**
	 * 功能描述:表单管理新增页面
	 *
	 *
	 */
	@At
	@Ok("->:/private/formyy/tableAdd.html")
	public void toadd(HttpServletRequest req,HttpSession session,@Param("id") int id) {
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		try {
			/*Form_def olddef=daoCtl.detailByName(dao, Form_def.class, "defid",id);
				String formhtml=olddef.getFormhtml();
				String restr=FormUtil.generateHtml(formhtml,"add").replaceAll("<body>", "").replaceAll("</body>", "");*/
			String restr=comUtil.formHtmlMap.get(id+"_add").replaceAll("<body>", "").replaceAll("</body>", "");
			req.setAttribute("body", restr);
			req.setAttribute("formdefid", id);
			String unitid=comUtil.yhjgHt.get(user.getUnitid());
			req.setAttribute("yhjg", unitid);
			req.setAttribute("yhjgname", comUtil.unitMap.get(unitid));
			String xzqh=user.getXzqh();
			req.setAttribute("xzqh1", xzqh.substring(0,2)+"0000");
			req.setAttribute("xzqh2", xzqh.substring(0,4)+"00");
			req.setAttribute("xzqh3", xzqh);
			if(id==241){
				req.setAttribute("xzqhname", "addr");
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
	public boolean add(HttpSession session,final HttpServletRequest req,final @Param("formdefid") int formdefid) {
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		final Sys_user user=(Sys_user) session.getAttribute("userSession");
		try {
			Trans.exec(new Atom() {
				public void run() {
					if (formdefid>0) {
						String lognote="";
						String main_zhujian="";//主表主键值
						//获取页面参数和值
						Map<String, String> map = new HashMap<String, String>();
						Enumeration paramNames = req.getParameterNames();
						while (paramNames.hasMoreElements()) {
							String paramName = (String) paramNames.nextElement();
							String[] paramValues = req.getParameterValues(paramName);
							if (paramValues.length >= 1) {
								String paramValue="";
								for (int i = 0; i < paramValues.length; i++) {
									paramValue=paramValue+paramValues[i]+"☆";
								}
								paramValue=paramValue.substring(0,paramValue.length()-1);
								map.put(paramName, paramValue);
							}
						}
						System.out.println("获取页面的状态值"+map.get("status"));
						//获取form_table表信息
						List<Form_table> tableList=daoCtl.list(dao, Form_table.class, Sqls.create("select * from form_table where formdefid="+formdefid+" order by ismain desc"));
						if (tableList.size()>0) {
							for (Form_table table :tableList) {
								//获取表名
								String tablename=CommonStaticUtil.TABLE_NAME_SUB+table.getTablekey();
								//判断表是否已经创建
								boolean is_create=dao.exists(tablename);
								if (is_create) {
									int datasize=0;//数据数量
									String isprimary="";//主键字段
									String foreignkey="";//外键字段
									String primarykeyvalue="";//主键值
									String field_qz="";
									List<Map<String,String>> list=new ArrayList<Map<String,String>>();
									//是否是主表
									if (table.getIsmain()==1) {
										String zjsql="SELECT sys_guid() FROM dual";
										main_zhujian=daoCtl.getStrRowValue(dao, Sqls.create(zjsql));
									}else{
										field_qz=table.getTablekey()+".";
									}
									Map<String,String> fieldMap=new HashMap<String,String>();
									fieldMap.put(".table", tablename);

									String fieldSql="select * from form_field where status=0 and tableid= "+table.getTableid()+" order by isprimary desc,fieldid";
									List<Form_field> fieldList=daoCtl.list(dao, Form_field.class, Sqls.create(fieldSql)); 
									fieldSql="select fieldname,controltype from form_field where status=0 and tableid= "+table.getTableid();
									Hashtable<String, String> controltypeHT=daoCtl.getHTable(dao, Sqls.create(fieldSql)); 
									for (Form_field field : fieldList) {
										if(field.getIsprimary()==1){
											isprimary=field.getFieldname();
										}
										if (EmptyUtils.isNotEmpty(field.getForeignkey())) {
											foreignkey=field.getFieldname();
										}
										if (map.containsKey(field_qz+field.getFieldname())&&field.getControltype()!=15) {
											String fieldvalue=map.get(field_qz+field.getFieldname());
											fieldMap.put(field.getFieldname(), fieldvalue);
											//保存临时数据数量
											String[] dd=fieldvalue.split("☆");
											if (datasize<dd.length) {
												datasize=dd.length;
											}
										}else{
											fieldMap.put(field.getFieldname(), "");
										}
									}
									//封装数据插入数据库
									for (int i = 0; i < datasize; i++) {
										Map<String,Object> sd=new HashMap<String,Object>();
										if(EmptyUtils.isNotEmpty(isprimary)){
											if (table.getIsmain()==1) {//主表
												primarykeyvalue=main_zhujian;
											}else{
												primarykeyvalue= daoCtl.getStrRowValue(dao, Sqls.create("SELECT sys_guid() FROM dual"));
											}
										}
										for (Map.Entry<String, String> zd : fieldMap.entrySet()) {
											String key=zd.getKey();
											String value=StringUtil.null2String(zd.getValue());
											//附件
											if ("15".equals(controltypeHT.get(key))) {
												YYFormUtil.saveFile(daoCtl,dao,key, map, field_qz, tablename, primarykeyvalue);
												sd.put(key, "");
											}else{
												if (value.indexOf("☆")!=-1) {
													String[] dd=value.split("☆");
													if ((i+1)>dd.length) {
														sd.put(key, "");
													}else{
														sd.put(key, dd[i]);
													}
												}else{
													sd.put(key, value);
												}
											}
										}

										if (EmptyUtils.isNotEmpty(foreignkey)) {
											sd.put(foreignkey, main_zhujian);
										}
										sd.put(isprimary, primarykeyvalue);
										if(((String) sd.get(".table")).toUpperCase().indexOf("_YW") == -1) {
											sd.put("actor", user.getLoginname());
											sd.put("unitid", user.getUnitid());
											sd.put("xzqh_unit", user.getXzqh());
										}
										//录入时间
										sd.put("add_time",DateUtil.getCurDateTime());
										//录入时间戳
										if (table.getIsmain()==1) {//主表
											sd.put("sjc",DateUtil.str2sjc(DateUtil.getCurDateTime()));
										}
										lognote+=SysLogUtil.getLogNoteForm(sd, table.getTableid()+"","add");
										dao.insert(sd);
									}
								}else{
									re.set(false);
									break;
								}
							}
						}
						re.set(true);
						String formname=daoCtl.getStrRowValue(dao, Sqls.create("select formtitle from form_def where defid='"+formdefid+"'"));
						SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, true, 1,
								"增加表单【"+ formname +"】", formname, lognote,
								req.getParameter("log.xgyy"), req.getParameter("log.xgyj"),url.replace("@defid", formdefid+"").replace("@mainid", main_zhujian));
					}else{
						re.set(false);
					}
				}
			});

		} catch (Exception e) {
			re.set(false);
			e.printStackTrace();
		}
		return re.get();
	}
	
	

	/**
	 * 功能描述:表单管理修改页面
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月25日 上午10:32:03
	 *
	 * @param req
	 * @param defid
	 * @param mainid 主表id
	 */
	@At
	@Ok("->:/private/formyy/tableUpdate.html")
	public void toupdate(HttpServletRequest req,HttpSession session,@Param("defid") String defid,@Param("mainid") String mainid) {
		try {
			req.setAttribute("formdefid", defid);
			/*Form_def olddef=daoCtl.detailByName(dao, Form_def.class, "defid",defid);
			String formhtml=olddef.getFormhtml();
			String restr=FormUtil.generateHtml(formhtml,"update");*/
			String restr=comUtil.formHtmlMap.get(defid+"_update");
			/**
			 * 查询主表主键字段名称
			 */
			String  zhujian_field="";
			List<Form_table> tableList=daoCtl.list(dao, Form_table.class, Sqls.create("select * from form_table where formdefid="+defid+" order by ismain desc"));
			if (tableList.size()>0) {
				for (Form_table table :tableList) {
					//获取表名
					String tablename=CommonStaticUtil.TABLE_NAME_SUB+table.getTablekey();
					String filed_qz="";
					Sql mainkeysql=Sqls.create("select  fieldname from form_field where status=0 and tableid=@tableid and ISPRIMARY=1");
					mainkeysql.params().set("tableid", table.getTableid());
					String keyfiled=daoCtl.getStrRowValue(dao, mainkeysql);//当前table主键
					//获取编辑数据
					Sql fieldvaluesql=Sqls.create("select * from $table where $id=@id order by $orderby");
					fieldvaluesql.vars().set("table", tablename);
					if (table.getIsmain()==1) {
						//查询主表主键
						zhujian_field=keyfiled;
						fieldvaluesql.vars().set("id", keyfiled);
						fieldvaluesql.vars().set("orderby", keyfiled);
						fieldvaluesql.params().set("id", mainid);
						List<Map> zhubiao =daoCtl.list(dao, fieldvaluesql);
						Object status=zhubiao.get(0).get("status");
						req.setAttribute("status", status);
					}else{
						filed_qz=table.getTablekey()+".";
						//查询子表外键字段
						mainkeysql=Sqls.create("select  fieldname from form_field where status=0 and tableid=@tableid and foreignkey=@foreignkey");
						mainkeysql.params().set("tableid", table.getTableid());
						mainkeysql.params().set("foreignkey", zhujian_field);
						String keyfiled2=daoCtl.getStrRowValue(dao, mainkeysql);
						//查询排序字段，默认找第一个
						mainkeysql=Sqls.create("select  fieldname from form_field where status=0 and tableid=@tableid order by location");
						mainkeysql.params().set("tableid", table.getTableid());
						String orderby=daoCtl.getStrRowValue(dao, mainkeysql);

						fieldvaluesql.vars().set("id", keyfiled2);
						fieldvaluesql.vars().set("orderby", orderby);
						fieldvaluesql.params().set("id", mainid);
					}
					System.out.println(fieldvaluesql);
					List<Map> sd=daoCtl.list(dao, fieldvaluesql);
					Sql fieldSql=Sqls.create("select * from form_field where status=0 and tableid=@tableid ");
					fieldSql.params().set("tableid", table.getTableid());
					List<Form_field> fieldList=daoCtl.list(dao, Form_field.class, fieldSql);
					Document doc=Jsoup.parse(restr);
					restr=YYFormUtil.getHtml(daoCtl,dao,table, doc, sd, fieldList,filed_qz,keyfiled,"","");
				}
			}
			req.setAttribute("body", restr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 功能描述:表单管理修改页面
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月25日 上午10:32:03
	 *
	 * @param req
	 * @param defid
	 * @param mainid 主表id
	 */
	@At
	@Ok("->:/private/formyy/tableBulu.html")
	public void tomakeup(HttpServletRequest req,HttpSession session,@Param("defid") String defid,@Param("mainid") String mainid) {
		try {
			req.setAttribute("formdefid", defid);
			/*Form_def olddef=daoCtl.detailByName(dao, Form_def.class, "defid",defid);
			String formhtml=olddef.getFormhtml();
			String restr=FormUtil.generateHtml(formhtml,"makeup");*/
			String restr=comUtil.formHtmlMap.get(defid+"_makeup");
			Document doc=Jsoup.parse(restr);
			/**
			 * 查询主表主键字段名称
			 */
			String  zhujian_field="";
			List<Form_table> tableList=daoCtl.list(dao, Form_table.class, Sqls.create("select * from form_table where formdefid="+defid+" order by ismain desc"));
			if (tableList.size()>0) {
				for (Form_table table :tableList) {
					//获取表名
					String tablename=CommonStaticUtil.TABLE_NAME_SUB+table.getTablekey();
					String filed_qz="";
					Sql mainkeysql=Sqls.create("select  fieldname from form_field where status=0 and tableid=@tableid and ISPRIMARY=1");
					mainkeysql.params().set("tableid", table.getTableid());
					String keyfiled=daoCtl.getStrRowValue(dao, mainkeysql);//当前表主键
					//获取编辑数据
					Sql fieldvaluesql=Sqls.create("select * from $table where $id=@id");
					fieldvaluesql.vars().set("table", tablename);
					if (table.getIsmain()==1) {
						//查询主表主键
						zhujian_field=keyfiled;
						fieldvaluesql.vars().set("id", keyfiled);
					}else{
						filed_qz=table.getTablekey()+".";
						//查询子表外键字段
						mainkeysql=Sqls.create("select  fieldname from form_field where status=0 and tableid=@tableid and foreignkey=@foreignkey");
						mainkeysql.params().set("tableid", table.getTableid());
						mainkeysql.params().set("foreignkey", zhujian_field);
						String keyfiled2=daoCtl.getStrRowValue(dao, mainkeysql);
						fieldvaluesql.vars().set("id", keyfiled2);
					}
					fieldvaluesql.params().set("id", mainid);
					List<Map> sd=daoCtl.list(dao, fieldvaluesql);
					Sql fieldSql=Sqls.create("select * from form_field where status=0 and  tableid=@tableid ");
					fieldSql.params().set("tableid", table.getTableid());
					List<Form_field> fieldList=daoCtl.list(dao, Form_field.class, fieldSql);
					restr=YYFormUtil.getHtml(daoCtl,dao,table, doc, sd, fieldList,filed_qz,keyfiled,"","");
				}
			}
			req.setAttribute("body", restr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 功能描述:表单管理修改/填报页面
	 *
	 *
	 */
	@At
	@Ok("raw")
	public boolean update(HttpSession session,final HttpServletRequest req,final @Param("formdefid") int formdefid,final @Param("status")int status,@Param("..") final File_info file) {
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		final ThreadLocal<Integer> delSum = new ThreadLocal<Integer>();
		try {
			final Sys_user user=(Sys_user) session.getAttribute("userSession");
			Trans.exec(new Atom() {
				public void run() {
					String lognote="";
					//获取页面参数和值
					Map<String, String> map = new HashMap<String, String>();
					Enumeration paramNames = req.getParameterNames();
					while (paramNames.hasMoreElements()) {
						String paramName = (String) paramNames.nextElement();
						String[] paramValues = req.getParameterValues(paramName);
						if (paramValues.length >= 1) {
							String paramValue="";
							for (int i = 0; i < paramValues.length; i++) {
								//此处来将状态值存入map
								if(paramName.equals("status")){
									paramValue=status+"☆";
								}else{
									paramValue=paramValue+paramValues[i]+"☆";
								}
							}
							paramValue=paramValue.substring(0,paramValue.length()-1);
							map.put(paramName, paramValue);
						}
					}

					String mainid="";//主表主键值

					//获取form_table表信息
					List<Form_table> tableList=daoCtl.list(dao, Form_table.class, Sqls.create("select * from form_table where formdefid="+formdefid+" order by ismain desc"));
					if (tableList.size()>0) {
						for (Form_table table :tableList) {
							String field_qz="";
							//获取表名
							String tablename=CommonStaticUtil.TABLE_NAME_SUB+table.getTablekey();
							//需要更新的数据
							Map<String,Object> fieldMap=new HashMap<String,Object>();
							fieldMap.put(".table", tablename);
							//查询表字段
							Sql fieldSql=Sqls.create("select * from form_field where status=0 and tableid=@tableid order by isprimary desc,fieldid");
							fieldSql.params().set("tableid", table.getTableid());
							List<Form_field> fieldList=daoCtl.list(dao, Form_field.class, fieldSql);
							fieldSql=Sqls.create("select fieldname,controltype from form_field where status=0 and tableid=@tableid");
							fieldSql.params().set("tableid", table.getTableid());
							Hashtable<String, String> controltypeHT=daoCtl.getHTable(dao, fieldSql); 
							//是否是主表
							if (table.getIsmain()==1) {
								String primary="";
								for (Form_field field : fieldList) {
									if (map.containsKey(field_qz+field.getFieldname())&&field.getControltype()!=15) {
										String fieldvalue=map.get(field_qz+field.getFieldname());
										//临时保存主表 主键id值
										if (field.getIsprimary()==1) {
											mainid=fieldvalue;
											primary=field.getFieldname();
										}
										fieldMap.put(field.getFieldname(), fieldvalue);
									}else if(field.getControltype()==15){//修正主表附件
										YYFormUtil.saveFile(daoCtl,dao,field.getFieldname(), map, field_qz, tablename, mainid);
									}else{
										fieldMap.put(field.getFieldname(), "");
									}
									fieldMap.put("sjc", DateUtil.str2sjc(DateUtil.getCurDateTime()));
								}
								String sqlstr="select * from "+tablename +" where "+primary+"='"+mainid+"'";
								List<Map> oldlist=daoCtl.list(dao, Sqls.create(sqlstr));
								lognote+=SysLogUtil.getLogNoteForm(oldlist.get(0), fieldMap, table.getTableid()+"");
								dao.update(tablename, Chain.from(fieldMap), Cnd.where(primary,"=",mainid));
							}else{
								int datasize=0;//数据数量
								field_qz=table.getTablekey()+".";
								//当前表的外键字段
								String fk_name="";
								String primary="";
								String primary_value="";
								//获取子表每条数据信息
								for (Form_field field : fieldList) {
									if (field.getIsprimary()==1) {
										primary=field.getFieldname();
									}
									if (map.containsKey(field_qz+field.getFieldname())&&field.getControltype()!=15) {
										String fieldvalue=map.get(field_qz+field.getFieldname());
										fieldMap.put(field.getFieldname(), fieldvalue);

										String[] dd=fieldvalue.split("☆");
										if (datasize<dd.length) {
											datasize=dd.length;
										}
									}else{
										//判断request中没有的值是否是外键字段
										if (EmptyUtils.isNotEmpty(field.getForeignkey())) {
											fieldMap.put(field.getFieldname(), mainid);
											fk_name=field.getFieldname();
										}else{
											fieldMap.put(field.getFieldname(), "");
										}
									}
								}
								if(EmptyUtils.isNotEmpty(primary)){//子表有主键
									//删除不存在的子表数据
									if(null!=map.get(field_qz+primary)){
										String findin=StringUtil.getStrsplit(map.get(field_qz+primary).split("☆"));
										String sqlstr="select * from "+tablename+" where "+primary+" not in "+findin+" and "+fk_name+"='"+mainid+"'";
										List<Map> oldlist=daoCtl.list(dao, Sqls.create(sqlstr));
										if(oldlist.size()>0){
											//删除子表数据
											sqlstr="delete from "+tablename+" where "+primary+" not in "+findin+" and "+fk_name+"='"+mainid+"'";
											Sql delsql=Sqls.create(sqlstr);
											dao.execute(delsql);
											//删除关联附件记录
											sqlstr="delete from file_info where tablenem="+tablename+" and tablekey  in "+findin;
											delsql=Sqls.create(sqlstr);
											dao.execute(delsql);
											lognote+=SysLogUtil.getLogNoteForm(oldlist, table.getTableid()+"", "delete");
										}
									}
								}else{//子表无主键，本系统中暂时不做处理

								}

								//封装数据插入或更新到数据库
								for (int i = 0; i < datasize; i++) {
									Map<String,String> sd=new HashMap<String,String>();
									String primarykeyvalue="";
									for (Map.Entry<String, Object> zd : fieldMap.entrySet()) {
										String key=zd.getKey();
										String value=(String) zd.getValue();
										if(EmptyUtils.isEmpty(sd.get(primary))){
											primarykeyvalue= daoCtl.getStrRowValue(dao, Sqls.create("SELECT sys_guid() FROM dual"));
										}else{
											primarykeyvalue=sd.get(primary);
										}
										//增加子表附件
										if ("15".equals(controltypeHT.get(key))) {
											System.out.println("增加子表附件+++++");
											YYFormUtil.saveFile(daoCtl,dao,key, map, field_qz, tablename, primarykeyvalue);
											value="";
										}
										if (value.indexOf("☆")!=-1) {
											String[] dd=value.split("☆");
											if ((i+1)>dd.length) {
												sd.put(key, "");
											}else{
												sd.put(key, dd[i]);
											}

										}else{
											sd.put(key, value);
										}
									}
									if(EmptyUtils.isNotEmpty(primary)){
										if(EmptyUtils.isEmpty(sd.get(primary))){
											sd.put(primary,primarykeyvalue);
											dao.insert(sd);
											lognote+=SysLogUtil.getLogNoteForm(sd, table.getTableid()+"","add");
										}else{
											String sqlstr="select * from "+tablename +" where "+primary+"='"+sd.get(primary)+"'";
											List<Map> oldlist=daoCtl.list(dao, Sqls.create(sqlstr));
											dao.update(tablename, Chain.from(sd), Cnd.where(primary,"=",sd.get(primary)));
											lognote+=SysLogUtil.getLogNoteForm(oldlist.get(0), sd, table.getTableid()+"");
										}
									}else{//子表无主键，本系统中暂时不做处理
									}
									//义务表主键
									String ywid = daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(tablename)));
									
								}
								delSum.set(dao.clear("Early_warning_info", Cnd.where("htid", "=", mainid)));
							}
						}
					}
					String formname=daoCtl.getStrRowValue(dao, Sqls.create("select formtitle from form_def where defid='"+formdefid+"'"));
					//修改证明附件
					if (EmptyUtils.isNotEmpty(file.getFilename()) && EmptyUtils.isNotEmpty(file.getFilepath())) {
						dao.insert(file);
					}
					SysLogUtil.addLogxg(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, true, 2,
							"更新表单【"+ formname +"】", formname, lognote, req.getParameter("log.xgyy"),
							req.getParameter("log.xgyj"),url.replace("@defid", formdefid+"").replace("@mainid", mainid),file.getId());
					re.set(true);
				}
			});
			if(EmptyUtils.isNotEmpty(delSum.get())&&delSum.get() > 0 ){
				YWCL.runCmd(daoCtl, dao);
			}
		} catch (Exception e) {
			re.set(false);
			e.printStackTrace();
		}
		return re.get();
	}



	/**
	 * 功能描述:表单管理浏览页面
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月25日 上午10:32:03
	 *
	 * @param req
	 * @param defid
	 * @param mainid 主表id
	 */
	@At
	@Ok("->:/private/formyy/tablePreview.html")
	public void topreview(HttpServletRequest req,@Param("defid") String defid,@Param("mainid") String mainid,@Param("ywid") String ywid) {
		try {
			req.setAttribute("ywid", ywid);
			/*Form_def olddef=daoCtl.detailByName(dao, Form_def.class, "defid",defid);
			String formhtml=olddef.getFormhtml();
			String restr=FormUtil.generateHtml(formhtml,"view");*/
			String restr=comUtil.formHtmlMap.get(defid+"_view");
			Document doc=Jsoup.parse(restr);
			/**
			 * 查询主表主键字段名称
			 */
			String  zhujian_field="";
			List<Form_table> tableList=daoCtl.list(dao, Form_table.class, Sqls.create("select * from form_table where formdefid="+defid+" order by ismain desc"));
			if (tableList.size()>0) {
				for (Form_table table :tableList) {
					//获取表名
					String tablename=CommonStaticUtil.TABLE_NAME_SUB+table.getTablekey();
					String filed_qz="";
					//获取表中的所有字段
					Sql fieldnamesql=Sqls.create("select fieldname from form_field where status=0 and tableid=@tableid");
					fieldnamesql.params().set("tableid", table.getTableid());
					Sql mainkeysql=Sqls.create("select  fieldname from form_field where status=0 and tableid=@tableid and ISPRIMARY=1");
					mainkeysql.params().set("tableid", table.getTableid());
					String keyfiled=daoCtl.getStrRowValue(dao, mainkeysql);//当前表主键

					//获取编辑数据
					Sql fieldvaluesql=Sqls.create("select * from $table where $id=@id");
					fieldvaluesql.vars().set("table", tablename);
					if (table.getIsmain()==1) {
						//查询主表主键
						zhujian_field=keyfiled;
						fieldvaluesql.vars().set("id", keyfiled);
					}else{
						filed_qz=table.getTablekey()+".";
						//查询子表外键字段
						mainkeysql=Sqls.create("select  fieldname from form_field where status=0 and tableid=@tableid and foreignkey=@foreignkey");
						mainkeysql.params().set("tableid", table.getTableid());
						mainkeysql.params().set("foreignkey", zhujian_field);
						String keyfiled2=daoCtl.getStrRowValue(dao, mainkeysql);
						fieldvaluesql.vars().set("id", keyfiled2);
					}
					fieldvaluesql.params().set("id", mainid);
					List<Map> sd=daoCtl.list(dao, fieldvaluesql);
					Sql fieldSql=Sqls.create("select * from form_field where status=0 and tableid=@tableid ");
					fieldSql.params().set("tableid", table.getTableid());
					List<Form_field> fieldList=daoCtl.list(dao, Form_field.class, fieldSql);
					String sxje="";
					for(Map fkjl:sd){
						if(!"".equals(fkjl.get("fkjl"))&&fkjl.get("fkjl")!=null){
							sxje+=(String) fkjl.get("fkjl")+"。";
							req.setAttribute("qk", "1");
							req.setAttribute("fkjl",sxje);
						}
					}
					restr=YYFormUtil.getHtml(daoCtl,dao,table, doc, sd, fieldList,filed_qz,keyfiled,"view",ywid);	
				}
			}
			req.setAttribute("body", restr);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 功能描述:删除表单应用数据
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月29日 下午1:22:51
	 *
	 * @param session
	 * @param req
	 * @param ids
	 * @param defid
	 * @return
	 */
	@At
	@Ok("raw")
	public boolean delete(HttpSession session,final HttpServletRequest req,final @Param("ids") String ids,final @Param("defid") String defid) {
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		try {
			final Sys_user user=(Sys_user) session.getAttribute("userSession");
			Trans.exec(new Atom() {
				public void run() {

					String idarry =StringUtil.getStrsplit(ids.split(","));
					Sql sql=Sqls.create("select * from form_table where formdefid=@formdefid and ismain=1 ");
					sql.params().set("formdefid", defid);

					List<Form_table> tableList=daoCtl.list(dao, Form_table.class,sql);
					for (Form_table table : tableList) {
						String tablename=CommonStaticUtil.TABLE_NAME_SUB+table.getTablekey();

						Sql mainkeysql=Sqls.create("select  fieldname from form_field where status=0 and tableid=@tableid and ISPRIMARY=1");
						mainkeysql.params().set("tableid", table.getTableid());
						String keyfiled=daoCtl.getStrRowValue(dao, mainkeysql);

						String sqlstr="";
						String temsql="select count(1) from form_field where status=0 and fieldname='status' and tableid="+table.getTableid();
						int i=daoCtl.getIntRowValue(dao, Sqls.create(temsql));
						if(i==0){
							sqlstr="delete from $tablename where $id in "+idarry;
						}else{
							sqlstr="update $tablename set status=-1 where $id in "+idarry;
						}
						Sql delsql=Sqls.create(sqlstr);
						delsql.vars().set("tablename", tablename);
						delsql.vars().set("id", keyfiled);
						dao.execute(delsql);
					}
					String formname=daoCtl.getStrRowValue(dao, Sqls.create("select formtitle from form_def where defid='"+defid+"'"));
					SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, true, 3,
							"删除表单【"+ formname +"】,ID："+ids, formname, "",
							req.getParameter("log.xgyy"), req.getParameter("log.xgyj"),url.replace("@defid", defid+"").replace("@mainid", ids));
					re.set(true);
				}
			});

		} catch (Exception e) {
			re.set(false);
			e.printStackTrace();
		}
		return re.get();
	}
	@At
	public JSONObject loadxyzt(@Param("tname") String tname,@Param("fname") String fname,@Param("sname") String sname,@Param("value") String value) {
		JSONObject jobj=null;
		try {
			Sql searchsql=Sqls.create("select $fname from $tname where $sname=@value");
			searchsql.vars().set("tname", CommonStaticUtil.TABLE_NAME_SUB+tname);
			searchsql.vars().set("fname", fname);
			searchsql.vars().set("sname", sname);
			searchsql.params().set("value", value);
			String xyid=daoCtl.getStrRowValue(dao, searchsql);
			if(EmptyUtils.isNotEmpty(xyid)){
				String xyzt="",xyname="",xycode="",zzjgdm="";
				String[] xyids=xyid.split(",");
				for (String id : xyids) {
					String[] xyzts=StringUtil.null2String(comUtil.xyztMap.get(id)).split("☆");
					xyzt+=id+",";
					if(xyzts.length>=3){
						xyname+=xyzts[0]+",";
						xycode+=xyzts[1]+",";
						zzjgdm+=xyzts[2]+",";
					}else if(xyzts.length>=2){
						xyname+=xyzts[0]+",";
						xycode+=xyzts[1]+",";
					}else if(xyzts.length>=1){
						xyname+=xyzts[0]+",";
					}
				}
				jobj=new JSONObject();
				jobj.put("xyzt", xyzt.substring(0,xyzt.lastIndexOf(",")>0?xyzt.lastIndexOf(","):0));
				jobj.put("xyname", xyname.substring(0,xyname.lastIndexOf(",")>0?xyname.lastIndexOf(","):0));
				jobj.put("xycode", xycode.substring(0,xycode.lastIndexOf(",")>0?xycode.lastIndexOf(","):0));
				jobj.put("zzjgdm", zzjgdm.substring(0,zzjgdm.lastIndexOf(",")>0?zzjgdm.lastIndexOf(","):0));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return jobj;
	}

	@At
	@Ok("->:/private/formyy/selectHt.html")
	public void selectHt(HttpSession session,HttpServletRequest req,@Param("xyml") String xyml,@Param("type") int type) {
		String sqlstr="select defid from form_def where ywtype='1' and xyml='"+xyml+"'";
		int id=daoCtl.getIntRowValue(dao, Sqls.create(sqlstr));
		sqlstr="select * from form_table where ismain=1 and formdefid="+id;
		Form_table table=daoCtl.detailBySql(dao, Form_table.class, Sqls.create(sqlstr));
		boolean is_create=false;
		if(EmptyUtils.isNotEmpty(table)){
			is_create=dao.exists(CommonStaticUtil.TABLE_NAME_SUB+table.getTablekey());
		}
		if(is_create||xyml.equals("0008")){
			req.setAttribute("table", table);
			sqlstr="select lower(fieldname) fieldname,fieldlabel from form_field where status=0 and islist=1 and tableid='"+table.getTableid()+"' order by location asc";
			List<Map> fieldlist=daoCtl.list(dao, Sqls.create(sqlstr));
			req.setAttribute("fieldlist", fieldlist);
			req.setAttribute("id", id);
			sqlstr="select lower(fieldname) fieldname from form_field where status=0 and isprimary=1 and tableid='"+table.getTableid()+"' order by location asc";
			req.setAttribute("primkey", daoCtl.getStrRowValue(dao, Sqls.create(sqlstr)));
			sqlstr="select * from form_query where formdefid='"+id+"'";
			Form_query query=daoCtl.detailBySql(dao, Form_query.class, Sqls.create(sqlstr));
			if (EmptyUtils.isNotEmpty(query)) {
				String queryhtml=FormUtil.getQueryHtml(query.getQueryhtml());
				req.setAttribute("queryhtml", queryhtml);
				req.setAttribute("query", query);
			}
		}else{
			req.setAttribute("primkey","id");
		}
		if(xyml.equals("0008")){
			is_create=true;
		}
		req.setAttribute("isgen", is_create);
		
		if(type==1){
			req.setAttribute("singleSelect", true);
		}else{
			req.setAttribute("singleSelect", false);
		}
	}
	/*
	 * 返回某目录下的合同数
	 */
	public static int getHTS(ObjectCtl daoc,Dao dao,String xyml) {
		String sqlstr="select count(1) from form_def where ywtype='1' and xyml='"+xyml+"'";
		return daoc.getIntRowValue(dao, Sqls.create(sqlstr));
	}

	@At
	@Ok("raw")
	public boolean checkOnly(HttpSession session,@Param("keyvalue") String keyvalue,@Param("id") String id
			,@Param("idname") String idname,@Param("keyname") String keyname,@Param("tablename") String tablename) {
		boolean bol=false;
		try {
			tablename=CommonStaticUtil.TABLE_NAME_SUB+tablename;
			String sql="select count(1) from "+tablename+" where "+keyname+"='"+keyvalue+"'";
			if(EmptyUtils.isNotEmpty(id)){
				sql+=" and "+idname+"!='"+id+"'";
			}
			String ssql="select count(1) from user_tab_cols where column_name='Status' and table_name='"+tablename.toUpperCase()+"' ";
			if(daoCtl.getIntRowValue(dao, Sqls.create(ssql))>0){//非删除项
				sql+=" and (status is null or status!=-1)";
			}
			int count= daoCtl.getIntRowValue(dao, Sqls.create(sql));
			if (count>0) {
				bol=false;
			}else{
				bol=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			bol=false;
		}
		return bol;
	}

	/**
	 *
	 * @param req
	 * @param type 1-单选；2-多选；
	 */
	@At
	@Ok("->:/private/formyy/selectXKZH.html")
	public void selectXKZH(HttpServletRequest req,@Param("type") int type,@Param("conName")String conName) {
		Gson gson = new Gson();
		Map xyztMap = daoCtl.getHTable(dao,Sqls.create(" select id,name||'☆☆'||code from XYZT_INFO  "));
		Map kzMap = daoCtl.getHTable(dao,Sqls.create(" select value,name from cs_value  where typeid = '00020013' "));
		req.setAttribute("kzMap",JSONObject.fromObject(kzMap));
		req.setAttribute("xyztMap",JSONObject.fromObject(xyztMap));
		req.setAttribute("conName",conName);
		if(type==1){
			req.setAttribute("singleSelect", true);
		}else{
			req.setAttribute("singleSelect", false);
		}
	}

	/**
	 *
	 * @param req
	 * @param type 1-单选；2-多选；
	 */
	@At
	@Ok("->:/private/formyy/selectTK_XKZH.html")
	public void selectTKXKZH(HttpServletRequest req,@Param("type") int type,@Param("conName")String conName) {
		Gson gson = new Gson();
		Map xyztMap = daoCtl.getHTable(dao,Sqls.create(" select id,name||'☆☆'||code from XYZT_INFO  "));
		Map kzMap = daoCtl.getHTable(dao,Sqls.create(" select value,name from cs_value  where typeid = '00020013' "));
		req.setAttribute("kzMap",JSONObject.fromObject(kzMap));
		req.setAttribute("xyztMap",JSONObject.fromObject(xyztMap));
		req.setAttribute("conName",conName);
		if(type==1){
			req.setAttribute("singleSelect", true);
		}else{
			req.setAttribute("singleSelect", false);
		}
	}
	
	@At
	@Ok("->:/private/formyy/selectXzcf.html")
	public void selectXzcf(HttpServletRequest req,@Param("type") int type,@Param("conName")String conName) {
		Gson gson = new Gson();
		Map xyztMap = daoCtl.getHTable(dao,Sqls.create(" select id,name||'☆☆'||code from XYZT_INFO  "));
		req.setAttribute("xyztMap",JSONObject.fromObject(xyztMap));
		req.setAttribute("conName",conName);
		if(type==1){
			req.setAttribute("singleSelect", true);
		}else{
			req.setAttribute("singleSelect", false);
		}
		//惩戒状态
		Sql sql1 = Sqls.create("select value,name from cs_value where typeid = 00010007");
		List<Map> cjtype = daoCtl.list(dao, sql1);
		Hashtable<String, String> cjTable = daoCtl.getHTable(dao, sql1);
		//发布状态
		Sql sql2 = Sqls.create("select value,name from cs_value where typeid = 00010009");
		Hashtable<String, String> fbtype = daoCtl.getHTable(dao, sql2);
		req.setAttribute("cjTable", JSONObject.fromObject(cjTable));
		req.setAttribute("fbTable", JSONObject.fromObject(fbtype));
		req.setAttribute("cjMap", cjtype);
		req.setAttribute("xyztMap", comUtil.xyztlxMap);
		req.setAttribute("statusMap", JSONObject.fromObject(comUtil.statusMap));
	}
	
}