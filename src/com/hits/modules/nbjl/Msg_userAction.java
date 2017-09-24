package com.hits.modules.nbjl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
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
import com.hits.modules.bean.Msg_user;

/**
 * @author 
 * @time 2014-05-06 13:33:35
 * 
 */
@IocBean
@At("/private/msg/msg_user")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Msg_userAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At("")
	@Ok("->:/private/msg/Msg_user.html")
	public void msg_user(HttpSession session,HttpServletRequest req) {
	
	}
	
	@At
	@Ok("->:/private/msg/Msg_userAdd.html")
	public void toadd() {
	
	}
	
	@At
	@Ok("raw")
	public boolean add(@Param("..") Msg_user msg_user) {
		return daoCtl.add(dao,msg_user);
	}
	
	//@At
	//@Ok("raw")
	//public int add(@Param("..") Msg_user msg_user) {
	//	return daoCtl.addT(dao,msg_user).getId();
	//}
	
	@At
	@Ok("json")
	public Msg_user view(@Param("id") int id) {
		return daoCtl.detailById(dao,Msg_user.class, id);
	}
	
	@At
	@Ok("->:/private/msg/Msg_userUpdate.html")
	public void toupdate(@Param("id") int id, HttpServletRequest req) {
		Msg_user msg_user = daoCtl.detailById(dao, Msg_user.class, id);
		req.setAttribute("obj", msg_user);
	}
	
	@At
	public boolean update(@Param("..") Msg_user msg_user) {
		return daoCtl.update(dao, msg_user);
	}
	
	@At
	public boolean delete(@Param("id") int id) {
		return daoCtl.deleteById(dao, Msg_user.class, id);
	}
	
	@At
	public boolean deleteIds(@Param("ids") String ids) {
		String[] id = StringUtil.null2String(ids).split(",");
		return daoCtl.deleteByIds(dao, Msg_user.class, id);
	}
	
	@At
	@Ok("raw")
	public JSONObject list(@Param("page") int curPage, @Param("rows") int pageSize){
		Criteria cri = Cnd.cri();
		cri.where().and("1","=","1");
		cri.getOrderBy().desc("ftime");
		return daoCtl.listPageJson(dao, Msg_user.class, curPage, pageSize, cri);
	}

}