package com.hits.modules.zjgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@IocBean
@At("/private/zjgl/yjxx")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Xzcf_yjxxAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/zjgl/xzcf_yjxx.html")
	public void user(HttpServletRequest req,HttpSession session) {
		System.out.println("In xzcf_yjxx..");
	}
	
	//信用档案信息列表
		@At
		@Ok("raw")
		public String yjxxlist(){
			System.out.println("In yjxxlist...");
			JSONObject jsonobj = new JSONObject();
			JSONArray array = new JSONArray();
			jsonobj.put("img", "red");
			jsonobj.put("a", "合肥土地劳动服务公司蓝地物资服务部");
			jsonobj.put("d", "肥东县土地整改合同");
			jsonobj.put("e", "未履行处罚决定书");
			jsonobj.put("f", "2018-01-01");
			jsonobj.put("g", "2016-01-01");
			jsonobj.put("ee", "人工录入");
			JSONObject json1 = new JSONObject();
			json1.put("img", "yellow");
			json1.put("a", "安徽省凤阳县宁江矿业开发有限公司");
			json1.put("d", "肥东县土地整改合同");
			json1.put("e", "未履行处罚决定书");
			json1.put("f", "2018-01-01");
			json1.put("g", "2016-01-01");
			json1.put("ee", "人工录入");
			JSONObject json = new JSONObject();
			json.put("img", "green");
			json.put("a", "李天明");
			json.put("d", "肥东县土地整改合同");
			json.put("e", "未履行处罚决定书");
			json.put("f", "2018-01-01");
			json.put("g", "2016-01-01");
			json.put("ee", "人工录入");
			array.add(jsonobj);
			array.add(json1);
			array.add(json);
			return array.toString();
		}
		
		@At()
		@Ok("->:/private/zjgl/xzcf_yjxxAdd.html")
		public void check(){
			System.out.println("In check..");
		}
		
		@At()
		@Ok("->:/private/zjgl/xzcf_yjxxUpdate.html")
		public void gogo(){
			System.out.println("In gogo..");
		}
}
