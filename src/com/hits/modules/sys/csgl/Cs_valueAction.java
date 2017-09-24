package com.hits.modules.sys.csgl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;

import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param; 


import com.hits.common.action.BaseAction;
import com.hits.common.config.Globals;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.StringUtil;
import com.hits.modules.bean.Cs_type;
import com.hits.modules.bean.Cs_value;
import com.hits.modules.com.YWCL;
import com.hits.modules.com.comUtil;
import com.hits.modules.sys.bean.Sys_role_resource;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;
import com.hits.util.PageObj;
import com.hits.util.SysLogUtil;

/**
 * @author lxy
 * @time 2015-05-22 14:58:17
 * 
 */
@IocBean
@At("/private/cs/value")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Cs_valueAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At("")
	@Ok("->:/private/sys/csgl/Cs_value.html")
	public void cs_value(HttpSession session,HttpServletRequest req) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String[] mp = StringUtil.null2String(
				user.getBtnmap().get("/private/dmj/t_daimjb")).split(";");
		req.setAttribute("btnmap", mp);

		List<Sys_role_resource> reslist = daoCtl.list(dao,Sys_role_resource.class, Cnd.wrap("resourceid = '000100020002'"));
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
	@Ok("raw")
	public String tree(@Param("flbh") String flbh, HttpSession session)
			throws Exception {
		flbh = StringUtil.null2String(flbh);
		JSONArray array = new JSONArray();
		Criteria cri = Cnd.cri();
		cri.where().and("id", "like", flbh + "____");
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		List<Cs_type> list = daoCtl.list(dao, Cs_type.class, cri);
		int i = 0;
		for (Cs_type type : list) {
			String pid = type.getId().substring(0, type.getId().length() - 4);
			int num = daoCtl.getRowCount(dao, Cs_type.class, Cnd.wrap("id like '" + type.getId() + "____'"));
			if (i == 0 || "".equals(pid))
				pid = "0";
			JSONObject obj = new JSONObject();
			obj.put("flbh", type.getId());
			obj.put("pId", pid);
			obj.put("name", type.getName());
			obj.put("target", "_self");
			obj.put("isParent", num > 0 ? true : false);
			if(num > 0){
				obj.put("iconSkin",  "pIcon01" );
			}else{
				obj.put("url", "javascript:list(\"" + type.getId() + "\")");
				obj.put("icon", Globals.APP_BASE_NAME+ "/images/icons/bm.gif");
			}
			array.add(obj);
			i++;
		}
		return array.toString();
	}
	/**
	 * 查询全部
	 * */
	@At
	@Ok("raw")
	public String listAll(@Param("flbh") String flbh, @Param("page") int curPage,@Param("rows") int pageSize,  HttpServletRequest req) {
//		Criteria cri = Cnd.cri();
//		flbh = StringUtil.null2String(flbh);
//		cri.where().and("typeid", "=", flbh);
//		cri.getOrderBy().asc("location");
//		cri.getOrderBy().asc("id");
//		JSONObject json = daoCtl.listPageJson(dao, Cs_value.class, curPage, pageSize, cri);
		req.setAttribute("flbh", flbh);
//		return json;
		String sql="select  * from cs_value where typeid='"+flbh +"' order by location asc,id asc";
		
		QueryResult qr =daoCtl.listPageSql(dao, Sqls.create(sql), curPage, pageSize);
		List<Map<String, Object>> list =  (List<Map<String, Object>>) qr.getList();
		for (int i = 0; i < list.size(); i++) {
			
			list.get(i).put("status", list.get(i).get("state"));
			
		}
		return PageObj.getPageJSON(qr);
	}
	
	/**
	 * 功能描述:查询问题所属区域的树形列表数据
	 *
	 * @author   2015年10月11日 下午8:40:50
	 * 
	 * @return
	 */
	@At
	@Ok("raw")
	public String listTreeAll(String typeid) {
		return getJSON(dao,null, typeid).toString();
	}
	
	private JSONArray getJSON(Dao dao,String code, String typeid) {
		Criteria cri = Cnd.cri();
		if (EmptyUtils.isEmpty(code)) {
			cri.where().and("typeid", "=", typeid).and("length(code)", "=", 4);
		} else {
			cri.where().and("typeid", "=", typeid).and("code", "like", code + "____");
		}
		
		cri.getOrderBy().asc("location");
//		cri.getOrderBy().asc("id");
		List<Cs_value> list = daoCtl.list(dao, Cs_value.class, cri);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Cs_value res = list.get(i);
			JSONObject jsonobj = new JSONObject();
			String pid = res.getCode().substring(0,res.getCode().length()-4);
			if ("".equals(pid)){
				pid = "0";
			}
			
			jsonobj.put("id", res.getId());
			jsonobj.put("name", res.getName());
			
			jsonobj.put("code", res.getCode());
			jsonobj.put("value", res.getValue());
			jsonobj.put("status", res.getState());
			jsonobj.put("_parentId", pid);
			
			//查询子集数量
			int num = daoCtl.getRowCount(dao, Cs_value.class,Cnd.where("code", "like", res.getCode() + "____").and("typeid", "=", typeid));
			if (num > 0) {
				jsonobj.put("children", getJSON(dao, res.getCode(), typeid));
			}
			array.add(jsonobj);
		}
		return array;
	}
	
	/**
	 * 功能描述:异步加载树形table
	 *
	 * @author (☆笑死宝宝了☆)  2015年10月26日 下午3:33:23
	 * 
	 * @param code
	 * @param typeid
	 * @return
	 */
	@At
	@Ok("raw")
	public String getAjaxTreeGraid(@Param("code")String code, @Param("typeid")String typeid) {
		Criteria cri = Cnd.cri();
		if (EmptyUtils.isEmpty(code)) {
			if (typeid.equals("00020005")) {
				cri.where().and("typeid", "=", typeid).and("length(code)", "=",8);
			}else{
				cri.where().and("typeid", "=", typeid).and("length(code)", "=",4);
			}
//			cri.where().and("typeid", "=", typeid).and("length(code)", "=", 4);
		} else {
			cri.where().and("typeid", "=", typeid).and("code", "like", code + "____");
		}
		
		cri.getOrderBy().asc("location");
		List<Cs_value> list = daoCtl.list(dao, Cs_value.class, cri);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Cs_value res = list.get(i);
			JSONObject jsonobj = new JSONObject();
			String pid = res.getCode().substring(0,res.getCode().length()-4);
			
			if ("".equals(pid)){
				pid = "0";
			}
			
			jsonobj.put("id", res.getId());
			jsonobj.put("name", res.getName());
			
			jsonobj.put("code", res.getCode());
			jsonobj.put("value", res.getValue());
			jsonobj.put("status", res.getState());
			jsonobj.put("_parentId", pid);
			
			//查询子集数量
			int num = daoCtl.getRowCount(dao, Cs_value.class,Cnd.where("code", "like", res.getCode() + "____").and("typeid", "=", typeid));
			if (num > 0) {
//				jsonobj.put("children", getJSON(dao, res.getCode(), typeid));
				jsonobj.put("state", "closed");
			}
			array.add(jsonobj);
		}
		return array.toString();
	}
	
	@At
	@Ok("->:/private/sys/csgl/Cs_valueAdd.html")
	public void toAdd(@Param("flbh") String flbh, HttpServletRequest req){
		Sql sql = Sqls.create("select code,name from cs_value where typeid='"+flbh+"' order by location asc");
		req.setAttribute("list", daoCtl.getMulRowValue(dao, sql));
		sql = Sqls.create("select name from cs_type where id='"+flbh+"'");
		req.setAttribute("flname", daoCtl.getStrRowValue(dao, sql));
		req.setAttribute("flbh", flbh);
	}
	
	@At
	public String add(@Param("..") Cs_value cs_value) {
		boolean result = false;
		String typeid="";
		try {
			String val = cs_value.getCode();
		    Sql sql = Sqls.create("select code from cs_value where typeid='"+cs_value.getTypeid()+"' and code like '" + val + "____' order by code desc");
		    String code=daoCtl.getStrRowValue(dao, sql);
			cs_value.setCode(YWCL.getTreeCode(val,code));
			result = daoCtl.add(dao, cs_value);
			if(result){
				comUtil.setHt(dao, daoCtl, cs_value.getTypeid());
				typeid=cs_value.getTypeid();
			}
			String res=result==true?"成功":"失败";
			SysLogUtil.addLog(dao, "",SysLogUtil.user_log_type, "增加"+cs_value.getName()+res+",id="+cs_value.getId()+",所属分类id为"+cs_value.getTypeid());
		} catch (Exception e) {
			e.printStackTrace();
			SysLogUtil.addLog(dao, "",SysLogUtil.yx_log_type, e.getMessage());
			return "";
		}
		return typeid;
	}
	
	@At
	@Ok("->:/private/sys/csgl/Cs_valueUpdate.html")
	public void toupdate(@Param("id") long id,HttpSession session, HttpServletRequest req){
		Cs_value obj = daoCtl.detailById(dao, Cs_value.class, id);
		req.setAttribute("obj", obj);
	}
	@At
	public boolean update(@Param("..") Cs_value cs_value) {
		boolean result = false;
		try {
			result = daoCtl.update(dao, cs_value);
			if(result){
				comUtil.setHt(dao, daoCtl, cs_value.getTypeid());
			}
			String res=result==true?"成功":"失败";
			SysLogUtil.addLog(dao, "",SysLogUtil.user_log_type, "修改"+res+",id="+cs_value.getId()+",所属分类id为"+cs_value.getTypeid());
		} catch (Exception e) {
			e.printStackTrace();
			SysLogUtil.addLog(dao, "",SysLogUtil.yx_log_type, e.getMessage());
			return false;
		} 
		return result;
	}
	
	@At
	@Ok("raw")
	public boolean del(@Param("id") String ids,@Param("flbh") String flbh) {
		boolean result = false;
		try {
			String[] id = StringUtil.null2String(ids).split(",");
			List<String> codes=daoCtl.getStrRowValues(dao, Sqls.create("select code from cs_value where id in ("+ids+")"));
			result = daoCtl.deleteByIds(dao, Cs_value.class, id);
			if (result) {
				for (int i = 0; i < codes.size(); i++) {
					String code=codes.get(i);
					daoCtl.exeUpdateBySql(dao,
							Sqls.create("delete from Cs_value where typeid='"+flbh+"' and code like '"+ code + "%'"));
				}
				comUtil.setHt(dao, daoCtl, flbh);
			}
			String res=result==true?"成功":"失败";
			SysLogUtil.addLog(dao, "",SysLogUtil.user_log_type, "删除"+res+",所属分类id为"+flbh);
		} catch (Exception e) {
			SysLogUtil.addLog(dao, "",SysLogUtil.yx_log_type, e.getMessage());
			return false;
		}
		return result;
	}
	
	/**
	 * 转到排序页面
	 * */
	@At
	@Ok("->:/private/sys/csgl/Cs_valueSort.html")
	public void toSort(@Param("flbh") String flbh,HttpServletRequest req) throws Exception {
		JSONArray array = new JSONArray();
		Criteria cri = Cnd.cri();
		cri.where().and("typeid", "=", flbh);
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		List<Cs_value> list = daoCtl.list(dao, Cs_value.class, cri);
		JSONObject jsonroot = new JSONObject();
		jsonroot.put("id", "");
		jsonroot.put("pId", "0");
		jsonroot.put("name", "排序列表");
		jsonroot.put("open", true);
		jsonroot.put("childOuter", false);
		jsonroot.put("icon", Globals.APP_BASE_NAME
				+ "/images/icons/icon042a1.gif");
		array.add(jsonroot);
		for (int i = 0; i < list.size(); i++) {
			JSONObject jsonobj = new JSONObject();
			Cs_value obj = list.get(i);
			jsonobj.put("id", obj.getCode());
			String p = obj.getCode().substring(0, obj.getCode().length() - 4);
			jsonobj.put("pId", "".equals(p) ? "0" : p);
			String name = obj.getName();
			jsonobj.put("name", name);
			jsonobj.put("childOuter", false);
			if (obj.getCode().length() < 12) {
				jsonobj.put("open", true);
			} else {
				jsonobj.put("open", false);
			}
			array.add(jsonobj);
		}
		req.setAttribute("str", array.toString());
		req.setAttribute("flbh", flbh);
	}

	/***
	 * 确认排序
	 * */
	@At
	@Ok("raw")
	public boolean sort(@Param("checkids") String checkids,@Param("flbh") String flbh) {
		boolean rs =false;
		try {
			if(checkids.endsWith(",")) checkids=checkids.substring(0,checkids.length()-1);
			if(checkids.startsWith(",")) checkids=checkids.substring(1,checkids.length());
			String[] codeids = checkids.split(",");
			String[] ids = new String[codeids.length];
			Hashtable<String,String> ht =daoCtl.getHTable(dao, Sqls.create("select code, id from cs_value where typeid='"+flbh+"' and code in ("+checkids+")"));
			for(int i=0;i<codeids.length;i++){
				ids[i] = ht.get(codeids[i]);
			}
			rs = daoCtl.updateSortRow(dao, "Cs_value", ids,"location", 0);
			if(rs){
				comUtil.setHt(dao, daoCtl, flbh);
			}
			String res=rs==true?"成功":"失败";
			SysLogUtil.addLog(dao, "",SysLogUtil.user_log_type, "排序"+res+",所属分类id为"+flbh);
		} catch (Exception e) {
			SysLogUtil.addLog(dao, "",SysLogUtil.yx_log_type, e.getMessage());
			return false;
		}
		return rs;

	}
	
	/**
	 * 功能描述:一次性查询所有的combotree数据   treeid 取得是 value字段值
	 *
	 * @author  2015年10月13日 上午11:27:24
	 * 
	 * @param typeid
	 * @param parentCode
	 * @param checkValue
	 * @return
	 */
	@At
	@Ok("raw")
	public String getAllCombotreeList(HttpSession session,String typeid, String code) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		return getAllCombotreeJson(user,typeid, code).toString();
	}
	
	private JSONArray getAllCombotreeJson(Sys_user user,String typeid, String code) {
		JSONArray array = new JSONArray();
		Criteria cri = Cnd.cri();
		
		if (EmptyUtils.isEmpty(code)) {
			cri.where().and("length(code)", "=", 4);
			JSONObject jsonobj1 = new JSONObject();
			jsonobj1.put("id", "");
			jsonobj1.put("text", "请选择");
			array.add(jsonobj1);
		} else {
			cri.where().and("code", "like", code + "____");
		}
		cri.where().and("state", "=", 0).and("typeid", "=", typeid);
		
		cri.getOrderBy().asc("location");
		List<Cs_value> list = daoCtl.list(dao, Cs_value.class, cri);
		
		
		for (int i = 0; i < list.size(); i++) {
			Cs_value res = list.get(i);
			JSONObject jsonobj = new JSONObject();
			
			String pid = res.getCode().substring(0, res.getCode().length() - 4);

			jsonobj.put("id",res.getValue());
			jsonobj.put("text", res.getName());
			
			int num = daoCtl.getRowCount(dao, Cs_value.class,Cnd.where("code", "like", res.getCode() + "____").and("typeid", "=", typeid));
			if (num > 0) {
				jsonobj.put("children", getAllCombotreeJson(user,typeid, res.getCode()));
			}
			array.add(jsonobj);
		}
		return array;
	}
	
	
	
	
	/**
	 * 功能描述:异步查询combotree数据 ，id的值为字段value的值
	 *
	 * @author   2015年10月11日 下午8:40:50
	 * 
	 * @param typeid 配置所属分类id
	 * @param code 当前选择配置code
	 * @param value 当前选择的代码值
	 * @return
	 */
	@At
	@Ok("raw")
	public String listCombotTree(@Param("code")String code,@Param("typeid") String typeid,@Param("value")String value) {
		JSONArray array = new JSONArray();
		Criteria cri = Cnd.cri();
		if (EmptyUtils.isEmpty(code)) {
			if (typeid.equals("00020005")) {
				cri.where().and("typeid", "=", typeid).and("length(code)", "=",8);
			}else{
				cri.where().and("typeid", "=", typeid).and("length(code)", "=",4);
			}
			JSONObject jsonobj1 = new JSONObject();
			jsonobj1.put("id", "");
			jsonobj1.put("text", "请选择");
			
			//查询用户当前选择的value代码值对应的级联code值（然后设置到 请选择的 自定义属性中，方便页面调用）
			if (EmptyUtils.isNotEmpty(value)) {
				String sql="select code from cs_value where typeid='"+typeid+"' and value='"+value+"'";
				String selectCode=daoCtl.getStrRowValue(dao, Sqls.create(sql));
				JSONObject attrobj = new JSONObject();
				attrobj.put("codeValue", selectCode);//自定义属性保存级联code值
				jsonobj1.put("attributes",attrobj);
			}else{
				jsonobj1.put("attributes","");
			}
			
			array.add(jsonobj1);
		} else {
			cri.where().and("typeid", "=", typeid).and("code", "like", code + "____");
		}
		cri.where().and("state", "=", 0);
		cri.getOrderBy().asc("location");
		List<Cs_value> list = daoCtl.list(dao, Cs_value.class, cri);
		
		for (int i = 0; i < list.size(); i++) {
			Cs_value res = list.get(i);
			JSONObject jsonobj = new JSONObject();
			
			jsonobj.put("id", res.getValue());
			jsonobj.put("text", res.getName());
			
			JSONObject attrobj = new JSONObject();
			attrobj.put("codeValue", res.getCode());//自定义属性保存级联code值
			jsonobj.put("attributes",attrobj);
			
			//查询子集数量
			int num = daoCtl.getRowCount(dao, Cs_value.class,Cnd.where("code", "like", res.getCode() + "____").and("typeid", "=", typeid));
			if (num > 0) {
				jsonobj.put("state", "closed");
				//jsonobj.put("children", getCombotree(res.getCode()));
			}
			array.add(jsonobj);
		}
		return array.toString();
	}
	
	/**
	 * 功能描述:异步查询combotree数据 ，id的值为字段code的值
	 *
	 * @author   2015年10月11日 下午8:40:50
	 * 
	 * @param typeid 配置所属分类id
	 * @param code 当前选择配置code
	 * @param value 当前选择的代码值
	 * @return
	 */
	@At
	@Ok("raw")
	public String listCombotTreeCode(@Param("code")String code,@Param("typeid") String typeid) {
		JSONArray array = new JSONArray();
		Criteria cri = Cnd.cri();
		if (EmptyUtils.isEmpty(code)) {
			cri.where().and("typeid", "=", typeid).and("length(code)", "=",4);
			JSONObject jsonobj1 = new JSONObject();
			jsonobj1.put("id", "");
			jsonobj1.put("text", "请选择");
			
			array.add(jsonobj1);
		} else {
			cri.where().and("typeid", "=", typeid).and("code", "like", code + "____");
		}
		cri.where().and("state", "=", 0);
		cri.getOrderBy().asc("location");
		List<Cs_value> list = daoCtl.list(dao, Cs_value.class, cri);
		
		for (int i = 0; i < list.size(); i++) {
			Cs_value res = list.get(i);
			JSONObject jsonobj = new JSONObject();
			
			jsonobj.put("id", res.getCode());
			jsonobj.put("text", res.getName());
			
			
			//查询子集数量
			int num = daoCtl.getRowCount(dao, Cs_value.class,Cnd.where("code", "like", res.getCode() + "____").and("typeid", "=", typeid));
			if (num > 0) {
				jsonobj.put("state", "closed");
				//jsonobj.put("children", getCombotree(res.getCode()));
			}
			array.add(jsonobj);
		}
		return array.toString();
	}
	
	/**异步验证代码值是否重复
	 *
	 * @author lht  2015年10月11日 下午8:40:50
	 * 
	 * @param typeid 配置所属分类id
	 * @param csvalue 代码值
	 * @return
	 */
	@At
	@Ok("raw")
	public String checkCsValue(@Param("typeid")String typeid, @Param("value")String csvalue) {
		String sql="select count(*) from cs_value where typeid='"+typeid+"' and value='"+csvalue+"'";
		int result=daoCtl.getIntRowValue(dao, Sqls.create(sql));
		if (result>0) {
			return "false";
		}
		return "true";
	}
	
	
	
	/**
	 * 功能描述:异步查询树形数据 ，id的值为字段code的值
	 *
	 * @author   2015年10月11日 下午8:40:50
	 * 
	 * @param typeid 配置所属分类id
	 * @param code 当前选择配置code
	 * @param value 当前选择的代码值
	 * @return
	 */
	@At
	@Ok("->:${obj}")
	public String toTree(HttpServletRequest req,HttpSession session, @Param("typeid") String typeid,@Param("code") String code,@Param("reqtype") int reqtype){
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		req.setAttribute("typeid", typeid);
		if (EmptyUtils.isNotEmpty(code)) {
			String sql="select code from cs_value where typeid='"+typeid+"' and value='"+code+"' and state=0";
			req.setAttribute("initCode", daoCtl.getStrRowValue(dao, Sqls.create(sql)));
		}
		
//		List<String> roleList=user.getRolelist();
//		if (roleList.contains(arg0)) {
//			
//		}
		String html="/private/global/csvalue_tree.html";
		/*if (reqtype==1) {
			html="/private/global/csvalue_treehj.html";//呼叫中心打开页面
		}else if(reqtype==3){
			html="/private/global/csvalue_treezhcxhj.html";//呼叫中综合查询页面
		}else if(reqtype==4){
			html="/private/global/csvalue_treezhcx.html";//督办中心综合查询页面
		}else if (reqtype==5) {
			html="/private/global/csvalue_treewl.html";//网络中心打开页面
		}else if (reqtype==6) {
			html="/private/global/csvalue_treezhcxwl.html";//网络中心综合查询页面
		}*/
		return html;
		
	}
	@At
	@Ok("raw")
	public String ajaxTree(@Param("id")String code,@Param("typeid") String typeid) {
		JSONArray array = new JSONArray();
		Criteria cri = Cnd.cri();
		
		if (EmptyUtils.isEmpty(code)) {
			cri.where().and("typeid", "=", typeid).and("length(code)", "=",4);
		} else {
			cri.where().and("typeid", "=", typeid).and("code", "like", code + "____");
		}
		cri.where().and("state", "=", 0);
		cri.getOrderBy().asc("location");
		
		List<Cs_value> list = daoCtl.list(dao, Cs_value.class, cri);
		
		if (EmptyUtils.isEmpty(code)) {
			for (int i = 0; i < list.size(); i++) {
				Cs_value res = list.get(i);
				JSONObject jsonobj = new JSONObject();
				
				jsonobj.put("id", res.getCode());
				jsonobj.put("pId", 0);
				jsonobj.put("name",  res.getName());
				jsonobj.put("value", res.getValue());
				jsonobj.put("target", "_self");
				jsonobj.put("icon", Globals.APP_BASE_NAME+ "/images/icons/base.png");
//				查询子集数量
				int num = daoCtl.getRowCount(dao, Cs_value.class,Cnd.where("code", "like", res.getCode() + "____").and("typeid", "=", typeid));
				if (num > 0) {
					jsonobj.put("isParent", true);
				}else{
					jsonobj.put("isParent", false);
				}
				array.add(jsonobj);
			}
		}else{
			for (int i = 0; i < list.size(); i++) {
				Cs_value res = list.get(i);
				JSONObject jsonobj = new JSONObject();
				
				jsonobj.put("id", res.getCode());
				jsonobj.put("pId", res.getCode().substring(0,(res.getCode().length()- 4)));
				jsonobj.put("name",  res.getName());
				jsonobj.put("value", res.getValue());
				jsonobj.put("target", "_self");
//				jsonobj.put("icon", Globals.APP_BASE_NAME+ "/images/icons/base.png");
//				查询子集数量
				int num = daoCtl.getRowCount(dao, Cs_value.class,Cnd.where("code", "like", res.getCode() + "____").and("typeid", "=", typeid));
				if (num > 0) {
					jsonobj.put("isParent", true);
				}else{
					jsonobj.put("isParent", false);
				}
				array.add(jsonobj);
			}
		}
		
		return array.toString();
	}

	/**异步查询指定分类的信息
	 *
	 * @author lht  2015年10月11日 下午8:40:50
	 * 
	 * @param typeid 配置所属分类id
	 * @param csvalue 代码值
	 * @return
	 */
	@At
	@Ok("json")
	public List<Cs_value> getAjaxCsValue(HttpSession session,@Param("typeid") String typeid,@Param("cascade") int cascade) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Sql sql=null;
		String sqlstr="select * from Cs_value where state=0 and ";
		if("00020015".equals(typeid)&&"340000".equals(user.getXzqh())){//实施地质勘查项目类型
			sqlstr+=" value in (select bi.ht_type from breach_info bi,user_breach ub where bi.id=ub.breachid and ub.loginname='"+user.getLoginname()+"') and ";
		}
		sqlstr+=" typeid='"+typeid+"' ";
		if (cascade==1) {
			sqlstr+="  and length(code)=4 ";
		}
		sqlstr+=" order by location asc,id asc";
		sql=Sqls.create(sqlstr);
		List<Cs_value> list=daoCtl.list(dao, Cs_value.class, sql);
		return list;
	}
	
	/**异步查询级联信息
	 *
	 * @author lht  2015年10月11日 下午8:40:50
	 * 
	 * @param typeid 配置所属分类id
	 * @param csvalue 代码值
	 * @return
	 */
	@At
	@Ok("json")
	public List<Cs_value> getAjaxCascadeCsValue(@Param("typeid") String typeid,@Param("value") String value) {
		List<Cs_value> list =new ArrayList<Cs_value>();
		Sql sql=Sqls.create("select * from Cs_value where typeid=@typeid and value=@value");
		sql.params().set("typeid", typeid);
		sql.params().set("value", value);
		Cs_value csvalue= daoCtl.detailBySql(dao, Cs_value.class, sql);
		
		if (EmptyUtils.isNotEmpty(csvalue)) {
			Sql cascadesql=Sqls.create("select * from Cs_value where typeid =@typeid and code like '"+csvalue.getCode()+"____' order by location asc,id asc");
			cascadesql.params().set("typeid", typeid);
			list=daoCtl.list(dao, Cs_value.class, cascadesql);
			
		}
		return list;
	}
	
	/**听过typeid和value值查询一条数据库信息
	 *
	 * @author lht  2015年10月11日 下午8:40:50
	 * 
	 * @param typeid 配置所属分类id
	 * @param csvalue 代码值
	 * @return
	 */
	@At
	@Ok("json")
	public Cs_value getAjaxSelectedValue(@Param("typeid") String typeid,@Param("value") String value) {
		Sql sql=Sqls.create("select * from Cs_value where typeid=@typeid and value=@value");
		sql.params().set("typeid", typeid);
		sql.params().set("value", value);
		Cs_value csvalue= daoCtl.detailBySql(dao, Cs_value.class, sql);
		
		return csvalue;
	}
	
	/**通过typeid和code值查询一条数据库信息
	 *
	 * @author lht  2015年10月11日 下午8:40:50
	 * 
	 * @param typeid 配置所属分类id
	 * @param csvalue 代码值
	 * @return
	 */
	@At
	@Ok("json")
	public Cs_value getAjaxCodeValue(@Param("typeid") String typeid,@Param("code") String code) {
		Sql sql=Sqls.create("select * from Cs_value where typeid=@typeid and code=@code");
		sql.params().set("typeid", typeid);
		sql.params().set("code", code);
		Cs_value csvalue= daoCtl.detailBySql(dao, Cs_value.class, sql);
		
		return csvalue;
	}
}