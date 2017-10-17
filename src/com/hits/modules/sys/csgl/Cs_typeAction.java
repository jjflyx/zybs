package com.hits.modules.sys.csgl;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
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
import com.hits.common.util.StringUtil;
import com.hits.modules.bean.Cs_type;
import com.hits.modules.sys.bean.Sys_role_resource;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.SysLogUtil;

/**
 * @author lxy
 * @time 2015-05-22 14:58:17
 * 
 */
@IocBean
@At("/private/cs/type")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Cs_typeAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At("")
	@Ok("->:/private/sys/csgl/Cs_type.html")
	public void cs_type(HttpSession session,HttpServletRequest req) {
		Sql sql = Sqls.create("select id,name from cs_type order by location asc,id asc");
		req.setAttribute("list",daoCtl.getMulRowValue(dao, sql));

		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String[] mp = StringUtil.null2String(user.getBtnmap().get("/private/cs/type")).split(";");
		req.setAttribute("btnmap", mp);

		List<Sys_role_resource> reslist = daoCtl.list(dao,Sys_role_resource.class, Cnd.wrap("resourceid = '000100020001'"));
		HashSet<String> set = new HashSet<String>();
		for(Sys_role_resource resource:reslist){
			if(user.getRolelist().contains(resource.getRoleid()+"")){
				String button = resource.getButton();
				if(!"".equals(button)&&button!=null){
					String[] buttons =button.split(",");
					for(int i=0;i<buttons.length;i++){
						set.add(buttons[i]);
					}
				}
			}
		}
		req.setAttribute("buttonset", set);
	}
	
	@At
	@Ok("->:/private/sys/csgl/Cs_typeAdd.html")
	public void toadd(HttpSession session,HttpServletRequest req,@Param("sjyfl") String sjyfl) {
		Sql sql = Sqls.create("select id,name from cs_type order by location asc,id asc");
		req.setAttribute("list", daoCtl.getMulRowValue(dao, sql));
		req.setAttribute("sjyfl", sjyfl);
	}
	
	@At
	@Ok("raw")
	public boolean add(@Param("..") Cs_type cs_type) {
		boolean result=false;
		try {
			cs_type.setId(daoCtl.getSubMenuId(dao, "cs_type", "id", cs_type.getId()));
			result=daoCtl.add(dao,cs_type);
			SysLogUtil.addLog(dao, "",SysLogUtil.user_log_type, "增加"+result);
		} catch (Exception e) {
			SysLogUtil.addLog(dao, "",SysLogUtil.yx_log_type, e.getMessage());
		}
		return result;
	}
	
	@At
	@Ok("->:/private/sys/csgl/Cs_typeUpdate.html")
	public void toupdate(@Param("id") String id, HttpServletRequest req) {
		Cs_type cs_type = daoCtl.detailByName(dao, Cs_type.class, id);
		req.setAttribute("obj", cs_type);
	}
	
	@At
	public boolean update(@Param("..") Cs_type cs_type) {
		boolean rs=false;
		try {
			rs=daoCtl.update(dao, cs_type);
			SysLogUtil.addLog(dao, "",SysLogUtil.user_log_type, "修改"+rs);
		} catch (Exception e) {
			SysLogUtil.addLog(dao, "",SysLogUtil.yx_log_type, e.getMessage());
		}
		return rs;
	}
	
	/**
	 * 删除
	 * */
	@At
	@Ok("raw")
	public boolean del(@Param("id") String ids) {
		boolean result = false;
		try {
			String[] id = StringUtil.null2String(ids).split(",");
			result = daoCtl.deleteByNames(dao, Cs_type.class, id);
			if (result) {
				for (int i = 0; i < id.length; i++) {
					daoCtl.exeUpdateBySql(
							dao,
							Sqls.create("delete from Cs_type where id like '"
									+ StringUtil.null2String(id[i]) + "%'"));
					daoCtl.exeUpdateBySql(
							dao,
							Sqls.create("delete from Cs_value where type like '"
									+ StringUtil.null2String(id[i]) + "%'"));
				}
			}
			SysLogUtil.addLog(dao, "",SysLogUtil.user_log_type, "删除"+result);
		} catch (Exception e) {
			SysLogUtil.addLog(dao, "",SysLogUtil.yx_log_type, e.getMessage());
		}
		return result;
	}
	
	@At
	@Ok("raw")
	public String list(HttpServletRequest req, HttpSession session, @Param("sjyfl") String sjyfl){
		return getJSON(dao,sjyfl).toString();
	}

	private JSONArray getJSON(Dao dao, String id) {
		Criteria cri = Cnd.cri();
		if (null == id || "".equals(id)) {
			cri.where().and("id", "like", "____");
		} else {
			cri.where().and("id", "like", id + "____");
		}
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		List<Cs_type> list = daoCtl.list(dao, Cs_type.class, cri);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Cs_type obj = list.get(i);
			JSONObject jsonobj = new JSONObject();
			String pid = obj.getId().substring(0, obj.getId().length() - 4);
			if (i == 0 || "".equals(pid))
				pid = "0";
			int num = daoCtl.getRowCount(dao, Cs_type.class,
					Cnd.where("id", "like", obj.getId() + "____"));
			jsonobj.put("id", obj.getId());
			jsonobj.put("code", obj.getId());
			jsonobj.put("name", obj.getName());
			if (num > 0) {
				jsonobj.put("children", getJSON(dao, obj.getId()));
			}
			array.add(jsonobj);
		}
		return array;
	}
	/**
	 * 转到排序页面
	 * */
	@At
	@Ok("->:/private/sys/csgl/Cs_typeSort.html")
	public void toSort(HttpServletRequest req) throws Exception {
		JSONArray array = new JSONArray();
		Criteria cri = Cnd.cri();
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		List<Cs_type> list = daoCtl.list(dao, Cs_type.class, cri);
		JSONObject jsonroot = new JSONObject();
		jsonroot.put("id", "");
		jsonroot.put("pId", "0");
		jsonroot.put("name", "排序列表");
		jsonroot.put("open", true);
		jsonroot.put("childOuter", false);
		jsonroot.put("icon", Globals.APP_BASE_NAME
				+ "/images/icons/icon042a1.gif");
		array.add(jsonroot);
		for (int i = 0; i < list.size(); i++) {
			JSONObject jsonobj = new JSONObject();
			Cs_type obj = list.get(i);
			jsonobj.put("id", obj.getId());
			String p = obj.getId().substring(0, obj.getId().length() - 4);
			jsonobj.put("pId", "".equals(p) ? "0" : p);
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
		boolean rs=false;
		try {
			rs = daoCtl.updateSortRow(dao, "Cs_type", checkids,"location", 0);
			SysLogUtil.addLog(dao, "",SysLogUtil.user_log_type, "排序"+rs);
		} catch (Exception e) {
			SysLogUtil.addLog(dao, "",SysLogUtil.yx_log_type, e.getMessage());
		}
		return rs;

	}
	/**异步查询项目参数json
	 *
	 * @author lht  2015年10月11日 下午8:40:50
	 * 
	 * @param typeid 配置所属分类id
	 * @param csvalue 代码值
	 * @return
	 */
	@At
	@Ok("json")
	public List<Cs_type> getAjaxCsType() {
		String sql="select * from cs_type where length(id)>4 order by location asc,id asc";
		List<Cs_type> list=daoCtl.list(dao, Cs_type.class, Sqls.create(sql));
		return list;
	}
}