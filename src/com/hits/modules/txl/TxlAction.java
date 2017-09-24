package com.hits.modules.txl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
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
import com.hits.modules.bean.Txl;
import com.hits.modules.bean.Txl_group;
import com.hits.modules.com.comUtil;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;

@IocBean
@At("/private/txl")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class TxlAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At
	@Ok("->:/private/txl/txlList.html")
	public void toTxl(HttpServletRequest req) {
		String sql = "select id,name from txl_group order by id";
		Hashtable<String, String> ht = daoCtl.getHTable(dao, Sqls.create(sql));
		req.setAttribute("txl_group_json", Json.toJson(ht));
	}

	@At
	public String list(HttpSession session, @Param("groupid") String groupid, @Param("page") int curPage,
			@Param("rows") int pageSize) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Condition cnd = null;
		if (groupid == null || groupid.equals("")) {
			cnd = Cnd.wrap("groupid in (select id from txl_group where creater='" + user.getLoginname() + "')");

		} else {
			cnd = Cnd.wrap("groupid = " + groupid);
		}
		return daoCtl.listPageJson(dao, Txl.class, curPage, pageSize, cnd).toString();
	}

	@At
	@Ok("->:/private/txl/txlAdd.html")
	public void toAdd(HttpServletRequest req, HttpSession session, @Param("groupid") String groupid) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		List<String> roleList = user.getRolelist();
		if (roleList.contains("146")) {
			req.setAttribute("ishjzx", true);
		} else {
			req.setAttribute("ishjzx", false);
		}
		String sql = "select id,name from txl_group where creater='" + user.getLoginname() + "' order by id";
		List<Map> list = daoCtl.list(dao, Sqls.create(sql));
		req.setAttribute("groups", list);
		if (groupid == null || groupid.equals("")) {
			return;
		}
		Txl_group txl_group = daoCtl.detailById(dao, Txl_group.class, Integer.valueOf(groupid));
		req.setAttribute("txl_group", txl_group);
	}

	@At
	public boolean add(@Param("..") Txl txl) {
		Txl_group txl_group = daoCtl.detailById(dao, Txl_group.class, Integer.valueOf(txl.getGroupid()));
		if (txl_group.getIsInsider() == 0) {
			return daoCtl.add(dao, txl);
		}
		String[] lns = txl.getLoginname().split(";");
		String users = getLoginnamesplit(lns);
		Sql sql = Sqls.create("insert into txl(id,loginname,realname,groupid,phone,bz)"
				+ " select TXL_S.nextval,loginname,realname," + txl.getGroupid() + ",'"
				+ StringUtil.null2String(txl.getPhone()) + "','" + StringUtil.null2String(txl.getBz()) + "' "
						+ "from sys_user where loginname in "+users);
		return daoCtl.exeUpdateBySql(dao, sql);
	}
	public  String getLoginnamesplit(String[] ids) {
		if ((ids == null) || (ids.length == 0))
			return "('')";
		String result = "(";
		for (int i = 0; i < ids.length; i++) {
			if (i == ids.length - 1)
				result += "'" + ids[i]+"'";
			else
				result += "'" + ids[i] + "',";
		}
		result = result + ")";
		return result;
	}
	@At
	@Ok("->:/private/txl/txlUpdate.html")
	public void toUpdate(HttpServletRequest req, HttpSession session, @Param("id") String id) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String sql = "select id,name from txl_group where creater='" + user.getLoginname() + "' order by id";
		Hashtable<String, String> ht = daoCtl.getHTable(dao, Sqls.create(sql));
		req.setAttribute("txl_group_json", ht);
		Object obj = daoCtl.detailById(dao, Txl.class, Integer.valueOf(id));
		req.setAttribute("obj", obj);
	}

	@At
	public boolean update(@Param("..") Txl txl) {

		return daoCtl.update(dao, txl);
	}

	@At
	public boolean delete(@Param("ids") String ids) {

		return daoCtl.deleteByIds(dao, Txl.class, ids.split(","));
	}

	@At
	@Ok("->:/private/txl/selectUser.html")
	public void list_user(HttpSession session, HttpServletRequest req, @Param("type") String type) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");

		String sql = "select loginname id,realname name "
				+ " from sys_user where loginname not in ('9999','superadmin','" + user.getLoginname()
				+ "') order by loginname";
		req.setAttribute("list", daoCtl.list(dao, Sqls.create(sql)));
		req.setAttribute("type", type);
	}

	@At
	public String isvalid(@Param("groupid") String groupid) {
		if (groupid == null || groupid.equals("")) {
			return "1";
		}
		Txl_group t = daoCtl.detailById(dao, Txl_group.class, Integer.valueOf(groupid));
		return t.getIsInsider() + "";
	}

	@At
	@Ok("->:${obj}")
	public String selectUser(@Param("checkeduser") String checkeduser, @Param("type") String type,
			HttpServletRequest req, @Param("reqtype") boolean reqtype) {
		req.setAttribute("checkeduser", checkeduser);
		req.setAttribute("type", type);

		String html = "/private/msg/selectUser.html";
		if (reqtype) {
			html = "/private/txl/selectUser.html";
		}
		return html;
	}

	@At
	@Ok("raw")
	public List<String> getUser(@Param("idstr") String idstr) {
		String[] ids = StringUtil.null2String(idstr).split(",");
		List<String> jsonStr = new ArrayList<String>();
		for (int i = 0; i < ids.length; i++) {
			String sql = "select * from Sys_user where loginname = '" + ids[i] + "' order by loginname";
			Sys_user user = daoCtl.detailBySql(dao, Sys_user.class, Sqls.create(sql));
			if (EmptyUtils.isNotEmpty(user)) {
				jsonStr.add("{realname:'" + user.getRealname() + "',loginname:'" + user.getLoginname() + "'}");
			}
		}
		return jsonStr;
	}
}
