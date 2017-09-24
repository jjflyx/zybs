package com.hits.modules.sys;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.hits.common.dao.ObjectCtl;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.util.StringUtil;
import com.hits.modules.bean.Cs_value;
import com.hits.modules.com.comUtil;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;
import com.hits.util.PageObj;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 * 
 */
@IocBean
@At("/private/welcome")
@Filters({ @By(type = GlobalsFilter.class) })
public class WelcomeAction extends BaseAction {
	private static Map<Integer,String> fieldMap=new HashMap<Integer,String>();//field
	private static Map<Integer,String> titleMap=new HashMap<Integer,String>();//title
	private static Map<Integer,String> widthMap=new HashMap<Integer,String>();//width
	@Inject
	protected Dao dao;
	
	@At
	@Ok("->:/private/welcome/zzxyxx.html")
	public void zzxyxx(@Param("xy_type") String xy_type,HttpServletRequest req,HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String xzqhcode=daoCtl.detailByName(dao, Sys_unit.class, user.getUnitid()).getXzqh();
		String xzqhname=daoCtl.detailBySql(dao, Cs_value.class, Sqls.create("select * from cs_value where typeid='00010004' and value='"+xzqhcode+"'")).getName();
		req.setAttribute("xzqhCode", xzqhcode);
		req.setAttribute("xzqhname", xzqhname);
		
		initFMap(xy_type);
		req.setAttribute("titleMap", titleMap);
		req.setAttribute("fieldMap", fieldMap);
		req.setAttribute("widthMap", widthMap);
		req.setAttribute("xy_type", xy_type);
	}
	
	@At
	@Ok("->:/private/welcome/htxyxx.html")
	public void htxyxx(@Param("xy_type") String xy_type,HttpServletRequest req,HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String xzqhcode=daoCtl.detailByName(dao, Sys_unit.class, user.getUnitid()).getXzqh();
		String xzqhname=daoCtl.detailBySql(dao, Cs_value.class, Sqls.create("select * from cs_value where typeid='00010004' and value='"+xzqhcode+"'")).getName();
		req.setAttribute("xzqhCode", xzqhcode);
		req.setAttribute("xzqhname", xzqhname);
		initFMap(xy_type);
		req.setAttribute("titleMap", titleMap);
		req.setAttribute("fieldMap", fieldMap);
		req.setAttribute("widthMap", widthMap);
		req.setAttribute("xy_type", xy_type);
	}
	
	private static void initFMap(String xy_type){
		if("00050005".equals(xy_type)||"00070002".equals(xy_type)){//地质灾害评估|地质灾害治理
			fieldMap.clear();titleMap.clear();widthMap.clear();
			fieldMap.put(1,"xyzt_name");
			titleMap.put(1,"信用主体");
			widthMap.put(1,"0.25");
			fieldMap.put(2,"zz_lb");
			titleMap.put(2,"资质类别");
			widthMap.put(2,"0.15");
			fieldMap.put(3,"zz_jb");
			titleMap.put(3,"资质级别");
			widthMap.put(3,"0.06");
			fieldMap.put(4,"zz_code");
			titleMap.put(4,"资质证书号");
			widthMap.put(4,"0.22");
			fieldMap.put(5,"yxq");
			titleMap.put(5,"资质有效期");
			widthMap.put(5,"0.14");
			fieldMap.put(6,"status");
			titleMap.put(6,"信用状态");
			widthMap.put(6,"0.08");
		}else if("00050001".equals(xy_type)){//土地价格评估
			fieldMap.clear();titleMap.clear();widthMap.clear();
			fieldMap.put(1,"xyzt_name");
			titleMap.put(1,"信用主体");
			widthMap.put(1,"0.22");
			fieldMap.put(2,"xyzt_code");
			titleMap.put(2,"统一社会信用代码");
			widthMap.put(2,"0.15");
			fieldMap.put(3,"zz_unit");
			titleMap.put(3,"发证单位");
			widthMap.put(3,"0.2");
			fieldMap.put(4,"zz_code");
			titleMap.put(4,"资质证书号");
			widthMap.put(4,"0.15");
			fieldMap.put(5,"zyfw");
			titleMap.put(5,"执业范围");
			widthMap.put(5,"0.2");
			fieldMap.put(6,"status");
			titleMap.put(6,"信用状态");
			widthMap.put(6,"0.08");
		}else if("00050002".equals(xy_type)){//土地规划服务
			fieldMap.clear();titleMap.clear();widthMap.clear();
			fieldMap.put(1,"xyzt_name");
			titleMap.put(1,"信用主体");
			widthMap.put(1,"0.25");
			fieldMap.put(2,"xyzt_code");
			titleMap.put(2,"统一社会信用代码");
			widthMap.put(2,"0.15");
			fieldMap.put(3,"zz_name");
			titleMap.put(3,"资质名称");
			widthMap.put(3,"0.2");
			fieldMap.put(4,"zz_jb");
			titleMap.put(4,"资质级别");
			widthMap.put(4,"0.22");
			fieldMap.put(5,"status");
			titleMap.put(5,"信用状态");
			widthMap.put(5,"0.08");
		}else if("00050003".equals(xy_type)){//土地登记代理服务
			fieldMap.clear();titleMap.clear();widthMap.clear();
			fieldMap.put(1,"xyzt_name");
			titleMap.put(1,"信用主体");
			widthMap.put(1,"0.25");
			fieldMap.put(2,"xyzt_type");
			titleMap.put(2,"类型");
			widthMap.put(2,"0.15");
			fieldMap.put(3,"xyzt_code");
			titleMap.put(3,"统一社会信用代码");
			widthMap.put(3,"0.15");
			fieldMap.put(4,"zz_name");
			titleMap.put(4,"资质名称");
			widthMap.put(4,"0.12");
			fieldMap.put(5,"zyfw");
			titleMap.put(5,"执业范围");
			widthMap.put(5,"0.12");
			fieldMap.put(6,"status");
			titleMap.put(6,"信用状态");
			widthMap.put(6,"0.08");
		}else if("00050004".equals(xy_type)){//矿业权价款评估
			fieldMap.clear();titleMap.clear();widthMap.clear();
			fieldMap.put(1,"xyzt_name");
			titleMap.put(1,"信用主体");
			widthMap.put(1,"0.25");
			fieldMap.put(2,"xyzt_code");
			titleMap.put(2,"统一社会信用代码");
			widthMap.put(2,"0.15");
			fieldMap.put(3,"zz_name");
			titleMap.put(3,"资质名称");
			widthMap.put(3,"0.2");
			fieldMap.put(4,"zz_jb");
			titleMap.put(4,"资质级别");
			widthMap.put(4,"0.22");
			fieldMap.put(5,"status");
			titleMap.put(5,"信用状态");
			widthMap.put(5,"0.08");
		}else if("00070001".equals(xy_type)){//农村土地整治
			fieldMap.clear();titleMap.clear();widthMap.clear();
			fieldMap.put(1,"xyzt_name");
			titleMap.put(1,"信用主体");
			widthMap.put(1,"0.2");
			fieldMap.put(2,"xyzt_code");
			titleMap.put(2,"统一社会信用代码");
			widthMap.put(2,"0.15");
			fieldMap.put(3,"zz_name");
			titleMap.put(3,"资质名称");
			widthMap.put(3,"0.2");
			fieldMap.put(4,"zz_lb");
			titleMap.put(4,"资质类别");
			widthMap.put(4,"0.15");
			fieldMap.put(5,"zz_jb");
			titleMap.put(5,"资质级别");
			widthMap.put(5,"0.1");
			fieldMap.put(6,"status");
			titleMap.put(6,"信用状态");
			widthMap.put(6,"0.08");
		}else if("00070003".equals(xy_type)){//矿山地质环境治理
			fieldMap.clear();titleMap.clear();widthMap.clear();
			fieldMap.put(1,"xyzt_name");
			titleMap.put(1,"信用主体");
			widthMap.put(1,"0.25");
			fieldMap.put(2,"xyzt_code");
			titleMap.put(2,"统一社会信用代码");
			widthMap.put(2,"0.15");
			fieldMap.put(3,"zz_name");
			titleMap.put(3,"资质名称");
			widthMap.put(3,"0.2");
			fieldMap.put(4,"zz_jb");
			titleMap.put(4,"资质级别");
			widthMap.put(4,"0.22");
			fieldMap.put(5,"status");
			titleMap.put(5,"信用状态");
			widthMap.put(5,"0.08");
		}else if("00070004".equals(xy_type)){//实施地质勘察
			fieldMap.clear();titleMap.clear();widthMap.clear();
			fieldMap.put(1,"xyzt_name");
			titleMap.put(1,"信用主体");
			widthMap.put(1,"0.25");
			fieldMap.put(2,"xyzt_code");
			titleMap.put(2,"统一社会信用代码");
			widthMap.put(2,"0.15");
			fieldMap.put(3,"zz_code");
			titleMap.put(3,"资质证书号");
			widthMap.put(3,"0.2");
			fieldMap.put(4,"zz_lb");
			titleMap.put(4,"资质类别");
			widthMap.put(4,"0.12");
			fieldMap.put(5,"zz_jb");
			titleMap.put(5,"资质级别");
			widthMap.put(5,"0.1");
			fieldMap.put(6,"status");
			titleMap.put(6,"信用状态");
			widthMap.put(6,"0.08");
		}else if("0006".equals(xy_type)){//测绘地理市场信用
			fieldMap.clear();titleMap.clear();widthMap.clear();
			fieldMap.put(1,"xyzt_name");
			titleMap.put(1,"信用主体");
			widthMap.put(1,"0.25");
			fieldMap.put(2,"xyzt_code");
			titleMap.put(2,"统一社会信用代码");
			widthMap.put(2,"0.15");
			fieldMap.put(3,"zz_code");
			titleMap.put(3,"资质证书号");
			widthMap.put(3,"0.2");
			fieldMap.put(4,"zz_jb");
			titleMap.put(4,"资质级别");
			widthMap.put(4,"0.22");
			fieldMap.put(5,"status");
			titleMap.put(5,"信用状态");
			widthMap.put(5,"0.08");
		}else if("0004".equals(xy_type)||"00030001".equals(xy_type)||"00030002".equals(xy_type)||"00050006".equals(xy_type)||"00050007".equals(xy_type)||"0008".equals(xy_type)||"0009".equals(xy_type)){//不含法人资质
			fieldMap.clear();titleMap.clear();widthMap.clear();
			fieldMap.put(1,"xyzt_name");
			titleMap.put(1,"信用主体");
			widthMap.put(1,"0.3");
			fieldMap.put(2,"xyzt_type");
			titleMap.put(2,"类型");
			widthMap.put(2,"0.15");
			fieldMap.put(3,"xyzt_code");
			titleMap.put(3,"统一社会信用代码");
			widthMap.put(3,"0.3");
			fieldMap.put(4,"status");
			titleMap.put(4,"信用状态");
			widthMap.put(4,"0.15");
		}
	}
	@At
	@Ok("raw")
	public String zzxyList(HttpServletRequest req,HttpSession session,@Param("xy_type") String xy_type,@Param("page") int curPage, @Param("sort") String sort,@Param("order") String order,@Param("rows") int pageSize){
		QueryResult qr=null;
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String zz_code=StringUtil.null2String(req.getParameter("zz_code"));
		String zz_jb=StringUtil.null2String(req.getParameter("zz_jb"));
		String zz_lb=StringUtil.null2String(req.getParameter("zz_lb"));
		String xyzt_name=StringUtil.null2String(req.getParameter("xyzt_name"));
		String xzqh=StringUtil.null2String(req.getParameter("xzqh"));
		String zz_name=StringUtil.null2String(req.getParameter("zz_name"));
		String xystatus=StringUtil.null2String(req.getParameter("xystatus"));
		String startdate=StringUtil.null2String(req.getParameter("startdate"));
		String enddate=StringUtil.null2String(req.getParameter("enddate"));
		try {
			Sql sql = null;
			String sqlstr = "select  zzid,'正常' status,zz_name,zyfw, zz_code,zz_unit,xyzt_id,zz_jb,zz_lb,xyzt_name,xyzt_code,xyzt_type,start_date||'至'||end_date as yxq,heib,hongb"+
					" from frzz_xyxx_view where xy_type = '"+xy_type+"'";
			System.out.println("xzqh:"+xzqh);
			if(EmptyUtils.isNotEmpty(xzqh)){
				if(xzqh.substring(2,xzqh.length()).equals("0000")){//省
					xzqh = xzqh.substring(0,2)+"__00";
//					sqlstr += " and xzqh like '"+xzqh.substring(0,2)+"%' ";
				}else if(xzqh.substring(4,xzqh.length()).equals("00")){//市
					sqlstr += " and xzqh like '"+xzqh.substring(0,4)+"%' ";
				}else{//县区
					sqlstr += " and xzqh = '"+xzqh+"' ";
				}
			}else{
				String xzqhcode=daoCtl.detailByName(dao, Sys_unit.class, user.getUnitid()).getXzqh();
				if(xzqhcode.substring(2,xzqhcode.length()).equals("0000")){//省
					xzqhcode = xzqhcode.substring(0,2)+"__00";
//					sqlstr += " and xzqh like '"+xzqh.substring(0,2)+"%' ";
				}else if(xzqhcode.substring(4,xzqhcode.length()).equals("00")){//市
					sqlstr += " and xzqh like '"+xzqhcode.substring(0,4)+"%' ";
				}else{//县区
					sqlstr += " and xzqh = '"+xzqhcode+"' ";
				}
			}
			if(EmptyUtils.isNotEmpty(xyzt_name)){
				sqlstr += " and xyzt_name like '%"+xyzt_name+"%' ";
			}
			if(EmptyUtils.isNotEmpty(zz_code)){
				sqlstr += " and zz_code like '%"+zz_code+"%' ";
			}
			if(EmptyUtils.isNotEmpty(startdate)){
				sqlstr += " and start_date > '"+startdate+"' ";
			}
			if(EmptyUtils.isNotEmpty(enddate)){
				sqlstr += " and end_date > '"+enddate+"' ";
			}
			if(EmptyUtils.isNotEmpty(zz_jb)){
				sqlstr += " and zz_jb = '"+zz_jb+"'";
			}
			if(EmptyUtils.isNotEmpty(zz_lb)){
				sqlstr += " and zz_lb like '"+zz_lb+"%'";
			}
			if(EmptyUtils.isNotEmpty(zz_name)){
				sqlstr += " and zz_name = '"+zz_name+"'";
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
			if(EmptyUtils.isNotEmpty(sort)&&EmptyUtils.isNotEmpty(order)){
				sqlstr += " order by "+sort+" "+order+" ";
			}else{
				sqlstr += " order by zzid desc";
			}
			sql = Sqls.create(sqlstr);
			System.out.println(sqlstr);
			qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
			List<Map<String,String>> list = (List<Map<String, String>>) qr.getList();
			//重新组装数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PageObj.getPageJSON(qr);
	}
	
	//获取带合同的信用信息
	@At
	@Ok("raw")
	public String htxyList(HttpServletRequest req,HttpSession session,@Param("xy_type") String xy_type,
			@Param("page") int curPage, @Param("sort") String sort,@Param("order") String order,@Param("rows") int pageSize){
		QueryResult qr=null;
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String xyzt_name=StringUtil.null2String(req.getParameter("xyzt_name"));
		String xzqh=StringUtil.null2String(req.getParameter("xzqh"));
		String xystatus=StringUtil.null2String(req.getParameter("xystatus"));
		String xyzt_code=StringUtil.null2String(req.getParameter("xyzt_code"));
		try {
			Sql sql = null;
			String sqlstr = "select distinct(a.xyzt_id), a.xyzt_name,a.xyzt_code,a.xyzt_zzjgdm,a.xyzt_type,a.heib,a.hongb"+
					" from fr_xyxx_VIEW a,sys_unit b,sys_user c,";
			if(xy_type.equals("0004")){//含土地市场合同
				sqlstr +="l_tdscht d where a.xyzt_id=d.xyzt and  d.actor =c.loginname and c.unitid=b.id ";
			}else if(xy_type.equals("00030002")){//含采矿权合同
				sqlstr +="l_ckqht d where a.xyzt_id=d.xyzt and  d.actor =c.loginname and c.unitid=b.id ";
			}else if(xy_type.equals("00030001")){//含探矿权合同
				sqlstr +="l_tkht d where a.xyzt_id=d.xyzt and  d.actor =c.loginname and c.unitid=b.id ";
			}else if(xy_type.equals("00050006")){//含国土资源监测
				sqlstr +="l_jcfw d where a.xyzt_id=d.xyzt and  d.actor =c.loginname and c.unitid=b.id ";
			}else if(xy_type.equals("00050007")){//含科技项目合同
				sqlstr +="l_kjxmht d where a.xyzt_id=d.xyzt and  d.actor =c.loginname and c.unitid=b.id ";
			}else if(xy_type.equals("0009")){//含复垦信用合同
				sqlstr +="l_tdfk d where a.xyzt_id=d.xyzt and  d.actor =c.loginname and c.unitid=b.id ";
			}else if(xy_type.equals("0008")){//行政处罚
				sqlstr +="xzcf_info d where a.xyzt_id=d.xyzt_id and  d.actor =c.loginname and c.unitid=b.id ";
			}
			System.out.println("xzqh:"+xzqh);
			if(EmptyUtils.isNotEmpty(xzqh)){
				if(xzqh.substring(2,xzqh.length()).equals("0000")){//省
					xzqh = xzqh.substring(0,2)+"__00";
//					sqlstr += " and xzqh like '"+xzqh.substring(0,2)+"%' ";
				}else if(xzqh.substring(4,xzqh.length()).equals("00")){//市
					sqlstr += " and b.xzqh like '"+xzqh.substring(0,4)+"%' ";
				}else{//县区
					sqlstr += " and b.xzqh = '"+xzqh+"' ";
				}
			}else{
				String xzqhcode=daoCtl.detailByName(dao, Sys_unit.class, user.getUnitid()).getXzqh();
				if(xzqhcode.substring(2,xzqhcode.length()).equals("0000")){//省
					xzqhcode = xzqhcode.substring(0,2)+"__00";
//					sqlstr += " and xzqh like '"+xzqh.substring(0,2)+"%' ";
				}else if(xzqhcode.substring(4,xzqhcode.length()).equals("00")){//市
					sqlstr += " and b.xzqh like '"+xzqhcode.substring(0,4)+"%' ";
				}else{//县区
					sqlstr += " and b.xzqh = '"+xzqhcode+"' ";
				}
			}
			if(EmptyUtils.isNotEmpty(xyzt_code)){
				sqlstr += " and a.xyzt_code like '%"+xyzt_code+"%' ";
			}
			if(EmptyUtils.isNotEmpty(xyzt_name)){
				sqlstr += " and a.xyzt_name like '%"+xyzt_name+"%' ";
			}
			
			if(EmptyUtils.isNotEmpty(xystatus)){
				if("1".equals(xystatus)){//黑榜
					sqlstr += " and a.heib>0";
				}else if("2".equals(xystatus)){//红榜
					sqlstr += " and a.hongb>0 ";
				}else if("3".equals(xystatus)){//正常
					sqlstr += " and a.hongb=0 and a.heib=0 ";
				}
			}
			if(EmptyUtils.isNotEmpty(sort)&&EmptyUtils.isNotEmpty(order)){
				sqlstr += " order by "+sort+" "+order+" ";
			}else{
				sqlstr += " order by a.xyzt_id desc";
			}
			sql = Sqls.create(sqlstr);
			System.out.println(sqlstr);
			qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
			List<Map<String,String>> list = (List<Map<String, String>>) qr.getList();
			//重新组装数据
			for(int i=0;i<list.size();i++){
				list.get(i).put("xyzt_type", comUtil.xyztlx.get(StringUtil.null2String(list.get(i).get("xyzt_type"))));
				int heib=StringUtil.StringToInt(StringUtil.null2String(list.get(i).get("heib")),0);
				int hongb=StringUtil.StringToInt(StringUtil.null2String(list.get(i).get("hongb")),0);
				list.get(i).put("status",getStatusByCC(heib, hongb));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return PageObj.getPageJSON(qr);
	}
	
	/*
	 * 根据信用主体id,查询信用状态
	 */
	private static String getStatusById(ObjectCtl daoCtl,Dao dao,String xyzt_id){
		String sqlstr="select count(1) from discredit_info where xyzt_id='"+xyzt_id+"'";
		int heib=daoCtl.getIntRowValue(dao, Sqls.create(sqlstr));
		sqlstr="select count(1) from reward_info where xyzt_id='"+xyzt_id+"'";
		int hongb=daoCtl.getIntRowValue(dao, Sqls.create(sqlstr));
		String status=getStatusByCC(heib, hongb);
		return status;
	}
	/*
	 * 
	 */
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
