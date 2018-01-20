package com.hits.modules.com;

import java.util.*;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.quartz.SchedulerException;

import com.hits.common.config.Globals;
import com.hits.common.dao.ObjectCtl;
import com.hits.common.util.StringUtil;
import com.hits.modules.bean.Cs_value;
import com.hits.modules.cgtable.util.CommonStaticUtil;
import com.hits.modules.form.FormUtil;
import com.hits.modules.quartz.QuartzTimeAction;
import com.hits.util.EmptyUtils;


@IocBean
public class comUtil {
	public static Hashtable<String, String> userMap = null;
	public static Hashtable<String, Hashtable<String, String>> GgbNameMp = new Hashtable<String, Hashtable<String, String>>();//企业类型
	
    public static Hashtable<String, String> xydj = new Hashtable<String, String>();//信用等级
    public static Hashtable<String, String> djjg = new Hashtable<String, String>();//登记机关
    public static Hashtable<String, String> xzqh = new Hashtable<String, String>();//行政区划
    public static Hashtable<String, String> xzqh_cv = new Hashtable<String, String>();//行政区划
    public static Hashtable<String, String> unitMap = new Hashtable<String, String>();//系统单位
    public static Hashtable<String, String> statusMap = new Hashtable<String, String>();//状态类型
    public static Hashtable<String, String> yhjgHt = new Hashtable<String, String>();//所有单位所在省厅或市县局或行业协会
    public static List<List<String>> unittype = new ArrayList<List<String>>();//单位类型
    public static List<List<String>> status = new ArrayList<List<String>>();//状态
    public static Hashtable<String, String> unittypeMap = new Hashtable<String, String>();//单位类型
    public static Hashtable<String, String> ryztMap = new Hashtable<String, String>();//人员状态
    public static List<Map> isfhMap = new ArrayList<Map>();//是否发货
    public static Hashtable<String, String> isfhHash = new Hashtable<String, String>();
    public static List<Cs_value> qyztList = new ArrayList<Cs_value>();//企业状态
    public static List<Cs_value> qylxList = new ArrayList<Cs_value>();//企业类型
    public static List<Cs_value> sxxwtypeList = new ArrayList<Cs_value>();//企业状态
    public static List<Map> xydjList = new ArrayList<Map>();//信用等级
    public static List<Cs_value> djjgList = new ArrayList<Cs_value>();//登记机关
    public static List<Cs_value> xzqhList = new ArrayList<Cs_value>();//行政区划
    public static List<Map> xzqhList_cv = new ArrayList<Map>();//行政区划
    
	public static Hashtable holidays=new Hashtable();//工作日放假
    public static Hashtable workdays=new Hashtable();//周末加班
    
    public static List<Map> tzList = new ArrayList<Map>();//通知方式
    public static Hashtable<String,String> enterPrise = new Hashtable<String,String>();
    /*************以下来自sys_userparamcofig*******************/
    public static String version="";//行政区划代码
    public static String sysCode="";//行政区划代码
    public static int qdSpacing = 0; // 查询时间间隔
    public static int pageSize = 0; // 一页记录显示条数
    public static String masUrl="";//短信url
    public static String masEpid="";//短信epid
    public static String masUsername="";//短信username
    public static String masPassword="";//短信password
    
    public static String filepath = "";// 上传文件存储路径
	public static String fileurl = "";// 上传文件访问路径(内网)
	public static String fileurl_ww = "";// 上传文件访问路径(外网)
    /*************以上来自sys_userparamcofig*******************/
	/*图片上传文件类型配置*/
	public static String images = "";// 图片
	public static String document = "";// 文档
	public static String music = "";// 音乐
	public static String video = "";// 视频
	public static String archive = "";// 其他
	/************信用主体初始化参数*****************************/
	public static Map<String,String> xyztMap = null;  // 信用主体Map，格式：键：信用主体id，值：主体名称☆统一社会信用代码☆组织机构代码
	public static Map<Integer,String> logCzMap = new HashMap<Integer,String>(); // 信用主体日志操作参数
	public static Map<String,String> xyMap = null; // 信用类型code对应关系Map
	public static Map<String,String> htTableMap = new HashMap<String,String>();//信用目录下对应的合同表名
	public static Map<String,String> formywMap = new HashMap<String,String>();//表单业务类型
	public static List<Map> xyztlxMap = new ArrayList<Map>();//信用主体类型
	public static Hashtable<String, String> formHtmlMap = new Hashtable<String,String>();//表单内容初始化
	/************信用主体初始化参数结束*****************************/
	public static Map<String,String> gmtjMap = new HashMap<String,String>();//购买途径
	public static Map<String,String> zcytMap = new HashMap<String,String>();//用途
	public static List<Map> gmtjList = new ArrayList<Map>();//购买途径 
	public static List<Map> ytList = new ArrayList<Map>();//用途
	public static Map<String,String> hhggMap = new HashMap<String,String>();//货号规格
	public static List<Map> hhggList = new ArrayList<Map>();//货号规格
    /*************以下来自sys_templateconfig*******************/
    /*************以上来自sys_templateconfig*******************/
	public static String taskGroup="gtxt";
	public static Hashtable<String, String> taskyxMap = new Hashtable<String, String>();//定时任务运行时间
	public void init() {
		/**************以下部分为测试使用，系统参数配置做好以后，更改此处代码********************/
		Dao dao = Mvcs.getIoc().get(Dao.class);
		ObjectCtl daoCtl=new ObjectCtl();
		setHt(dao, daoCtl, "00010001");
		setHt(dao, daoCtl, "00010004");
		setHt(dao, daoCtl, "00010005");
		setHt(dao, daoCtl, "00010006");
		setHt(dao, daoCtl, "00010010");
		setHt(dao, daoCtl, "00010011");
		setHt(dao, daoCtl, "00010012");
		setHt(dao, daoCtl, "00010013");
		setHt(dao, daoCtl, "00010022");
		setHt(dao, daoCtl, "00010014");
		setHt(dao, daoCtl, "00010015");
		setHt(dao, daoCtl, "00010016");
		setHt(dao, daoCtl, "00010038");//是否发货
		setHt(dao, daoCtl, "00010039");//货号规格
		setHt(dao, daoCtl, "00010040");//购买途径
		setHt(dao, daoCtl, "00010041");//支出用途
		setHt(dao, daoCtl, "00020001");
		setHt(dao, daoCtl, "00020003");
		setHt(dao, daoCtl, "00020004");
		setHt(dao, daoCtl, "00020007");
		setHt(dao, daoCtl, "00020014");
		setHt(dao, daoCtl, "00020015");
		setHt(dao, daoCtl, "00030003");
		setHt(dao, daoCtl, "00040001");
		setHt(dao, daoCtl, "00040002");
		setHt(dao, daoCtl, "00040003");
		setHt(dao, daoCtl, "00040004");
		setHt(dao, daoCtl, "00040005");
		setHt(dao, daoCtl, "00040006");
		setHt(dao, daoCtl, "00040007");
		setHt(dao, daoCtl, "00040008");
		setHt(dao, daoCtl, "00040009");
		/**************以上部分为测试使用，系统参数配置做好以后，更改此处代码********************/
		
		setParamConfig(dao, daoCtl);
		System.out.println("pageSize:::"+pageSize);
		
		setTemplate(dao, daoCtl);
		initUnit(dao, daoCtl);
		String sql = "select loginname k,realname v from sys_user";
		userMap = daoCtl.getHTable(dao, Sqls.create(sql));
		/*********初始化信用主体Map************/
		//xyztMap = daoCtl.getHTable(dao,Sqls.create(" select id,name||'☆'||nvl(code,' ')||'☆'||nvl(zzjgdm,' ')||'☆' from xyzt_info order by id "));
		

		logCzMap.put(1,"新增");
		logCzMap.put(2,"修改");
		logCzMap.put(3,"删除");
		logCzMap.put(4,"撤销");
		logCzMap.put(5,"发布");

		
		
		setHtTable(dao, daoCtl, "1");//信用目录对应的合同表名
		setHt(dao, daoCtl, "00020009");
		setFormParam(dao, daoCtl);
		/**
		 * 项目启动的时候初始化quartz任务
		 */
		QuartzTimeAction qt = Mvcs.getIoc().get(QuartzTimeAction.class);
		try {
			qt.initQuartzTask();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void setFormParam(Dao dao,ObjectCtl daoCtl) {
		try {
			System.out.println("******setFormHtml******start********");
			Sql sql=Sqls.create("select defid,formhtml,viewhtml from form_def  order by defid");
			List<Map> list=daoCtl.list(dao,sql);
			for (Map map : list) {
				String defid=StringUtil.null2String(map.get("defid"));
				String formhtml=StringUtil.null2String(map.get("formhtml"));
				String viewhtml=StringUtil.null2String(map.get("viewhtml"));
				formHtmlMap.put(defid+"_add", FormUtil.generateHtml(formhtml,"add"));
				formHtmlMap.put(defid+"_update", FormUtil.generateHtml(formhtml,"update"));
				formHtmlMap.put(defid+"_makeup", FormUtil.generateHtml(formhtml,"makeup"));
				if(EmptyUtils.isEmpty(viewhtml)){
					formHtmlMap.put(defid+"_view", FormUtil.generateHtml(formhtml,"view"));
				}else{
					formHtmlMap.put(defid+"_view", FormUtil.generateHtml(viewhtml,"view"));
				}
			}
			sql=Sqls.create("select tableid from form_table  order by tableid");
			list=daoCtl.list(dao,sql);
			for (Map map : list) {
				String tableid=StringUtil.null2String(map.get("tableid"));
				sql=Sqls.create("select fieldname,fieldlabel from form_field where status=0 and tableid="+tableid+" order by location");
				GgbNameMp.put(tableid, daoCtl.getHTable(dao, sql));
			}
			System.out.println(GgbNameMp.toString());
			System.out.println("******setFormHtml******end********");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void setHtTable(Dao dao,ObjectCtl daoCtl,String typeid) {
		Sql sql=Sqls.create("select d.xyml,'"+CommonStaticUtil.TABLE_NAME_SUB+"'||t.tablekey from form_def d,form_table t where t.ismain=1 and d.ywtype='"+typeid+"' and d.defid=t.formdefid order by defid");
		htTableMap=daoCtl.getHTable(dao, sql);
	}
	
	
	// 获取系统默认参数
	public static void setHt(Dao dao,ObjectCtl daoCtl,String typeid) {
		Sql sql=Sqls.create("select value,name from cs_value where typeid='"+typeid+"' and state=0 order by location asc,id asc ");
		if("00020004".equals(typeid)){
			xydj=daoCtl.getHTable(dao, sql);
			xydjList=daoCtl.list(dao, sql);
		}else if("00010039".equals(typeid)){
			//sql=Sqls.create("select value,name from cs_value where typeid='"+typeid+"' and state=0 and value like '____' order by location asc,id asc ");
			hhggList = daoCtl.list(dao, sql);
			hhggMap=daoCtl.getHTable(dao, sql);
		}else if("00010040".equals(typeid)){
			gmtjList = daoCtl.list(dao, sql);
			gmtjMap=daoCtl.getHTable(dao, sql);
		}else if("00010041".equals(typeid)){
			zcytMap=daoCtl.getHTable(dao, sql);
			ytList=daoCtl.list(dao, sql);
		}else if("00010004".equals(typeid)){
			xzqh=daoCtl.getHTable(dao, sql);
			xzqhList=daoCtl.list(dao, Cs_value.class, sql);
			Sql sql_cv = Sqls.create("select code,value from cs_value where typeid='"+typeid+"' and state=0 order by location asc,id asc");
			xzqh_cv=daoCtl.getHTable(dao, sql_cv);
			xzqhList_cv=daoCtl.list(dao, sql_cv);
		}else if("00010038".equals(typeid)){
			isfhHash = daoCtl.getHTable(dao, sql);
			isfhMap=daoCtl.list(dao, sql);
		}else if("00020006".equals(typeid)){
			djjg=daoCtl.getHTable(dao, sql);
			djjgList=daoCtl.list(dao, Cs_value.class, sql);
		}else if("00010001".equals(typeid)){
			unittypeMap=daoCtl.getHTable(dao, sql);
			unittype = daoCtl.getMulRowValue(dao, sql);
		}else if(("00010023").equals(typeid)){
			statusMap = daoCtl.getHTable(dao, sql);
			status=daoCtl.getMulRowValue(dao, sql);
		}else if("00020009".equals(typeid)){
			formywMap=daoCtl.getHTable(dao, sql);
		}else if(("00040001").equals(typeid)){
			GgbNameMp.put("fr", daoCtl.getHTable(dao, sql));
		}else if(("00040002").equals(typeid)){
			GgbNameMp.put("zrr", daoCtl.getHTable(dao, sql));
		}else if(("00040003").equals(typeid)){
			GgbNameMp.put("qtzz", daoCtl.getHTable(dao, sql));
		}else if(("00040004").equals(typeid)){
			GgbNameMp.put("xzcf", daoCtl.getHTable(dao, sql));
		}else if(("00040005").equals(typeid)){
			GgbNameMp.put("xzqz", daoCtl.getHTable(dao, sql));
		}else if(("00040006").equals(typeid)){
			GgbNameMp.put("sxcj", daoCtl.getHTable(dao, sql));
		}else if(("00040007").equals(typeid)){
			GgbNameMp.put("jlxx", daoCtl.getHTable(dao, sql));
		}else if(("00040008").equals(typeid)){
			GgbNameMp.put("zzxx", daoCtl.getHTable(dao, sql));
		}else if(("00040009").equals(typeid)){
			GgbNameMp.put("zcxx", daoCtl.getHTable(dao, sql));
		}
		
	}
	
	public static void initHoliday(Dao dao,ObjectCtl daoCtl)
    {
    	holidays.clear();
    	workdays.clear();
    	holidays=daoCtl.getHTable(dao, Sqls.create("select holiday,holiday from SYS_HOLIDAYINFO where iswork=0"));
    	workdays=daoCtl.getHTable(dao, Sqls.create("select holiday,holiday from SYS_HOLIDAYINFO where iswork=1"));

    }
	
	public static void initUnit(Dao dao,ObjectCtl daoCtl)
    {
    	unitMap.clear();
    	unitMap=daoCtl.getHTable(dao, Sqls.create("select id,name from Sys_unit order by id,location"));
    	yhjgHt=daoCtl.getHTable(dao, Sqls.create("select id,id name from Sys_unit where unittype in (88,90) or length(id)=4  order by id,location"));
    	List<Map> yhjg=daoCtl.list(dao, Sqls.create(" select id,name from SYS_UNIT where unittype not in (88,90) and length(id)=8 order by id asc,location asc "));
    	for (Map map : yhjg) {
			String unitid=(String)map.get("id");
			yhjgHt.put(unitid, yhjgHt.get(unitid.substring(0,4)));
		}
    	List<Map> tmp=daoCtl.list(dao, Sqls.create(" select id,name from SYS_UNIT where unittype not in (88,90) and length(id)=12 order by id asc,location asc "));
    	for (Map map : tmp) {
			String unitid=(String)map.get("id");
			yhjgHt.put(unitid, yhjgHt.get(unitid.substring(0,8)));
		}
    }
	
	/*
	 * 设置系统用户参数哦
	 */
	public static void  setParamConfig(Dao dao,ObjectCtl daoCtl){
		Hashtable<String,String> ht = daoCtl.getHTable(dao, Sqls.create("select typename,typevalue from sys_userparamconfig "));
		version=ht.get("version");
		sysCode=ht.get("sys_code");
		Globals.APP_NAME=ht.get("app_name");
		masUrl=ht.get("mas_url");
		masEpid=ht.get("mas_epid");
		masUsername=ht.get("mas_username");
		masPassword=ht.get("mas_password");
		pageSize=StringUtil.StringToInt(ht.get("PageSizes")); //一页记录显示条数
		filepath = ht.get("filepath");
		fileurl = ht.get("fileurl");
		fileurl_ww = ht.get("fileurl_ww");
		images = ht.get("file-images");
		music = ht.get("file-music");
		video = ht.get("file-video");
		document = ht.get("file-document");
		archive = ht.get("file-archive");
	}
	
	/*
	 * 设置系统系统模板
	 */
	public static void  setTemplate(Dao dao,ObjectCtl daoCtl){
		Hashtable<String,String> ht = daoCtl.getHTable(dao, Sqls.create("select id,des from sys_templateconfig  where id like 'dxpj____' and state=0"));
	}
}
