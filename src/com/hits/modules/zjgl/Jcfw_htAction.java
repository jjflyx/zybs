package com.hits.modules.zjgl;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
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
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;
import com.hits.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@IocBean
@At("/private/zjgl/jcfwht")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Jcfw_htAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/zjgl/jcfwht.html")
	public void user(HttpServletRequest req, HttpSession session) {
		System.out.println("����������ͬҳ��");
	}
	
	// ������Ϣ
		@At
		@Ok("raw")
		public String htlist() {
			System.out.println("�����ͬ��ѯ����");
			JSONObject jsonobj = new JSONObject();
			JSONArray array = new JSONArray();
			jsonobj.put("a", "100020160102");
			jsonobj.put("b", "���XX��ҵ��ͬ");
			jsonobj.put("c", "�й�����Դ����");
			jsonobj.put("d", "����ұ���ɽ��ҵ��Ӫ��˾");
			jsonobj.put("e", "12.0000");
			jsonobj.put("f", "2016-01-13");
			jsonobj.put("g","0");
			jsonobj.put("id", "1");
			JSONObject json = new JSONObject();
			json.put("a", "200020160102");
			json.put("b", "���XX��ɽ��ͬ");
			json.put("c", "ʡ������Դ����");
			json.put("d", "���ջ�����ҵ���޹�˾");
			json.put("e", "223.0980");
			json.put("f", "2016-01-01");
			json.put("g", "0");
			JSONObject json1 = new JSONObject();
			json1.put("a", "200020160102");
			json1.put("b", "��⿪��XX��ҵ��ͬ");
			json1.put("c", "ʡ������Դ����");
			json1.put("d", "������");
			json1.put("e", "223.0980");
			json1.put("f", "2016-01-01");
			json1.put("g", "1");
			array.add(jsonobj);
			array.add(json);
			array.add(json1);
			return array.toString();
		}
		
		@At
		@Ok("->:/private/zjgl/jcfwhtadd.html")
		public void toadd(){
			
		}
		
		@At
		@Ok("->:/private/zjgl/jcfwhtupdate.html")
		public void toUpdate(){
			
		}
		@At
		@Ok("->:/private/zjgl/jcfwhtDetail.html")
		public void detail(){
			
		}
	//Don't have anything..
		@At
		@Ok("")
		public void anyting(@Param("id") String id,@Param("name")String name,
				HttpServletRequest req,HttpSession session){
			id = StringUtil.null2String(id);
			name = StringUtil.null2String("name");
			Sys_user user = (Sys_user)session.getAttribute("userSession");
			String sql = "select * from sys_user where id in (select letterid from blj_info)";
			if(EmptyUtils.isEmpty(id)){
				sql += "and id like '%"+id+"%'";
			}
			ArrayList<Sys_user> userlist = new ArrayList<Sys_user>();
			userlist = (ArrayList<Sys_user>) daoCtl.list(dao, Sys_user.class, Sqls.create("select * from sys_user"));
		}
		
}
