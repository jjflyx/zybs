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
@At("/private/zjgl/sxcj")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Zjgl_sxjcAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/zjgl/sxcj.html")
	public void user(HttpServletRequest req,HttpSession session) {
		System.out.println("In sxcj..");
	}
	
	//失信惩戒信息
	@At
	@Ok("raw")
	public String sxcjlist(){
		System.out.println("In sxcjlist...");
		JSONObject jsonobj = new JSONObject();
		JSONArray array = new JSONArray();
		jsonobj.put("a", "安徽华力矿业有限公司");
		jsonobj.put("b", "法人");
		jsonobj.put("c", "300010001000");
		jsonobj.put("d", "未依法履行土地复垦义务");
		jsonobj.put("e", "土地开发合同");
		jsonobj.put("f", "2016-01-01 至 2017-01-01");
		jsonobj.put("g", "2016-01-01");
		jsonobj.put("h", "人工录入");
		jsonobj.put("i", "0");
		jsonobj.put("k", "惩戒中");
		jsonobj.put("id","1");
		JSONObject json = new JSONObject();
		json.put("a", "安徽冶金矿山企业联营公司");
		json.put("b", "法人");
		json.put("c", "200010001000");
		json.put("d", "未依法履行土地复垦义务");
		json.put("e", "土地开发合同");
		json.put("f", "2015-02-01 至 2019-02-01");
		json.put("g", "2015-01-01");
		json.put("h", "人工录入");
		json.put("k", "惩戒中");
		json.put("i", "0");
		JSONObject json1 = new JSONObject();
		json1.put("a", "安徽省凤阳县宁江矿业开发有限公司");
		json1.put("b", "法人");
		json1.put("c", "200010001000");
		json1.put("d", "未依法履行土地复垦义务");
		json1.put("e", "土地开发合同");
		json1.put("f", "2015-02-01 至 2019-02-01");
		json1.put("g", "2015-01-01");
		json1.put("h", "自动生成");
		json1.put("k", "惩戒结束");
		json1.put("i", "1");
		array.add(jsonobj);
		array.add(json);
		array.add(json1);
		return array.toString();
	}
	
	@At
	@Ok("->:/private/zjgl/sxcjadd.html")
	public void toadd(){
		
	}
	
	//失信惩戒信息添加
	@At
	@Ok("raw")
	public String add(){
		return "true";
	}
	
	//失惩添加合同信息
	@At
	@Ok("->:/private/zjgl/sxcj_addht.html")
	public void addht(){
		System.out.println("In addfr...");
	}
	
	//失信惩戒信息修改
	@At
	@Ok("->:/private/zjgl/sxcjupdate.html")
	public void toUpdate(){
		System.out.println("In toUpdate...");
	}
	
	//失信惩戒信息修改
	@At
	@Ok("raw")
	public String updateSave(){
		return "1";
	}
	
	//失信惩戒信息浏览
	@At
	@Ok("->:/private/zjgl/sxcjDetail.html")
	public void detail(){
		
	}
	
	//失信惩戒信息撤销
	@At
	@Ok("->:/private/zjgl/sxcjchexiao.html")
	public void toChexiao(){
		
	}
}
