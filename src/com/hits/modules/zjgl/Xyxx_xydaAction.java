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
	
	//���õ�����Ϣ�б�
	@At
	@Ok("raw")
	public String xydalist(){
		System.out.println("In xydalist...");
		JSONObject jsonobj = new JSONObject();
		JSONArray array = new JSONArray();
		jsonobj.put("a", "����ʡ���ع�����Ͷ�����˾�����Ʒ������");
		jsonobj.put("b", "91510421MA6210013T");
		jsonobj.put("c", "С��;Сǿ");
		jsonobj.put("d", "����");
		jsonobj.put("g", "�ͽ���");
		jsonobj.put("h", "��������");
		jsonobj.put("i", "�ͽ���");
		jsonobj.put("id","1");
		jsonobj.put("j", "2015-01-01 �� 2017-01-01");
		JSONObject json = new JSONObject();
		json.put("a", "������");
		json.put("b", "81321421MB3450013N");
		json.put("c", "Сǿ");
		json.put("d", "��Ȼ��");
		json.put("g", "������");
		json.put("h", "��������");
		json.put("i", "�ͽ���");
		json.put("j", "2015-01-01 �� 2017-01-01");
		JSONObject json1 = new JSONObject();
		json1.put("a", "����ʡ���ع�����Ͷ�����˾�������ʷ���");
		json1.put("b", "24683279WC3450013K");
		json1.put("c", "С��");
		json1.put("d", "����");
		json1.put("g", "�ͽ���");
		json1.put("h", "��������");
		json1.put("i", "�ͽ����");
		json1.put("j", "2015-01-01 �� 2017-01-01");
		array.add(jsonobj);
		array.add(json);
		array.add(json1);
		return array.toString();
	}
	//���õ�����Ϣ�б�
	@At
	@Ok("->:/private/zjgl/xyda_sx.html")
	public void checkSx(){
		System.out.println("In checkSx...");
	}
	
	//���õ�����Ϣ�б�
	@At
	@Ok("->:/private/zjgl/xyda_jl.html")
	public void checkJl(){
		System.out.println("In checkJl..");
	}
}
