package com.hits.modules.sys;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hits.modules.sys.bean.*;
import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.hits.common.action.BaseAction;
import com.hits.common.config.Globals;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.DateUtil;
import com.hits.common.util.DecodeUtil;
import com.hits.common.util.SortHashtable;
import com.hits.common.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Wizzer.cn
 * @time 2012-9-21 下午1:30:15
 * 
 */
@IocBean
@At("/private/sys/user")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class UserAction extends BaseAction {
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/sys/user.html")
	public void user(HttpSession session, HttpServletRequest req) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String unitid = user.getUnitid();
		req.setAttribute("unitid", unitid);
	}

	@At
	@Ok("raw")
	public String tree(@Param("id") String id, HttpSession session)
			throws Exception {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		id = StringUtil.null2String(id);
		JSONArray array = new JSONArray();
		Criteria cri = Cnd.cri();
		if (user.getSysrole()) // 判断是否为系统管理员角色
		{
			cri.where().and("id", "like", id + "____");
			cri.getOrderBy().asc("location");
			cri.getOrderBy().asc("id");
		} else if(user.getUnitid().equals("00080023")){
			cri.where().and("id", "like", id + "____");
			cri.where().and("id","<>","0001");
			cri.getOrderBy().asc("location");
			cri.getOrderBy().asc("id");
		}else {
			if ("".equals(id)) {
				cri.where().and("id", "=", user.getUnitid());
				cri.getOrderBy().asc("location");
				cri.getOrderBy().asc("id");
			} else {
				cri.where().and("id", "like", id + "____");
				cri.getOrderBy().asc("location");
				cri.getOrderBy().asc("id");
			}
		}
		List<Sys_unit> unitlist = daoCtl.list(dao, Sys_unit.class, cri);
		int i = 0;
		for (Sys_unit u : unitlist) {
			String pid = u.getId().substring(0, u.getId().length() - 4);
			int num = daoCtl.getRowCount(dao, Sys_unit.class,
					Cnd.wrap("id like '" + u.getId() + "____'"));
			if (i == 0 || "".equals(pid))
				pid = "0";
			JSONObject obj = new JSONObject();
			obj.put("id", u.getId());
			obj.put("pId", pid);
			obj.put("name", u.getName());
			obj.put("url", "javascript:list(\"" + u.getId() + "\")");
			obj.put("target", "_self");
			obj.put("isParent", num > 0 ? true : false);
			obj.put("icon", Globals.APP_BASE_NAME + "/images/icons/icon042a1.gif");
			array.add(obj);
			i++;
		}
		return array.toString();
	}

	@At
	@Ok("raw")
	public String getuname(@Param("unitid") String unitid) {
		if ("".equals(unitid))
			return "所有机构";
		return daoCtl.detailByName(dao, Sys_unit.class, unitid).getName();
	}

	@At
	@Ok("raw")
	public JSONObject list(@Param("unitid") String unitid,
			@Param("page") int curPage,@Param("rows") int pageSize,
			@Param("SearchUserName") String SearchUserName,@Param("lock") String lock,HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Criteria cri = Cnd.cri();
		 
		if(!"".equals(unitid)){
			cri.where().and("unitid","like",unitid+"%");
		}else{
			if (user.getSysrole()){ // 判断是否为系统管理员角色
				cri.where().and("1","=",1);
			}else{
				cri.where().and("unitid","like",user.getUnitid()+"%");
			}
		}
		if("1".equals(lock)){
			cri.where().and("state","=",1);
		}
		if (!"".equals(SearchUserName)) {
			SqlExpressionGroup exp = Cnd.exps("loginname", "LIKE", "%"+SearchUserName+"%").or("realname", "LIKE", "%"+SearchUserName+"%");
			cri.where().and(exp);
		} 
		cri.getOrderBy().asc("loginname");
		return daoCtl.listPageJson(dao, Sys_user.class, curPage,
				pageSize, cri);
	}

	@SuppressWarnings("rawtypes")
	@At
	@Ok("->:/private/sys/userAdd.html")
	public void toadd(@Param("unitid") String unitid1, HttpSession session,
			HttpServletRequest req)  {

		Sys_unit unit = daoCtl.detailByName(dao, Sys_unit.class, unitid1);
		req.setAttribute("unit", unit);

		Sys_user user = (Sys_user) session.getAttribute("userSession");
		
		Condition sql;
		Condition sqlunit;
		Condition sqlrole;
		List<Sys_role> list = null;
		List<Sys_role> rolelist = null;
		List<Sys_unit> unitlist = null;
		int sysrole = 0;
		if (user.getRolelist().contains(2 + "")) // 判断是否为系统管理员角色
		{
			sqlunit = Cnd
					.wrap("id in (select unitid from sys_role ) order by location,id asc");
			sqlrole = Cnd
					.wrap("unitid is null or unitid='' order by location,id asc");
			sysrole = 1;
		} else {
			sqlunit = Cnd
					.wrap("id in (select unitid from sys_role where unitid like '"
							+ user.getUnitid() + "%') order by location,id asc");
			sqlrole = Cnd
					.wrap("(unitid is null or unitid='') and id in (select roleid from sys_user_role where userid='"
							+ user.getUserid()
							+ "') order by location,id asc");
		}
		unitlist = daoCtl.list(dao, Sys_unit.class, sqlunit);
		req.setAttribute("sysrole", sysrole);
		Hashtable<String, String> hm = new Hashtable<String, String>();
		JSONArray array = new JSONArray();
		JSONObject jsonroot = new JSONObject();
		jsonroot.put("id", "");
		jsonroot.put("pId", "0");
		jsonroot.put("name", "角色列表");
		jsonroot.put("checked", false);
		jsonroot.put("nocheck", true);
		jsonroot.put("open", true);
		jsonroot.put("icon", Globals.APP_BASE_NAME
				+ "/images/icons/icon042a1.gif");
		if (!"".equals(sqlrole)) {
			rolelist = daoCtl.list(dao, Sys_role.class, sqlrole);
			for (int j = 0; j < rolelist.size(); j++) {

				Sys_role roleobj = rolelist.get(j);
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("id", String.valueOf(roleobj.getId()));
				jsonobj.put("pId", "");
				jsonobj.put("name", roleobj.getName());
				if (roleobj.getId() == 1) {
				jsonobj.put("checked", true);
			} else {
				jsonobj.put("checked", false);
			}
				jsonobj.put("url", "javascript:list(\"" + roleobj.getId()
						+ "\")");
				jsonobj.put("target", "_self");
				array.add(jsonobj);

			}

		}
		array.add(jsonroot);
		for (int i = 0; i < unitlist.size(); i++) { // 得到单位列表
			Sys_unit unitobj = unitlist.get(i);
			String unitid = unitobj.getId();
			sql = Cnd.wrap("unitid='" + unitid + "' order by id");
			list = daoCtl.list(dao, Sys_role.class, sql);
			String tunit = "";
			int t = 1;
			hm.put(unitid, unitid);
			for (int k = unitid.length(); k > 4; k = k - 4) { // 计算单位的上级（递归）
				t = k / 4 - 1;
				if (t != 0) {
					tunit = unitid.substring(0, unitid.length() - 4 * t);
				} else {
					tunit = unitid;
				}
				hm.put(tunit, tunit);
			}
			String name = "";

			for (int j = 0; j < list.size(); j++) { // 得到单位列表下的角色

				Sys_role roleobj = list.get(j);
				name = roleobj.getName();
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("id", String.valueOf(roleobj.getId()));
				jsonobj.put("pId", unitid);
				jsonobj.put("name", name);
				jsonobj.put("checked", false);
				jsonobj.put("url", "javascript:list(\"" + roleobj.getId()
						+ "\")");
				jsonobj.put("target", "_self");

				array.add(jsonobj);

			}
		}
		String temp = "";

		Map.Entry[] set = SortHashtable.getSortedHashtableByKey(hm);
		for (int i = 0; i < set.length; i++) {

			String key = set[i].getKey().toString();
			Sys_unit su = daoCtl.detailByName(dao, Sys_unit.class,
					String.valueOf(key));
			if (su != null) {
				String tempUid = String.valueOf(key);
				String tempName = su.getName();
				if (tempUid.length() == 4) {
					temp = "0";
				} else {
					temp = tempUid.substring(0, tempUid.length() - 4);
				}
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("id", tempUid);
				jsonobj.put("pId", temp);
				jsonobj.put("name", StringUtil.substr(tempName, 12));
				jsonobj.put("isParent", true);
				jsonobj.put("open", true);
				jsonobj.put("nocheck", true);
				jsonobj.put("icon", Globals.APP_BASE_NAME
						+ "/images/icons/icon042a1.gif");
				array.add(jsonobj);
			}
		}
		req.setAttribute("str", array.toString());
	}

	@At
	@Ok("raw")
	public boolean ajaxname(@Param("loginname") String loginname) {
		int res=daoCtl.getRowCount(dao, Sys_user.class, Cnd.where("loginname","=",loginname));
		if(res>0){
			return true;
		}
		return false;
	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") Sys_user user,
			@Param("checkids") String checkids, HttpServletRequest req) {
		Sys_user result = null;
		try {
			String[] ids = StringUtil.null2String(checkids).split(",");
			user.setPassword(DecodeUtil.Encrypt(user.getPassword()));
			user.setLogintime(DateUtil.getCurDateTime());
			result = daoCtl.addT(dao, user);
			if (result != null) {
				for (int i = 0; i < ids.length && (!"".equals(ids[0])); i++) {
					Sys_user_role syr = new Sys_user_role();
					syr.setUserid(result.getUserid());
					syr.setRoleid(StringUtil.StringToInt(StringUtil
							.null2String(ids[i])));
					daoCtl.add(dao, syr);
				}
				System.out.println("=================用户添加默认皮肤==========================");
				// 给用户添加默认皮肤
				Cms_user_style cmsUserStyle = new Cms_user_style();
				cmsUserStyle.setStylename("blue");
				cmsUserStyle.setUser_id(user.getUserid());
				daoCtl.addT(dao,cmsUserStyle);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@At
	@Ok("->:/private/sys/userUpdate.html")
	public void toupdate(@Param("userid") long userid,
			HttpSession session, HttpServletRequest req){

		Sys_user obj = daoCtl.detailById(dao, Sys_user.class, userid);
		Sys_unit unit = daoCtl.detailByName(dao, Sys_unit.class,
				obj.getUnitid());
		req.setAttribute("obj", obj);
		req.setAttribute("unit", unit);
		obj.setPassword(DecodeUtil.Decrypt(obj.getPassword()));
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		boolean self = false;
		if (user.getLoginname().equals(obj.getLoginname())) {
			self = true;
			req.setAttribute("self", self);
		}
		Hashtable<String, String> hashrole = new Hashtable<String, String>();
		Sql sql = Sqls
				.create("select roleid,'wiz' from sys_user_role where userid = '"
						+ obj.getUserid() + "'");
		hashrole = daoCtl.getHTable(dao, sql);

		LinkedList<Sys_role> list = null;
		LinkedList<Sys_role> rolelist = null;
		LinkedList<Sys_unit> unitlist = null;
		int sysrole = 0;
		Condition unitc;
		Condition rolec;
		if (user.getSysrole()) // 判断是否为系统管理员角色
		{
			unitc = Cnd
					.wrap("id in (select unitid from sys_role ) order by location,id asc");
			rolec = Cnd
					.wrap("unitid is null or unitid='' order by location,id asc");
			sysrole = 1;
		} else {
			unitc = Cnd
					.wrap("id in (select unitid from sys_role where unitid like '"
							+ user.getUnitid() + "%') order by location,id asc");
			rolec = Cnd
					.wrap("(unitid is null or unitid='') and id in (select roleid from sys_user_role where userid='"
							+ user.getUserid() + "') order by location,id asc");
		}
		unitlist = (LinkedList) daoCtl.list(dao, Sys_unit.class, unitc);
		req.setAttribute("sysrole", sysrole);
		Hashtable<String, String> hm = new Hashtable<String, String>();
		JSONArray array = new JSONArray();
		JSONObject jsonroot = new JSONObject();
		jsonroot.put("id", "");
		jsonroot.put("pId", "0");
		jsonroot.put("name", "角色列表");
		jsonroot.put("checked", false);
		jsonroot.put("nocheck", true);
		jsonroot.put("open", true);
		jsonroot.put("icon", Globals.APP_BASE_NAME
				+ "/images/icons/icon042a1.gif");
		if (!"".equals(rolec)) {
			rolelist = (LinkedList<Sys_role>) daoCtl.list(dao, Sys_role.class,
					rolec);
			for (int j = 0; j < rolelist.size(); j++) {

				Sys_role roleobj = rolelist.get(j);
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("id", String.valueOf(roleobj.getId()));
				jsonobj.put("pId", "");
				jsonobj.put("name", roleobj.getName());
				if (!"".equals(StringUtil.null2String(hashrole.get(String
						.valueOf(roleobj.getId()))))) {
					if (self) {
						jsonobj.put("chkDisabled", true);
					}
					jsonobj.put("checked", true);
				} else {
					jsonobj.put("checked", false);
				}
				array.add(jsonobj);

			}
		}
		array.add(jsonroot);
		for (int i = 0; i < unitlist.size(); i++) { // 得到单位列表
			Sys_unit unitobj = unitlist.get(i);
			String unitid = unitobj.getId();
			Condition c = Cnd.wrap("unitid='" + unitid + "' order by id");
			list = (LinkedList) daoCtl.list(dao, Sys_role.class, c);
			String tunit = "";
			int t = 1;
			hm.put(unitid, unitid);
			for (int k = unitid.length(); k > 4; k = k - 4) { // 计算单位的上级（递归）
				t = k / 4 - 1;
				if (t != 0) {
					tunit = unitid.substring(0, unitid.length() - 4 * t);
				} else {
					tunit = unitid;
				}
				hm.put(tunit, tunit);
			}
			String name = "";

			for (int j = 0; j < list.size(); j++) { // 得到单位列表下的角色

				Sys_role roleobj = list.get(j);
				name = roleobj.getName();
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("id", String.valueOf(roleobj.getId()));
				jsonobj.put("pId", unitid);
				jsonobj.put("name", name);
				if (!"".equals(StringUtil.null2String(hashrole.get(String
						.valueOf(roleobj.getId()))))) {
					jsonobj.put("checked", true);
				} else {
					jsonobj.put("checked", false);
				}
				array.add(jsonobj);

			}
		}
		String temp = "";

		Map.Entry[] set = SortHashtable.getSortedHashtableByKey(hm);
		for (int i = 0; i < set.length; i++) {

			String key = set[i].getKey().toString();
			Sys_unit su = daoCtl.detailByName(dao, Sys_unit.class,
					String.valueOf(key));
			if (su != null) {
				String tempUid = String.valueOf(key);
				String tempName = su.getName();
				if (tempUid.length() == 4) {
					temp = "0";
				} else {
					temp = tempUid.substring(0, tempUid.length() - 4);
				}
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("id", tempUid);
				jsonobj.put("pId", temp);
				jsonobj.put("name", StringUtil.substr(tempName, 12));
				jsonobj.put("isParent", true);
				jsonobj.put("open", true);
				jsonobj.put("nocheck", true);
				jsonobj.put("icon", Globals.APP_BASE_NAME
						+ "/images/icons/icon042a1.gif");
				array.add(jsonobj);

			}
		}
		req.setAttribute("str", array.toString());
	}

	@At
	public boolean update(@Param("..") Sys_user user,
			@Param("checkids") String checkids,
			@Param("oldloginname") String oldloginname) {
		boolean result = false;
		try {
			String[] ids = StringUtil.null2String(checkids).split(",");
			user.setPassword(DecodeUtil.Encrypt(user.getPassword()));
			user.setLogintime(DateUtil.getCurDateTime());
			result = daoCtl.update(dao, user);
			if (result) {
				daoCtl.delete(dao, "sys_user_role",
						Cnd.where("userid","=",user.getUserid()));
				for (int i = 0; i < ids.length && (!"".equals(ids[0])); i++) {
					Sys_user_role syr = new Sys_user_role();
					syr.setUserid(user.getUserid());
					syr.setRoleid(StringUtil.StringToInt(StringUtil
							.null2String(ids[i])));
					daoCtl.add(dao, syr);
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
		}
	}

	@At
	public boolean delete(@Param("ids") String ids) {
		boolean result = false;
		String[] id = StringUtil.null2String(ids).split(",");	 
		result= daoCtl.deleteByIdsLong(dao, Sys_user.class, id);
		if(result){
			Sql sql = Sqls.create("delete from sys_user_role where userid=@userid");
			for (int i = 0; i < id.length; i++) {
				sql.params().set("userid",StringUtil.StringToLong(StringUtil.null2String(id[i])));
				sql.addBatch();
			}
			dao.execute(sql);
		}
		return result;
		 
	}

	@At
	public boolean lock(@Param("ids") String ids) {
		String[] id = StringUtil.null2String(ids).split(",");
		boolean result = false;
		for (int i = 0; i < id.length; i++) {
			result = daoCtl.update(dao, Sys_user.class, Chain.make("state", 1),
			Cnd.where("userid", "=", StringUtil.StringToLong(StringUtil.null2String(id[i]))));
		}
		return result;
	}

	@At
	public boolean unlock(@Param("ids") String ids) {
		String[] id = StringUtil.null2String(ids).split(",");
		boolean result = false;
		for (int i = 0; i < id.length; i++) {
			result = daoCtl.update(dao, Sys_user.class, Chain.make("state", 0),
					Cnd.where("userid", "=", StringUtil.StringToLong(StringUtil.null2String(id[i]))));
		}
		return result;

	}

	/**
	 * 个人信息
	 * */
	@At
	@Ok("->:/private/sys/userInfo.html")
	public Sys_user info(HttpSession session,@Param("pwd")String pwd,HttpServletRequest req) {
		System.out.println("+======>"+pwd);
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		req.setAttribute("pwd", pwd);
		return daoCtl.detailById(dao, Sys_user.class, user.getUserid());
	}
	
	/**
	 * 强制修改密码
	 * */
	@At
	@Ok("->:/private/sys/xgmm.html")
	public Sys_user xgmm(HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		return daoCtl.detailById(dao, Sys_user.class, user.getUserid());
	}

	/***
	 * 更新个人信息
	 * */
	@At
	@Ok("raw")
	public String updateInfo(@Param("..") Sys_user user,
			@Param("userid") String userid, @Param("password2") String pass,
			@Param("oldpassword") String oldpassword) {
		Sys_user olduser = daoCtl.detailByName(dao, Sys_user.class, "userid",
				userid);
		boolean relogin = false;
		if (DecodeUtil.Decrypt(olduser.getPassword()).equals(oldpassword)) {
			if (!"".equals(pass)) {
				relogin = true;
				olduser.setPassword(DecodeUtil.Encrypt(pass));
			}
			if (!olduser.getLoginname().equals(user.getLoginname())) {
				relogin = true;
			}
			olduser.setLoginname(user.getLoginname());
			olduser.setRealname(user.getRealname());
			olduser.setDescript(user.getDescript());
			olduser.setPozition(user.getPozition());
			olduser.setAddress(user.getAddress());
			olduser.setTelephone(user.getTelephone());
			olduser.setMobile(user.getMobile());
			olduser.setEmail(user.getEmail());
			olduser.setLinkqq(user.getLinkqq());
			olduser.setLinkweb(user.getLinkweb());
			boolean up = daoCtl.update(dao, olduser);
			if (up && relogin) {
				return "relogin";
			} else if (up) {
				return "true";
			} else {
				return "false";
			}
		} else {
			return "error";
		}
	}

	@At
	@Ok("raw")
	public List<String> ajaxAllUsers(HttpSession session){
		//String resultStr = "";
		System.out.println("选择用户!");
		List<String> jsonStr = new ArrayList<String>();
		String sql = "select * from sys_user where loginname <>'superadmin'";
		List<Sys_user> list = daoCtl.list(dao, Sys_user.class,Sqls.create(sql));
		for(Sys_user user : list){
			//resultStr += user.getRealname()+"-"+user.getLoginname()+";";
			jsonStr.add("{realname:'"+user.getRealname()+"',loginname:'"+user.getLoginname()+"'}");
		}
		System.out.println("选择用户："+jsonStr);
		return jsonStr;
	}
	
	@At
	@Ok("->:/private/msg/selectUser.html")
	public void selectUser(@Param("checkeduser") String checkeduser, HttpServletRequest req){
		System.out.println("checkeduser="+checkeduser);
		req.setAttribute("checkeduser", checkeduser);
	}
	
	@At
	@Ok("raw")
	public List<String> getUser(@Param("idstr")String idstr){
		String[] ids = StringUtil.null2String(idstr).split(",");
		List<String> jsonStr = new ArrayList<String>();
		for(int i = 0;i<ids.length;i++){
			String sql = "select * from Sys_user where loginname = '" + ids[i]+"'";
			Sys_user user = daoCtl.detailBySql(dao, Sys_user.class, Sqls.create(sql));
			jsonStr.add("{realname:'"+user.getRealname()+"',loginname:'"+user.getLoginname()+"'}");
		}
		return jsonStr;
	}
	
	@At
	@Ok("raw")
	public List<String> findUser(@Param("unitid")String unitid){
		System.out.println("选择单位!");
		String sql = "select * from sys_user where unitid = "+unitid;
		List<Sys_user> list = daoCtl.list(dao,Sys_user.class,Sqls.create(sql));
		List<String> jsonStr = new ArrayList<String>();
		for(Sys_user user : list){
			jsonStr.add("{userid:'"+user.getUserid()+"',realName:'"+user.getRealname()+"',loginName:'"+user.getLoginname()+"'}");
		}
		return jsonStr;
	}
	@At
	@Ok("json")
	public JSONArray findUser_(@Param("unitid") String unitid,@Param("src")String src) {
		String sql = "";
		if(src.equals("sys")){
			sql = "select * from sys_user where unitid like '" + unitid + "%' order by loginname";
		}else if(src.equals("txl")){
			sql = "select * from sys_user where loginname in (select loginname from txl where groupid="+unitid+") order by loginname";
		}
		
		List<Sys_user> list = daoCtl.list(dao, Sys_user.class, Sqls.create(sql));
		JSONArray arr = new JSONArray();
		for (Sys_user user : list) {
			JSONObject json = new JSONObject();
			json.put("userid", user.getUserid());
			json.put("realName", user.getRealname());
			json.put("loginName", user.getLoginname());
			arr.add(json);
		}

		return arr;
	}
	
}
