package com.hits.modules.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
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
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.DateUtil;
import com.hits.common.util.StringUtil;
import com.hits.modules.bean.File_info;
import com.hits.modules.com.comUtil;
import com.hits.modules.order.bean.OrderBean;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;
import com.hits.util.SysLogUtil;

/**
 * @author JJF E-mail:
 * @version 创建时间：2017年8月8日 上午7:55:26
 * 类说明
 */
@IocBean
@At("/private/order")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class OrderAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/order/orderList.html")
	public void order(HttpSession session, HttpServletRequest req) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String unitid = user.getUnitid();
		req.setAttribute("unitid", unitid);
	}
	
	//订单页面
	@At
	@Ok("raw")
	public JSONObject orderList(HttpServletRequest req,@Param("unitid") String unitid,
			@Param("name") String name,@Param("page") int curPage, @Param("rows") int pageSize){
		Criteria cri = Cnd.cri();
		if(EmptyUtils.isNotEmpty(unitid)){
			cri.where().and("unitid","like",unitid+"%");
		}
		return daoCtl.listPageJson(dao, OrderBean.class, curPage, pageSize,cri);
	}
	
	//新增订单页面
	@At
	@Ok("->:/private/order/orderAdd.html")
	public void toAdd(HttpServletRequest req,HttpSession session) {
		req.setAttribute("isfhMap", comUtil.isfhMap);
		req.setAttribute("today", DateUtil.getToday());
		Sql sql=Sqls.create("select id,name from sys_unit where unittype=88");
		List<Map> unitMap = new ArrayList<Map>();
		unitMap=daoCtl.list(dao, sql);
		req.setAttribute("unitMap", unitMap);
	}
	
	//新增订单
	@At
	@Ok("raw")
	public boolean addSave(final HttpServletRequest req,HttpSession session,@Param("..") final OrderBean order,@Param("filepath") final String path,
			@Param("filename") final String name,@Param("filesize") final String filesize){
		final Sys_user user= (Sys_user)session.getAttribute("userSession");
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		try {
			re.set(false);
			Trans.exec(new Atom() {
				public void run() {
					order.setActor(user.getLoginname());
					order.setXzqh_unit(daoCtl.detailByName(dao, Sys_unit.class, order.getUnitid()).getXzqh());
					OrderBean newOrder = dao.insert(order);
					//附件
					/*String[] paths=path.split(";");
					String[] names=name.split(";");
					String[] sizes=filesize.split(";");
					for(int i=0;i<paths.length;i++){
						File_info att = new File_info();
						if(EmptyUtils.isNotEmpty(paths[i])&&EmptyUtils.isNotEmpty(names[i])){
							att.setFilename(names[i]);
							att.setFilepath(paths[i]);
							att.setFieldname("fj");
							att.setCreate_time(DateUtil.getCurDateTime());
							att.setFilesize(sizes[i]);
							dao.insert(att);
						}
					}*/
					if(EmptyUtils.isNotEmpty(newOrder)){
						re.set(true);
					}else{
						re.set(false);
					}
				}
			});
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return re.get();
	}
	
	/**
	 * 获取单位下负责人的信息
	 * @param loginname
	 * @return
	 */
	@At
	@Ok("raw")
	public String getUnit(@Param("unitid") String unitid) {
		if(EmptyUtils.isNotEmpty(unitid)){
			Sql sql=Sqls.create("select * from sys_unit where id = "+unitid);
			//List<Map> unitMap = new ArrayList<Map>();
			Sys_unit unit = daoCtl.detailByName(dao, Sys_unit.class, unitid);
			String fzrxx = unit.getHandler()+";"+unit.getHandlerphone();
			return fzrxx;
		}else{
			return null;
		}
		
	}
}

