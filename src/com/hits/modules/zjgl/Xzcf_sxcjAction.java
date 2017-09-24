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
@At("/private/xzcj/sxcj")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Xzcf_sxcjAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/zjgl/xzcf_sxcj.html")
	public void user(HttpServletRequest req,HttpSession session) {
		System.out.println("In xzcf..");
	}
	
	//失信惩戒信息
		@At
		@Ok("raw")
		public String sxcjlist(){
			System.out.println("In sxcjlist...");
			JSONObject jsonobj = new JSONObject();
			JSONArray array = new JSONArray();
			jsonobj.put("a", "合肥土地劳动服务公司蓝地物资服务部");
			jsonobj.put("b", "法人");
			jsonobj.put("c", "鼎力集团土地建设合同");
			jsonobj.put("d", "未按照批准用途开发利用土地");
			jsonobj.put("e", "2015-12-12");
			jsonobj.put("f", "安徽省国土资源厅");
			jsonobj.put("g", "0");
			jsonobj.put("h", "人工录入");
			jsonobj.put("i", "0");
			jsonobj.put("k", "惩戒中");
			jsonobj.put("id","1");
			JSONObject json = new JSONObject();
			json.put("a", "安徽省土地管理局劳动服务公司蓝地物资服务部");
			json.put("b", "法人");
			json.put("c", "永力公司土地建设合同");
			json.put("d", "未遵守转让建设用地使用权规定");
			json.put("e", "2016-01-01");
			json.put("f", "安徽省国土资源厅");
			json.put("g", "0");
			json.put("h", "人工录入");
			json.put("k", "惩戒中");
			json.put("i", "0");
			JSONObject json1 = new JSONObject();
			json1.put("a", "李天明");
			json1.put("b", "自然人");
			json1.put("c", "私人土地建设合同");
			json1.put("d", "未遵守转让建设用地使用权规定");
			json1.put("e", "2016-02-21");
			json1.put("f", "安徽省国土资源厅");
			json1.put("g", "1");
			json1.put("h", "自动生成");
			json1.put("k", "惩戒结束");
			json1.put("i", "1");
			array.add(jsonobj);
			array.add(json);
			array.add(json1);
			return array.toString();
		}
		
		@At
		@Ok("->:/private/zjgl/xzcf_sxcjadd.html")
		public void toadd(){
			System.out.println("In xzcf-toadd...");
		}
		
		//失信惩戒信息修改
		@At
		@Ok("->:/private/zjgl/xzcf_sxcjupdate.html")
		public void toUpdate(){
			System.out.println("In xzcj toUpdate...");
		}
		
		//行政处罚信息浏览
		@At
		@Ok("->:/private/zjgl/xzcf_detail.html")
		public void detail(){
			System.out.println("In xzcf detail...");
		}
}
