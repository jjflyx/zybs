package com.hits.modules.jlry;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hits.util.*;

import org.nutz.dao.*;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param; 

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.SqlFilter;
import com.hits.common.filter.UserLoginFilter;

import com.hits.modules.jlry.bean.Reward_info;
import com.hits.modules.com.comUtil;
import com.hits.modules.gtxt.bean.Xyzt_info;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;

import net.sf.json.JSONObject;

/**
 * @author hzg
 * @time 2016-02-29 10:57:21
 * 
 */
@IocBean
@At("/private/jlxx")
@Filters({ @By(type = GlobalsFilter.class), @By(type = SqlFilter.class), @By(type = UserLoginFilter.class) })
public class Reward_infoAction extends BaseAction {
	@Inject
	protected Dao dao;
	public static String url="/private/jlxx/detail?jlid=@id";
	
	
	@At("/tolist")
	@Ok("->:/private/jlry/jllist.html")
	public void user(@Param("xy_type") String xy_type,HttpServletRequest req,HttpSession session) {
		req.setAttribute("xy_type", xy_type);
		req.setAttribute("ztlxlist", comUtil.xyztlxMap);
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
	}
	@At
	@Ok("->:/private/jlry/jlcxlist.html")
	public void jlcx(HttpServletRequest req,HttpSession session) {
		req.setAttribute("ztlxlist", comUtil.xyztlxMap);
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("csvalueList", comUtil.sxxwtypeList);
		req.setAttribute("sxxwtypeMap", JSONObject.fromObject(comUtil.sxxwtypeMap));
	}
	/*
	 * 奖励信息list
	 * */
	@At
	@Ok("raw")
	public String jllist(HttpSession session,HttpServletRequest req,@Param("xyzt_name") String xyzt_name,
			@Param("iscx") int iscx,@Param("startdate") String startdate,@Param("enddate") String enddate,
			@Param("xyzt_type") String xyzt_type,@Param("xy_type") String xy_type,@Param("page") int curPage, @Param("rows") int pageSize) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String unitid = user.getUnitid();
		String name = user.getLoginname();
		Sql sql = null;
		String sqlstr = "select a.id as jlid,a.jl_date,b.name,b.type,a.note,a.xy_type,a.create_date,c.name as unitname from reward_info a,xyzt_info b,sys_unit c where a.xyzt_id = b.id and a.unitid=c.id and a.type = 0 ";
		if(iscx!=1){
			sqlstr+=" and a.unitid='"+unitid+"' and a.actor = '"+name+"'";
		}
		if(EmptyUtils.isNotEmpty(xyzt_name)){
			sqlstr += "and b.name like '%"+xyzt_name+"%'";
		}
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr += "and b.type = '"+xyzt_type+"'";
		}
		if(EmptyUtils.isNotEmpty(xy_type)){
			sqlstr += "and a.xy_type like '"+xy_type+"%'";
		}
		if(EmptyUtils.isNotEmpty(startdate)){
			sqlstr += "and a.jl_date >= '"+startdate+"'";
		}
		if(EmptyUtils.isNotEmpty(enddate)){
			sqlstr += "and a.jl_date <= '"+enddate+"'";
		}
		sqlstr += " order by a.id desc";
		System.out.println("sqlstr:"+sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		return PageObj.getPageJSON(qr);
	}
	
	//奖励信息添加页面
	@At
	@Ok("->:/private/jlry/jlAdd.html")
	public void add(@Param("xytype") String xytype,HttpServletRequest req,HttpSession session) {
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("xytype", xytype);
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String unitid = user.getUnitid();
		Sys_unit unit = daoCtl.detailByName(dao, Sys_unit.class, unitid);
		if(!"88".equals(unit.getUnittype())&&!"90".equals(unit.getUnittype())){
			unitid=unitid.length()>4?unitid.substring(0,unitid.length()-4):unitid;
		}
		if(EmptyUtils.isNotEmpty(xytype) && ("00050001".equals(xytype) || "00050002".equals(xytype) || "00050003".equals(xytype) )){
			String id = "";
			if("00050001".equals(xytype)){
				id = "0010";
			}else if("00050002".equals(xytype)){
				id = "0011";
			}else if("00050003".equals(xytype)){
				id = "0012";
			}
			req.setAttribute("unit", id);
			req.setAttribute("unitname", daoCtl.getStrRowValue(dao,Sqls.create(" select name from sys_unit where id = '"+id+"' ")));
		}else {
			req.setAttribute("unit", unitid);
		}
	}
	
	//奖励信息添加操作
	@At
	@Ok("raw")
	public boolean addSave(HttpSession session,@Param("xyzt_id") String xyzt_id,@Param("unit") String unit,@Param("jl_date")String jl_date,
			@Param("xy_type") String xy_type,@Param("note") String note,@Param("jlry_type") String jlry_type,@Param("file_html") String file_html) {
		boolean flag =false;
		try {
			Sys_user user = (Sys_user) session.getAttribute("userSession");
			String unitid = user.getUnitid();
			String name = user.getLoginname();
			
			Reward_info reward = new Reward_info();
			reward.setXyzt_id(StringUtil.StringToInt(xyzt_id));
			reward.setCreate_date(DateUtil.getCurDateTime());
			reward.setUnit(unit);
			reward.setJl_date(jl_date);
			reward.setNote(note);
			reward.setType(StringUtil.StringToInt(jlry_type));
			reward.setUnitid(unitid);
			reward.setActor(name);
			reward.setXy_type(xy_type);
			System.out.println(file_html);
			reward.setFile_html(Decode64Util.Decrypt(file_html));
			reward.setXzqh(daoCtl.getStrRowValue(dao,Sqls.create(" select xzqh from sys_unit where id = '"+reward.getUnitid()+"' ")));
			flag = daoCtl.add(dao, reward);
			String xyzt = daoCtl.getStrRowValue(dao,Sqls.create(" select name from xyzt_info where id = '"+reward.getXyzt_id()+"' "));
			String lognote=SysLogUtil.getLogNote(reward, "jlxx");
			SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag,1,
					"新增信用主体奖励信息:" + xyzt, "奖励信息", lognote, "", "",url.replace("@id", reward.getId()+""));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}
	
	//奖励信息修改页面
	@At
	@Ok("->:/private/jlry/jlUpdate.html")
	public void toUpdate(HttpServletRequest req,@Param("jlid") String jlid){
		 int id = StringUtil.StringToInt(jlid);
		 //Sql sql = Sqls.create("select * from reward_info where id = '"+jlid+"'");
		 Reward_info reward= daoCtl.detailById(dao, Reward_info.class, id);
		 Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, reward.getXyzt_id());
		 xyzt.setType((xyzt.getType()).trim());
		 req.setAttribute("ztMap", comUtil.xyztlx);
		 req.setAttribute("reward", reward);
		 req.setAttribute("xyzt", xyzt);
		if(EmptyUtils.isNotEmpty(reward.getUnit()) && ( "0010".equals(reward.getUnit()) || "0011".equals(reward.getUnit()) || "0012".equals(reward.getUnit()) )) {
			req.setAttribute("jljg", daoCtl.getHTable(dao,Sqls.create(" select id,name from sys_unit where id in ('0010','0011','0012') ")));
		}
	}
	
	//奖励信息修改操作
	@At
	@Ok("raw")
	public boolean Update(@Param("..") Reward_info reward,HttpSession session){
		boolean flag =false;
		try {
			Sys_user user=(Sys_user) session.getAttribute("userSession");
			Reward_info oldObj=daoCtl.detailById(dao, Reward_info.class, reward.getId());
			if(EmptyUtils.isEmpty(reward.getFile_html())){
				reward.setFile_html(oldObj.getFile_html());
			}else{
				reward.setFile_html(Decode64Util.Decrypt(reward.getFile_html()));
			}
			flag = daoCtl.update(dao, reward);
			String xyzt = daoCtl.getStrRowValue(dao,Sqls.create(" select name from xyzt_info where id = '"+reward.getXyzt_id()+"' "));
			String lognote=SysLogUtil.getLogNote(oldObj, reward, "jlxx");
			SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag,2, 
					"更新信用主体奖励信息:" + xyzt, "奖励信息", lognote, "", "",url.replace("@id", reward.getId()+""));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	//转入添加信用主体页面
	@At
	@Ok("->:/private/jlry/jlryAddfr.html")
	public void toAddfr(@Param("ctype")int ctype,@Param("zttype")String zttype,@Param("frbutton") int frbutton,
			@Param("xytype")String xytype,@Param("tname")String tname,HttpServletRequest req,HttpSession session){
		
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String unitid = user.getUnitid();
		//Sql sqlztlx = Sqls.create("select type from reward_info where unitid = '"+unitid+"' and actor = '"+user.getLoginname()+"' group by type");
		System.out.println("ctype:"+ctype);
		if(EmptyUtils.isNotEmpty(zttype)){
			String whereSql="";
			if(zttype.indexOf(",")>0){
				whereSql="value in ("+zttype+")";
			}else{
				whereSql="value="+zttype;
			}
			Sql sql=Sqls.create("select value,name from cs_value where "+whereSql+" and typeid='00010006' and state=0 order by location asc,id asc ");
			req.setAttribute("ztlxlist", daoCtl.list(dao, sql));
		}else{
			req.setAttribute("ztlxlist", comUtil.xyztlxMap);
		}
		
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("ctype",ctype);
		req.setAttribute("xytype",xytype);
		req.setAttribute("tname",tname);
		req.setAttribute("frbutton",frbutton);
	}
	
	//添加信用主体页面列表
	@At
	@Ok("raw")
	public String addFrlist(@Param("xyzt_name") String xyzt_name,@Param("xyzt_type")String xyzt_type,
			@Param("xytype")String xytype,@Param("tname")String tname,
			@Param("page") int curPage, @Param("rows") int pageSize){
		Sql sql = null;
		String sqlstr = "select zt.* from xyzt_info zt where status=1";
		System.out.println("11xytype:"+xytype);
		System.out.println("11tname"+tname);
		if(EmptyUtils.isNotEmpty(xytype)&&EmptyUtils.isNotEmpty(tname)&&"zz_info".equals(tname.toLowerCase())){
			sqlstr = "select distinct zt.* from xyzt_info zt,zz_info t where t.xyzt_id=zt.id and xy_type='"+xytype+"' ";
		}
		if(EmptyUtils.isNotEmpty(xyzt_name)){
			sqlstr += " and zt.name like '%"+xyzt_name+"%'";
		}
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr += " and  zt.type = '"+xyzt_type+"'";
		}
		sqlstr += " order by zt.id desc";
		System.out.println("sqlstr:"+sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map<String,String>> list=(List<Map<String,String>>)qr.getList();
		for(int i=0;i<list.size();i++){
			list.get(i).put("type", list.get(i).get("type").toString().trim());	
		}
		return PageObj.getPageJSON(qr);
	}
	
	
	//奖励信息浏览页面
	@At
	@Ok("->:/private/jlry/jlDetail.html")
	public void detail(@Param("jlid") String jlid,HttpServletRequest req){
		int id = StringUtil.StringToInt(jlid);
		Reward_info reward = daoCtl.detailById(dao, Reward_info.class, id);
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, reward.getXyzt_id());
		xyzt.setType((xyzt.getType()).trim());
		Hashtable<String, String> zttype = comUtil.xyztlx;
		req.setAttribute("qyzt", comUtil.qyzt);
		req.setAttribute("qytype", comUtil.qytype);
		req.setAttribute("ztMap", zttype);
		req.setAttribute("reward", reward);
		req.setAttribute("xyzt", xyzt);
		if(EmptyUtils.isNotEmpty(reward.getUnit()) && ( "0010".equals(reward.getUnit()) || "0011".equals(reward.getUnit()) || "0012".equals(reward.getUnit()) )) {
			req.setAttribute("jljg", daoCtl.getHTable(dao,Sqls.create(" select id,name from sys_unit where id in ('0010','0011','0012') ")));
		}
	}
	
	

}