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
@At("/private/zjgl/qtzz")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Zjgl_qtzzAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/zjgl/qtzz.html")
	public void user(HttpServletRequest req,HttpSession session) {
		System.out.println("In qtzz..");
	}
	
	//������֯��Ϣ�б�
	@At
	@Ok("raw")
	public String qtzzlist(){
		System.out.println("In qtzzlist...");
		JSONObject jsonobj = new JSONObject();
		JSONArray array = new JSONArray();
		jsonobj.put("a", "��֤ͨȯ�ɷ����޹�˾");
		jsonobj.put("b", "100020000001");
		jsonobj.put("c", "С��");
		jsonobj.put("d", "15209872248");
		jsonobj.put("id","1");
		JSONObject json = new JSONObject();
		json.put("a", "������Ӯ�ϿƼ��ɷ����޹�˾");
		json.put("b", "2000100000003");
		json.put("c", "Сǿ");
		json.put("d", "15209872248");
		array.add(jsonobj);
		array.add(json);
		return array.toString();
	}
	
	@At
	@Ok("->:/private/zjgl/qtzzadd.html")
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
	@Ok("->:/private/zjgl/qtzzupdate.html")
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
	
	//���������֯��Ϣ
	@At
	@Ok("->:/private/zjgl/qtzzDetail.html")
	public void detail(){
		System.out.println("In detail..");
	}
}
