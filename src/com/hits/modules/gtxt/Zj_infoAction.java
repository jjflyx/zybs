package com.hits.modules.gtxt;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.hits.common.util.DateUtil;
import com.hits.modules.com.comUtil;
import com.hits.modules.gtxt.bean.Xyzt_info;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;
import com.hits.util.PageObj;
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
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.StringUtil;
import com.hits.modules.gtxt.bean.Zj_info;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * @author Numbgui
 * @time 2016-03-12 16:18:54
 * 
 */
@IocBean
@At("/private/gtxt/zj")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Zj_infoAction extends BaseAction {
	@Inject
	protected Dao dao;
	public static String url="/private/gtxt/zj/view?id=@id";

	@At("")
	@Ok("->:/private/ratifyFollow/zjList.html")
	public void zj_info(@Param("type") String type,HttpSession session,HttpServletRequest req) {
		Gson gson = new Gson();
		Hashtable<String,String> unitMap = daoCtl.getHTable(dao, Sqls.create("select id,name from XYZT_INFO where xylx = '00050001' and type = '1'"));
		req.setAttribute("type",type);
		req.setAttribute("unitMap",gson.toJson(unitMap));
		req.setAttribute("ryztmap",gson.toJson(comUtil.ryztMap));
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		req.setAttribute("loginname", user.getLoginname());
	}
	
	@At
	@Ok("->:/private/ratifyFollow/zjAdd.html")
	public void toadd(@Param("type")String type,HttpServletRequest req) {
		req.setAttribute("type",type);
		
	}
	
	@At
	@Ok("raw")
	public boolean add(@Param("..") final Zj_info info,HttpSession session) {
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		info.setAdd_time(DateUtil.getCurDateTime());
		info.setActor(user.getLoginname());
		final Xyzt_info xyzt=daoCtl.detailBySql(dao, Xyzt_info.class, Sqls.create("select * from xyzt_info where code='"+info.getCode()+"'"));
		try {
			re.set(false);
			Trans.exec(new Atom() {
				public void run() {
					if(EmptyUtils.isEmpty(xyzt)){
						Xyzt_info xyzt=new Xyzt_info();
						xyzt.setType("0");
						xyzt.setName(info.getName());
						xyzt.setCode(info.getCode());
						xyzt.setTel(info.getTel());
						xyzt.setPhone(info.getPhone());
						xyzt.setSex(info.getSex());
						dao.insert(xyzt);
						info.setXyzt_id(xyzt.getId());
					}else{
						info.setXyzt_id(xyzt.getId());
					}
					dao.insert(info);
					String name="";
					if(info.getType()==1){
						name="估价师";
					}else if(info.getType()==2){
						name="土地代理登记人";
					}else{
						name="专业技术人员信息";
					}
					SysLogUtil.addLog(dao, info.getActor(), SysLogUtil.gtyx_log_type, 
							true, 1,"新增"+name+"【" + info.getName() + "】", name + "管理"  ,
							"", "", "",url.replace("@id", info.getId()+""));
					re.set(true);
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			re.set(false);
		}
		return re.get();
	}

	
	@At
	@Ok("->:/private/ratifyFollow/zjDetail.html")
	public Zj_info view(@Param("id") String id,HttpServletRequest req) {
		Zj_info info =  daoCtl.detailByName(dao,Zj_info.class, id);
		info.setUnit(daoCtl.getStrRowValue(dao,Sqls.create(" select name from xyzt_info where type = '1' and id = '"+info.getUnit()+"' ")));
		req.setAttribute("ryzt", comUtil.ryztMap);
		return info;
	}

	@At
	@Ok("->:/private/ratifyFollow/gjjgDetail.html")
	public void gjjgView(@Param("id") int id,HttpServletRequest req) {
		Xyzt_info info =  daoCtl.detailById(dao,Xyzt_info.class, id);
		List<Zj_info> zjList = daoCtl.list(dao,Zj_info.class,Cnd.where("unit","=",id).and("state","=","0").and("type","=","1").asc("add_time"));
		if(EmptyUtils.isNotEmpty(zjList) && zjList.size() > 0){
			req.setAttribute("zjList",zjList);
		}
		req.setAttribute("qyzt", comUtil.qyzt);
		req.setAttribute("qytype",comUtil.qytype);
		req.setAttribute("xyzt_info",info);
	}
	
	@At
	@Ok("->:/private/ratifyFollow/zjUpdate.html")
	public void toupdate(@Param("id") String id,HttpServletRequest req) {
		Zj_info zj_info = daoCtl.detailByName(dao, Zj_info.class, id);
		req.setAttribute("obj", zj_info);
		req.setAttribute("unit_name",daoCtl.getStrRowValue(dao,Sqls.create(" select name from xyzt_info where type = '1' and id = '"+zj_info.getUnit()+"' ")));
	}
	
	@At
	public boolean update(@Param("..") final Zj_info info,HttpSession session) {
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		final Sys_user user=(Sys_user) session.getAttribute("userSession");
		final Xyzt_info xyzt=daoCtl.detailBySql(dao, Xyzt_info.class, Sqls.create("select * from xyzt_info where id='"+info.getXyzt_id()+"'"));
		try {
			re.set(false);
			Trans.exec(new Atom() {
				public void run() {
					xyzt.setName(info.getName());
					xyzt.setTel(info.getTel());
					xyzt.setPhone(info.getPhone());
					xyzt.setSex(info.getSex());
					dao.update(xyzt);
					//info.setXyzt_id(xyzt.getId());
					dao.update(info);
					String name = info.getType()==1 ? "估价师" : "土地代理登记人";
					SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, true,
							2,"修改"+name+"【" + info.getName() + "】", name + "管理"  , "",
							"", "",url.replace("@id", info.getId()+""));
					re.set(true);
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			re.set(false);
		}
		return re.get();
	}
	
	@At
	@Ok("raw")
	public boolean del(@Param("id") String id,HttpSession session) {
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		boolean flag  = dao.update(Zj_info.class,Chain.make("state",-1),Cnd.where("id","=",id)) > 0;
		Zj_info zjInfo = daoCtl.detailByCnd(dao,Zj_info.class,Cnd.where("id","=",id));
		String name = zjInfo.getType()==1 ? "估价师" : "土地代理登记人";
		SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag,
				3,"删除"+name+"【" + zjInfo.getName() + "】", name + "管理"  , "",
				"", "",url.replace("@id", zjInfo.getId()+""));
		return flag;
	}
	
	@At
	public boolean deleteIds(@Param("ids") String ids) {
		String[] id = StringUtil.null2String(ids).split(",");
		return daoCtl.deleteByNames(dao, Zj_info.class,  id);
	}
	
	@At
	@Ok("raw")
	public String list(@Param("type")Integer type,@Param("cx_name")String cx_name,@Param("cx_code")String cx_code,@Param("page") int curPage, @Param("rows") int pageSize){
		Criteria cri = Cnd.cri();
		cri.where().and("type","=",type);
		if(EmptyUtils.isNotEmpty(cx_name)){
			cri.where().andLike("name",cx_name);
		}
		if(EmptyUtils.isNotEmpty(cx_code)){
			cri.where().andLike("code",cx_code);
		}
		cri.getOrderBy().desc("add_time");
		Sql sql=Sqls.create("select * from zj_info"+cri.toString());
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();
		for(int i=0;i<list.size();i++){
			String unit = StringUtil.null2String(list.get(i).get("unit"));
			list.get(i).put("unit", comUtil.xyztMap.get(unit).split("☆")[0]);
		}
		return PageObj.getPageJSON(qr);
	}

	@At
	@Ok("raw")
	public boolean checkCode(@Param("unit") String unit,@Param("code")String code,@Param("type")Integer type){
		Zj_info zjInfo = null;
		/*if(EmptyUtils.isEmpty(unit)){
			zjInfo=daoCtl.detailByCnd(dao, Zj_info.class, Cnd.where("code", "=", code).and("type","=",type).and("state","=","0"));
		}else{
			zjInfo=daoCtl.detailByCnd(dao, Zj_info.class, Cnd.where("code", "=", code).and("type","=",type).and("unit","=",unit).and("state","=","0"));
		}*/
		zjInfo=daoCtl.detailByCnd(dao, Zj_info.class, Cnd.where("code", "=", code).and("type","=",type).and("state","=","0"));
		System.out.println("========1====>"+zjInfo);
		System.out.println("===========>"+zjInfo==null);
		if(zjInfo==null){
			return true;
		}else{
			return false;
		}
	}

	@At
	public JSONObject selectList(@Param("page") int curPage, @Param("rows") int pageSize,HttpSession session){
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		Criteria cri = Cnd.cri();
		cri.where().and("xylx","=","00050001");
		cri.getOrderBy().desc("id");
		return daoCtl.listPageJson(dao, Xyzt_info.class, curPage, pageSize, cri);
	}

	@At
	@Ok("->:/private/ratifyFollow/gjjgList.html")
	public void gjjgPage(@Param("type")int type,HttpServletRequest req){
		Map ztMap = daoCtl.getHTable(dao,Sqls.create(" select value,name from CS_VALUE where typeid = '00020001' "));
		req.setAttribute("ztMap",JSONObject.fromObject(ztMap));
		if(type==1){
			req.setAttribute("singleSelect", true);
		}else{
			req.setAttribute("singleSelect", false);
		}
	}
}