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
@At("/private/zjgl/fr")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Zjgl_frAction extends BaseAction{
	@Inject
	protected Dao dao;

	@At("")
	@Ok("->:/private/zjgl/fr.html")
	public void user(HttpServletRequest req,HttpSession session) {
		System.out.println("进入法人页面");
	}
	
	//法人信息
	@At
	@Ok("raw")
	public String frlist(){
		System.out.println("进入法人查询方法");
		JSONObject jsonobj = new JSONObject();
		JSONArray array = new JSONArray();
		jsonobj.put("a", "安徽冶金矿山企业联营公司");
		jsonobj.put("b", "91510421MA6210013T");
		jsonobj.put("c", "吊销");
		jsonobj.put("d", "吴福形");
		jsonobj.put("e", "340111111");
		jsonobj.put("f", "安徽省合肥市");
		jsonobj.put("h", "2016-01-01");
		jsonobj.put("id", "1");
		jsonobj.put("g", "0");
		JSONObject json = new JSONObject();
		json.put("a", "安徽省凤阳县宁江矿业开发有限公司");
		json.put("b", "81321421MB3450013N");
		json.put("c", "吊销");
		json.put("d", "李庆安");
		json.put("e", "340111111");
		json.put("f", "安徽省合肥市");
		json.put("h", "2016-01-01");
		json.put("id", "2");
		json.put("g", "0");
		JSONObject json1 = new JSONObject();
		json1.put("a", "安徽华力矿业有限公司");
		json1.put("b", "24683279WC3450013K");
		json1.put("c", "正常在业");
		json1.put("d", "杨续喜");
		json1.put("e", "340111111");
		json1.put("f", "安徽省合肥市");
		json1.put("h", "2016-01-01");
		json1.put("id", "2");
		json1.put("g", "1");
		array.add(jsonobj);
		array.add(json);
		array.add(json1);
		return array.toString();
	}
	
	@At
	@Ok("->:/private/zjgl/fradd.html")
	public void toadd(){
		
	}
	
	//法人信息添加
	@At
	@Ok("raw")
	public String add(){
		return "true";
	}
	
	//法人信息修改
	@At
	@Ok("->:/private/zjgl/frupdate.html")
	public void toUpdate(){
		
	}
	
	//保存法人信息修改
	@At
	@Ok("raw")
	public String updateSave(){
		return "1";
	}
	
	//删除法人信息
	@At
	@Ok("raw")
	public String del(){
		return "true";
	}
	
	//浏览页面
	@At
	@Ok("->:/private/zjgl/frDetail.html")
	public void detail(){
		System.out.println("In detail..");
	}
}
