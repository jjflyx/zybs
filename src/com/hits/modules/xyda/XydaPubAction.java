package com.hits.modules.xyda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jsoup.helper.DataUtil;
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
@At("/public/xyda")
@Filters({ @By(type = GlobalsFilter.class) })
public class XydaPubAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	
	//对外公开的信用信息查询
	@At
	@Ok("->:/public/list.html")
	public void list(HttpServletRequest req,@Param("xyzt_type") String xyzt_type,@Param("xytype") String xytype,
			@Param("xyzt_name") String xyzt_name,@Param("curPage") String curPage,@Param("jlcurPage") String jlcurPage,
			@Param("startdate") String startdate,@Param("enddate") String enddate,@Param("code") String code,@Param("jlhcj") String jlhcj){
		if(EmptyUtils.isEmpty(xytype)){
			xytype="0004";
		}
		List<Map> dhlMap = new ArrayList<Map>();//导航栏
		Sql dhlsql=Sqls.create("select code,name,value from cs_value where typeid='00010032'  and state=0 order by location asc,id asc ");
		dhlMap=daoCtl.list(dao, dhlsql);
		req.setAttribute("dhl", dhlMap);//导航栏
		dhlsql=Sqls.create("select code from cs_value where  value='"+xytype+"' and state=0 and typeid='00010032' order by location asc,id asc ");
		req.setAttribute("typecode", daoCtl.getStrRowValue(dao, dhlsql));//当前栏目的code
		req.setAttribute("csvalueList", comUtil.sxxwtypeList);//业务类别
		req.setAttribute("ztlxlist", comUtil.xyztlxMap);//主体类型
		req.setAttribute("lb", jlhcj);
		req.setAttribute("xytype", xytype);
		String jlxy_type=xytype;
		//失信惩戒
		Sql sql = null;
		String sqlstr = "select a.id as sxcjid,a.sxxw_id,a.xy_type,a.create_date,a.start_date,a.unit,a.end_date,a.type,a.issue,a.src,a.sxqx_id,a.xyzt_id,a.contract_id,a.discipline_date,a.xzcf_id,b.name,b.code,b.zzjgdm,b.company_addr,b.type as xyzt_type,"+
				"c.sxxw,c.cjcs,c.yj,a.perform_date,a.src_type,d.name as unitname,a.sxxw_other,a.sxqx_other,a.cjcs_other from discredit_info a,xyzt_info b,breach_info c,sys_unit d where a.xyzt_id = b.id and a.sxxw_id = c.id and a.unitid=d.id and a.type='0' ";
		if(EmptyUtils.isNotEmpty(xytype)){
			req.setAttribute("xytype", xytype);
			if(xytype.equals("0008")){
				//行政处罚（已撤销02，依法不再履行04不用显示在外网）
				sqlstr="select a.id as xzcfid,a.xzcf_note,a.au as sxxw,b.company_addr,b.name,b.code,b.zzjgdm,b.type as "
						+ "xyzt_type,a.xzcf_yiju,a.create_date,a.cfsx_date as start_date,c.name as unitname,a.end_date from xzcf_info a,xyzt_info b,sys_unit c where a.xyzt_id=b.id and a.cf_unit=c.id and a.xzcf_zt<>2 and sjlxqk<>'04'";
			}else if(xytype.equals("00110002")){
				//行政强制
				sqlstr="select a.id as xzqzid,a.jg_unitid,a.aj_name as sxxw,a.reason,b.company_addr,b.name,b.code,b.zzjgdm,b.type as "
						+ "xyzt_type,substr(a.create_date,0,10) as create_date from xzqz_info a,xyzt_info b where a.xyzt_id=b.id ";
			}else{
				sqlstr += " and a.xy_type like '"+xytype+"%'";
			}
		}else{
			sqlstr += " and a.xy_type like '0004'";
		}
		//信用主体类型
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			req.setAttribute("xyzt_type", xyzt_type);
			sqlstr += " and b.type = '"+xyzt_type+"'";
		}
		//统一社会信用代码或组织机构代码
		if(EmptyUtils.isNotEmpty(code)){
			req.setAttribute("code", code);
			sqlstr += " and (b.code like '%"+code+"%' or  b.zzjgdm like '%"+code+"%' or b.name like '%"+code+"%') ";
		}
		//信用主体名称
		if(EmptyUtils.isNotEmpty(xyzt_name)){
			sqlstr += " and b.name like '%"+xyzt_name+"%'";
		}
		if(EmptyUtils.isNotEmpty(xytype)){
			if(xytype.equals("0008")){
				sqlstr += " ORDER BY a.cfsx_date desc";
			}else if(xytype.equals("00110002")){
				sqlstr += " ORDER BY a.create_date desc";
			}else{
				sqlstr += " ORDER BY a.start_date desc";
			}
		}else{
			sqlstr += " ORDER BY a.start_date desc";
		}
		
		// 判断
		if (EmptyUtils.isEmpty(curPage) ){
			curPage = "1";  	// 第一次访问，设置当前页为1;
		}
		int currentPage = Integer.parseInt(curPage);
		int pageSize=10;
		String sqlrow="select count(1) from ("+sqlstr+")";
		sql = Sqls.create(sqlrow);
		//总的记录数
		int total=Integer.valueOf(daoCtl.getStrRowValue(dao, sql));
		//总页数
		int totalYs1=total/10+1;
		if(total%10==0){//取余为0不加1
			totalYs1=total/10;
		}
		int totalYs = (int)totalYs1;
		List<Map> dijiye = new ArrayList<Map>();
		for(int i=0;i<totalYs;i++){
			int j=i+1;
			Map whichPage=new HashMap();
			whichPage.put("value", j);
			whichPage.put("page", "第"+j+"页");
			dijiye.add(whichPage);
		}
		req.setAttribute("whichPage", dijiye);
		req.setAttribute("curPage", currentPage);//当前页
		req.setAttribute("lastPage", currentPage-1);//上一页
		req.setAttribute("curjls", (currentPage-1)*10);//当前记录数
		req.setAttribute("nextPage", currentPage+1);//下一页
		if(currentPage>=totalYs){
			req.setAttribute("next", 1);//下一页
		}
		req.setAttribute("totalYs", totalYs);//总页数
		req.setAttribute("total", total);//总记录数
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, currentPage, pageSize);
		List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();
		String currtoday=DateUtil.getToday();
		for(int i=0;i<list.size();i++){
			//判读三天内的标识
			String sxcurrday=StringUtil.null2String(list.get(i).get("create_date"));//失信日期
			sxcurrday=sxcurrday.substring(0, 10);
			String today=DateUtil.getToday();
			int xcts=DateUtil.getWorkDays(today, sxcurrday);//相差天数
			list.get(i).put("xcts", xcts);
			String cjyj=StringUtil.null2String(list.get(i).get("yj"));
			if(cjyj.length()>20){
				cjyj=cjyj.substring(0, 20)+"......";
			}
			list.get(i).put("cjyj", cjyj);
			String start_date = StringUtil.null2String(list.get(i).get("start_date"));
			String end_date = StringUtil.null2String(list.get(i).get("end_date"));
			if(EmptyUtils.isEmpty(xytype)){
				xytype= StringUtil.null2String(list.get(i).get("xy_type"));
			}
			if(end_date==null||"".equals(end_date)){
				if(xytype.equals("0008")){
					list.get(i).put("startDend", start_date);
				}else{
					list.get(i).put("startDend", start_date+" 起 ");
				}
				
			}else{
				list.get(i).put("startDend", start_date+" 至 "+(null == end_date?"":end_date));
			}
			String xyztlx=StringUtil.null2String(list.get(i).get("xyzt_type"));
			String frcode=StringUtil.null2String(list.get(i).get("code"));
			if(xyztlx.equals("0")){
				//法人身份证号中间部分显示*
				if(EmptyUtils.isNotEmpty(frcode)){
					frcode=frcode.substring(0, 6)+"********"+frcode.substring(frcode.length()-4,frcode.length());
				}
				list.get(i).put("code", frcode);
			}
			list.get(i).put("xyzt_type", list.get(i).get("xyzt_type").toString().trim());
			String sxxw_id=StringUtil.null2String(list.get(i).get("sxxw_id"));
			Breach_info breachinfo=null;
			
			if(xytype.equals("0008")||xytype.equals("00110002")){
				/*String cjcs="";
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
				list.get(i).put("sxxw", breachinfo.getSxxw());*/
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
		req.setAttribute("sxcjList", list);
		
		//奖励信息
		Sql sqljlxx = null;
		String sqljl = "select a.id as jlid,a.jl_date,b.name,b.code,b.zzjgdm,b.company_addr,b.type,a.note,a.xy_type,a.create_date,c.name as unitname from reward_info a,xyzt_info b,sys_unit c where a.xyzt_id = b.id and a.unitid=c.id and a.type = 0 ";
		
		if(EmptyUtils.isNotEmpty(xyzt_name)){
			sqljl += "and b.name like '%"+xyzt_name+"%'";
		}
		if(EmptyUtils.isNotEmpty(code)){
			sqljl += "and (b.code like '%"+code+"%' or b.zzjgdm like '%"+code+"%')";
		}
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqljl += "and b.type = '"+xyzt_type+"'";
		}
		if(EmptyUtils.isNotEmpty(jlxy_type)){
			sqljl += "and a.xy_type like '"+jlxy_type+"%'";
		}else{
			sqljl += "and a.xy_type like '0004'";
		}
		if(EmptyUtils.isNotEmpty(startdate)){
			sqljl += "and a.jl_date >= '"+startdate+"'";
		}
		if(EmptyUtils.isNotEmpty(enddate)){
			sqljl += "and a.jl_date <= '"+enddate+"'";
		}
		sqljl += " order by a.jl_date desc";
		System.out.println("sqlstr:"+sqljl);
		sqljlxx = Sqls.create(sqljl);
		// 判断
		if (EmptyUtils.isEmpty(jlcurPage) ){
			jlcurPage = "1";  	// 第一次访问，设置当前页为1;
		}
		int jlcurrentPage = Integer.parseInt(jlcurPage);
		String sqljlrow="select count(1) from ("+sqljl+")";//奖励信息的总记录数
		sql = Sqls.create(sqljlrow);
		//总的记录数
		int jlTotal=daoCtl.getIntRowValue(dao, sql);
		//总页数
		int jltotalYs1=jlTotal/10+1;
		if(jlTotal%10==0){//取余为0不加1
			jltotalYs1=jlTotal/10;
		}
		int jltotalYs = jltotalYs1;
		List<Map> jlwhichPage = new ArrayList<Map>();
		for(int i=0;i<jltotalYs;i++){
			int j=i+1;
			Map whichPage=new HashMap();
			whichPage.put("value", j);
			whichPage.put("page", "第"+j+"页");
			jlwhichPage.add(whichPage);
		}
		req.setAttribute("jlwhichPage", jlwhichPage);
		req.setAttribute("jlcurPage", jlcurrentPage);//当前页
		req.setAttribute("jllastPage", jlcurrentPage-1);//上一页
		req.setAttribute("jlcurjls", (jlcurrentPage-1)*10);//当前记录数
		req.setAttribute("jlnextPage", jlcurrentPage+1);//下一页
		req.setAttribute("jlTotal", jlTotal);//下一页
		if(jlcurrentPage>=jltotalYs){
			req.setAttribute("jlnext", 1);//下一页
		}
		req.setAttribute("jltotalYs", jltotalYs);//总页数
		QueryResult qrjlxx = daoCtl.listPageSql(dao, sqljlxx, jlcurrentPage, pageSize);
		List<Map<String,Object>> jllist = (List<Map<String, Object>>) qrjlxx.getList();
		req.setAttribute("jlxxList", jllist);
		for(int i=0;i<list.size();i++){
			//判读三天内的标识
			String jlcurrday=StringUtil.null2String(list.get(i).get("create_date"));//奖励日期
			jlcurrday=jlcurrday.substring(0, 10);
			String today=DateUtil.getToday();
			int xcts=DateUtil.getWorkDays(today, jlcurrday);//相差天数
			list.get(i).put("jlxcts", xcts);
		}
	}
	//双公示信息查询
		@At
		@Ok("->:/public/sgs_list.html")
		public void sgslist(HttpServletRequest req,@Param("gslx") String gslx,@Param("xdr") String xdr,@Param("wsh") String wsh,
				@Param("code") String code,@Param("curPage") String curPage){
			Sql sql = null;
			String sqlstr = "";
			String whereSql="";
			String orderSql="";
			if(EmptyUtils.isNotEmpty(gslx)&&"xzcf".equals(gslx)){//行政处罚
				
			}else{//行政许可
				sqlstr = "select * from l_gt_xzxkxx";
				if(EmptyUtils.isNotEmpty(xdr)){
					whereSql+="and xk_xdr like '%"+xdr+"%' ";
				}
				if(EmptyUtils.isNotEmpty(wsh)){
					whereSql+="and xk_wsh like '%"+wsh+"%' ";
				}
				if(EmptyUtils.isNotEmpty(whereSql)){
					whereSql=" where " +whereSql.replaceFirst("and", "");
				}
				orderSql=" order by xk_jdrq desc";
			}
			// 判断
			if (EmptyUtils.isEmpty(curPage) ){
				curPage = "1";  	// 第一次访问，设置当前页为1;
			}
			int currentPage = Integer.parseInt(curPage);
			int pageSize=10;
			int total=0;
			if(EmptyUtils.isNotEmpty(sqlstr)){
				String sqlrow="select count(1) from ("+sqlstr+whereSql+")";
				sql = Sqls.create(sqlrow);
				//总的记录数
				total=daoCtl.getIntRowValue(dao, sql);
			}
			//总页数
			int totalYs =total%10==0?total/10:(total/10+1);
			totalYs=totalYs>0?totalYs:1;
			List<Map> dijiye = new ArrayList<Map>();
			for(int i=0;i<totalYs;i++){
				int j=i+1;
				Map whichPage=new HashMap();
				whichPage.put("value", j);
				whichPage.put("page", "第"+j+"页");
				dijiye.add(whichPage);
			}
			req.setAttribute("whichPage", dijiye);
			req.setAttribute("curPage", currentPage);//当前页
			req.setAttribute("totalYs", totalYs);//总页数
			req.setAttribute("total", total);//总记录数
			req.setAttribute("gslx", gslx);
			req.setAttribute("wsh", wsh);
			req.setAttribute("xdr", xdr);
			if(EmptyUtils.isNotEmpty(sqlstr)){
//				System.out.println(sqlstr+whereSql+orderSql);
				sql = Sqls.create(sqlstr+whereSql+orderSql);
				QueryResult qr = daoCtl.listPageSql(dao, sql, currentPage, pageSize);
				List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();
				req.setAttribute("list", list);
			}
		}
	
}
