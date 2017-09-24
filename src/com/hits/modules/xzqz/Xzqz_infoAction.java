package com.hits.modules.xzqz;
import java.math.BigDecimal;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hits.modules.sxcj.bean.Discredit_info;
import org.apache.commons.lang.StringUtils;

import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
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
import com.hits.modules.com.comUtil;
import com.hits.modules.gtxt.bean.Xyzt_info;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.modules.xzcf.bean.Xzcf_info;
import com.hits.modules.xzqz.bean.Xzqz_info;
import com.hits.util.Decode64Util;
import com.hits.util.EmptyUtils;
import com.hits.util.PageObj;
import com.hits.util.StringUtil;
import com.hits.util.SysLogUtil;

import net.sf.json.JSONObject;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

/**
 * @author Wizzer
 * @time 2016-03-18 16:19:18
 *
 */
@IocBean
@At("/private/xzqz")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Xzqz_infoAction extends BaseAction {
	@Inject
	protected Dao dao;
	public static String url="/private/xzqz/detail?xzqzid=@id";

	//行政强制信息列表
	@At("/tolist")
	@Ok("->:/private/xzqz/xzqzlist.html")
	public void user(HttpServletRequest req,HttpSession session) {
		//惩戒状态
		Sql sql1 = Sqls.create("select value,name from cs_value where typeid = 00010007");
		List<Map> cjtype = daoCtl.list(dao, sql1);
		Hashtable<String, String> cjTable = daoCtl.getHTable(dao, sql1);
		req.setAttribute("cjTable", JSONObject.fromObject(cjTable));
		req.setAttribute("xyztMap", comUtil.xyztlxMap);
		req.setAttribute("ztTable", JSONObject.fromObject(comUtil.xyztlx));
	}

	//行政强制列表查询方法
	@At()
	@Ok("raw")
	public String xzqzlist(@Param("xyzt_type") String xyzt_type,@Param("cfsx_date") String cfsx_date,@Param("xyzt_name") String xyzt_name,
						   @Param("page") int curPage, @Param("rows") int pageSize,HttpServletRequest req,HttpSession session){
		Sys_user user= (Sys_user)session.getAttribute("userSession");
		Sql sql = null;
		String sqlstr = "select a.id as xzqzid,a.aj_name,a.reason,a.create_date,a.xzcf_zt,a.issue,b.name,b.type as xyzt_type"+
				" from xzqz_info a,xyzt_info b where a.xyzt_id = b.id and a.actor='"+user.getLoginname()+"'";
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr += " and b.type = '"+xyzt_type+"' ";
		}
		if(EmptyUtils.isNotEmpty(cfsx_date)){
			sqlstr += " and a.start_date like '"+cfsx_date+"%' ";
		}
		if(EmptyUtils.isNotEmpty(xyzt_name)){
			sqlstr += " and b.name like '%"+xyzt_name+"%'";
		}
		sqlstr += " order by a.id desc";
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();
		for(int i=0; i < list.size(); i++){
			list.get(i).put("xyzt_type", list.get(i).get("xyzt_type").toString().trim());
		}
		return PageObj.getPageJSON(qr);
	}

	//行政强制信息添加
	@At
	@Ok("->:/private/xzqz/xzqzAdd.html")
	public void toadd(@Param("id")String id,HttpServletRequest req){
		System.out.println("id : "+id);
		Xzcf_info xzcf = daoCtl.detailByCnd(dao,Xzcf_info.class,Cnd.where("id","=",id));
		Xyzt_info info = daoCtl.detailByCnd(dao,Xyzt_info.class,Cnd.where("id","=",xzcf.getXyzt_id()));
		String type = "";
		switch (Integer.parseInt(info.getType())){
			case  0:
				type = "自然人";
				break;
			case 1:
				type = "法人";
				break;
			case 2:
				type = "其他组织";
				break;
			default:
				type = "法人";
				break;
		}
		req.setAttribute("info",info);
		req.setAttribute("type",type);
		req.setAttribute("xzcf",xzcf);
		//信用主体类型
		req.setAttribute("xyztMap", JSONObject.fromObject(comUtil.xyztlx));
	}

	@At
	@Ok("raw")
	public boolean addSave(@Param("..") final Xzqz_info xzqz_info,HttpSession session) {
		boolean flag = false;
		final Sys_user user = (Sys_user) session.getAttribute("userSession");
		xzqz_info.setActor(user.getLoginname());
		xzqz_info.setUnitid(user.getUnitid());
		xzqz_info.setCreate_date(DateUtil.getCurDateTime());
		//判断是否在惩戒中
		xzqz_info.setXzcf_zt("0");  // 0 表示惩戒中
		xzqz_info.setSjc(DateUtil.str2sjc(DateUtil.getCurDateTime()));  // 时间戳 
		final Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzqz_info.getXyzt_id());
		try {
			Trans.exec(new Atom() {
				public void run() {
					boolean flag = daoCtl.add(dao, xzqz_info);
					dao.update("XZCF_INFO", Chain.make("is_qz", "1"), Cnd.where("id", "=", xzqz_info.getXzcf_id()));
					/*Xzcf_info xzcf=daoCtl.detailBySql(dao, Xzcf_info.class, Sqls.create("select * from xzcf_info where id='"+xzqz_info.getXzcf_id()+"'"));
					if(xzqz_info.getQzzxqk().equals("2")){//执行完毕
						xzcf.setSjlxqk("01");
					}else if(xzqz_info.getQzzxqk().equals("3")){//依法不再执行
						xzcf.setSjlxqk("04");
					}else{//正在执行
						xzcf.setSjlxqk("02");
					}
					daoCtl.update(dao, xzcf);*/
					String lognote=SysLogUtil.getLogNote(xzqz_info, "xzcf");
					SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag, 1,
							"添加信用主体【" + xyzt.getName() + "】的行政强制信息", "行政强制", lognote, "", "", url.replace("@id", xzqz_info.getId() + ""));
					/**
					 * 增加失信惩戒信息  2016-03-31
					 */
					Discredit_info disinfo=new Discredit_info();

					disinfo.setXyzt_id(xzqz_info.getXyzt_id()+"");
					disinfo.setActor(user.getLoginname());
					disinfo.setCreate_date(DateUtil.getCurDateTime());
					disinfo.setUnitid(user.getUnitid());
					disinfo.setXzqh(daoCtl.getStrRowValue(dao,Sqls.create(" select xzqh from sys_unit where id = '"+disinfo.getUnitid()+"' ")));
					disinfo.setSrc(2);
					disinfo.setSrc_type(2); //2 表示行政强制

					disinfo.setUnit(comUtil.yhjgHt.get(user.getUnitid()));
					//disinfo.setStart_date(xzqz_info.getStart_date());
					//disinfo.setEnd_date(xzqz_info.getEnd_date());
					disinfo.setStart_date(DateUtil.getToday());
					disinfo.setCreate_date(xzqz_info.getCreate_date());
					disinfo.setDiscipline_date(DateUtil.getToday());
					disinfo.setCjcs_id(disinfo.getSxxw_id());
					disinfo.setSxqx_id(disinfo.getSxxw_id());
					disinfo.setXzcf_id(xzqz_info.getXzcf_id());

					disinfo.setSxxw_id("0045"); // 0045表示被行政强制
					disinfo.setSxqx_id("0045"); 
					disinfo.setCjcs_id("0045");
					disinfo.setXy_type("0008");
					disinfo.setTablekey(comUtil.htTableMap.get(disinfo.getXy_type()));
					disinfo.setType("0");  // 0 表示惩戒中
					dao.insert(disinfo);
				}
			});
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	//修改行政强制信息
	@At
	@Ok("->:/private/xzqz/xzqzUpdate.html")
	public void toupdate(@Param("xzqzid") String xzqzid, HttpServletRequest req) {
		int id = StringUtil.StringToInt(xzqzid);
		//行政强制信息
		Xzqz_info xzqz = daoCtl.detailById(dao, Xzqz_info.class, id);
		//信用主体信息
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzqz.getXyzt_id());
		xyzt.setType(xyzt.getType().trim());
		req.setAttribute("xzqz", xzqz);
		req.setAttribute("xyzt", xyzt);
		//信用主体类型
		req.setAttribute("xyztMap", JSONObject.fromObject(comUtil.xyztlx));
		//强制机关
	}

	@At
	public boolean updateSave(@Param("..") Xzqz_info xzqz_info, HttpSession session, @Param("note") String note,
							  @Param("u_reason") String reason, @Param("basis") String basis) {
		boolean flag = false;
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		if(EmptyUtils.isNotEmpty(xzqz_info.getFile_html())){
			xzqz_info.setFile_html(Decode64Util.Decrypt(xzqz_info.getFile_html()));
		}
		String xzcfid=xzqz_info.getXzcf_id();
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzqz_info.getXyzt_id());
		//判断是否在惩戒中
		xzqz_info.setXzcf_zt("0");  // 0 表示惩戒中
		xzqz_info.setSjc(DateUtil.str2sjc(DateUtil.getCurDateTime()));  // 时间戳 
		Xzqz_info oldObj=daoCtl.detailById(dao, Xzqz_info.class, xzqz_info.getId());
		flag = daoCtl.update(dao, xzqz_info);
		if(flag){
			String zxqk=xzqz_info.getQzzxqk();
			String sql="select * from Discredit_info where xzcf_id='"+xzcfid+"' and Sxxw_id=0045";
			Discredit_info disinfo=daoCtl.detailBySql(dao, Discredit_info.class, Sqls.create(sql));
			if(EmptyUtils.isNotEmpty(disinfo)){
				if(zxqk.equals("2")){//当执行情况为全部履行的时候，惩戒结束
					disinfo.setType("1");//惩戒结束
				}else if(zxqk.equals("3")){//当执行情况为依法不再履行，撤销惩戒
					disinfo.setType("2");//已撤销
				}
				daoCtl.update(dao, disinfo);
			}
		}
		/*if(flag){
			Xzcf_info xzcf=daoCtl.detailBySql(dao, Xzcf_info.class, Sqls.create("select * from xzcf_info where id='"+xzqz_info.getXzcf_id()+"'"));
			if(xzqz_info.getQzzxqk().equals("2")){//执行完毕
				xzcf.setSjlxqk("01");
			}else if(xzqz_info.getQzzxqk().equals("3")){//依法不再执行
				xzcf.setSjlxqk("04");
			}else{//正在执行
				xzcf.setSjlxqk("02");
			}
			daoCtl.update(dao, xzcf);
		}*/
		String lognote=SysLogUtil.getLogNote(oldObj,xzqz_info, "xzcf");
		SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag, 2,
				"修改信用主体【" + xyzt.getName() + "】的行政强制记录", "行政强制", lognote, reason,
				basis, url.replace("@id", xzqz_info.getId() + ""));
		return flag;
	}

	//撤销 revoke撤销  release 发布
	@At
	@Ok("->:/private/xzqz/xzqzCancel.html")
	public void toCancel(@Param("xzqzid") String xzqzid, HttpServletRequest req) {
		int id = StringUtil.StringToInt(xzqzid);
		//行政强制信息
		Xzqz_info xzqz = daoCtl.detailById(dao, Xzqz_info.class, id);
		//信用主体信息
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzqz.getXyzt_id());
		xyzt.setType(xyzt.getType().trim());
		req.setAttribute("xzqz", xzqz);
		req.setAttribute("xyzt", xyzt);
		//信用主体类型
		req.setAttribute("xyztMap", JSONObject.fromObject(comUtil.xyztlx));
		//强制机关
	}

	@At
	@Ok("raw")
	public boolean revoke(HttpSession session, @Param("xyzt_id") String xyzt_id, @Param("id") String xzqzid, @Param("note") String note,
						  @Param("u_reason") String reason, @Param("basis") String basis) {
		boolean flag = false;
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, StringUtil.StringToInt(xyzt_id));
		int id = StringUtil.StringToInt(xzqzid);
		flag = daoCtl.exeUpdateBySql(dao, Sqls.create("update xzqz_info set xzcf_zt=2,issue=0 where id = '" + id + "'"));
		SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag, 4,
				"撤销信用主体【" + xyzt.getName() + "】的行政强制记录",
				"行政强制", "" + note + "", "" + reason + "", "" + basis + "", url.replace("@id", xzqzid));
		return flag;
	}

	//发布 revoke撤销  release 发布
	@At
	@Ok("raw")
	public boolean toIssue(@Param("xzqzid") String xzqzid, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		boolean flag = true;
		Xzqz_info xzqz_info = daoCtl.detailById(dao, Xzqz_info.class, StringUtil.StringToInt(xzqzid));
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzqz_info.getXyzt_id());
		flag = daoCtl.exeUpdateBySql(dao, Sqls.create("update xzqz_info set issue='1'  where id='" + xzqzid + "'"));
		SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag, 5,
				"发布信用主体【" + xyzt.getName() + "】的行政强制记录", "行政强制", "", "", "", url.replace("@id", xzqzid));
		return flag;
	}

	//浏览
	@At
	@Ok("->:/private/xzqz/xzqzDetail.html")
	public void detail(@Param("xzqzid") String xzqzid, HttpServletRequest req) {
		int id = StringUtil.StringToInt(xzqzid);
		//行政强制信息
		Xzqz_info xzqz = daoCtl.detailById(dao, Xzqz_info.class, id);
		//信用主体信息
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzqz.getXyzt_id());
		xyzt.setType(xyzt.getType().trim());
		req.setAttribute("xzqz", xzqz);
		req.setAttribute("xyzt", xyzt);
		//信用主体类型
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
		//强制机关
	}
}