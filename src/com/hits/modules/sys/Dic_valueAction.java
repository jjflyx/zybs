package com.hits.modules.sys;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param; 

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.StringUtil;
import com.hits.modules.sys.bean.Dic_value;

/**
 * @author frigyes
 * @time 2015-06-30 19:29:25
 * 
 */
@IocBean
@At("/private/cform/dic_value")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Dic_valueAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At("")
	@Ok("->:/private/cform/Dic_value.html")
	public void dic_value(HttpSession session,HttpServletRequest req) {
	
	}
	
	@At
	@Ok("->:/private/cform/Dic_valueAdd.html")
	public void toadd() {
	
	}
	
	@At
	@Ok("raw")
	public boolean add(@Param("..") Dic_value dic_value) {
		return daoCtl.add(dao,dic_value);
	}
	
	//@At
	//@Ok("raw")
	//public String add(@Param("..") Dic_value dic_value) {
	//	return daoCtl.addT(dao,dic_value).getId();
	//}
	
	@At
	@Ok("json")
	public Dic_value view(@Param("id") String id) {
		return daoCtl.detailByName(dao,Dic_value.class, id);
	}
	
	@At
	@Ok("->:/private/cform/Dic_valueUpdate.html")
	public void toupdate(@Param("id") String id, HttpServletRequest req) {
		Dic_value dic_value = daoCtl.detailByName(dao, Dic_value.class, id);
		req.setAttribute("obj", dic_value);
	}
	
	@At
	public boolean update(@Param("..") Dic_value dic_value) {
		return daoCtl.update(dao, dic_value);
	}
	
	@At
	public boolean delete(@Param("id") String id) {
		return daoCtl.deleteByName(dao, Dic_value.class, id);
	}
	
	@At
	public boolean deleteIds(@Param("ids") String ids) {
		String[] id = StringUtil.null2String(ids).split(",");
		return daoCtl.deleteByNames(dao, Dic_value.class,  id);
	}
	
	@At
	@Ok("raw")
	public JSONObject list(@Param("page") int curPage, @Param("rows") int pageSize){
		Criteria cri = Cnd.cri();
		cri.where().and("1","=","1");
		cri.getOrderBy().desc("id");
		return daoCtl.listPageJson(dao, Dic_value.class, curPage, pageSize, cri);
	}

}