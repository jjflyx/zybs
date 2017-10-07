package com.hits.modules.order;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.hits.common.action.BaseAction;
import com.hits.common.dao.ObjectCtl;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.DateUtil;
import com.hits.common.util.StringUtil;
import com.hits.modules.bean.File_info;
import com.hits.modules.com.YWCL;
import com.hits.modules.com.comUtil;
import com.hits.modules.order.bean.OrderBean;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;
import com.hits.util.PageObj;
import com.hits.util.SysLogUtil;

/**
 * @author JJF E-mail:
 * @version 创建时间：2017年8月8日 上午7:55:26
 * 类说明
 */
@IocBean
@At("/private/order")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class OrderAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/order/orderList.html")
	public void order(HttpSession session, HttpServletRequest req,@Param("startdate") String startdate,@Param("enddate") String enddate,@Param("unitid") String unitid) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		req.setAttribute("unitid", unitid);
		req.setAttribute("startdate", startdate);
		req.setAttribute("enddate", enddate);
		req.setAttribute("isfhHash", JSONObject.fromObject(comUtil.isfhHash));
		req.setAttribute("loginname",user.getLoginname());
		Sql sql=Sqls.create("select id,name from sys_unit where unittype=88");
		List<Map> unitMap = new ArrayList<Map>();
		unitMap=daoCtl.list(dao, sql);
		req.setAttribute("unitMap", unitMap);
		req.setAttribute("isfhMap",comUtil.isfhMap);
	}
	
	//订单页面
	@At
	@Ok("raw")
	public String orderList(HttpServletRequest req,@Param("unitid") String unitid,@Param("isfh") String isfh,HttpSession session,@Param("startdate") String startdate,@Param("enddate") String enddate,
			@Param("name") String name,@Param("page") int curPage, @Param("rows") int pageSize,@Param("sort") String sort,@Param("order") String order){
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		Criteria cri = Cnd.cri();
		String sql="select * from l_jsgg where (actor = '"+user.getLoginname()+"' or unitid = '"+user.getUnitid()+"') ";
		cri.where().and(Cnd.exps("actor", "=", user.getLoginname()).or("unitid", "=", user.getUnitid()));
		if(EmptyUtils.isNotEmpty(unitid)){
			cri.where().and("unitid","like",unitid+"%");
			sql+=" and unitid like '"+unitid+"%'";
		}
		if(EmptyUtils.isNotEmpty(isfh)){
			cri.where().and("isfh","=",isfh);
			sql+=" and isfh = '"+isfh+"'";
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			cri.where().and("add_time",">=",startdate).and("add_time","<=",enddate);
			sql+=" and add_time >= '"+startdate+"' and add_time <= '"+enddate+"'";
		}
		sql += " order by add_time desc";
		QueryResult qr = daoCtl.listPage(dao,OrderBean.class ,curPage, pageSize,Sqls.create(sql),cri);
		List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();
		sql="select code,name from Cs_value where typeid = '00010039'";
		Map<String,String> loadselectMap=daoCtl.getHTable(dao, Sqls.create(sql));
		sql="select id,name from sys_unit where unittype=88";
		Map<String,String> unitMap=daoCtl.getHTable(dao, Sqls.create(sql));
		for(int i=0;i<list.size();i++){
			//获取货品规格信息
			String odKz=StringUtil.null2String(list.get(i).get("hhgg"));
			sql="select code from Cs_value where value='"+odKz+"' and typeid = '00010039'";
			String code=daoCtl.getStrRowValue(dao, Sqls.create(sql));
			int lg=code.length()/4;
			String value="";
			for (int j = 1; j <= lg; j++) {
				value+=loadselectMap.get(code.substring(0,j*4))+"/";
			}
			int lg2=value.length()>0?value.length()-1:0;
			value=value.substring(0,lg2);
			list.get(i).put("hhgg",EmptyUtils.isEmpty(value)?odKz:value);
			//获取客户单位名称
			String unitname = StringUtil.null2String(list.get(i).get("unitid"));
			list.get(i).put("unit", unitMap.get(unitname));
		}
		return PageObj.getPageJSON(qr);
	}
	
	//新增订单页面
	@At
	@Ok("->:/private/order/orderAdd.html")
	public void toAdd(HttpServletRequest req,HttpSession session) {
		req.setAttribute("isfhMap", comUtil.isfhMap);
		req.setAttribute("today", DateUtil.getToday());
		Sql sql=Sqls.create("select id,name from sys_unit where unittype=88");
		List<Map> unitMap = new ArrayList<Map>();
		unitMap=daoCtl.list(dao, sql);
		req.setAttribute("unitMap", unitMap);
	}
	
	//新增订单
	@At
	@Ok("raw")
	public boolean addSave(final HttpServletRequest req,HttpSession session,@Param("..") final OrderBean order,@Param("filepath") final String path,
			@Param("filename") final String name,@Param("filesize") final String filesize){
		final Sys_user user= (Sys_user)session.getAttribute("userSession");
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		try {
			re.set(false);
			Trans.exec(new Atom() {
				public void run() {
					order.setActor(user.getLoginname());
					order.setXzqh_unit(daoCtl.detailByName(dao, Sys_unit.class, order.getUnitid()).getXzqh());
					order.setHtid(getHtid());
					order.setCreate_time(DateUtil.getCurDateTime());
					dao.insert(order);
					//附件
					String[] paths=path.split(";");
					String[] names=name.split(";");
					String[] sizes=filesize.split(";");
					for(int i=0;i<paths.length;i++){
						File_info att = new File_info();
						if(EmptyUtils.isNotEmpty(paths[i])&&EmptyUtils.isNotEmpty(names[i])){
							att.setFilename(names[i]);
							att.setFilepath(paths[i]);
							att.setTablekey(order.getHtid()+"");
							att.setTablename("l_jsgg");
							att.setFieldname("fj");
							att.setCreate_time(DateUtil.getCurDateTime());
							att.setFilesize(sizes[i]);
							dao.insert(att);
						}
					}
					re.set(true);
				}
			});
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return re.get();
	}
	
	/**
	 * 获取单位下负责人的信息
	 * @param loginname
	 * @return
	 */
	@At
	@Ok("raw")
	public String getUnit(@Param("unitid") String unitid) {
		if(EmptyUtils.isNotEmpty(unitid)){
			Sql sql=Sqls.create("select * from sys_unit where id = "+unitid);
			Sys_unit unit = daoCtl.detailByName(dao, Sys_unit.class, unitid);
			String fzrxx = unit.getHandler()+";"+unit.getHandlerphone();
			return fzrxx;
		}else{
			return null;
		}
	}
	
	/**
	 * 前往订单修改页面
	 */
	@At
	@Ok("->:/private/order/orderUpdate.html")
	public void toUpdate(@Param("htid") String id,HttpServletRequest req,HttpSession session){
		OrderBean order = daoCtl.detailByName(dao, OrderBean.class, id);
		req.setAttribute("order", order);
		req.setAttribute("fileList", daoCtl.getMulRowValue(dao, Sqls.create("select filename,filepath||'*'||filesize from file_info where tablekey='"+id+"' and tablename='l_jsgg' order by create_time asc")));
		req.setAttribute("isfhMap", comUtil.isfhMap);
		Sql sql=Sqls.create("select id,name from sys_unit where unittype=88");
		List<Map> unitMap = new ArrayList<Map>();
		unitMap=daoCtl.list(dao, sql);
		req.setAttribute("unitMap", unitMap);
	}
	
	//新增订单
    @At
    @Ok("raw")
    public boolean updateSave(final HttpServletRequest req,HttpSession session,@Param("..") final OrderBean order,@Param("filepath") final String path,
    		@Param("filename") final String name,@Param("filesize") final String filesize){
    	final Sys_user user= (Sys_user)session.getAttribute("userSession");
    	final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
    	try {
    		re.set(false);
    		Trans.exec(new Atom() {
    			public void run() {
    				//附件
					String sqlfile="delete from file_info where tablekey='"+order.getHtid()+"' and tableName='l_jsgg'";
					dao.execute(Sqls.create(sqlfile));
    				String[] paths=path.split(";");
    				String[] names=name.split(";");
    				String[] sizes=filesize.split(";");
    				for(int i=0;i<paths.length;i++){
    					File_info att = new File_info();
    					if(EmptyUtils.isNotEmpty(paths[i])&&EmptyUtils.isNotEmpty(names[i])){
    						att.setFilename(names[i]);
    						att.setFilepath(paths[i]);
    						att.setTablekey(order.getHtid()+"");
    						att.setTablename("l_jsgg");
    						att.setFieldname("fj");
    						att.setCreate_time(DateUtil.getCurDateTime());
    						att.setFilesize(sizes[i]);
    						dao.insert(att);
    					}
    				}
    				dao.update(order);
    				re.set(true);
    			}
    		});
    	}catch (Exception e) {
    		// TODO: handle exception
    		e.printStackTrace();
    	}
    	return re.get();
    }
    
    /**
	 * 前往订单修改页面
	 */
	@At
	@Ok("->:/private/order/orderDetail.html")
	public void toPreview(@Param("htid") String id,HttpServletRequest req,HttpSession session){
		OrderBean order = daoCtl.detailByName(dao, OrderBean.class, id);
		order.setHhgg(YWCL.getValueFromCs(daoCtl, dao, "00010039", order.getHhgg()));
		order.setIsfh(YWCL.getValueFromCs(daoCtl, dao, "00010038", order.getIsfh()));
		req.setAttribute("unit",daoCtl.detailByName(dao, Sys_unit.class, order.getUnitid()) );
		req.setAttribute("fileList", daoCtl.getMulRowValue(dao, Sqls.create("select filename,filepath from file_info where tablekey='"+id+"' and tablename='l_jsgg' order by create_time asc")));
		req.setAttribute("isfhMap", comUtil.isfhMap);
		Sql sql=Sqls.create("select id,name from sys_unit where unittype=88");
		List<Map> unitMap = new ArrayList<Map>();
		unitMap=daoCtl.list(dao, sql);
		req.setAttribute("unitMap", unitMap);
		req.setAttribute("order", order);
	}
	
	/**
	 * 删除合同
	 * @return
	 */
	@At
    @Ok("raw")
    public boolean delete(final @Param("htid") String htid){
		boolean resule = false;
		if(EmptyUtils.isNotEmpty(htid)){
			resule= daoCtl.deleteByName(dao, OrderBean.class, htid);
		}
		return resule;
    }
    
    public synchronized static String getHtid() {
		String letterid="";
		Dao dao = Mvcs.getIoc().get(Dao.class);
		ObjectCtl daoCtl=new ObjectCtl();
		//获取年月日
		 SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		 String ymd=sdf.format(new java.util.Date());
		 letterid=ymd;
		 //查询最新的工号
		 letterid=daoCtl.getSubMenuId(dao, "l_jsgg", "htid", letterid);
		 return letterid;
	}
    
    //导出合同信息
    @At
	public void dcXxsb(HttpServletRequest req,HttpSession session,@Param("htid")String htid,
			@Param("startdate")String startdate,@Param("enddate")String enddate,
			HttpServletResponse response) {
		try {
			String sqlstr = "";
			String hyxhId="";
			List<String> hyxhList = daoCtl.getStrRowValues(dao,Sqls.create(" select id from sys_unit where unittype = 90 order by id "));
			hyxhId = hyxhId.substring(0,hyxhId.length()-1);
			sqlstr = "select t.id,t.type,t.content,t.create_time,t.login_ip,t.bowser,t.letter_id,t.class_name,t.method_name,t.title,t.reason,t.basis,t.cz,t.success,t.url,t.note,t.loginname||'/'||u.realname as loginname from  Sys_log t,sys_user u,sys_unit un where t.loginname=u.loginname and u.unitid=un.id "+
					"and un.unittype=90";
			
			if(EmptyUtils.isNotEmpty(cz)){
				sqlstr+=" and t.cz='"+cz+"' ";
			}
			
			if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
				sqlstr += "and t.create_time between '" + startdate + " 00:00:00' and '" + enddate + " 23:59:59'";
			}
			sqlstr+=" and t.type <> 0";
			
			List<Map> list=daoCtl.list(dao, Sqls.create(sqlstr));
			// 第一步，创建一个webbook，对应一个Excel文件  
			HSSFWorkbook wb = new HSSFWorkbook();  
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
			HSSFSheet sheet = wb.createSheet("中业标识订单信息");  
			sheet.setColumnWidth(1, 20 * 256); //设置列宽
			// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
			int rowIndex = 0;
			HSSFRow row = sheet.createRow(rowIndex++);  
			// 第四步，创建单元格，并设置值表头 设置表头居中  
			HSSFCellStyle style = wb.createCellStyle();  
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
		String[] headers = {"序号","订单名称","客户名称","货品规格","订单日期","应付价款"};
		for (int i = 0; i < headers.length; i++) { // 输出头部
			HSSFCell cell =  row.createCell((short) i);
			cell.setCellValue(headers[i]);
			cell.setCellStyle(style);
		}
		for(int i=0;i<list.size();i++){
			row = sheet.createRow(i+1);
			Map map=list.get(i);
			HSSFCell cell =  row.createCell((short) 0);
			cell.setCellValue(i+1);
			cell.setCellStyle(style);
			cell=  row.createCell((short) 1);
			cell.setCellValue(StringUtil.null2String(map.get("loginname")));
			cell.setCellStyle(style);
			cell=  row.createCell((short) 2);
			cell.setCellValue(StringUtil.null2String(map.get("title")));
			cell.setCellStyle(style);
			cell=  row.createCell((short) 3);
			cell.setCellValue(StringUtil.null2String(map.get("content")));
			cell.setCellStyle(style);
			cell=  row.createCell((short) 4);
			cell.setCellValue(StringUtil.null2String(map.get("cz")));
			cell.setCellStyle(style);
			cell=  row.createCell((short) 5);
			cell.setCellValue(StringUtil.null2String(map.get("create_time")));
			cell.setCellStyle(style);
			cell=  row.createCell((short) 6);
			cell.setCellValue(StringUtil.null2String(map.get("login_ip")));
			cell.setCellStyle(style);
		}
			
			
			response.setContentType("application/vnd.ms-excel;charset=ISO8859-1");
			//response.setHeader("Content-Disposition", "attachment; filename=法人资质信息.xls");// 设定输出文件头
			String fileName="信息上报信息";
			response.setHeader("Content-Disposition", "attachment;filename=\""+ new String(fileName.getBytes("gb18030"),"ISO8859-1") + ".xls" + "\"");
			OutputStream output;
			output = response.getOutputStream();
			wb.write(output);
			output.flush();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

