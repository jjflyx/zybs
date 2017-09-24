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
@At("/private/zjgl/jlry")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Zjgl_jlryAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/zjgl/jlry.html")
	public void user(HttpServletRequest req,HttpSession session) {
		System.out.println("In jlry..");
	}
	
	//奖励荣誉信息
	@At
	@Ok("raw")
	public String jlrylist(){
		System.out.println("In jlrylist...");
		JSONObject jsonobj = new JSONObject();
		JSONArray array = new JSONArray();
		jsonobj.put("a", "合肥土地开发有限公司");
		jsonobj.put("b", "法人");
		jsonobj.put("c", "300010001000");
		jsonobj.put("d", "依法履行土地复垦义务");
		jsonobj.put("e", "2015-02-13");
		jsonobj.put("f", "0");
		jsonobj.put("id","1");
		JSONObject json = new JSONObject();
		json.put("a", "池州土地开发有限公司");
		json.put("b", "法人");
		json.put("c", "200010001000");
		json.put("d", "未依法履行土地复垦义务");
		json.put("e", "2016-01-01");
		json.put("f", "1");
		array.add(jsonobj);
		array.add(json);
		return array.toString();
	}
	@At
	@Ok("->:/private/zjgl/jlryadd.html")
	public void toadd(){
		
	}
	
	//奖励荣誉信息添加
	@At
	@Ok("raw")
	public String add(){
		return "true";
	}
	
	//奖励荣誉信息修改
	@At
	@Ok("->:/private/zjgl/jlryupdate.html")
	public void toUpdate(){
		System.out.println("In toUpdate...");
	}
	
	//保存奖励荣誉信息修改
	@At
	@Ok("raw")
	public String updateSave(){
		return "1";
	}
}
