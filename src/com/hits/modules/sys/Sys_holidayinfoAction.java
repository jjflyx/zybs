package com.hits.modules.sys;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.sql.Criteria;
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
import com.hits.modules.com.comUtil;
import com.hits.modules.sys.bean.Sys_holidayinfo;
import com.hits.util.DateUtil;

/**
 * @author wanfly
 * @time 2014-02-27 10:24:15
 * 
 */
@IocBean
@At("/private/sys/holidayinfo")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Sys_holidayinfoAction extends BaseAction {
	@Inject
	protected Dao dao;

	
	@At("")
	@Ok("->:/private/sys/holidayCalendar.html")
	public void index(HttpSession session,HttpServletRequest req) {
	
	}
	
	@At
	@Ok("->:/private/sys/holidayinfoAdd.html")
	public Sys_holidayinfo toadd(@Param("thisdate") String thisdate) {
		Sys_holidayinfo result=new Sys_holidayinfo();
		if(thisdate!=null && thisdate.length()>0){
			result.setHoliday(thisdate);
			if(DateUtil.isWeekend(thisdate)){
				result.setIsweekend(1);
				result.setIswork(1);
			}else{
				result.setIsweekend(0);
				result.setIswork(0);
			}
		}
		return result;
	}
	
	@At
	@Ok("raw")
	public boolean add(@Param("..") Sys_holidayinfo sys_holidayinfo) {
		boolean bol=false;
		try {
			bol=daoCtl.add(dao,sys_holidayinfo);
			if(bol){
				comUtil.initHoliday(dao, daoCtl);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bol;
	}
	
	//@At
	//@Ok("raw")
	//public int add(@Param("..") Sys_holidayinfo sys_holidayinfo) {
	//	return daoCtl.addT(dao,sys_holidayinfo).getId();
	//}
	
	@At
	@Ok("json")
	public Sys_holidayinfo view(@Param("id") int id) {
		return daoCtl.detailById(dao,Sys_holidayinfo.class, id);
	}
	
	@At
	@Ok("->:/private/sys/holidayinfoUpdate.html")
	public Sys_holidayinfo toupdate(@Param("id") int id, HttpServletRequest req) {
		return daoCtl.detailById(dao, Sys_holidayinfo.class, id);//html:obj
	}
	
	@At
	public boolean update(@Param("..") Sys_holidayinfo sys_holidayinfo) {
		boolean bol=false;
		try {
			bol=daoCtl.update(dao, sys_holidayinfo);
			if(bol){
				comUtil.initHoliday(dao, daoCtl);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bol;
	}
	
	@At
	public boolean delete(@Param("id") int id) {
		boolean bol=false;
		try {
			bol=daoCtl.deleteById(dao, Sys_holidayinfo.class, id);
			if(bol){
				comUtil.initHoliday(dao, daoCtl);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bol;
	}
	
	@At
	public boolean deleteIds(@Param("ids") String ids) {
		boolean bol=false;
		try {
			String[] id = StringUtil.null2String(ids).split(",");
			bol=daoCtl.deleteByIds(dao, Sys_holidayinfo.class, id);
			if(bol){
				comUtil.initHoliday(dao, daoCtl);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return bol;
	}
	
	@At
	public String getHolidayRecords(@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("bgColor") String bgColor, @Param("nowdatebgColor") String nowdatebgColor, @Param("workBgcolor") String workBgcolor){
		Criteria cri = Cnd.cri();
		cri.where().and("HOLIDAY",">=",beginDate).and("HOLIDAY","<=",endDate);
		cri.getOrderBy().desc("id");
		List<Sys_holidayinfo> alllist=daoCtl.list(dao, Sys_holidayinfo.class, cri);
		Map<Integer,String> iswork=new HashMap<Integer, String>();
		iswork.put(1, "工作");
		iswork.put(0, "休假");
		StringBuffer sb=new StringBuffer();
		String nowndate=DateUtil.date2str(new Date());
		System.out.println(beginDate+endDate+"bgColor="+bgColor+",nowdatebgColor="+nowdatebgColor+",workBgcolor="+workBgcolor);
        sb.append("[");
		if(alllist.size()==1){
			int colorcheck=0;
			if(alllist.get(0).getIsweekend()==1){//是周末
				if(alllist.get(0).getIswork()==1){//如果工作
					colorcheck=1;
				}
			}else{//非周末
				if(alllist.get(0).getIswork()==0){//如果休息
					colorcheck=2;
				}
			}
			sb.append("{\"id\":"+alllist.get(0).getId()+",\"title\":\""+iswork.get(alllist.get(0).getIswork())+"\\n<"+StringUtil.substr(alllist.get(0).getDes(),15)+
					">\",\"start\":\""+alllist.get(0).getHoliday()+"\",\"className\":\"rcClass\",\"textColor\":\"#000000\"," +
					"\"backgroundColor\":\"#"+(colorcheck!=1 ? (colorcheck==2 ? bgColor:nowdatebgColor):workBgcolor)+"\"}");
		}else if(alllist.size()>0){
			for(int i=0;i<alllist.size();i++){
				Sys_holidayinfo ts=alllist.get(i);
				int colorcheck=0;
				if(ts.getIsweekend()==1){//是周末
					if(ts.getIswork()==1){//如果工作
						colorcheck=1;
					}
				}else{//非周末
					if(ts.getIswork()==0){//如果休息
						colorcheck=2;
					}
				}
				
				if(i!=(alllist.size()-1)){
					if(ts.getHoliday().equals(nowndate)){
						sb.append("{\"id\":"+ts.getId()+",\"title\":\""+iswork.get(ts.getIswork())+"\\n<"+StringUtil.substr(ts.getDes(),15)+
								">\",\"start\":\""+ts.getHoliday()+"\",\"className\":\"rcClass\",\"textColor\":\"#000000\"," +
								"\"backgroundColor\":\"#"+(colorcheck!=1 ? (colorcheck==2 ? bgColor:nowdatebgColor):workBgcolor)+"\"},");
					}else{
						sb.append("{\"id\":"+ts.getId()+",\"title\":\""+iswork.get(ts.getIswork())+"\\n<"+StringUtil.substr(ts.getDes(),15)+
								">\",\"start\":\""+ts.getHoliday()+"\",\"className\":\"rcClass\",\"textColor\":\"#000000\"," +
								"\"backgroundColor\":\"#"+(colorcheck!=1 ? (colorcheck==2 ? bgColor:nowdatebgColor):workBgcolor)+"\"},");
					}
				}else{
					if(ts.getHoliday().equals(nowndate)){
						sb.append("{\"id\":"+ts.getId()+",\"title\":\""+iswork.get(ts.getIswork())+"\\n<"+StringUtil.substr(ts.getDes(),15)+
								">\",\"start\":\""+ts.getHoliday()+"\",\"className\":\"rcClass\",\"textColor\":\"#000000\"," +
								"\"backgroundColor\":\"#"+(colorcheck!=1 ? (colorcheck==2 ? bgColor:nowdatebgColor):workBgcolor)+"\"}");
					}else{
						sb.append("{\"id\":"+ts.getId()+",\"title\":\""+iswork.get(ts.getIswork())+"\\n<"+StringUtil.substr(ts.getDes(),15)+
								">\",\"start\":\""+ts.getHoliday()+"\",\"className\":\"rcClass\",\"textColor\":\"#000000\"," +
								"\"backgroundColor\":\"#"+(colorcheck!=1 ? (colorcheck==2 ? bgColor:nowdatebgColor):workBgcolor)+"\"}");
					}
				}
			}
		}
		sb.append("]");
		System.out.println(sb);
		return sb.toString();
	}

}