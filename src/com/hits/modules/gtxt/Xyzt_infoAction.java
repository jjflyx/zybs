package com.hits.modules.gtxt;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.hits.common.util.*;
import com.hits.modules.bean.Breach_info;
import com.hits.modules.bean.Cs_value;
import com.hits.modules.bean.File_info;
import com.hits.modules.com.YWCL;
import com.hits.modules.com.comUtil;
import com.hits.modules.gtxt.bean.L_ckqht;
import com.hits.modules.gtxt.bean.Warnexception;
import com.hits.modules.sxcj.Discredit_infoAction;
import com.hits.modules.sxcj.bean.Discredit_info;
import com.hits.modules.sxcj.bean.Discredit_info_bf;
import com.hits.modules.sys.bean.Sys_log;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.*;
import com.hits.util.DateUtil;
import com.hits.util.StringUtil;

import net.sf.json.JSONObject;

import org.nutz.dao.*;
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
import com.hits.common.filter.SqlFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.modules.gtxt.bean.Xyzt_info;


/**
 * @author Numbgui
 * @time 2016-02-29 10:39:07
 *
 */
@IocBean
@At("/private/gtxt/xyzt")
@Filters({ @By(type = GlobalsFilter.class), @By(type = SqlFilter.class), @By(type = UserLoginFilter.class) })
public class Xyzt_infoAction extends BaseAction {
	@Inject
	protected Dao dao;
	public static String url="/private/gtxt/xyzt/view?id=@id";

	@At("")
	@Ok("->:/private/zjgl/xyztList.html")
	public void xyztIndex(@Param("type")String type,@Param("xylx")String xylx,HttpServletRequest req,HttpSession session) {
		req.setAttribute("type",type);
		req.setAttribute("xylx",xylx);
		req.setAttribute("qyzt", JSONObject.fromObject(comUtil.qyzt));
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		req.setAttribute("loginname", user.getLoginname());
		req.setAttribute("statusMap", JSONObject.fromObject(comUtil.statusMap));
	}


	@At
	@Ok("->:${obj}")
	public String toadd(@Param("type")int type,@Param("xylx")String xylx,HttpServletRequest req){
		String reName = "";
		switch (type) {
			case 0 :
				reName = "/private/zjgl/zrradd.html";
				break;
			case 1 :
				req.setAttribute("qyztList",comUtil.qyztList);
				req.setAttribute("qyzt", JSONObject.fromObject(comUtil.qyzt_nv));
				req.setAttribute("xylx",xylx);
				reName = "/private/zjgl/fradd.html";
				break;
			case 2 :
				reName = "/private/zjgl/qtzzadd.html";break;
			default:
				reName = "/private/zjgl/fradd.html";break;
		}
		return reName;
	}

	@At
	@Ok("raw")
	public boolean add(@Param("..") Xyzt_info xyzt_info,HttpSession session) {
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		String xy_type = "";
		String logcs="";
		xyzt_info.setActor(user.getLoginname());
		if("0".equals(xyzt_info.getType())){ //自然人
			xy_type = "自然人";
			logcs="zrr";
		}else if("1".equals(xyzt_info.getType())){ //法人
			if("-1".equals(xyzt_info.getState())){
				xyzt_info.setState("06");
			}
			xy_type = "法人";
			logcs="fr";
		}else{ // 其他组织
			xy_type = "其他组织";
			logcs="qtzz";
		}
		xyzt_info.setModetype(1);
		boolean flag = daoCtl.add(dao,xyzt_info);
		if(flag){
			comUtil.xyztMap.put(String.valueOf(xyzt_info.getId()),xyzt_info.getName()+"☆"+xyzt_info.getCode()+"☆"+xyzt_info.getZzjgdm());
		}
		String note=SysLogUtil.getLogNote(xyzt_info, logcs);
		SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type,flag,1,
				"添加"+xy_type+"信用主体【"+xyzt_info.getName()+"】","信用主体",note,"","",url.replace("@id", xyzt_info.getId()+""));
		return flag;
	}

	@At
	@Ok("->:${obj}")
	public String view(@Param("id") int id,@Param("type")int type,HttpServletRequest req) {
		Xyzt_info xyzt_info = daoCtl.detailById(dao, Xyzt_info.class, id);
		String reName = "";
		if(EmptyUtils.isNotEmpty(xyzt_info.getType())){
			type=StringUtil.StringToInt(xyzt_info.getType().trim());
		}
		switch (type) {
			case 0 :
				reName = "/private/zjgl/zrrDetail.html";break;
			case 1 :
				req.setAttribute("qyzt",comUtil.qyzt);
				req.setAttribute("qytype",comUtil.qytype);
				reName = "/private/zjgl/frDetail.html";
				break;
			case 2 :
				reName = "/private/zjgl/qtzzDetail.html";break;
			default:
				reName = "/private/zjgl/frDetail.html";break;
		}
		req.setAttribute("xyzt_info", xyzt_info);
		return reName;
	}

	@At
	@Ok("->:${obj}")
	public String toupdate(@Param("id") int id,@Param("type")int type,@Param("xylx")String xylx, HttpServletRequest req) {
		Xyzt_info xyzt_info = daoCtl.detailById(dao, Xyzt_info.class, id);
		String reName = "";
		if(EmptyUtils.isNotEmpty(xyzt_info.getType())){
			type=StringUtil.StringToInt(xyzt_info.getType().trim());
		}
		switch (type) {
			case 0 :
				reName = "/private/zjgl/zrrupdate.html";break;
			case 1 :
				req.setAttribute("qyztList",comUtil.qyztList);
				reName = "/private/zjgl/frupdate.html";
				break;
			case 2 :
				reName = "/private/zjgl/qtzzupdate.html";break;
			default:
				reName = "/private/zjgl/frupdate.html";break;
		}
		req.setAttribute("xyzt_info", xyzt_info);
		req.setAttribute("xylx", xylx);
		return reName;
	}

	@At
	public boolean update(@Param("..") Xyzt_info xyzt_info,HttpSession session) {
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		xyzt_info.setActor(user.getLoginname());
		String xy_type = "";
		String logcs="";
		if("0".equals(xyzt_info.getType())){ //自然人
			xy_type = "自然人";
			logcs="zrr";
		}else if("1".equals(xyzt_info.getType())){ //法人
			if("-1".equals(xyzt_info.getState())){
				xyzt_info.setState("06");
			}
			xy_type = "法人";
			logcs="fr";
		}else{ // 其他组织
			xy_type = "其他组织";
			logcs="qtzz";
		}
		Xyzt_info oldxyzt=daoCtl.detailById(dao, Xyzt_info.class, xyzt_info.getId());
		boolean flag = daoCtl.update(dao, xyzt_info);
		if(flag){
			comUtil.xyztMap.put(String.valueOf(xyzt_info.getId()),xyzt_info.getName()+"☆"+xyzt_info.getCode()+"☆"+xyzt_info.getZzjgdm());
		}
		String note=SysLogUtil.getLogNote(oldxyzt,xyzt_info, logcs);
		SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type,flag,2,
				"修改"+xy_type+"信用主体【"+xyzt_info.getName()+"】","信用主体",note,"","",url.replace("@id", xyzt_info.getId()+""));
		return flag;
	}

	@At("/getFrObj")
	@Ok("raw")
	public JSONObject getFrObj(@Param("key")String key){
		Gson gson = new Gson();
		Map<String,String> frMap = null;
		try {
			frMap = FrUtil.getFrObj(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return JSONObject.fromObject(frMap);
	}

	/**
	 *
	 * @param type 0-自然人、1-法人、2-其他组织
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@At
	@Ok("raw")
	public JSONObject list(@Param("type")int type,@Param("cx_code")String cx_code,@Param("cx_name")String cx_name,
						   @Param("page") int curPage, @Param("rows") int pageSize,HttpSession session){
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		Sql sql = null;
		switch (type){
			case 0 :
				sql = Sqls.create(" select id,name a,code b,phone c,modetype g,actor,status from Xyzt_info where type=0 $s ");break;
			case 1 :
				sql = Sqls.create(" select id,name a,code b,fr_name c,state d,modetype g,actor,status from Xyzt_info where type=1 $s ");break;
			case 2 :
				sql = Sqls.create(" select id,name a,code b,fzr c,phone d,modetype g,actor,status from Xyzt_info where type=2 $s ");break;
			default:
				sql = null;break;
		}
		String s = "";
		s += " and (actor='"+user.getLoginname()+"' or status=1)";
		if(!"".equals(StringUtil.null2String(cx_name))){
			s += " and name like '%"+cx_name+"%' ";
		}
		if(!"".equals(StringUtil.null2String(cx_code))){
			s += " and code like '%"+cx_code+"%' ";
		}
		s += " order by id desc ";
		sql.vars().set("s", s);
		return daoCtl.listPageJsonSql(dao, sql, curPage, pageSize);
	}

	/**
	 * 跳转异议管理页面
	 */
	@At
	@Ok("->:/private/ratifyFollow/warnExceptionList.html")
	public void warnExceptionPage(HttpServletRequest req,HttpSession session){
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		if(user.getRolelist().contains("506") || user.getRolelist().contains("507")) {
			req.setAttribute("leader","处理");
		}else{
			req.setAttribute("leader","<a href='javascript:update(\"+row.id+\",\\\"1\\\");'>处理</a>");
		}
		req.setAttribute("unitid",user.getUnitid());
		req.setAttribute("endTime",DateUtil.getToday());
		req.setAttribute("startTime", DateUtil.getFirstMonDay(DateUtil.getToday()));
	}

	/**
	 *  预警管理列表:储备中心可查看所有的异议申请，其他人仅可处理自己作出惩戒信息的相关异议
	 * @param start_date
	 * @param end_date
	 * @param keyword
	 * @param unitid
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@At
	@Ok("raw")
	public JSONObject warnExceptionList(@Param("start_date")String start_date,@Param("end_date")String end_date,@Param("keyword")String keyword,@Param("source")String source,
										@Param("unitid")String unitid,@Param("state")String state,@Param("page") int curPage, @Param("rows") int pageSize,HttpSession session){
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		Sql sql = null;
		if(user.getRolelist().contains("506") || user.getRolelist().contains("507")) {
			sql = Sqls.create(" select w.id,w.sxcj_id,w.xyzt_id,w.contract_id,w.applicant,w.add_time,w.note,w.code,w.state,w.contract_name,z.name xyzt_name,w.source,w.clsx from WARNEXCEPTION w,xyzt_info z where w.xyzt_id=z.id and w.state <> -1  $s ");
		}else {
			sql = Sqls.create(" select w.id,w.sxcj_id,w.xyzt_id,w.contract_id,w.applicant,w.add_time,w.note,w.code,w.state,w.contract_name,z.name xyzt_name,w.source,w.clsx from WARNEXCEPTION w " 
					+"left join xyzt_info z on w.xyzt_id=z.id "
					+"left join discredit_info d on d.id=w.sxcj_id "
					+"where w.state <> -1 and d.actor = '" + user.getLoginname() + "' $s ");//and w.username = '" + user.getLoginname() + "'
		}
		Warnexception a=null;
		String s ="";
		if(EmptyUtils.isNotEmpty(start_date)){
			s+=" AND w.add_time >= '"+start_date+" 00:00:00'";
		}
		if(EmptyUtils.isNotEmpty(end_date)){
			s+=" AND w.add_time <= '"+end_date+" 00:00:00'";
		}
		
		if(EmptyUtils.isNotEmpty(keyword)){
			s = s +  " AND w.applicant like '%"+keyword+"%' ";
		}

		if(EmptyUtils.isNotEmpty(source) && !"".equals(source)){
			s += " AND w.source = '"+source+"' ";
		}
		
		if(EmptyUtils.isNotEmpty(state) && !"".equals(state)){
			s += " AND w.state = '"+state+"' ";
		}
//		if(EmptyUtils.isNotEmpty(unitid)){
//			s += " and unit = '"+unitid+"' ";
//		}
		s = s + " order by w.state asc , w.add_time desc ";
		sql.vars().set("s", s);
		System.out.println(sql.toString());
		return daoCtl.listPageJsonSql(dao, sql, curPage, pageSize);
	}

	@At
	@Ok("->:/private/ratifyFollow/warnExceptionChoice.html")
	public void toWarnExceptionChoice(@Param("xyzt_id")Integer id,HttpServletRequest req){
		Gson gson = new Gson();
		Hashtable<String, String> zttype = daoCtl.getHTable(dao,  Sqls.create("select value,name from cs_value where typeid = 00010006"));
		Hashtable<String, String> cjtype = daoCtl.getHTable(dao,  Sqls.create(" select id,name from SYS_UNIT where id like '0008%' "));
		req.setAttribute("ztMap", JSONObject.fromObject(zttype));
		if(EmptyUtils.isNotEmpty(id)){
			Xyzt_info xyzt_info = daoCtl.detailById(dao,Xyzt_info.class,id);
			List<Discredit_info> discreditInfoList = discreditInfoList = daoCtl.list(dao, Discredit_info.class, Cnd.where("xyzt_id","=",id+"").and("type", "=", "0"));
			List<Breach_info> breach_infos = daoCtl.list(dao, Breach_info.class, Cnd.orderBy().asc("id"));
			Map<String,Breach_info> breach_infoMap = new HashMap<String,Breach_info>();
			for(Breach_info breach_info : breach_infos ){
				breach_infoMap.put(breach_info.getId(),breach_info);
			}
			Map<String,String> contractNameMap = new HashMap<String,String>();
			for(Discredit_info discredit_info : discreditInfoList){
				//判断是否涉及合同
				//合同信息
				if(EmptyUtils.isNotEmpty(discredit_info.getTablekey())){
					Sql sqlht = Sqls.create("select htbh,htmc from "+discredit_info.getTablekey()+" where htid = '"+discredit_info.getContract_id()+"' ");
					if("00030002".equals(discredit_info.getXy_type())||"l_tkqsyf".equals(discredit_info.getTablekey())){//采矿权或探矿权使用费
						String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(discredit_info.getTablekey())));
						String xkzhSql="select xkzh from "+discredit_info.getTablekey()+" where "+tablePK+" = '"+discredit_info.getContract_id()+"' ";
						sqlht=Sqls.create(xkzhSql);
					}else if("l_tkht".equals(discredit_info.getTablekey())){//探矿权合同
						String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(discredit_info.getTablekey())));
						String xkzhSql="select kcxkz from "+discredit_info.getTablekey()+" where "+tablePK+" = '"+discredit_info.getContract_id()+"' ";
						sqlht=Sqls.create(xkzhSql);
					}else if("l_tdfk".equals(discredit_info.getTablekey())){//探矿权合同
						String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(discredit_info.getTablekey())));
						String xkzhSql="select name from "+discredit_info.getTablekey()+" where "+tablePK+" = '"+discredit_info.getContract_id()+"' ";
						sqlht=Sqls.create(xkzhSql);
					}  
					contractNameMap.put(discredit_info.getContract_id(), daoCtl.getStrRowValue(dao, sqlht));
					System.out.println(contractNameMap.get(discredit_info.getContract_id()));
				}
				discredit_info.setSxxw_id(breach_infoMap.get(discredit_info.getSxxw_id()).getSxxw());
				System.out.println(discredit_info.getSxqx_id());
				if(EmptyUtils.isEmpty(discredit_info.getSxqx_id())){
					discredit_info.setSxqx_id(discredit_info.getSxqx_other());
				}else if(discredit_info.getSxqx_id().indexOf(",") > 0){

				}else{
					discredit_info.setSxqx_id(breach_infoMap.get(discredit_info.getSxqx_id()).getSxqx());
				}
				if(EmptyUtils.isEmpty(discredit_info.getCjcs_id())){
					discredit_info.setCjcs_id(discredit_info.getCjcs_other());
				}else if(discredit_info.getCjcs_id().indexOf(",") > 0){
					String[] str = discredit_info.getCjcs_id().split(",");
					String cjcs = "";
					for(String str_id : str){
						cjcs += breach_infoMap.get(str_id).getCjcs()+"\n";
					}
					discredit_info.setCjcs_id(cjcs);
				}else{
					discredit_info.setCjcs_id(breach_infoMap.get(discredit_info.getCjcs_id()+"").getCjcs());
				}
				if(EmptyUtils.isNotEmpty(discredit_info.getUnit())) {
					//惩戒机构替换为中文
					discredit_info.setUnit(StringUtil.isNull(cjtype.get(discredit_info.getUnit()),""));
				}
			}
			req.setAttribute("info",xyzt_info);
			req.setAttribute("size",discreditInfoList.size());
			req.setAttribute("discreditInfoList",discreditInfoList);
			req.setAttribute("contractNameMap",contractNameMap);
			req.setAttribute("type",1);
		}
	}

	/**
	 * 线下异议申请新增方法 by.Numbgui 2016-03-30
	 * @param id
	 * @param applicant
	 * @param tel
	 * @param note
	 * @param file_html
	 * @param code
	 * @return
	 */
	@At
	@Ok("raw")
	public Boolean addWarnException(@Param("id") String id,@Param("applicant") String applicant,@Param("tel") String tel,@Param("note") String note,
									@Param("file_html") String file_html,@Param("code")String code){
		boolean flag = false;
		file_html=Decode64Util.Decrypt(file_html);
		String[] strArr = id.split("_");
		Discredit_info discredit_info = daoCtl.detailByCnd(dao,Discredit_info.class,Cnd.where("id","=",Integer.parseInt(strArr[0])));
		Warnexception warnexceptionInfo = new Warnexception();
		warnexceptionInfo.setApplicant(applicant);
		warnexceptionInfo.setNote(note);
		warnexceptionInfo.setTel(tel);
		warnexceptionInfo.setFile_html(file_html);
		warnexceptionInfo.setSxcj_id(Integer.parseInt(strArr[0]));
		warnexceptionInfo.setCode(code);
		warnexceptionInfo.setUnit(discredit_info.getUnitid());
		warnexceptionInfo.setXzqh(daoCtl.getStrRowValue(dao,Sqls.create(" select xzqh from sys_unit where id = '"+discredit_info.getUnitid()+"' ")));
		warnexceptionInfo.setXyzt_id(discredit_info.getXyzt_id());
		warnexceptionInfo.setAdd_time(com.hits.util.DateUtil.getCurDateTime());
		warnexceptionInfo.setXy_type(strArr[1]);
		warnexceptionInfo.setUsername(discredit_info.getActor());
		String clsx=com.hits.util.DateUtil.getNexWeekDayByStr(com.hits.util.DateUtil.getNexWeekDayByStr(com.hits.util.DateUtil.getToday()));
		warnexceptionInfo.setClsx(clsx);
		// source == 1 表示为线下
		warnexceptionInfo.setSource(1);
		if(EmptyUtils.isNotEmpty(discredit_info.getContract_id())){
			warnexceptionInfo.setContract_id(discredit_info.getContract_id());
			warnexceptionInfo.setContract_name(daoCtl.getStrRowValue(dao,Sqls.create(" select htmc from " +
					comUtil.htTableMap.get(strArr[1]) + " where htid = '" + discredit_info.getContract_id() + "' ")));
		}
		warnexceptionInfo.setXyzt_name(daoCtl.getStrRowValue(dao,Sqls.create(" select name from XYZT_INFO where id = '"+warnexceptionInfo.getXyzt_id()+"' ")));
		flag=daoCtl.add(dao,warnexceptionInfo);
		if(flag){
			discredit_info.setType("3");//暂停使用
			daoCtl.update(dao, discredit_info);
		}
		return flag;
	}

	@At
	@Ok("->:/private/ratifyFollow/warnExceptionDetail.html")
	public void toWarnExceptionUpdate(@Param("type")String type,@Param("id") String id,HttpServletRequest req,HttpSession session){
		Warnexception warnexceptionInfo = daoCtl.detailByCnd(dao,Warnexception.class,Cnd.where("id","=",id));
		if(warnexceptionInfo.getState()==0){
			Sys_user user=(Sys_user) session.getAttribute("userSession");
			warnexceptionInfo.setState(1);
			warnexceptionInfo.setUsername(user.getLoginname());
			warnexceptionInfo.setAcceptance_time(DateUtil.getCurDateTime());
			daoCtl.update(dao,warnexceptionInfo);
		}
		//失信惩戒信息
		Discredit_info discredit_info = daoCtl.detailByCnd(dao,Discredit_info.class,Cnd.where("id","=",warnexceptionInfo.getSxcj_id()));
		discredit_info.setSxxw_id(getDiscreditInfoValue(dao,discredit_info.getSxxw_id(),"sxxw"));
		discredit_info.setSxqx_id(getDiscreditInfoValue(dao,discredit_info.getSxqx_id(),"sxqx"));
		discredit_info.setCjcs_id(getDiscreditInfoValue(dao,discredit_info.getCjcs_id(),"cjcs"));
		if(EmptyUtils.isEmpty(discredit_info.getSxqx_id())){
			discredit_info.setSxqx_id(discredit_info.getSxqx_other());
		}
		if(EmptyUtils.isEmpty(discredit_info.getCjcs_id())){
			discredit_info.setCjcs_id(discredit_info.getCjcs_other());
		}
		//失信惩戒备份表信息
		Discredit_info_bf discredit_info_bf = daoCtl.detailByCnd(dao,Discredit_info_bf.class,Cnd.where("id","=",id));
		if(EmptyUtils.isNotEmpty(discredit_info_bf)){
		discredit_info_bf.setSxxw_id(getDiscreditInfoValue(dao,discredit_info_bf.getSxxw_id(),"sxxw"));
		discredit_info_bf.setSxqx_id(getDiscreditInfoValue(dao,discredit_info_bf.getSxqx_id(),"sxqx"));
		discredit_info_bf.setCjcs_id(getDiscreditInfoValue(dao,discredit_info_bf.getCjcs_id(),"cjcs"));
		if(EmptyUtils.isEmpty(discredit_info_bf.getSxqx_id())){
			discredit_info_bf.setSxqx_id(discredit_info_bf.getSxqx_other());
		}
		if(EmptyUtils.isEmpty(discredit_info_bf.getCjcs_id())){
			discredit_info_bf.setCjcs_id(discredit_info_bf.getCjcs_other());
		}
		}
		//法人信息
		Xyzt_info xyztInfo = daoCtl.detailByCnd(dao,Xyzt_info.class,Cnd.where("id","=",warnexceptionInfo.getXyzt_id()));
		//处理法人类型多于的空格
		xyztInfo.setType(xyztInfo.getType().trim());
		//合同信息
//		String xy_type = daoCtl.getStrRowValue(dao, Sqls.create(" select xy_type from sys_unit where  id='" + discredit_info.getUnitid() + "' "));
		if(EmptyUtils.isNotEmpty(warnexceptionInfo.getContract_id())) {
			String contractTableName = comUtil.htTableMap.get(discredit_info.getXy_type());
			String contractStr = daoCtl.getStrRowValue(dao, Sqls.create(" select nvl(htid,'无')||','||nvl(htbh,'无')||','||nvl(htmc,'无')||','||nvl(dlrq,'无')||','||nvl(xyzt,'无')||','||nvl(fj,'无') from " + contractTableName + " where htid = '" + warnexceptionInfo.getContract_id() + "' "));
			String[] contractArr = contractStr.split(",");
			if (!"无".equals(contractArr[0])) {
				req.setAttribute("fileList", daoCtl.getMulRowValue(dao, Sqls.create("select filename,filepath from file_info where tablekey='"+contractArr[0]+"' and tablename='"+contractTableName+"' and fieldname='fj' order by create_time asc")));
			}
			req.setAttribute("contractArr", contractArr);
		}
		req.setAttribute("xy_name",comUtil.xyMap.get(discredit_info.getXy_type()));
		req.setAttribute("xy_type",discredit_info.getXy_type());
		req.setAttribute("discredit_info", discredit_info);
		req.setAttribute("discredit_info_bf", discredit_info_bf);
		req.setAttribute("xyztInfo",xyztInfo);
		req.setAttribute("info",warnexceptionInfo);
		req.setAttribute("type",type);
		req.setAttribute("state",warnexceptionInfo.getState());
		req.setAttribute("cz",warnexceptionInfo.getCz());
	}

	@At
	@Ok("raw")
	public boolean warnExceptionUpdate(@Param("id")Integer id,@Param("cz")Integer cz,@Param("cz_note")String cz_note,@Param("cz_why")String cz_why,@Param("cz_yj")String cz_yj,HttpSession session){
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		Warnexception warnexceptionInfo = daoCtl.detailByCnd(dao,Warnexception.class,Cnd.where("id","=",id));
		warnexceptionInfo.setCz_note(cz_note);
		warnexceptionInfo.setCz_why(cz_why);
		warnexceptionInfo.setCz_yj(cz_yj);
		warnexceptionInfo.setCz(cz);
		String cz_name = "";
		Integer cz_type = 0;
		switch (cz){
			case 1 :
				cz_name = "维持异议操作";
				cz_type = 2;
				break;
			case 2 :
				cz_name = "修改异议操作";
				cz_type = 2;
				break;
			case 3 :
				cz_name = "撤销异议操作";
				cz_type = 4;
				break;
			default:
				cz_name = "无操作";break;
		}
		warnexceptionInfo.setState(2);
		boolean flag = daoCtl.update(dao,warnexceptionInfo);
		if(flag){
			if(cz==1){
				//失信惩戒信息修改为惩戒中
				daoCtl.update(dao, Discredit_info.class,Chain.make("type",0),Cnd.where("id","=",warnexceptionInfo.getSxcj_id()));
			}
			SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type,flag,cz_type,
					"异议申请处理，执行【"+cz_name+"】","异议管理",cz_note,cz_why,cz_yj,"/private/gtxt/xyzt/toWarnExceptionUpdate?id="+id+"&type=2");
		}
		return flag;
	}

	private static String getDiscreditInfoValue(Dao dao,String key,String type){
		return daoCtl.getStrRowValue(dao,Sqls.create(" select "+type+" from BREACH_INFO where id = '"+key+"' "));
	}


	/**
	 * 功能描述:验证统一社会信用代码是否唯一
	 *
	 * @author L H T  2016年4月1日 下午1:48:31
	 *
	 * @param session
	 * @param code
	 * @return
	 */
	@At
	@Ok("raw")
	public boolean checkCode(HttpSession session,@Param("code") String code,@Param("id") String ztid,@Param("gszch")String gszch,@Param("frmc")String frmc) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		boolean bol=false;
		try {
			String sql="select count(1) from XYZT_INFO where code='"+code+"' or gszch='"+gszch+"' or name='"+frmc+"'";
			if(EmptyUtils.isNotEmpty(ztid)){
				sql+=" and id!='"+ztid+"'";
			}
			int count= daoCtl.getIntRowValue(dao, Sqls.create(sql));
			if (count>0) {
				bol=false;
			}else{
				bol=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			bol=false;
		}
		return bol;
	}


	@At
	@Ok("->:/private/ratifyFollow/queryList.html")
	public void queryAll(@Param("xy_type")String xy_type,@Param("type")String type,HttpServletRequest req){
		//产生方式
		Sql sql = Sqls.create("select value,name from cs_value where typeid = 00010008");
		List<Map> cstype = daoCtl.list(dao, sql);
		Hashtable<String, String> csTable = daoCtl.getHTable(dao, sql);
		//惩戒状态
		Sql sql1 = Sqls.create("select value,name from cs_value where typeid = 00010007");
		List<Map> cjtype = daoCtl.list(dao, sql1);
		Hashtable<String, String> cjTable = daoCtl.getHTable(dao, sql1);
		//发布状态
		Sql sql2 = Sqls.create("select value,name from cs_value where typeid = 00010009");
		Hashtable<String, String> fbtype = daoCtl.getHTable(dao, sql2);
		req.setAttribute("csTable", JSONObject.fromObject(csTable));
		req.setAttribute("cjTable", JSONObject.fromObject(cjTable));
		req.setAttribute("fbTable", JSONObject.fromObject(fbtype));
		req.setAttribute("ztTable", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("csMap", cstype);
		req.setAttribute("cjMap", cjtype);
		req.setAttribute("endTime",DateUtil.getToday());
		req.setAttribute("startTime", DateUtil.getFirstMonDay(DateUtil.getToday()));
		req.setAttribute("type",type);
		req.setAttribute("xy_type",xy_type);

	}

	@At
	@Ok("raw")
	public String cjList(@Param("start_date")String startDate,@Param("end_date")String endDate,@Param("keyword")String keyword,@Param("xyzt_type")String xyzt_type,@Param("xy_type")String xy_type,
						 @Param("code")String code,@Param("unit")String unit,@Param("page") int curPage, @Param("rows") int pageSize,HttpSession session){
		Sql sql = null;
		String tmpsql="";
		if("0004".equals(xy_type)||"00030002".equals(xy_type)){//土地市场和采矿权市场，显示土地复垦的行政处罚
			tmpsql="(a.xy_type = '"+xy_type+"' or a.xy_type = '0009' or a.sxxw_id in(select id from breach_info where xylx='"+xy_type+"'))";
		}else{
			tmpsql="(a.xy_type = '"+xy_type+"'  or a.sxxw_id in(select id from breach_info where xylx='"+xy_type+"'))";
		}
		String sqlstr = "select a.id as sxcjid,a.create_date,a.start_date,a.sxxw_id,a.end_date,a.type,a.issue,a.src,a.discipline_date,b.name,b.type as xyzt_type,"+
				"c.sxxw,a.perform_date,a.unitid,d.name as unitname from discredit_info a,xyzt_info b,breach_info c,sys_unit d where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unitid=d.id "+
				" and "+tmpsql+" and a.create_date between '"+startDate+" 00:00:00' and '"+endDate+" 23:59:59' ";
		//信用主体类型
		if(EmptyUtils.isNotEmpty(xyzt_type) && !"all".equals(xyzt_type)){
			sqlstr += " and b.type = '"+xyzt_type+"'";
		}
		//产生方式
		if(EmptyUtils.isNotEmpty(unit) && !"0".equals(unit)){
			sqlstr += " and a.unitid = '"+unit+"' ";
		}
//		//信用主体名称
		if(EmptyUtils.isNotEmpty(keyword)){
			sqlstr += " and b.name like '%"+keyword+"%'";
		}
		//信用主体社会统一信用代码
		if(EmptyUtils.isNotEmpty(code)){
			sqlstr += " and b.code like '%"+code+"%' ";
		}
		sqlstr += " order by a.create_date desc";
		System.out.println("sqlstr:"+ sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();
		for(int i=0;i<list.size();i++){
			String sxxw_id=StringUtil.null2String(list.get(i).get("sxxw_id"));
			String sxcjid=StringUtil.null2String(list.get(i).get("sxcjid"));
			Breach_info breachinfo=daoCtl.detailByName(dao, Breach_info.class, sxxw_id);
			list.get(i).put("sxxw", breachinfo.getSxxw());
			String cjcs=Discredit_infoAction.getCjcs(dao,xy_type,sxxw_id,sxcjid);
			list.get(i).put("cjcs", cjcs);
			//获取惩戒单位id号，判断若是县区分局，则在前面加上所在市
			String unitid=StringUtil.null2String(list.get(i).get("unitid"));
			String unitname=StringUtil.null2String(list.get(i).get("unitname"));
			if(unitid.length()>8){
				Sys_unit sysunit=daoCtl.detailByName(dao, Sys_unit.class, unitid.substring(0, 8));
				unitname="("+sysunit.getName().substring(0, 3)+")"+unitname;
			}
			list.get(i).put("unitname",unitname);
		}
		return PageObj.getPageJSON(qr);
	}

	/*
         * 奖励信息list
         * */
	@At
	@Ok("raw")
	public String jlList(@Param("start_date")String startDate,@Param("end_date")String endDate,@Param("keyword")String keyword,@Param("xyzt_type")String xyzt_type,@Param("xy_type")String xy_type,
						 @Param("code")String code,@Param("unit")String unit,@Param("page") int curPage, @Param("rows") int pageSize,HttpSession session){
		Sql sql = null;
		String sqlstr = "select a.id as jlid,a.jl_date,a.note,a.create_date,b.name,b.type,c.name as unitname from reward_info a,xyzt_info b,sys_unit c where a.xyzt_id = b.id and a.unitid=c.id and a.type = 0 and a.create_date between '"+startDate+" 00:00:00' and '"+endDate+" 23:59:59' " +
				" and a.xy_type = '"+xy_type+"'";
		if(EmptyUtils.isNotEmpty(keyword)){
			sqlstr += "and b.name like '%"+keyword+"%'";
		}
		if(EmptyUtils.isNotEmpty(xyzt_type) && !"all".equals(xyzt_type)){
			sqlstr += "and b.type = '"+xyzt_type+"'";
		}
		if(EmptyUtils.isNotEmpty(unit) && !"0".equals(unit)){
			sqlstr += "and a.unitid='"+unit+"' ";
		}
		//信用主体社会统一信用代码
		if(EmptyUtils.isNotEmpty(code)){
			sqlstr += " and b.code like '%"+code+"%' ";
		}
		sqlstr += " order by a.create_date desc";
		System.out.println("sqlstr:"+sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		return PageObj.getPageJSON(qr);
	}

	@At
	@Ok("->:/private/ratifyFollow/queryXzList.html")
	public void queryXzPage(@Param("type")String type,HttpServletRequest req){
		//惩戒状态
		Sql sql1 = Sqls.create("select value,name from cs_value where typeid = 00010007");
		List<Map> cjtype = daoCtl.list(dao, sql1);
		Hashtable<String, String> cjTable = daoCtl.getHTable(dao, sql1);
		//发布状态
		Sql sql2 = Sqls.create("select value,name from cs_value where typeid = 00010009");
		Hashtable<String, String> fbtype = daoCtl.getHTable(dao, sql2);
		req.setAttribute("cjTable", JSONObject.fromObject(cjTable));
		req.setAttribute("fbTable", JSONObject.fromObject(fbtype));
		req.setAttribute("cjMap", cjtype);
		req.setAttribute("xyztMap", comUtil.xyztlxMap);
		req.setAttribute("ztTable", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("endTime",DateUtil.getToday());
		req.setAttribute("startTime", DateUtil.getFirstMonDay(DateUtil.getToday()));
		req.setAttribute("type",type);
		req.setAttribute("csvalueList", comUtil.sxxwtypeList);
	}

	//行政处罚信息列表查询
	@At()
	@Ok("raw")
	public String xzcflist(@Param("start_date")String startDate,@Param("end_date")String endDate,@Param("xyzt_type") String xyzt_type,@Param("cfsx_date") String cfsx_date,@Param("xyzt_name") String xyzt_name,@Param("unit")String unit,
						   @Param("code")String code,@Param("xzcf_zt")String xzcf_zt,@Param("page") int curPage, @Param("rows") int pageSize,HttpServletRequest req,HttpSession session){
		Sys_user user= (Sys_user)session.getAttribute("userSession");
		Sql sql = null;
		StringBuffer sqlstr = new StringBuffer("select a.id as xzcfid,a.contract_id,a.xzcf_type,a.au,a.cfsx_date,a.xzcf_zt,a.issue,a.start_date,a.end_date,b.name,b.type as xyzt_type from xzcf_info a left join xyzt_info b on A.XYZT_ID = b.id  where " +
				"  a.cfsx_date between '"+DateUtil.getPreDayStr(startDate)+"' and '"+DateUtil.getNextDayStr(endDate)+"' ");
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr.append(" and b.type = '"+xyzt_type+"' ");
		}
		if(EmptyUtils.isNotEmpty(cfsx_date)){
			sqlstr.append(" and a.start_date like '"+cfsx_date+"%' ");
		}
		if(EmptyUtils.isNotEmpty(xyzt_name)){
			sqlstr.append(" and b.name like '%"+xyzt_name+"%' ");
		}
		if(EmptyUtils.isNotEmpty(xzcf_zt)){
			sqlstr.append(" and a.xzcf_zt = '"+xzcf_zt+"' ");
		}
		if(EmptyUtils.isNotEmpty(unit)){
			sqlstr.append(" and a.unitid like '"+unit+"%' ");
		}
		//信用主体社会统一信用代码
		if(EmptyUtils.isNotEmpty(code)){
			sqlstr.append(" and b.code like '%"+code+"%' ");
		}
		sqlstr.append(" order by a.id desc");
		sql = Sqls.create(sqlstr.toString());
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();

		for(int i=0;i<list.size();i++){
			/*String start_date = EmptyUtils.isNotEmpty(list.get(i).get("start_date")) ? list.get(i).get("start_date").toString() : DateUtil.getToday();
			String end_date = list.get(i).get("end_date").toString();
			//判断是否为已撤销    2表示已撤销
			if(!"2".equals(list.get(i).get("xzcf_zt"))){
				if(com.hits.common.util.DateUtil.getDifferDays(com.hits.common.util.DateUtil.getToday(), start_date) > 0 &&
						com.hits.common.util.DateUtil.getDifferDays(com.hits.common.util.DateUtil.getToday(), end_date) > 0){
					list.get(i).put("xzcf_zt", " ");// ' '表示惩戒尚未开始
					int xzcfid = ((BigDecimal)list.get(i).get("xzcfid")).intValue();
					daoCtl.exeUpdateBySql(dao, Sqls.create("update xzcf_info set xzcf_zt = ' ' where id = '"+xzcfid+"'"));
				}
				//跟现在时间对比是否在惩戒中((BigDecimal)list.get(i).get("nbid")).intValue()
				if(com.hits.common.util.DateUtil.getDifferDays(start_date, com.hits.common.util.DateUtil.getToday()) > 0 &&
						com.hits.common.util.DateUtil.getDifferDays(com.hits.common.util.DateUtil.getToday(), end_date) > 0){
					list.get(i).put("xzcf_zt", "0");// 0 表示惩戒中
					int xzcfid = ((BigDecimal)list.get(i).get("xzcfid")).intValue();
					daoCtl.exeUpdateBySql(dao, Sqls.create("update xzcf_info set xzcf_zt = 0 where id = '"+xzcfid+"'"));
				}
				//跟现在时间对比是否惩戒结束
				if(com.hits.common.util.DateUtil.getDifferDays(start_date, com.hits.common.util.DateUtil.getToday()) > 0 &&
						com.hits.common.util.DateUtil.getDifferDays(end_date, com.hits.common.util.DateUtil.getToday()) > 0){
					list.get(i).put("xzcf_zt", "1");// 1表示惩戒结束
					int xzcfid = ((BigDecimal)list.get(i).get("xzcfid")).intValue();
					daoCtl.exeUpdateBySql(dao, Sqls.create("update xzcf_info set xzcf_zt = 1 where id = '"+xzcfid+"'"));
				}
			}*/
			String xm_name = "";
			list.get(i).put("xyzt_type", list.get(i).get("xyzt_type").toString().trim());
			//行政处罚类型
			String xzcf_type = list.get(i).get("xzcf_type").toString();
			//合同id
			String contract_id = "";
			if(EmptyUtils.isNotEmpty(list.get(i).get("contract_id"))){
				contract_id = list.get(i).get("contract_id").toString();
			}
			//合同信息
			String htbm = comUtil.htTableMap.get(xzcf_type);
			if(EmptyUtils.isNotEmpty(htbm) && EmptyUtils.isNotEmpty(contract_id)){
				Sql sqlht = Sqls.create("select htmc from "+htbm+" where htid = '"+contract_id+"' ");
				List<Map> htlist = daoCtl.list(dao, sqlht);
				if(htlist.size()>0){
					xm_name = htlist.get(0).get("htmc").toString();
				}
			}
			list.get(i).put("xm_name", xm_name);
		}
		return PageObj.getPageJSON(qr);
	}

	//行政强制列表查询方法
	@At
	@Ok("raw")
	public String xzqzlist(@Param("start_date")String startDate,@Param("end_date")String endDate,@Param("xyzt_type") String xyzt_type,@Param("cfsx_date") String cfsx_date,@Param("xyzt_name") String xyzt_name,@Param("unit")String unit,
						   @Param("code")String code,@Param("xzcf_zt")String xzcf_zt,@Param("page") int curPage, @Param("rows") int pageSize,HttpServletRequest req,HttpSession session){
		Sql sql = null;
		StringBuffer sqlstr = new StringBuffer("select a.id as xzqzid,a.aj_name,a.reason,a.start_date,a.end_date,a.xzcf_zt,a.issue,b.name,b.type as xyzt_type"+
				" from xzqz_info a left join xyzt_info b on a.xyzt_id = b.id  where a.create_date between '"+DateUtil.getPreDayStr(startDate)+"' and '"+DateUtil.getNextDayStr(endDate)+"' ");
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr.append(" and b.type = '"+xyzt_type+"' ");
		}
		if(EmptyUtils.isNotEmpty(cfsx_date)){
			sqlstr.append(" and a.start_date like '"+cfsx_date+"%' ");
		}
		if(EmptyUtils.isNotEmpty(xyzt_name)){
			sqlstr.append(" and b.name like '%"+xyzt_name+"%'");
		}
		if(EmptyUtils.isNotEmpty(unit)){
			sqlstr.append(" and a.unitid like '"+unit+"%' ");
		}
		//信用主体社会统一信用代码
		if(EmptyUtils.isNotEmpty(code)){
			sqlstr.append(" and b.code like '%"+code+"%' ");
		}
		sqlstr.append(" order by a.id desc");
		sql = Sqls.create(sqlstr.toString());
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();
		for(int i=0; i < list.size(); i++){
			list.get(i).put("xyzt_type", list.get(i).get("xyzt_type").toString().trim());
		}
		return PageObj.getPageJSON(qr);
	}
}