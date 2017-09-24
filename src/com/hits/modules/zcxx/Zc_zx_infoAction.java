package com.hits.modules.zcxx;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
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
import com.hits.modules.sys.bean.Sys_user;
import com.hits.modules.zcxx.bean.Zc_info;
import com.hits.modules.zcxx.bean.Zc_zx_info;
import com.hits.util.DateUtil;

/**
 * @author L H T
 * @time 2016-03-31 13:55:57
 * 
 */
@IocBean
@At("/private/zcxx/zx")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Zc_zx_infoAction extends BaseAction {
	@Inject
	protected Dao dao;

	/**
	 * 功能描述:前往注销页面
	 *
	 * @author L H T  2016年3月31日 下午3:31:35
	 * 
	 * @param id
	 * @param req
	 */
	@At
	@Ok("->:/private/zcxx/zc_cancel.html")
	public void tocancel(@Param("id") String id, HttpServletRequest req) {
//		Zc_info zc_info = daoCtl.detailByName(dao, Zc_info.class, id);
		Zc_info zc_info = dao.fetchLinks(dao.fetch(Zc_info.class, id), "zczxlist");
		req.setAttribute("obj", zc_info);
	}
	
	/**
	 * 功能描述:保存修改
	 *
	 * @author L H T  2016年3月31日 下午3:34:54
	 * 
	 * @param zc_info
	 * @return
	 */
	@At
	public boolean cancel(HttpSession session,@Param("..") final Zc_zx_info zx_info) {
		final ThreadLocal<Boolean> result = new ThreadLocal<Boolean>();
		final Sys_user user = (Sys_user) session.getAttribute("userSession");
		try {
			Trans.exec(new Atom(){
		        public void run(){
		        	Zc_info zcinfo=daoCtl.detailByName(dao, Zc_info.class, zx_info.getZc_id());
		        	zcinfo.setZc_state(1);//注销状态
		        	dao.update(zcinfo);
		        	
		        	zx_info.setZx_loginname(user.getLoginname());
		        	zx_info.setZx_time(DateUtil.getCurDateTime());
		        	dao.insert(zx_info);
		        	result.set(true);
		        }
			});
		} catch (Exception e) {
			e.printStackTrace();
			result.set(false);
		}
		return result.get();
	}

}