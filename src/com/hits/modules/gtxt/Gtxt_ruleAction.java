package com.hits.modules.gtxt;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.hits.common.util.DateUtil;
import com.hits.modules.bean.Breach_info;
import com.hits.modules.bean.Cs_value;
import com.hits.modules.com.comUtil;
import com.hits.modules.form.bean.Form_field;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;
import com.hits.util.SysLogUtil;
import net.sf.json.JSONObject;

import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
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
import com.hits.modules.gtxt.bean.Gtxt_rule;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import java.util.List;
import java.util.Map;

/**
 * @author Numbgui
 * @time 2016-03-09 13:49:53
 * 
 */
@IocBean
@At("/private/gtxt/rule")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Gtxt_ruleAction extends BaseAction {
	@Inject
	protected Dao dao;
	public static String url="/private/gtxt/rule/view?id=@id";

	@At("")
	@Ok("->:/private/ratifyFollow/warnRuleList.html")
	public void gtxt_rule(HttpServletRequest req) {
		Gson gson = new Gson();
		Map<String,String> xylxMap = daoCtl.getHTable(dao, Sqls.create(" select code,name from CS_VALUE where typeid = '00010005' "));
		Map<String,String> sxxwMap = daoCtl.getHTable(dao,Sqls.create(" select id,sxxw from BREACH_INFO where sxxw is not null order by id "));
		Map<String,String> sxqxMap = daoCtl.getHTable(dao,Sqls.create(" select id,sxqx from BREACH_INFO where cjcs is not null order by id "));
		List<Cs_value> csValues = daoCtl.list(dao,Cs_value.class, Sqls.create("select * from CS_VALUE  where typeid = '00010005' order by code "));

		req.setAttribute("csValues", csValues);
		req.setAttribute("sxxwMap",JSONObject.fromObject(sxxwMap));
		req.setAttribute("sxqxMap",JSONObject.fromObject(sxqxMap));
		req.setAttribute("xylxMap",JSONObject.fromObject(xylxMap));
	}
	
	@At
	@Ok("->:/private/ratifyFollow/warnRule_Add.html")
	public void toAdd(HttpServletRequest req) {
		List<Cs_value> csValues = daoCtl.list(dao,Cs_value.class, Sqls.create("select * from CS_VALUE  where typeid = '00010005' order by code "));
		req.setAttribute("csValues", csValues);
	}

	@At
	@Ok("raw")
	public String getBreachInfoList(@Param("name")String name,@Param("id")String id,@Param("xylx")String xylx){
		Sql sql = null;
		if("sxxw".equals(name)) {
			sql = Sqls.create(" select id," + name + " as \"name\" from BREACH_INFO where is_use = 1  and xylx = '" + xylx + "' $s ");
		}else{
			sql = Sqls.create(" select id," + name + " as \"name\" from BREACH_INFO where is_use = 1 and rule = 0 and xylx = '" + xylx + "' $s ");
		}
		String s = "";
		if(EmptyUtils.isNotEmpty(id)){
			s += " AND id like '"+id+"%' ";
		}
		if("sxxw".equals(name)){
			s += " AND id like '____' ";
		}
		sql.vars().set("s", s);
		List<Map> sxxwMap = daoCtl.list(dao,sql);
		Gson gson = new Gson();
		String str = gson.toJson(sxxwMap);
		return str;
	}

	@At
	@Ok("raw")
	public String getTableList(@Param("xylx_id")String id){
		List<Map> tableMap = daoCtl.list(dao,Sqls.create("  select tableid,t.tablename as \"formtitle\" from form_def d,form_table t where t.tablekey like '%_yw' and d.table_type = 1 and d.defid = t.formdefid and d.xyml = '"+id+"' "));
		Gson gson = new Gson();
		String str = gson.toJson(tableMap);
		return str;
	}

	@At
	@Ok("raw")
	public String getTbaleOption(@Param("tableid")String id){
		List<Form_field> formFieldList = daoCtl.list(dao, Form_field.class,Cnd.where("tableid","=",id));
		Gson gson = new Gson();
		String str = gson.toJson(formFieldList);
		return str;
	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") Gtxt_rule gtxt_rule,HttpSession session) {
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		gtxt_rule.setAdd_time(DateUtil.getCurDateTime());
		gtxt_rule.setActor(user.getLoginname());
		gtxt_rule.setState(0);
		boolean flag = daoCtl.add(dao,gtxt_rule);
		if(flag){
			//rule==1 表示 改条失信行为已经绑定预警规则
			//daoCtl.update(dao, Breach_info.class, Chain.make("rule", 1), Cnd.where("id", "=", gtxt_rule.getSxqx_id()));
		}
		SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag, 1, 
				"添加预警规则【"+gtxt_rule.getRule_name()+"】", "预警规则", "", "", "",url.replace("@id",gtxt_rule.getId()));
		return flag;
	}

	@At
	@Ok("->:/private/ratifyFollow/warnRule_Detail.html")
	public void view(@Param("id") String id,HttpServletRequest req) {
		Gtxt_rule gtxtRule = daoCtl.detailByName(dao, Gtxt_rule.class, id);
		gtxtRule.setSxxw_id(getDiscreditInfoValue(dao, gtxtRule.getSxxw_id(), "sxxw"));
		gtxtRule.setSxqx_id(getDiscreditInfoValue(dao,gtxtRule.getSxqx_id(),"sxqx"));
		gtxtRule.setXy_type(comUtil.xyMap.get(gtxtRule.getXy_type()));
		String[] rule = gtxtRule.getRule().split("@@");
		String[] str = null;
 		for(int i =1; i < rule.length ;i++){
			System.out.println(" str : "+rule[i]);
			str = rule[i].split("☆☆");
			str[0] = getFieldTableString(dao,rule[0],str[0]);
			str[2] = getFieldTableString(dao,rule[0],str[2]);
			rule[i] = str[0] + " <span style='color:red;'>" + str[1] + "</span>" + str[2];
		}
		rule[0] = daoCtl.getStrRowValue(dao,Sqls.create(" select tablename from FORM_TABLE where tableid = '"+rule[0]+"' "));
		req.setAttribute("rule",rule);
		req.setAttribute("ruleInfo",gtxtRule);
	}
	
	@At
	@Ok("->:/private/ratifyFollow/warnRule_Update.html")
	public void toUpdate(@Param("id") String id, HttpServletRequest req) {
		Gtxt_rule gtxt_rule = daoCtl.detailByName(dao, Gtxt_rule.class, id);
		gtxt_rule.setSxxw_id(getDiscreditInfoValue(dao, gtxt_rule.getSxxw_id(), "sxxw"));
		gtxt_rule.setSxqx_id(getDiscreditInfoValue(dao,gtxt_rule.getSxqx_id(),"sxqx"));
		gtxt_rule.setXy_type(comUtil.xyMap.get(gtxt_rule.getXy_type()));
		String[] rule = gtxt_rule.getRule().split("@@");
		String[] str = null;
		for(int i =1; i < rule.length ;i++){
			System.out.println(" str : "+rule[i]);
			str = rule[i].split("☆☆");
			str[0] = getFieldTableString(dao,rule[0],str[0]);
			str[2] = getFieldTableString(dao,rule[0],str[2]);
			rule[i] = str[0] + " <span style='color:red;'>" + str[1] + "</span>" + str[2];
		}
		rule[0] = daoCtl.getStrRowValue(dao,Sqls.create(" select tablename from FORM_TABLE where tableid = '"+rule[0]+"' "));
		req.setAttribute("rule",rule);
		req.setAttribute("obj", gtxt_rule);
	}

	@At
	@Ok("raw")
	public boolean changeState(@Param("id")String id,@Param("type")Integer type){
		return daoCtl.update(dao, Gtxt_rule.class, Chain.make("is_work", type), Cnd.where("id", "=", id));
	}
	
	@At
	public boolean update(@Param("id")String id,@Param("rule_name") String rule_name,@Param("note")String note,
			@Param("sms_model")String sms_model,@Param("is_work")String is_work,@Param("cjqx")String cjqx,HttpSession session) {
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		boolean flag = daoCtl.update(dao, Gtxt_rule.class,
				Chain.make("rule_name", rule_name).add("note", note).add("sms_model", sms_model).add("is_work",is_work).add("cjqx",cjqx), Cnd.where("id", "=", id));
		SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag, 2, 
				"修改预警规则【"+rule_name+"】", "预警规则", "", "", "",url.replace("@id",id));
		return flag;
	}
	
	@At
	public boolean delete(@Param("id") final String id,HttpSession session) {
		final Sys_user user=(Sys_user) session.getAttribute("userSession");
		final ThreadLocal<Boolean> flag = new ThreadLocal<Boolean>();
		Trans.exec(new Atom() {
			@Override
			public void run() {
				flag.set(dao.update(Gtxt_rule.class, Chain.make("state", -1), Cnd.where("id", "=", id)) > 0 );
				Gtxt_rule gtxtRule = dao.fetch(Gtxt_rule.class,Cnd.where("id","=",id));
				if(flag.get()){
					// rule==0 表示预警规则已经解绑
					dao.update(Breach_info.class, Chain.make("rule", 0), Cnd.where("id", "=", gtxtRule.getSxqx_id()));
				}
				SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag.get(), 3, 
						"删除预警规则【"+ gtxtRule.getRule_name() +"】,ID："+id, "预警规则", "", "", "",url.replace("@id",id));
			}
		});

		return flag.get();
	}
	
	@At
	public boolean deleteIds(@Param("ids") String ids,HttpSession session) {
		final Sys_user user=(Sys_user) session.getAttribute("userSession");
		final String[] id = StringUtil.null2String(ids).split(",");
		final ThreadLocal<Boolean> flag = new ThreadLocal<Boolean>();
		flag.set(false);
		Trans.exec(new Atom() {
			@Override
			public void run() {
				flag.set(dao.update(Gtxt_rule.class, Chain.make("state", "-1"), Cnd.where("id", "IN", id)) > 0);
				if(flag.get()){
					Gtxt_rule gtxtRule = null;
					for(String idNumber : id){
						gtxtRule = dao.fetch(Gtxt_rule.class,Cnd.where("id", "=", idNumber));
						int sum = dao.update(Breach_info.class, Chain.make("rule", 0), Cnd.where("id", "=", gtxtRule.getSxqx_id()));
						SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, (sum > 0 ? true : false), 3, 
								"删除预警规则【"+ gtxtRule.getRule_name() +"】,ID："+id, "预警规则", "", "", "",url.replace("@id",idNumber));
					}
				}
			}
		});
		return flag.get();
	}
	
	@At
	@Ok("raw")
	public JSONObject list(@Param("xy_type")String xy_type,@Param("keyword")String keyword,
						   @Param("is_work")String is_work,@Param("page") int curPage, @Param("rows") int pageSize){
		Criteria cri = Cnd.cri();
		cri.where().and("state", "=", 0);
		if(EmptyUtils.isNotEmpty(keyword)){
			cri.where().andLike("rule_name", keyword.trim());
		}
		if(!"99".equals(is_work) && EmptyUtils.isNotEmpty(is_work)) {
			cri.where().and("is_work", "=", is_work);
		}
		if(!"0".equals(xy_type) && EmptyUtils.isNotEmpty(xy_type)){
			cri.where().and("xy_type","=",xy_type);
		}
		cri.getOrderBy().desc("is_work").desc("add_time");
		return daoCtl.listPageJson(dao, Gtxt_rule.class, curPage, pageSize, cri);
	}

	private static String getDiscreditInfoValue(Dao dao,String key,String type){
		return daoCtl.getStrRowValue(dao,Sqls.create(" select "+type+" from BREACH_INFO where id = '"+key+"' "));
	}

	private static String getFieldTableString(Dao dao,String table_id,String key_id){
		return daoCtl.getStrRowValue(dao,Sqls.create(" select fieldlabel||'['||fieldname||']' from FORM_FIELD where tableid = '"+table_id+"' and fieldname = '"+key_id+"' "));
	}
}