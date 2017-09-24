package com.hits.modules.gtxt;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hits.util.EmptyUtils;
import net.sf.json.JSONObject;

import org.nutz.dao.*;
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
import com.hits.modules.gtxt.bean.Early_warning_info;
import com.hits.modules.sys.bean.Sys_user;

/**
 * @author Numbgui
 * @time 2016-03-11 13:17:18
 *
 */
@IocBean
@At("/private/gtxt/warn_info")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Early_warning_infoAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At("")
	@Ok("->:/private/ratifyFollow/earlyWarning.html")
	public void early_warning_info(@Param("xy_type")String xy_type,HttpSession session,HttpServletRequest req) {
		req.setAttribute("xy_type",xy_type);
	}

	@At
	@Ok("raw")
	public JSONObject list(HttpSession session,@Param("type")Integer type,@Param("xy_type")String xy_type,@Param("keyword")String keyword,@Param("page") int curPage, @Param("rows") int pageSize){
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		Criteria cri = Cnd.cri();
		cri.where().and("state","=",0);
		if(EmptyUtils.isNotEmpty(keyword)){
			cri.where().andLike("xyzt_name", keyword);
		}
		switch (type){
			case 1 :
				cri.where().and("day",">=",21);
				break;
			case 2 :
				cri.where().and("day",">",10).and("day","<=",20);
				break;
			case 3 :
				cri.where().and("day",">",0).and("day","<=",10);
				break;
			default:
				cri.where().and("day",">",-5);
				break;
		}
		if(EmptyUtils.isNotEmpty(xy_type)){
			cri.where().and("xy_type","=",xy_type);
		}
		cri.where().and(Cnd.exps("loginname", "=", user.getLoginname()).or("loginname", "is", null));
		cri.where().and("unitid","=",user.getUnitid());
		cri.getOrderBy().asc("day").desc("create_time");
		System.out.println(cri.toString());
		return daoCtl.listPageJson(dao, Early_warning_info.class, curPage, pageSize, cri);
	}

}