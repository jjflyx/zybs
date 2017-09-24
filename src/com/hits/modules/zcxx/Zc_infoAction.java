package com.hits.modules.zcxx;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
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
import com.hits.common.util.StringUtil;
import com.hits.modules.com.comUtil;
import com.hits.modules.gtxt.bean.Xyzt_info;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.modules.zcxx.bean.Zc_info;
import com.hits.common.util.DateUtil;
import com.hits.util.EmptyUtils;
import com.hits.util.SysLogUtil;

/**
 * @author L H T
 * @time 2016-03-31 13:55:57
 * 
 */
@IocBean
@At("/private/zcxx")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Zc_infoAction extends BaseAction {
	@Inject
	protected Dao dao;
	public static String url="/private/zcxx/view?id=@id";
	/**
	 * 功能描述:前往职称列表页面
	 *
	 * @author L H T  2016年3月31日 下午2:10:14
	 * 
	 * @param session
	 * @param req
	 */
	@At
	@Ok("->:/private/zcxx/zc_list.html")
	public void to_zclist(HttpSession session,HttpServletRequest req) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		req.setAttribute("loginname", user.getLoginname());
	}

	@At
	@Ok("->:/private/zcxx/queryList.html")
	public void queryList(){}
	
	/**
	 * 功能描述:职称列表数据
	 *
	 * @author L H T  2016年3月31日 下午2:10:29
	 * 
	 * @param session
	 * @param req
	 */
	@At
	@Ok("raw")
	public JSONObject zclist(HttpSession session,HttpServletRequest req,
			@Param("name") String name, @Param("idcard") String idcard,
			@Param("zc_name") String zc_name, @Param("zc_card") String zc_card,
			@Param("starttime") String starttime, @Param("endtime") String endtime,
			@Param("page") int curPage, @Param("rows") int pageSize) {
		JSONObject zclist=new JSONObject();
		try {
			Sql sql=Sqls.create("select * from  zc_info where 1=1 $wheresql");
			
			String wheresql="";
			if (EmptyUtils.isNotEmpty(name)) {
				wheresql +=" and name like'%"+name+"%'";
			}
			if (EmptyUtils.isNotEmpty(idcard)) {
				wheresql +=" and idcard like'%"+idcard+"%'";
			}
			if (EmptyUtils.isNotEmpty(zc_name)) {
				wheresql +=" and zc_name like'%"+zc_name+"%'";
			}
			if (EmptyUtils.isNotEmpty(zc_card)) {
				wheresql +=" and zc_card like'%"+zc_card+"%'";
			}
			
			if (EmptyUtils.isNotEmpty(starttime)) {
				wheresql +=" and zc_time >='"+starttime+"'";
			}
			if (EmptyUtils.isNotEmpty(endtime)) {
				wheresql +=" and zc_time <='"+zc_card+"'";
			}
			wheresql +=" order by create_time desc ";
			
			sql.vars().set("wheresql", wheresql);
			
			zclist=daoCtl.listPageJsonSql(dao, sql, curPage, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zclist;
	}
	
	/**
	 * 功能描述:职称新增页面
	 *
	 * @author L H T  2016年3月31日 下午2:40:46
	 *
	 */
	@At
	@Ok("->:/private/zcxx/zc_add.html")
	public void toadd() {
	
	}
	
	/**
	 * 功能描述:增加职称信息
	 *
	 * @author L H T  2016年3月31日 下午2:55:08
	 * 
	 * @param zc_info
	 * @return
	 */
	@At
	@Ok("raw")
	public boolean add(HttpSession session,@Param("..") final Zc_info zc_info) {
		final ThreadLocal<Boolean> result = new ThreadLocal<Boolean>();
		final Sys_user user = (Sys_user) session.getAttribute("userSession");
		try {
			Trans.exec(new Atom(){
		        public void run(){
		        	zc_info.setCreate_loginname(user.getLoginname());
		        	zc_info.setCreate_time(DateUtil.getCurDateTime());
		        	zc_info.setSjc(DateUtil.str2sjc(DateUtil.getCurDateTime()));  // 时间戳 
		        	dao.insert(zc_info);
					String lognote=SysLogUtil.getLogNote(zc_info, "zcxx");
					SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, true, 1,
							"增加职称信息", "职称信息", lognote,
							"","",url.replace("@id", zc_info.getId()));
		        	result.set(true);
		        }
			});
		} catch (Exception e) {
			e.printStackTrace();
			result.set(false);
		}
		return result.get();
	}
	
	
	/**
	 * 功能描述:前往浏览页面
	 *
	 * @author L H T  2016年3月31日 下午3:17:42
	 * 
	 * @param id
	 * @return
	 */
	@At
	@Ok("->:/private/zcxx/zc_detail.html")
	public Zc_info view(@Param("id") String id) {
		Zc_info master = dao.fetchLinks(dao.fetch(Zc_info.class, id), "zczxlist");
		return master;
	}
	
	/**
	 * 功能描述:前往修改页面
	 *
	 * @author L H T  2016年3月31日 下午3:31:35
	 * 
	 * @param id
	 * @param req
	 */
	@At
	@Ok("->:/private/zcxx/zc_update.html")
	public void toupdate(@Param("id") String id, HttpServletRequest req) {
		Zc_info zc_info = daoCtl.detailByName(dao, Zc_info.class, id);
		
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
	public boolean update(HttpSession session,@Param("..") final Zc_info zc_info) {
		final ThreadLocal<Boolean> result = new ThreadLocal<Boolean>();
		final Sys_user user = (Sys_user) session.getAttribute("userSession");
		try {
			Trans.exec(new Atom(){
		        public void run(){
		        	Zc_info oldzc_info=daoCtl.detailByName(dao, Zc_info.class, zc_info.getId());
		        	zc_info.setSjc(DateUtil.str2sjc(DateUtil.getCurDateTime()));  // 时间戳 
		        	dao.update(zc_info);
		        	String note=SysLogUtil.getLogNote(oldzc_info,zc_info,"zcxx");
		        	SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type,true,2,
		    				"修改职称信息【"+zc_info.getName()+"】","信用主体",note,"","",url.replace("@id", zc_info.getId()+""));
		        	result.set(true);
		        }
			});
		} catch (Exception e) {
			e.printStackTrace();
			result.set(false);
		}
		return result.get();
	}
	
	/**
	 * 功能描述:删除职称
	 *
	 * @author L H T  2016年3月31日 下午3:39:50
	 * 
	 * @param ids
	 * @return
	 */
	@At
	public boolean deleteIds(@Param("ids") final String ids) {
		final ThreadLocal<Boolean> result = new ThreadLocal<Boolean>();
		try {
			Trans.exec(new Atom(){
		        public void run(){
		        	String[] id = StringUtil.null2String(ids).split(",");
		        	for (int i = 0; i < id.length; i++) {
		    			dao.delete(Zc_info.class, id[i]);
		    		}
		        	result.set(true);
		        }
			});
		} catch (Exception e) {
			e.printStackTrace();
			result.set(false);
		}
		return result.get();
	}
	
	@At
	@Ok("raw")
	public boolean ajaxname(@Param("zc_card") String zc_card) {
		int res=daoCtl.getRowCount(dao, Zc_info.class, Cnd.where("zc_card","=",zc_card));
		if(res>0){
			return true;
		}
		return false;
	}

}