package com.hits.modules.sys;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import com.hits.common.filter.*;
import com.hits.common.util.StringUtil;

import com.hits.common.action.BaseAction;
import com.hits.common.config.Globals;
import com.hits.modules.sys.bean.Sys_resource;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 * 
 */
@IocBean
@At("/private/sys/res")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class ResourceAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At("")
	@Ok("->:/private/sys/resource.html")
	public void user(HttpSession session, HttpServletRequest req) {
		Sql sql = Sqls.create("select subtype,name from sys_resource where id like '____' order by id");
		List<Map> list = daoCtl.list(dao, sql);
		req.setAttribute("subtypeList", list);
	}

	@At
	@Ok("json")
	public List<Sys_resource> list(@Param("subtype") int subtype) {
		Criteria cri = Cnd.cri();
		cri.where().and("subtype", "=", subtype);
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		return daoCtl.list(dao, Sys_resource.class, cri);
	}

	/**
	 * 查询全部
	 * */
	@At
	@Ok("raw")
	public String listAll(@Param("id") String id, @Param("subtype") int subtype) {
		return getJSON(dao, id, subtype).toString();
	}
	
	private JSONArray getJSON(Dao dao, String id, int subtype) {
		Criteria cri = Cnd.cri();
		if (null == id || "".equals(id)) {
			cri.where().and("id", "like", "____");
		} else {
			cri.where().and("id", "like", id + "____");
		}
		if (subtype >= 0) {
			cri.where().and("subtype", "=", subtype);
		}
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		List<Sys_resource> list = daoCtl.list(dao, Sys_resource.class, cri);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Sys_resource res = list.get(i);
			JSONObject jsonobj = new JSONObject();
			String pid = res.getId().substring(0, res.getId().length() - 4);
			if (i == 0 || "".equals(pid))
				pid = "0";
			int num = daoCtl.getRowCount(dao, Sys_resource.class,
					Cnd.where("id", "like", res.getId() + "____"));
			jsonobj.put("id", res.getId());
			jsonobj.put("name", res.getName());
			if(res.getState()==0){
				jsonobj.put("qyzt", "启用");
			}
			else {
				jsonobj.put("qyzt", "禁用");
			}
			jsonobj.put("descript", res.getDescript());
			jsonobj.put("url", res.getUrl());
			jsonobj.put("_parentId", pid);
			String bts = StringUtil.null2String(res.getButton());
			String[] bt;
			String temp = "";
			if (!"".equals(bts)) {
				bt = bts.split(";");
				for (int j = 0; j < bt.length; j++)
					temp += bt[j].substring(0, bt[j].indexOf("/")) + ";";
			}
			jsonobj.put("bts", temp);
			if (num > 0) {
				jsonobj.put("children", getJSON(dao, res.getId(), subtype));
			}
			array.add(jsonobj);
		}
		return array;
	}

	/***
	 * 修改前查找
	 * */
	@At
	@Ok("->:/private/sys/resourceUpdate.html")
	public void toupdate(@Param("id") String id, HttpServletRequest req) {
		Sys_resource res = daoCtl.detailByName(dao, Sys_resource.class, id);
		req.setAttribute("obj", res);
	}

	/****
	 * 修改
	 * */
	@At
	@Ok("raw")
	public String updateSave(@Param("..") Sys_resource res,
			@Param("button2") String button2, HttpServletRequest req) {

		res.setButton(button2);
		return daoCtl.update(dao, res) == true ? res.getId() : "";
	}

	/****
	 * 新建菜单，查找单位。
	 * */
	@At
	@Ok("->:/private/sys/resourceAdd.html")
	public void toAdd(HttpServletRequest req) {
		Sql sql = Sqls.create("select subtype,name from sys_resource where id like '____' order by id");
		List<Map> list = daoCtl.list(dao, sql);
		req.setAttribute("subtypeList", list);
	}

	/***
	 * 新建资源
	 * */
	@At
	@Ok("raw")
	public boolean addSave(@Param("..") Sys_resource res,
			@Param("button2") String button2) {

		Sql sql = Sqls
				.create("select max(location) from Sys_resource where id like  @id");
		sql.params().set("id", res.getId() + "_%");
		int location = daoCtl.getIntRowValue(dao, sql);
		if (location == 0) {
			sql = Sqls
					.create("select max(location) from Sys_resource where id =  @id");
			sql.params().set("id", res.getId());
			location = daoCtl.getIntRowValue(dao, sql);
		}
		res.setLocation(location);
		res.setId(daoCtl.getSubMenuId(dao, "Sys_resource", "id", StringUtil.null2String(res.getId())));
		res.setButton(button2);
		if(res.getSubtype()==-1){
			int subtype = Integer.valueOf(res.getId().substring(0, 4))-1;
			res.setSubtype(subtype);
		}
		return daoCtl.add(dao, res);
	}

	/**
	 * 删除
	 * */
	@At
	@Ok("raw")
	public boolean del(@Param("id") String ids) {
		boolean result = false;
		String[] id = StringUtil.null2String(ids).split(",");
		result = daoCtl.deleteByNames(dao, Sys_resource.class, id);
		if (result) {
			for (int i = 0; i < id.length; i++) {
				daoCtl.exeUpdateBySql(
						dao,
						Sqls.create("delete from Sys_resource where id like '"
								+ StringUtil.null2String(id[i]) + "%'"));
				daoCtl.exeUpdateBySql(
						dao,
						Sqls.create("delete from Sys_role_resource where resourceid like '"
								+ StringUtil.null2String(id[i]) + "%'"));
			}
		}
		return result;
	}

	/**
	 * 转到排序页面
	 * */
	@At
	@Ok("->:/private/sys/resourceSort.html")
	public void toSort(HttpServletRequest req) throws Exception {
		JSONArray array = new JSONArray();
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		List<Sys_resource> list = daoCtl.list(dao, Sys_resource.class, cri);
		JSONObject jsonroot = new JSONObject();
		jsonroot.put("id", "");
		jsonroot.put("pId", "0");
		jsonroot.put("name", "资源列表");
		jsonroot.put("open", true);
		jsonroot.put("childOuter", false);
		jsonroot.put("icon", Globals.APP_BASE_NAME
				+ "/images/icons/icon042a1.gif");
		array.add(jsonroot);
		for (int i = 0; i < list.size(); i++) {
			JSONObject jsonobj = new JSONObject();
			Sys_resource obj = list.get(i);
			jsonobj.put("id", obj.getId());
			String p = obj.getId().substring(0, obj.getId().length() - 4);
			jsonobj.put("pId", p == "" ? "0" : p);
			String name = obj.getName();
			jsonobj.put("name", name);
			jsonobj.put("childOuter", false);
			if (obj.getId().length() < 12) {
				jsonobj.put("open", true);
			} else {
				jsonobj.put("open", false);
			}
			array.add(jsonobj);
		}
		req.setAttribute("str", array.toString());
	}

	/***
	 * 确认排序
	 * */
	@At
	@Ok("raw")
	public boolean sort(@Param("checkids") String[] checkids) {
		boolean rs = daoCtl.updateSortRow(dao, "Sys_resource", checkids,
				"location", 0);
		return rs;

	}

}
