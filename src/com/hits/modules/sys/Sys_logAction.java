package com.hits.modules.sys;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hits.modules.bean.File_info;
import com.hits.modules.com.comUtil;
import com.hits.modules.sys.bean.Sjts_log;
import com.hits.modules.sys.bean.Sys_log;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;

import net.sf.json.JSONObject;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.pager.Pager;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.google.gson.Gson;
import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.util.DateUtil;
import com.hits.util.EmptyUtils;

/**
 * @author 
 * @time 2015-05-28 12:37:34
 * 
 */
@IocBean
@At("/private/sys/syslog")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Sys_logAction extends BaseAction {
	@Inject
	protected Dao dao;
	/**
	 * 操作日志页面
	 * @param session
	 * @param req
	 */

	@At("/cz")
	@Ok("->:/private/sys/sys_CZlog.html")
	public void sys_log() {
	
	}
	
	/**
	 * 操作日志列表
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@At("/list")
	@Ok("raw")
	public JSONObject list(@Param("SearchUserName")String loginname,@Param("letterid")String letterid,@Param("page") int curPage, @Param("rows") int pageSize){
		String qsql="select t.*,u.realname from  Sys_log t,sys_user u where t.loginname=u.loginname and type='1' ";
		if(EmptyUtils.isNotEmpty(loginname)){
			qsql+=" and t.loginname='"+loginname+"'";
		}
		if(EmptyUtils.isNotEmpty(letterid)){
			qsql+=" and t.letter_id='"+letterid+"'";
		}
		String order=" order by id desc ";
		Sql sql=Sqls.create(qsql+order);
		return daoCtl.listPageJsonSql(dao, sql, curPage, pageSize);
	}
	/**
	 * 运行日志页面
	 * @param session
	 * @param req
	 */
	@At("/yx")
	@Ok("->:/private/sys/sys_YXlog.html")
	public void sys_yxlog(HttpSession session,HttpServletRequest req) {
	
	}
	/**
	 *  运行日志列表
	 * @param loginname
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@At("/yxlist")
	@Ok("raw")
	public JSONObject yxlist(@Param("SearchUserName")String loginname,@Param("page") int curPage, @Param("rows") int pageSize){
		String qsql="select t.*,u.realname from  Sys_log t,sys_user u where t.loginname=u.loginname and type='2' ";
		if(EmptyUtils.isNotEmpty(loginname)){
			qsql+=" and t.loginname='"+loginname+"'";
		}
		String order=" order by id desc ";
		Sql sql=Sqls.create(qsql+order);
		return daoCtl.listPageJsonSql(dao, sql, curPage, pageSize);
	}
	
	/**
	 * 功能描述:前往 日志查看页面
	 *
	 * @author (☆_☆)  2015年6月4日 下午2:15:29
	 * 
	 * @param type
	 * @param request
	 */
	@At("/?")
	@Ok("->:/private/sys/sys_log.html")
	public void log(@Param("types") Integer type,HttpServletRequest request) {
		request.setAttribute("type", type);
		Hashtable<String, String> usermap=daoCtl.getHTable(dao, Sqls.create("select loginname,realname from sys_user "));
		Gson gson=new Gson();
		request.setAttribute("userMap", gson.toJson(usermap));
	}
	
	/**
	 * 功能描述:查询日志列表
	 *
	 * @author (☆_☆)  2015年6月4日 下午2:15:56
	 * 
	 * @param curPage
	 * @param pageSize
	 * @param SearchUserName
	 * @return
	 */
	@At
	@Ok("raw")
	public JSONObject loglist(HttpSession session,@Param("page") int curPage,@Param("rows") int pageSize,
			@Param("startdate")String startdate,@Param("enddate")String enddate,@Param("czkssj")String czkssj,@Param("czjssj")String czjssj,
			@Param("mk_id")String mk_id,@Param("cz_id")String cz_id,@Param("unitid") String unitid,
			@Param("SearchUserName") String SearchUserName,@Param("type") String type,@Param("letterid")String letterid) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String qsql="select t.id,t.type,t.content,t.create_time,t.login_ip,t.bowser,t.letter_id,t.class_name,t.method_name,t.title,t.reason,t.basis,t.cz,t.success,t.url,t.note,t.loginname||'/'||u.realname as loginname from  Sys_log t,sys_user u where t.loginname=u.loginname and type='"+type+"' ";
		if(!user.getUnitid().equals("00080007")&&!user.getUnitid().equals("00080023")&&!user.getUnitid().equals("00080045")&&!user.getSysrole()){
			qsql+=" and u.unitid like '"+user.getUnitid()+"%'";
		}
		if(EmptyUtils.isNotEmpty(SearchUserName)){
			qsql+=" and t.loginname like '%"+SearchUserName+"%'";
		}
		if (EmptyUtils.isNotEmpty(letterid)) {
			qsql+=" and t.letter_id like '%"+letterid+"%'";
		}
		if(EmptyUtils.isNotEmpty(mk_id)&&!"0".equals(mk_id)){
			qsql += " and t.title = '"+mk_id+"' ";
		}
		if(EmptyUtils.isNotEmpty(cz_id)&&!"0".equals(cz_id)){
			qsql += " and t.cz = '"+cz_id+"' ";
		}
		if(EmptyUtils.isNotEmpty(startdate)){
			qsql+=" and t.create_time>='"+startdate+" 00:00:00' ";
		}
		if(EmptyUtils.isNotEmpty(enddate)){
			qsql+=" and t.create_time<='"+enddate+" 23:59:59' ";
		}
		System.out.println("操作开始时间："+czkssj+"操作结束时间："+czjssj);
		if(EmptyUtils.isNotEmpty(czkssj)&&EmptyUtils.isNotEmpty(czjssj)){
			qsql+=" and t.create_time between '"+czkssj+" 00:00:00' "+" and '"+czjssj+" 23:59:59' ";
		}
		
		if (EmptyUtils.isNotEmpty(unitid)) {
			qsql+=" and u.unitid='"+unitid+"'";
		}
		String order=" order by id desc ";
		System.out.println(qsql+order);
		Sql sql=Sqls.create(qsql+order);
		return daoCtl.listPageJsonSql(dao, sql, curPage, pageSize);
	}

	/**
	 * 功能描述:前往 日志查看页面
	 *
	 * @author Numbgui  2016年3月2日 下午2:15:29
	 * @param request
	 */
	@At
	@Ok("->:/private/sys/sys_gtxylog.html")
	public void xylog(HttpServletRequest request,HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		if(!user.getUnitid().equals("00080007")&&!user.getUnitid().equals("00080023")&&!user.getUnitid().equals("00080045")&&!user.getSysrole()){
			request.setAttribute("sjunit", user.getUnitid());
		}
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
	}
	
	@At
	@Ok("->:/private/sys/sys_gtxyDetail.html")
	public void logdetail(@Param("id") String id,HttpServletRequest req) {
		Sys_log obj=daoCtl.detailByName(dao, Sys_log.class, "id", id);
		req.setAttribute("obj", obj);
		if(EmptyUtils.isNotEmpty(obj.getXgfj())){
			File_info file = daoCtl.detailByName(dao, File_info.class, obj.getXgfj());
			req.setAttribute("file", file);
		}
		req.setAttribute("logMap", comUtil.logCzMap);
		req.setAttribute("userMap",comUtil.userMap);
	}
	
	/**
	 * JJF
	 * @param transname
	 * @param startdate
	 * @param enddate
	 * @param status
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@At
	@Ok("raw")
	public JSONObject sjtsList(@Param("transname") String transname,@Param("startdate") String startdate,@Param("enddate") String enddate,@Param("status") String status, @Param("page") int curPage,@Param("rows") int pageSize){
		String qsql="select id_batch,transname,status,to_char(logdate,'YYYY-MM-DD hh24:mi:ss') as enddate,to_char(replaydate,'YYYY-MM-DD hh24:mi:ss') as startdate from log_stsjjh where 1=1 ";
		if(EmptyUtils.isNotEmpty(transname)){
			qsql+=" and transname like '%"+transname+"%'";
		}
		if(EmptyUtils.isNotEmpty(startdate)){
			qsql+=" and to_char(replaydate,'YYYY-MM-DD hh24:mi:ss')>='"+startdate+" 00:00:00' ";
		}
		if(EmptyUtils.isNotEmpty(enddate)){
			qsql+=" and to_char(replaydate,'YYYY-MM-DD hh24:mi:ss')<='"+enddate+" 23:59:59' ";
		}
		if(EmptyUtils.isNotEmpty(status)){
			qsql+=" and status='"+status+"' ";
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			qsql+=" and to_char(replaydate,'YYYY-MM-DD hh24:mi:ss') between '"+startdate+" 00:00:00' "+" and '"+enddate+" 23:59:59' ";
		}
		String order=" order by id_batch desc ";
		System.out.println(qsql+order);
		Sql sql=Sqls.create(qsql+order);
		return daoCtl.listPageJsonSql(dao, sql, curPage, pageSize);
	}
	
	@At
	@Ok("->:/private/sys/sys_sjtsDetail.html")
	public void sjtsDetail(@Param("id") String id,HttpServletRequest req) {
		Sjts_log obj=daoCtl.detailByName(dao, Sjts_log.class, "id_batch", id);
		req.setAttribute("obj", obj);
		String str="";
		String []aa=obj.getLog_field().split("\n|\r\n|\r");
		for(String a:aa){
			if(a.indexOf("Insert row")!=-1){
				str+="<span style='"+"color:red"+"'>新增数据：</span>"+a+"</br>";
			}else if(a.indexOf("Found row for update")!=-1){
				str+="<span style='"+"color:red"+"'>修改数据：</span>"+a+"</br>";
			}
		}
		if("".equals(str)){
			str="暂无更新";
		}
		req.setAttribute("log_field", str);
		req.setAttribute("enddate",DateUtil.date2str(obj.getLOGDATE()) );
		req.setAttribute("startdate",DateUtil.date2str(obj.getEnddate()) );
	}
}