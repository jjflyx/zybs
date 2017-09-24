package com.hits.modules.txl;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
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
import com.hits.modules.bean.Txl_group;
import com.hits.modules.sys.bean.Sys_user;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@IocBean
@At("/private/txl_group")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Txl_groupAction extends BaseAction {
	@Inject
	private Dao dao;

	@At
	@Ok("->:/private/txl/txl_groupList.html")
	public void toTxl_group(HttpServletRequest req) {

	}

	@At
	public String list(HttpSession session,@Param("page") int curPage,
			@Param("rows") int pageSize) {
		Sys_user user = (Sys_user)session.getAttribute("userSession");
		Condition cnd = Cnd.wrap("creater='"+user.getLoginname()+"'");
		return daoCtl.listPageJson(dao, Txl_group.class, curPage, pageSize, cnd).toString();
	}

	@At
	@Ok("->:/private/txl/txl_groupAdd.html")
	public void toadd(HttpServletRequest req) {

	}

	@At
	public boolean add(@Param("..") Txl_group txl_group, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		txl_group.setCreater(user.getLoginname());
		return daoCtl.add(dao, txl_group);
	}

	@At
	@Ok("->:/private/txl/txl_groupUpdate.html")
	public void toUpdate(@Param("id")String id,HttpServletRequest req) {

		Object obj = daoCtl.detailById(dao, Txl_group.class, Integer.valueOf(id));
		req.setAttribute("obj", obj);
	}

	@At
	public boolean update(@Param("..") Txl_group txl_group) {
		Txl_group txl = daoCtl.detailById(dao, Txl_group.class, txl_group.getId());
		txl.setBz(txl_group.getBz());
		txl.setName(txl_group.getName());
		return daoCtl.update(dao, txl);
	}

	@At
	public boolean delete(@Param("ids") final String ids,HttpSession session) {
		final ThreadLocal<Boolean> t = new ThreadLocal<Boolean>();
		t.set(false);
		final Sys_user user = (Sys_user) session.getAttribute("userSession");
		Trans.exec(new Atom(){
			public void run(){
				String sql1 = "delete from txl where groupid in ("+ids+")";
				String sql2 = "delete from txl_group where id in ("+ids+") and creater='"+user.getLoginname()+"'";
				dao.execute(Sqls.create(sql1),Sqls.create(sql2));
				t.set(true);
			}
		});
		return t.get();
	}
	
	/**
	 * 根据ID获取JSON字符串
	 */
	@At
	@Ok("raw")
	public String listAll(@Param("id") String id, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		return getJSON(this.dao, id, user).toString();
	}

	/**
	 * 返回JSON字符串
	 */
	private JSONArray getJSON(Dao dao, String id, Sys_user user) {
		Criteria cri = Cnd.cri();
		
		cri.where().and("creater", "=", user.getLoginname());
		cri.getOrderBy().asc("id");
		List<Txl_group> list = this.daoCtl.list(dao, Txl_group.class, cri);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Txl_group res = (Txl_group) list.get(i);
			JSONObject jsonobj = new JSONObject();
			
			
			
			jsonobj.put("id", res.getId());
			jsonobj.put("name", res.getName());
			jsonobj.put("_parentId", "0");
			jsonobj.put("url", "javascript:list(\"" + res.getId() + "\")");
			jsonobj.put("target", "_self");

			
			array.add(jsonobj);
		}
		return array;
	}
}
