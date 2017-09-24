package com.hits.modules.xyda;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.lucene.util.TwoPhaseCommitTool.CommitFailException;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
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
import com.hits.modules.bean.Breach_info;
import com.hits.modules.cgtable.util.CommonStaticUtil;
import com.hits.modules.com.comUtil;
import com.hits.modules.gtxt.bean.Xyzt_info;
import com.hits.modules.sxcj.bean.Discredit_info;
import com.hits.modules.sys.WelcomeAction;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.DateUtil;
import com.hits.util.EmptyUtils;
import com.hits.util.PageObj;
import com.hits.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * @author hzg
 * @time 2016-03-07 09:00:21
 * 
 */

@IocBean
@At("/private/xyda")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Xyda_Action extends BaseAction{
	@Inject
	protected Dao dao;

	@At("/tolist")
	@Ok("->:/private/xyda/xydalist.html")
	public void user(HttpServletRequest req) {
		System.out.println("In xyda..");
		req.setAttribute("csvalueList", comUtil.sxxwtypeList);//业务类别
		req.setAttribute("ztMap", comUtil.xyztlxMap);//信用主体类型
		req.setAttribute("xyztlx", JSONObject.fromObject(comUtil.xyztlx));
	}
	
	//信用档案列表页面
	@At
	@Ok("raw")
	public String xydalist(HttpServletRequest req,@Param("xy_type") String xy_type,@Param("xyzt_type") String xyzt_type,@Param("xystatus") String xystatus,
			@Param("name") String name,@Param("page") int curPage, @Param("rows") int pageSize){
		Sql sql = null;
		/*String sqlstr = "select distinct a.id as xyzt_id,a.name,a.type as xyzt_type,a.code,a.zzjgdm from xyzt_info a,discredit_info b,reward_info c,"+
				"sys_unit d where a.modetype in (0,1)";
		//信用类型
		if(EmptyUtils.isNotEmpty(xy_type)){
			sqlstr += " and a.id=b.xyzt_id and a.id=c.xyzt_id and d.id in (b.unitid,c.unitid) and d.xy_type like '"+xy_type+"%'";
		}*/
		String sqlstr = "select distinct a.id as xyzt_id,a.name,a.type as xyzt_type,a.code,a.zzjgdm from xyzt_info a where 1=1 ";
		//信用类型
		if(EmptyUtils.isNotEmpty(xy_type)){
			sqlstr = "select distinct a.id as xyzt_id,a.name,a.type as xyzt_type,a.code,a.zzjgdm from xyzt_info a,"+
					" discredit_info b,reward_info c where 1=1 ";
			sqlstr += " and b.xyzt_id = a.id or c.xyzt_id = a.id and b.xy_type = '"+xy_type+"' or c.xy_type = '"+xy_type+"' ";
		}
		sqlstr = "select xyzt_id, xyzt_name,xyzt_code,xyzt_zzjgdm,xyzt_type,heib,hongb"+
				" from fr_xyxx_VIEW where 1=1";
		//信用主体类型
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr += " and xyzt_type = '"+xyzt_type+"'";
		}
		//信用主体名称
		if(EmptyUtils.isNotEmpty(name)){
			sqlstr += " and xyzt_name like '%"+name+"%' ";
		}
		if(EmptyUtils.isNotEmpty(xystatus)){
			if("1".equals(xystatus)){//黑榜
				sqlstr += " and heib>0";
			}else if("2".equals(xystatus)){//红榜
				sqlstr += " and hongb>0 ";
			}else if("3".equals(xystatus)){//正常
				sqlstr += " and hongb=0 and heib=0 ";
			}
		}
		sqlstr += " order by xyzt_id desc ";
		sql = Sqls.create(sqlstr);
		System.out.println(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map<String,String>> list = (List<Map<String, String>>) qr.getList();
		for(int i=0;i<list.size();i++){
			list.get(i).put("xyzt_type", list.get(i).get("xyzt_type").toString().trim());
			int heib=StringUtil.StringToInt(StringUtil.null2String(list.get(i).get("heib")),0);
			int hongb=StringUtil.StringToInt(StringUtil.null2String(list.get(i).get("hongb")),0);
			list.get(i).put("status",getStatusByCC(heib, hongb));
		}
		return PageObj.getPageJSON(qr);
	}
	
	//浏览
	@At
	@Ok("->:/private/xyda/xydaDetail.html")
	public void detailXyda(HttpSession session,HttpServletRequest req,@Param("xyzt_id") String xyzt_id){
		int id = StringUtil.StringToInt(xyzt_id);
		//信用主体信息
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, id);
		xyzt.setType(xyzt.getType().trim());
		//失信惩戒信息
		List<Map> sxcjlist = daoCtl.list(dao, Sqls.create("select * from discredit_info where xyzt_id = '"+xyzt_id+"' and type<>2"));
		//奖励荣誉信息
		List<Map> jlrylist = daoCtl.list(dao, Sqls.create("select * from reward_info where xyzt_id = '"+xyzt_id+"'"));
		//失信行为
		Hashtable<String, String> sxxwHt = daoCtl.getHTable(dao, Sqls.create("select id,sxxw from breach_info order by id desc"));
		String sxxw_id ="";
		//获取合同信息
		for(int i=0; i<sxcjlist.size();i++){
			sxxw_id = sxcjlist.get(i).get("sxxw_id").toString();
			if(sxxw_id.contains(",")){
				String []sxxwid=sxxw_id.split(",");
				if(sxxwid[0].equals("0043")){
					sxxw_id=sxxwid[1];
				}else{
					sxxw_id=sxxwid[0];
				}
			}
			sxcjlist.get(i).put("sxxw_id", sxxw_id);
			if(EmptyUtils.isNotEmpty(sxcjlist.get(i).get("contract_id"))){
				String ht_id = sxcjlist.get(i).get("contract_id").toString();
				String unitid = sxcjlist.get(i).get("unitid").toString();
				Sys_unit unit = daoCtl.detailByName(dao, Sys_unit.class, unitid);
				if (EmptyUtils.isNotEmpty(unit.getXy_type())) {
					String htbm = comUtil.htTableMap.get(unit.getXy_type());
					if (EmptyUtils.isNotEmpty(htbm)) {
						if(htbm.equals("l_ckqht")){
							
						}
						Sql sqlht = Sqls.create("select htbh,htmc,fj from "+htbm+" where htid = '"+ht_id+"'");
						List<Map> htlist = daoCtl.list(dao, sqlht);
						for(int j=0; j<htlist.size();j++){
							if(EmptyUtils.isNotEmpty(htlist.get(j).get("fj"))){
								String fj_id = htlist.get(j).get("fj").toString();
								Sql sqlfj = Sqls.create("select filename,filepath from file_info where id = '"+fj_id+"'");
								List<Map> fjlist = daoCtl.list(dao, sqlfj);
								sxcjlist.get(i).put("fjlist", fjlist);
							}
						}
						sxcjlist.get(i).put("htlist", htlist);
					}
				}
				
			}
		}
		//失信情形
		Hashtable<String,String> sxqxHt = daoCtl.getHTable(dao, Sqls.create("select id,sxqx from breach_info order by id desc"));
		//惩戒措施
		Hashtable<String, String> cjcsHt = daoCtl.getHTable(dao, Sqls.create("select id,cjcs from breach_info order by id desc"));
		//惩戒依据
		Hashtable<String, String> yjHt = daoCtl.getHTable(dao, Sqls.create("select id,yj from breach_info order by id desc"));
		req.setAttribute("xyzt", xyzt);
		req.setAttribute("sxcjlist", sxcjlist);
		req.setAttribute("jlrylist", jlrylist);
		req.setAttribute("sxxwHt", sxxwHt);
		req.setAttribute("sxqxHt", sxqxHt);
		req.setAttribute("cjcsHt", cjcsHt);
		req.setAttribute("yjHt", yjHt);
		req.setAttribute("ztMap", comUtil.xyztlx);
		req.setAttribute("qytypeMap", comUtil.qytype);
		req.setAttribute("qyztMap", comUtil.qyzt);
		//惩戒状态
		Sql sql1 = Sqls.create("select value,name from cs_value where typeid = 00010007");
		List<Map> cjtype = daoCtl.list(dao, sql1);
		Hashtable<String, String> cjTable = daoCtl.getHTable(dao, sql1);
		req.setAttribute("cjTable", JSONObject.fromObject(cjTable));
		req.setAttribute("cjMap", cjtype);
		
		//获取该信用主体的所有合同信息
		Sql sql=Sqls.create("select defid,'"+CommonStaticUtil.TABLE_NAME_SUB+"'||t.tablekey as htmc from form_def d,form_table t where t.ismain=1 and d.ywtype='1' and d.defid=t.formdefid order by defid");
		List<Map> htmcList=daoCtl.list(dao, sql);
		List<Map> htList=new ArrayList<Map>();
		for(int j=0;j<htmcList.size();j++){
			Sql ht = Sqls.create("select htid,htmc,htbh from "+htmcList.get(j).get("htmc")+" where xyzt = '"+xyzt_id+"' and status=1 ");
			if(htmcList.get(j).get("htmc").equals("l_ckqht")){
				ht = Sqls.create("select htid,htmc,htbh,bjh,xkzh from "+htmcList.get(j).get("htmc")+" where xyzt = '"+xyzt_id+"' and status=1");
			}else if(htmcList.get(j).get("htmc").equals("l_tkht")){
				ht = Sqls.create("select htid,htmc,htbh,bjh,kcxkz from "+htmcList.get(j).get("htmc")+" where xyzt = '"+xyzt_id+"' and status=1");
			}
			List<Map> htxx=daoCtl.list(dao, ht);
			for(int i=0;i<htxx.size();i++){
				String url="/private/formyy/topreview?defid="+htmcList.get(j).get("defid")+"&mainid="+htxx.get(i).get("htid");
				String isHtbh="";
				if(htmcList.get(j).get("htmc").toString().equals("l_tkht")){
					if(EmptyUtils.isEmpty(htxx.get(i).get("htbh"))){
						isHtbh="tk";
					}
					url="/private/tkqsc/topreview?id="+htxx.get(i).get("htid");
				}else if(htmcList.get(j).get("htmc").toString().equals("l_ckqht")){
					if(EmptyUtils.isEmpty(htxx.get(i).get("htbh"))){
						isHtbh="ck";
					}
					url="/private/ckqsc/topreview?id="+htxx.get(i).get("htid");
				}
				htxx.get(i).put("isHtbh", isHtbh);
				htxx.get(i).put("url", url);
				htList.add(htxx.get(i));
			}
		}
		req.setAttribute("htList", htList);
		
		//获取该信用主体的所有资质信息
		List<Map> zzList=new ArrayList<Map>();
		Sql zz=Sqls.create("select * from zz_info where xyzt_id="+xyzt_id);
		List<Map> zzxx=daoCtl.list(dao, zz);
		for(int i=0;i<zzxx.size();i++){
			String url="/private/zzxx/tdpg/detail?zzid="+zzxx.get(i).get("id");
			zzxx.get(i).put("url", url);
			zzList.add(zzxx.get(i));
			if(zzxx.get(i).get("xy_type").equals("00050003")){
				String zyfw=zzxx.get(i).get("zyfw").toString();
				if(EmptyUtils.isNotEmpty(zyfw)&&zyfw.length()==6){
					if(zyfw.endsWith("0000")){
						zzxx.get(i).put("zyfw",comUtil.xzqh.get(zyfw));
					}else if(zyfw.endsWith("00")){
						zzxx.get(i).put("zyfw",comUtil.xzqh.get(zyfw.substring(0,2)+"0000")+comUtil.xzqh.get(zyfw));
					}else{
						zzxx.get(i).put("zyfw",comUtil.xzqh.get(zyfw.substring(0,2)+"0000")+comUtil.xzqh.get(zyfw.substring(0,4)+"00")+comUtil.xzqh.get(zyfw));
					}
				}
			}
		}
	}
	
	//信用信息动态
	@At
	@Ok("->:/private/xyda/xyxxdtlist.html")
	public void xyxxdt(HttpServletRequest req,@Param("xy_type") String xy_type,@Param("type") String type){
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
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
	}
	
	
	//信用信息动态
	@At
	@Ok("raw")
	public String xyxxsblist(HttpServletRequest req,@Param("xy_type") String xy_type,@Param("xyzt_type") String xyzt_type,@Param("xystatus") String xystatus,
			@Param("name") String name,@Param("page") int curPage, @Param("rows") int pageSize){
		Sql sql = null;
		String sqlstr = "select xyzt_id, xyzt_name,xyzt_code,xyzt_zzjgdm,xyzt_type,heib,hongb"+
				" from fr_xyxx_VIEW where (xyzt_id in (select distinct(xyzt_id) from discredit_info where xy_type='"+xy_type+"' and type<>2) or xyzt_id in(select distinct(xyzt_id) from reward_info where xy_type='"+xy_type+"'))";
		//信用主体类型
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr += " and xyzt_type = '"+xyzt_type+"'";
		}
		//信用主体名称
		if(EmptyUtils.isNotEmpty(name)){
			sqlstr += " and xyzt_name like '%"+name+"%' ";
		}
		if(EmptyUtils.isNotEmpty(xystatus)){
			if("1".equals(xystatus)){//黑榜
				sqlstr += " and heib>0";
			}else if("2".equals(xystatus)){//红榜
				sqlstr += " and hongb>0 ";
			}else if("3".equals(xystatus)){//正常
				sqlstr += " and hongb=0 and heib=0 ";
			}
		}
		sqlstr += " order by xyzt_id desc ";
		sql = Sqls.create(sqlstr);
		System.out.println(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map<String,String>> list = (List<Map<String, String>>) qr.getList();
		for(int i=0;i<list.size();i++){
			list.get(i).put("xyzt_type", list.get(i).get("xyzt_type").toString().trim());
			int heib=StringUtil.StringToInt(StringUtil.null2String(list.get(i).get("heib")),0);
			int hongb=StringUtil.StringToInt(StringUtil.null2String(list.get(i).get("hongb")),0);
			list.get(i).put("status",getStatusByCC(heib, hongb));
		}
		return PageObj.getPageJSON(qr);
	}
	
	private static String getStatusByCC(int heib,int hongb){
		String status="";
		if(heib>0&&hongb>0){//既有黑榜又有红榜
			status="黑榜|红榜";
		}else if(heib>0){
			status="黑榜";
		}else if(hongb>0){
			status="红榜";
		}else{
			status="正常";
		}
		return status;
	}
	
}
