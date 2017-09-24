package com.hits.modules.zjgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;

@IocBean
@At("/private/zjgl/xyda")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Xyxx_xydaAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/zjgl/xyda.html")
	public void user(HttpServletRequest req,HttpSession session) {
		System.out.println("In xyda..");
	}
	
	//信用档案信息列表
	@At
	@Ok("raw")
	public String xydalist(){
		System.out.println("In xydalist...");
		JSONObject jsonobj = new JSONObject();
		JSONArray array = new JSONArray();
		jsonobj.put("a", "安徽省土地管理局劳动服务公司机电产品经销部");
		jsonobj.put("b", "91510421MA6210013T");
		jsonobj.put("c", "小明;小强");
		jsonobj.put("d", "法人");
		jsonobj.put("g", "惩戒中");
		jsonobj.put("h", "内网公布");
		jsonobj.put("i", "惩戒中");
		jsonobj.put("id","1");
		jsonobj.put("j", "2015-01-01 至 2017-01-01");
		JSONObject json = new JSONObject();
		json.put("a", "李天明");
		json.put("b", "81321421MB3450013N");
		json.put("c", "小强");
		json.put("d", "自然人");
		json.put("g", "处理中");
		json.put("h", "内网公布");
		json.put("i", "惩戒中");
		json.put("j", "2015-01-01 至 2017-01-01");
		JSONObject json1 = new JSONObject();
		json1.put("a", "安徽省土地管理局劳动服务公司蓝地物资服务部");
		json1.put("b", "24683279WC3450013K");
		json1.put("c", "小红");
		json1.put("d", "法人");
		json1.put("g", "惩戒中");
		json1.put("h", "外网公布");
		json1.put("i", "惩戒结束");
		json1.put("j", "2015-01-01 至 2017-01-01");
		array.add(jsonobj);
		array.add(json);
		array.add(json1);
		return array.toString();
	}
	//信用档案信息列表
	@At
	@Ok("->:/private/zjgl/xyda_sx.html")
	public void checkSx(){
		System.out.println("In checkSx...");
	}
	
	//信用档案信息列表
	@At
	@Ok("->:/private/zjgl/xyda_jl.html")
	public void checkJl(){
		System.out.println("In checkJl..");
	}
}
