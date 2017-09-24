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
import com.hits.modules.bean.Msg_fj;

/**
 * @author 
 * @time 2014-05-06 13:33:35
 * 
 */
@IocBean
@At("/private/msg/msg_fj")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Msg_fjAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At("")
	@Ok("->:/private/xxx/Msg_fj.html")
	public void msg_fj(HttpSession session,HttpServletRequest req) {
	
	}
	
	@At
	@Ok("->:/private/msg/Msg_fjAdd.html")
	public void toadd() {
	
	}
	
	@At
	@Ok("raw")
	public boolean add(@Param("..") Msg_fj msg_fj) {
		return daoCtl.add(dao,msg_fj);
	}
	
	//@At
	//@Ok("raw")
	//public int add(@Param("..") Msg_fj msg_fj) {
	//	return daoCtl.addT(dao,msg_fj).getId();
	//}
	
	@At
	@Ok("json")
	public Msg_fj view(@Param("id") int id) {
		return daoCtl.detailById(dao,Msg_fj.class, id);
	}
	
	@At
	@Ok("->:/private/msg/Msg_fjUpdate.html")
	public void toupdate(@Param("id") int id, HttpServletRequest req) {
		Msg_fj msg_fj = daoCtl.detailById(dao, Msg_fj.class, id);
		req.setAttribute("obj", msg_fj);
	}
	
	@At
	public boolean update(@Param("..") Msg_fj msg_fj) {
		return daoCtl.update(dao, msg_fj);
	}
	
	@At
	public boolean delete(@Param("id") int id) {
		return daoCtl.deleteById(dao, Msg_fj.class, id);
	}
	
	@At
	public boolean deleteIds(@Param("ids") String ids) {
		String[] id = StringUtil.null2String(ids).split(",");
		return daoCtl.deleteByIds(dao, Msg_fj.class, id);
	}
	
	@At
	@Ok("raw")
	public JSONObject list(@Param("page") int curPage, @Param("rows") int pageSize){
		Criteria cri = Cnd.cri();
		cri.where().and("1","=","1");
		cri.getOrderBy().desc("id");
		return daoCtl.listPageJson(dao, Msg_fj.class, curPage, pageSize, cri);
	}

}