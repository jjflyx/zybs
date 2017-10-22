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
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
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
import com.hits.modules.bean.Cs_value;
import com.hits.modules.bean.File_info;
import com.hits.modules.com.YWCL;
import com.hits.modules.com.comUtil;
import com.hits.modules.order.bean.HkzdBean;
import com.hits.modules.order.bean.OrderBean;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;
import com.hits.util.PageObj;

@IocBean
@At("/private/hkzd")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class HkzdAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	@At("")
	@Ok("->:/private/hkzd/hkzdList.html")
	public void hkzd(HttpSession session, HttpServletRequest req,@Param("startdate") String startdate,@Param("enddate") String enddate,@Param("userid") String userid) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		req.setAttribute("userid", userid);//从统计分析跳转过来的unitid
		req.setAttribute("czrunitid", user.getUnitid());//当前登录用户的单位id
		req.setAttribute("startdate", startdate);
		req.setAttribute("enddate", enddate);
		req.setAttribute("isfhHash", JSONObject.fromObject(comUtil.isfhHash));
		req.setAttribute("loginname",user.getLoginname());
		Sql sql=Sqls.create("select userid,realname from sys_user where unitid=0016");
		List<Map> fzrMap = new ArrayList<Map>();
		fzrMap=daoCtl.list(dao, sql);
		req.setAttribute("fzrMap", fzrMap);
		req.setAttribute("isfhMap",comUtil.isfhMap);
	}
	
	//订单页面
	@At
	@Ok("raw")
	public String hkzdList(HttpServletRequest req,@Param("userid") String userid,HttpSession session,@Param("startdate") String startdate,@Param("enddate") String enddate,
			@Param("name") String name,@Param("page") int curPage, @Param("rows") int pageSize,@Param("sort") String sort,@Param("order") String order){
		Criteria cri = Cnd.cri();
		String sql="select * from l_hkzd where 1=1 ";
		if(EmptyUtils.isNotEmpty(userid)){
			cri.where().and("userid","=",userid);
			sql+=" and userid = '"+userid+"'";
		}
		if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
			cri.where().and("fkrq",">=",startdate).and("fkrq","<=",enddate);
			sql+=" and fkrq >= '"+startdate+"' and fkrq <= '"+enddate+"'";
		}
		sql += " order by fkrq desc";
		QueryResult qr = daoCtl.listPage(dao,HkzdBean.class ,curPage, pageSize,Sqls.create(sql),cri);
		List<Map<String,Object>> list = (List<Map<String, Object>>) qr.getList();
		sql="select code,name from Cs_value where typeid = '00010040'";
		Map<String,String> loadselectMap=daoCtl.getHTable(dao, Sqls.create(sql));
		sql="select userid,realname from sys_user where unitid=0016";
		Map<String,String> userMap=daoCtl.getHTable(dao, Sqls.create(sql));
		for(int i=0;i<list.size();i++){
			//获取购买途径
			String gmtj=StringUtil.null2String(list.get(i).get("gmtj"));
			list.get(i).put("gmtj",loadselectMap.get(gmtj));
			//获取客户单位名称
			String unitname = StringUtil.null2String(list.get(i).get("userid"));
			list.get(i).put("userid", userMap.get(unitname));
		}
		return PageObj.getPageJSON(qr);
	}
	
	//新增订单页面
	@At
	@Ok("->:/private/hkzd/hkzdAdd.html")
	public void toAdd(HttpServletRequest req,HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		req.setAttribute("userid", user.getUserid());
		req.setAttribute("isfhMap", comUtil.isfhMap);
		req.setAttribute("today", DateUtil.getToday());
		Sql sql=Sqls.create("select userid,realname from sys_user where unitid=0016");
		List<Map> fzrMap = new ArrayList<Map>();
		fzrMap=daoCtl.list(dao, sql);
		req.setAttribute("fzrMap", fzrMap);
	}
	
	//新增订单
	@At
	@Ok("raw")
	public boolean addSave(final HttpServletRequest req,HttpSession session,@Param("..") final HkzdBean hkzd,@Param("filepath") final String path,
			@Param("filename") final String name,@Param("filesize") final String filesize){
		final Sys_user user= (Sys_user)session.getAttribute("userSession");
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		try {
			re.set(false);
			Trans.exec(new Atom() {
				public void run() {
					hkzd.setActor(user.getLoginname());
					hkzd.setZdid(getHtid());
					hkzd.setCreate_time(DateUtil.getCurDateTime());
					dao.insert(hkzd);
					//附件
					String[] paths=path.split(";");
					String[] names=name.split(";");
					String[] sizes=filesize.split(";");
					for(int i=0;i<paths.length;i++){
						File_info att = new File_info();
						if(EmptyUtils.isNotEmpty(paths[i])&&EmptyUtils.isNotEmpty(names[i])){
							att.setFilename(names[i]);
							att.setFilepath(paths[i]);
							att.setTablekey(hkzd.getZdid()+"");
							att.setTablename("l_hkzd");
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
	 * 前往订单修改页面
	 */
	@At
	@Ok("->:/private/hkzd/hkzdUpdate.html")
	public void toUpdate(@Param("zdid") String id,HttpServletRequest req,HttpSession session){
		HkzdBean hkzd = daoCtl.detailByName(dao, HkzdBean.class, id);
		req.setAttribute("hkzd", hkzd);
		req.setAttribute("fileList", daoCtl.getMulRowValue(dao, Sqls.create("select filename,filepath||'*'||filesize from file_info where tablekey='"+id+"' and tablename='l_hkzd' order by create_time asc")));
		req.setAttribute("isfhMap", comUtil.isfhMap);
		Sql sql=Sqls.create("select userid,realname from sys_user where unitid=0016");
		List<Map> fzrMap = new ArrayList<Map>();
		fzrMap=daoCtl.list(dao, sql);
		req.setAttribute("fzrMap", fzrMap);
	}
	
	//新增订单
    @At
    @Ok("raw")
    public boolean updateSave(final HttpServletRequest req,HttpSession session,@Param("..") final HkzdBean hkzd,@Param("filepath") final String path,
    		@Param("filename") final String name,@Param("filesize") final String filesize){
    	final Sys_user user= (Sys_user)session.getAttribute("userSession");
    	final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
    	try {
    		re.set(false);
    		Trans.exec(new Atom() {
    			public void run() {
    				//附件
					String sqlfile="delete from file_info where tablekey='"+hkzd.getZdid()+"' and tableName='l_hkzd'";
					dao.execute(Sqls.create(sqlfile));
    				String[] paths=path.split(";");
    				String[] names=name.split(";");
    				String[] sizes=filesize.split(";");
    				for(int i=0;i<paths.length;i++){
    					File_info att = new File_info();
    					if(EmptyUtils.isNotEmpty(paths[i])&&EmptyUtils.isNotEmpty(names[i])){
    						att.setFilename(names[i]);
    						att.setFilepath(paths[i]);
    						att.setTablekey(hkzd.getZdid()+"");
    						att.setTablename("l_hkzd");
    						att.setFieldname("fj");
    						att.setCreate_time(DateUtil.getCurDateTime());
    						att.setFilesize(sizes[i]);
    						dao.insert(att);
    					}
    				}
    				dao.update(hkzd);
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
	@Ok("->:/private/hkzd/hkzdDetail.html")
	public void toPreview(@Param("zdid") String id,HttpServletRequest req,HttpSession session){
		HkzdBean hkzd = daoCtl.detailByName(dao, HkzdBean.class, id);
		hkzd.setUserid(daoCtl.detailByName(dao, Sys_user.class, hkzd.getUserid()).getRealname());
		hkzd.setGmtj(YWCL.getValueFromCs(daoCtl, dao, "00010040", hkzd.getGmtj()));
		req.setAttribute("fileList", daoCtl.getMulRowValue(dao, Sqls.create("select filename,filepath from file_info where tablekey='"+id+"' and tablename='l_gmtj' order by create_time asc")));
		req.setAttribute("hkzd", hkzd);
	}
	
	/**
	 * 删除合同
	 * @return
	 */
	@At
    @Ok("raw")
    public boolean delete(final @Param("zdid") String zdid){
		boolean resule = false;
		if(EmptyUtils.isNotEmpty(zdid)){
			resule= daoCtl.deleteByName(dao, HkzdBean.class, zdid);
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
		 letterid=daoCtl.getSubMenuId(dao, "l_hkzd", "zdid", letterid);
		 return letterid;
	}
    
    //导出合同信息
    @At
	public void dcXxsb(HttpServletRequest req,HttpSession session,@Param("unitid")String unitid,
			@Param("startdate")String startdate,@Param("enddate")String enddate,
			HttpServletResponse response) {
		try {
			String sqlstr = "";
			String sqlzj = "";
			sqlstr = "select ddmc,unitid,hhgg,add_time,yfjk from l_jsgg where isfh='0002' ";
			sqlzj = "select sum(yfjk) as zj from l_jsgg where isfh='0002'";
			if(EmptyUtils.isNotEmpty(unitid)){
				sqlstr+=" and unitid='"+unitid+"' ";
				sqlzj+=" and unitid='"+unitid+"' ";
			}
			if(EmptyUtils.isNotEmpty(startdate)&&EmptyUtils.isNotEmpty(enddate)){
				sqlstr += "and add_time between '" + startdate + "' and '" + enddate + "'";
				sqlzj += "and add_time between '" + startdate + "' and '" + enddate + "'";
			}
			sqlstr += " order by add_time desc";
			List<Map> list=daoCtl.list(dao, Sqls.create(sqlstr));
			String zj = daoCtl.getStrRowValue(dao, Sqls.create(sqlzj));
			for(Map yytjmap:list){
				yytjmap.put("unitid",daoCtl.detailByName(dao, Sys_unit.class, yytjmap.get("unitid").toString()).getName());
				yytjmap.put("hhgg", daoCtl.detailBySql(dao, Cs_value.class, Sqls.create("select * from cs_value where code ='"+yytjmap.get("hhgg")+"' and typeid='00010039'")).getName());
			}
			// 第一步，创建一个webbook，对应一个Excel文件  
			HSSFWorkbook wb = new HSSFWorkbook();  
			// 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
			HSSFSheet sheet = wb.createSheet("中业标识订单信息");  
			sheet.setColumnWidth(1, 20 * 256); //设置列宽
			sheet.setColumnWidth(2, 30 * 256); //设置列宽
			sheet.setColumnWidth(3, 20 * 256); //设置列宽
			sheet.setColumnWidth(4, 30 * 256); //设置列宽
			sheet.setColumnWidth(5, 20 * 256); //设置列宽
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
		HSSFCell cell =  row.createCell((short) 0);
		for(int i=0;i<list.size();i++){
			row = sheet.createRow(i+1);
			Map map=list.get(i);
			cell=  row.createCell((short) 0);
			cell.setCellValue(i+1);
			cell.setCellStyle(style);
			cell=  row.createCell((short) 1);
			cell.setCellValue(StringUtil.null2String(map.get("ddmc")));
			cell.setCellStyle(style);
			cell=  row.createCell((short) 2);
			cell.setCellValue(StringUtil.null2String(map.get("unitid")));
			cell.setCellStyle(style);
			cell=  row.createCell((short) 3);
			cell.setCellValue(StringUtil.null2String(map.get("hhgg")));
			cell.setCellStyle(style);
			cell=  row.createCell((short) 4);
			cell.setCellValue(StringUtil.null2String(map.get("add_time")));
			cell.setCellStyle(style);
			cell=  row.createCell((short) 5);
			cell.setCellValue(StringUtil.null2String(map.get("yfjk")));
			cell.setCellStyle(style);
		}
		cell=  row.createCell((short) 6);
		cell.setCellValue("总计："+zj);
		response.setContentType("application/vnd.ms-excel;charset=ISO8859-1");
		String fileName="中业标识订单表";
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
