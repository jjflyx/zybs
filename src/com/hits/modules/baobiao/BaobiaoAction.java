package com.hits.modules.baobiao;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hits.common.util.PinyinUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.google.gson.Gson;
import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.modules.bean.Cs_value;
import com.hits.modules.com.comUtil;
import com.hits.modules.gtxt.bean.L_ckqht;
import com.hits.modules.gtxt.bean.L_gt_xzxkxx;
import com.hits.modules.sxcj.bean.Discredit_info;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.DateUtil;
import com.hits.util.EmptyUtils;
import com.hits.util.PageObj;
import com.hits.util.StringUtil;
import com.hits.util.SysLogUtil;


/**
 *
 *
 * 功能说明: 统计报表
 *
 *
 */
@IocBean
@At("/private/baobiao")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class BaobiaoAction extends BaseAction {
	@Inject
	protected Dao dao;

	/*
	 * 操作统计
	 */
	@At
	@Ok("->:/private/baobiao/cztj.html")
	public void cztj(HttpSession session, HttpServletRequest req, @Param("startdate") String startdate,
					 @Param("enddate") String enddate, @Param("type") String type) {
		try {
			String sqlstr="";
			String tempsql="";
			startdate=EmptyUtils.isEmpty(startdate)?"2016-03-01":startdate;
			enddate=EmptyUtils.isEmpty(enddate)?"2016-03-28":enddate;
			if(EmptyUtils.isNotEmpty(startdate)){
				tempsql+=" t.create_time>='"+startdate+" 00:00:00' and ";
			}
			if(EmptyUtils.isNotEmpty(enddate)){
				tempsql+=" t.create_time<='"+enddate+" 23:59:59' and ";
			}
			if("mk".equals(type)){//按模块
				sqlstr="select title name,count(1) value from SYS_LOG t where "+tempsql+" type = '3' group by title";
			}else{//按操作人单位
				req.setAttribute("unitMap",comUtil.unitMap);
				sqlstr="select u.unitid name,count(1) value from  Sys_log t,sys_user u where "+tempsql+" t.loginname=u.loginname and type='3' group by u.unitid order by u.unitid";
			}
			System.out.println(sqlstr);
			req.setAttribute("type",type);
			req.setAttribute("startdate",startdate);
			req.setAttribute("enddate",enddate);
			req.setAttribute("list", daoCtl.list(dao, Sqls.create(sqlstr)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 异议统计 by Numbgui
	 * @param session
	 * @param req
	 * @param startdate
	 * @param xylx
	 * @param xzqh
	 * @param enddate
	 * @param type
	 */
	@At
	@Ok("->:/private/baobiao/yytj.html")
	public void yytj(HttpSession session, HttpServletRequest req, @Param("startdate") String startdate,@Param("xylx")String xylx,@Param("xzqh")String xzqh,
					 @Param("enddate") String enddate, @Param("type") String type) {
		try {
			xzqh = EmptyUtils.isEmpty(xzqh)?"340000" : xzqh;
			Sys_user user=(Sys_user) session.getAttribute("userSession");
			if(user.getRolelist().contains("645")){
				req.setAttribute("sxld", "1");
				xzqh=daoCtl.detailByName(dao, Sys_unit.class, user.getUnitid()).getXzqh();
			}
			enddate = EmptyUtils.isEmpty(enddate)?DateUtil.getToday():enddate;
			startdate = EmptyUtils.isEmpty(startdate)?DateUtil.getFirstMonDay(enddate):startdate;
			/*****************信用类型显示（表头）************************/
			String xymlSql = " select value,name from cs_value where typeid='00010005' and state=0 and value like '____' and value < '0008' order by location asc,id asc ";
			List<Cs_value> csValueList = daoCtl.list(dao, Cs_value.class,Sqls.create(xymlSql));
			if(EmptyUtils.isNotEmpty(xylx)){
				xymlSql = " select value,name from cs_value where typeid='00010005' and state=0 and value like '"+xylx+"%' and value < '0008' order by location asc,id asc ";
			}
			//表头显示
			List<Cs_value> baobiaoHeadList = daoCtl.list(dao, Cs_value.class,Sqls.create(xymlSql));
			/**********************************************************/

			/*****************单位显示（列）************************/
			req.setAttribute("xzqhObj",daoCtl.detailByCnd(dao,Cs_value.class, Cnd.where("typeid","=","00010004").and("state","=",0).and("value","=",xzqh)));
			String xzqhSql = " xzqh ";
			String xzqhLike = "%";
			if(xzqh.substring(2,xzqh.length()).equals("0000")){
				xzqh = xzqh.substring(0,2)+"__00";
				xzqhSql = " substr(xzqh,0,4)||'00' ";
				xzqhLike = xzqh.substring(0,2)+"%";
			}else if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
				xzqhLike = xzqh.substring(0,4)+"%";
			}

			List<Cs_value> xzqhList = daoCtl.list(dao,Cs_value.class,Sqls.create(" select value,name from cs_value where typeid='00010004' and state=0 and value like '"+xzqh+"'  order by location asc,id asc "));
			/*****************************************************/

			/***************************行业协会************************/
			List<String> hyxhList = daoCtl.getStrRowValues(dao,Sqls.create(" select id from sys_unit where unittype = 90 order by id "));
			String hyxhId = "";
			for(String hyxh : hyxhList){
				hyxhId += hyxh+",";
			}
			hyxhId = hyxhId.substring(0,hyxhId.length()-1);
			System.out.println("hyxhId :　"+hyxhId);
			/**********************************************************/

			/*******************查询报表数据***********************/
			Map<String,Map<String,Map<String,String>>> allListMap = new HashMap<String, Map<String, Map<String,String>>>();
			for(Cs_value csValue : baobiaoHeadList) {
				allListMap.put(csValue.getValue(),new HashMap<String,Map<String, String>>());
				//未处理
				Map<String, String> wclMap = daoCtl.getHTable(dao, Sqls.create(" select " + xzqhSql + ",count(id) from WARNEXCEPTION where " +
						" xzqh like '" + xzqhLike + "' and xy_type like '" + csValue.getValue() + "%' and unit not in (" + hyxhId + ") and state = 0 and add_time between '"+startdate+" 00:00:00' and '"+enddate+" 23:59:59' group by " + xzqhSql));
				allListMap.get(csValue.getValue()).put("wclMap",wclMap);
				//处理中
				Map<String, String> clzMap = daoCtl.getHTable(dao, Sqls.create(" select " + xzqhSql + ",count(id) from WARNEXCEPTION where " +
						" xzqh like '" + xzqhLike + "' and xy_type like '" + csValue.getValue() + "%' and unit not in (" + hyxhId + ") and state = 1 and add_time between '"+startdate+" 00:00:00' and '"+enddate+" 23:59:59' group by " + xzqhSql));
				allListMap.get(csValue.getValue()).put("clzMap",clzMap);
				//维持惩戒
				Map<String, String> wcMap = daoCtl.getHTable(dao, Sqls.create(" select " + xzqhSql + ",count(id) from WARNEXCEPTION where " +
						" xzqh like '" + xzqhLike + "' and xy_type like '" + csValue.getValue() + "%' and unit not in (" + hyxhId + ") and state= 2 and cz = 1 and add_time between '"+startdate+" 00:00:00' and '"+enddate+" 23:59:59' group by " + xzqhSql));
				allListMap.get(csValue.getValue()).put("wcMap",wcMap);
				//修改惩戒
				Map<String, String> xgMap = daoCtl.getHTable(dao, Sqls.create(" select " + xzqhSql + ",count(id) from WARNEXCEPTION where " +
						" xzqh like '" + xzqhLike + "' and xy_type like '" + csValue.getValue() + "%' and unit not in (" + hyxhId + ") and state= 2 and cz = 2 and add_time between '"+startdate+" 00:00:00' and '"+enddate+" 23:59:59' group by " + xzqhSql));
				allListMap.get(csValue.getValue()).put("xgMap",xgMap);
				//撤销惩戒
				Map<String, String> cxMap = daoCtl.getHTable(dao, Sqls.create(" select " + xzqhSql + ",count(id) from WARNEXCEPTION where " +
						" xzqh like '" + xzqhLike + "' and xy_type like '" + csValue.getValue() + "%' and unit not in (" + hyxhId + ") and state= 2 and cz = 3 and add_time between '"+startdate+" 00:00:00' and '"+enddate+" 23:59:59' group by " + xzqhSql));
				allListMap.get(csValue.getValue()).put("cxMap", cxMap);
				if(xzqh.equals("34__00")) {
					//行业协会-未处理
					allListMap.put("hyxh", new HashMap<String, Map<String, String>>());
					Map<String, String> h_wclMap = daoCtl.getHTable(dao, Sqls.create(" select 'wcl',count(id) from WARNEXCEPTION where " +
							" xy_type like '" + csValue.getValue() + "%' and unit in (" + hyxhId + ") and state = 0 and add_time between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' "));
					allListMap.get(csValue.getValue()).put("h_wclMap", h_wclMap);
					//行业协会-处理中
					Map<String, String> h_clzMap = daoCtl.getHTable(dao, Sqls.create(" select 'clz',count(id) from WARNEXCEPTION where " +
							" xy_type like '" + csValue.getValue() + "%' and unit in (" + hyxhId + ") and state = 1 and add_time between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' "));
					allListMap.get(csValue.getValue()).put("h_clzMap", h_clzMap);
					//行业协会-维持惩戒
					Map<String, String> h_wcMap = daoCtl.getHTable(dao, Sqls.create(" select 'wc',count(id) from WARNEXCEPTION where " +
							" xy_type like '" + csValue.getValue() + "%' and unit in (" + hyxhId + ") and state= 2 and cz = 1 and add_time between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' "));
					allListMap.get(csValue.getValue()).put("h_wcMap", h_wcMap);
					//行业协会-修改惩戒
					Map<String, String> h_xgMap = daoCtl.getHTable(dao, Sqls.create(" select 'xg',count(id) from WARNEXCEPTION where " +
							" xy_type like '" + csValue.getValue() + "%' and unit in (" + hyxhId + ") and state= 2 and cz = 2 and add_time between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'"));
					allListMap.get(csValue.getValue()).put("h_xgMap", h_xgMap);
					//撤销惩戒
					Map<String, String> h_cxMap = daoCtl.getHTable(dao, Sqls.create(" select 'cx',count(id) from WARNEXCEPTION where " +
							" xy_type like '" + csValue.getValue() + "%' and unit in (" + hyxhId + ") and state= 2 and cz = 3 and add_time between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' "));
					allListMap.get(csValue.getValue()).put("h_cxMap", h_cxMap);
				}
			} 
			/*****************************************************/

			req.setAttribute("allListMap",allListMap);
			req.setAttribute("type",type);
			req.setAttribute("startdate",startdate);
			req.setAttribute("enddate",enddate);
			req.setAttribute("baobiaoHeadList", baobiaoHeadList);
			req.setAttribute("xzqhList", xzqhList);
			req.setAttribute("xylx",xylx);
			req.setAttribute("csvalueList",csValueList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 异议统计钻取页面
	 * @param req
	 * @param session
	 * @param xzqh
	 * @param xy_type
	 * @param startdate
	 * @param enddate
	 */
	@At
	@Ok("->:/private/baobiao/yytjList.html")
	public void yytjYm( HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xy_type")String xy_type,@Param("startdate")String startdate,@Param("enddate")String enddate,
			@Param("state")String state,@Param("cz")String cz){
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		if(user.getRolelist().contains("506") || user.getRolelist().contains("507")) {
			req.setAttribute("leader","处理");
		}else{
			req.setAttribute("leader","<a href='javascript:update(\"+row.id+\",\\\"1\\\");'>处理</a>");
		}
		req.setAttribute("unitid",user.getUnitid());
		req.setAttribute("startdate",startdate);
		req.setAttribute("enddate",enddate);
		req.setAttribute("xzqh",xzqh);
		req.setAttribute("xy_type", xy_type);
		req.setAttribute("state",state);
		req.setAttribute("cz", cz);
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
	}
	
	/**
	 * 异议统计列表
	 * @param start_date
	 * @param end_date
	 * @param keyword
	 * @param source
	 * @param unitid
	 * @param curPage
	 * @param pageSize
	 * @param session
	 * @return
	 */
	@At
	@Ok("raw")
	public JSONObject yytjList(@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("keyword")String keyword,@Param("source")String source,@Param("xyzt_id")String xyzt_id,
		@Param("page") int curPage, @Param("rows") int pageSize,HttpSession session,@Param("cz")String cz,@Param("state")int state,@Param("xy_type")String xy_type,@Param("xzqh")String xzqh){
		Sql sql = null;
		sql = Sqls.create(" select w.id,w.sxcj_id,w.xyzt_id,w.contract_id,w.applicant,w.add_time,w.note,w.code,w.state,w.contract_name,z.name xyzt_name,w.source from WARNEXCEPTION w,xyzt_info z where w.xyzt_id=z.id and w.state <> -1  $s ");
		String s = " AND w.add_time between '"+startdate+" 00:00:00' and '"+enddate+" 23:59:59'  ";
		/***************************行业协会************************/
		List<String> hyxhList = daoCtl.getStrRowValues(dao,Sqls.create(" select id from sys_unit where unittype = 90 order by id "));
		String hyxhId = "";
		for(String hyxh : hyxhList){
			hyxhId += hyxh+",";
		}
		hyxhId = hyxhId.substring(0,hyxhId.length()-1);
		if(xzqh.equals("hyxh")){
			s=s+" and w.unit in (" + hyxhId + ")";
		}else{
			if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}
			s=s+" and w.unit not in (" + hyxhId + ") and w.xzqh like '"+xzqh+"'";
		}
		if(state==2){
			s=s+" and w.state='"+state+"' and w.cz='"+cz+"'";
		}else{
			s=s+" and w.state='"+state+"'";
		}
		if(EmptyUtils.isNotEmpty(keyword)){
			s = s +  " AND w.applicant like '%"+keyword+"%' ";
		}
		if(EmptyUtils.isNotEmpty(source) && !"".equals(source)){
			s += " AND w.source = '"+source+"' ";
		}
		if(EmptyUtils.isNotEmpty(xyzt_id)){
			s  += " and w.xyzt_id = '"+xyzt_id+"'";
		}
//		if(EmptyUtils.isNotEmpty(unitid)){
//			s += " and unit = '"+unitid+"' ";
//		}
		s = s + " order by w.state asc , w.add_time desc ";
		sql.vars().set("s", s);
		return daoCtl.listPageJsonSql(dao, sql, curPage, pageSize);
	}
	
	//异议统计按照信用主体查询
	@At
	@Ok("raw")
	public String yyxyztSel( HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyztsel")String xyzt_type,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("xy_type")String xy_type,@Param("state")int state,
			@Param("cz")String cz,@Param("page") int curPage, @Param("rows") int pageSize){
		Sql sql = null;
		String sqlstr="";
		String hyxhId="";
		List<Sys_unit> unitList=daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
		for(Sys_unit unit : unitList){
			hyxhId += unit.getId()+",";
		}
		hyxhId = hyxhId.substring(0,hyxhId.length()-1);
		if(xzqh.equals("hyxh")){
			sqlstr = "select a.xyzt_id,b.name,b.type as xyzt_type,count(1) as yyjl"+
			" from WARNEXCEPTION a,xyzt_info b where a.xyzt_id = b.id and a.unit in (" + hyxhId + ")";
		}else{
			if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}
			sqlstr = "select a.xyzt_id,b.name,b.type as xyzt_type,count(1) as yyjl from "
					+ "WARNEXCEPTION a,xyzt_info b,sys_unit d where a.xyzt_id = b.id and a.unit=d.id and a.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
		}
		if(state==2){
			sqlstr+=" and a.state='"+state+"' and a.cz='"+cz+"'";
		}else{
			sqlstr+=" and a.state='"+state+"'";
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			sqlstr += "and a.add_time between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
		}
		sqlstr += " group by a.xyzt_id,b.name,b.type ";
		System.out.println("sqlstr:"+sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		return PageObj.getPageJSON(qr);
	}	

	/**
	 * 统计分析-守信奖励 by.Numbgui 2016-03-26
	 * @param session
	 * @param req
	 * @param startdate
	 * @param enddate
	 * @param type
	 * @param xzqh
	 */
	@At
	@Ok("->:/private/baobiao/sxjltj.html")
	public void sxjl(HttpSession session, HttpServletRequest req, @Param("startdate") String startdate,
					 @Param("enddate") String enddate, @Param("type") String type,@Param("xzqh") String xzqh) {
		try {
			xzqh = EmptyUtils.isEmpty(xzqh)?"340000" : xzqh;
			Sys_user user=(Sys_user) session.getAttribute("userSession");
			if(user.getRolelist().contains("645")){
				req.setAttribute("sxld", "1");
				xzqh=daoCtl.detailByName(dao, Sys_unit.class, user.getUnitid()).getXzqh();
			}
			Gson gson = new Gson();
			
			enddate = EmptyUtils.isEmpty(enddate)?DateUtil.getToday():enddate;
			startdate = EmptyUtils.isEmpty(startdate)?DateUtil.getFirstMonDay(enddate):startdate;
			req.setAttribute("startdate",startdate);
			req.setAttribute("enddate",enddate);
			//行政区划
			req.setAttribute("xzqhlist", comUtil.xzqhList);
			List<Sys_unit> unitList = null;
			unitList =daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
			//如果是340000表示是省级的，省级需要包含行业协会信息
			if("340000".equals(xzqh)){
				req.setAttribute("unitList",unitList);
			}
			req.setAttribute("xzqhObj",daoCtl.detailByCnd(dao,Cs_value.class, Cnd.where("typeid","=","00010004").and("state","=",0).and("value","=",xzqh)));
			if(xzqh.substring(2,xzqh.length()).equals("0000")){
				xzqh = xzqh.substring(0,2)+"__00";
			}else if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}

			//得到列，行政区划
			List<Cs_value> xzqhList = daoCtl.list(dao,Cs_value.class,Sqls.create(" select value,name from cs_value where typeid='00010004' and state=0 and value like '"+xzqh+"'  order by location asc,id asc "));
			req.setAttribute("xzqhList",xzqhList);
			req.setAttribute("xzqhGson",gson.toJson(xzqhList));
			List<Cs_value> xyList = daoCtl.list(dao,Cs_value.class,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' order by location asc "));
			Map<String,String> xyMap = daoCtl.getHTable(dao,Sqls.create("select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' "));
			for(Map.Entry<String, String> entry:xyMap.entrySet()){
				xyMap.put(entry.getKey(), PinyinUtil.cn2py(xyMap.get(entry.getKey()).replace("、","")));
			}
			req.setAttribute("xyGson",gson.toJson(xyList));

			String charsStr = "";
			//图形报表Map
			Map<String,String> xyNameMap = daoCtl.getHTable(dao,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' order by location asc"));
			List<String> xyCode = daoCtl.getStrRowValues(dao,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' order by location asc"));
				
			req.setAttribute("xyNameMap",gson.toJson(xyNameMap));

			//开始处理报表数据
			Map<String,Map<String,String>> allMap = new HashMap<String, Map<String, String>>();
			JSONArray array = new JSONArray();
			//行业协会数据  分别展示数据
			if("34__00".equals(xzqh)){
				for(Sys_unit unit : unitList){
					System.out.println("unit : "+unit.getId());
					Map<String,String> csMap = daoCtl.getHTable(dao,Sqls.create(" select xy_type,count(id) from REWARD_INFO where unitid = '"+unit.getId()+"' and type = 0 and  create_date between '" + startdate + "' and '" + enddate + "' group by xy_type "));
					allMap.put(unit.getId(),csMap);
					JSONObject jsonroot = new JSONObject();
					jsonroot.put("id", unit.getId());
					jsonroot.put("name",unit.getName());
					for(Map.Entry<String, String> entry:csMap.entrySet()){
						jsonroot.put(xyMap.get(entry.getKey()),entry.getValue());
					}
					array.add(jsonroot);
				}
			}
			
			//行业协会总体展示数据
			String hyxhId = "";
			for(Sys_unit unit : unitList){
				hyxhId += unit.getId()+",";
			}
			hyxhId = hyxhId.substring(0,hyxhId.length()-1);
			if("34__00".equals(xzqh)){
				System.out.println("hyxhId :　"+hyxhId);
				Map<String,String> csMap = daoCtl.getHTable(dao,Sqls.create(" select xy_type,count(id) from REWARD_INFO where unitid in (" + hyxhId + ") and type = 0 and  create_date between '" + startdate + "' and '" + enddate + "' group by xy_type "));
				req.setAttribute("hyxhMap", csMap);
			}
			
			for(Cs_value csValue : xzqhList){
				String xzqhStr = csValue.getValue();
				if(xzqhStr.substring(4,xzqhStr.length()).equals("00")){
					xzqhStr = xzqhStr.substring(0,4)+"__";
				}
				Map<String,String> csMap = daoCtl.getHTable(dao,Sqls.create(" select xy_type,count(id) from REWARD_INFO where xzqh like '"+xzqhStr+"' and  type = 0 and  create_date between '" + startdate + "' and '" + enddate + "' group by xy_type "));
				if(!hyxhId.isEmpty()){
					csMap = daoCtl.getHTable(dao,Sqls.create(" select xy_type,count(id) from REWARD_INFO where xzqh like '"+xzqhStr+"' and unitid not in (" + hyxhId + ") and type = 0 and  create_date between '" + startdate + "' and '" + enddate + "' group by xy_type "));
				}
				allMap.put(csValue.getValue(),csMap);
				JSONObject jsonroot = new JSONObject();
				jsonroot.put("id", csValue.getValue());
				jsonroot.put("name",csValue.getName());
				for(Map.Entry<String, String> entry:csMap.entrySet()){
					jsonroot.put(xyMap.get(entry.getKey()),entry.getValue());
				}
				array.add(jsonroot);
			}
			req.setAttribute("charsData", array.toString());
			req.setAttribute("allMap",allMap);

		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//奖励信息按照信用主体查询
	@At
	@Ok("raw")
	public String jlxyztSel( HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyztsel")String xyzt_type,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("xy_type")String xy_type,
			@Param("page") int curPage, @Param("rows") int pageSize){
		Sql sql = null;
		String sqlstr="";
		String hyxhId="";
		List<Sys_unit> unitList=daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
		for(Sys_unit unit : unitList){
			hyxhId += unit.getId()+",";
		}
		hyxhId = hyxhId.substring(0,hyxhId.length()-1);
		if(xzqh.equals("hyxh")){
			sqlstr = "select a.xyzt_id,b.name,b.type as xyzt_type,count(1) as sxjl"+
			" from reward_info a,xyzt_info b where a.xyzt_id = b.id and a.unit in (" + hyxhId + ")";
		}else{
			if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}
			sqlstr = "select a.xyzt_id,b.name,b.type as xyzt_type,count(1) as sxjl from "
					+ "reward_info a,xyzt_info b,sys_unit d where a.xyzt_id = b.id and a.unitid=d.id and d.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
		}
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr += "and b.type = '"+xyzt_type+"'";
		}
		if(EmptyUtils.isNotEmpty(xy_type)){
			sqlstr += "and a.xy_type = '"+xy_type+"'";
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			sqlstr += "and a.create_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
		}
		sqlstr += " group by a.xyzt_id,b.name,b.type ";
		System.out.println("sqlstr:"+sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		return PageObj.getPageJSON(qr);
	}
	
	@At
	@Ok("->:/private/baobiao/sxjlList.html")
	public void sxjlYm( HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xy_type")String xy_type,@Param("startdate")String startdate,@Param("enddate")String enddate){
		req.setAttribute("startdate",startdate);
		req.setAttribute("enddate",enddate);
		req.setAttribute("xzqh",xzqh);
		req.setAttribute("xy_type", xy_type);
		req.setAttribute("ztlxlist", comUtil.xyztlxMap);
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("sxxwtypeMap", JSONObject.fromObject(comUtil.sxxwtypeMap));
	}
	
	@At
	@Ok("raw")
	public String sxjlList( HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyzt_type")String xyzt_type,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("xy_type")String xy_type,@Param("xyzt_id")String xyzt_id,
			@Param("page") int curPage, @Param("rows") int pageSize){
		Sql sql = null;
		String sqlstr="";
		String hyxhId="";
		List<Sys_unit> unitList=daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
		for(Sys_unit unit : unitList){
			hyxhId += unit.getId()+",";
		}
		hyxhId = hyxhId.substring(0,hyxhId.length()-1);
		if(xzqh.equals("hyxh")){
			sqlstr = "select a.id as jlid,a.xy_type,a.jl_date,b.name,b.type,a.note from reward_info a,xyzt_info b where unitid in (" + hyxhId + ") and a.xyzt_id = b.id and a.type = 0 ";
		}else{
			if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}
			sqlstr = "select a.id as jlid,a.xy_type,a.jl_date,b.name,b.type,a.note from reward_info a,xyzt_info b where xzqh like '"+xzqh+"' and unitid not in (" + hyxhId + ") and a.xyzt_id = b.id and a.type = 0 ";
		}
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr += "and b.type = '"+xyzt_type+"'";
		}
		if(EmptyUtils.isNotEmpty(xyzt_id)){
			sqlstr += "and a.xyzt_id = '"+xyzt_id+"'";
		}
		if(EmptyUtils.isNotEmpty(xy_type)){
			sqlstr += "and a.xy_type = '"+xy_type+"'";
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			sqlstr += "and a.create_date between '"+startdate+"' and '"+enddate+"'";
		}
		sqlstr += " order by a.id desc";
		System.out.println("sqlstr:"+sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		return PageObj.getPageJSON(qr);
	}
	
	@At
	@Ok("->:/private/baobiao/xxsb.html")
	public void xxsb(HttpSession session, HttpServletRequest req, @Param("startdate") String startdate,
			 @Param("enddate") String enddate, @Param("type") String type,@Param("xzqh") String xzqh){
		try {
			xzqh = EmptyUtils.isEmpty(xzqh)?"340000" : xzqh;
			Sys_user user=(Sys_user) session.getAttribute("userSession");
			if(user.getRolelist().contains("645")){
				req.setAttribute("sxld", "1");
				xzqh=daoCtl.detailByName(dao, Sys_unit.class, user.getUnitid()).getXzqh();
			}
			Gson gson = new Gson();
			enddate = EmptyUtils.isEmpty(enddate)?DateUtil.getToday():enddate;
			startdate = EmptyUtils.isEmpty(startdate)?DateUtil.getFirstMonDay(enddate):startdate;
			req.setAttribute("startdate",startdate);
			req.setAttribute("enddate",enddate);
			//行政区划
			
			List<Sys_unit> unitList = null;
			unitList =daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
			//如果是340000表示是省级的，省级需要包含行业协会信息
			if("340000".equals(xzqh)){
				
				req.setAttribute("unitList",unitList);
				req.setAttribute("unitGson",gson.toJson(unitList));
			}
			req.setAttribute("xzqhObj",daoCtl.detailByCnd(dao,Cs_value.class, Cnd.where("typeid","=","00010004").and("state","=",0).and("value","=",xzqh)));
			if(xzqh.substring(2,xzqh.length()).equals("0000")){
				xzqh = xzqh.substring(0,2)+"__00";
			}else if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}

			//得到列，行政区划
			List<Cs_value> xzqhList = daoCtl.list(dao,Cs_value.class,Sqls.create(" select value,name from cs_value where typeid='00010004' and state=0 and value like '"+xzqh+"'  order by location asc,id asc "));
			req.setAttribute("xzqhList",xzqhList);
			req.setAttribute("xzqhGson",gson.toJson(xzqhList));
			List<Cs_value> xyList = daoCtl.list(dao,Cs_value.class,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code like '____' order by location asc "));
			Map<String,String> xyMap = daoCtl.getHTable(dao,Sqls.create("select code,name from cs_value where typeid = '00010005' and state = 0 and code like '____'"));
			for(Map.Entry<String, String> entry:xyMap.entrySet()){
				xyMap.put(entry.getKey(), PinyinUtil.cn2py(xyMap.get(entry.getKey()).replace("、","")));
			}
			req.setAttribute("xyGson",gson.toJson(xyList));

			String charsStr = "";
			//图形报表Map
			Map<String,String> xyNameMap = daoCtl.getHTable(dao,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' order by location asc"));
			List<String> xyCode = daoCtl.getStrRowValues(dao,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' order by location asc"));

			req.setAttribute("xyNameMap",gson.toJson(xyNameMap));
			//开始处理报表数据
			Map<String,Map<String,String>> allMap = new HashMap<String, Map<String, String>>();
			String groupBy="xy_type";
			JSONArray array = new JSONArray();
			
			//行业协会总体展示数据
			String hyxhId = "";
			for(Sys_unit unit : unitList){
				hyxhId += unit.getId()+",";
			}
			hyxhId = hyxhId.substring(0,hyxhId.length()-1);
			if("34__00".equals(xzqh)){
				Map<String,String> csMap = daoCtl.getHTable(dao,Sqls.create("select cz,count(l.id) from  sys_log l,sys_user us,sys_unit un where l.loginname=us.loginname and us.unitid=un.id and  un.unittype=90 and  create_time between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by cz"));
				req.setAttribute("hyxhMap", csMap);
			}

			for(Cs_value csValue : xzqhList){
				String xzqhStr = csValue.getValue();
				if(xzqhStr.substring(4,xzqhStr.length()).equals("00")){
					xzqhStr = xzqhStr.substring(0,4)+"__";
				}
				String sqlstr=" select cz,count(l.id) from  sys_log l,sys_user us,sys_unit un where l.loginname=us.loginname and us.unitid=un.id and unitid not in("+hyxhId+") and un.xzqh like '"+xzqhStr+"' and  create_time between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by cz";
				Map<String,String> csMap = daoCtl.getHTable(dao,Sqls.create(sqlstr));
				allMap.put(csValue.getValue(),csMap);
				JSONObject jsonroot = new JSONObject();
				jsonroot.put("id", csValue.getValue());
				jsonroot.put("name",csValue.getName());
				for(Map.Entry<String, String> entry:csMap.entrySet()){
					if(EmptyUtils.isNotEmpty(xyMap.get(entry.getKey()))){
						jsonroot.put(xyMap.get(entry.getKey()),entry.getValue());
					}
				}
				array.add(jsonroot);
			}
			req.setAttribute("charsData", array.toString());
			req.setAttribute("allMap",allMap);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//信息上报按照业务模块来查询
	@At
	@Ok("raw")
	public String ywmkSel( HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyzt_type")String xyzt_type,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("xy_type")String xy_type,@Param("cz")String cz,
			@Param("page") int curPage, @Param("rows") int pageSize){
		Sql sql = null;
		String sqlstr="";
		String hyxhId="";
		List<Sys_unit> unitList=daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
		for(Sys_unit unit : unitList){
			hyxhId += unit.getId()+",";
		}
		hyxhId = hyxhId.substring(0,hyxhId.length()-1);
		if(xzqh.equals("hyxh")){
			sqlstr = "select title,l.cz,count(1) as czjl"+
			" from sys_log l,sys_user us,sys_unit un where l.loginname=us.loginname and us.unitid=un.id  and un.unittype = 90 ";
		}else{
			if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}
			sqlstr = "select title,l.cz,count(1) as czjl from "
					+ "sys_log l,sys_user us,sys_unit un where l.loginname=us.loginname and us.unitid=un.id  and un.xzqh like '"+xzqh+"' and us.unitid not in (" + hyxhId + ")  ";
		}
		
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			sqlstr += "and create_time between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
		}	
		if(EmptyUtils.isNotEmpty(cz)){
			sqlstr+=" and l.cz='"+cz+"' ";
		}
		sqlstr += "and l.type<>0 group by l.title,l.cz ";
		System.out.println("sqlstr:"+sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		return PageObj.getPageJSON(qr);
	}
	
	//按照厅下属部门来查询
	@At
	@Ok("raw")
	public String xxsbXsbmSel( HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyzt_type")String xyzt_type,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("xy_type")String xy_type,@Param("cz")String cz,
			@Param("page") int curPage, @Param("rows") int pageSize){
		Sql sql = null;
		String sqlstr="";
		String hyxhId="";
		List<Sys_unit> unitList=daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
		for(Sys_unit unit : unitList){
			hyxhId += unit.getId()+",";
		}
		hyxhId = hyxhId.substring(0,hyxhId.length()-1);
		if(xzqh.equals("hyxh")){
			sqlstr = "select un.id,un.name,l.cz,count(1) as czjl"+
			" from sys_log l,sys_user us,sys_unit un where l.loginname=us.loginname and us.unitid=un.id  and un.unittype = 90 ";
		}else{
			if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}
			sqlstr = "select un.id,un.name,l.cz,count(1) as czjl from "
					+ "sys_log l,sys_user us,sys_unit un where l.loginname=us.loginname and us.unitid=un.id  and un.xzqh like '"+xzqh+"' and us.unitid not in (" + hyxhId + ")  ";
		}
		
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			sqlstr += "and create_time between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
		}	
		if(EmptyUtils.isNotEmpty(cz)){
			sqlstr+=" and l.cz='"+cz+"' ";
		}
		sqlstr += "and l.type<>0 group by un.id,un.name,l.cz ";
		System.out.println("sqlstr:"+sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		return PageObj.getPageJSON(qr);
	}
	
	
	
	
	@At
	@Ok("->:/private/baobiao/xxsbList.html")
	public void xxsbYm( HttpServletRequest request,HttpSession session,@Param("xzqh")String xzqh,@Param("cz")String cz,@Param("startdate")String startdate,@Param("enddate")String enddate){
		Gson gson=new Gson();
		Hashtable<String, String> usermap=daoCtl.getHTable(dao, Sqls.create("select loginname,realname from sys_user "));
		request.setAttribute("userMap", gson.toJson(usermap));
		List<String> titleType = daoCtl.getStrRowValues(dao,Sqls.create(" select title from SYS_LOG where type = '3' group by title "));
		request.setAttribute("titleType",titleType);
		request.setAttribute("logMap", comUtil.logCzMap);
		request.setAttribute("startdate", request.getParameter("startdate"));
		request.setAttribute("enddate", request.getParameter("enddate"));
		request.setAttribute("mk_id", request.getParameter("mk_id"));
		request.setAttribute("unitid", request.getParameter("unitid"));
		request.setAttribute("startdate",startdate);
		request.setAttribute("enddate",enddate);
		request.setAttribute("xzqh",xzqh);
		request.setAttribute("cz", cz);
	}
	
	@At
	@Ok("raw")
	public JSONObject xxsbList(HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyzt_type")String xyzt_type,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("cz")String cz,@Param("mk_id")String mk_id,@Param("unitid")String unitid,
			@Param("SearchUserName") String SearchUserName,@Param("page") int curPage, @Param("rows") int pageSize) {
		List<Sys_unit> unitList =daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
		String hyxhId="";
		for(Sys_unit unit : unitList){
			hyxhId += unit.getId()+",";
		}
		hyxhId = hyxhId.substring(0,hyxhId.length()-1);
		String qsql="select t.id,t.type,t.content,t.create_time,t.login_ip,t.bowser,t.letter_id,t.class_name,t.method_name,t.title,t.reason,t.basis,t.cz,t.success,t.url,t.note,t.loginname||'/'||u.realname as loginname from  Sys_log t,sys_user u,sys_unit un where t.loginname=u.loginname and u.unitid=un.id  ";
		if(xzqh.equals("hyxh")){
			qsql+=" and un.unittype=90 ";
		}else{
			if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}
			qsql+="and xzqh like '"+xzqh+"' and unitid not in (" + hyxhId + ")";
		}
		if(EmptyUtils.isNotEmpty(startdate)){
			qsql+=" and t.create_time>='"+startdate+" 00:00:00' ";
		}
		if(EmptyUtils.isNotEmpty(enddate)){
			qsql+=" and t.create_time<='"+enddate+" 23:59:59' ";
		}
		if(EmptyUtils.isNotEmpty(mk_id)&&!"0".equals(mk_id)){
			qsql += " and t.title = '"+mk_id+"' ";
		}
		if(EmptyUtils.isNotEmpty(unitid)){
			qsql += " and un.id = '"+unitid+"' ";
		}
		if(EmptyUtils.isNotEmpty(cz)){
			qsql+=" and t.cz='"+cz+"' ";
		}
		if(EmptyUtils.isNotEmpty(SearchUserName)){
			qsql+=" and t.loginname like '%"+SearchUserName+"%'";
		}
		String order=" order by id desc ";
		System.out.println(qsql+order);
		Sql sql=Sqls.create(qsql+order);
		return daoCtl.listPageJsonSql(dao, sql, curPage, pageSize);
	}

	@At
	@Ok("->:/private/baobiao/sxcj.html")
	public void sxcj(HttpSession session, HttpServletRequest req, @Param("startdate") String startdate,
					 @Param("enddate") String enddate, @Param("type") String type,@Param("xzqh") String xzqh) {
		try {
			xzqh = EmptyUtils.isEmpty(xzqh)?"340000" : xzqh;
			Sys_user user=(Sys_user) session.getAttribute("userSession");
			if(user.getRolelist().contains("645")){
				req.setAttribute("sxld", "1");
				xzqh=daoCtl.detailByName(dao, Sys_unit.class, user.getUnitid()).getXzqh();
			}
			Gson gson = new Gson();
			enddate = EmptyUtils.isEmpty(enddate)?DateUtil.getToday():enddate;
			startdate = EmptyUtils.isEmpty(startdate)?DateUtil.getFirstMonDay(enddate):startdate;
			req.setAttribute("startdate",startdate);
			req.setAttribute("enddate",enddate);
			//行政区划
			req.setAttribute("xzqhlist", comUtil.xzqhList);
			List<Sys_unit> unitList = null;
			//如果是340000表示是省级的，省级需要包含行业协会信息
			if("340000".equals(xzqh)){
				unitList =daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
				req.setAttribute("unitList",unitList);
				req.setAttribute("unitGson",gson.toJson(unitList));
			}
			req.setAttribute("xzqhObj",daoCtl.detailByCnd(dao,Cs_value.class, Cnd.where("typeid","=","00010004").and("state","=",0).and("value","=",xzqh)));
			if(xzqh.substring(2,xzqh.length()).equals("0000")){
				xzqh = xzqh.substring(0,2)+"__00";
			}else if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}

			//得到列，行政区划
			List<Cs_value> xzqhList = daoCtl.list(dao,Cs_value.class,Sqls.create(" select value,name from cs_value where typeid='00010004' and state=0 and value like '"+xzqh+"'  order by location asc,id asc "));
			req.setAttribute("xzqhList",xzqhList);
			req.setAttribute("xzqhGson",gson.toJson(xzqhList));
			List<Cs_value> xyList = daoCtl.list(dao,Cs_value.class,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code like '____' order by location asc "));
			Map<String,String> xyMap = daoCtl.getHTable(dao,Sqls.create("select code,name from cs_value where typeid = '00010005' and state = 0 and code like '____'"));
			for(Map.Entry<String, String> entry:xyMap.entrySet()){
				xyMap.put(entry.getKey(), PinyinUtil.cn2py(xyMap.get(entry.getKey()).replace("、","")));
			}
			req.setAttribute("xyGson",gson.toJson(xyList));

			String charsStr = "";
			//图形报表Map
			Map<String,String> xyNameMap = daoCtl.getHTable(dao,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' order by location asc"));
			List<String> xyCode = daoCtl.getStrRowValues(dao,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' order by location asc"));

			req.setAttribute("xyNameMap",gson.toJson(xyNameMap));
			//开始处理报表数据
			Map<String,Map<String,String>> allMap = new HashMap<String, Map<String, String>>();
			String groupBy="xy_type";
			JSONArray array = new JSONArray();
			//行业协会数据
			/*if("34__00".equals(xzqh)){
				for(Sys_unit unit : unitList){
					String sqlstr=" select "+groupBy+",count(id) from discredit_info_VIEW where unitid = '"+unit.getId()+"' and  create_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by "+groupBy;
					Map<String,String> csMap = daoCtl.getHTable(dao,Sqls.create(sqlstr));
					allMap.put(unit.getId(),csMap);
					JSONObject jsonroot = new JSONObject();
					jsonroot.put("id", unit.getId());
					jsonroot.put("name",unit.getName());
					for(Map.Entry<String, String> entry:csMap.entrySet()){
						System.out.println("nihaoa "+entry.getKey());
						if(EmptyUtils.isNotEmpty(xyMap.get(entry.getKey()))){
							jsonroot.put(xyMap.get(entry.getKey()),entry.getValue());
						}
					}
					array.add(jsonroot);
				}
			}*/
			
			//行业协会总体展示数据
			String hyxhId = "";
			if("34__00".equals(xzqh)){
				for(Sys_unit unit : unitList){
					hyxhId += unit.getId()+",";
				}
				hyxhId = hyxhId.substring(0,hyxhId.length()-1);
				Map<String,String> csMap = daoCtl.getHTable(dao,Sqls.create(" select "+groupBy+",count(id) from discredit_info_VIEW where unittype=90 and  create_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by "+groupBy));
				req.setAttribute("hyxhMap", csMap);
			}

			for(Cs_value csValue : xzqhList){
				String xzqhStr = csValue.getValue();
				if(xzqhStr.substring(4,xzqhStr.length()).equals("00")){
					xzqhStr = xzqhStr.substring(0,4)+"__";
				}
				String sqlstr=" select "+groupBy+",count(id) from (select id,xy_type,xzqh,create_date,unittype from discredit_info_view) where unittype<>90 and xzqh like '"+xzqhStr+"' and  create_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by "+groupBy;
				Map<String,String> csMap = daoCtl.getHTable(dao,Sqls.create(sqlstr));
				allMap.put(csValue.getValue(),csMap);
				JSONObject jsonroot = new JSONObject();
				jsonroot.put("id", csValue.getValue());
				jsonroot.put("name",csValue.getName());
				for(Map.Entry<String, String> entry:csMap.entrySet()){
					if(EmptyUtils.isNotEmpty(xyMap.get(entry.getKey()))){
						jsonroot.put(xyMap.get(entry.getKey()),entry.getValue());
					}
				}
				array.add(jsonroot);
			}
			req.setAttribute("charsData", array.toString());
			req.setAttribute("allMap",allMap);
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@At
	@Ok("->:/private/baobiao/sxcjList.html")
	public void sxcjYm( HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xy_type")String xy_type,@Param("startdate")String startdate,@Param("enddate")String enddate){
		req.setAttribute("startdate",startdate);
		req.setAttribute("enddate",enddate);
		req.setAttribute("xzqh",xzqh);
		req.setAttribute("xy_type", xy_type);
		req.setAttribute("ztlxlist", comUtil.xyztlxMap);
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
		Sql sql = Sqls.create("select value,name from cs_value where typeid = 00010008");
		Hashtable<String, String> csTable = daoCtl.getHTable(dao, sql);
		req.setAttribute("xyztMap", comUtil.xyztlxMap);
		req.setAttribute("ztTable", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("csTable", JSONObject.fromObject(csTable));
		req.setAttribute("xytype", comUtil.sxxwtypeList);
		req.setAttribute("sxxwtypeMap", JSONObject.fromObject(comUtil.sxxwtypeMap));
		sql = Sqls.create("SELECT value,name FROM CS_VALUE  WHERE typeid='00010007' AND length(code)=4 ORDER BY location ASC ");
		req.setAttribute("cjztTable", JSONObject.fromObject(daoCtl.getHTable(dao, sql)));
	}
	
	@At
	@Ok("raw")
	public String sxcjList( HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyzt_type")String xyzt_type,@Param("src")String src,@Param("type")String type,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("xy_type")String xy_type,@Param("xyzt_id")String xyzt_id,@Param("sxxw_id")String sxxw_id,
			@Param("page") int curPage, @Param("rows") int pageSize){
		Sql sql = null;
		String sqlstr="";
		String hyxhId="";
		List<Sys_unit> unitList=daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
		for(Sys_unit unit : unitList){
			hyxhId += unit.getId()+",";
		}
		hyxhId = hyxhId.substring(0,hyxhId.length()-1);
		if(xzqh.equals("hyxh")){
			sqlstr = "select a.id as sxcjid,a.xy_type,a.start_date,a.end_date,a.src,b.name,b.type as xyzt_type,"+
			"c.sxxw,a.type from discredit_info a,xyzt_info b,breach_info c where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unit in (" + hyxhId + ")";
		}else{
			if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}
			sqlstr = "select a.id as sxcjid,a.xy_type,a.start_date,a.end_date,a.src,b.name,b.type as xyzt_type, c.sxxw,a.type from "
					+ "discredit_info a,xyzt_info b,breach_info c, sys_unit d where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unitid=d.id and d.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
		}
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr += " and b.type = '"+xyzt_type+"'";
		}
		if(EmptyUtils.isNotEmpty(xy_type)){
			sqlstr += " and a.xy_type = '"+xy_type+"'";
		}
		if(EmptyUtils.isNotEmpty(type)){
			sqlstr += " and a.type = '"+type+"'";
		}
		if(EmptyUtils.isNotEmpty(xyzt_id)){
			sqlstr += " and a.xyzt_id = '"+xyzt_id+"'";
		}
		if(EmptyUtils.isNotEmpty(sxxw_id)){
			sxxw_id=sxxw_id.substring(1, sxxw_id.length());
			sqlstr += " and a.sxxw_id = '"+sxxw_id+"'";
		}
		if(EmptyUtils.isNotEmpty(src)){
			sqlstr += " and a.src = '"+src+"'";
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			sqlstr += "and a.create_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
		}
		sqlstr += " order by a.id desc";
		System.out.println("sqlstr:"+sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map<String,String>> list = (List<Map<String, String>>) qr.getList();
		for(int i=0;i<list.size();i++){
			String start_date = list.get(i).get("start_date");
			String end_date = list.get(i).get("end_date");
			if(end_date==null||"".equals(end_date)){
				list.get(i).put("startDend", start_date+" 起 ");
			}else{
				list.get(i).put("startDend", start_date+" 至 "+(null == end_date?"":end_date));
			}
			list.get(i).put("xyzt_type", list.get(i).get("xyzt_type").toString().trim());
		}
		return PageObj.getPageJSON(qr);
	}
	
	//失信惩戒按照惩戒状态
	@At
	@Ok("raw")
	public String cjztSel(@Param("page") int curPage, @Param("rows") int pageSize,@Param("xzqh")String xzqh,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("xy_type")String xy_type,@Param("type")String type){
		Sql sql = null;
		String sqlstr="";
		String hyxhId="";
		List<Sys_unit> unitList=daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
		for(Sys_unit unit : unitList){
			hyxhId += unit.getId()+",";
		}
		hyxhId = hyxhId.substring(0,hyxhId.length()-1);
		if(xzqh.equals("hyxh")){
			sqlstr = "select a.type,count(1) as sxjl"+
			" from discredit_info a where unit in (" + hyxhId + ")";
		}else{
			if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}
			sqlstr = "select a.type,count(1) as sxjl from "
					+ "discredit_info a, sys_unit d where a.unitid=d.id and d.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
		}
		if(EmptyUtils.isNotEmpty(xy_type)){
			sqlstr += " and a.xy_type = '"+xy_type+"'";
		}
		if(EmptyUtils.isNotEmpty(type)){
			sqlstr += " and a.type = '"+type+"'";
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			sqlstr += "and a.create_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
		}
		sqlstr += " group by a.type ";
		System.out.println("sqlstr:"+sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		return PageObj.getPageJSON(qr);
	}
	
	//失信惩戒按照生成方式查询
	@At
	@Ok("raw")
	public String scfsSel( HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyzt_type")String xyzt_type,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("xy_type")String xy_type,
			@Param("page") int curPage, @Param("rows") int pageSize){
		Sql sql = null;
		String sqlstr="";
		String hyxhId="";
		List<Sys_unit> unitList=daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
		for(Sys_unit unit : unitList){
			hyxhId += unit.getId()+",";
		}
		hyxhId = hyxhId.substring(0,hyxhId.length()-1);
		if(xzqh.equals("hyxh")){
			sqlstr = "select a.src,count(1) as sxjl"+
			" from discredit_info a where unit in (" + hyxhId + ")";
		}else{
			if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}
			sqlstr = "select a.src,count(1) as sxjl from "
					+ "discredit_info a, sys_unit d where a.unitid=d.id and d.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
		}
		if(EmptyUtils.isNotEmpty(xy_type)){
			sqlstr += "and a.xy_type = '"+xy_type+"'";
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			sqlstr += "and a.create_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
		}	
		sqlstr += " group by a.src ";
		System.out.println("sqlstr:"+sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		return PageObj.getPageJSON(qr);
	}
	
	//失信惩戒按照失信行为查询
	@At
	@Ok("raw")
	public String sxxwSel( HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyzt_type")String xyzt_type,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("xy_type")String xy_type,
			@Param("page") int curPage, @Param("rows") int pageSize){
		Sql sql = null;
		String sqlstr="";
		String hyxhId="";
		List<Sys_unit> unitList=daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
		for(Sys_unit unit : unitList){
			hyxhId += unit.getId()+",";
		}
		hyxhId = hyxhId.substring(0,hyxhId.length()-1);
		if(xzqh.equals("hyxh")){
			sqlstr = "select a.sxxw_id,c.sxxw,count(1) as sxjl"+
			" from discredit_info a,xyzt_info b,breach_info c where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unit in (" + hyxhId + ")";
		}else{
			if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}
			sqlstr = "select a.sxxw_id,c.sxxw,count(1) as sxjl from "
					+ "discredit_info a,xyzt_info b,breach_info c, sys_unit d where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unitid=d.id and d.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
		}
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr += "and b.type = '"+xyzt_type+"'";
		}
		if(EmptyUtils.isNotEmpty(xy_type)){
			sqlstr += "and a.xy_type = '"+xy_type+"'";
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			sqlstr += "and a.create_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
		}
		sqlstr += " group by a.sxxw_id,c.sxxw ";
		System.out.println("sqlstr:"+sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		return PageObj.getPageJSON(qr);
	}
	
	//失信惩戒按照信用主体查询
	@At
	@Ok("raw")
	public String xyztSel( HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyztsel")String xyzt_type,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("xy_type")String xy_type,
			@Param("page") int curPage, @Param("rows") int pageSize){
		Sql sql = null;
		String sqlstr="";
		String hyxhId="";
		List<Sys_unit> unitList=daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
		for(Sys_unit unit : unitList){
			hyxhId += unit.getId()+",";
		}
		hyxhId = hyxhId.substring(0,hyxhId.length()-1);
		if(xzqh.equals("hyxh")){
			sqlstr = "select a.xyzt_id,b.name,b.type as xyzt_type,count(1) as sxjl"+
			" from discredit_info a,xyzt_info b where a.xyzt_id = b.id and a.unit in (" + hyxhId + ")";
		}else{
			if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}
			sqlstr = "select a.xyzt_id,b.name,b.type as xyzt_type,count(1) as sxjl from "
					+ "discredit_info a,xyzt_info b,sys_unit d where a.xyzt_id = b.id and a.unitid=d.id and d.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
		}
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr += "and b.type = '"+xyzt_type+"'";
		}
		if(EmptyUtils.isNotEmpty(xy_type)){
			sqlstr += "and a.xy_type = '"+xy_type+"'";
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			sqlstr += "and a.create_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
		}
		sqlstr += " group by a.xyzt_id,b.name,b.type ";
		System.out.println("sqlstr:"+sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		return PageObj.getPageJSON(qr);
	}
	
	

	//失信奖励柱状图/饼状图
	@At
	@Ok("raw")
	public String getChartsxjl(HttpSession session, HttpServletRequest req, @Param("startdate") String startdate,
							   @Param("enddate") String enddate, @Param("xzqh") String xzqh){
		System.out.println("In getChartsxcj..");
		JSONArray array = new JSONArray();
		JSONObject jsonobj = new JSONObject();
		try {
			String today = DateUtil.getToday();
			if (EmptyUtils.isEmpty(startdate)) {
				startdate = DateUtil.getPreDayStr(today);
			}
			if (EmptyUtils.isEmpty(enddate)) {
				enddate = DateUtil.getNextDayStr(today);
			}
			req.setAttribute("startdate",startdate);
			req.setAttribute("enddate",enddate);
			//列表显示单位  默认显示 省级、行业协会、合肥市、蚌埠市、芜湖市
			List<Map> xzqhMap = new ArrayList<Map>();
			if(EmptyUtils.isNotEmpty(xzqh)){
				//通过value取code值
				String xzqh_code = "";
				List<Map> cv = comUtil.xzqhList_cv;
				//System.out.println("----->cv:"+cv);
				for(int i = 0; i< cv.size();i++){
					if(cv.get(i).get("value").toString().equals(xzqh)){
						xzqh_code = cv.get(i).get("code").toString();
					}
				}
				//矿业
				int kycount = daoCtl.getIntRowValue(dao, Sqls.create("select count(1) from reward_info a,sys_unit b where a.xy_type like '0003%'"+
						" and a.unitid = b.id and b.xzqh = '"+xzqh_code+"%' and a.create_date >='"+startdate+" 00:00:00' and a.create_date <='"+enddate+" 23:59:59' group by a.unitid order by a.unitid "));
				jsonobj.put("kycount", kycount);
				//土地
				int tdcount = daoCtl.getIntRowValue(dao, Sqls.create("select count(1) from reward_info a,sys_unit b where a.xy_type like '0004%'"+
						" and a.unitid = b.id and b.xzqh = '"+xzqh+"' and a.create_date >='"+startdate+" 00:00:00' and a.create_date <='"+enddate+" 23:59:59' group by a.unitid order by a.unitid "));
				jsonobj.put("tdcount", tdcount);
				//测绘市场
				int chcount = daoCtl.getIntRowValue(dao, Sqls.create("select count(1) from reward_info a,sys_unit b where a.xy_type like '0006%'"+
						" and a.unitid = b.id and b.xzqh = '"+xzqh+"' and a.create_date >='"+startdate+" 00:00:00' and a.create_date <='"+enddate+" 23:59:59' group by a.unitid order by a.unitid "));
				jsonobj.put("chcount", chcount);
				//中介
				int zjcount = daoCtl.getIntRowValue(dao, Sqls.create("select count(1) from reward_info a,sys_unit b where a.xy_type like '0005%'"+
						" and a.unitid = b.id and b.xzqh = '"+xzqh+"' and a.create_date >='"+startdate+" 00:00:00' and a.create_date <='"+enddate+" 23:59:59' group by a.unitid order by a.unitid "));
				jsonobj.put("zjcount", zjcount);
				//工程项目
				int gccount = daoCtl.getIntRowValue(dao, Sqls.create("select count(1) from reward_info a,sys_unit b where a.xy_type like '0007%'"+
						" and a.unitid = b.id and b.xzqh = '"+xzqh+"' and a.create_date >='"+startdate+" 00:00:00' and a.create_date <='"+enddate+" 23:59:59' group by a.unitid order by a.unitid "));
				jsonobj.put("gccount", gccount);
			}
			array.add(jsonobj);
			System.out.println("array:"+array);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return array.toString();
	}

	@At
	@Ok("raw")
	public String getChart(HttpSession session, HttpServletRequest req) {
		List<List<String>> list = new ArrayList<List<String>>();
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String sql = "";
		try {
			String today = DateUtil.getToday();

			// 根据分页获取列表
			List<String> clist = new ArrayList<String>();
			JSONObject josn=new JSONObject();
			clist.add( "合肥市国土局");
			clist.add("12");
			list.add(clist);

			clist.add("芜湖市国土局");
			clist.add("5");
			list.add(clist);

			clist.add("淮南市国土局");
			clist.add("1");
			list.add(clist);

			clist = new ArrayList<String>();
			clist.add("蚌埠市国土局");
			clist.add("5");
			list.add(clist);

			clist = new ArrayList<String>();
			clist.add("阜阳市国土局");
			clist.add("11");
			list.add(clist);

		} catch (Exception e) {
			e.printStackTrace();
			SysLogUtil.addLog(dao, user, SysLogUtil.yx_log_type, "图形统计出现错误：" + e.getMessage());
		}
		return JSONArray.fromObject(list).toString();
	}

	//导出失信惩戒的报表
	@At
	public void dcSxcj(HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyzt_type")String xyzt_type,@Param("src")String src,@Param("lb")String lb,@Param("type")String type,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("xy_type")String xy_type,@Param("xyzt_id")String xyzt_id,@Param("sxxw_id")String sxxw_id,
			HttpServletResponse response) {
		try {
			String sqlstr = "";
			String hyxhId="";
			List<String> hyxhList = daoCtl.getStrRowValues(dao,Sqls.create(" select id from sys_unit where unittype = 90 order by id "));
			for(String hyxh : hyxhList){
				hyxhId += hyxh+",";
			}
			hyxhId = hyxhId.substring(0,hyxhId.length()-1);
			if(lb.equals("1")){
				if(xzqh.equals("hyxh")){
					sqlstr = "select a.id as sxcjid,a.xy_type,a.start_date,a.end_date,a.src,b.name,b.type as xyzt_type,"+
					"c.sxxw,a.type from discredit_info a,xyzt_info b,breach_info c where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unit in (" + hyxhId + ")";
				}else{
					if(xzqh.substring(4,xzqh.length()).equals("00")){
						xzqh = xzqh.substring(0,4)+"__";
					}
					sqlstr = "select a.id as sxcjid,a.xy_type,a.start_date,a.end_date,a.src,b.name,b.type as xyzt_type, c.sxxw,a.type from "
							+ "discredit_info a,xyzt_info b,breach_info c, sys_unit d where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unitid=d.id and d.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
				}
			}else if(lb.equals("2")){
				if(xzqh.equals("hyxh")){
					sqlstr = "select a.xyzt_id,b.name,b.type as xyzt_type,count(1) as sxjl"+
					" from discredit_info a,xyzt_info b,breach_info c where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unit in (" + hyxhId + ")";
				}else{
					if(xzqh.substring(4,xzqh.length()).equals("00")){
						xzqh = xzqh.substring(0,4)+"__";
					}
					sqlstr = "select a.xyzt_id,b.name,b.type as xyzt_type,count(1) as sxjl from "
							+ "discredit_info a,xyzt_info b,breach_info c, sys_unit d where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unitid=d.id and d.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
				}
			}else if(lb.equals("3")){
				if(xzqh.equals("hyxh")){
					sqlstr = "select a.sxxw_id,c.sxxw,count(1) as sxjl"+
					" from discredit_info a,xyzt_info b,breach_info c where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unit in (" + hyxhId + ")";
				}else{
					if(xzqh.substring(4,xzqh.length()).equals("00")){
						xzqh = xzqh.substring(0,4)+"__";
					}
					sqlstr = "select a.sxxw_id,c.sxxw,count(1) as sxjl from "
							+ "discredit_info a,xyzt_info b,breach_info c, sys_unit d where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unitid=d.id and d.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
				}
			}else if(lb.equals("4")){
				if(xzqh.equals("hyxh")){
					sqlstr = "select a.src,count(1) as sxjl"+
					" from discredit_info a,xyzt_info b,breach_info c where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unit in (" + hyxhId + ")";
				}else{
					if(xzqh.substring(4,xzqh.length()).equals("00")){
						xzqh = xzqh.substring(0,4)+"__";
					}
					sqlstr = "select a.src,count(1) as sxjl from "
							+ "discredit_info a,xyzt_info b,breach_info c, sys_unit d where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unitid=d.id and d.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
				}
			}else if(lb.equals("5")){
				if(xzqh.equals("hyxh")){
					sqlstr = "select a.type,count(1) as sxjl"+
					" from discredit_info a,xyzt_info b,breach_info c where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unit in (" + hyxhId + ")";
				}else{
					if(xzqh.substring(4,xzqh.length()).equals("00")){
						xzqh = xzqh.substring(0,4)+"__";
					}
					sqlstr = "select a.type,count(1) as sxjl from "
							+ "discredit_info a,xyzt_info b,breach_info c, sys_unit d where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unitid=d.id and d.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
				}
			}
			
			if(EmptyUtils.isNotEmpty(xyzt_type)){
				sqlstr += " and b.type = '"+xyzt_type+"'";
			}
			if(EmptyUtils.isNotEmpty(xy_type)){
				sqlstr += " and a.xy_type = '"+xy_type+"'";
			}
			if(EmptyUtils.isNotEmpty(xyzt_id)){
				sqlstr += " and a.xyzt_id = '"+xyzt_id+"'";
			}
			if(EmptyUtils.isNotEmpty(sxxw_id)){
				sxxw_id=sxxw_id.substring(1, sxxw_id.length());
				sqlstr += " and a.sxxw_id = '"+sxxw_id+"'";
			}
			if(EmptyUtils.isNotEmpty(src)){
				sqlstr += " and a.src = '"+src+"'";
			}
			if(EmptyUtils.isNotEmpty(type)){
				sqlstr += " and a.type = '"+type+"'";
			}
			if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
				sqlstr += "and a.create_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			}
			if(lb.equals("1")){
				sqlstr += " order by a.id desc";
			}else if(lb.equals("2")){
				sqlstr += " group by a.xyzt_id,b.name,b.type";
			}else if(lb.equals("3")){
				sqlstr += " group by a.sxxw_id,c.sxxw";
			}else if(lb.equals("4")){
				sqlstr += " group by a.src";
			}else if(lb.equals("5")){
				sqlstr += " group by a.type";
			}
			
			System.out.println(sqlstr);
			List<Map> list=daoCtl.list(dao, Sqls.create(sqlstr));
			for(Map sxcjmap:list){
				if(lb.equals("1")){
					sxcjmap.put("xyzt_type", comUtil.xyztlx.get(sxcjmap.get("xyzt_type")));
					Sql sqls = Sqls.create("select value,name from cs_value where typeid = 00010008");
					List<Map> cstype = daoCtl.list(dao, sqls);
					Hashtable<String, String> csTable = daoCtl.getHTable(dao, sqls);
					String scfs=sxcjmap.get("src").toString();
					sxcjmap.put("src", csTable.get(scfs));
					sxcjmap.put("xy_type", comUtil.sxxwtypeMap.get(sxcjmap.get("xy_type")));
					sqls = Sqls.create("select value,name from cs_value where typeid = 00010007 and state=0");
					sxcjmap.put("type", daoCtl.getHTable(dao, sqls).get(sxcjmap.get("type")));
					String start_date = (String) sxcjmap.get("start_date");
					String end_date = (String) sxcjmap.get("end_date");
					if(end_date==null||"".equals(end_date)){
						sxcjmap.put("startDend", start_date+" 起 ");
					}else{
						sxcjmap.put("startDend", start_date+" 至 "+(null == end_date?"":end_date));
					}
				}else if(lb.equals("2")){
					sxcjmap.put("xyzt_type", comUtil.xyztlx.get(sxcjmap.get("xyzt_type")));
				}else if(lb.equals("4")){
					Sql sqls = Sqls.create("select value,name from cs_value where typeid = 00010008");
					Hashtable<String, String> csTable = daoCtl.getHTable(dao, sqls);
					String scfs=sxcjmap.get("src").toString();
					sxcjmap.put("src", csTable.get(scfs));
				}else if(lb.equals("5")){
					Sql sqls = Sqls.create("select value,name from cs_value where typeid = 00010007 and state=0");
					sxcjmap.put("type", daoCtl.getHTable(dao, sqls).get(sxcjmap.get("type")));
				}
			}
			// 第一步，创建一个webbook，对应一个Excel文件  
			HSSFWorkbook wb = new HSSFWorkbook();  
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
			HSSFSheet sheet = wb.createSheet("失信惩戒信息");  
			sheet.setColumnWidth(1, 20 * 256); //设置列宽
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
			int rowIndex = 0;
			HSSFRow row = sheet.createRow(rowIndex++);  
			// 第四步，创建单元格，并设置值表头 设置表头居中  
			HSSFCellStyle style = wb.createCellStyle();  
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
			if(lb.equals("1")){
				String[] headers = {"序号","信用主体","类型","失信行为","生成方式","业务类别","惩戒期限","惩戒状态"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("name")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("xyzt_type")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 3);
					cell.setCellValue(StringUtil.null2String(map.get("sxxw")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 4);
					cell.setCellValue(StringUtil.null2String(map.get("src")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 5);
					cell.setCellValue(StringUtil.null2String(map.get("xy_type")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 6);
					cell.setCellValue(StringUtil.null2String(map.get("startdend")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 7);
					cell.setCellValue(StringUtil.null2String(map.get("type")));
					cell.setCellStyle(style);
				}
			}else if(lb.equals("2")){
				String[] headers = {"序号","信用主体","类型","失信惩戒记录数"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("name")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("xyzt_type")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 3);
					cell.setCellValue(StringUtil.null2String(map.get("sxjl")));
					cell.setCellStyle(style);
				}
			}else if(lb.equals("3")){
				String[] headers = {"序号","失信行为","失信惩戒记录数"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("sxxw")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("sxjl")));
					cell.setCellStyle(style);
				}
			}else if(lb.equals("4")){
				String[] headers = {"序号","生成方式","失信惩戒记录数"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("src")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("sxjl")));
					cell.setCellStyle(style);
				}
			}else if(lb.equals("5")){
				String[] headers = {"序号","惩戒状态","失信惩戒记录数"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("type")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("sxjl")));
					cell.setCellStyle(style);
				}
			}
			
			response.setContentType("application/vnd.ms-excel;charset=ISO8859-1");
			//response.setHeader("Content-Disposition", "attachment; filename=法人资质信息.xls");// 设定输出文件头
			String fileName="失信惩戒信息";
			response.setHeader("Content-Disposition", "attachment;filename=\""+ new String(fileName.getBytes("gb18030"),"ISO8859-1") + ".xls" + "\"");
			OutputStream output;
			output = response.getOutputStream();
			wb.write(output);
			output.flush();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//导出奖励信息的报表
	@At
	public void dcSxjl(HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyzt_type")String xyzt_type,@Param("src")String src,@Param("lb")String lb,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("xy_type")String xy_type,@Param("xyzt_id")String xyzt_id,@Param("sxxw_id")String sxxw_id,
			HttpServletResponse response) {
		try {
			String sqlstr = "";
			String hyxhId="";
			List<String> hyxhList = daoCtl.getStrRowValues(dao,Sqls.create(" select id from sys_unit where unittype = 90 order by id "));
			for(String hyxh : hyxhList){
				hyxhId += hyxh+",";
			}
			hyxhId = hyxhId.substring(0,hyxhId.length()-1);
			if(lb.equals("1")){
				if(xzqh.equals("hyxh")){
					sqlstr = "select a.id as jlid,a.xy_type,a.jl_date,b.name,b.type as xyzt_type,"+
					"a.note from reward_info a,xyzt_info b where a.xyzt_id = b.id and a.unit in (" + hyxhId + ")";
				}else{
					if(xzqh.substring(4,xzqh.length()).equals("00")){
						xzqh = xzqh.substring(0,4)+"__";
					}
					sqlstr = "select a.id as jlid,a.xy_type,a.jl_date,b.name,b.type as xyzt_type, a.note from "
							+ "reward_info a,xyzt_info b, sys_unit d where a.xyzt_id = b.id and  a.unitid=d.id and d.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
				}
			}else if(lb.equals("2")){
				if(xzqh.equals("hyxh")){
					sqlstr = "select a.xyzt_id,b.name,b.type as xyzt_type,count(1) as sxjl"+
					" from reward_info a,xyzt_info b where a.xyzt_id = b.id  and a.unit in (" + hyxhId + ")";
				}else{
					if(xzqh.substring(4,xzqh.length()).equals("00")){
						xzqh = xzqh.substring(0,4)+"__";
					}
					sqlstr = "select a.xyzt_id,b.name,b.type as xyzt_type,count(1) as sxjl from "
							+ "reward_info a,xyzt_info b, sys_unit d where a.xyzt_id = b.id and  a.unitid=d.id and d.xzqh like '"+xzqh+"' and a.unit not in (" + hyxhId + ")  ";
				}
			}
			
			if(EmptyUtils.isNotEmpty(xyzt_type)){
				sqlstr += " and b.type = '"+xyzt_type+"'";
			}
			if(EmptyUtils.isNotEmpty(xy_type)){
				sqlstr += " and a.xy_type = '"+xy_type+"'";
			}
			if(EmptyUtils.isNotEmpty(xyzt_id)){
				sqlstr += " and a.xyzt_id = '"+xyzt_id+"'";
			}
			
			if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
				sqlstr += "and a.create_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			}
			if(lb.equals("1")){
				sqlstr += " order by a.id desc";
			}else if(lb.equals("2")){
				sqlstr += " group by a.xyzt_id,b.name,b.type";
			}
			
			List<Map> list=daoCtl.list(dao, Sqls.create(sqlstr));
			for(Map sxcjmap:list){
				sxcjmap.put("xyzt_type", comUtil.xyztlx.get(sxcjmap.get("xyzt_type")));
				if(lb.equals("1")){
					sxcjmap.put("xy_type", comUtil.sxxwtypeMap.get(sxcjmap.get("xy_type")));
				}
			}
			// 第一步，创建一个webbook，对应一个Excel文件  
			HSSFWorkbook wb = new HSSFWorkbook();  
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
			HSSFSheet sheet = wb.createSheet("守信奖励信息");  
			sheet.setColumnWidth(1, 20 * 256); //设置列宽
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
			int rowIndex = 0;
			HSSFRow row = sheet.createRow(rowIndex++);  
			// 第四步，创建单元格，并设置值表头 设置表头居中  
			HSSFCellStyle style = wb.createCellStyle();  
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
			if(lb.equals("1")){
				String[] headers = {"序号","信用主体","类型","业务类别","奖励作出时间"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("name")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("xyzt_type")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 3);
					cell.setCellValue(StringUtil.null2String(map.get("xy_type")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 4);
					cell.setCellValue(StringUtil.null2String(map.get("jl_date")));
					cell.setCellStyle(style);
				}
			}else if(lb.equals("2")){
				String[] headers = {"序号","信用主体","类型","守信奖励记录数"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("name")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("xyzt_type")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 3);
					cell.setCellValue(StringUtil.null2String(map.get("sxjl")));
					cell.setCellStyle(style);
				}
			}
			
			response.setContentType("application/vnd.ms-excel;charset=ISO8859-1");
			//response.setHeader("Content-Disposition", "attachment; filename=法人资质信息.xls");// 设定输出文件头
			String fileName="守信奖励信息";
			response.setHeader("Content-Disposition", "attachment;filename=\""+ new String(fileName.getBytes("gb18030"),"ISO8859-1") + ".xls" + "\"");
			OutputStream output;
			output = response.getOutputStream();
			wb.write(output);
			output.flush();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//导出信息上报
	@At
	public void dcXxsb(HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyzt_type")String xyzt_type,@Param("src")String src,@Param("lb")String lb,@Param("title")String mk_id,
			@Param("startdate")String startdate,@Param("cz")String cz,@Param("enddate")String enddate,@Param("xy_type")String xy_type,@Param("xyzt_id")String xyzt_id,@Param("sxxw_id")String sxxw_id,
			HttpServletResponse response) {
		try {
			String sqlstr = "";
			String hyxhId="";
			List<String> hyxhList = daoCtl.getStrRowValues(dao,Sqls.create(" select id from sys_unit where unittype = 90 order by id "));
			for(String hyxh : hyxhList){
				hyxhId += hyxh+",";
			}
			hyxhId = hyxhId.substring(0,hyxhId.length()-1);
			if(lb.equals("1")){
				if(xzqh.equals("hyxh")){
					sqlstr = "select t.id,t.type,t.content,t.create_time,t.login_ip,t.bowser,t.letter_id,t.class_name,t.method_name,t.title,t.reason,t.basis,t.cz,t.success,t.url,t.note,t.loginname||'/'||u.realname as loginname from  Sys_log t,sys_user u,sys_unit un where t.loginname=u.loginname and u.unitid=un.id "+
					"and un.unittype=90";
				}else{
					if(xzqh.substring(4,xzqh.length()).equals("00")){
						xzqh = xzqh.substring(0,4)+"__";
					}
					sqlstr = "select t.id,t.type,t.content,t.create_time,t.login_ip,t.bowser,t.letter_id,t.class_name,t.method_name,t.title,t.reason,t.basis,t.cz,t.success,t.url,t.note,t.loginname||'/'||u.realname as loginname from  Sys_log t,sys_user u,sys_unit un where t.loginname=u.loginname and u.unitid=un.id "+
							"and xzqh like '"+xzqh+"' and unitid not in (" + hyxhId + ")";
				}
			}else if(lb.equals("2")){
				if(xzqh.equals("hyxh")){
					sqlstr = "select title,cz,count(1) as czjl"+
							" from sys_log t,sys_user us,sys_unit un where t.loginname=us.loginname and us.unitid=un.id  and un.unittype = 90 ";
				}else{
					if(xzqh.substring(4,xzqh.length()).equals("00")){
						xzqh = xzqh.substring(0,4)+"__";
					}
					sqlstr = "select title,cz,count(1) as czjl from "
							+ "sys_log t,sys_user us,sys_unit un where t.loginname=us.loginname and us.unitid=un.id  and un.xzqh like '"+xzqh+"' and us.unitid not in (" + hyxhId + ")  ";
				}
			}else if(lb.equals("3")){
				sqlstr = "select un.id,un.name,t.cz,count(1) as czjl"+
						" from sys_log t,sys_user us,sys_unit un where t.loginname=us.loginname and us.unitid=un.id  and un.unittype = 90 ";
			}else if(lb.equals("4")){
				if(xzqh.substring(4,xzqh.length()).equals("00")){
					xzqh = xzqh.substring(0,4)+"__";
				}
				sqlstr = "select un.id,un.name,t.cz,count(1) as czjl from "
						+ "sys_log t,sys_user us,sys_unit un where t.loginname=us.loginname and us.unitid=un.id  and un.xzqh like '"+xzqh+"' and us.unitid not in (" + hyxhId + ")  ";
			}
			
			if(EmptyUtils.isNotEmpty(xyzt_type)){
				sqlstr += " and b.type = '"+xyzt_type+"'";
			}
			if(EmptyUtils.isNotEmpty(xy_type)){
				sqlstr += " and a.xy_type = '"+xy_type+"'";
			}
			if(EmptyUtils.isNotEmpty(mk_id)&&!"0".equals(mk_id)){
				sqlstr += " and t.title = '"+mk_id+"' ";
			}
			if(EmptyUtils.isNotEmpty(cz)){
				sqlstr+=" and t.cz='"+cz+"' ";
			}
			
			if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
				sqlstr += "and t.create_time between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			}
			sqlstr+=" and t.type <> 0";
			if(lb.equals("1")){
				sqlstr += " order by t.id desc";
			}else if(lb.equals("2")){
				sqlstr += " group by title ,cz";
			}else if(lb.equals("3")||lb.equals("4")){
				sqlstr += " group by un.id,un.name,t.cz ";
			}
			
			List<Map> list=daoCtl.list(dao, Sqls.create(sqlstr));
			// 第一步，创建一个webbook，对应一个Excel文件  
			HSSFWorkbook wb = new HSSFWorkbook();  
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
			HSSFSheet sheet = wb.createSheet("信息上报信息");  
			sheet.setColumnWidth(1, 20 * 256); //设置列宽
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
			int rowIndex = 0;
			HSSFRow row = sheet.createRow(rowIndex++);  
			// 第四步，创建单元格，并设置值表头 设置表头居中  
			HSSFCellStyle style = wb.createCellStyle();  
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
			if(lb.equals("1")){
				String[] headers = {"序号","用户名","涉及模块","操作内容","执行操作","操作时间","登陆ip"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("loginname")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("title")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 3);
					cell.setCellValue(StringUtil.null2String(map.get("content")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 4);
					cell.setCellValue(StringUtil.null2String(map.get("cz")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 5);
					cell.setCellValue(StringUtil.null2String(map.get("create_time")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 6);
					cell.setCellValue(StringUtil.null2String(map.get("login_ip")));
					cell.setCellStyle(style);
				}
			}else if(lb.equals("2")){
				String[] headers = {"序号","所属模块","类别","操作记录数"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("title")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("cz")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 3);
					cell.setCellValue(StringUtil.null2String(map.get("czjl")));
					cell.setCellStyle(style);
				}
			}else if(lb.equals("3")){
				String[] headers = {"序号","行业协会","类别","操作记录数"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("name")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("cz")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 3);
					cell.setCellValue(StringUtil.null2String(map.get("czjl")));
					cell.setCellStyle(style);
				}
			}else if(lb.equals("4")){
				String[] headers = {"序号","厅下属部门","类别","操作记录数"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("name")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("cz")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 3);
					cell.setCellValue(StringUtil.null2String(map.get("czjl")));
					cell.setCellStyle(style);
				}
			}
			
			response.setContentType("application/vnd.ms-excel;charset=ISO8859-1");
			//response.setHeader("Content-Disposition", "attachment; filename=法人资质信息.xls");// 设定输出文件头
			String fileName="信息上报信息";
			response.setHeader("Content-Disposition", "attachment;filename=\""+ new String(fileName.getBytes("gb18030"),"ISO8859-1") + ".xls" + "\"");
			OutputStream output;
			output = response.getOutputStream();
			wb.write(output);
			output.flush();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@At
	public void dcYytj(HttpServletRequest req,HttpSession session,@Param("xzqh")String xzqh,@Param("xyzt_type")String xyzt_type,@Param("keyword")String keyword,@Param("src")String src,@Param("lb")String lb,@Param("xyzt_id")String xyzt_id,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("cz")String cz,@Param("state")int state,@Param("source")String source,@Param("xy_type")String xy_type,
			HttpServletResponse response) {
		try {
			String sqlstr = "";
			String hyxhId="";
			List<String> hyxhList = daoCtl.getStrRowValues(dao,Sqls.create(" select id from sys_unit where unittype = 90 order by id "));
			for(String hyxh : hyxhList){
				hyxhId += hyxh+",";
			}
			hyxhId = hyxhId.substring(0,hyxhId.length()-1);
			if(lb.equals("1")){
				sqlstr = "select w.id,w.sxcj_id,w.xyzt_id,w.contract_id,w.applicant,w.add_time,w.note,w.code,w.state,w.contract_name,z.name xyzt_name,w.source from WARNEXCEPTION w,xyzt_info z where w.xyzt_id=z.id and w.state <> -1  $s ";
				if(xzqh.equals("hyxh")){
					sqlstr+=" and w.unit in (" + hyxhId + ")";
				}else{
					if(xzqh.substring(4,xzqh.length()).equals("00")){
						xzqh = xzqh.substring(0,4)+"__";
					}
					sqlstr+=" and w.unit not in (" + hyxhId + ") and w.xzqh like '"+xzqh+"'";
				}
			}else if(lb.equals("2")){
				if(xzqh.equals("hyxh")){
					sqlstr = "select w.xyzt_id,b.name,b.type as xyzt_type,count(1) as yyjl"+
					" from WARNEXCEPTION w,xyzt_info b where w.xyzt_id = b.id and w.unit in (" + hyxhId + ")";
				}else{
					if(xzqh.substring(4,xzqh.length()).equals("00")){
						xzqh = xzqh.substring(0,4)+"__";
					}
					sqlstr = "select w.xyzt_id,b.name,b.type as xyzt_type,count(1) as yyjl from "
							+ "WARNEXCEPTION w,xyzt_info b,sys_unit d where w.xyzt_id = b.id and w.unit=d.id and w.xzqh like '"+xzqh+"' and w.unit not in (" + hyxhId + ")  ";
				}
				
			}else if(lb.equals("3")){
				sqlstr = "select un.id,un.name,t.cz,count(1) as czjl"+
						" from sys_log t,sys_user us,sys_unit un where t.loginname=us.loginname and us.unitid=un.id  and un.unittype = 90 ";
			}else if(lb.equals("4")){
				if(xzqh.substring(4,xzqh.length()).equals("00")){
					xzqh = xzqh.substring(0,4)+"__";
				}
				sqlstr = "select un.id,un.name,t.cz,count(1) as czjl from "
						+ "sys_log t,sys_user us,sys_unit un where t.loginname=us.loginname and us.unitid=un.id  and un.xzqh like '"+xzqh+"' and us.unitid not in (" + hyxhId + ")  ";
			}
			if(state==2){
				sqlstr+=" and w.state='"+state+"' and w.cz='"+cz+"'";
			}else{
				sqlstr+=" and w.state='"+state+"'";
			}
			if(EmptyUtils.isNotEmpty(keyword)){
				sqlstr+=  " AND w.applicant like '%"+keyword+"%' ";
			}
			
			if(EmptyUtils.isNotEmpty(source) && !"".equals(source)){
				sqlstr+=" AND w.source = '"+source+"' ";
			}
			if(EmptyUtils.isNotEmpty(xyzt_id)){
				sqlstr+=" and w.xyzt_id = '"+xyzt_id+"'";
			}
			if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
				sqlstr += " AND w.add_time between '"+startdate+" 00:00:00' and '"+enddate+" 23:59:59'  ";
			}
			if(lb.equals("1")){
				sqlstr += " order by w.add_time desc";
			}else if(lb.equals("2")){
				sqlstr += " group by w.xyzt_id,b.name,b.type ";
			}else if(lb.equals("3")||lb.equals("4")){
				sqlstr += " group by un.id,un.name,t.cz ";
			}
			List<Map> list=daoCtl.list(dao, Sqls.create(sqlstr));
			for(Map yytjmap:list){
				if(yytjmap.get("source").equals("0")){
					yytjmap.put("source", "线上");
				}else{
					yytjmap.put("source", "线下");
				}
				if(yytjmap.get("state").equals("0")){
					yytjmap.put("state", "待处理");
				}else if(yytjmap.get("state").equals("1")){
					yytjmap.put("state", "处理中");
				}else{
					yytjmap.put("state", "办结");
				}
				if(lb.equals("2")){
					yytjmap.put("xyzt_type", comUtil.xyztlx.get(yytjmap.get("xyzt_type")));
				}
			}
			// 第一步，创建一个webbook，对应一个Excel文件  
			HSSFWorkbook wb = new HSSFWorkbook();  
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
			HSSFSheet sheet = wb.createSheet("异议统计信息");  
			sheet.setColumnWidth(1, 20 * 256); //设置列宽
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
			int rowIndex = 0;
			HSSFRow row = sheet.createRow(rowIndex++);  
			// 第四步，创建单元格，并设置值表头 设置表头居中  
			HSSFCellStyle style = wb.createCellStyle();  
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
			if(lb.equals("1")){
				String[] headers = {"序号","涉及合同","涉及信用主体","申请人","证件号码","申请时间","来源","状态"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("contract_name")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("xyzt_name")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 3);
					cell.setCellValue(StringUtil.null2String(map.get("applicant")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 4);
					cell.setCellValue(StringUtil.null2String(map.get("code")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 5);
					cell.setCellValue(StringUtil.null2String(map.get("add_time")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 6);
					cell.setCellValue(StringUtil.null2String(map.get("source")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 7);
					cell.setCellValue(StringUtil.null2String(map.get("state")));
					cell.setCellStyle(style);
				}
			}else if(lb.equals("2")){
				String[] headers = {"序号","信用主体","类别","异议条数"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("name")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("xyzt_type")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 3);
					cell.setCellValue(StringUtil.null2String(map.get("yyjl")));
					cell.setCellStyle(style);
				}
			}else if(lb.equals("3")){
				String[] headers = {"序号","行业协会","类别","操作记录数"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("name")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("cz")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 3);
					cell.setCellValue(StringUtil.null2String(map.get("czjl")));
					cell.setCellStyle(style);
				}
			}else if(lb.equals("4")){
				String[] headers = {"序号","厅下属部门","类别","操作记录数"};
				for (int i = 0; i < headers.length; i++) { // 输出头部
					HSSFCell cell =  row.createCell((short) i);
					cell.setCellValue(headers[i]);
					cell.setCellStyle(style);
				}
				for(int i=0;i<list.size();i++){
					row = sheet.createRow(i+1);
					Map map=list.get(i);
					HSSFCell cell =  row.createCell((short) 0);
					cell.setCellValue(i+1);
					cell.setCellStyle(style);
					cell=  row.createCell((short) 1);
					cell.setCellValue(StringUtil.null2String(map.get("name")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 2);
					cell.setCellValue(StringUtil.null2String(map.get("cz")));
					cell.setCellStyle(style);
					cell=  row.createCell((short) 3);
					cell.setCellValue(StringUtil.null2String(map.get("czjl")));
					cell.setCellStyle(style);
				}
			}
			
			response.setContentType("application/vnd.ms-excel;charset=ISO8859-1");
			//response.setHeader("Content-Disposition", "attachment; filename=法人资质信息.xls");// 设定输出文件头
			String fileName="异议统计信息";
			response.setHeader("Content-Disposition", "attachment;filename=\""+ new String(fileName.getBytes("gb18030"),"ISO8859-1") + ".xls" + "\"");
			OutputStream output;
			output = response.getOutputStream();
			wb.write(output);
			output.flush();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * JJF
	 * 行政许可信息报表
	 */
	@At
	@Ok("->:/private/baobiao/xzxk.html")
	public void xzxkXx(HttpSession session, HttpServletRequest req, @Param("startdate") String startdate,
			 @Param("enddate") String enddate, @Param("type") String type,@Param("xzqh") String xzqh){
			enddate = EmptyUtils.isEmpty(enddate)?DateUtil.getToday():enddate;
			startdate = EmptyUtils.isEmpty(startdate)?DateUtil.getFirstMonDay(enddate):startdate;
			req.setAttribute("startdate",startdate);
			req.setAttribute("enddate",enddate);
			//得到列，行政审批项
			List<Cs_value> xzxkList = daoCtl.list(dao,Cs_value.class,Sqls.create(" select value,name from cs_value where typeid='00010035' and state=0  order by location asc,id asc "));
			req.setAttribute("xzxkList",xzxkList);
			Map<String,String> csMap=new HashMap<String, String>();
			String sqlstr="select xy_type ,count(1) from(select xy_type, substr(xyptmodify_date,0,10) as sj,count(1) as sl from l_gt_xzxkxx where  to_char(XYPTMODIFY_DATE,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by xy_type,substr(xyptmodify_date,0,10)) group by xy_type";
			csMap = daoCtl.getHTable(dao,Sqls.create(sqlstr));
			req.setAttribute("allMap",csMap);
			sqlstr=" select xy_type,count(*) from l_gt_xzxkxx where  to_char(XYPTMODIFY_DATE,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by xy_type";
			Map<String,String> jlsMap = daoCtl.getHTable(dao,Sqls.create(sqlstr));
			req.setAttribute("jlsMap",jlsMap);
	}
	
	
	/**
	 * 行政许可批次
	 * @param req
	 * @param xy_type
	 */
	@At
	@Ok("->:/private/baobiao/xzxkPc.html")
	public void xzxkPc(HttpServletRequest req,@Param("xy_type") String xy_type,@Param("startdate") String startdate,
			@Param("enddate") String enddate,@Param("curPage") int curPage,@Param("pageSize") int pageSize){
		req.setAttribute("xy_type",xy_type);
		req.setAttribute("startdate",startdate);
		req.setAttribute("enddate",enddate);
	}
	
	@At
	@Ok("raw")
	public String xzxkPcList(HttpServletRequest req,@Param("xy_type") String xy_type,@Param("startdate") String startdate,
			@Param("enddate") String enddate,@Param("curPage") int curPage,@Param("pageSize") int pageSize){
		String sqlstr="select  to_char(xyptmodify_date,'yyyy-mm-dd') as xyptmodify_date,count(1) as tsjl from l_gt_xzxkxx where xy_type='"+xy_type+"' and to_char(XYPTMODIFY_DATE,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by xy_type, to_char(xyptmodify_date,'yyyy-mm-dd')";
		Map<String,String> csMap=new HashMap<String, String>();
		List<Map> xzxkList = daoCtl.list(dao, Sqls.create(sqlstr));
		QueryResult qr=daoCtl.listPageSql(dao, Sqls.create(sqlstr), curPage, pageSize);
		sqlstr="select value,name from cs_value where typeid='00010035' and state=0  order by location asc,id asc";
		csMap = daoCtl.getHTable(dao,Sqls.create(sqlstr));
		req.setAttribute("xzxkname",csMap.get(xy_type));
		List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();
		for(int i=0;i<list.size();i++){
			list.get(i).put("xzxkname", csMap.get(xy_type));
		}
		return PageObj.getPageJSON(qr);
	}
	
	@At
	@Ok("->:/private/baobiao/xzxkList.html")
	public void xzxkYm(HttpServletRequest req,@Param("xy_type") String xy_type,@Param("startdate") String startdate,@Param("enddate") String enddate,
			@Param("tssj") String tssj){
		req.setAttribute("xy_type",xy_type);
		req.setAttribute("startdate",startdate);
		req.setAttribute("enddate",enddate);
		req.setAttribute("tssj",tssj);
	}
	
	@At
	@Ok("raw")
	public JSONObject xzxkList(@Param("xy_type") String xy_type,@Param("page") int curPage, @Param("rows") int pageSize,@Param("xk_wsh") String xk_wsh,@Param("xk_xmmc") String xk_xmmc,
			 @Param("startdate") String startdate,@Param("enddate") String enddate,@Param("tssj") String tssj){
		String sql="select id,xk_wsh,xk_xmmc,xk_xdr,xk_splb,xk_xzjg,xk_zt from l_gt_xzxkxx where xy_type="+xy_type+" ";
		if(EmptyUtils.isNotEmpty(xk_wsh)){
			sql+=" and xk_wsh='"+xk_wsh+"' ";
		}
		if(EmptyUtils.isNotEmpty(xk_xmmc)){
			sql+=" and xk_xmmc='"+xk_xmmc+"'";
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			sql+=" and to_char(XYPTMODIFY_DATE,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
		}
		if(EmptyUtils.isNotEmpty(tssj)){
			sql+=" and to_char(XYPTMODIFY_DATE,'YYYY-MM-DD') ='"+tssj+"'";
		}
		sql+=" order by sjc desc";
		return daoCtl.listPageJsonSql(dao, Sqls.create(sql), curPage, pageSize);
	}
	
	@At
	@Ok("->:/private/baobiao/xzxkDetail.html")
	public void xzxkDetail(@Param("id") String id,HttpServletRequest req){
		L_gt_xzxkxx xzxk = daoCtl.detailByName(dao, L_gt_xzxkxx.class, id);
		req.setAttribute("xzxk", xzxk);
	}
	
	/**
	 * JJF
	 * 13类省信用数据交换信息
	 */
	@At
	@Ok("->:/private/baobiao/stsjts.html")
	public void sxyjhXx(HttpSession session, HttpServletRequest req, @Param("startdate") String startdate,
			 @Param("enddate") String enddate, @Param("type") String type,@Param("xzqh") String xzqh){
			enddate = EmptyUtils.isEmpty(enddate)?DateUtil.getToday():enddate;
			startdate = EmptyUtils.isEmpty(startdate)?DateUtil.getFirstMonDay(enddate):startdate;
			req.setAttribute("startdate",startdate);
			req.setAttribute("enddate",enddate);
			//得到列，省厅数据交换类别
			List<Cs_value> xzxkList = daoCtl.list(dao,Cs_value.class,Sqls.create(" select value,name from cs_value where typeid='00010036' and state=0  order by location asc,id asc "));
			req.setAttribute("xzxkList",xzxkList);
			Map<String,String> csMap=new HashMap<String, String>();
			Map<String,String> pcMap=new HashMap<String, String>();
			//采矿权
			String sqlstr=" select count(1) from l_ckqht where  status=1 and xkzh is not null and unitid not in(select id from sys_unit where unittype=88 and id<>0008) and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			csMap.put("0001", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			sqlstr="select count(*) from (select substr(tstime,0,10) from l_ckqht where  status=1 and xkzh is not null and unitid not in(select id from sys_unit where unittype=88 and id<>0008) and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by substr(tstime,0,10))";
			pcMap.put("0001", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			//探矿权
			sqlstr=" select count(1) from l_tkht where status=1 and kcxkz is not null and unitid not in(select id from sys_unit where unittype=88 and id<>0008) and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' ";
			csMap.put("0002", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			sqlstr="select count(*) from (select substr(tstime,0,10) from l_tkht where status=1 and kcxkz is not null and unitid not in(select id from sys_unit where unittype=88 and id<>0008) and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by substr(tstime,0,10))";
			pcMap.put("0002", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			//欠缴矿业价款
			sqlstr=" select  count(1) from l_ckqht_yw where  sfwc is null and ylxrq_y<to_char(sysdate,'yyyy-mm-dd') and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			csMap.put("0003", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			sqlstr="select count(*) from (select substr(tstime,0,10) from l_ckqht_yw where sfwc is null and ylxrq_y<to_char(sysdate,'yyyy-mm-dd') and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by substr(tstime,0,10))";
			pcMap.put("0003", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			//地质勘察
			sqlstr=" select count(1) from zz_info where xy_type=00070004 and status=1 and zz_lb is not null and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			csMap.put("0004", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			sqlstr="select count(*) from (select substr(tstime,0,10) from zz_info where xy_type=00070004 and status=1 and zz_lb is not null and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by substr(tstime,0,10))";
			pcMap.put("0004", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			//测绘资质
			sqlstr=" select count(1) from zz_info where xy_type=0006 and status=1 and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			csMap.put("0005", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			sqlstr="select count(*) from (select substr(tstime,0,10) from zz_info where xy_type=0006 and status=1 and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by substr(tstime,0,10))";
			pcMap.put("0005", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			//地质灾害甲级
			sqlstr=" select count(1) from zz_info where xy_type=00070002 and status=1 and zz_jb='0' and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			csMap.put("0006", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			sqlstr="select count(*) from (select substr(tstime,0,10) from zz_info where xy_type=00070002 and status=1 and zz_jb='0' and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by substr(tstime,0,10))";
			pcMap.put("0006", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			//地质灾害乙丙级
			sqlstr=" select count(1) from zz_info where xy_type=00070002 and status=1 and zz_jb in('1','2') and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			csMap.put("0007", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			sqlstr="select count(*) from (select substr(tstime,0,10) from zz_info where xy_type=00070002 and status=1 and zz_jb in('1','2') and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by substr(tstime,0,10))";
			pcMap.put("0007", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			//行政强制
			sqlstr=" select  count(1) from xzqz_info where to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			csMap.put("0008", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			sqlstr="select count(*) from (select substr(tstime,0,10) from xzqz_info where to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by substr(tstime,0,10))";
			pcMap.put("0008", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			//企业行政处罚
			sqlstr=" select  count(1) from xzcf_info a,(select * from xyzt_info where type=1) b where a.xyzt_id=b.id and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			csMap.put("0009", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			sqlstr="select count(*) from (select substr(tstime,0,10) from xzcf_info a,(select * from xyzt_info where type=1) b where a.xyzt_id=b.id	and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'  group by substr(tstime,0,10))";
			pcMap.put("0009", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			//个人行政处罚
			sqlstr=" select  count(1) from xzcf_info a,(select * from xyzt_info where type=0) b where a.xyzt_id=b.id  and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			csMap.put("0012", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			sqlstr="select count(*) from (select substr(tstime,0,10) from xzcf_info a,(select * from xyzt_info where type=0) b where a.xyzt_id=b.id	and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'  group by substr(tstime,0,10))";
			pcMap.put("0012", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			//职称信息
			sqlstr=" select  count(1) from zc_info where zc_state=0 and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			csMap.put("0010", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			sqlstr="select count(*) from (select substr(tstime,0,10) from zc_info where zc_state=0 and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by substr(tstime,0,10))";
			pcMap.put("0010", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			//职称注销信息
			sqlstr="select count(1) from zc_info where zc_state=1 and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			csMap.put("0011", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			sqlstr="select count(*) from (select substr(tstime,0,10) from zc_info where zc_state=1 and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by substr(tstime,0,10))";
			pcMap.put("0011", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr))+"");
			req.setAttribute("allMap",csMap);
			req.setAttribute("pcMap", pcMap);
	}
	
	/**
	 * 省厅13类数据批次
	 * @param req
	 * @param xy_type
	 */
	@At
	@Ok("->:/private/baobiao/stsjtsPc.html")
	public void sxyjhPc(HttpServletRequest req,@Param("xy_type") String xy_type,@Param("startdate") String startdate,
			@Param("enddate") String enddate){
		req.setAttribute("xy_type",xy_type);
		req.setAttribute("startdate",startdate);
		req.setAttribute("enddate",enddate);
	}
	
	@At
	@Ok("raw")
	public String sxyjhPcList(HttpServletRequest req,@Param("xy_type") String xy_type,@Param("startdate") String startdate,
			@Param("enddate") String enddate,@Param("curPage") int curPage,@Param("pageSize") int pageSize){
		String sqlstr="";
		if(xy_type.equals("0001")){
			sqlstr="select  to_char(tstime,'yyyy-mm-dd') as tstime,count(1) as tsjl from l_ckqht where status=1 and xkzh is not null and unitid not in(select id from sys_unit where unittype=88 and id<>0008) ";
		}else if(xy_type.equals("0002")){
			sqlstr="select to_char(tstime,'yyyy-mm-dd') as tstime,count(1) as tsjl from l_tkht where status=1 and kcxkz is not null and unitid not in(select id from sys_unit where unittype=88 and id<>0008) ";
		}else if(xy_type.equals("0003")){
			sqlstr="select to_char(tstime,'yyyy-mm-dd') as tstime,count(1) as tsjl  from l_ckqht_yw where sfwc is null and ylxrq_y<to_char(sysdate,'yyyy-mm-dd')";
		}else if(xy_type.equals("0004")){
			sqlstr="select to_char(tstime,'yyyy-mm-dd') as tstime,count(1) as tsjl from zz_info where xy_type=00070004 and status=1 and zz_lb is not null ";
		}else if(xy_type.equals("0005")){
			sqlstr="select to_char(tstime,'yyyy-mm-dd') as tstime,count(1) as tsjl from zz_info where xy_type=0006 and status=1";
		}else if(xy_type.equals("0006")){
			sqlstr="select to_char(tstime,'yyyy-mm-dd') as tstime,count(1) as tsjl from zz_info where xy_type=00070002 and zz_jb='0' and status=1";
		}else if(xy_type.equals("0007")){
			sqlstr="select to_char(tstime,'yyyy-mm-dd') as tstime,count(1) as tsjl from zz_info where xy_type=00070002 and zz_jb in('1','2') and status=1";
		}else if(xy_type.equals("0008")){
			sqlstr="select to_char(tstime,'yyyy-mm-dd') as tstime,count(1) as tsjl from xzqz_info where 1=1 ";
		}else if(xy_type.equals("0009")){
			sqlstr="select to_char(tstime,'yyyy-mm-dd') as tstime,count(1) as tsjl from xzcf_info a,(select * from xyzt_info where type=1) b where a.xyzt_id=b.id ";
		}else if(xy_type.equals("0010")){
			sqlstr="select to_char(tstime,'yyyy-mm-dd') as tstime,count(1) as tsjl from zc_info where zc_state=0 ";
		}else if(xy_type.equals("0011")){
			sqlstr="select to_char(tstime,'yyyy-mm-dd') as tstime,count(1) as tsjl from zc_info where zc_state=1 ";
		}else if(xy_type.equals("0012")){
			sqlstr="select to_char(tstime,'yyyy-mm-dd') as tstime,count(1) as tsjl from xzcf_info a,(select * from xyzt_info where type=0) b where a.xyzt_id=b.id ";
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			sqlstr+=" and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
		}
		sqlstr+=" group by to_char(tstime,'yyyy-mm-dd')";
		Map<String,String> csMap=new HashMap<String, String>();
		List<Map> xzxkList = daoCtl.list(dao, Sqls.create(sqlstr));
		QueryResult qr=daoCtl.listPageSql(dao, Sqls.create(sqlstr), curPage, pageSize);
		sqlstr="select value,name from cs_value where typeid='00010036' and state=0  order by location asc,id asc";
		csMap = daoCtl.getHTable(dao,Sqls.create(sqlstr));
		req.setAttribute("xzxkname",csMap.get(xy_type));
		List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();
		for(int i=0;i<list.size();i++){
			list.get(i).put("xzxkname", csMap.get(xy_type));
		}
		return PageObj.getPageJSON(qr);
	}
	
	@At
	@Ok("->:/private/baobiao/stsjtsList.html")
	public void sxyjhYm(HttpServletRequest req,@Param("xy_type") String xy_type,@Param("tssj") String tssj,@Param("startdate") String startdate,
			@Param("enddate") String enddate){
		req.setAttribute("xy_type",xy_type);
		req.setAttribute("tssj",tssj);
		req.setAttribute("startdate", startdate);
		req.setAttribute("enddate", enddate);
	}
	
	@At
	@Ok("raw")
	public JSONObject sxyjhList(@Param("xy_type") String xy_type,@Param("page") int curPage, @Param("rows") int pageSize,@Param("xk_wsh") String xk_wsh,@Param("xk_xmmc") String xk_xmmc,@Param("startdate") String startdate,
			@Param("enddate") String enddate,@Param("tssj") String tssj,@Param("xk_wsh")String xkzh){
		String sql="";	
		if(xy_type.equals("0001")){
			sql="select a.htid as id,a.xkzh as XKZH,a.kqmc as KSMC,c.name as KCZKZ, KQMJ,"
					+ "b.name as CKQR,a.qxq as XKYXQSRP,qsz "
					+ "as XKYXJZRP,'安徽省国土资源厅' as SPJG,sprq "
					+ "as PZRP from l_ckqht a,xyzt_info b,(select * from cs_value where "
					+ "typeid=00020013)c where a.xyzt=b.id and a.kz=c.value and a.status=1 "
					+ "and a.xkzh is not null and unitid not in(select id from sys_unit "
					+ "where unittype=88 and id<>0008) ";
			if(EmptyUtils.isNotEmpty(xkzh)){
				sql+=" and a.xkzh like '%"+xkzh+"%'";
			}
		}else if(xy_type.equals("0002")){
			sql="select a.htid as id,a.kcxkz as XKZH,c.name as KCKZ, KCMJ,b.name as TKQR,b.name as KCDW,xmmc,XMXZ,kcjd,start_date as XKYXQSRP,end_date as XKYXJZRP,'安徽省国土资源厅' as SPJG,sprq as sprp from l_tkht a,xyzt_info b,(select * from cs_value where typeid=00020013)c where a.xyzt=b.id and a.kz=c.value and a.status=1 and a.kcxkz is not null and unitid not in(select id from sys_unit where unittype=88 and id<>0008)";
			if(EmptyUtils.isNotEmpty(xkzh)){
				sql+=" and a.kcxkz like '%"+xkzh+"%'";
			}
		}else if(xy_type.equals("0003")){
			sql="select ywid as id, b.name as zrdw,c.kqmc as KSMC,c.xkzh,NVL(ylxx_y,0) - NVL(sjlxx_s,0) as QJZSE,'安徽省国土资源厅' as tgjg,sysdate as TGRP  from l_ckqht_yw a,xyzt_info b,l_ckqht c where a.fk_htid=c.htid and b.id = c.xyzt and sfwc is null and ylxrq_y<to_char(sysdate,'yyyy-mm-dd')";
			if(EmptyUtils.isNotEmpty(xkzh)){
				sql+=" and c.xkzh like '%"+xkzh+"%'";
			}
		}else if(xy_type.equals("0004")){
			sql="select a.id,b.name as DWMC,fr_name as FDDBR,zz_code as ZZZBH,c.name as zzlb,d.name as zzdj,start_date as ZZQSRP,end_date as ZZJZRP, zz_unit as spjg from (select * from zz_info where xy_type=00070004 and status=1) a,xyzt_info b,(select * from Cs_value where state=0 and  typeid='00010014') c,(select * from cs_value where state=0 and typeid='00010013')d where a.xyzt_id=b.id and a.zz_lb=c.value and a.zz_jb=d.value";
			if(EmptyUtils.isNotEmpty(xkzh)){
				sql+=" and zz_code like '%"+xkzh+"%'";
			}
		}else if(xy_type.equals("0005")){
			sql="select a.id,b.name as DWMC,b.fr_name as FDDBR,a.zz_code as ZZZBH,c.name as zzdj,a.major as ZZFW,start_date as ZZQSRP,'2099-12-31' as ZZJZRP, zz_unit as spjg,start_date as SPRP from (select * from zz_info where xy_type=0006 and status=1) a,xyzt_info b,(select * from Cs_value where state=0 and  typeid='00010022') c where a.xyzt_id=b.id and a.zz_jb=c.value ";
			if(EmptyUtils.isNotEmpty(xkzh)){
				sql+=" and a.zz_code like '%"+xkzh+"%'";
			}
		}else if(xy_type.equals("0006")){
			sql="select a.id,b.name as DWMC,a.zz_code as ZZZBH,c.name as zzlb,d.name as zzdj,a.major as ZZFW,start_date as ZZQSRP,'2099-12-31' as ZZJZRP, zz_unit as spjg,to_date(a.start_date,'yyyy-MM-dd') as SPRP from (select * from zz_info where xy_type=00070002 and zz_jb='0' and status=1) a,xyzt_info b,(select * from Cs_value where state=0 and  typeid='00010016') c,(select * from Cs_value where state=0 and  typeid='00010013') d where a.xyzt_id=b.id and a.zz_jb=d.value and a.zz_lb=c.value";
			if(EmptyUtils.isNotEmpty(xkzh)){
				sql+=" and a.zz_code like '%"+xkzh+"%'";
			}
		}else if(xy_type.equals("0007")){
			sql="select a.id,a.zz_code as ZZZBH,b.name as dwmc,b.fr_name as FDDBR,c.name as zzlb,d.name as zzdj,a.major as ZZFW,start_date as ZZQSRP,'2099-12-31' as ZZJZRP, zz_unit as spjg,start_date as SPRP from (select * from zz_info where xy_type=00070002 and zz_jb in('1','2') and status=1) a,xyzt_info b,(select * from Cs_value where state=0 and  typeid='00010016') c,(select * from Cs_value where state=0 and  typeid='00010013') d where a.xyzt_id=b.id and a.zz_jb=d.value and a.zz_lb=c.value";
			if(EmptyUtils.isNotEmpty(xkzh)){
				sql+=" and a.zz_code like '%"+xkzh+"%'";
			}
		}else if(xy_type.equals("0008")){
			sql="select a.id,a.jds_code as JDWSH,b.name as qymc,aj_name as AJMC,reason as QZLY,reply as XZQZJL, jg_unitid as QZJGQC,c.name as ZXQK from xzqz_info a,xyzt_info b,(select * from cs_value where typeid=00010020)c where a.xyzt_id=b.id and a.qzzxqk=c.value";
			if(EmptyUtils.isNotEmpty(xkzh)){
				sql+=" and a.jds_code like '%"+xkzh+"%'";
			}
		}else if(xy_type.equals("0009")){
			sql="select a.id,a.xzcf_code as JDWSH,b.name as qymc,au as CFSY,xzcf_yiju as CFYJ,xzcf_note as CFJL,cfsx_date as CFRP from xzcf_info a,(select * from xyzt_info where type=1) b where a.xyzt_id=b.id";
			if(EmptyUtils.isNotEmpty(xkzh)){
				sql+=" and a.xzcf_code like '%"+xkzh+"%'";
			}
		}else if(xy_type.equals("0010")){
			sql="select id,name as xm,idcard as sfzhm,zc_name as ZGZCMC,zc_card as ZGZCZSBH,zc_level as ZGZCDJ,zc_time as QDSJ,sp_unit as SPDW from zc_info where zc_state=0";
			if(EmptyUtils.isNotEmpty(xkzh)){
				sql+=" and zc_card like '%"+xkzh+"%'";
			}
		}else if(xy_type.equals("0011")){
			sql="select id,name as xm,idcard as sfzhm,zc_name as ZGZCMC,zc_card as ZGZCZSBH,zc_zxsj as ZGZDXSJ,zc_zxjg as ZGZDXJG,zc_zxyy as ZDXYY from zc_info where zc_state=1";
			if(EmptyUtils.isNotEmpty(xkzh)){
				sql+=" and zc_card like '%"+xkzh+"%'";
			}
		}else if(xy_type.equals("0012")){
			sql="select a.id,a.xzcf_code as CFWSBH,b.name as XM,b.code as SFZHM,au as WFXWAY,cfsx_date as CFRP,xzcf_note as CFJG ,'安徽省国土资源厅' as CFDEPART from xzcf_info a,(select * from xyzt_info where type=0) b where a.xyzt_id=b.id ";
			if(EmptyUtils.isNotEmpty(xkzh)){
				sql+=" and code like '%"+xkzh+"%'";
			}
		}
		if(EmptyUtils.isNotEmpty(tssj)){
			if(xy_type.equals("0003")){
				sql+=" and to_char(a.tstime,'YYYY-MM-DD') ='"+tssj+"'";
			}else{
				sql+=" and to_char(tstime,'YYYY-MM-DD') ='"+tssj+"'";
			}
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			if(xy_type.equals("0003")){
				sql+=" and to_char(a.tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			}else{
				sql+=" and to_char(tstime,'YYYY-MM-DD hh24:mi:ss') between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			}
		}
		sql+=" order by sjc desc";
		return daoCtl.listPageJsonSql(dao, Sqls.create(sql), curPage, pageSize);
	}
	
	@At
	@Ok("->:/private/baobiao/stsjtsDetail.html")
	public void sxyjhDetail(@Param("id") String id,@Param("xy_type")String xy_type,HttpServletRequest req){
		String sql="";	
		if(xy_type.equals("0001")){
			sql="select b.code as TYSHXYDM,b.zzjgdm as ZZJGDM,b.gszch as GSZCH,a.kqmc as KSMC,a.xkzh as XKZH,b.name as CKQR,c.name as KCZKZ, KQMJ,"
					+ "a.qxq as XKYXQSRP,qsz "
					+ "as XKYXJZRP,'安徽省国土资源厅' as SPJG,sprq "
					+ "as PZRP from l_ckqht a,xyzt_info b,(select * from cs_value where "
					+ "typeid=00020013)c where a.xyzt=b.id and a.kz=c.value and a.status=1 "
					+ "and a.xkzh is not null and unitid not in(select id from sys_unit "
					+ "where unittype=88 and id<>0008) and a.htid='"+id+"'";
		}else if(xy_type.equals("0002")){
			sql="select b.code as TYSHXYDM,b.zzjgdm as ZZJGDM,b.name as KCDW,a.kcxkz as XKZH,c.name as KCKZ,kcjd,"
					+ "start_date as XKYXQSRP,"
					+ "end_date as XKYXJZRP,'安徽省国土资源厅' as SPJG,substr(sprq,0,10) as sprp from "
					+ "l_tkht a,xyzt_info b,(select * from cs_value where typeid=00020013)c "
					+ "where a.xyzt=b.id and a.kz=c.value and a.status=1 and a.kcxkz is "
					+ "not null and unitid not in(select id from sys_unit where unittype=88 "
					+ "and id<>0008) and a.htid='"+id+"'";
		}else if(xy_type.equals("0003")){
			sql="select b.code as TYSHXYDM,b.zzjgdm as ZZJGDM,b.gszch as GSZCH,c.kqmc as KSMC,c.xkzh,NVL(ylxx_y,0) - NVL(sjlxx_s,0) as QJZSE from l_ckqht_yw a,xyzt_info b,l_ckqht c where a.fk_htid=c.htid and b.id = c.xyzt and sfwc is null and ylxrq_y<to_char(sysdate,'yyyy-mm-dd') and ywid='"+id+"'";
		}else if(xy_type.equals("0004")){
			sql="select b.code as TYSHXYDM,b.zzjgdm as ZZJGDM,b.gszch as GSZCH,b.name as DWMC,zz_code as ZZZBH,d.name as zzdj,start_date as ZZQSRP,end_date as ZZJZRP, zz_unit as spjg from (select * from zz_info where xy_type=00070004) a,xyzt_info b,(select * from Cs_value where state=0 and  typeid='00010014') c,(select * from cs_value where state=0 and typeid='00010013')d where a.xyzt_id=b.id and a.zz_lb=c.value and a.zz_jb=d.value and a.id='"+id+"'";
		}else if(xy_type.equals("0005")){
			sql="select b.code as TYSHXYDM,b.zzjgdm as ZZJGDM,b.gszch as GSZCH,b.name as DWMC,b.fr_name as FDDBR,a.zz_code as ZZZBH,c.name as zzdj,a.major as zzfw,start_date as ZZQSRP,'2099-12-31' as ZZJZRP, zz_unit as spjg,start_date as SPRP from (select * from zz_info where xy_type=0006) a,xyzt_info b,(select * from Cs_value where state=0 and  typeid='00010022') c where a.xyzt_id=b.id and a.zz_jb=c.value and a.id='"+id+"'";
		}else if(xy_type.equals("0006")){
			sql="select b.code as TYSHXYDM,b.zzjgdm as ZZJGDM,b.gszch as GSZCH,b.name as DWMC,a.zz_code as ZZZBH,c.name as zzlb,d.name as zzdj,a.major as ZZFW,start_date as ZZQSRP,'2099-12-31' as ZZJZRP, zz_unit as spjg,to_date(a.start_date,'yyyy-MM-dd') as SPRP from (select * from zz_info where xy_type=00070002 and zz_jb='0') a,xyzt_info b,(select * from Cs_value where state=0 and  typeid='00010016') c,(select * from Cs_value where state=0 and  typeid='00010013') d where a.xyzt_id=b.id and a.zz_jb=d.value and a.zz_lb=c.value and a.id='"+id+"'";
		}else if(xy_type.equals("0007")){
			sql="select b.code as TYSHXYDM,b.zzjgdm as ZZJGDM,b.gszch as GSZCH,b.name as dwmc,b.fr_name as FDDBR,a.zz_code as ZZZBH,c.name as zzlb,d.name as zzdj,start_date as ZZQSRP,'2099-12-31' as ZZJZRP, zz_unit as spjg from (select * from zz_info where xy_type=00070002 and zz_jb in('1','2')) a,xyzt_info b,(select * from Cs_value where state=0 and  typeid='00010016') c,(select * from Cs_value where state=0 and  typeid='00010013') d where a.xyzt_id=b.id and a.zz_jb=d.value and a.zz_lb=c.value and a.id='"+id+"'";
		}else if(xy_type.equals("0008")){//行政强制
			sql="select b.code as TYSHXYDM,b.zzjgdm as ZZJGDM,b.gszch as GSZCH,b.name as qymc,a.jds_code as JDWSH,c.name as ZXQK ,aj_name as AJMC,reason as QZLY,reply as XZQZJL from xzqz_info a,xyzt_info b,(select * from cs_value where typeid=00010020)c where a.xyzt_id=b.id and a.qzzxqk=c.value and a.id='"+id+"'";
		}else if(xy_type.equals("0009")){//企业行政处罚
			sql="select b.code as TYSHXYDM,b.zzjgdm as ZZJGDM,b.gszch as GSZCH,b.name as qymc,a.xzcf_code as JDWSH,cfsx_date as CFRP,au as CFSY,xzcf_yiju as CFYJ,xzcf_note as CFJL from xzcf_info a,(select * from xyzt_info where type=1) b where a.xyzt_id=b.id and a.id='"+id+"'";
		}else if(xy_type.equals("0010")){//职称信息
			sql="select name as xm,idcard as sfzhm,zc_name as ZGZCMC,zc_card as ZGZCZSBH,zc_level as ZGZCDJ,zc_time as QDSJ,sp_unit as SPDW from zc_info where zc_state=0 and id='"+id+"'";
		}else if(xy_type.equals("0011")){//职称注销信息
			sql="select name as xm,idcard as sfzhm,zc_name as ZGZCMC,zc_card as ZGZCZSBH,zc_zxsj as ZGZDXSJ,zc_zxjg as ZGZDXJG,zc_zxyy as ZDXYY from zc_info where zc_state=1 and id='"+id+"'";
		}else if(xy_type.equals("0012")){//个人行政处罚
			sql="select b.name as XM,b.code as SFZHM,a.xzcf_code as CFWSBH,'安徽省国土资源厅' as CFDEPART,au as WFXWAY,xzcf_note as CFJG,cfsx_date as CFRP  from xzcf_info a,(select * from xyzt_info where type=0) b where a.xyzt_id=b.id and a.id='"+id+"'";
		}
		List<List<String>> xzxk= daoCtl.getMulRowValue(dao, Sqls.create(sql));
		req.setAttribute("xzxk", xzxk);
		req.setAttribute("xy_type", xy_type);
	}
}