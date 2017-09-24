package com.hits.modules.sxcj;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.*;
import org.nutz.dao.entity.annotation.Column;
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
import com.hits.common.util.DateUtil;
import com.hits.modules.bean.Breach_info;
import com.hits.modules.bean.File_info;
import com.hits.modules.cgtable.util.CommonStaticUtil;
import com.hits.modules.com.YWCL;
import com.hits.modules.com.comUtil;
import com.hits.modules.form.YYFormAction;
import com.hits.modules.form.bean.Form_table;
import com.hits.modules.gtxt.bean.Warnexception;
import com.hits.modules.gtxt.bean.Xyzt_info;
import com.hits.modules.sxcj.bean.Discredit_info;
import com.hits.modules.sxcj.bean.Discredit_info_bf;
import com.hits.modules.sys.bean.Sys_log;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.modules.xzcf.bean.Xzcf_info;
import com.hits.util.EmptyUtils;
import com.hits.util.PageObj;
import com.hits.util.StringUtil;
import com.hits.util.SysLogUtil;

import net.sf.json.JSONObject;

/**
 * @author hzg
 * @time 2016-03-02 10:38:44
 * 
 */
@IocBean
@At("/private/sxcj")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Discredit_infoAction extends BaseAction {
	@Inject
	protected Dao dao;
	public static String url="/private/sxcj/detail?sxcjid=@id";
	
	//失信惩戒列表页
	@At("/tolist")
	@Ok("->:/private/sxcj/sxcjlist.html")
	public void user(@Param("xy_type") String xy_type,HttpServletRequest req,HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String unitid = user.getUnitid();
		String name = user.getLoginname();
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
		req.setAttribute("ztlxlist", comUtil.xyztlxMap);
		req.setAttribute("xytype", xy_type);
	}
	
	//失信惩戒列表页面查询
	@At
	@Ok("raw")
	public String sxcjList(HttpServletRequest req,HttpSession session,@Param("xyzt_type") String xyzt_type,
			@Param("src") String src,@Param("type") String type,@Param("xyzt_name") String xyzt_name,@Param("xytype") String xytype,
			@Param("page") int curPage, @Param("rows") int pageSize) {
		Sys_user user= (Sys_user)session.getAttribute("userSession");
		Sql sql = null;
		String sqlstr = "select a.id as sxcjid,a.sxxw_id,a.create_date,a.start_date,a.end_date,a.type,a.issue,a.src,a.sxqx_id,a.xyzt_id,a.contract_id,a.discipline_date,a.xzcf_id,b.name,b.type as xyzt_type,"+
		"c.sxxw,c.cjcs,a.perform_date,d.name as unitname,a.sxxw_other,a.sxqx_other,a.cjcs_other  from discredit_info a,xyzt_info b,breach_info c,sys_unit d where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unitid=d.id "+
				"and (a.actor = '"+user.getLoginname()+"' or (a.actor is null)) and a.unitid='"+user.getUnitid()+"' and a.xy_type = '"+xytype+"'";
		if(xytype.equals("0008")){
			sqlstr = "select a.id as sxcjid,a.sxxw_id,a.create_date,a.start_date,a.end_date,a.type,a.issue,a.src,a.discipline_date,a.xyzt_id,a.xzcf_id,b.name,b.type as xyzt_type,"+
			"a.perform_date,c.name as username from discredit_info a,xyzt_info b,sys_unit c where a.unitid=c.id and a.xyzt_id = b.id "+
					"and (a.actor = '"+user.getLoginname()+"' or (a.actor is null)) and a.unitid='"+user.getUnitid()+"' and a.xy_type = '"+xytype+"'";
		}
		//信用主体类型
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr += " and b.type = '"+xyzt_type+"'";
		}
		//产生方式
		if(EmptyUtils.isNotEmpty(src)){
			sqlstr += " and a.src = '"+StringUtil.StringToInt(src)+"'";
		}
		//惩戒状态
		if(EmptyUtils.isNotEmpty(type)){
			sqlstr += " and a.type = '"+type+"'";
		}
		//信用主体名称
		if(EmptyUtils.isNotEmpty(xyzt_name)){
			sqlstr += " and b.name like '%"+xyzt_name+"%'";
		}
		sqlstr += " ORDER BY a.id desc";
		System.out.println("sqlstr:"+ sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();
		for(int i=0;i<list.size();i++){
			String start_date = StringUtil.null2String(list.get(i).get("start_date"));
			String end_date = StringUtil.null2String(list.get(i).get("end_date"));
			String perform_date = StringUtil.null2String(list.get(i).get("perform_date"));
			if(end_date==null||"".equals(end_date)){
				list.get(i).put("startDend", start_date+" 起 ");
			}else{
				list.get(i).put("startDend", start_date+" 至 "+(null == end_date?"":end_date));
			}
			String createDate=StringUtil.null2String(list.get(i).get("create_date"));
			list.get(i).put("xyzt_type", list.get(i).get("xyzt_type").toString().trim());
			String sxxw_id=StringUtil.null2String(list.get(i).get("sxxw_id"));
			Breach_info breachinfo=null;
			if(xytype.equals("0008")){
				String cjcs="";
				String tongbao="";
				String chengjie="";
				String aa=daoCtl.getStrRowValue(dao, Sqls.create("select xylx from breach_info where id='"+sxxw_id+"'"));
				if(!"0008".equals(aa)){
					breachinfo=daoCtl.detailByName(dao, Breach_info.class, "0043");
					tongbao=breachinfo.getCjcs();
				}
				System.out.println("sxxw_id:"+sxxw_id);
				breachinfo=daoCtl.detailByName(dao, Breach_info.class, sxxw_id);
				chengjie=breachinfo.getCjcs();
				cjcs=chengjie;
				if(!"".equals(tongbao)){
					cjcs="1、"+tongbao+"(通报) 2、"+chengjie+"(惩戒中)";
				}
				list.get(i).put("cjcs", cjcs);
				list.get(i).put("sxxw", breachinfo.getSxxw());
			}else if(xytype.equals("00070001")){//农村土地整治
				//获取该条失信惩戒信息的年份
				String year=StringUtil.null2String(list.get(i).get("discipline_date")).substring(0,4);
				//获取该条失信惩戒信息所关联的项目
				String contractid=StringUtil.null2String(list.get(i).get("contract_id"));
				//获取该条失信惩戒信息的信用主体
				String xyzt_id=StringUtil.null2String(list.get(i).get("xyzt_id"));
				//获取该条失信惩戒信息的失信情形
				String sxqx_id=StringUtil.null2String(list.get(i).get("sxqx_id"));
				String sqlstr1="select count(1) from discredit_info where discipline_date like '"+year+"%' and type not in (2,3) and xyzt_id='"+xyzt_id+"' and sxqx_id='"+sxqx_id+"'";
				req.setAttribute("syfss", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr1)));
				if(EmptyUtils.isNotEmpty(contractid)){
					sqlstr1="select count(1) from discredit_info where discipline_date like '"+year+"%' and contract_id='"+contractid+"' and type not in (2,3) and xyzt_id='"+xyzt_id+"' and sxqx_id='"+sxqx_id+"'";
					req.setAttribute("txmfss", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr1)));
				}
				breachinfo=daoCtl.detailByName(dao, Breach_info.class, sxxw_id);
				list.get(i).put("sxxw", breachinfo.getSxxw());
				
				String sxcjid=StringUtil.null2String(list.get(i).get("sxcjid"));
				
				//根据信用主体来排序
				String sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxxw_id="+sxxw_id+" and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
				Map DisMap = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				System.out.println("DisMap:"+DisMap.toString());
				//根据项目来排序
				String sqlxm="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxxw_id="+sxxw_id+" and contract_id='"+contractid+"' and type not in (2,3) ORDER BY create_date asc";
				Map XmMap = daoCtl.getHTable(dao,Sqls.create(sqlxm));
				int rownumber=0,xmnumber=0;
				if(DisMap.get(sxcjid)!=null){
					rownumber=Integer.valueOf(DisMap.get(sxcjid).toString());
				}
				if(XmMap.get(sxcjid)!=null){
					xmnumber=Integer.valueOf(XmMap.get(sxcjid).toString());
				}
				if(sxxw_id.equals("0027")){//设计义务
					int a=0,b=0,c=0,d=0,e=0,f=0,g=0;
					//查询土地设计义务0027
					sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=0027 and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
					Map map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
					if(map.get(sxcjid)!=null){
						a=Integer.valueOf(map.get(sxcjid).toString());
					}
					//查询土地设计义务的00270001
					sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=00270001 and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
					map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
					if(map.get(sxcjid)!=null){
						b=Integer.valueOf(map.get(sxcjid).toString());
					}
					//查询土地设计义务同信用主体的00270002
					sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=00270002 and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
					map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
					if(map.get(sxcjid)!=null){
						c=Integer.valueOf(map.get(sxcjid).toString());
					}
					//查询土地设计义务的同项目的00270002
					sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=00270002 and contract_id='"+contractid+"' and type not in (2,3) ORDER BY create_date asc";
					map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
					if(map.get(sxcjid)!=null){
						d=Integer.valueOf(map.get(sxcjid).toString());
					}
					//查询土地设计义务的00270003
					sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=00270003 and contract_id='"+contractid+"' and type not in (2,3) ORDER BY create_date asc";
					map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
					if(map.get(sxcjid)!=null){
						e=Integer.valueOf(map.get(sxcjid).toString());
					}
					sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id in (0027,00270001,00270002) and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
					map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
					if(map.get(sxcjid)!=null){
						f=Integer.valueOf(map.get(sxcjid).toString());
					}
					sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id in (00270002,00270003) and contract_id='"+contractid+"' and type not in (2,3) ORDER BY create_date asc";
					map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
					if(map.get(sxcjid)!=null){
						g=Integer.valueOf(map.get(sxcjid).toString());
					}
					if(a>2||b>1||c>2||d>1||e>2||f>2||g>2){
						list.get(i).put("cjcs", "国土资源管理部门不得委托其开展下一年度土地整治项目设计");
					}else{
						list.get(i).put("cjcs", "国土资源管理部门予以通报批评1次");
					}
				}else if(sxxw_id.equals("0028")){//施工义务
					if(xmnumber<3){
						list.get(i).put("cjcs", "国土资源管理部门予以通报批评1次");
					}else{
						list.get(i).put("cjcs", "国土资源管理部门不得委托其开展下一年度土地整治项目设计");
					}
				}else if(sxxw_id.equals("0029")){//监理义务
					if(xmnumber>2||rownumber>5){
						list.get(i).put("cjcs", "国土资源管理部门不得委托其开展下一年度土地整治项目设计");
					}else{
						list.get(i).put("cjcs", "国土资源管理部门予以通报批评1次");
					}
				}
			}else{
				breachinfo=daoCtl.detailByName(dao, Breach_info.class, sxxw_id);
				list.get(i).put("sxxw", breachinfo.getSxxw());
				String otherCjcs=StringUtil.null2String(list.get(i).get("cjcs_other"));;
				if(EmptyUtils.isNotEmpty(otherCjcs)){
					list.get(i).put("cjcs", otherCjcs);
				}else{
					list.get(i).put("cjcs", breachinfo.getCjcs());
				}
			}
		}
		return PageObj.getPageJSON(qr);
	}
	
	//转入添加信用主体页面备用
	@At
	@Ok("->:/private/jlry/jlryAddfr.html")
	public void toAddfr(HttpServletRequest req,HttpSession session){
		Hashtable<String, String> zttype = comUtil.xyztlx;
//		System.out.println(JSONObject.fromObject(zttype));
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String unitid = user.getUnitid();
		//分组查询失信惩戒中信用主体类型
//		Sql sqlztlx = Sqls.create("select type from discredit_info where unitid = '"+unitid+"' and actor = '"+user.getLoginname()+"' group by type");
//		List<Map> list = daoCtl.list(dao, sqlztlx);
//		req.setAttribute("ztlxlist", list);
		
		//备用信用主体类型条件查询数据，失信惩戒中有数据使用上面查询语句
		req.setAttribute("ztlxlist", zttype);
		req.setAttribute("ztMap", JSONObject.fromObject(zttype));
	}
	//根据合同信息查找信用主体
	@At
	@Ok("raw")
	public String tofindXyzt(@Param("xyzt_ids") String xyztids){
		String [] ids = StringUtil.null2String(xyztids).split(",");
		List<Map> list = daoCtl.list(dao, Sqls.create("select type,code,zzjgdm from xyzt_info where id = '"+ids[0]+"'"));
		for(int i=0;i<list.size();i++){
			list.get(i).put("type", list.get(i).get("type").toString().trim());
		}
		return list+"";
	}
	
	//转入失信惩戒添加页面
	@At
	@Ok("->:/private/sxcj/sxcjAdd.html")
	public void add(HttpServletRequest req,HttpSession session,@Param("xytype") String xytype) {
		try {
			Sys_user user = (Sys_user) session.getAttribute("userSession");
			String unitid = user.getUnitid();
			Sys_unit unit = daoCtl.detailByName(dao, Sys_unit.class, unitid);
			String sqlstr="";
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@At
	@Ok("raw")
	public boolean addSave(@Param("..") Discredit_info discreditInfo,@Param("xyztid") String xyztid,
			@Param("local") String local,HttpSession session){
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		boolean flag = false;
		discreditInfo.setActor(user.getLoginname());
		discreditInfo.setCreate_date(DateUtil.getCurDateTime());
		discreditInfo.setUnitid(user.getUnitid());
		discreditInfo.setXzqh(daoCtl.getStrRowValue(dao,Sqls.create(" select xzqh from sys_unit where id = '"+discreditInfo.getUnitid()+"' ")));
		discreditInfo.setSrc(1);
		//判断是否在惩戒中
		String today = DateUtil.getToday();
		if(EmptyUtils.isNotEmpty(discreditInfo.getEnd_date())){
			if(DateUtil.getDifferDays(discreditInfo.getStart_date(),today) > 0 &&
					DateUtil.getDifferDays(today,discreditInfo.getEnd_date()) > 0){
				discreditInfo.setType("0");  // 0 表示惩戒中
			}
		}else{
			discreditInfo.setType("0");  // 0 表示惩戒中
		}
		if("0".equals(local)){
			String [] ids = StringUtil.null2String(xyztid).split(",");
			for(int i =0; i<ids.length;i++){
				int idn = Integer.parseInt(ids[i]);
				discreditInfo.setXyzt_id(ids[i]);
				flag = daoCtl.add(dao, discreditInfo);
			}
		}else{
			flag = daoCtl.add(dao, discreditInfo);
		}
		String xyzt = daoCtl.getStrRowValue(dao,Sqls.create(" select name from xyzt_info where id = '"+discreditInfo.getXyzt_id()+"'  "));
		String lognote=SysLogUtil.getLogNote(discreditInfo, "sxcj");
		SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag, 1, 
				"添加信用主体【" + xyzt + "】的失信惩戒记录", "失信惩戒", lognote, "",
				"",url.replace("@id", discreditInfo.getId()+""));
		return flag;
	}
	
	//失信惩戒修改
	@At
	@Ok("->:/private/sxcj/sxcjUpdate.html")
	public void toUpdate(@Param("sxcjid") String sxcjid,HttpServletRequest req,HttpSession session,
			@Param("yyid") String yyid,@Param("warnid")String warnid){
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		int id = StringUtil.StringToInt(sxcjid);
		//信用主体类型
		Hashtable<String, String> zttype = comUtil.xyztlx;
		//失信惩戒信息
		Discredit_info sxcj = daoCtl.detailById(dao, Discredit_info.class, id);
		//信用类型
		String xytype = sxcj.getXy_type();
		//信用主体信息
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, StringUtil.StringToInt(sxcj.getXyzt_id()));
		xyzt.setType(xyzt.getType().trim());
		//合同信息
		if(EmptyUtils.isNotEmpty(sxcj.getTablekey())){
			Sql sqlht = Sqls.create("select htmc from "+sxcj.getTablekey()+" where htid = '"+sxcj.getContract_id()+"' ");
			if("00030002".equals(sxcj.getXy_type())||"l_tkqsyf".equals(sxcj.getTablekey())){//采矿权或探矿权使用费
				String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(sxcj.getTablekey())));
				String xkzhSql="select xkzh from "+sxcj.getTablekey()+" where "+tablePK+" = '"+sxcj.getContract_id()+"' ";
				sqlht=Sqls.create(xkzhSql);
			}else if("l_tkht".equals(sxcj.getTablekey())){//探矿权合同
				String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(sxcj.getTablekey())));
				String xkzhSql="select kcxkz from "+sxcj.getTablekey()+" where "+tablePK+" = '"+sxcj.getContract_id()+"' ";
				sqlht=Sqls.create(xkzhSql);
			} 
			req.setAttribute("glxm", daoCtl.getStrRowValue(dao, sqlht));
			//获取主表的附表
			List<Form_table> tableList = daoCtl.list(dao, Form_table.class, Sqls.create("select * from form_table where formdefid=(select formdefid from form_table where tablekey='"+sxcj.getTablekey().substring(2)+"' and form_type=1) order by ismain desc"));
			if (tableList.size()>0) {
				for (Form_table table :tableList) {
					String tablename=CommonStaticUtil.TABLE_NAME_SUB+table.getTablekey();
					if(table.getIsmain() != 1){
						List<Map> ywlist=daoCtl.list(dao, Sqls.create("select * from "+tablename+" where ywid='"+sxcj.getYwid()+"'"));
						if(ywlist.size()>0){
							req.setAttribute("ywlist", ywlist);
						}
					}
				}
			}
			String defid = daoCtl.getStrRowValue(dao, Sqls.create("select formdefid from form_table where tablekey='"+sxcj.getTablekey().substring(2)+"' and form_type=1"));
			String url="/private/formyy/topreview?defid="+defid+"&mainid="+sxcj.getContract_id()+"&ywid="+sxcj.getYwid();
			req.setAttribute("url", url);
		}
		//失信行为
		String sqlstr="";
		if("00070004".equals(xytype)&&"0008".equals(comUtil.yhjgHt.get(user.getUnitid()))){//实施地质勘查并且是省厅地勘处的单位
			sqlstr="select bi.id,bi.sxxw,bi.ht_type,bi.xylx from breach_info bi "
					+"where bi.is_use=1 and ((bi.id in (select breachid from user_breach where loginname='"+user.getLoginname()+"' )) or bi.xylx='0000')  order by bi.location,bi.id";
		}else if("0009".equals(xytype)){//复垦嘻嘻你
			sqlstr="select id,sxxw,ht_type,xylx from breach_info where is_use=1 and is_fk=0 and xylx='"+xytype+"' and length(id)=4 order by location,id";
		}else{
			sqlstr="select id,sxxw,ht_type,xylx from breach_info where is_use=1 and is_fk=0 and xylx in ('"+xytype+"','0000') and length(id)=4 order by location,id";
		}
		
		Sql sqlxw = Sqls.create(sqlstr);
		List<Map> xwlist = daoCtl.list(dao, sqlxw);
		//失信情形
		Sql sqlqx = Sqls.create("select id,sxqx,(case when cjcs is null then 0 else 1 end) cjcs from breach_info where id like '"+sxcj.getSxxw_id()+"%'");
		List<Map> qxlist = daoCtl.list(dao, sqlqx);
		//判断情形是否需要进行约谈
		if(EmptyUtils.isNotEmpty(sxcj.getSxqx_id()) && sxcj.getSxqx_id().equals("00200001")){
			int count = daoCtl.getIntRowValue(dao, Sqls.create("select count(1) from discredit_info where xyzt_id = '"+sxcj.getXyzt_id()+"' and sxqx_id = '"+sxcj.getSxqx_id()+"'"));
			req.setAttribute("count", count);
		}
		req.setAttribute("xyMap", comUtil.xyMap);
		//惩戒措施
		Sql sqlcs = Sqls.create("select id,cjcs from breach_info where id like '"+sxcj.getSxxw_id()+"%' and sxqx =(select sxqx from breach_info where id = '"+sxcj.getSxqx_id()+"')");
		List<Map> cslist = daoCtl.list(dao, sqlcs);
		//惩戒依据
		Sql sqlyj = Sqls.create("select yj from breach_info where id = '"+sxcj.getSxqx_id()+"'");
		List<Map> yjlist = daoCtl.list(dao, sqlyj);
		req.setAttribute("sxcj", JSONObject.fromObject(sxcj));
		req.setAttribute("xyzt", xyzt);
		req.setAttribute("xwlist", xwlist);
		req.setAttribute("qxlist", qxlist);
		req.setAttribute("yjlist", yjlist);
		req.setAttribute("cslist", cslist);
		if(EmptyUtils.isNotEmpty(sxcj.getUnit()) && ( "0010".equals(sxcj.getUnit()) || "0011".equals(sxcj.getUnit()) || "0012".equals(sxcj.getUnit()) )) {
			req.setAttribute("cjjg", daoCtl.getHTable(dao,Sqls.create(" select id,name from sys_unit where id in ('0010','0011','0012') ")));
		}
		req.setAttribute("ztMap", JSONObject.fromObject(zttype));
		req.setAttribute("yyid", yyid);
		req.setAttribute("warnid", yyid);
	}
	@At
	@Ok("raw")
	public boolean Update(HttpSession session,@Param("..")Discredit_info sxcj,@Param("note") String note,@Param("..") final File_info file,
			@Param("reason") String reason,@Param("basis") String basis,@Param("yyid") String yyid,@Param("warnid")String warnid){
		Sys_user user = (Sys_user)session.getAttribute("userSession");
		Discredit_info oldObj=daoCtl.detailById(dao, Discredit_info.class, sxcj.getId());
		System.out.println("--------------->"+warnid);
		//将失信记录表修改前的数据插入至备份表
		if(EmptyUtils.isNotEmpty(warnid)){
			Discredit_info_bf bfobj=new Discredit_info_bf();
			int bfid=Integer.valueOf(warnid);
			bfobj.setId(bfid);
			bfobj.setXyzt_id(oldObj.getXyzt_id());
			bfobj.setContract_id(oldObj.getContract_id());
			bfobj.setSxxw_id(oldObj.getSxxw_id());
			bfobj.setSxqx_id(oldObj.getSxqx_id());
			bfobj.setCjcs_id(oldObj.getCjcs_id());
			bfobj.setSxxw_other(oldObj.getSxxw_other());
			bfobj.setSxqx_other(oldObj.getSxqx_other());
			bfobj.setCjcs_other(oldObj.getCjcs_other());
			bfobj.setPerform_date(oldObj.getPerform_date());
			bfobj.setUnit(oldObj.getUnit());
			bfobj.setStart_date(oldObj.getStart_date());
			bfobj.setMeasures(oldObj.getMeasures());
			bfobj.setType(oldObj.getType());
			bfobj.setDiscipline_date(oldObj.getDiscipline_date());
			bfobj.setEnd_date(oldObj.getEnd_date());
			bfobj.setSrc(oldObj.getSrc());
			bfobj.setActor(oldObj.getActor());
			bfobj.setCreate_date(oldObj.getCreate_date());
			bfobj.setIssue(oldObj.getIssue());
			bfobj.setYt_date(oldObj.getYt_date());
			bfobj.setUnitid(oldObj.getUnitid());
			bfobj.setXy_type(oldObj.getXy_type());
			bfobj.setXzqh(oldObj.getXzqh());
			bfobj.setTablekey(oldObj.getTablekey());
			bfobj.setYwid(oldObj.getYwid());
			bfobj.setSrc_type(oldObj.getSrc_type());
			bfobj.setXzcf_id(oldObj.getXzcf_id());
			bfobj.setBiaozhu(oldObj.getBiaozhu());
			dao.insert(bfobj);
		}
		//异议处理模块调用
		if(EmptyUtils.isNotEmpty(yyid)){
			Warnexception warnexceptionInfo = daoCtl.detailByCnd(dao,Warnexception.class,Cnd.where("id","=",StringUtil.StringToInt(yyid)));
			warnexceptionInfo.setCz_note(note);
			warnexceptionInfo.setCz_why(reason);
			warnexceptionInfo.setCz_yj(basis);
			warnexceptionInfo.setCz(2);
			String cz_name = "";
			Integer cz_type = 0;
			switch (warnexceptionInfo.getCz()){
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
				if(cz_type==4){
					//失信惩戒信息修改为结束状态
					daoCtl.update(dao, Discredit_info.class,Chain.make("type",1),Cnd.where("id","=",warnexceptionInfo.getSxcj_id()));
				}
				SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type,flag,cz_type, 
						"异议申请处理，执行【"+cz_name+"】","异议管理",note, reason, basis
						,url.replace("@id", yyid+""));
			}
		}
		
		boolean flag = false;
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, StringUtil.StringToInt(sxcj.getXyzt_id()));
		//判断是否在惩戒中
		String today = DateUtil.getToday();
		if(null != sxcj.getEnd_date() && sxcj.getEnd_date().length()>0){
			if(DateUtil.getDifferDays(sxcj.getStart_date(),today) > 0 &&
					DateUtil.getDifferDays(today,sxcj.getEnd_date()) > 0){
					sxcj.setType("0");  // 0 表示惩戒中
			}else if(DateUtil.getDifferDays(sxcj.getStart_date(),today) > 0 &&
					DateUtil.getDifferDays(sxcj.getEnd_date(),today) > 0){
				sxcj.setType("1");  // 0 表示惩戒结束
			}
		}else{
			sxcj.setType("0");  // 0 表示惩戒中
		}
		if(EmptyUtils.isEmpty(sxcj.getActor())){
			sxcj.setActor(user.getLoginname());
		}
		flag = daoCtl.update(dao, sxcj);
		String lognote=SysLogUtil.getLogNote(oldObj, sxcj, "sxcj");
		if (EmptyUtils.isNotEmpty(file.getFilename()) && EmptyUtils.isNotEmpty(file.getFilepath())) {
			dao.insert(file);
		}
		/**
		 * 同一合同对应多个信用主体的情况，修改其中任一信用主体的失信信息，在同一时间，同一合同id的情况下生成的失信惩戒信息都需修改
		 */
		if(flag){
			//获取同一合同，同时间生成的失信惩戒信息
			List<Discredit_info> disList = daoCtl.list(dao, Discredit_info.class, Sqls.create("select * from discredit_info where contract_id='"+sxcj.getContract_id()+"' and create_date='"+sxcj.getCreate_date()+"'"));
			for(Discredit_info discreditInfo : disList){
				sxcj.setId(discreditInfo.getId());
				sxcj.setXyzt_id(discreditInfo.getXyzt_id());
				daoCtl.update(dao, sxcj);
			}
		}
		SysLogUtil.addLogxg(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag,2, 
				"修改信用主体【"+xyzt.getName()+"】的失信惩戒记录", "失信惩戒", 
				lognote, reason, basis,url.replace("@id", sxcj.getId()+""),file.getId());
		return flag;
	}
	
	//失信情形查询
	@At
	@Ok("raw")
	public String sxqxAjax(@Param("sxxwid")String sxxwid) {
		System.out.println("In sxqxAjax..");
		Sql sql = Sqls.create("select id,sxqx,(case when cjcs is null then 0 else 1 end) cjcs from breach_info where is_use=1 and id like '"+sxxwid+"%'");
		List<Map> sxqxlist = daoCtl.list(dao, sql);
		System.out.println("sxqx:"+sql.toString());
		return sxqxlist+"";
	}
	
	@At
	@Ok("raw")
	public String yjAjax(@Param("id")String id){
		Sql sql = Sqls.create("select id,yj from breach_info where id like '"+id+"%' ");
		List<Map> yjlist = daoCtl.list(dao, sql);
		System.out.println("---》yj:"+yjlist);
		return yjlist+"";
	}
	
	//惩戒措施查询
	@At
	@Ok("raw")
	public String cjcsAjax(@Param("sxqxid")String sxqxid,@Param("sxqx_name") String sxqx_name) {
		String id = sxqxid.substring(0, 4);
		System.out.println("id:"+sxqxid+"---"+"name:"+sxqx_name);
		Sql sql = Sqls.create("select id,cjcs from breach_info where id like '"+id+"%' and sxqx = '"+sxqx_name+"'");
		List<Map> csyjlist = daoCtl.list(dao, sql);
		System.out.println("sxqx:"+csyjlist);
		return csyjlist+"";
	}
	
	//撤销 revoke撤销  release 发布
	@At
	@Ok("->:/private/sxcj/sxcjCancel.html")
	public void toCancel(@Param("sxcjid")String sxcjid,@Param("xytype")String xytype,HttpServletRequest req,HttpSession session,
			@Param("yyid") String yyid){
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Sys_unit unit = daoCtl.detailByName(dao, Sys_unit.class, user.getUnitid());
		int id = StringUtil.StringToInt(sxcjid);
		Discredit_info sxcj = daoCtl.detailById(dao, Discredit_info.class, id);
		//合同信息
		if(EmptyUtils.isNotEmpty(sxcj.getTablekey())){
			Sql sqlht = Sqls.create("select htmc from "+sxcj.getTablekey()+" where htid = '"+sxcj.getContract_id()+"' ");
			if("00030002".equals(sxcj.getXy_type())||"l_tkqsyf".equals(sxcj.getTablekey())){//采矿权或探矿权使用费
				String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(sxcj.getTablekey())));
				String xkzhSql="select xkzh from "+sxcj.getTablekey()+" where "+tablePK+" = '"+sxcj.getContract_id()+"' ";
				sqlht=Sqls.create(xkzhSql);
			}else if("l_tkht".equals(sxcj.getTablekey())){//探矿权合同
				String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(sxcj.getTablekey())));
				String xkzhSql="select kcxkz from "+sxcj.getTablekey()+" where "+tablePK+" = '"+sxcj.getContract_id()+"' ";
				sqlht=Sqls.create(xkzhSql);
			} 
			req.setAttribute("glxm", daoCtl.getStrRowValue(dao, sqlht));
			//获取主表的附表
			List<Form_table> tableList = daoCtl.list(dao, Form_table.class, Sqls.create("select * from form_table where formdefid=(select formdefid from form_table where tablekey='"+sxcj.getTablekey().substring(2)+"' and form_type=1) order by ismain desc"));
			if (tableList.size()>0) {
				for (Form_table table :tableList) {
					String tablename=CommonStaticUtil.TABLE_NAME_SUB+table.getTablekey();
					if(table.getIsmain() != 1){
						List<Map> ywlist=daoCtl.list(dao, Sqls.create("select * from "+tablename+" where ywid='"+sxcj.getYwid()+"'"));
						if(ywlist.size()>0){
							req.setAttribute("ywlist", ywlist);
						}
					}
				}
			}
			String defid = daoCtl.getStrRowValue(dao, Sqls.create("select formdefid from form_table where tablekey='"+sxcj.getTablekey().substring(2)+"' and form_type=1"));
			String url="/private/formyy/topreview?defid="+defid+"&mainid="+sxcj.getContract_id()+"&ywid="+sxcj.getYwid();
			req.setAttribute("url", url);
		}
		//信用主体
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, StringUtil.StringToInt(sxcj.getXyzt_id()));
		xyzt.setType((xyzt.getType()).trim());
		//信用主体类型
		Hashtable<String, String> zttype = comUtil.xyztlx;
		//失信行为
		Sql sqlxw = Sqls.create("select sxxw from breach_info where id = '"+sxcj.getSxxw_id()+"'");
		String sxxw = daoCtl.getStrRowValue(dao, sqlxw);
		//失信情形
		Breach_info binfo = daoCtl.detailBySql(dao,Breach_info.class, Sqls.create("select * from breach_info where id ='"+sxcj.getSxqx_id()+"'"));
		req.setAttribute("ztMap", zttype);
		req.setAttribute("sxcj", sxcj);
		req.setAttribute("xyzt", xyzt);
		req.setAttribute("sxxw", sxxw);
		req.setAttribute("binfo", binfo);
		String cjcs=Discredit_infoAction.getCjcs(dao,sxcj.getXy_type(),sxcj.getSxxw_id(),sxcjid);
		req.setAttribute("cjcs", cjcs);
		
		if(EmptyUtils.isNotEmpty(sxcj.getUnit()) && ( "0010".equals(sxcj.getUnit()) || "0011".equals(sxcj.getUnit()) || "0012".equals(sxcj.getUnit()) )) {
			req.setAttribute("cjjg", daoCtl.getHTable(dao,Sqls.create(" select id,name from sys_unit where id in ('0010','0011','0012') ")));
		}
		//判断情形是否需要进行约谈
		if(EmptyUtils.isNotEmpty(sxcj.getSxqx_id()) && sxcj.getSxqx_id().equals("00200001")){
			int count = daoCtl.getIntRowValue(dao, Sqls.create("select count(1) from discredit_info where xyzt_id = '"+sxcj.getXyzt_id()+"' and sxqx_id = '"+sxcj.getSxqx_id()+"'"));
			req.setAttribute("count", count);
		}
		req.setAttribute("yyid", yyid);
		
	}
	
	@At
	@Ok("raw")
	public boolean revoke(HttpSession session,@Param("xyzt_id") String xyzt_id,@Param("id") String sxcjid,@Param("note") String note,@Param("..") final File_info file,
			@Param("reason") String reason,@Param("basis") String basis,@Param("yyid") String yyid){
		Sys_user user = (Sys_user)session.getAttribute("userSession");
		if(EmptyUtils.isNotEmpty(yyid)){
			Warnexception warnexceptionInfo = daoCtl.detailByCnd(dao,Warnexception.class,Cnd.where("id","=",yyid));
			warnexceptionInfo.setCz_note(note);
			warnexceptionInfo.setCz_why(reason);
			warnexceptionInfo.setCz_yj(basis);
			warnexceptionInfo.setCz(3);
			String cz_name = "";
			Integer cz_type = 0;
			switch (warnexceptionInfo.getCz()){
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
				if(cz_type==4){
					//失信惩戒信息修改为结束状态
					daoCtl.update(dao, Discredit_info.class,Chain.make("type",1),Cnd.where("id","=",warnexceptionInfo.getSxcj_id()));
				}
				SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type,flag,cz_type, 
						"异议申请处理，执行【"+cz_name+"】","异议管理",note, reason, basis
						,"");
			}
		}
		boolean flag = false;
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, StringUtil.StringToInt(xyzt_id));
		int id = StringUtil.StringToInt(sxcjid);
		flag = daoCtl.exeUpdateBySql(dao, Sqls.create("update discredit_info set type=2,issue=0,actor='"+user.getLoginname()+"' where id = '"+id+"'"));
		/**
		 * 同一合同对应多个信用主体的情况，修改其中任一信用主体的失信信息，在同一时间，同一合同id的情况下生成的失信惩戒信息都需修改
		 */
		Discredit_info sxcj = daoCtl.detailById(dao, Discredit_info.class, id);
		if(flag){
			//获取同一合同，同时间生成的失信惩戒信息
			List<Discredit_info> disList = daoCtl.list(dao, Discredit_info.class, Sqls.create("select * from discredit_info where contract_id='"+sxcj.getContract_id()+"' and create_date='"+sxcj.getCreate_date()+"'"));
			for(Discredit_info discreditInfo : disList){
				daoCtl.exeUpdateBySql(dao, Sqls.create("update discredit_info set type=2,issue=0,actor='"+user.getLoginname()+"' where id = '"+discreditInfo.getId()+"'"));
			}
		}
		if (EmptyUtils.isNotEmpty(file.getFilename()) && EmptyUtils.isNotEmpty(file.getFilepath())) {
			dao.insert(file);
		}
		SysLogUtil.addLogxg(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag,4, 
				"撤销信用主体【"+xyzt.getName()+"】的失信惩戒记录", 
				"失信惩戒", ""+note+"", ""+reason+"", ""+basis+"",url.replace("@id", sxcjid),file.getId());
		return flag;
	}
	
	//标注
	@At
	@Ok("->:/private/sxcj/sxcjBz.html")
	public void toBz(@Param("sxcjid")String sxcjid,@Param("xytype")String xytype,HttpServletRequest req,HttpSession session,
			@Param("yyid") String yyid){
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Sys_unit unit = daoCtl.detailByName(dao, Sys_unit.class, user.getUnitid());
		int id = StringUtil.StringToInt(sxcjid);
		Discredit_info sxcj = daoCtl.detailById(dao, Discredit_info.class, id);
		//合同信息
		if(EmptyUtils.isNotEmpty(sxcj.getTablekey())){
			Sql sqlht = Sqls.create("select htmc from "+sxcj.getTablekey()+" where htid = '"+sxcj.getContract_id()+"' ");
			if("00030002".equals(sxcj.getXy_type())||"l_tkqsyf".equals(sxcj.getTablekey())){//采矿权或探矿权使用费
				String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(sxcj.getTablekey())));
				String xkzhSql="select xkzh from "+sxcj.getTablekey()+" where "+tablePK+" = '"+sxcj.getContract_id()+"' ";
				sqlht=Sqls.create(xkzhSql);
			}else if("l_tkht".equals(sxcj.getTablekey())){//探矿权合同
				String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(sxcj.getTablekey())));
				String xkzhSql="select kcxkz from "+sxcj.getTablekey()+" where "+tablePK+" = '"+sxcj.getContract_id()+"' ";
				sqlht=Sqls.create(xkzhSql);
			} 
			req.setAttribute("glxm", daoCtl.getStrRowValue(dao, sqlht));
		}
		//信用主体
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, StringUtil.StringToInt(sxcj.getXyzt_id()));
		xyzt.setType((xyzt.getType()).trim());
		//信用主体类型
		Hashtable<String, String> zttype = comUtil.xyztlx;
		//失信行为
		Sql sqlxw = Sqls.create("select sxxw from breach_info where id = '"+sxcj.getSxxw_id()+"'");
		String sxxw = daoCtl.getStrRowValue(dao, sqlxw);
		//失信情形
		Breach_info binfo = daoCtl.detailBySql(dao,Breach_info.class, Sqls.create("select * from breach_info where id ='"+sxcj.getSxqx_id()+"'"));
		req.setAttribute("ztMap", zttype);
		req.setAttribute("sxcj", sxcj);
		req.setAttribute("xyzt", xyzt);
		req.setAttribute("sxxw", sxxw);
		req.setAttribute("binfo", binfo);
		String cjcs=Discredit_infoAction.getCjcs(dao,xytype,sxcj.getSxxw_id(),sxcjid);
		req.setAttribute("cjcs", cjcs);
		if(EmptyUtils.isNotEmpty(sxcj.getUnit()) && ( "0010".equals(sxcj.getUnit()) || "0011".equals(sxcj.getUnit()) || "0012".equals(sxcj.getUnit()) )) {
			req.setAttribute("cjjg", daoCtl.getHTable(dao,Sqls.create(" select id,name from sys_unit where id in ('0010','0011','0012') ")));
		}
		//判断情形是否需要进行约谈
		if(EmptyUtils.isNotEmpty(sxcj.getSxqx_id()) && sxcj.getSxqx_id().equals("00200001")){
			int count = daoCtl.getIntRowValue(dao, Sqls.create("select count(1) from discredit_info where xyzt_id = '"+sxcj.getXyzt_id()+"' and sxqx_id = '"+sxcj.getSxqx_id()+"'"));
			req.setAttribute("count", count);
		}
		req.setAttribute("yyid", yyid);
	}
		
	@At
	@Ok("raw")
	public boolean biaozhu(HttpSession session,@Param("xyzt_id") int xyzt_id,@Param("id") String sxcjid,
			@Param("basis") String basis,	@Param("biaozhu")String biaozhu){
		boolean flag = false;
		Sys_user user = (Sys_user)session.getAttribute("userSession");
		int id = StringUtil.StringToInt(sxcjid);
		if(EmptyUtils.isNotEmpty(biaozhu)){
		Discredit_info disinfo=daoCtl.detailById(dao, Discredit_info.class,  id);
		//disinfo.setPerform_date(cflx_date);
		disinfo.setType("3");//暂停使用
		disinfo.setBiaozhu(biaozhu);
		if(biaozhu.equals("05")){
			String today = DateUtil.getToday();
			if(null != disinfo.getEnd_date() && disinfo.getEnd_date().length()>0){
				if(DateUtil.getDifferDays(disinfo.getStart_date(),today) > 0 &&
						DateUtil.getDifferDays(today,disinfo.getEnd_date()) > 0){
						disinfo.setType("0");  // 0 表示惩戒中
				}else if(DateUtil.getDifferDays(disinfo.getStart_date(),today) > 0 &&
						DateUtil.getDifferDays(disinfo.getEnd_date(),today) > 0){
					disinfo.setType("1");  // 0 表示惩戒结束
				}
			}else{
				disinfo.setType("0");  // 0 表示惩戒中
			}
		}else{
			disinfo.setType("3");//暂停使用
		}
		flag=daoCtl.update(dao, disinfo);
		/**
		 * 同一合同对应多个信用主体的情况，修改其中任一信用主体的失信信息，在同一时间，同一合同id的情况下生成的失信惩戒信息都需修改
		 */
		if(flag){
			//获取同一合同，同时间生成的失信惩戒信息
			List<Discredit_info> disList = daoCtl.list(dao, Discredit_info.class, Sqls.create("select * from discredit_info where contract_id='"+disinfo.getContract_id()+"' and create_date='"+disinfo.getCreate_date()+"'"));
			for(Discredit_info discreditInfo : disList){
				disinfo.setId(discreditInfo.getId());
				disinfo.setXyzt_id(discreditInfo.getXyzt_id());
				daoCtl.update(dao, disinfo);
			}
		}
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xyzt_id);
		SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag,4, 
				"标注信用主体【"+xyzt.getName()+"】的失信惩戒记录", 
				"失信惩戒", "","",""+basis+"",url.replace("@id", sxcjid));
		}
		return flag;
	}
	
	//浏览
	@At
	@Ok("->:/private/sxcj/sxcjDetail.html")
	public void detail(@Param("sxcjid") String sxcjid,@Param("xytype") String xytype,HttpServletRequest req,HttpSession session){
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		int id = StringUtil.StringToInt(sxcjid);
		//失信信息
		Discredit_info sxcj = daoCtl.detailById(dao, Discredit_info.class, id);
		//信用主体
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, StringUtil.StringToInt(sxcj.getXyzt_id()));
		//行政处罚
		Xzcf_info xzcf = null;
		if(null!=sxcj.getXzcf_id() && sxcj.getXzcf_id().length()>0){
			xzcf = daoCtl.detailById(dao, Xzcf_info.class, Integer.parseInt(sxcj.getXzcf_id()));
		}
		xyzt.setType((xyzt.getType()).trim());
		//合同信息
		if(EmptyUtils.isNotEmpty(sxcj.getTablekey())){
			Sql sqlht = Sqls.create("select htbh,htmc from "+sxcj.getTablekey()+" where htid = '"+sxcj.getContract_id()+"' ");
			if("00030002".equals(sxcj.getXy_type())||"l_tkqsyf".equals(sxcj.getTablekey())){//采矿权或探矿权使用费
				String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(sxcj.getTablekey())));
				String xkzhSql="select xkzh from "+sxcj.getTablekey()+" where "+tablePK+" = '"+sxcj.getContract_id()+"' ";
				sqlht=Sqls.create(xkzhSql);
			}else if("l_tkht".equals(sxcj.getTablekey())){//探矿权合同
				String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(sxcj.getTablekey())));
				String xkzhSql="select kcxkz from "+sxcj.getTablekey()+" where "+tablePK+" = '"+sxcj.getContract_id()+"' ";
				sqlht=Sqls.create(xkzhSql);
			}else if("l_tdfk".equals(sxcj.getTablekey())){//探矿权合同
				String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(sxcj.getTablekey())));
				String xkzhSql="select name from "+sxcj.getTablekey()+" where "+tablePK+" = '"+sxcj.getContract_id()+"' ";
				sqlht=Sqls.create(xkzhSql);
			}  
			req.setAttribute("glxm", daoCtl.getStrRowValue(dao, sqlht));
			//获取主表的附表
			List<Form_table> tableList = daoCtl.list(dao, Form_table.class, Sqls.create("select * from form_table where formdefid=(select formdefid from form_table where tablekey='"+sxcj.getTablekey().substring(2)+"' and form_type=1) order by ismain desc"));
			if (tableList.size()>0) {
				for (Form_table table :tableList) {
					String tablename=CommonStaticUtil.TABLE_NAME_SUB+table.getTablekey();
					if(table.getIsmain() != 1){
						List<Map> ywlist=daoCtl.list(dao, Sqls.create("select * from "+tablename+" where ywid='"+sxcj.getYwid()+"'"));
						if(ywlist.size()>0){
							req.setAttribute("ywlist", ywlist);
						}
					}
				}
			}
			String defid = daoCtl.getStrRowValue(dao, Sqls.create("select formdefid from form_table where tablekey='"+sxcj.getTablekey().substring(2)+"' and form_type=1"));
			String url="/private/formyy/topreview?defid="+defid+"&mainid="+sxcj.getContract_id()+"&ywid="+sxcj.getYwid();
			/*if("l_tkht".equals(sxcj.getTablekey())){
				url="/private/tkqsc/topreview?id="+sxcj.getContract_id()+"&ywid="+sxcj.getYwid();
			}else if("l_ckqht".equals(sxcj.getTablekey())){
				System.out.println("1111111111111111111111111111111");
				url="/private/ckqsc/topreview?id="+sxcj.getContract_id()+"&ywid="+sxcj.getYwid();
			}*/
			req.setAttribute("url", url);
		}
		if(sxcj.getXy_type().equals("00070001")){//农村土地整治
			String year=sxcj.getDiscipline_date().substring(0,4);
			String sqlstr="select count(1) from discredit_info where discipline_date like '"+year+"%' and type not in (2,3) and xyzt_id='"+sxcj.getXyzt_id()+"' and sxqx_id='"+sxcj.getSxqx_id()+"'";
			req.setAttribute("syfss", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr)));
			if(EmptyUtils.isNotEmpty(sxcj.getContract_id())){
				sqlstr="select count(1) from discredit_info where discipline_date like '"+year+"%' and contract_id='"+StringUtil.null2String(sxcj.getContract_id())+"' and type not in (2,3) and xyzt_id='"+sxcj.getXyzt_id()+"' and sxqx_id='"+sxcj.getSxqx_id()+"'";
				req.setAttribute("txmfss", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr)));
			}
			req.setAttribute("year", year);
		}
		//信用主体类型
		Hashtable<String, String> zttype = comUtil.xyztlx;
		//失信行为
		Sql sqlxw = Sqls.create("select sxxw from breach_info where id = '"+sxcj.getSxxw_id()+"'");
		String sxxw = daoCtl.getStrRowValue(dao, sqlxw);
		//失信情形
		Breach_info binfo = daoCtl.detailBySql(dao,Breach_info.class, Sqls.create("select * from breach_info where id ='"+sxcj.getSxqx_id()+"'"));
		//惩戒措施
		Hashtable<String, String> cjcsHt = daoCtl.getHTable(dao, Sqls.create("select id,cjcs from breach_info order by id desc"));
		String sxxw_id=sxcj.getSxxw_id();
		Breach_info breachinfo=null;
		String cjcs="";
		if("0008".equals(xytype)){
			String tongbao="";
			String chengjie="";
			String aa=daoCtl.getStrRowValue(dao, Sqls.create("select xylx from breach_info where id='"+sxxw_id+"'"));
			if(!"0008".equals(aa)){
				breachinfo=daoCtl.detailByName(dao, Breach_info.class, "0043");
				tongbao=breachinfo.getCjcs();
			}
			System.out.println("sxxw_id:"+sxxw_id);
			breachinfo=daoCtl.detailByName(dao, Breach_info.class, sxxw_id);
			chengjie=breachinfo.getCjcs();
			cjcs=chengjie;
			if(!"".equals(tongbao)){
				cjcs="1、"+tongbao+"(通报) 2、"+chengjie+"(惩戒中)";
			}
		}else if("00070001".equals(xytype)){//农村土地整治
			//获取该条失信惩戒信息的年份
			String year=sxcj.getDiscipline_date().substring(0,4);
			//获取该条失信惩戒信息所关联的项目
			String contractid=sxcj.getContract_id();
			//获取该条失信惩戒信息的信用主体
			String xyzt_id=sxcj.getXyzt_id();
			//获取该条失信惩戒信息的失信情形
			String sxqx_id=sxcj.getSxqx_id();
			String sqlstr1="select count(1) from discredit_info where discipline_date like '"+year+"%' and type not in (2,3) and xyzt_id='"+xyzt_id+"' and sxqx_id='"+sxqx_id+"'";
			req.setAttribute("syfss", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr1)));
			if(EmptyUtils.isNotEmpty(contractid)){
				sqlstr1="select count(1) from discredit_info where discipline_date like '"+year+"%' and contract_id='"+contractid+"' and type not in (2,3) and xyzt_id='"+xyzt_id+"' and sxqx_id='"+sxqx_id+"'";
				req.setAttribute("txmfss", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr1)));
			}
			breachinfo=daoCtl.detailByName(dao, Breach_info.class, sxxw_id);
			
			//根据信用主体来排序
			String sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxxw_id="+sxxw_id+" and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
			Map DisMap = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
			System.out.println("DisMap:"+DisMap.toString());
			//根据项目来排序
			String sqlxm="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxxw_id="+sxxw_id+" and contract_id='"+contractid+"' and type not in (2,3) ORDER BY create_date asc";
			Map XmMap = daoCtl.getHTable(dao,Sqls.create(sqlxm));
			int rownumber=0,xmnumber=0;
			if(DisMap.get(sxcjid)!=null){
				rownumber=Integer.valueOf(DisMap.get(sxcjid).toString());
			}
			if(XmMap.get(sxcjid)!=null){
				xmnumber=Integer.valueOf(XmMap.get(sxcjid).toString());
			}
			if(sxxw_id.equals("0027")){//设计义务
				int a=0,b=0,c=0,d=0,e=0,f=0,g=0;
				//查询土地设计义务0027
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=0027 and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
				Map map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					a=Integer.valueOf(map.get(sxcjid).toString());
				}
				//查询土地设计义务的00270001
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=00270001 and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
				map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					b=Integer.valueOf(map.get(sxcjid).toString());
				}
				//查询土地设计义务同信用主体的00270002
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=00270002 and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
				map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					c=Integer.valueOf(map.get(sxcjid).toString());
				}
				//查询土地设计义务的同项目的00270002
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=00270002 and contract_id='"+contractid+"' and type not in (2,3) ORDER BY create_date asc";
				map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					d=Integer.valueOf(map.get(sxcjid).toString());
				}
				//查询土地设计义务的00270003
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=00270003 and contract_id='"+contractid+"' and type not in (2,3) ORDER BY create_date asc";
				map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					e=Integer.valueOf(map.get(sxcjid).toString());
				}
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id in (0027,00270001,00270002) and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
				map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					f=Integer.valueOf(map.get(sxcjid).toString());
				}
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id in (00270002,00270003) and contract_id='"+contractid+"' and type not in (2,3) ORDER BY create_date asc";
				map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					g=Integer.valueOf(map.get(sxcjid).toString());
				}
				if(a>2||b>1||c>2||d>1||e>2||f>2||g>2){
					cjcs="国土资源管理部门不得委托其开展下一年度土地整治项目设计";
				}else{
					cjcs="国土资源管理部门予以通报批评1次";
				}
			}else if(sxxw_id.equals("0028")){//施工义务
				if(xmnumber<3){
					cjcs="国土资源管理部门予以通报批评1次";
				}else{
					cjcs="国土资源管理部门不得委托其开展下一年度土地整治项目设计";
				}
			}else if(sxxw_id.equals("0029")){//监理义务
				if(xmnumber>2||rownumber>5){
					cjcs="国土资源管理部门不得委托其开展下一年度土地整治项目设计";
				}else{
					cjcs="国土资源管理部门予以通报批评1次";
				}
			}
		}else{
			breachinfo=daoCtl.detailByName(dao, Breach_info.class, sxxw_id);
			cjcs=breachinfo.getCjcs();
		}
		//惩戒状态
		Sql sql = Sqls.create("SELECT value,name FROM CS_VALUE  WHERE typeid='00010007' AND length(code)=4 ORDER BY location ASC ");
		req.setAttribute("cjztTable", daoCtl.getHTable(dao, sql));
		//撤销原因
		String url="sxcjid="+id;
		if(EmptyUtils.isNotEmpty(sxcj.getXzcf_id())){
			url="xzcfid="+sxcj.getXzcf_id();
		}
		String csjlSql="select * from sys_log where cz='撤销' and substr(url,22,38)='"+url+"'";
		Sys_log syslog= daoCtl.detailBySql(dao, Sys_log.class, Sqls.create(csjlSql));
		req.setAttribute("syslog", syslog);
		req.setAttribute("ztMap", zttype);
		req.setAttribute("sxcj", sxcj);
		req.setAttribute("xyzt", xyzt);
		req.setAttribute("xzcf", xzcf);
		req.setAttribute("cjcsHt", cjcsHt);
		//req.setAttribute("fbMap", fbtype);
		req.setAttribute("sxxw", sxxw);
		req.setAttribute("binfo", binfo);
		req.setAttribute("cjcs", cjcs);
		if(EmptyUtils.isNotEmpty(sxcj.getUnit()) && ( "0010".equals(sxcj.getUnit()) || "0011".equals(sxcj.getUnit()) || "0012".equals(sxcj.getUnit()) )) {
			req.setAttribute("cjjg", daoCtl.getHTable(dao,Sqls.create(" select id,name from sys_unit where id in ('0010','0011','0012') ")));
		}
		req.setAttribute("qytypeMap", comUtil.qytype);
		req.setAttribute("qyztMap", comUtil.qyzt);
		req.setAttribute("sjlxqk", comUtil.sjlxqkMap);
		req.setAttribute("biaozhu", comUtil.biaozhuMap);
		//判断情形是否需要进行约谈
		if(EmptyUtils.isNotEmpty(sxcj.getSxqx_id()) && sxcj.getSxqx_id().equals("00200001")){
			int count = daoCtl.getIntRowValue(dao, Sqls.create("select count(1) from discredit_info where xyzt_id = '"+sxcj.getXyzt_id()+"' and sxqx_id = '"+sxcj.getSxqx_id()+"'"));
			req.setAttribute("count", count);
		}
	}
	
	//发布 revoke撤销  release 发布
	@At
	@Ok("raw")
	public boolean toIssue(@Param("sxcjid") String sxcjid,HttpSession session){
		Sys_user user = (Sys_user)session.getAttribute("userSession");
		boolean flag = true;
		Discredit_info sxcj_info = daoCtl.detailById(dao, Discredit_info.class, StringUtil.StringToInt(sxcjid));
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, StringUtil.StringToInt(sxcj_info.getXyzt_id()));
		flag = daoCtl.exeUpdateBySql(dao,Sqls.create("update discredit_info set issue='1',actor='"+user.getLoginname()+"'  where id='" + sxcjid + "'"));
		SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag,5,
				"发布信用主体【"+xyzt.getName()+"】的失信惩戒记录", "失信惩戒", "", "", "",url.replace("@id", sxcjid));
		return flag;
	}
	
	//验证是否有合同
	@At
	@Ok("raw")
	public String checkHt(@Param("xyml") String xyml){
		int count = YYFormAction.getHTS(daoCtl, dao, xyml);
		return count+"";
	}
	
	//查询约谈次数
	@At
	@Ok("raw")
	public String checkCount(@Param("xyzt_id") String xyzt_id,@Param("sxqxid") String sxqxid){
		int count = daoCtl.getIntRowValue(dao, Sqls.create("select count(1) from discredit_info where xyzt_id = '"+xyzt_id+"' and sxqx_id = '"+sxqxid+"'"));
		return count+"";
	}
	
	public static String getCjcs(Dao dao,String xytype,String sxxw_id,String sxcjid){
		String cjcs="";
		Breach_info breachinfo=null;
		int sxcj_id=Integer.valueOf(sxcjid);
		Discredit_info sxcj=daoCtl.detailById(dao, Discredit_info.class, sxcj_id);
		if(xytype.equals("0008")){
			String tongbao="";
			String chengjie="";
			String aa=daoCtl.getStrRowValue(dao, Sqls.create("select xylx from breach_info where id='"+sxxw_id+"'"));
			if(!"0008".equals(aa)){
				breachinfo=daoCtl.detailByName(dao, Breach_info.class, "0043");
				tongbao=breachinfo.getCjcs();
			}
			System.out.println("sxxw_id:"+sxxw_id);
			breachinfo=daoCtl.detailByName(dao, Breach_info.class, sxxw_id);
			chengjie=breachinfo.getCjcs();
			cjcs=chengjie;
			if(!"".equals(tongbao)){
				cjcs="1、"+tongbao+"(通报) 2、"+chengjie+"(惩戒中)";
			}
		}else if(xytype.equals("00070001")){//农村土地整治
			//获取该条失信惩戒信息的年份
			String year=sxcj.getDiscipline_date().substring(0,4);
			//获取该条失信惩戒信息所关联的项目
			String contractid=sxcj.getContract_id();
			//获取该条失信惩戒信息的信用主体
			String xyzt_id=sxcj.getXyzt_id();
			//获取该条失信惩戒信息的失信情形
			String sxqx_id=sxcj.getSxqx_id();
			String sqlstr1="select count(1) from discredit_info where discipline_date like '"+year+"%' and type not in (2,3) and xyzt_id='"+xyzt_id+"' and sxqx_id='"+sxqx_id+"'";
			if(EmptyUtils.isNotEmpty(contractid)){
				sqlstr1="select count(1) from discredit_info where discipline_date like '"+year+"%' and contract_id='"+contractid+"' and type not in (2,3) and xyzt_id='"+xyzt_id+"' and sxqx_id='"+sxqx_id+"'";
			}
			breachinfo=daoCtl.detailByName(dao, Breach_info.class, sxxw_id);
			
			//根据信用主体来排序
			String sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxxw_id="+sxxw_id+" and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
			Map DisMap = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
			System.out.println("DisMap:"+DisMap.toString());
			//根据项目来排序
			String sqlxm="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxxw_id="+sxxw_id+" and contract_id='"+contractid+"' and type not in (2,3) ORDER BY create_date asc";
			Map XmMap = daoCtl.getHTable(dao,Sqls.create(sqlxm));
			int rownumber=0,xmnumber=0;
			if(DisMap.get(sxcjid)!=null){
				rownumber=Integer.valueOf(DisMap.get(sxcjid).toString());
			}
			if(XmMap.get(sxcjid)!=null){
				xmnumber=Integer.valueOf(XmMap.get(sxcjid).toString());
			}
			if(sxxw_id.equals("0027")){//设计义务
				int a=0,b=0,c=0,d=0,e=0,f=0,g=0;
				//查询土地设计义务0027
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=0027 and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
				Map map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					a=Integer.valueOf(map.get(sxcjid).toString());
				}
				//查询土地设计义务的00270001
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=00270001 and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
				map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					b=Integer.valueOf(map.get(sxcjid).toString());
				}
				//查询土地设计义务同信用主体的00270002
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=00270002 and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
				map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					c=Integer.valueOf(map.get(sxcjid).toString());
				}
				//查询土地设计义务的同项目的00270002
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=00270002 and contract_id='"+contractid+"' and type not in (2,3) ORDER BY create_date asc";
				map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					d=Integer.valueOf(map.get(sxcjid).toString());
				}
				//查询土地设计义务的00270003
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id=00270003 and contract_id='"+contractid+"' and type not in (2,3) ORDER BY create_date asc";
				map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					e=Integer.valueOf(map.get(sxcjid).toString());
				}
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id in (0027,00270001,00270002) and xyzt_id="+xyzt_id+" and type not in (2,3) ORDER BY create_date asc";
				map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					f=Integer.valueOf(map.get(sxcjid).toString());
				}
				sqlxyzt="select id,row_number() over(ORDER BY create_date asc)as rownumber from discredit_info where  discipline_date like '"+year+"%' and sxqx_id in (00270002,00270003) and contract_id='"+contractid+"' and type not in (2,3) ORDER BY create_date asc";
				map = daoCtl.getHTable(dao,Sqls.create(sqlxyzt));
				if(map.get(sxcjid)!=null){
					g=Integer.valueOf(map.get(sxcjid).toString());
				}
				if(a>2||b>1||c>2||d>1||e>2||f>2||g>2){
					cjcs="国土资源管理部门不得委托其开展下一年度土地整治项目设计";
				}else{
					cjcs="国土资源管理部门予以通报批评1次";
				}
			}else if(sxxw_id.equals("0028")){//施工义务
				if(xmnumber<3){
					cjcs="国土资源管理部门予以通报批评1次";
				}else{
					cjcs="国土资源管理部门不得委托其开展下一年度土地整治项目设计";
				}
			}else if(sxxw_id.equals("0029")){//监理义务
				if(xmnumber>2||rownumber>5){
					cjcs="国土资源管理部门不得委托其开展下一年度土地整治项目设计";
				}else{
					cjcs="国土资源管理部门予以通报批评1次";
				}
			}
		}else{
			breachinfo=daoCtl.detailByName(dao, Breach_info.class, sxxw_id);
			cjcs=breachinfo.getCjcs();
		}
		return cjcs;
	}
	
}