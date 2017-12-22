package com.hits.modules.baobiao;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.alibaba.druid.sql.visitor.functions.Substring;
import com.google.gson.Gson;
import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.DateUtil;
import com.hits.util.EmptyUtils;


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

	@At
	@Ok("->:/private/baobiao/zsr.html")
	public void zsr(HttpSession session, HttpServletRequest req, @Param("startdate") String startdate,
			 @Param("enddate") String enddate, @Param("type") String type){
		try {
			Gson gson = new Gson();
			enddate = EmptyUtils.isEmpty(enddate)?DateUtil.getToday():enddate;
			startdate = EmptyUtils.isEmpty(startdate)?DateUtil.getFirstMonDay(enddate):startdate;
			req.setAttribute("startdate",startdate);
			req.setAttribute("enddate",enddate);
			//得到列，订货单位
			List<Sys_unit> xzqhList = daoCtl.list(dao,Sys_unit.class,Sqls.create(" select id,name from sys_unit where unittype = 88 order by id asc "));
			req.setAttribute("xzqhList",xzqhList);
			//图形报表Map
			Map<String,String> xyNameMap = daoCtl.getHTable(dao,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' order by location asc"));
			List<String> xyCode = daoCtl.getStrRowValues(dao,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' order by location asc"));
			req.setAttribute("xyNameMap",gson.toJson(xyNameMap));
			//开始处理报表数据
			Map<String,String> allMap = new HashMap<String, String>();//订单数量
			Map<String,String> wfkMap = new HashMap<String, String>();//未付款
			Map<String,String> yfkMap = new HashMap<String, String>();//已付款
			Map<String,String> zjMap = new HashMap<String, String>();//总计
			//查询订货单位的订单数量
			String sqlstr=" select unitid,count(*) from l_jsgg where add_time between '" + startdate + "' and '" + enddate + "' group by unitid";
			allMap = daoCtl.getHTable(dao,Sqls.create(sqlstr));
			//查询订货单位未付款信息
			sqlstr = "select unitid ,sum(yfjk) from l_jsgg where isfh='0002' and add_time between '" + startdate + "' and '" + enddate + "' group by unitid";
			wfkMap = daoCtl.getHTable(dao, Sqls.create(sqlstr));
			//查询订货单位已付款信息
			sqlstr = "select unitid ,sum(yfjk) from l_jsgg where isfh='0001' and add_time between '" + startdate + "' and '" + enddate + "' group by unitid";
			yfkMap = daoCtl.getHTable(dao, Sqls.create(sqlstr));
			//订货单位所有账目信息
			sqlstr = "select unitid ,sum(yfjk) from l_jsgg where add_time between '" + startdate + "' and '" + enddate + "' group by unitid";
			zjMap = daoCtl.getHTable(dao, Sqls.create(sqlstr));
			JSONArray array = new JSONArray();
			for(Sys_unit unit : xzqhList){
				JSONObject jsonroot = new JSONObject();
				jsonroot.put("id", unit.getId());
				jsonroot.put("name",unit.getName());
			}
			req.setAttribute("charsData", array.toString());
			req.setAttribute("allMap",allMap);
			req.setAttribute("wfkMap",wfkMap);
			req.setAttribute("yfkMap",yfkMap);
			req.setAttribute("zjMap",zjMap);
			req.setAttribute("ddzs", daoCtl.getStrRowValue(dao, Sqls.create("select count(*) from l_jsgg where add_time between '" + startdate + "' and '" + enddate + "'")));
			req.setAttribute("wfkzs", daoCtl.getStrRowValue(dao, Sqls.create("select sum(yfjk) from l_jsgg where isfh='0002' and add_time between '" + startdate + "' and '" + enddate + "'")));
			req.setAttribute("yfkzs", daoCtl.getStrRowValue(dao, Sqls.create("select sum(yfjk) from l_jsgg where isfh='0001' and add_time between '" + startdate + "' and '" + enddate + "'")));
			req.setAttribute("jkzj", daoCtl.getStrRowValue(dao, Sqls.create("select sum(yfjk) from l_jsgg where add_time between '" + startdate + "' and '" + enddate + "'")));
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	@At
	@Ok("->:/private/baobiao/zzc.html")
	public void zzc(HttpSession session, HttpServletRequest req, @Param("startdate") String startdate,
			 @Param("enddate") String enddate, @Param("type") String type){
		try {
			Gson gson = new Gson();
			enddate = EmptyUtils.isEmpty(enddate)?DateUtil.getToday():enddate;
			startdate = EmptyUtils.isEmpty(startdate)?DateUtil.getFirstMonDay(enddate):startdate;
			req.setAttribute("startdate",startdate);
			req.setAttribute("enddate",enddate);
			//得到付款人
			List<Sys_user> xzqhList = daoCtl.list(dao,Sys_user.class,Sqls.create(" select userid,realname from sys_user where unitid=0016 order by userid asc "));
			req.setAttribute("xzqhList",xzqhList);
			//图形报表Map
			Map<String,String> xyNameMap = daoCtl.getHTable(dao,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' order by location asc"));
			List<String> xyCode = daoCtl.getStrRowValues(dao,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' order by location asc"));
			req.setAttribute("xyNameMap",gson.toJson(xyNameMap));
			//开始处理报表数据
			Map<String,String> allMap = new HashMap<String, String>();//订单数量
			Map<String,String> wfkMap = new HashMap<String, String>();//未付款
			Map<String,String> yfkMap = new HashMap<String, String>();//已付款
			Map<String,String> zjMap = new HashMap<String, String>();//总计
			//查询付款人的订单数量
			String sqlstr=" select userid,count(*) from l_hkzd where fkrq between '" + startdate + "' and '" + enddate + "' group by userid";
			allMap = daoCtl.getHTable(dao,Sqls.create(sqlstr));
			//查询付款人未付款信息
			sqlstr = "select userid ,sum(sfjk) from l_hkzd where isfk='0002' and fkrq between '" + startdate + "' and '" + enddate + "' group by userid";
			wfkMap = daoCtl.getHTable(dao, Sqls.create(sqlstr));
			//查询订货单位已付款信息
			sqlstr = "select userid ,sum(sfjk) from l_hkzd where isfk='0001' and fkrq between '" + startdate + "' and '" + enddate + "' group by userid";
			yfkMap = daoCtl.getHTable(dao, Sqls.create(sqlstr));
			//订货单位所有账目信息																					
			sqlstr = "select userid ,sum(sfjk) from l_hkzd where fkrq between '" + startdate + "' and '" + enddate + "' group by userid";
			zjMap = daoCtl.getHTable(dao, Sqls.create(sqlstr));
			JSONArray array = new JSONArray();
			for(Sys_user user : xzqhList){
				JSONObject jsonroot = new JSONObject();
				jsonroot.put("id", user.getUserid());
				jsonroot.put("name",user.getRealname());
			}
			req.setAttribute("charsData", array.toString());
			req.setAttribute("allMap",allMap);
			req.setAttribute("wfkMap",wfkMap);
			req.setAttribute("yfkMap",yfkMap);
			req.setAttribute("zjMap",zjMap);
			req.setAttribute("ddzs", daoCtl.getStrRowValue(dao, Sqls.create("select count(*) from l_hkzd where fkrq between '" + startdate + "' and '" + enddate + "'")));
			req.setAttribute("wfkzs", daoCtl.getStrRowValue(dao, Sqls.create("select sum(sfjk) from l_hkzd where isfk='0002' and fkrq between '" + startdate + "' and '" + enddate + "'")));
			req.setAttribute("yfkzs", daoCtl.getStrRowValue(dao, Sqls.create("select sum(sfjk) from l_hkzd where isfk='0001' and fkrq between '" + startdate + "' and '" + enddate + "'")));
			req.setAttribute("jkzj", daoCtl.getStrRowValue(dao, Sqls.create("select sum(sfjk) from l_hkzd where fkrq between '" + startdate + "' and '" + enddate + "'")));
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	@At
	@Ok("->:/private/baobiao/zcfx.html")
	public void zcfx(HttpSession session, HttpServletRequest req,@Param("startdate") String startdate){
		try {
			Gson gson = new Gson();
			startdate = EmptyUtils.isEmpty(startdate)?DateUtil.getToday():startdate;
			req.setAttribute("year",DateUtil.getToday().substring(0, 4));
			//当前年份的第一天
			String firstday = startdate .substring(0, 4)+"-01-01";
			//当前年份的最后一天
			String endday = DateUtil.getToday();
			if(!EmptyUtils.isEmpty(startdate)){
				endday = startdate +"-12-31";
			}
			//得到单位
			List<Sys_unit> xzqhList = daoCtl.list(dao,Sys_unit.class,Sqls.create(" select id,name from sys_unit where unittype = 88 order by id asc "));
			req.setAttribute("xzqhList",xzqhList);
			List<String> months = DateUtil.getMonthBetween(firstday,endday);
			req.setAttribute("months",months);
			String a="select CONCAT(YEAR(fkrq),'-',DATE_FORMAT(fkrq,'%m')) months ,sum(sfjk) as "
					+ "count_amount from l_hkzd WHERE fkrq BETWEEN '"+firstday+"' AND '"+endday+"' group by months";
			//开始处理报表数据
			Map<String,String> srMap = new HashMap<String, String>();
			Map<String,String> zcMap = new HashMap<String, String>();
			Map<String,String> ylMap = new HashMap<String, String>();
			//查询月度支出
			String sqlstr="select CONCAT(YEAR(fkrq),'-',DATE_FORMAT(fkrq,'%m')) months ,sum(sfjk) as "
					+ "count_amount from l_hkzd WHERE fkrq BETWEEN '"+firstday+"' AND '"+endday+"' group by months";
			zcMap = daoCtl.getHTable(dao,Sqls.create(sqlstr));
			//查询月度收入
			sqlstr="select CONCAT(YEAR(add_time),'-',DATE_FORMAT(add_time,'%m')) months ,sum(yfjk) as "
					+ "count_amount from l_jsgg WHERE add_time BETWEEN '"+firstday+"' AND '"+endday+"' group by months";
			srMap = daoCtl.getHTable(dao, Sqls.create(sqlstr));
			//盈利情况
			for(String month : months){
				int sr = 0;
				int zc = 0;
				if(EmptyUtils.isNotEmpty(srMap.get(month))){
					sr = Integer.valueOf(srMap.get(month));
				}
				if(EmptyUtils.isNotEmpty(zcMap.get(month))){
					zc = Integer.valueOf(zcMap.get(month));
				}
				String yl =  (sr - zc)+"";
				ylMap.put(month, yl);
			}
			req.setAttribute("srMap",srMap);
			req.setAttribute("zcMap",zcMap);
			req.setAttribute("ylMap",ylMap);
			req.setAttribute("srzj", daoCtl.getStrRowValue(dao, Sqls.create("select sum(yfjk) from l_jsgg where add_time between '" + firstday + "' and '" + endday + "'")));
			req.setAttribute("zczj", daoCtl.getStrRowValue(dao, Sqls.create("select sum(sfjk) from l_hkzd where fkrq between '" + firstday + "' and '" + endday + "'")));
			req.setAttribute("ylzj", daoCtl.getStrRowValue(dao, Sqls.create("select sr.sum-zc.sum as count_amount from (select sum(yfjk) as sum from l_jsgg WHERE add_time BETWEEN'" + firstday + "' and '" + endday + "')sr,(select sum(sfjk) as sum from l_hkzd WHERE fkrq BETWEEN '" + firstday + "' and '" + endday + "' )zc")));
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	@At
	@Ok("->:/private/baobiao/ywfx.html")
	public void ywfx(){
		
	}
	
	/**
	 * 各个广告所占份额
	 * @param session
	 * @param response
	 * @param req
	 * @throws IOException
	 */
	@At
	public void ggdfe(HttpSession session, HttpServletResponse response,HttpServletRequest req) throws IOException{
		List<Chart> chartlist =new ArrayList<Chart>();
		//得到列，订货单位
		List<Sys_unit> unitList = daoCtl.list(dao,Sys_unit.class,Sqls.create(" select id,name from "
				+ "sys_unit where unittype = 88 order by id asc "));
		//订货单位所有账目信息
		Map<String,String> zjMap = new HashMap<String, String>();//总计
		String sqlstr = "select unitid ,sum(yfjk) from l_jsgg group by unitid";
		zjMap = daoCtl.getHTable(dao, Sqls.create(sqlstr));
		for(Sys_unit unit : unitList){
			Chart chart=new Chart();
			chart.setTime(unit.getName());
			chart.setSumCount(zjMap.get(unit.getId()));
			chartlist.add(chart);
		}
		JSONObject param=new JSONObject();
		JSONArray json = JSONArray.fromObject(chartlist);
		param.put("RowCount", chartlist.size());
		param.put("Rows", json);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(param.toString());
		out.flush();
		out.close();
		Gson gson = new Gson();
	}
	
	/**
	 * 收入折线图
	 * @param session
	 * @param response
	 * @param req
	 * @throws IOException
	 */
	@At
	public void srzxq(HttpSession session, HttpServletResponse response,HttpServletRequest req) throws IOException{
		List<Chart> chartlist =new ArrayList<Chart>();
		String[] months = DateUtil.getLast12Months();
		for(int i=0;i<months.length;i++){
			System.out.println("====>"+months[i]);
		}
		/*//查询月度支出
		String sqlstr="select CONCAT(YEAR(fkrq),'-',DATE_FORMAT(fkrq,'%m')) months ,sum(sfjk) as "
				+ "count_amount from l_hkzd WHERE fkrq BETWEEN '"+firstday+"' AND '"+endday+"' group by months";
		zcMap = daoCtl.getHTable(dao,Sqls.create(sqlstr));
		//查询月度收入
		sqlstr="select CONCAT(YEAR(add_time),'-',DATE_FORMAT(add_time,'%m')) months ,sum(yfjk) as "
				+ "count_amount from l_jsgg WHERE add_time BETWEEN '"+firstday+"' AND '"+endday+"' group by months";
		srMap = daoCtl.getHTable(dao, Sqls.create(sqlstr));
		//盈利情况
		for(String month : months){
			int sr = 0;
			int zc = 0;
			if(EmptyUtils.isNotEmpty(srMap.get(month))){
				sr = Integer.valueOf(srMap.get(month));
			}
			if(EmptyUtils.isNotEmpty(zcMap.get(month))){
				zc = Integer.valueOf(zcMap.get(month));
			}
			String yl =  (sr - zc)+"";
			ylMap.put(month, yl);
		}
		for(Sys_unit unit : unitList){
			Chart chart=new Chart();
			chart.setTime(unit.getName());
			chart.setSumCount(zjMap.get(unit.getId()));
			chartlist.add(chart);
		}*/
		JSONObject param=new JSONObject();
		JSONArray json = JSONArray.fromObject(chartlist);
		param.put("RowCount", chartlist.size());
		param.put("Rows", json);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out=response.getWriter();
		out.println(param.toString());
		out.flush();
		out.close();
		Gson gson = new Gson();
	}
}