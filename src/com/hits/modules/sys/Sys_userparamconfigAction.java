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
import com.hits.modules.com.comUtil;
import com.hits.modules.sys.bean.Sys_userparamconfig;

/**
 * @author wanfly
 * @time 2014-02-26 13:50:16
 * 
 */
@IocBean
@At("/private/sys/sys_userparamconfig")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Sys_userparamconfigAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At("")
	@Ok("->:/private/sys/userparamconfig.html")
	public void index(HttpSession session,HttpServletRequest req) {
	
	}
	
	@At
	@Ok("->:/private/sys/userparamconfigadd.html")
	public void toadd() {
	
	}
	
	@At
	@Ok("raw")
	public boolean add(@Param("..") Sys_userparamconfig sys_userparamconfig) {
		boolean bol=false;
		try{
			bol=daoCtl.add(dao,sys_userparamconfig);
			if(bol){
				comUtil.setParamConfig(dao, daoCtl);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bol;
	}
	
	//@At
	//@Ok("raw")
	//public int add(@Param("..") Sys_userparamconfig sys_userparamconfig) {
	//	return daoCtl.addT(dao,sys_userparamconfig).getId();
	//}
	
	@At
	@Ok("json")
	public Sys_userparamconfig view(@Param("id") int id) {
		return daoCtl.detailById(dao,Sys_userparamconfig.class, id);
	}
	
	@At
	@Ok("->:/private/sys/userparamconfigUpdate.html")
	public Sys_userparamconfig toupdate(@Param("id") int id, HttpServletRequest req) {
		return daoCtl.detailById(dao, Sys_userparamconfig.class, id);//html:obj
	}
	
	@At
	public boolean update(@Param("..") Sys_userparamconfig sys_userparamconfig) {
		boolean bol=false;
		try{
			bol=daoCtl.update(dao,sys_userparamconfig);
			if(bol){
				comUtil.setParamConfig(dao, daoCtl);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bol;
	}
	
	@At
	public boolean delete(@Param("id") String ids) {
		boolean bol=false;
		try{
			String[] id = StringUtil.null2String(ids).split(",");
			System.out.println("id="+ids);
			bol=daoCtl.deleteByIds(dao, Sys_userparamconfig.class, id);
			if(bol){
				comUtil.setParamConfig(dao, daoCtl);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bol;
	}
	
	@At
	public boolean deleteIds(@Param("ids") String ids) {
		boolean bol=false;
		try{
			String[] id = StringUtil.null2String(ids).split(",");
			System.out.println("id="+ids);
			bol=daoCtl.deleteByIds(dao, Sys_userparamconfig.class, id);
			if(bol){
				comUtil.setParamConfig(dao, daoCtl);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return bol;
	}
	
	@At
	@Ok("raw")
	public JSONObject list(@Param("page") int curPage, @Param("rows") int pageSize){
		Criteria cri = Cnd.cri();
		cri.where().and("1","=",1);
		cri.getOrderBy().asc("id");
		return daoCtl.listPageJson(dao, Sys_userparamconfig.class, curPage, pageSize, cri);
	}
	@At
	public boolean checktypename(@Param("typename") String typename){
		
		if(typename!=null && !typename.equals("")){
			Sys_userparamconfig ts=daoCtl.detailByName(dao, Sys_userparamconfig.class, "typename", typename);
			//System.out.println("typename="+ts);
			if(ts!=null && ts.getTypename().equals(typename)){
				System.out.println("true");
				return true;
			}
		}
		return false;
	}
}