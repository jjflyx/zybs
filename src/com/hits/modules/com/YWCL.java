package com.hits.modules.com;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.mvc.Mvcs;

import com.hits.common.dao.ObjectCtl;
import com.hits.modules.sxcj.bean.Discredit_info;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.modules.tasks.bean.Tasks;
import com.hits.util.DateUtil;
import com.hits.util.EmptyUtils;
import com.hits.util.StringUtil;

public class YWCL {
	public static boolean isInner() {
		String ip = getIpAddr();
		// System.out.println(ip);
		String reg = "(10|172|192)\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})\\.([0-1][0-9]{0,2}|[2][0-5]{0,2}|[3-9][0-9]{0,1})";// ������ʽ=��
																																															// =�����������ִ����ˡ�
		Pattern p = Pattern.compile(reg);
		Matcher matcher = p.matcher(ip);
		return matcher.find();

	}

	public static String getIpAddr() {
		HttpServletRequest request = Mvcs.getReq();
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * ��������:��ȡ�µ��ż����
	 *
	 * @author (��_��) 2015��5��28�� ����11:19:06
	 * 
	 * @param workNo
	 *            �û�����
	 * @return
	 */
	public synchronized static String getNewLetterId(String workNo) {
		String letterid = "";
		Dao dao = Mvcs.getIoc().get(Dao.class);
		ObjectCtl daoCtl = new ObjectCtl();
		// ��ȡ������
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String ymd = sdf.format(new java.util.Date());
		// ���Ʒletterid
		letterid = ymd + comUtil.sysCode + workNo;
		// ��ѯ���µĹ���
		letterid = daoCtl.getSubMenuId(dao, "slj_info", "id", letterid);
		return letterid;
	}

	// �õ�12λʱ�����������֤��
	public synchronized static String getVifCode() {
		int num = 0;
		for (int i = 0; i < 6; i++) {
			num = (int) (num * 10 + Math.round(Math.random() * 10));
		}
		String time = DateUtil.time.format(new Date());
		return time + String.valueOf(num);
	}

	public static String getSlj_infoSNByMaxSN(String maxSN) {
		String SN = "";
		int length = maxSN.length();
		int SNEnd = Integer.parseInt(maxSN.substring(length - 4, length)) + 1;
		String strSNEnd = SNEnd + "";
		while (strSNEnd.length() < 4) {
			strSNEnd = "0" + strSNEnd;
		}
		SN = maxSN.substring(0, length - 4) + strSNEnd;
		return SN;
	}

	public static String getTreeCode(String val, String maxSN) {

		String SN = "";
		int length = maxSN.length();
		if (length == 0)
			return val + "0001";
		int SNEnd = Integer.parseInt(maxSN.substring(length - 4, length)) + 1;
		String strSNEnd = SNEnd + "";
		while (strSNEnd.length() < 4) {
			strSNEnd = "0" + strSNEnd;
		}
		SN = maxSN.substring(0, length - 4) + strSNEnd;
		return SN;
	}

	/**
	 * ��ISO�ַ�ת��ΪUTF-8�ַ�
	 * 
	 * @param str
	 */
	public static String getUTFFromISO(String str) {
		try {
			if (str == null)
				str = "";
			byte[] buf = str.getBytes("iso-8859-1");
			byte[] buf2 = str.getBytes("UTF-8");
			if (!str.equals(new String(buf2, "UTF-8"))) {
				str = new String(buf, "UTF-8");
			}

			return str;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * ͨ��λ����͵�λ��õ���λ��A;B��
	 * 
	 * @param TteeidAndNames
	 */
	public static String getUnitnamesByTreeidAndNames(String TteeidAndNames) {
		StringBuffer result = new StringBuffer();

		TteeidAndNames = TteeidAndNames.trim();
		String[] strs = TteeidAndNames.split(";");
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i];
			if (str != null && !str.trim().equals("")) {
				String temp = str.substring(0, str.indexOf("/"));
				result.append(temp).append("��");
			}
		}
		String str = "";
		if (result.length() > 0) {
			str = result.toString();
			str = str.substring(0, str.length() - 1);
		}
		return str;

	}

	/**
	 * ͨ��λ����͵�λ��õ���λ����(A;B)
	 * 
	 * @param TteeidAndNames
	 */
	public static String getTreeidByTreeidAndNames(String TteeidAndNames) {
		if (TteeidAndNames == null || TteeidAndNames.length() == 0) {
			return "";
		} else {
			StringBuffer result = new StringBuffer();

			TteeidAndNames = TteeidAndNames.trim();
			String[] strs = TteeidAndNames.split(";");
			for (int i = 0; i < strs.length; i++) {
				String str = strs[i];
				if (str != null && !str.trim().equals("")) {
					String temp = str.substring(str.indexOf("/") + 1);
					result.append(temp).append(";");
				}
			}
			return result.toString();
		}
	}

	/**
	 * velocityר��object to String
	 */
	public String toStr(Object obj) {
		return obj.toString();
	}

	/**
	 * �ж�ͬһ����ĵ�λ�Ƿ��Ѿ�����
	 * 
	 * @param con
	 * @param nibanId
	 * @return boolean
	 */
	public static boolean ifALLDo(ObjectCtl daoCtl, Dao dao, int nibanId) {
		String totalSql = "SELECT count(id) FROM slj_nibanunit WHERE nibanid ='" + nibanId + "'";
		String doSql = "SELECT count(id) FROM slj_nibanunit WHERE nibanid ='" + nibanId + "' AND nbstate=3";
		int total = daoCtl.getIntRowValue(dao, Sqls.create(totalSql));
		int dos = daoCtl.getIntRowValue(dao, Sqls.create(doSql));
		System.out.println("total=" + total + ";dos=" + dos);
		return total == dos;
	}

	/**
	 * �ж��Ƿ���һ����λ
	 *
	 * @param user
	 * @return boolean
	 */
	public static boolean ifYiJDW(Sys_user user) {
		boolean result = true;
		try {
			result = user.getUnitid().length() / 4 == 1;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}
	public static String getPKSql(String tablename){
		return " select a.column_name from user_cons_columns a, user_constraints b where a.constraint_name = b.constraint_name and b.constraint_type='P' and a.table_name ='"+tablename.toUpperCase()+"'";
	}
	public static String getFKSql(String tablename){
		return " select a.column_name from user_cons_columns a, user_constraints b where a.constraint_name = b.constraint_name and b.constraint_type='R' and a.table_name ='"+tablename.toUpperCase()+"'";
	}
	public static String getValueFromCs(ObjectCtl daoCtl,Dao dao,String typeid,String oldVal){
		String value="";
		try {
			String sql="select code,name from Cs_value where typeid = '"+typeid+"'";
			Map<String,String> loadselectMap=daoCtl.getHTable(dao, Sqls.create(sql));
			sql="select code from Cs_value where value='"+oldVal+"' and typeid = '"+typeid+"'";
			String code=daoCtl.getStrRowValue(dao, Sqls.create(sql));
			int lg=code.length()/4;
			for (int j = 1; j <= lg; j++) {
				value+=loadselectMap.get(code.substring(0,j*4))+"/";
			}
			int lg2=value.length()>0?value.length()-1:0;
			value=value.substring(0,lg2);
			value=EmptyUtils.isEmpty(value)?oldVal:value;
		} catch (Exception e) {
			e.printStackTrace();
			return oldVal;
		}
		return value;
	}
	public static int updateDisinfoByRule(ObjectCtl daoCtl,Dao dao,String tableid,Discredit_info disinfo){
		int re=1;
		try {
			List<Map> rlist=daoCtl.list(dao, Sqls.create("select * from gtxt_rule where rule like '"+tableid+"@@%'"));
			for (Map<String,String> map : rlist) {
				String[] ruleList = StringUtil.null2String(map.get("rule")).split("@@");
				List<Map> subtabobj=daoCtl.list(dao, Sqls.create("select tablekey,formdefid from  FORM_TABLE where tableid = '"+ruleList[0]+"'"));
				System.out.println("select tablekey,formdefid from  FORM_TABLE where tableid = '"+ruleList[0]+"'");
				if(EmptyUtils.isEmpty(subtabobj)){
					break;
				}
				String tableName = StringUtil.null2String(subtabobj.get(0).get("tablekey"));
				String tableName_id = StringUtil.null2String(subtabobj.get(0).get("formdefid"));
				String tableMain = "l_"+daoCtl.getStrRowValue(dao, Sqls.create("select tablekey from  FORM_TABLE where formdefid = '"+tableName_id+"' and ismain = 1 "));
				System.out.println("tableMain : "+tableMain);
				String formdefid_y = daoCtl.getStrRowValue(dao, Sqls.create("select formdefid from  FORM_TABLE where tablekey = '"+tableName+"' and form_type = 1 "));
				String formdefid_s = daoCtl.getStrRowValue(dao, Sqls.create("select formdefid from  FORM_TABLE where tablekey = '"+tableName+"' and form_type = 2 "));
				String xylx_id = daoCtl.getStrRowValue(dao, Sqls.create(" select xyml from FORM_DEF where defid = '"+formdefid_s+"' "));
				//义务表外键
				String htid_col = daoCtl.getStrRowValue(dao, Sqls.create(getFKSql("L_"+tableName.toUpperCase())));
				//义务表主键
				String ywid = daoCtl.getStrRowValue(dao, Sqls.create(getPKSql("L_"+tableName.toUpperCase())));
				//主表主键
				String tableMain_Pk = daoCtl.getStrRowValue(dao, Sqls.create(getPKSql(tableMain.toUpperCase())));
				String sxqx_id = map.get("sxqx_id");
				String httype = daoCtl.getStrRowValue(dao, Sqls.create("select ht_type from breach_info where id='"+sxqx_id+"'"));
				String httypeSql="";
				if(httype!=null&&!"".equals(httype)){//失信惩戒标准有具体的合同类型
				httypeSql=htid_col+" in (select "+tableMain_Pk+" from "+tableMain+" where httype='"+httype+"') and";
				}
				String ylxrq_y = "";
				String slxrq_y = "";
				String sql_select = "";
				String sql_body = "";
				for(int i=1; i < ruleList.length;i++){
					String[] strs = ruleList[i].split("☆☆");
					if(strs[0].indexOf("rq_") == -1 ){
						//sql_body += " and to_number("+ strs[0]+") "+getSymbol(strs[1])+" nvl(to_number("+strs[2]+"),0)  ";	
						sql_select+=strs[0]+","+strs[2]+",round(to_number("+ strs[0]+")-nvl(to_number("+strs[2]+"),0)) as val"+i+",";
					}else{
						ylxrq_y = strs[0];
						slxrq_y = strs[2];
						//sql_body += " and TO_DATE("+ strs[0]+",'YYYY-mm-dd')  "+getSymbol(strs[1])+" nvl(TO_DATE("+strs[2]+",'YYYY-mm-dd')+10,sysdate+10) ";
						sql_select+=strs[0]+","+strs[2]+",round(TO_DATE("+ strs[0]+",'YYYY-mm-dd')  - nvl(TO_DATE("+strs[2]+",'YYYY-mm-dd'),sysdate)) as val"+i+",";
					}
				}
				String sql_head = "select "+sql_select+"round((to_date("+ylxrq_y+",'yyyy-mm-dd')-to_date(to_char(sysdate,'YYYY-MM-DD'),'YYYY-MM-DD')),0) as value "
						+ "  from l_" + tableName +" where "+httypeSql+" "+ywid+"='"+disinfo.getYwid()+"'";
				
				String sql = sql_head + sql_body ;
				System.out.println("sql ************************: ");
				System.out.println(sql);
				List<Map> list = daoCtl.list(dao, Sqls.create(sql));
				if(list.size()>0){//
					String syts=StringUtil.null2String(list.get(0).get("value"));
					String slxrq=StringUtil.null2String(list.get(0).get(slxrq_y));
					if(EmptyUtils.isNotEmpty(syts)&&EmptyUtils.isEmpty(slxrq)){
						if(Integer.parseInt(syts) >11){
							//如果剩余日期大于11，撤销失信记录
							re=dao.update("discredit_info", Chain.make("type", 2), Cnd.where("id", "=", disinfo.getId()));
							dao.update("l_"+tableName, Chain.make("is_warn", 0), Cnd.where(ywid, "=", disinfo.getYwid()));
						}else if(Integer.parseInt(syts) <=11&&Integer.parseInt(syts) >=0){
							//如果剩余日期在0-10之间
							dao.update("l_"+tableName, Chain.make("is_warn", 0), Cnd.where(ywid, "=", disinfo.getYwid()));
							re=dao.update("discredit_info", Chain.make("type", 2), Cnd.where("id", "=", disinfo.getId()));
						}
					}else{
						boolean sfwc=true;
						for(int i=1; i < ruleList.length;i++){
							String[] strs = ruleList[i].split("☆☆");
							if(strs[0].indexOf("rq_") == -1 ){
								int tmp=StringUtil.StringToInt(StringUtil.null2String(list.get(0).get("val"+i)));
								if(getSymbol(strs[1], tmp)==false){
									sfwc=false;
									continue;
								}
							}
						}
						if(sfwc){//义务已完成
							re=dao.update("discredit_info", Chain.make("type", 1).add("perform_date", slxrq), Cnd.where("id", "=", disinfo.getId()));
						}
					}
				}else{
					dao.update("l_"+tableName, Chain.make("is_warn", 0), Cnd.where(ywid, "=", disinfo.getYwid()));
					re=dao.update("discredit_info", Chain.make("type", 2), Cnd.where("id", "=", disinfo.getId()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			re=-1;
		}
		return re;
	}
		private static String getSymbol(String str){
			String symbol = "=";
			if("大于".equals(str)){
				symbol = ">" ;
			}else if("大于等于".equals(str)){
				symbol = ">=" ;
			}else if("等于".equals(str)){
				symbol = "=" ;
			}else if("小于".equals(str)){
				symbol = "<" ;
			}else if("小于等于".equals(str)){
				symbol = "<=" ;
			}
			return symbol;
		}
		/*
		 * a☆☆str☆☆b,val=a-b
		 */
		private static boolean getSymbol(String str,int val){
			boolean bol = false;
			System.out.println(str+";"+val);
			if("大于".equals(str)&&val>=0){
				bol = true;
			}else if("大于等于".equals(str)&&val<0){
				bol = true;
			}else if("等于".equals(str)&&val==0){
				bol = true;
			}else if("小于".equals(str)&&val>=0){
				bol = true;
			}else if("小于等于".equals(str)&&val>0){
				bol = true;
			}
			return bol;
		}
		public static Map getValue(Object thisObj)  
		  {  
		    Map map = new HashMap();  
		    Class c;  
		    try  
		    {  
		      c = Class.forName(thisObj.getClass().getName());  
		      Method[] m = c.getMethods();  
		      for (int i = 0; i < m.length; i++)  
		      {  
		        String method = m[i].getName();  
		        if (method.startsWith("get"))  
		        {  
		          try{  
		          Object value = m[i].invoke(thisObj);  
		          if (value != null)  
		          {  
		            String key=method.substring(3);  
		            key=key.substring(0,1).toUpperCase()+key.substring(1);  
		            map.put(method.substring(3,method.length()).toLowerCase(), value);  
		          }  
		          }catch (Exception e) {  
		            // TODO: handle exception  
		            System.out.println("error:"+method);  
		          }  
		        }  
		      }  
		    }  
		    catch (Exception e)  
		    {  
		      // TODO: handle exception  
		      e.printStackTrace();  
		    }  
		    return map;  
		  } 
		/**
		 *  运行失信规则定时任务
		 */
		public static void runCmd(ObjectCtl daoCtl,Dao dao){
			Tasks tasks = daoCtl.detailByCnd(dao,Tasks.class,Cnd.where("is_rule","=",1));
			File root = new File(tasks.getFile_path());
			String findFilePath = "";
			if (root.exists() && root.isDirectory()) {
				for (File file : root.listFiles()) {
					if (file.isFile() && file.getName().indexOf(".jar") >-1) {
						findFilePath = file.getPath();//这里输出文件名！
					}
				}
			}
			System.out.println("FileName : "+findFilePath);
			Runtime rt = Runtime.getRuntime();
			boolean flag = false;
			try{
				Process ps = rt.exec("cmd.exe /c start java -jar " + findFilePath +"");
				if (ps.waitFor() != 0) {
					//p.exitValue()==0表示正常结束，1：非正常结束
					if (ps.exitValue() == 1){
						System.err.println("命令执行失败!"+ps.exitValue());
					}
				}else{
					System.out.println("命令执行成功!");
					flag = true;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
}
