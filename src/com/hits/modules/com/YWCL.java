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
