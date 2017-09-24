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
@At("/private/zjgl/jcfw")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Jcfw_sxcjAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/zjgl/jcfw.html")
	public void user(HttpServletRequest req,HttpSession session) {
		System.out.println("In jcfw..");
	}
	
	//ʧ�ųͽ���Ϣ
		@At
		@Ok("raw")
		public String sxcjlist(){
			System.out.println("In sxcjlist...");
			JSONObject jsonobj = new JSONObject();
			JSONArray array = new JSONArray();
			jsonobj.put("a", "���ջ�����ҵ���޹�˾");
			jsonobj.put("b", "����");
			jsonobj.put("c", "300010001000");
			jsonobj.put("d", "δ���ռ���ͬҪ���ṩ���ɹ�");
			jsonobj.put("e", "���ؿ�����ͬ");
			jsonobj.put("f", "2016-01-01 �� 2017-01-01");
			jsonobj.put("g", "2016-01-01");
			jsonobj.put("h", "�˹�¼��");
			jsonobj.put("i", "0");
			jsonobj.put("k", "�ͽ���");
			jsonobj.put("id","1");
			JSONObject json = new JSONObject();
			json.put("a", "����ұ���ɽ��ҵ��Ӫ��˾");
			json.put("b", "����");
			json.put("c", "200010001000");
			json.put("d", "Υ�����ܹ涨");
			json.put("e", "���ؿ�����ͬ");
			json.put("f", "2015-02-01 �� 2019-02-01");
			json.put("g", "2015-01-01");
			json.put("h", "�Զ�����");
			json.put("k", "�ͽ���");
			json.put("i", "0");
			JSONObject json1 = new JSONObject();
			json1.put("a", "����ʡ������������ҵ�������޹�˾");
			json1.put("b", "����");
			json1.put("c", "200010001000");
			json1.put("d", "Υ�����ܹ涨");
			json1.put("e", "���ؿ�����ͬ");
			json1.put("f", "2015-02-01 �� 2019-02-01");
			json1.put("g", "2015-01-01");
			json1.put("h", "�Զ�����");
			json1.put("k", "�ͽ����");
			json1.put("i", "1");
			array.add(jsonobj);
			array.add(json);
			array.add(json1);
			return array.toString();
		}
	
	@At
	@Ok("->:/private/zjgl/jcfwSxcjadd.html")
	public void toadd(){
		
	}
	
	//ʧ�ųͽ���Ϣ�޸�
	@At
	@Ok("->:/private/zjgl/jcfwSxcjupdate.html")
	public void toUpdate(){
		System.out.println("In toUpdate...");
	}
	
	//ʧ�ųͽ���Ϣ����
	@At
	@Ok("->:/private/zjgl/jcfwSxcjchexiao.html")
	public void toChexiao(){
		
	}
}
