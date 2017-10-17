package com.hits.modules.sys;


import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.licenseFilter;
import com.hits.common.util.DateUtil;
import com.hits.common.util.DecodeUtil;
import com.hits.common.util.OnlineUtil;
import com.hits.common.util.StringUtil;
import com.hits.modules.sys.bean.Sys_safeconfig;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.Decode64Util;
import com.hits.util.EmptyUtils;
import com.hits.util.SysLogUtil;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 * 
 */
@IocBean
@At("/private")
@Filters({ @By(type = GlobalsFilter.class),@By(type = licenseFilter.class) })
public class LoginAction extends BaseAction {
	@Inject
	protected Dao dao;
	@At("/doLogin")
	public String login(@Param("loginname") String loginname, @Param("password") String password, @Param("verifcode") String verifcode,
			HttpSession session, HttpServletRequest req) {
		loginname=new String(Decode64Util.Decrypt(loginname));
		password=new String(Decode64Util.Decrypt(password));
		verifcode=new String(Decode64Util.Decrypt(verifcode));
		String vcode = StringUtil.null2String(session.getAttribute("ValidateCode"));
		session.setAttribute("ValidateCode","");
		if (Strings.isBlank(loginname) || Strings.isBlank(password))
			return "用户名及密码不能为空！";
		
		if (EmptyUtils.isNotEmpty(vcode)&&!vcode.equals(verifcode))
			return "验证码不正确！"; 
		Sys_user user = daoCtl.detailByName(dao,Sys_user.class, "oldloginname", loginname);
		if(user!=null){
			return "您的新账号是："+user.getLoginname()+",请用新账号登陆";
		}
		
		user = daoCtl.detailByName(dao,Sys_user.class, "loginname", loginname);
		
		if (user == null){
			//增加日志
			SysLogUtil.addLog(dao,  loginname, SysLogUtil.login_log_type, "用户名不存在！");
			return "您输入的密码和账户名不匹配！";
		}
		if (user.getState() == 1){
			//增加日志
			SysLogUtil.addLog(dao, loginname, SysLogUtil.login_log_type, "用户被禁止登陆");
			return "用户被禁止登陆。请联系管理员！";
		}
		if (!StringUtil.null2String(password).equals(DecodeUtil.Decrypt(user.getPassword()))) {
			int all = 5;
			int count = StringUtil.StringToInt(StringUtil.null2String(session.getAttribute("errorlogincount")), 0);
			if (count > 4) {
				if (user.getState() == 0){
					daoCtl.update(dao,Sys_user.class, Chain.make("state", 1),Cnd.where("loginname", "=", user.getLoginname()));
				}
				session.removeAttribute("errorlogincount");
				//增加日志
				SysLogUtil.addLog(dao, loginname, SysLogUtil.login_log_type, "密码输错次数过多，用户已被禁用！");
				return "密码输错次数过多，用户已被禁用！";
			} else {
				session.setAttribute("errorlogincount", count + 1);
			}

			all = all - count;
			//增加日志
			SysLogUtil.addLog(dao, loginname, SysLogUtil.login_log_type, "密码不正确，还有" + all + "次机会！");
			return "您输入的密码和账户名不匹配！";
		}
		String ip = getIpAddr(req);
		
		Sys_safeconfig safe = daoCtl.detailByName(dao,Sys_safeconfig.class,"state",0);
		
		if (safe != null) {
			if (safe.getType() == 0) // 拒绝登陆IP
			{
				if (safe.getNote() != null && safe.getNote().indexOf(ip) != -1) {
					return "用户当前IP地址被禁止登陆。";
				}
			} else // 允许登陆IP
			{
				if (safe.getNote() != null && safe.getNote().indexOf(ip) == -1) {
					return "用户当前IP地址被禁止登陆。";
				}
			}
		}
		Sys_unit u = daoCtl.detailByName(dao,Sys_unit.class, user.getUnitid());
		if (u != null){
			user.setUnitname(u.getName());
			user.setXzqh(u.getXzqh());
		}
		user.setLogincount(user.getLogincount() + 1);
		user.setLoginip(ip);
		user.setLogintime(DateUtil.getCurDateTime());
		user.setLogintype(0);
		daoCtl.update(dao,user);
		Sql sql = Sqls.create("select roleid from sys_user_role where userid=@userid");
		sql.params().set("userid", user.getUserid());
		List<String> rolelist = daoCtl.getStrRowValues(dao, sql);
		// 判断是否为系统管理员角色
		if (rolelist.contains("2")) {
			user.setSysrole(true);
		} else {
			user.setSysrole(false);
		}
		user.setRolelist(rolelist);//设置用户角色
		Sql sql1 = Sqls
				.create("select distinct resourceid from sys_role_resource where ( roleid in(select roleid from sys_user_role where userid=@userid) or roleid=1) and resourceid not in(select id from sys_resource where state=1)");
		sql1.params().set("userid", user.getUserid());
		user.setReslist(daoCtl.getStrRowValues(dao, sql1));//设置资源
		// 获取用户资源button HashMap
		List<List<String>> reslist = daoCtl
				.getMulRowValue(
						dao,
						Sqls.create("SELECT a.url,b.button FROM sys_resource a,sys_role_resource b WHERE a.ID=b.RESOURCEID "
								+ " AND (b.button<>'' or b.button is not null) AND ( b.roleid IN(SELECT roleid FROM sys_user_role WHERE userid="
								+ user.getUserid()
								+ ") OR roleid=1) "
								+ " AND b.resourceid NOT IN(SELECT id FROM sys_resource WHERE state=1)"));
		Hashtable<String, String> btnmap = new Hashtable<String, String>();
		for (List<String> obj : reslist) {
			String key = StringUtil.null2String(obj.get(0));
			String value = StringUtil.null2String(btnmap.get(key))
					+ StringUtil.null2String(obj.get(1));
			btnmap.put(key, value);
		}
		user.setBtnmap(btnmap);//设置用户按钮权限
		session.setAttribute("userSession", user);
		session.setAttribute("validate", "");
		//增加日志
		SysLogUtil.addLog(dao, loginname, SysLogUtil.login_log_type, "登陆系统成功");
		return "true";

	}
	public String getIpAddr(HttpServletRequest request) {  
	    String ip = request.getHeader("x-forwarded-for");  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("PRoxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getHeader("WL-Proxy-Client-IP");  
	    }  
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
	        ip = request.getRemoteAddr();  
	    }  
	    return ip;  
	}  
	@At
	@Ok(">>:/private/login")
	public void logout(HttpSession session, HttpServletRequest req) {
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		if(EmptyUtils.isNotEmpty(user)){
			//增加日志
			SysLogUtil.addLog(dao, user, SysLogUtil.login_log_type, "退出系统");
			session.removeAttribute("userSession");
		}
	}

	@At
	@Ok("->:/private/login.html")
	public void login() {
	}
	@At
	@Ok("raw")
	public int Online(@Param("loginname") String loginname,HttpSession session) { 
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		if(user==null){ 
			return -2;
		}
		if (loginname != null&&!"".equals(loginname)) {
			OnlineUtil.addUser(loginname, String.valueOf(1));
		} 
		return OnlineUtil.getOnlineCount(String.valueOf(1));
	} 

}
