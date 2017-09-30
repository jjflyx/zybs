package com.hits.modules.xzcf;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.nutz.dao.*;
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
import com.hits.common.util.DateUtil;
import com.hits.modules.bean.Breach_info;
import com.hits.modules.bean.File_info;
import com.hits.modules.com.comUtil;
import com.hits.modules.gtxt.bean.Xyzt_info;
import com.hits.modules.sxcj.bean.Discredit_info;
import com.hits.modules.sys.bean.Sys_log;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.modules.xzcf.bean.Xzcf_info;
import com.hits.util.Decode64Util;
import com.hits.util.EmptyUtils;
import com.hits.util.PageObj;
import com.hits.util.StringUtil;
import com.hits.util.SysLogUtil;

import net.sf.json.JSONObject;

/**
 * @author hzg
 * @time 2016-03-16 10:38:44
 *
 */
@IocBean
@At("/private/xzcf")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Xzcf_infoAction extends BaseAction{
	@Inject
	protected Dao dao;
	public static String url="/private/xzcf/detail?xzcfid=@id";

	//行政处罚信息列表
	@At("/tolist")
	@Ok("->:/private/xzcf/xzcfxxlist.html")
	public void user(HttpServletRequest req,HttpSession session) {
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
		req.setAttribute("statusMap", JSONObject.fromObject(comUtil.statusMap));
	}

	//行政处罚信息列表查询
	@At()
	@Ok("raw")
	public String xzcflist(@Param("xyzt_type") String xyzt_type,@Param("cfsx_date") String cfsx_date,@Param("xyzt_name") String xyzt_name,
						   @Param("xzcf_zt")String xzcf_zt,@Param("page") int curPage, @Param("rows") int pageSize,HttpServletRequest req,HttpSession session){
		Sys_user user= (Sys_user)session.getAttribute("userSession");
		Sql sql = null;
		String sqlstr = "select a.id as xzcfid,a.sjlxqk,a.contract_id,a.xzcf_type,a.au,a.xzcf_code,a.cfsx_date,a.xzcf_zt,a.issue,a.start_date,a.status,a.end_date,b.id as xyztid,b.name,b.type as xyzt_type,a.is_qz,a.cflx_date,a.biaozhu from xzcf_info a,xyzt_info b"+
				" where b.id = a.xyzt_id and a.actor='"+user.getLoginname()+"'";
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr += " and b.type = '"+xyzt_type+"' ";
		}
		if(EmptyUtils.isNotEmpty(cfsx_date)){
			sqlstr += " and a.start_date like '"+cfsx_date+"%' ";
		}
		if(EmptyUtils.isNotEmpty(xyzt_name)){
			sqlstr += " and b.name like '%"+xyzt_name+"%'";
		}
		if(EmptyUtils.isNotEmpty(xzcf_zt)){
			sqlstr += " and a.xzcf_zt = '"+xzcf_zt+"'";
		}
		sqlstr += " order by a.id desc";
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();

		for(int i=0;i<list.size();i++){
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

	//行政处罚信息添加
	@At
	@Ok("->:/private/xzcf/xzcfxxAdd.html")
	public void toadd(HttpServletRequest req,HttpSession session){
//		Sql sql = Sqls.create("select distinct a.xylx,b.name from breach_info a,cs_value b where b.typeid='00010005' "
//	            +"and a.xylx = b.code and is_xzcf = 1 order by a.xylx desc");
//		List<Map> xzcf_type = daoCtl.list(dao, sql);
		//行政处罚信用类型
		//信用主体类型
		req.setAttribute("xyztMap", comUtil.xyztlxMap);
		req.setAttribute("ztHt", JSONObject.fromObject(comUtil.xyztlx));
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		String unitid = user.getUnitid();
		Sys_unit unit = daoCtl.detailByName(dao, Sys_unit.class, unitid);
		if(!"88".equals(unit.getUnittype())&&!"90".equals(unit.getUnittype())){
			unitid=unitid.length()>4?unitid.substring(0,unitid.length()-4):unitid;
		}
		req.setAttribute("unit", unitid);
		req.setAttribute("today", DateUtil.getToday());
	}

	//行政处罚添加失信行为
	@At
	@Ok("raw")
	public String sxxwAjax(@Param("xzcf_type") String xzcf_type){
		Sql sql = Sqls.create("select id,sxxw from breach_info where is_xzcf =1 and xzcf_type = '"+xzcf_type+"' and length(id)=4"); //xylx like '"+xy_type+"%' and
		List<Map> sxxwlist = daoCtl.list(dao, sql);
		return sxxwlist+"";
	}

	//添加失信惩戒中的信用主体
	@At
	@Ok("->:/private/xzcf/xzcfAddfr.html")
	public void toAddfr(HttpServletRequest req,@Param("xy_type") String xy_type){
		//产生方式
		Sql sql = Sqls.create("select value,name from cs_value where typeid = '00010008'");
		List<Map> cstype = daoCtl.list(dao, sql);

		Hashtable<String, String> csTable = daoCtl.getHTable(dao, sql);
		//惩戒状态
		Sql sql1 = Sqls.create("select value,name from cs_value where typeid = '00010007'");
		List<Map> cjtype = daoCtl.list(dao, sql1);
		Hashtable<String, String> cjTable = daoCtl.getHTable(dao, sql1);

		req.setAttribute("csTable", JSONObject.fromObject(csTable));
		System.out.println("csTable:"+JSONObject.fromObject(csTable));
		req.setAttribute("cjTable", JSONObject.fromObject(cjTable));

		req.setAttribute("ztTable", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("csMap", cstype);
		req.setAttribute("cjMap", cjtype);
		req.setAttribute("ztlxlist", comUtil.xyztlxMap);
		req.setAttribute("xy_type", xy_type);
	}

	//失信惩戒中信用主体列表查询
	@At
	@Ok("raw")
	public String sxcjList(HttpServletRequest req,HttpSession session,@Param("xyzt_type") String xyzt_type,
						   @Param("src") String src,@Param("type") String type,@Param("xyzt_name") String xyzt_name,@Param("xy_type") String xy_type,
						   @Param("page") int curPage, @Param("rows") int pageSize) {
		System.out.println("xy_type:"+xy_type);
		Sys_user user= (Sys_user)session.getAttribute("userSession");
		Sql sql = null;

		String sqlstr = "select a.id as sxcjid,a.xyzt_id,a.start_date,a.end_date,a.type,a.issue,a.src,a.contract_id,b.name,b.type as xyzt_type,"+
				"c.sxxw from discredit_info a,xyzt_info b,breach_info c where a.xyzt_id = b.id and a.sxxw_id = c.id "+
				"and a.xy_type = '"+xy_type+"'";
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
		sqlstr += " order by a.id desc";
		System.out.println("sqlstr:"+ sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();
		for(int i=0;i<list.size();i++){
			String start_date = list.get(i).get("start_date").toString();
			String end_date = list.get(i).get("end_date").toString();
			list.get(i).put("startDend", start_date+" 至 "+(null == end_date?"":end_date));
//			//判断是否为已撤销    2表示已撤销
//			if(!"2".equals(list.get(i).get("type"))){
//				if(DateUtil.getDifferDays(DateUtil.getToday(),start_date) > 0 &&
//						DateUtil.getDifferDays(DateUtil.getToday(),end_date) > 0){
//					list.get(i).put("type", " ");// ' '表示惩戒尚未开始
//					int sxcjid = ((BigDecimal)list.get(i).get("sxcjid")).intValue();
//					daoCtl.exeUpdateBySql(dao, Sqls.create("update discredit_info set type = ' ' where id = '"+sxcjid+"'"));
//				}
//				//跟现在时间对比是否在惩戒中((BigDecimal)list.get(i).get("nbid")).intValue()
//				if(DateUtil.getDifferDays(start_date,DateUtil.getToday()) > 0 &&
//						DateUtil.getDifferDays(DateUtil.getToday(),end_date) > 0){
//					list.get(i).put("type", "0");// 0 表示惩戒中
//					int sxcjid = ((BigDecimal)list.get(i).get("sxcjid")).intValue();
//					daoCtl.exeUpdateBySql(dao, Sqls.create("update discredit_info set type = 0 where id = '"+sxcjid+"'"));
//				}
//				//跟现在时间对比是否惩戒结束
//				if(DateUtil.getDifferDays(start_date,DateUtil.getToday()) > 0 &&
//						DateUtil.getDifferDays(end_date,DateUtil.getToday()) > 0){
//					list.get(i).put("type", "1");// 1表示惩戒结束
//					int sxcjid = ((BigDecimal)list.get(i).get("sxcjid")).intValue();
//					daoCtl.exeUpdateBySql(dao, Sqls.create("update discredit_info set type = 1 where id = '"+sxcjid+"'"));
//				}
//			}
			list.get(i).put("xyzt_type", list.get(i).get("xyzt_type").toString().trim());
		}
		return PageObj.getPageJSON(qr);
	}

	//查询合同
	@At
	@Ok("raw")
	public String tofindHtmc(@Param("htid") String htid,@Param("xy_type") String xy_type){
		//合同信息
		String htmc = "";
		String htbm = comUtil.htTableMap.get(xy_type);
		if(EmptyUtils.isNotEmpty(htbm)){
			Sql sqlht = Sqls.create("select htmc from "+htbm+" where htid = '"+htid+"' ");
			List<Map> htlist = daoCtl.list(dao, sqlht);
			htmc = htlist.get(0).get("htmc").toString();
		}
		System.out.println("htmc:"+htmc);
		return htmc;
	}

	//保存操作
	@At
	@Ok("raw")
	public boolean addSave(HttpSession session,@Param("..") final Xzcf_info xzcfinfo,@Param("cj_start_date") final String cj_start_date,
			@Param("cj_end_date") final String cj_end_date,@Param("filepath") final String path,
			@Param("filename") final String name,@Param("filesize") final String filesize){
		final ThreadLocal<Boolean> result = new ThreadLocal<Boolean>();
		final Sys_user user = (Sys_user) session.getAttribute("userSession");
		if(EmptyUtils.isNotEmpty(xzcfinfo.getFile_html())){
			xzcfinfo.setFile_html(Decode64Util.Decrypt(xzcfinfo.getFile_html()));
		}
		try {
			Trans.exec(new Atom(){
				public void run(){
					xzcfinfo.setSxqx_id(xzcfinfo.getSxxw_id());
					xzcfinfo.setCjcs_id(xzcfinfo.getSxxw_id());
					xzcfinfo.setStart_date(xzcfinfo.getCfsx_date());
					xzcfinfo.setActor(user.getLoginname());
					xzcfinfo.setUnitid(user.getUnitid());
					xzcfinfo.setCreate_date(DateUtil.getCurDateTime());
					xzcfinfo.setXzcf_zt("0");  // 0 表示惩戒中
					xzcfinfo.setSjlxqk("03");  // 03 表示未履行
					xzcfinfo.setSjc(DateUtil.str2sjc(DateUtil.getCurDateTime()));  // 时间戳 
					xzcfinfo.setData_state("1");  // 1：新增数据；2：更新数据；3：删除数据
					xzcfinfo.setXyptbatchno(1);//新增时为1  每次修改+1
					Xzcf_info info = dao.insert(xzcfinfo);
					result.set(true);
					//附件
					String[] paths=path.split(";");
					String[] names=name.split(";");
					String[] sizes=filesize.split(";");
					for(int i=0;i<paths.length;i++){
						File_info att = new File_info();
						if(EmptyUtils.isNotEmpty(paths[i])&&EmptyUtils.isNotEmpty(names[i])){
							att.setFilename(names[i]);
							att.setFilepath(paths[i]);
							att.setTablekey(info.getId()+"");
							att.setTablename("xzcf_info");
							att.setFieldname("fj");
							att.setCreate_time(DateUtil.getCurDateTime());
							att.setFilesize(sizes[i]);
							dao.insert(att);
						}
					}
					/**
					 * 增加失信惩戒信息  2016-03-31
					 */
					if(xzcfinfo.getStatus()==1){
						Discredit_info disinfo=new Discredit_info();
						disinfo.setXzcf_id(String.valueOf(info.getId()));
						disinfo.setXyzt_id(xzcfinfo.getXyzt_id()+"");
						disinfo.setActor(user.getLoginname());
						disinfo.setCreate_date(DateUtil.getCurDateTime());
						disinfo.setUnitid(user.getUnitid());
						disinfo.setXzqh(daoCtl.getStrRowValue(dao,Sqls.create(" select xzqh from sys_unit where id = '"+disinfo.getUnitid()+"' ")));
						disinfo.setSrc(2);
						disinfo.setSrc_type(1); // 1 表示行政处罚

						disinfo.setUnit(xzcfinfo.getCf_unit());
						disinfo.setStart_date(cj_start_date);
						disinfo.setEnd_date(cj_end_date);

						disinfo.setDiscipline_date(DateUtil.getToday());
						if(xzcfinfo.getSxxw_id().startsWith("0043")) {
//						disinfo.setSxxw_id(xzcfinfo.getSxxw_id());
							disinfo.setSxxw_id("0043");
							disinfo.setType("4");
							disinfo.setCjcs_id(disinfo.getSxxw_id());
							disinfo.setSxqx_id(disinfo.getSxxw_id());
						}else{
							disinfo.setXy_type(daoCtl.getStrRowValue(dao,Sqls.create(" select xylx from BREACH_INFO where id = '"+xzcfinfo.getSxxw_id()+"' ")));
							disinfo.setSxxw_id(xzcfinfo.getSxxw_id());
							disinfo.setCjcs_id(xzcfinfo.getCjcs_id());
							disinfo.setSxqx_id(xzcfinfo.getSxqx_id());
							if(EmptyUtils.isEmpty(disinfo.getEnd_date())||(DateUtil.getDifferDays(disinfo.getStart_date(),DateUtil.getToday()) > 0 &&
			    				DateUtil.getDifferDays(DateUtil.getToday(),disinfo.getEnd_date()) > 0)){
								disinfo.setType("0");  // 0 表示惩戒中
							}else{
								disinfo.setType("1");  // 0 表示惩戒结束
							}
						}
						disinfo.setXyzt_id(xzcfinfo.getXyzt_id()+"");

						disinfo.setXy_type("0008");
						/*if(!"0043".equals(disinfo.getSxxw_id())) {
							try {
								BeanUtils beanUtils = new BeanUtils();
								Discredit_info disinfoDest=new Discredit_info();
								beanUtils.copyProperties(disinfoDest, disinfo);
								disinfoDest.setSxxw_id("0043");
								disinfoDest.setType("4");
								dao.insert(disinfoDest);
								disinfo.setSxxw_id(disinfo.getSxxw_id()+",0043");
								//disinfo.setType(disinfo.getType()+",4");
							} catch (Exception e) {
								e.printStackTrace();
							}
						}*/
						dao.insert(disinfo);
						Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzcfinfo.getXyzt_id());
						String lognote=SysLogUtil.getLogNote(disinfo, "xzcf");
						SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, result.get(), 1,
							"添加信用主体【" + xyzt.getName() + "】的行政处罚信息", "行政处罚", lognote, "",
							"",url.replace("@id", xzcfinfo.getId()+""));
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			result.set(false);
		}

		return result.get();
	}

	//行政处罚修改页面
	@At()
	@Ok("->:/private/xzcf/xzcfxxUpdate.html")
	public void toUpdate(@Param("xzcfid") String xzcfid,HttpServletRequest req){
		//行政处罚信息
		int id = StringUtil.StringToInt(xzcfid);
		Xzcf_info xzcf = daoCtl.detailById(dao, Xzcf_info.class, id);
		//信用主体信息
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzcf.getXyzt_id());
		xyzt.setType(xyzt.getType().trim());
		//合同信息
		String htbm = comUtil.htTableMap.get(xzcf.getXzcf_type());
		if(EmptyUtils.isNotEmpty(htbm)){
			Sql sqlht = Sqls.create("select htmc from "+htbm+" where htid = '"+xzcf.getContract_id()+"' ");
			List<Map> htlist = daoCtl.list(dao, sqlht);
			req.setAttribute("htlist", htlist);
		}
		//失信行为
		Sql sqlxw = Sqls.create("select sxxw from breach_info where id = '" + xzcf.getSxxw_id() + "' ");
		if("0043".equals(xzcf.getSxxw_id().substring(0,4)) ||"0044".equals(xzcf.getSxxw_id())||"0045".equals(xzcf.getSxxw_id()) ){
			req.setAttribute("isxz",1);
		}
		String sxxw=daoCtl.getStrRowValue(dao, sqlxw);
		Breach_info xwinfo = daoCtl.detailByName(dao, Breach_info.class, xzcf.getSxqx_id());
		req.setAttribute("sxxw", sxxw);
		req.setAttribute("xwinfo", xwinfo);

		req.setAttribute("xzcf", xzcf);
		req.setAttribute("xyzt", xyzt);
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("today", DateUtil.getToday());
		req.setAttribute("fileList", daoCtl.getMulRowValue(dao, Sqls.create("select filename,filepath||'*'||filesize from file_info where tablekey='"+id+"' and tablename='xzcf_info' order by create_time asc")));
	}

	//修改保存
	@At
	@Ok("raw")
	public boolean updateSave(HttpSession session,@Param("..") final Xzcf_info xzcfinfo,@Param("cj_start_date") final String cj_start_date,@Param("cj_end_date") final String cj_end_date,@Param("note") final String note,
							  @Param("reason") final String reason,@Param("basis") final String basis,@Param("id1") final String id1,
							  @Param("filepath") final String path,@Param("filename") final String name,@Param("filesize") final String size,
								@Param("filepath1") final String filepath1,@Param("filename1") final String filename1,@Param("filesize1") final String filesize1,@Param("oldstatus")final int oldstatus){
		final ThreadLocal<Boolean> result = new ThreadLocal<Boolean>();
		final Sys_user user = (Sys_user) session.getAttribute("userSession");
		final Xzcf_info oldObj=daoCtl.detailById(dao, Xzcf_info.class, xzcfinfo.getId());
		final Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzcfinfo.getXyzt_id());
		if(EmptyUtils.isNotEmpty(xzcfinfo.getFile_html())){
			xzcfinfo.setFile_html(Decode64Util.Decrypt(xzcfinfo.getFile_html()));
		}
		if((oldObj.getStatus()==1)||(oldObj.getStatus()==0&&xzcfinfo.getStatus()==1)){
			int ph=oldObj.getXyptbatchno();
			xzcfinfo.setXyptbatchno(ph+1);//更新批号
			xzcfinfo.setSjc(DateUtil.str2sjc(DateUtil.getCurDateTime()));  // 时间戳 
			xzcfinfo.setData_state("2");//1：新增数据；2：更新数据；3：删除数据
		}
		try {
			Trans.exec(new Atom(){
				public void run(){
					//判断是否在惩戒中
					String today = DateUtil.getToday();
					/*if(DateUtil.getDifferDays(xzcfinfo.getStart_date(),today) > 0 &&
							DateUtil.getDifferDays(DateUtil.getToday(),today) > 0){
						xzcfinfo.setXzcf_zt("0");  // 0 表示惩戒中
					}*/
					dao.update(xzcfinfo);
					daoCtl.update(dao, xzcfinfo);
					/*if(EmptyUtils.isNotEmpty(xzcfinfo.getCflx_date())){//填报实际履行处罚时间
						String sql="update Discredit_info set type=1,perform_date='"+xzcfinfo.getCflx_date()+"' where type=0 and xzcf_id='"+xzcfinfo.getId()+"'";
						daoCtl.exeUpdateBySql(dao, Sqls.create(sql));
					}*/
					//附件
					String sqlfile="delete from file_info where tablekey='"+xzcfinfo.getId()+"' and tableName='xzcf_info'";
					dao.execute(Sqls.create(sqlfile));
					String[] paths=path.split(";");
					String[] names=name.split(";");
					String[] sizes=size.split(";");
					for(int i=0;i<paths.length;i++){
						File_info att = new File_info();
						if(EmptyUtils.isNotEmpty(paths[i])&&EmptyUtils.isNotEmpty(names[i])&&EmptyUtils.isNotEmpty(sizes[i])){
							att.setFilename(names[i]);
							att.setFilepath(paths[i]);
							att.setTablekey(xzcfinfo.getId()+"");
							att.setTablename("xzcf_info");
							att.setFieldname("fj");
							att.setCreate_time(DateUtil.getCurDateTime());
							att.setFilesize(sizes[i]);
							dao.insert(att);	
						}
					}
					
					/**
					 * 增加失信惩戒信息  2016-03-31
					 */
					if(xzcfinfo.getStatus()==1&&oldstatus==0){
					Discredit_info disinfo=new Discredit_info();
					disinfo.setXzcf_id(String.valueOf(xzcfinfo.getId()));
					disinfo.setXyzt_id(xzcfinfo.getXyzt_id()+"");
					disinfo.setActor(user.getLoginname());
					disinfo.setCreate_date(DateUtil.getCurDateTime());
					disinfo.setUnitid(user.getUnitid());
					disinfo.setXzqh(daoCtl.getStrRowValue(dao,Sqls.create(" select xzqh from sys_unit where id = '"+disinfo.getUnitid()+"' ")));
					disinfo.setSrc(2);
					disinfo.setSrc_type(1); // 1 表示行政处罚

					disinfo.setUnit(xzcfinfo.getCf_unit());
					disinfo.setStart_date(cj_start_date);
					disinfo.setEnd_date(cj_end_date);

					disinfo.setDiscipline_date(DateUtil.getToday());
					if(xzcfinfo.getSxxw_id().startsWith("0043")) {
//						disinfo.setSxxw_id(xzcfinfo.getSxxw_id());
						disinfo.setSxxw_id("0043");
						disinfo.setType("4");
						disinfo.setCjcs_id(disinfo.getSxxw_id());
						disinfo.setSxqx_id(disinfo.getSxxw_id());
					}else{
						disinfo.setXy_type(daoCtl.getStrRowValue(dao,Sqls.create(" select xylx from BREACH_INFO where id = '"+xzcfinfo.getSxxw_id()+"' ")));
						disinfo.setSxxw_id(xzcfinfo.getSxxw_id());
						disinfo.setCjcs_id(xzcfinfo.getCjcs_id());
						disinfo.setSxqx_id(xzcfinfo.getSxqx_id());
						if(EmptyUtils.isEmpty(disinfo.getEnd_date())||(DateUtil.getDifferDays(disinfo.getStart_date(),DateUtil.getToday()) > 0 &&
			    				DateUtil.getDifferDays(DateUtil.getToday(),disinfo.getEnd_date()) > 0)){
			    			disinfo.setType("0");  // 0 表示惩戒中
			    		}else{
			    			disinfo.setType("1");  // 0 表示惩戒结束
			    		}
						
					}
					disinfo.setXyzt_id(xzcfinfo.getXyzt_id()+"");

					disinfo.setXy_type("0008");
					if(!"0043".equals(disinfo.getSxxw_id())) {
						try {
							disinfo.setSxxw_id(disinfo.getSxxw_id()+",0043");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					dao.insert(disinfo);
					Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzcfinfo.getXyzt_id());

					result.set(true);
					String lognote=SysLogUtil.getLogNote(disinfo, "xzcf");
				
					SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, result.get(), 1,
							"添加信用主体【" + xyzt.getName() + "】的行政处罚信息", "行政处罚", lognote, "",
							"",url.replace("@id", xzcfinfo.getId()+""));
					}
					
					String lognote=SysLogUtil.getLogNote(oldObj, xzcfinfo, "xzcf");
					String fileid="";
					if (EmptyUtils.isEmpty(id1) &&EmptyUtils.isNotEmpty(filename1) && EmptyUtils.isNotEmpty(filepath1)) {
						File_info file=new File_info();
						file.setId(id1);
						file.setFilename(filename1);
						file.setFilesize(filesize1);
						file.setFilepath(filepath1);
						dao.insert(file);
						fileid=file.getId();
					}
					SysLogUtil.addLogxg(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, true,2,
							"修改信用主体【"+xyzt.getName()+"】的行政处罚记录", "行政处罚", lognote,
							reason, basis,url.replace("@id", xzcfinfo.getId()+""),fileid);
					result.set(true);
				}
			});
		}catch (Exception e) {
			e.printStackTrace();
			result.set(false);
		}
		return result.get();
	}

	//浏览
	@At
	@Ok("->:/private/xzcf/xzcfxxDetail.html")
	public void detail(@Param("xzcfid") String xzcfid,HttpServletRequest req){
		//行政处罚信息
		int id = StringUtil.StringToInt(xzcfid);
		Xzcf_info xzcf = daoCtl.detailById(dao, Xzcf_info.class, id);
		//信用主体信息
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzcf.getXyzt_id());
		xyzt.setType(xyzt.getType().trim());
		//合同信息
		String htbm = comUtil.htTableMap.get(xzcf.getXzcf_type());
		if(EmptyUtils.isNotEmpty(htbm)){
			Sql sqlht = Sqls.create("select htmc from "+htbm+" where htid = '"+xzcf.getContract_id()+"' ");
			List<Map> htlist = daoCtl.list(dao, sqlht);
			req.setAttribute("htlist", htlist);
		}
		//失信行为
		Sql sqlxw = Sqls.create("select sxxw from breach_info where id = '" + xzcf.getSxxw_id() + "'and is_xzcf =1");
		if("0043".equals(xzcf.getSxxw_id().substring(0,4)) ||"0044".equals(xzcf.getSxxw_id())||"0045".equals(xzcf.getSxxw_id()) ){
			sqlxw = Sqls.create("select sxxw from breach_info where id = '" + xzcf.getSxxw_id() + "' ");
			req.setAttribute("isxz",1);
		}
		List<Map> xwlist = daoCtl.list(dao, sqlxw);
		//失信情形
		List<String> qxlist = daoCtl.getStrRowValues(dao, Sqls.create("select sxqx from breach_info where id in ('" + xzcf.getSxqx_id() + "') and is_xzcf =1"));
		//惩戒措施
		List<String> cslist = daoCtl.getStrRowValues(dao, Sqls.create("select cjcs from breach_info where id in ('" + xzcf.getCjcs_id() + "') and is_xzcf =1"));
		//惩戒依据
		List<String> yjlist = daoCtl.getStrRowValues(dao, Sqls.create("select distinct yj from breach_info where id in ('" + xzcf.getSxqx_id() + "') and is_xzcf =1"));
		req.setAttribute("xwlist", xwlist);
		req.setAttribute("qxlist", qxlist);
		req.setAttribute("cslist", cslist);
		//行政处罚信用类型
//		Sql sql = Sqls.create("select distinct a.xylx,b.name from breach_info a,cs_value b where b.typeid='00010005' "
//			       +"and a.xylx = b.code and is_xzcf = 1 order by a.xylx desc");
//		Hashtable<String, String> xzcf_type = daoCtl.getHTable(dao, sql);

		//撤销原因
		String url="xzcfid="+id;
		String csjlSql="select * from sys_log where cz='撤销' and substr(url,22,38)='"+url+"'";
		Sys_log syslog= daoCtl.detailBySql(dao, Sys_log.class, Sqls.create(csjlSql));
		req.setAttribute("syslog", syslog);
		
		req.setAttribute("xzcf", xzcf);
		req.setAttribute("xyzt", xyzt);
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("fileList", daoCtl.getMulRowValue(dao, Sqls.create("select filename,filepath from file_info where tablekey='"+id+"' and tablename='xzcf_info' order by create_time asc")));
	}

	//发布 revoke撤销  release 发布
	@At
	@Ok("raw")
	public boolean toIssue(@Param("xzcfid") String xzcfid,HttpSession session){
		Sys_user user = (Sys_user)session.getAttribute("userSession");
		boolean flag = true;
		Xzcf_info xzcf_info = daoCtl.detailById(dao, Xzcf_info.class, StringUtil.StringToInt(xzcfid));
		int ph=xzcf_info.getXyptbatchno();
		xzcf_info.setXyptbatchno(ph+1);//更新批号
		xzcf_info.setSjc(DateUtil.str2sjc(DateUtil.getCurDateTime()));  // 时间戳 
		xzcf_info.setData_state("2");//1：新增数据；2：更新数据；3：删除数据
		dao.update(xzcf_info);
		daoCtl.update(dao, xzcf_info);
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzcf_info.getXyzt_id());
		flag = daoCtl.exeUpdateBySql(dao,Sqls.create("update xzcf_info set issue='1'  where id='" + xzcfid + "'"));
		SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag,5,
				"发布信用主体【"+xyzt.getName()+"】的行政处罚记录", "行政处罚", "", "", "",url.replace("@id", xzcfid));
		return flag;
	}

	//撤销 revoke撤销  release 发布
	@At
	@Ok("->:/private/xzcf/xzcfxxCancel.html")
	public void toCancel(@Param("xzcfid")String xzcfid,HttpServletRequest req,HttpSession session){
		//行政处罚信息
		int id = StringUtil.StringToInt(xzcfid);
		Xzcf_info xzcf = daoCtl.detailById(dao, Xzcf_info.class, id);
		//信用主体信息
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzcf.getXyzt_id());
		xyzt.setType(xyzt.getType().trim());
		//合同信息
		String htbm = comUtil.htTableMap.get(xzcf.getXzcf_type());
		if(EmptyUtils.isNotEmpty(htbm)){
			Sql sqlht = Sqls.create("select htmc from "+htbm+" where htid = '"+xzcf.getContract_id()+"' ");
			List<Map> htlist = daoCtl.list(dao, sqlht);
			req.setAttribute("htlist", htlist);
		}
		//失信行为
		Sql sqlxw = Sqls.create("select sxxw from breach_info where id = '" + xzcf.getSxxw_id() + "' ");
		if("0043".equals(xzcf.getSxxw_id().substring(0,4)) ||"0044".equals(xzcf.getSxxw_id())||"0045".equals(xzcf.getSxxw_id()) ){
			req.setAttribute("isxz",1);
		}
		String sxxw=daoCtl.getStrRowValue(dao, sqlxw);
		Breach_info xwinfo = daoCtl.detailByName(dao, Breach_info.class, xzcf.getSxqx_id());
		req.setAttribute("sxxw", sxxw);
		req.setAttribute("xwinfo", xwinfo);

		req.setAttribute("xzcf", xzcf);
		req.setAttribute("xyzt", xyzt);
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("fileList", daoCtl.getMulRowValue(dao, Sqls.create("select filename,filepath from file_info where tablekey='"+id+"' and tablename='xzcf_info' order by create_time asc")));
	}

	@At
	@Ok("raw")
	public boolean revoke(HttpSession session,@Param("xyzt_id") String xyzt_id,@Param("xzcfid") final String xzcfid,@Param("note") final String note,
						  @Param("reason") final String reason,@Param("basis") final String basis){
		final Sys_user user = (Sys_user)session.getAttribute("userSession");
		final Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, StringUtil.StringToInt(xyzt_id));
		final int id = StringUtil.StringToInt(xzcfid);
		Xzcf_info xzcf_info = daoCtl.detailById(dao, Xzcf_info.class, id);
		int ph=xzcf_info.getXyptbatchno();
		xzcf_info.setXyptbatchno(ph+1);//更新批号
		xzcf_info.setSjc(DateUtil.str2sjc(DateUtil.getCurDateTime()));  // 时间戳
		xzcf_info.setData_state("3");//1：新增数据；2：更新数据；3：删除数据
		dao.update(xzcf_info);
		daoCtl.update(dao, xzcf_info);
		final ThreadLocal<Boolean> result = new ThreadLocal<Boolean>();
		try {
			Trans.exec(new Atom() {
				public void run() {
//					boolean flag = daoCtl.exeUpdateBySql(dao, Sqls.create("update xzcf_info set xzcf_zt=2,issue=0 where id = '" + id + "'"));
					boolean flag = dao.update("xzcf_info", Chain.make("xzcf_zt","2").add("issue","0"), Cnd.where("id","=",id)) > 0;
					dao.update("DISCREDIT_INFO",Chain.make("type",2),Cnd.where("xzcf_id","=",id));

					SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag, 4,
							"撤销信用主体【" + xyzt.getName() + "】的行政处罚记录", "行政处罚", "" + note + "", "" + reason + "", "" + basis + ""
							, url.replace("@id", xzcfid));
					result.set(flag);
				}
			});
		}catch(Exception e){
			e.printStackTrace();
			result.set(false);
		}
		return result.get();
	}
	@At
	@Ok("->:/private/xzcf/xzcfxxLX.html")
	public void toLx(@Param("xzcfid")String xzcfid,HttpServletRequest req,HttpSession session){
		//行政处罚信息
		int id = StringUtil.StringToInt(xzcfid);
		Xzcf_info xzcf = daoCtl.detailById(dao, Xzcf_info.class, id);
		//信用主体信息
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzcf.getXyzt_id());
		xyzt.setType(xyzt.getType().trim());
		//合同信息
		String htbm = comUtil.htTableMap.get(xzcf.getXzcf_type());
		if(EmptyUtils.isNotEmpty(htbm)){
			Sql sqlht = Sqls.create("select htmc from "+htbm+" where htid = '"+xzcf.getContract_id()+"' ");
			List<Map> htlist = daoCtl.list(dao, sqlht);
			req.setAttribute("htlist", htlist);
		}
		//失信行为
		Sql sqlxw = Sqls.create("select id,sxxw from breach_info where id = '"+xzcf.getSxxw_id()+"' and length(id)=4 and is_xzcf =1");
		if("0043".equals(xzcf.getSxxw_id().substring(0,4)) ||"0044".equals(xzcf.getSxxw_id())||"0045".equals(xzcf.getSxxw_id()) ){
			sqlxw = Sqls.create("select sxxw from breach_info where id = '" + xzcf.getSxxw_id() + "' ");
			req.setAttribute("isxz",1);
		}
		List<Map> xwlist = daoCtl.list(dao, sqlxw);
		//失信情形
		Sql sqlqx = Sqls.create("select id,sxqx from breach_info where id like '"+xzcf.getSxxw_id()+"%' and is_xzcf =1");
		List<Map> qxlist = daoCtl.list(dao, sqlqx);
		//惩戒措施
		Sql sqlcs = Sqls.create("select id,cjcs from breach_info where id like '"+xzcf.getSxxw_id()+"%' and sxqx =(select sxqx from breach_info where id = '"+xzcf.getSxqx_id()+"' and is_xzcf =1) and is_xzcf =1");
		List<Map> cslist = daoCtl.list(dao, sqlcs);
		//惩戒依据
		Sql sqlyj = Sqls.create("select yj from breach_info where id = '"+xzcf.getSxqx_id()+"'");
		List<Map> yjlist = daoCtl.list(dao, sqlyj);

		req.setAttribute("xzcf", xzcf);
		req.setAttribute("xyzt", xyzt);
		req.setAttribute("xwlist", xwlist);
		req.setAttribute("qxlist", qxlist);
		req.setAttribute("yjlist", yjlist);
		req.setAttribute("cslist", cslist);
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("fileList", daoCtl.getMulRowValue(dao, Sqls.create("select filename,filepath from file_info where tablekey='"+id+"' and tablename='xzcf_info' order by create_time asc")));
	}
	@At
	@Ok("raw")
	public boolean doLxqk(HttpSession session,@Param("xzcfid") int xzcfid,@Param("sjlxqk") String sjlxqk){
		boolean flag = false;
		Sys_user user = (Sys_user)session.getAttribute("userSession");
		Xzcf_info info = daoCtl.detailById(dao, Xzcf_info.class, xzcfid);
		if(EmptyUtils.isNotEmpty(info)&&EmptyUtils.isNotEmpty(sjlxqk)){
			Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, info.getXyzt_id());
			//info.setCflx_date(cflx_date);
			info.setSjlxqk(sjlxqk);
			info.setXzcf_zt("1");  // 0 表示惩戒结束
			int ph=info.getXyptbatchno();
			info.setXyptbatchno(ph+1);//更新批号
			info.setSjc(DateUtil.str2sjc(DateUtil.getCurDateTime()));  // 时间戳
			info.setData_state("2");//1：新增数据；2：更新数据；3：删除数据
			flag = daoCtl.update(dao, info);
			
			if(flag){
				Discredit_info disinfo=daoCtl.detailByName(dao, Discredit_info.class, "xzcf_id", xzcfid);
				//disinfo.setPerform_date(cflx_date);
				if(sjlxqk.equals("2")){//当实际履行情况为全部履行的时候，惩戒结束
					disinfo.setType("1");//惩戒结束
				}else if(sjlxqk.equals("3")){//当实际履行情况为依法不再履行，撤销惩戒
					disinfo.setType("2");//已撤销
				}
				daoCtl.update(dao, disinfo);
			}
			SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag,2,
					"填报信用主体【"+xyzt.getName()+"】的行政处罚履行情况", "行政处罚", "",
					"", "",url.replace("@id", xzcfid+""));
		}
		return flag;
	}
	
	/**
	 * 标注
	 * @param xzcfid
	 * @param req
	 * @param session
	 */
	@At
	@Ok("->:/private/xzcf/xzcfxxBZ.html")
	public void toBz(@Param("xzcfid")String xzcfid,HttpServletRequest req,HttpSession session){
		//行政处罚信息
		int id = StringUtil.StringToInt(xzcfid);
		Xzcf_info xzcf = daoCtl.detailById(dao, Xzcf_info.class, id);
		//信用主体信息
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, xzcf.getXyzt_id());
		xyzt.setType(xyzt.getType().trim());
		//合同信息
		String htbm = comUtil.htTableMap.get(xzcf.getXzcf_type());
		if(EmptyUtils.isNotEmpty(htbm)){
			Sql sqlht = Sqls.create("select htmc from "+htbm+" where htid = '"+xzcf.getContract_id()+"' ");
			List<Map> htlist = daoCtl.list(dao, sqlht);
			req.setAttribute("htlist", htlist);
		}
		//失信行为
		Sql sqlxw = Sqls.create("select id,sxxw from breach_info where id = '"+xzcf.getSxxw_id()+"' and length(id)=4 and is_xzcf =1");
		if("0043".equals(xzcf.getSxxw_id().substring(0,4)) ||"0044".equals(xzcf.getSxxw_id())||"0045".equals(xzcf.getSxxw_id()) ){
			sqlxw = Sqls.create("select sxxw from breach_info where id = '" + xzcf.getSxxw_id() + "' ");
			req.setAttribute("isxz",1);
		}
		List<Map> xwlist = daoCtl.list(dao, sqlxw);
		//失信情形
		Sql sqlqx = Sqls.create("select id,sxqx from breach_info where id like '"+xzcf.getSxxw_id()+"%' and is_xzcf =1");
		List<Map> qxlist = daoCtl.list(dao, sqlqx);
		//惩戒措施
		Sql sqlcs = Sqls.create("select id,cjcs from breach_info where id like '"+xzcf.getSxxw_id()+"%' and sxqx =(select sxqx from breach_info where id = '"+xzcf.getSxqx_id()+"' and is_xzcf =1) and is_xzcf =1");
		List<Map> cslist = daoCtl.list(dao, sqlcs);
		//惩戒依据
		Sql sqlyj = Sqls.create("select yj from breach_info where id = '"+xzcf.getSxqx_id()+"'");
		List<Map> yjlist = daoCtl.list(dao, sqlyj);

		req.setAttribute("xzcf", xzcf);
		req.setAttribute("xyzt", xyzt);
		req.setAttribute("xwlist", xwlist);
		req.setAttribute("qxlist", qxlist);
		req.setAttribute("yjlist", yjlist);
		req.setAttribute("cslist", cslist);
		req.setAttribute("ztMap", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("fileList", daoCtl.getMulRowValue(dao, Sqls.create("select filename,filepath from file_info where tablekey='"+id+"' and tablename='xzcf_info' order by create_time asc")));
	}
	@At
	@Ok("raw")
	public boolean doBiaozhu(HttpSession session,@Param("xzcfid") int xzcfid,@Param("biaozhu") String biaozhu){
		boolean flag = false;
		Sys_user user = (Sys_user)session.getAttribute("userSession");
		Xzcf_info info = daoCtl.detailById(dao, Xzcf_info.class, xzcfid);
		if(EmptyUtils.isNotEmpty(info)&&EmptyUtils.isNotEmpty(biaozhu)){
			Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, info.getXyzt_id());
			info.setBiaozhu(biaozhu);
			info.setXzcf_zt("1");  // 0 表示惩戒结束
			int ph=info.getXyptbatchno();
			info.setXyptbatchno(ph+1);//更新批号
			info.setSjc(DateUtil.str2sjc(DateUtil.getCurDateTime()));  // 时间戳
			info.setData_state("2");//1：新增数据；2：更新数据；3：删除数据
			flag = daoCtl.update(dao, info);
			if(flag){
				Discredit_info disinfo=daoCtl.detailByName(dao, Discredit_info.class, "xzcf_id", xzcfid);
				if(biaozhu.equals("05")){
					
					String today = DateUtil.getToday();
					if(null != disinfo.getEnd_date() && disinfo.getEnd_date().length()>0){
						if(DateUtil.getDifferDays(disinfo.getStart_date(),today) > 0 &&
								DateUtil.getDifferDays(today,disinfo.getEnd_date()) > 0){
							if(disinfo.getSxxw_id().equals("0043")){//若该条失信记录属于行政处罚
								disinfo.setType("4");//则改为通报
							}else{
								disinfo.setType("0");//恢复正常则改为惩戒中
							}
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
				daoCtl.update(dao, disinfo);
				
			}
			SysLogUtil.addLog(dao, user.getLoginname(), SysLogUtil.gtyx_log_type, flag,2,
					"填报信用主体【"+xyzt.getName()+"】的行政处罚标注", "行政处罚", "",
					"", "",url.replace("@id", xzcfid+""));
		}
		return flag;
	}
}
