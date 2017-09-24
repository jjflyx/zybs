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
		System.out.println("���뷨��ҳ��");
	}
	
	//������Ϣ
	@At
	@Ok("raw")
	public String frlist(){
		System.out.println("���뷨�˲�ѯ����");
		JSONObject jsonobj = new JSONObject();
		JSONArray array = new JSONArray();
		jsonobj.put("a", "����ұ���ɽ��ҵ��Ӫ��˾");
		jsonobj.put("b", "91510421MA6210013T");
		jsonobj.put("c", "����");
		jsonobj.put("d", "�⸣��");
		jsonobj.put("e", "340111111");
		jsonobj.put("f", "����ʡ�Ϸ���");
		jsonobj.put("h", "2016-01-01");
		jsonobj.put("id", "1");
		jsonobj.put("g", "0");
		JSONObject json = new JSONObject();
		json.put("a", "����ʡ������������ҵ�������޹�˾");
		json.put("b", "81321421MB3450013N");
		json.put("c", "����");
		json.put("d", "���찲");
		json.put("e", "340111111");
		json.put("f", "����ʡ�Ϸ���");
		json.put("h", "2016-01-01");
		json.put("id", "2");
		json.put("g", "0");
		JSONObject json1 = new JSONObject();
		json1.put("a", "���ջ�����ҵ���޹�˾");
		json1.put("b", "24683279WC3450013K");
		json1.put("c", "������ҵ");
		json1.put("d", "����ϲ");
		json1.put("e", "340111111");
		json1.put("f", "����ʡ�Ϸ���");
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
	
	//������Ϣ���
	@At
	@Ok("raw")
	public String add(){
		return "true";
	}
	
	//������Ϣ�޸�
	@At
	@Ok("->:/private/zjgl/frupdate.html")
	public void toUpdate(){
		
	}
	
	//���淨����Ϣ�޸�
	@At
	@Ok("raw")
	public String updateSave(){
		return "1";
	}
	
	//ɾ��������Ϣ
	@At
	@Ok("raw")
	public String del(){
		return "true";
	}
	
	//���ҳ��
	@At
	@Ok("->:/private/zjgl/frDetail.html")
	public void detail(){
		System.out.println("In detail..");
	}
}
