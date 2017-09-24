package com.hits.modules.sys.breach;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.hits.common.config.Globals;
import com.hits.modules.gtxt.bean.User_breach;
import com.hits.modules.sys.bean.Sys_resource;
import com.hits.modules.sys.bean.Sys_role_resource;
import com.hits.modules.sys.bean.Sys_unit;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
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
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.StringUtil;
import com.hits.modules.bean.Breach_info;
import com.hits.modules.bean.Cs_value;
import com.hits.modules.com.comUtil;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.DateUtil;
import com.hits.util.EmptyUtils;
import com.hits.util.MyBeanUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 *
 *  #(c) IFlytek gtxt <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: 失信行为
 *
 *  <br/>创建说明: 2016年1月25日 上午11:26:24    创建文件<br/>
 *
 *  修改历史:<br/>
 *
 */
@IocBean
@At("/private/breach")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Breach_Action extends BaseAction{
	@Inject
	protected Dao dao;


	/**
	 * 功能描述:前往失信行为列表页面
	 *
	 * @author   2016年1月25日 上午11:44:05
	 *
	 * @param session
	 * @param req
	 */
	@At
	@Ok("->:/private/sys/breach/breach_list.html")
	public void toBreach(HttpSession session,HttpServletRequest req) {

	}

	/**
	 * 功能描述:异步加载树形table
	 *
	 * @author  2015年10月26日 下午3:33:23
	 *
	 * @param code
	 * @param typeid
	 * @return
	 */
	@At
	@Ok("raw")
	public String ajax_list(@Param("code") String id) {
		Criteria cri = Cnd.cri();
		if (EmptyUtils.isEmpty(id)) {
			cri.where().and("length(id)", "=",4);
		} else {
			cri.where().and("id", "like", id + "____");
		}

		cri.getOrderBy().asc("id").asc("location");
		List<Breach_info> list = daoCtl.list(dao, Breach_info.class, cri);
		JSONArray array = new JSONArray();
		if (list.size()>0) {
			for (int i = 0; i < list.size(); i++) {
				Breach_info breach = list.get(i);
				JSONObject jsonobj = new JSONObject();
				String pid = breach.getId().substring(0,breach.getId().length()-4);

				if ("".equals(pid)){
					pid = "0";
				}
				jsonobj.put("code", breach.getId());
				jsonobj.put("sxxw", EmptyUtils.isNotEmpty(breach.getSxxw()) ? breach.getSxxw() : "");
				jsonobj.put("xylx", comUtil.sxxwtypeMap.get(breach.getXylx()));
				jsonobj.put("sxqx", breach.getSxqx());
				jsonobj.put("cjcs", breach.getCjcs());
				jsonobj.put("yj", breach.getYj());
				jsonobj.put("isuse", breach.getIs_use());
				jsonobj.put("xzcf", breach.getIs_xzcf());
				jsonobj.put("_parentId", pid);

				//查询子集数量
				int num = daoCtl.getRowCount(dao, Breach_info.class,Cnd.where("id", "like", breach.getId() + "____"));
				if (num > 0) {
//					jsonobj.put("children", getJSON(dao, res.getCode(), typeid));
					jsonobj.put("state", "closed");
				}
				array.add(jsonobj);
			}
		}

		return array.toString();
	}


	/**
	 * 功能描述:前往失信行为添加页面
	 *
	 * @author   2016年1月25日 上午11:44:05
	 *
	 * @param session
	 * @param req
	 */
	@At
	@Ok("->:/private/sys/breach/breach_add.html")
	public void toBreachAdd(HttpSession session,HttpServletRequest req) {
		//行为类别
		Sql sql=Sqls.create("select code,value,name from cs_value where typeid='00010005' and state=0 order by location asc,id asc ");
		List<Cs_value> csvalueList= daoCtl.list(dao, Cs_value.class,sql);
		req.setAttribute("csvalueList", csvalueList);

		//所属行为父类
		Sql sql1=Sqls.create("select id,sxxw from breach_info where length(id)=4 order by location asc ");
		List<Breach_info> parentList= daoCtl.list(dao, Breach_info.class,sql1);
		req.setAttribute("parentList", parentList);
		//行政处罚类型
		List<Sys_user> userList = daoCtl.list(dao,Sys_user.class,Sqls.create(" select userid,realname from sys_user where loginname <> 'superadmin'  "));
		req.setAttribute("userList",userList);
	}

	@At
	@Ok("json")
	public JSONObject getHTFL(@Param("id")String id){
		Gson gson = new Gson();
		Map<String,String> map = daoCtl.getHTable(dao, Sqls.create(" select value,name from cs_value where typeid = '"+id+"' "));
		return JSONObject.fromObject(map);
	}
	
	@At
	@Ok("json")
	public JSONObject getSSXW(@Param("id")String id){
		Gson gson = new Gson();
		Map<String,String> map = daoCtl.getHTable(dao, Sqls.create(" select id,sxxw from breach_info where length(id)=4 and xylx = '"+id+"' order by location asc "));
		return JSONObject.fromObject(map);
	}


	/**
	 * 功能描述:新增失信行为
	 *
	 * @author  2016年1月25日 下午2:30:23
	 *
	 * @param tid
	 * @return
	 */
	@At
	@Ok("raw")
	public boolean addBreach(@Param("..") final Breach_info breach,@Param("tid") final String tid,HttpSession session) {
		final Sys_user user = (Sys_user) session.getAttribute("userSession");
		final ThreadLocal<Boolean> result = new ThreadLocal<Boolean>();
		try {
			Trans.exec(new Atom() {
				public void run() {
					breach.setCjr(user.getLoginname());
					breach.setCjsj(DateUtil.getCurDateTime());
					if(EmptyUtils.isNotEmpty(tid)){//添加子类
						Breach_info oldBreach=daoCtl.detailByName(dao, Breach_info.class, tid);
						breach.setXylx(oldBreach.getXylx());
						breach.setId(daoCtl.getSubMenuId(dao, "breach_info", "id", tid));

						Sql sql = Sqls.create("select max(location) from breach_info");
						int location = daoCtl.getIntRowValue(dao, sql);
						breach.setLocation(location+1);
					}else{//新增
						Sql sql = Sqls.create("select max(location) from breach_info where id like  @id");
						sql.params().set("id", breach.getId() + "_%");
						int location = daoCtl.getIntRowValue(dao, sql);
						if (location == 0) {
							sql = Sqls.create("select max(location) from breach_info where id =  @id");
							sql.params().set("id", breach.getId());
							location = daoCtl.getIntRowValue(dao, sql);
						}
						breach.setLocation(location+1);
						breach.setId(daoCtl.getSubMenuId(dao, "breach_info", "id", ""));
					}
					dao.insert(breach);
					comUtil.setTemplate(dao, daoCtl);

					result.set(true);
				}
			});
		} catch (Exception e) {
			result.set(false);
		}
		return result.get();
	}


	/**
	 * 功能描述:前往失信行为修改页面
	 *
	 * @author   2016年1月25日 上午11:44:05
	 *
	 * @param session
	 * @param req
	 */
	@At
	@Ok("->:/private/sys/breach/breach_update.html")
	public void toBreachUpdate(HttpSession session,HttpServletRequest req,@Param("id") String id) {
		//行为类别
		req.setAttribute("sxxwtypeMap", comUtil.sxxwtypeMap);

		//所属行为父类
		Sql sql1=Sqls.create("select id,sxxw from breach_info order by location asc ");
		Map<String,String> sxxwMap= daoCtl.getHTable(dao,sql1);
		req.setAttribute("sxxwMap", sxxwMap);

		Breach_info breach=daoCtl.detailByName(dao, Breach_info.class, id);

		String sxxw="";
		if (EmptyUtils.isNotEmpty(breach)) {
			if(breach.getId().length()>4){
				sxxw=sxxwMap.get(breach.getId().substring(0, (breach.getId().length()-4)));
				req.setAttribute("ischild", false);//是否显示修改失信行为编辑框
			}else{
				sxxw=sxxwMap.get(breach.getId());
				req.setAttribute("ischild", true);
			}

		}
		req.setAttribute("parentSxxw", sxxw);
		req.setAttribute("breach", breach);

		//行政处罚类型
		List<Sys_user> userList = daoCtl.list(dao,Sys_user.class,Sqls.create(" select userid,realname from sys_user where loginname <> 'superadmin'  "));
		req.setAttribute("userList",userList);
		if("00070001".equals(breach.getXylx()) || "00070004".equals(breach.getXylx())){
			String type = "00070001".equals(breach.getXylx()) ? "00020014" : "00020015";
			List<Cs_value> htList = daoCtl.list(dao, Cs_value.class, Sqls.create(" select value,name from cs_value where typeid = '" + type + "' "));
			req.setAttribute("htList",htList);
			req.setAttribute("htfq",1);
		}

	}


	/**
	 * 功能描述:修改失信行为
	 *
	 * @author  2016年1月25日 下午2:30:23
	 *
	 * @return
	 */
	@At
	@Ok("raw")
	public boolean updateBreach(@Param("..") final Breach_info breach,HttpSession session) {

		final ThreadLocal<Boolean> result = new ThreadLocal<Boolean>();
		try {
			final Breach_info oldBreach=daoCtl.detailByName(dao, Breach_info.class, breach.getId());
			MyBeanUtils.copyBeanNotNull2Bean(breach, oldBreach);

			Trans.exec(new Atom() {
				public void run() {
					dao.update(oldBreach);
					result.set(true);
				}
			});
		} catch (Exception e) {
			result.set(false);
		}
		return result.get();
	}

	/**
	 * 功能描述:删除失信行为
	 *
	 * @author 2016年1月25日 下午5:32:42
	 *
	 * @param ids
	 * @return
	 */
	@At
	@Ok("raw")
	public boolean del(@Param("ids") final String ids) {
		try {
			final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
			Trans.exec(new Atom() {
				public void run() {
					System.out.println("ids="+ids);
					boolean result = false;
					String[] id = StringUtil.null2String(ids).split(",");
					String id_s=StringUtil.getStrsplit(id);

					result = daoCtl.exeUpdateBySql(dao,Sqls.create("delete from breach_info where id in "+ id_s));

					if (result) {
						for (int i = 0; i < id.length; i++) {
							result=daoCtl.exeUpdateBySql(dao,Sqls.create("delete from breach_info where id like '"
									+ StringUtil.null2String(id[i]) + "%'"));
							if(!result){
								break;
							}
						}

					}
					re.set(result);
					if(result){
						comUtil.setTemplate(dao, daoCtl);
					}
				}
			});
			return re.get();
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 用户权限分配页面
	 */
	@At
	@Ok("->:/private/sys/breach/userMenu.html")
	public void toUserMenu(@Param("roleid") String roleid, HttpSession session,
						   HttpServletRequest req) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		try {
			Hashtable<String,String> userCountMap = daoCtl.getHTable(dao,Sqls.create(" select unitid,count(userid) from sys_user group by unitid  "));
			Sql htsql = Sqls.create("select userid,id from user_breach where breachid = '" + roleid + "' ");
			Hashtable<String, String> rh = daoCtl.getHTable(dao, htsql);
			List<Sys_unit> list = daoCtl.list(dao, Sys_unit.class, Sqls.create(" select id,name from sys_unit order by id "));
			JSONArray array = new JSONArray();
			JSONObject jsonroot = new JSONObject();
			jsonroot.put("id", "");
			jsonroot.put("pId", "0");
			jsonroot.put("name", "机构列表");
			jsonroot.put("checked", false);
			jsonroot.put("nocheck", true);
			jsonroot.put("open", true);
			jsonroot.put("icon", Globals.APP_BASE_NAME
					+ "/images/icons/icon042a1.gif");
			array.add(jsonroot);
			for (int i = 0; i < list.size(); i++) {
				Sys_unit obj = list.get(i);
				JSONObject jsonobj = new JSONObject();
				jsonobj.put("id", obj.getId());
				jsonobj.put("pId",
						obj.getId().substring(0, obj.getId().length() - 4));
				jsonobj.put("name", obj.getName());
				array.add(jsonobj);
				if(EmptyUtils.isNotEmpty(userCountMap.get(obj.getId())) && Integer.parseInt(userCountMap.get(obj.getId())) > 0) {
					List<Sys_user> userList = daoCtl.list(dao,Sys_user.class,Cnd.where("unitid","=",obj.getId()));
					for(Sys_user sys_user : userList) {
						JSONObject jsonO = new JSONObject();
						jsonO.put("id", sys_user.getUserid());
						jsonO.put("pId", obj.getId());
						jsonO.put("name", "<span style='color:red;'>"+sys_user.getRealname()+"</span>");
						if (rh != null && null != rh.get(String.valueOf(sys_user.getUserid()))) {
							jsonO.put("checked", true);
						} else {
							jsonO.put("checked", false);
						}
						array.add(jsonO);
					}
				}

			}
			req.setAttribute("str", array.toString());
			req.setAttribute("roleid", roleid);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	/**
	 * 权限分配处理
	 */
	@At
	public boolean userMenu(@Param("checkids") String checkids,
							@Param("roleid") final String roleid) {
		final Condition c = Cnd.where("breachid", "=", roleid);
		try {
			if (!"".equals(checkids)) {
				final String[] ids = StringUtil.null2String(checkids).split(";");
				Trans.exec(new Atom() {
					public void run() {
						dao.clear("USER_BREACH", c);
						for (int i = 0; i < ids.length; i++) {
							User_breach user_breach = new User_breach();
							String[] id_Button = ids[i].split(":");
							user_breach.setBreachid(roleid);
							user_breach.setUserid(id_Button[0]);
							user_breach.setLoginname(daoCtl.getStrRowValue(dao,Sqls.create(" select loginname from sys_user where userid = '"+id_Button[0]+"' ")));
							dao.insert(user_breach);
						}
					}
				});
			} else {
				dao.clear("USER_BREACH", c);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return false;
	}
}
