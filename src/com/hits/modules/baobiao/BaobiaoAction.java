package com.hits.modules.baobiao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.servlet.ServletUtilities;
import org.jfree.data.category.DefaultCategoryDataset;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
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
import com.hits.modules.sys.bean.Sys_unit;
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
	@Ok("->:/private/baobiao/xxsb.html")
	public void xxsb(HttpSession session, HttpServletRequest req, @Param("startdate") String startdate,
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
	
	
}