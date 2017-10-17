package com.hits.modules.sys;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
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

import com.google.gson.Gson;
import com.hits.common.action.BaseAction;
import com.hits.common.config.Globals;
import com.hits.common.util.DecodeUtil;
import com.hits.common.util.PinyinUtil;
import com.hits.common.util.StringUtil;
import com.hits.common.filter.*;
import com.hits.modules.bean.Cs_value;
import com.hits.modules.com.comUtil;
import com.hits.modules.sys.bean.Cms_user_style;
import com.hits.modules.sys.bean.Sys_resource;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.DateUtil;
import com.hits.util.Decode64Util;
import com.hits.util.EmptyUtils;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 * 
 */
@IocBean
@At("/private")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class IndexAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At
	@Ok("raw")
	public String yzPassword(HttpSession session){
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String pwd=DecodeUtil.Decrypt(user.getPassword());
		return pwd;
	}
	
	@At
	@Ok("->:/private/lock.html")
	public void dolock(HttpServletRequest req, HttpSession session) {
		/*session.setAttribute("validate", "openLockWindow();");
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		user.setValidate(true);
		session.setAttribute("userSession", user);*/
	}

	@At
	@Ok("->:/private/lock.html")
	public void lock(HttpServletRequest req, HttpSession session) {

	}
	
	@At
	@Ok("->:/private/桌面统计.html")
	public void zmtj(HttpServletRequest req, HttpSession session,@Param("xzqh") String xzqh) {
		String sql = "";
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		try {
			//基本信息
			sql = "select count(1) from xyzt_info where 1=1";
			req.setAttribute("count1", daoCtl.getIntRowValue(dao, Sqls.create(sql)));
			// 红榜
			sql = "select count(1) from reward_info a,xyzt_info b where a.xyzt_id = b.id and a.type = 0";
			req.setAttribute("count2", daoCtl.getIntRowValue(dao, Sqls.create(sql )));
			// 黑榜
			sql = "select count(1) from discredit_info a,xyzt_info b,breach_info c where a.xyzt_id = b.id and a.sxxw_id = c.id and a.type ='0' ";
			req.setAttribute("count3", daoCtl.getIntRowValue(dao, Sqls.create(sql )));
			
			//失信惩戒柱状图
			Gson gson = new Gson();
			xzqh = EmptyUtils.isEmpty(xzqh)?"340000" : xzqh;
			
			List<Sys_unit> unitList = null;
			//如果是340000表示是省级的，省级需要包含行业协会信息
			if("340000".equals(xzqh)){
				unitList =daoCtl.list(dao, Sys_unit.class, Sqls.create("select id,name from sys_unit where unittype = 90 order by id"));
				req.setAttribute("unitList",unitList);
				req.setAttribute("unitGson",gson.toJson(unitList));
			}
			req.setAttribute("xzqhObj",daoCtl.detailByCnd(dao,Cs_value.class, Cnd.where("typeid","=","00010004").and("state","=",0).and("value","=",xzqh)));
			if(xzqh.substring(2,xzqh.length()).equals("0000")){
				xzqh = xzqh.substring(0,2)+"__00";
			}else if(xzqh.substring(4,xzqh.length()).equals("00")){
				xzqh = xzqh.substring(0,4)+"__";
			}

			//得到列，行政区划
			List<Cs_value> xzqhList = daoCtl.list(dao,Cs_value.class,Sqls.create(" select value,name from cs_value where typeid='00010004' and state=0 and value like '"+xzqh+"'   order by location asc,id asc "));
			req.setAttribute("xzqhList",xzqhList);
			req.setAttribute("xzqhGson",gson.toJson(xzqhList));
			
			List<Cs_value> xyList = daoCtl.list(dao,Cs_value.class,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code like '____' order by location asc "));
			//获取信用类型的拼音首字母
			Map<String,String> xyMap = daoCtl.getHTable(dao,Sqls.create("select code,name from cs_value where typeid = '00010005' and state = 0 and code like '____' "));
			for(Map.Entry<String, String> entry:xyMap.entrySet()){
				xyMap.put(entry.getKey(), PinyinUtil.cn2py(xyMap.get(entry.getKey()).replace("、","")));
			}
			req.setAttribute("xyGson",gson.toJson(xyList));//信用类型
			
			//获取行政区域的拼音首字母
			Map<String,String> xzqhMap = daoCtl.getHTable(dao,Sqls.create("select value,name from cs_value where typeid='00010004' and state=0 and value like '34__00'   order by location asc,id asc"));
			for(Map.Entry<String, String> entry:xzqhMap.entrySet()){
				xzqhMap.put(entry.getKey(), PinyinUtil.cn2py(xzqhMap.get(entry.getKey()).replace("、","")));
			}
			//获取协会的拼英首字母
			Map<String,String> hyxhMap = daoCtl.getHTable(dao,Sqls.create("select id,name from sys_unit where unittype = 90 order by id "));
			for(Map.Entry<String, String> entry:hyxhMap.entrySet()){
				hyxhMap.put(entry.getKey(), PinyinUtil.cn2py(hyxhMap.get(entry.getKey()).replace("、","")));
			}

			//图形报表Map
			Map<String,String> xyNameMap = daoCtl.getHTable(dao,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' order by location asc"));
			List<String> xyCode = daoCtl.getStrRowValues(dao,Sqls.create(" select code,name from cs_value where typeid = '00010005' and state = 0 and code < '0008' order by location asc"));

			req.setAttribute("xyNameMap",gson.toJson(xyNameMap));
			//开始处理报表数据
			Map<String,Map<String,String>> allMap = new HashMap<String, Map<String, String>>();
			String groupBy="xy_type";
			JSONArray array = new JSONArray();
			JSONArray xzqharray = new JSONArray();
			
			
			//行业协会总体展示数据
			String hyxhId = "";
			if("34__00".equals(xzqh)){
				for(Sys_unit unit : unitList){
					hyxhId += unit.getId()+",";
				}
				hyxhId = hyxhId.substring(0,hyxhId.length()-1);
				Map<String,String> csMap = daoCtl.getHTable(dao,Sqls.create(" select unitid,count(id) from discredit_info_view where unittype='90' group by unitid"));
				JSONObject jsonroot = new JSONObject();
				for(Map.Entry<String, String> entry:csMap.entrySet()){
					if(EmptyUtils.isNotEmpty(hyxhMap.get(entry.getKey()))){
						jsonroot.put(hyxhMap.get(entry.getKey()),entry.getValue());
					}
				}
				xzqharray.add(jsonroot);
			}

			//按区域统计
			for(Cs_value csValue : xzqhList){
				String xzqhStr = csValue.getValue();
				if(xzqhStr.substring(4,xzqhStr.length()).equals("00")){
					xzqhStr = xzqhStr.substring(0,4)+"__";
				}
				//String sqlstr=" select "+groupBy+",count(id) from discredit_info_VIEW where xzqh like '"+xzqhStr+"' and  create_date between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59' group by "+groupBy;
				String sqlstr=" select xzqh,count(id) from discredit_info_VIEW where xzqh like '"+xzqhStr+"'and unittype!=90 group by xzqh";
				Map<String,String> csMap = daoCtl.getHTable(dao,Sqls.create(sqlstr));
				allMap.put(csValue.getValue(),csMap);
				JSONObject jsonroot = new JSONObject();
				jsonroot.put("id", csValue.getValue());
				jsonroot.put("name",csValue.getName());
				for(Map.Entry<String, String> entry:csMap.entrySet()){
					if(EmptyUtils.isNotEmpty(xzqhMap.get(entry.getKey()))){
						jsonroot.put(xzqhMap.get(entry.getKey()),entry.getValue());
					}
				}
				xzqharray.add(jsonroot);
			}
			
			//按信用类型统计
			String sqlstr=" select "+groupBy+",count(id) from (select id,substr(xy_type,0,4)xy_type,xzqh from discredit_info_view)  group by "+groupBy;
			Map<String,String> csMap = daoCtl.getHTable(dao,Sqls.create(sqlstr));
			JSONObject jsonroot = new JSONObject();
			for(Map.Entry<String, String> entry:csMap.entrySet()){
				if(EmptyUtils.isNotEmpty(xyMap.get(entry.getKey()))){
					jsonroot.put(xyMap.get(entry.getKey()),entry.getValue());
				}
			}
			array.add(jsonroot);
				
			req.setAttribute("charsData", array.toString());
			req.setAttribute("xzqhdata", xzqharray.toString());
			req.setAttribute("allMap",allMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 打开系统帮助pdf浏览界面
	 * 
	 * @param req
	 * @param session
	 */
	@At
	@Ok("->:/private/helpfile/help.html")
	public void help(HttpServletRequest req, HttpSession session) {

	}

	/**
	 * 打开系统帮助pdf浏览界面
	 * 
	 * @param req
	 * @param session
	 */
	@At
	@Ok("raw")
	public void downfile(HttpServletRequest req, HttpServletResponse rep,
			HttpSession session,@Param("filename") String filename) {
		try {
			String filepath = Globals.APP_BASE_PATH+ "\\private\\helpfile\\file\\";
			// File file = new File(filepath+name+".pdf");
			rep.setContentType("application/pdf");
			rep.addHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(filename, "utf-8"));

			BufferedInputStream input = new BufferedInputStream(
					new FileInputStream(filepath + filename));
			byte buffBytes[] = new byte[1024];
			OutputStream os = rep.getOutputStream();
			int read = 0;
			while ((read = input.read(buffBytes)) != -1) {
				os.write(buffBytes, 0, read);
			}
			os.flush();
			os.close();
			input.close(); // 下载指定PDF文件
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 打开系统帮助页面
	 * 
	 * @param req
	 * @param session
	 */
	@At
	@Ok("->:/private/system_help.html")
	public void sysHelp(HttpServletRequest req, HttpSession session) {

	}

	/**
	 * 打开系统帮助pdf浏览界面
	 * 
	 * @param req
	 * @param session
	 */
	@At
	@Ok("->:/private/helpPdf.html")
	public void helppdf(HttpServletRequest req, HttpSession session) {

	}

	/**
	 * 打开系统帮助视频浏览界面
	 * 
	 * @param req
	 * @param session
	 */
	@At
	@Ok("->:/private/helpMedia.html")
	public void helpmedia(HttpServletRequest req, HttpSession session) {

	}

	@At
	@Ok("raw")
	public boolean reload(@Param("resid") String resid, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		if (daoCtl.update(dao, Sys_user.class, Chain.make("loginresid", resid),
				Cnd.where("userid", "=", user.getUserid()))) {
			user.setLoginresid(resid);
			session.setAttribute("userSession", user);
			return true;
		}
		return false;
	}

	/*@At
	@Ok("raw")
	public String dounlock(@Param("password") String password,HttpServletRequest req, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		if (!StringUtil.null2String(password).equals(
				DecodeUtil.Decrypt(user.getPassword()))) {
			return "密码不正确，请输入当前登陆用户密码！";
		}else{
			session.setAttribute("validate", "");
			return "true";
		}
		
	}*/
	
	@At
	@Ok("raw")
	public String dounlock(@Param("password") String password,
			HttpServletRequest req, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		password = new String(Decode64Util.Decrypt(password));
		if (!StringUtil.null2String(password).equals(
				DecodeUtil.Decrypt(user.getPassword()))) {
			return "密码不正确，请输入当前登陆用户密码！";
		} else {
			session.setAttribute("validate", "");
			user.setValidate(false);
			session.setAttribute("userSession", user);
			return "true";
		}
	}

	@At
	@Ok("->:/private/index.html")
	public void index(HttpServletRequest req, HttpSession session,ServletContext content) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		
		// 判断用户角色是否具有CTI
		Sql cti = Sqls
				.create("select count(1) from sys_user_role a  inner join sys_user b on(a.UserId=b.userId) inner join sys_role c on(c.Id=a.RoleId) where  c.name='CTI' and b.Userid=@userid");
		cti.params().set("userid", user.getUserid());
		int hascti = daoCtl.getIntRowValue(dao, cti);
		if (hascti == 1) {
			req.setAttribute("cti", "cti");
		}
		
		// 获取用户一级资源菜单
		List<Sys_resource> moduleslist = daoCtl.list(
				dao,
				Sys_resource.class,
				Cnd.wrap(" id like '____' and id in "
						+ StringUtil.getStrsplit(user.getReslist())
						+ " order by location "));
		req.setAttribute("moduleslist", moduleslist);
//		String resid = StringUtil.null2String(user.getLoginresid());
		String resid=daoCtl.getStrRowValue(dao, Sqls.create("select loginresid from sys_user where userid='"+user.getUserid()+"'"));
		if ("".equals(resid)) {
			for (Sys_resource res : moduleslist) {
				resid = res.getId();
				break;
			}
		}
		// 获取用户二级资源菜单
		List<Sys_resource> modulessublist = daoCtl.list(
				dao,
				Sys_resource.class,
				Cnd.wrap(" id like '" + resid + "____' and id in "
						+ StringUtil.getStrsplit(user.getReslist())
						+ " order by location "));
		req.setAttribute("modulessublist", modulessublist);
		req.setAttribute("resid", resid);
		
		req.setAttribute("validate", session.getAttribute("validate"));
		// 查询目前该用户的未读消息
		int notices_num = daoCtl
				.getIntRowValue(
						dao,
						Sqls.create("select count(id) from msg_info i where id in (select u.msgid from MSG_USER u where u.jstate =0  and jlogin = '"
								+ user.getLoginname() + "' )"));
		req.setAttribute("notices_num", notices_num);

		// 返回工号或分机号
		req.setAttribute("user", user);

		// 获取当前用户配色方案
		Cms_user_style cmsUserStyle = daoCtl.detailByCnd(dao,
				Cms_user_style.class,
				Cnd.where("user_id", "=", user.getUserid()));
		if (cmsUserStyle != null) {
			content.setAttribute("stylename", cmsUserStyle.getStylename());
		} else {
			content.setAttribute("stylename", "blue");
		}
		req.setAttribute("version", comUtil.version);
	}

	// 去掉督办中心工作流程
	public void cancelDB() {
		Trans.exec(new Atom() {

			public void run() {
				daoCtl.update(
						dao,
						Sys_resource.class,
						Chain.make("state", "1"),
						Cnd.where("id", "in",
								"'0002000200010002','0002000200010006','0002000200010004','0002000200020001'"));
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200010008"),
						Cnd.where("id", "=", "0002000200020006"));
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200010009"),
						Cnd.where("id", "=", "0002000200020005"));
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200010010"),
						Cnd.where("id", "=", "0002000200020003"));
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200010011"),
						Cnd.where("id", "=", "0002000200020004"));
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200010012"),
						Cnd.where("id", "=", "0002000200020007"));
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200010013"),
						Cnd.where("id", "=", "0002000200020008"));
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200010014"),
						Cnd.where("id", "=", "0002000200020009"));
			}
		});

	}

	// 保留督办中心流程
	public void useDB() {
		Trans.exec(new Atom() {

			public void run() {
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200020006"),
						Cnd.where("id", "=", "0002000200010008"));
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200020005"),
						Cnd.where("id", "=", "0002000200010009"));
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200020003"),
						Cnd.where("id", "=", "0002000200010010"));
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200020004"),
						Cnd.where("id", "=", "0002000200010011"));
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200020007"),
						Cnd.where("id", "=", "0002000200010012"));
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200020008"),
						Cnd.where("id", "=", "0002000200010013"));
				daoCtl.update(dao, Sys_resource.class,
						Chain.make("id", "0002000200020009"),
						Cnd.where("id", "=", "0002000200010014"));
				daoCtl.update(
						dao,
						Sys_resource.class,
						Chain.make("state", "0"),
						Cnd.where(
								"id",
								"in",
								"'0002000200010002','0002000200010006','0002000200010004','0002000200020001','0002000200020008'"));
			}
		});
	}

	@At
	@Ok("->:/private/left.html")
	public void left(@Param("sys_menuid") String sys_menuid,
			HttpServletRequest req, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		List<Sys_resource> menulist = daoCtl.list(
				dao,
				Sys_resource.class,
				Cnd.wrap(" id like '" + sys_menuid + "____' and id in "
						+ StringUtil.getStrsplit(user.getReslist())
						+ " order by LOCATION "));
		Hashtable<String, List<Sys_resource>> threemenu = new Hashtable<String, List<Sys_resource>>();
		for (int i = 0; i < menulist.size(); i++) {
			List<Sys_resource> threemenulist = daoCtl.list(
					dao,
					Sys_resource.class,
					Cnd.wrap(" id like '" + menulist.get(i).getId()
							+ "____' and id in "
							+ StringUtil.getStrsplit(user.getReslist())
							+ " order by LOCATION "));
			threemenu.put(menulist.get(i).getId(), threemenulist);
		}
		req.setAttribute("menulist", menulist);
		req.setAttribute("threemenulist", threemenu);
	}

	@At
	@Ok("->:/private/welcome.html")
	public void welcome() {

	}

	@At
	@Ok("->:/private/changeStyle.html")
	public void toChangeStyle() {
	}

	@At
	@Ok("->:/private/left.html")
	public String doChangeStyle(@Param("stylename") String stylename,
			HttpServletRequest req, HttpSession session, ServletContext content) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Cms_user_style cmsUserStyle = daoCtl.detailByCnd(dao,
				Cms_user_style.class,
				Cnd.where("user_id", "=", user.getUserid()));
		boolean flag = false;
		if (cmsUserStyle == null || cmsUserStyle.equals("")) {
			cmsUserStyle = new Cms_user_style();
			cmsUserStyle.setStylename(stylename);
			cmsUserStyle.setUser_id(user.getUserid());
			flag = daoCtl.add(dao, cmsUserStyle);
		} else {
			cmsUserStyle.setStylename(stylename);
			flag = daoCtl.update(dao, cmsUserStyle);
		}
		content.setAttribute("stylename", cmsUserStyle.getStylename());
		return flag ? "true" : "false";
	}

	/**
	 * @author wanfly
	 * @time 2014-05-20 11:02:35 描述：主要用于页面Ajax查询该登陆用户此时的未读消息（包括通知、站内消息、文件传送）
	 *       的总数。
	 */
	@At
	@Ok("raw")
	public String searchNotics(HttpServletRequest req, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		int notices_num = daoCtl
				.getIntRowValue(
						dao,
						Sqls.create("select count(i.id) from msg_info i where i.id in (select u.msgid from MSG_USER u where u.jstate =0  and u.jlogin = '"
								+ user.getLoginname() + "' )"));
		// System.out.println("查询消息="+notices_num);
		return notices_num + "";
	}

	public static void main(String[] args) {
		System.out.println("000" + (Long.parseLong("0002000200010008") + 1));
	}
}
