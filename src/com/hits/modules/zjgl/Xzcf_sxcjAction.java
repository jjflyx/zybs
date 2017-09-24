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
	
	//ʧ�ųͽ���Ϣ
		@At
		@Ok("raw")
		public String sxcjlist(){
			System.out.println("In sxcjlist...");
			JSONObject jsonobj = new JSONObject();
			JSONArray array = new JSONArray();
			jsonobj.put("a", "�Ϸ������Ͷ�����˾�������ʷ���");
			jsonobj.put("b", "����");
			jsonobj.put("c", "�����������ؽ����ͬ");
			jsonobj.put("d", "δ������׼��;������������");
			jsonobj.put("e", "2015-12-12");
			jsonobj.put("f", "����ʡ������Դ��");
			jsonobj.put("g", "0");
			jsonobj.put("h", "�˹�¼��");
			jsonobj.put("i", "0");
			jsonobj.put("k", "�ͽ���");
			jsonobj.put("id","1");
			JSONObject json = new JSONObject();
			json.put("a", "����ʡ���ع�����Ͷ�����˾�������ʷ���");
			json.put("b", "����");
			json.put("c", "������˾���ؽ����ͬ");
			json.put("d", "δ����ת�ý����õ�ʹ��Ȩ�涨");
			json.put("e", "2016-01-01");
			json.put("f", "����ʡ������Դ��");
			json.put("g", "0");
			json.put("h", "�˹�¼��");
			json.put("k", "�ͽ���");
			json.put("i", "0");
			JSONObject json1 = new JSONObject();
			json1.put("a", "������");
			json1.put("b", "��Ȼ��");
			json1.put("c", "˽�����ؽ����ͬ");
			json1.put("d", "δ����ת�ý����õ�ʹ��Ȩ�涨");
			json1.put("e", "2016-02-21");
			json1.put("f", "����ʡ������Դ��");
			json1.put("g", "1");
			json1.put("h", "�Զ�����");
			json1.put("k", "�ͽ����");
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
		
		//ʧ�ųͽ���Ϣ�޸�
		@At
		@Ok("->:/private/zjgl/xzcf_sxcjupdate.html")
		public void toUpdate(){
			System.out.println("In xzcj toUpdate...");
		}
		
		//����������Ϣ���
		@At
		@Ok("->:/private/zjgl/xzcf_detail.html")
		public void detail(){
			System.out.println("In xzcf detail...");
		}
}
