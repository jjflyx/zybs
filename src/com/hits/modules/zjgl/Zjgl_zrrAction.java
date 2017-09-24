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
@At("/private/zjgl/zrr")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Zjgl_zrrAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/zjgl/zrr.html")
	public void user(HttpServletRequest req,HttpSession session) {
		System.out.println("In zrr..");
	}
	
	//自然人信息列表
	@At
	@Ok("raw")
	public String zrrlist(){
		System.out.println("In zrrlist...");
		JSONObject jsonobj = new JSONObject();
		JSONArray array = new JSONArray();
		jsonobj.put("a", "王仁付");
		jsonobj.put("b", "340111188102139775");
		jsonobj.put("c", "15209872248");
		jsonobj.put("id","1");
		JSONObject json = new JSONObject();
		json.put("a", "李天明");
		json.put("b", "340234188102237775");
		json.put("c", "15209872248");
		JSONObject json1 = new JSONObject();
		json1.put("a", "王小强");
		json1.put("b", "340234188102237775");
		json1.put("c", "15209872248");
		array.add(jsonobj);
		array.add(json);
		array.add(json1);
		return array.toString();
	}
	
	@At
	@Ok("->:/private/zjgl/zrradd.html")
	public void toadd(){
		
	}
	
	//自然人信息添加
	@At
	@Ok("raw")
	public String add(){
		return "true";
	}
	
	//自然人信息修改
	@At
	@Ok("->:/private/zjgl/zrrupdate.html")
	public void toUpdate(){
		
	}
	
	//保存自然人信息修改
	@At
	@Ok("raw")
	public String updateSave(){
		return "1";
	}
	
	//删除自然人信息
	@At
	@Ok("raw")
	public String del(){
		return "true";
	}
	
	//浏览自人人信息
	@At
	@Ok("->:/private/zjgl/zrrDetail.html")
	public void detail(){
		
	}
}
