package com.hits.modules.sys;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hits.modules.bean.Cs_value;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

import com.google.gson.Gson;
import com.hits.common.action.BaseAction;
import com.hits.common.config.Globals;
import com.hits.common.filter.*;
import com.hits.common.util.DecodeUtil;
import com.hits.common.util.StringUtil;
import com.hits.modules.com.comUtil;
import com.hits.modules.sys.bean.Sys_role_resource;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.Decode64Util;
import com.hits.util.EmptyUtils;

/**
 * @author Wizzer.cn
 * @time 2012-9-14 上午11:45:52
 * 
 */
@IocBean
@At("/private/sys/unit")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class UnitAction extends BaseAction {
	@Inject
	protected Dao dao;
	@At("")
	@Ok("->:/private/sys/unit.html")
	public void unit(HttpServletRequest req, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String[] mp = StringUtil.null2String(
				user.getBtnmap().get("/private/sys/unit")).split(";");
		req.setAttribute("btnmap", mp);
		
		List<Sys_role_resource> reslist = daoCtl.list(dao,Sys_role_resource.class, Cnd.wrap("resourceid = '000100010001'"));				
		HashSet<String> set = new HashSet<String>();
		for(Sys_role_resource resource:reslist){
			if(user.getRolelist().contains(resource.getRoleid()+"")){
			String button = resource.getButton();
			if(!"".equals(button)&&button!=null){
				String[] buttons =button.split(",");
				for(int i=0;i<buttons.length;i++){
					set.add(buttons[i]);
				}
			}
			}
		}
		req.setAttribute("buttonset", set);
	}
	
	
	@At
	@Ok("->:${obj}")
	public String toTree(HttpServletRequest req,HttpSession session, @Param("id") String id,@Param("sjunit")String sjunit){
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		if (EmptyUtils.isEmpty(id)) {
			id = "0008";
		}
		if(EmptyUtils.isNotEmpty(sjunit)){
			id=sjunit;
		}
		String sql="select id from Sys_unit where id='"+id+"'";
		req.setAttribute("initCode", daoCtl.getStrRowValue(dao, Sqls.create(sql)));
		req.setAttribute("id", id);
		req.setAttribute("sjunit", sjunit);
//		List<String> roleList=user.getRolelist();
//		if (roleList.contains(arg0)) {
//			
//		}
		String html="/private/global/unit_tree.html";
		return html;
		
	}
	
	@At
	@Ok("raw")
	public String ajaxTree(@Param("id")String unitid,@Param("sjunit")String sjunit) {
		Long start = System.currentTimeMillis();
		JSONArray array = new JSONArray();
		Criteria cri = Cnd.cri();
		
		if (EmptyUtils.isEmpty(unitid)&&EmptyUtils.isEmpty(sjunit)) {
			cri.where().and("id", "=","0008").and("length(id)", "=",4);
		}else if(EmptyUtils.isNotEmpty(sjunit)&&EmptyUtils.isEmpty(unitid)){
			cri.where().and("id", "=", sjunit);
		}else{
			cri.where().and("id", "like", unitid+"____");
		}
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		
		List<Sys_unit> list = daoCtl.list(dao, Sys_unit.class, cri);
		for (int i = 0; i < list.size(); i++) {
			Sys_unit res = list.get(i);
			JSONObject jsonobj = new JSONObject();
			
			jsonobj.put("id", res.getId());
			jsonobj.put("pId", 0);
			jsonobj.put("name",  res.getName());
			jsonobj.put("value", res.getId());
			jsonobj.put("target", "_self");
			jsonobj.put("icon", Globals.APP_BASE_NAME+ "/images/icons/base.png");
//				查询子集数量
			int num = daoCtl.getRowCount(dao, Sys_unit.class,Cnd.where("id", "like", res.getId() + "____"));
			if (num > 0) {
				jsonobj.put("isParent", true);
			}else{
				jsonobj.put("isParent", false);
			}
			array.add(jsonobj);
		}
		Long end = System.currentTimeMillis();
		System.out.println(end-start+"-------------------用时");
		return array.toString();
	}

	@At
	@Ok("raw")
	public String tree(@Param("id") String id, HttpSession session)
			throws Exception {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		id = StringUtil.null2String(id);
		JSONArray array = new JSONArray();
//		if ("".equals(id)) {
//			JSONObject jsonroot = new JSONObject();
//			jsonroot.put("id", "0");
//			jsonroot.put("pId", "");
//			jsonroot.put("name", "机构列表");
//			jsonroot.put("icon", Globals.APP_BASE_NAME
//					+ "/images/icons/icon042a1.gif");
//			array.add(jsonroot);
//		}
		if(id.length()<4){
			Criteria cri = Cnd.cri();
//				if (user.getSysrole()) // 判断是否为系统管理员角色
//				{
			cri.where().and("id" ,"like","____");
			cri.getOrderBy().asc("location");
			cri.getOrderBy().asc("id");
//				} else {
//					if ("".equals(id)) {
//						cri.where().and("unittype", "=", id);
//						cri.where().and("id" ,"like","____");
//						cri.getOrderBy().asc("location");
//						cri.getOrderBy().asc("id");
//					} else {
//						cri.where().and("unittype", "=",id);
//						cri.where().and("id" ,"like","____");
//						cri.getOrderBy().asc("location");
//						cri.getOrderBy().asc("id");
//					}
//				}
			
			List<Sys_unit> unitlist = daoCtl.list(dao,Sys_unit.class, cri);
			int i = 0;
			for (Sys_unit u : unitlist) {
				String pid = u.getId().substring(0, u.getId().length() - 4);
				int num = daoCtl.getRowCount(dao,Sys_unit.class,
						Cnd.wrap("id like '" + u.getId() + "____'"));
				if (i == 0 || "".equals(pid))
					pid = "0";
				JSONObject obj = new JSONObject();
				obj.put("id", u.getId());
				obj.put("pId", pid);
				obj.put("name", u.getName());
				obj.put("url", "javascript:view(\"" + u.getId() + "\")");
				obj.put("isParent", num > 0 ? true : false);
				obj.put("target", "_self");
				obj.put("icon", Globals.APP_BASE_NAME+"/images/icons/icon040a1.gif");
				array.add(obj);
				i++;
			}
		}else{
			Criteria cri = Cnd.cri();
			if (user.getSysrole()) // 判断是否为系统管理员角色
			{
				cri.where().and("id", "like", id + "____");
				cri.getOrderBy().asc("location");
				cri.getOrderBy().asc("id");
			} else {
				if ("".equals(id)) {
					cri.where().and("id", "=", user.getUnitid());
					cri.getOrderBy().asc("location");
					cri.getOrderBy().asc("id");
				} else {
					cri.where().and("id", "like", id + "____");
					cri.getOrderBy().asc("location");
					cri.getOrderBy().asc("id");
				}
			}
			
			List<Sys_unit> unitlist = daoCtl.list(dao,Sys_unit.class, cri);
			int i = 0;
			for (Sys_unit u : unitlist) {
				String pid = u.getId().substring(0, u.getId().length() - 4);
				int num = daoCtl.getRowCount(dao,Sys_unit.class,
						Cnd.wrap("id like '" + u.getId() + "____'"));
				if (i == 0 || "".equals(pid))
					pid = "0";
				JSONObject obj = new JSONObject();
				obj.put("id", u.getId());
				obj.put("pId", pid);
				obj.put("name", u.getName());
				obj.put("url", "javascript:view(\"" + u.getId() + "\")");
				obj.put("isParent", num > 0 ? true : false);
				obj.put("target", "_self");
				obj.put("icon", Globals.APP_BASE_NAME+"/images/icons/icon040a1.gif");
				array.add(obj);
				i++;
			}
		}
		//System.out.println("机构列表：\n"+array.toString());
		return array.toString();
	}

	@At
	@Ok("json")
	public Sys_unit view(@Param("id") String id)
			 {
		id = StringUtil.null2String(id);
		Sys_unit unit = daoCtl.detailByName(dao,Sys_unit.class, id);
		unit.setUnittype(comUtil.unittypeMap.get(unit.getUnittype()));
		String xzqh="";
		String xzqhCode=unit.getXzqh();
		if(EmptyUtils.isNotEmpty(xzqhCode)&&xzqhCode.length()>4){
			if(xzqhCode.endsWith("0000")){
				xzqh=comUtil.xzqh.get(xzqhCode);
			}else if(xzqhCode.endsWith("00")){
				xzqh=comUtil.xzqh.get(xzqhCode.substring(0,xzqhCode.length()-4)+"0000")+">"+comUtil.xzqh.get(xzqhCode);
			}else{
				xzqh=comUtil.xzqh.get(xzqhCode.substring(0,xzqhCode.length()-4)+"0000")+">"+comUtil.xzqh.get(xzqhCode.substring(0,xzqhCode.length()-2)+"00")+">"+comUtil.xzqh.get(xzqhCode);
			}
		}
		unit.setXzqh(xzqh);
		return unit;
	}

	@At
	@Ok("->:/private/sys/unitAdd.html")
	public void add(@Param("id") String id, HttpServletRequest req) {
		List<Sys_unit> unit = daoCtl.list(dao,Sys_unit.class, Sqls.create("select * from sys_unit order by location,id"));
		List<Sys_unit> unit_xy = daoCtl.list(dao,Sys_unit.class, Sqls.create("select * from sys_unit where unittype = '88' order by location,id"));
		req.setAttribute("unit", unit);
		req.setAttribute("unit_xy", unit_xy);
		req.setAttribute("unittypes", comUtil.unittype);
		req.setAttribute("csValues", comUtil.sxxwtypeList);
	}

	@At
	@Ok("raw")
	public String addSave(@Param("..") Sys_unit unit) {
		String id="";
		try{
			id = daoCtl.getSubMenuId(dao,"sys_unit", "id",
					StringUtil.null2String(unit.getId()).equals("0")?"":StringUtil.null2String(unit.getId()));
			unit.setId(id);
			int location=daoCtl.getIntRowValue(dao,Sqls.create("select max(location) from sys_unit"));
			unit.setLocation(location);
			if (daoCtl.add(dao,unit) ){
				comUtil.initUnit(dao, daoCtl);
			}else{
				id="";
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return id;
	}	
	
	@At
	@Ok("raw")
	public boolean del(@Param("id") String id) {
		boolean res;
		Sql sql=Sqls.create("delete from sys_unit where id like @id");
		sql.params().set("id", id+"%");
		res=daoCtl.exeUpdateBySql(dao,sql);
		if(res){
			daoCtl.exeUpdateBySql(dao,Sqls.create("delete from sys_role_resource where roleid in(" +
					"select id from sys_role where unitid like '"+id+"%')"));
			daoCtl.exeUpdateBySql(dao,Sqls.create("delete from sys_user_role where userid in(" +
					"select userid from sys_user where unitid like '"+id+"%')"));
			daoCtl.exeUpdateBySql(dao,Sqls.create("delete from sys_user where unitid like '"+id+"%'"));
			daoCtl.exeUpdateBySql(dao,Sqls.create("delete from sys_role where unitid like '"+id+"%'"));
			comUtil.initUnit(dao, daoCtl);
		}
		
		return res; 
	}
	
	@At
	@Ok("->:/private/sys/unitUpdate.html")
	public void update(@Param("id") String id, HttpServletRequest req) {
		Sys_unit unit = daoCtl.detailByName(dao,Sys_unit.class, id);
		List<Sys_unit> unit_xy = daoCtl.list(dao,Sys_unit.class, Sqls.create("select * from sys_unit where unittype = '88' order by location,id"));
		req.setAttribute("obj", unit);
		req.setAttribute("unit_xy", unit_xy);
		req.setAttribute("csValues", comUtil.sxxwtypeList);
		req.setAttribute("xzqhMap", comUtil.xzqh);
		req.setAttribute("unittypes", comUtil.unittype);
	}
	
	@At
	@Ok("raw")
	public String updateSave(@Param("..") Sys_unit unit) { 
		String id="";
		try {
			if(daoCtl.update(dao,unit)){
				id=unit.getId();
				comUtil.initUnit(dao, daoCtl);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return id;
		 
	}
	
	@At
	@Ok("->:/private/sys/unitSort.html")
	public void sort(@Param("id") String id, HttpServletRequest req,HttpSession session) throws Exception{
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Condition c; 
        if (user.getSysrole())  //判断是否为系统管理员角色
        {      	 
        	c=Cnd.where("id","is not", null).asc("location,id");
        } else { 
            c=Cnd.where("id","like",user.getUnitid() + "%'").asc("location,id");
        }
        List<Sys_unit> list=daoCtl.list(dao,Sys_unit.class, c);
        JSONArray array = new JSONArray();
        JSONObject jsonroot = new JSONObject();
		jsonroot.put("id", "");
		jsonroot.put("pId", "0");
		jsonroot.put("name", "机构列表");
		jsonroot.put("open", true);
		jsonroot.put("childOuter", false);
		jsonroot.put("icon", Globals.APP_BASE_NAME
				+ "/images/icons/icon042a1.gif");
		array.add(jsonroot);
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonobj = new JSONObject();
            Sys_unit obj = list.get(i);
            jsonobj.put("id", obj.getId());
            String p=obj.getId().substring(0, obj.getId().length() - 4);
            jsonobj.put("pId", p==""?"0":p); 
            jsonobj.put("name", obj.getName());
            jsonobj.put("childOuter", false);
            if (obj.getId().length() < 12) {
                jsonobj.put("open", true);
            }else{
                jsonobj.put("open", false);
            }
            array.add(jsonobj);
        }
		
		req.setAttribute("str", array.toString());
	}
	
	@At
	@Ok("raw")
	public boolean sortSave(@Param("checkids") String checkids,HttpSession session) { 

		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String[] ids=StringUtil.null2String(checkids).split(","); 
		int initvalue=0; 
        if (!user.getSysrole())  //判断是否为系统管理员角色
        {
        	initvalue=daoCtl.getIntRowValue(dao,Sqls.create("select min(location) from sys_unit where id in " + StringUtil.getIdsplit(ids)));
        }
		return daoCtl.updateSortRow(dao,"sys_unit", ids, "location", initvalue);
		 
	}
	/**
	 * 转到单位查询界面
	 * @param req
	 * @param session
	 */
	@At
	@Ok("->:/private/sys/unitsearch.html")
	public void unitsearch(HttpServletRequest req,HttpSession session){
		//unitcode='bldw' 
		req.setAttribute("unitlist",daoCtl.list(dao, Sys_unit.class, Sqls.create("select * from Sys_unit order by id,location")));
	}
	
	@At
	@Ok("raw")
	public String unitsearchlist(HttpSession session, HttpServletRequest req,@Param("page") int curPage, 
			@Param("rows") int pageSize,@Param("keys") String keys,@Param("unitid") String unitid){
		StringBuffer sqls=new StringBuffer("select * from sys_unit ");
		List<String[]> params=new ArrayList<String[]>();
		if(null!=unitid && !unitid.equals("")){
			String[] a={"unitid",unitid};
			params.add(a);
		}
		if(null!=keys && !keys.equals("")){
			String[] a={"keys",keys};
			params.add(a);
		}
		System.out.println(keys+","+unitid+","+params.size());
		if(params.size()>0){
			sqls.append("where");
			for(int i=0;i<params.size();i++){
				if(i==0){
					if(params.get(i)[0].equals("keys"))
						sqls.append(" (").append("name").append(" like '%")
							.append(params.get(i)[1]).append("%'").append(" or HANDLER like '%")
							.append(params.get(i)[1]).append("%'").append(" or LEADER like '%")
							.append(params.get(i)[1]).append("%'").append(" or HANDLERPHONE like '%")
							.append(params.get(i)[1]).append("%'").append(" or LEADPHONE like '%")
							.append(params.get(i)[1]).append("%'").append(" or TELEPHONE like '%")
							.append(params.get(i)[1]).append("%'").append(" or ADDRESS like '%")
							.append(params.get(i)[1]).append("%')");
					else
						sqls.append(" ").append("id").append("='")
							.append(params.get(i)[1]).append("'");
				}else{
					if(params.get(i)[0].equals("keys"))
						sqls.append(" and (").append("name").append(" like '%")
							.append(params.get(i)[1]).append("%'").append(" or HANDLER like '%")
							.append(params.get(i)[1]).append("%'").append(" or LEADER like '%")
							.append(params.get(i)[1]).append("%'").append(" or HANDLERPHONE like '%")
							.append(params.get(i)[1]).append("%'").append(" or LEADPHONE like '%")
							.append(params.get(i)[1]).append("%'").append(" or TELEPHONE like '%")
							.append(params.get(i)[1]).append("%'").append(" or ADDRESS like '%")
							.append(params.get(i)[1]).append("%')");
					else
						sqls.append(" and ").append("id").append("='")
							.append(params.get(i)[1]).append("'");
				}
			}
		}
		sqls.append(" order by LOCATION,id DESC");
		System.out.println("ss="+sqls.toString());
		return daoCtl.listPageJsonSql(dao, Sqls.create(sqls.toString()), curPage, pageSize).toString();
	}
	
	@At
	@Ok("raw")
	public List<String> allUnit(){
		System.out.println("所有单位!");
		List<Sys_unit> list = daoCtl.list(dao,Sys_unit.class,Sqls.create("select * from sys_unit order by id"));
		List<String> jsonStr = new ArrayList<String>();
		for(Sys_unit unit : list){
			jsonStr.add("{id:'"+unit.getId()+"',name:'"+unit.getName()+"'}");
		}
		return jsonStr;
	}
	/*
	 * 跳转外乎、转接、三方通话页面
	 */
	@At
	@Ok("->:/include/js/call/telunit_usc.html")
	public void telunitHtml(HttpSession session, HttpServletRequest req,@Param("type") int type) {
        req.setAttribute("type",type);
	}
	@At
	@Ok("raw")
	public String telunit(HttpSession session, HttpServletRequest req) {
		System.out.println("------------------------------");
		List<Sys_unit> list = daoCtl.list(dao,Sys_unit.class,Sqls.create("select * from sys_unit where unittype=123  order by location,id"));
        Gson gson=new Gson();
        return gson.toJson(list);
//        return  daoCtl.listPageJsonSql(dao, Sqls.create("select * from sys_unit where unittype=123  order by location,id"), 1, 200).toString();
	}
}
