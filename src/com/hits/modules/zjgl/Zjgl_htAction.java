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
import org.nutz.mvc.annotation.Param;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.StringUtil;

@IocBean
@At("/private/zjgl/ht")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Zjgl_htAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/zjgl/ht.html")
	public void user(HttpServletRequest req, HttpSession session) {
		System.out.println("进入合同页面");
	}
	
	// 法人信息
	@At
	@Ok("raw")
	public String htlist() {
		System.out.println("进入合同查询方法");
		JSONObject jsonobj = new JSONObject();
		JSONArray array = new JSONArray();
		jsonobj.put("a", "100020160102");
		jsonobj.put("b", "鼎力集团开采XX矿业合同");
		jsonobj.put("c", "煤矿");
		jsonobj.put("d", "安徽冶金矿山企业联营公司");
		jsonobj.put("e", "12.0000");
		jsonobj.put("f", "2016-01-13");
		jsonobj.put("g","0");
		jsonobj.put("id", "1");
		JSONObject json = new JSONObject();
		json.put("a", "200020160102");
		json.put("b", "永力公司开采XX矿业合同");
		json.put("c", "煤矿");
		json.put("d", "安徽华力矿业有限公司");
		json.put("e", "223.0980");
		json.put("f", "2016-01-01");
		json.put("g", "0");
		JSONObject json1 = new JSONObject();
		json1.put("a", "200020160102");
		json1.put("b", "永力公司开采XX矿业合同");
		json1.put("c", "铁矿石");
		json1.put("d", "李天明");
		json1.put("e", "223.0980");
		json1.put("f", "2016-01-01");
		json1.put("g", "1");
		array.add(jsonobj);
		array.add(json);
		array.add(json1);
		return array.toString();
	}
	
	// 法人信息
		@At
		@Ok("raw")
		public String htlist1() {
			System.out.println("进入合同查询方法");
			JSONObject jsonobj = new JSONObject();
			JSONArray array = new JSONArray();
			jsonobj.put("a", "100020160102");
			jsonobj.put("b", "鼎力集团开采XX矿业合同");
			jsonobj.put("c", "安徽省国土资源厅");
			jsonobj.put("d", "小明;小强");
			jsonobj.put("e", "12.0000");
			jsonobj.put("f", "2016-01-13");
			jsonobj.put("g","0");
			jsonobj.put("id", "1");
			JSONObject json = new JSONObject();
			json.put("a", "200020160102");
			json.put("b", "永力公司开采XX矿业合同");
			json.put("c", "安徽省国土资源厅");
			json.put("d", "小明;小强");
			json.put("e", "223.0980");
			json.put("f", "2016-01-01");
			json.put("g", "0");
			JSONObject json1 = new JSONObject();
			json1.put("a", "200020160102");
			json1.put("b", "永力公司开采XX矿业合同");
			json1.put("c", "安徽省国土资源厅");
			json1.put("d", "小明;小强");
			json1.put("e", "223.0980");
			json1.put("f", "2016-01-01");
			json1.put("g", "1");
			array.add(jsonobj);
			array.add(json);
			array.add(json1);
			return array.toString();
		}
	
	@At
	@Ok("->:/private/zjgl/htadd_ck.html")
	public void toadd(){
		
	}
	
	//合同信息添加
	@At
	@Ok("raw")
	public String add(){
		return "true";
	}
	//合同信息添加法人信息
	@At
	@Ok("->:/private/zjgl/ht_addfr.html")
	public void addfr(){
		System.out.println("In addfr...");
	}
	
	//合同信息修改
	@At
	@Ok("->:/private/zjgl/htupdate_ck.html")
	public void toUpdate(){
		System.out.println("In toUpdate...");
	}
	
	// 保存合同信息修改
	@At
	@Ok("raw")
	public String updateSave() {
		return "1";
	}
	
	// 浏览合同信息
	@At
	@Ok("->:/private/zjgl/htDetail.html")
	public void detail() {

	}
	
	// 添加法人信息
	@At
	@Ok("raw")
	public String toaddFr(HttpServletRequest req, @Param("checkids") String ids, @Param("a") String a) {
		System.out.println("In toaddFr..");
		System.out.println("ids:" + ids);
		System.out.println("a:" + a);
		String name = "";
		String[] id = StringUtil.null2String(ids).split(",");
		if(a.length() > 1){
			name = "宁江矿业开发 ; 冶金矿山企业";
		}else{
			name = "安徽冶金矿山企业联营公司";
		}
		/*for (int i = 0; i < id.length; i++) {
			System.out.println(id[i]);
		}*/
		return name;
	}
	
	@At
	@Ok("->:/private/zjgl/htbulu_ck.html")
	public void bulu(){
		System.out.println("In htbulu...");
	}
}
