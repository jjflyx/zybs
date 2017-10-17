package com.hits.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hits.modules.com.comUtil;
import org.nutz.dao.Dao;
import org.nutz.mvc.Mvcs;

import com.hits.common.action.BaseAction;
import com.hits.modules.sys.bean.Sys_log;
import com.hits.modules.sys.bean.Sys_user;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * 
 *  #(c) IFlytek xmdd <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: 公用的log日志类
 * 
 *  <br/>创建说明: 2014-9-22 下午05:58:52 L H T   创建文件<br/>
 * 
 *  修改历史:<br/>
 *
 */
public class SysLogUtil extends BaseAction{

	
	//========日志类型=======================
	/**
	 * 登录日志 0
	 */
	public  static final int login_log_type=0;
	
	/**
	 * 用户操作日志 1
	 */
	public  static final int user_log_type=1;
	
	/**
	 * 系统运行日志 2 
	 */
	public  static final int yx_log_type=2;
	/**
	 *国土信用操作日志 3
	 */
	public  static final int gtyx_log_type=3;
	
	
	
	/**
	 * 功能描述:新增sys_user日志
	 *
	 * @author L H T  
	 * 
	 * @param request
	 * @param user 
	 * @param type 日志类型，在LogUtil中有定义
	 * @param note 操作内容
	 */
	public static boolean addLog(Dao dao,Sys_user user, Integer type,String note) {
		HttpServletRequest request=Mvcs.getReq();
		StackTraceElement[] s = new Exception().getStackTrace();
		System.out.println("LogUtil上级调用类："+s[1].getClassName() +"  --调用方法："+ s[1].getMethodName());
		if(EmptyUtils.isEmpty(user)){
			HttpSession session=Mvcs.getHttpSession();
			user= (Sys_user) session.getAttribute("userSession");
		}
		Sys_log log = new Sys_log();
		log.setLoginname(user.getLoginname());
		log.setType(type);
		log.setContent(note);
		log.setCreate_time(DateUtil.getCurDateTime());
		log.setLogin_ip(IpUtil.getIpAddr(request));
		log.setBowser(BrowserUtils.checkBrowse(request));
		log.setLetter_id("");
		log.setClass_name(s[1].getClassName());
		log.setMethod_name(s[1].getMethodName());
		
		Sys_log saveLog=dao.insert(log);
		if (EmptyUtils.isNotEmpty(saveLog)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 功能描述:新增sys_user日志
	 *
	 * @author L H T  
	 * 
	 * @param request
	 * @param user 
	 * @param type 日志类型，在LogUtil中有定义
	 * @param note 操作内容
	 * @param letterid 信件id
	 */
	public static boolean addLog(Dao dao,Sys_user user, Integer type,String note,String letterid) {
		HttpServletRequest request=Mvcs.getReq();
		StackTraceElement[] s = new Exception().getStackTrace();
		System.out.println("LogUtil上级调用类："+s[1].getClassName() +"  --调用方法："+ s[1].getMethodName());
		if(EmptyUtils.isEmpty(user)){
			HttpSession session=Mvcs.getHttpSession();
			user= (Sys_user) session.getAttribute("userSession");
		}
		Sys_log log = new Sys_log();
		log.setLoginname(user.getLoginname());
		log.setType(type);
		log.setContent(note);
		log.setCreate_time(DateUtil.getCurDateTime());
		log.setLogin_ip(IpUtil.getIpAddr(request));
		log.setBowser(BrowserUtils.checkBrowse(request));
		log.setLetter_id(letterid);
		log.setClass_name(s[1].getClassName());
		log.setMethod_name(s[1].getMethodName());
		
		Sys_log saveLog=dao.insert(log);
		if (EmptyUtils.isNotEmpty(saveLog)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 功能描述:新增sys_user日志
	 *
	 * @author L H T  
	 *
	 * @param loginname 手动指定用户名 
	 * @param type 日志类型，在LogUtil中有定义
	 * @param note 操作内容
	 */
	public static boolean addLog(Dao dao,String loginname, Integer type,String note) {
		HttpServletRequest request=Mvcs.getReq();
		StackTraceElement[] s = new Exception().getStackTrace();
		System.out.println("LogUtil上级调用类："+s[1].getClassName() +"  --调用方法："+ s[1].getMethodName());
		if(EmptyUtils.isEmpty(loginname)){
			HttpSession session=Mvcs.getHttpSession();
			loginname= ((Sys_user) session.getAttribute("userSession")).getLoginname();
		}
		Sys_log log = new Sys_log();
		log.setLoginname(loginname);
		log.setType(type);
		log.setContent(note);
		log.setCreate_time(DateUtil.getCurDateTime());
		log.setLogin_ip(IpUtil.getIpAddr(request));
		log.setBowser(BrowserUtils.checkBrowse(request));
		log.setLetter_id("");
		log.setClass_name(s[1].getClassName());
		log.setMethod_name(s[1].getMethodName());
		
		Sys_log saveLog=dao.insert(log);
		if (EmptyUtils.isNotEmpty(saveLog)) {
			return true;
		}
		return false;
	}

	/**
	 * 功能描述:新增国土信用系统用户操作日志
	 *
	 * @author Numbgui
	 *
	 * @param loginname 手动指定用户名
	 * @param type 日志类型，在LogUtil中有定义
	 * @param content 操作内容
	 * @param title 执行操作名
	 * @param note 审核内容
	 * @param reason 修改原因
	 * @param basis 修改依据
	 * @param flag 是否成功
	 * @param cz 1-新增,2-修改,3-删除,4-撤销,5-发布
	 */
	/*public static boolean addLog(Dao dao,String loginname, Integer type,boolean flag,Integer cz,String content,String title,String note,String reason,String basis) {
		HttpServletRequest request=Mvcs.getReq();
		StackTraceElement[] s = new Exception().getStackTrace();
		System.out.println("LogUtil上级调用类："+s[1].getClassName() +"  --调用方法："+ s[1].getMethodName());
		if(EmptyUtils.isEmpty(loginname)){
			HttpSession session=Mvcs.getHttpSession();
			loginname= ((Sys_user) session.getAttribute("userSession")).getLoginname();
		}
		Sys_log log = new Sys_log();
		log.setLoginname(loginname);
		log.setType(type);
		log.setContent(content);
		log.setCreate_time(DateUtil.getCurDateTime());
		log.setLogin_ip(IpUtil.getIpAddr(request));
		log.setBowser(BrowserUtils.checkBrowse(request));
		log.setLetter_id("");
		log.setClass_name(s[1].getClassName());
		log.setMethod_name(s[1].getMethodName());
		log.setTitle(title);
		log.setNote(note);
		log.setReason(reason);
		log.setBasis(basis);
		log.setSuccess(flag?"true":"false"); //操作是否成功
		log.setCz(comUtil.logCzMap.get(cz)); //操作类型


		Sys_log saveLog=dao.insert(log);
		if (EmptyUtils.isNotEmpty(saveLog)) {
			return true;
		}
		return false;
	}*/
	/**
	 * 功能描述:新增国土信用系统用户操作日志，带数据url
	 *
	 * @author Numbgui
	 *
	 * @param loginname 手动指定用户名
	 * @param type 日志类型，在LogUtil中有定义
	 * @param content 操作内容
	 * @param title 执行操作名
	 * @param note 审核内容
	 * @param reason 修改原因
	 * @param basis 修改依据
	 * @param flag 是否成功
	 * @param cz 1-新增,2-修改,3-删除,4-撤销,5-发布
	 */
	public static boolean addLog(Dao dao,String loginname, Integer type,boolean flag,Integer cz,String content,String title,String note,String reason,String basis,String url) {
		HttpServletRequest request=Mvcs.getReq();
		StackTraceElement[] s = new Exception().getStackTrace();
		System.out.println("LogUtil上级调用类："+s[1].getClassName() +"  --调用方法："+ s[1].getMethodName());
		if(EmptyUtils.isEmpty(loginname)){
			HttpSession session=Mvcs.getHttpSession();
			loginname= ((Sys_user) session.getAttribute("userSession")).getLoginname();
		}
		Sys_log log = new Sys_log();
		log.setLoginname(loginname);
		log.setType(type);
		log.setContent(content);
		log.setCreate_time(DateUtil.getCurDateTime());
		log.setLogin_ip(IpUtil.getIpAddr(request));
		log.setBowser(BrowserUtils.checkBrowse(request));
		log.setLetter_id("");
		log.setClass_name(s[1].getClassName());
		log.setMethod_name(s[1].getMethodName());
		log.setTitle(title);
		log.setNote(note);
		log.setReason(reason);
		log.setBasis(basis);
		log.setSuccess(flag?"true":"false"); //操作是否成功
		log.setCz(comUtil.logCzMap.get(cz)); //操作类型
		log.setUrl(url);


		Sys_log saveLog=dao.insert(log);
		if (EmptyUtils.isNotEmpty(saveLog)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 功能描述:新增国土信用系统用户操作日志，带数据url
	 *
	 * @author Numbgui
	 *
	 * @param loginname 手动指定用户名
	 * @param type 日志类型，在LogUtil中有定义
	 * @param content 操作内容
	 * @param title 执行操作名
	 * @param note 审核内容
	 * @param reason 修改原因
	 * @param basis 修改依据
	 * @param flag 是否成功
	 * @param cz 1-新增,2-修改,3-删除,4-撤销,5-发布
	 */
	public static boolean addLogxg(Dao dao,String loginname, Integer type,boolean flag,Integer cz,String content,String title,String note,String reason,String basis,String url,String fileid) {
		HttpServletRequest request=Mvcs.getReq();
		StackTraceElement[] s = new Exception().getStackTrace();
		System.out.println("LogUtil上级调用类："+s[1].getClassName() +"  --调用方法："+ s[1].getMethodName());
		if(EmptyUtils.isEmpty(loginname)){
			HttpSession session=Mvcs.getHttpSession();
			loginname= ((Sys_user) session.getAttribute("userSession")).getLoginname();
		}
		Sys_log log = new Sys_log();
		log.setLoginname(loginname);
		log.setType(type);
		log.setContent(content);
		log.setCreate_time(DateUtil.getCurDateTime());
		log.setLogin_ip(IpUtil.getIpAddr(request));
		log.setBowser(BrowserUtils.checkBrowse(request));
		log.setLetter_id("");
		log.setClass_name(s[1].getClassName());
		log.setMethod_name(s[1].getMethodName());
		log.setTitle(title);
		log.setNote(note);
		log.setReason(reason);
		log.setBasis(basis);
		log.setSuccess(flag?"true":"false"); //操作是否成功
		log.setCz(comUtil.logCzMap.get(cz)); //操作类型
		log.setUrl(url);
		log.setXgfj(fileid);


		Sys_log saveLog=dao.insert(log);
		if (EmptyUtils.isNotEmpty(saveLog)) {
			return true;
		}
		return false;
	}
	
	public static String getLogNote(Object oldObj, Object newObj,String tablename)
	{
		String msgAll= "";
		try
		{
			// 获取实体类的所有属性，返回Field数组
			Field[] fieldNew = newObj.getClass().getDeclaredFields();
			// 获取属性的名字
			String[] modelNameNew = new String[fieldNew.length];
			String[] modelTypeNew = new String[fieldNew.length];
			
			Field[] fieldOld = oldObj.getClass().getDeclaredFields();
			// 获取属性的名字
			String[] modelNameOld = new String[fieldOld.length];
			String[] modelTypeOld = new String[fieldOld.length];
			String msg = "";
			Hashtable<String, String> Name_cnMp = comUtil.GgbNameMp.get(tablename);
			for (int i = 0; i < fieldNew.length; i++)
			{
				String name_Cn = "";//字段中文名
				msg = "";
				// 获取属性的名字
				String nameNew = fieldNew[i].getName();
				
				modelNameNew[i] = nameNew;
				// 获取属性类型
				String typeNew = fieldNew[i].getGenericType().toString();
				modelTypeNew[i] = typeNew;
				
				// 获取属性的名字
				String nameOld = fieldOld[i].getName();
				modelNameOld[i] = nameOld;
				// 获取属性类型
				String typeOld = fieldOld[i].getGenericType().toString();
				modelTypeOld[i] = typeOld;

				// 关键。。。可访问私有变量
				fieldNew[i].setAccessible(true);
				// 关键。。。可访问私有变量
				fieldOld[i].setAccessible(true);

				name_Cn = Name_cnMp.get(nameNew);
				// 将属性的首字母大写
				nameNew = nameNew.replaceFirst(nameNew.substring(0, 1), nameNew.substring(0, 1).toUpperCase());
				nameOld = nameOld.replaceFirst(nameOld.substring(0, 1), nameOld.substring(0, 1).toUpperCase());
				
				// 如果type是类类型，则前面包含"class "，后面跟类名
				Method m_N = newObj.getClass().getMethod("get" + nameNew);
				Method m_O = newObj.getClass().getMethod("get" + nameOld);
				// 调用getter方法获取属性值
				Object value_N = (Object) m_N.invoke(newObj);
				Object value_O = (Object) m_O.invoke(oldObj);
				if(value_N!=null&&value_O!=null){
					System.out.println("value_N==="+value_N+",value_O===="+value_O);
					if(EmptyUtils.isNotEmpty(name_Cn)&&!(value_N.toString()).equals(value_O.toString()))
					{
						System.out.println("=======");
						msg += "[<font color=blue>" + name_Cn + "</font>]字段原值："+value_O+"，新值："+value_N+"</br>";
					}
				}
				msgAll += msg;
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgAll;

	}
	public static String getLogNote(Object newObj,String tablename)
	{
		String msgAll= "";
		try
		{
			// 获取实体类的所有属性，返回Field数组
			Field[] fieldNew = newObj.getClass().getDeclaredFields();
			// 获取属性的名字
			String[] modelNameNew = new String[fieldNew.length];
			String[] modelTypeNew = new String[fieldNew.length];
			
			String msg = "";
			Hashtable<String, String> Name_cnMp = comUtil.GgbNameMp.get(tablename);
			for (int i = 0; i < fieldNew.length; i++)
			{
				String name_Cn = "";//字段中文名
				msg = "";
				// 获取属性的名字
				String nameNew = fieldNew[i].getName();
				
				modelNameNew[i] = nameNew;
				// 获取属性类型
				String typeNew = fieldNew[i].getGenericType().toString();
				modelTypeNew[i] = typeNew;
				// 关键。。。可访问私有变量
				fieldNew[i].setAccessible(true);

				name_Cn = Name_cnMp.get(nameNew);
				// 将属性的首字母大写
				nameNew = nameNew.replaceFirst(nameNew.substring(0, 1), nameNew.substring(0, 1).toUpperCase());
				
				// 如果type是类类型，则前面包含"class "，后面跟类名
				Method m_N = newObj.getClass().getMethod("get" + nameNew);
				// 调用getter方法获取属性值
				Object value_N = (Object) m_N.invoke(newObj);
				if(EmptyUtils.isNotEmpty(name_Cn)&&EmptyUtils.isNotEmpty(value_N)){
					msg += "[<font color=blue>" + name_Cn + "</font>]字段值："+value_N+"</br>";
				}
				msgAll += msg;
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgAll;

	}
	public static String getLogNoteForm(Map oldObj, Map newObj,String tablename)
	{
		String msgAll= "";
		try
		{
			Hashtable<String, String> Name_cnMp = comUtil.GgbNameMp.get(tablename);
			Set<String> fieldNew=newObj.keySet();
			for (String name : fieldNew) {
				String name_Cn = Name_cnMp.get(name);//字段中文名
				String value_N = StringUtil.null2String(newObj.get(name));
				String value_O = StringUtil.null2String(oldObj.get(name));
				System.out.println(name_Cn + "，新值==="+value_N+",旧值===="+value_O);
				if(EmptyUtils.isNotEmpty(name_Cn)&&EmptyUtils.isNotEmpty(value_N)&&!value_N.equals(value_O)){
					msgAll += "[<font color=blue>" + name_Cn + "</font>]字段原值："+value_O+"，新值："+value_N+"</br>";
				}
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgAll;

	}
	public static String getLogNoteForm(List<Map> newObj,String tablename,String type)
	{
		String msgAll= "";
		String msg= "添加本条数据</br>";
		try
		{
			if("delete".equals(type)){
				msg= "删除本条数据</br>";
			}
			Hashtable<String, String> Name_cnMp = comUtil.GgbNameMp.get(tablename);
			for (Map map : newObj) {
				msgAll +=msg;
				Set<String> fieldNew=map.keySet();
				for (String name : fieldNew) {
					String name_Cn = Name_cnMp.get(name);//字段中文名
					String value_N = StringUtil.null2String(map.get(name));
					if(EmptyUtils.isNotEmpty(name_Cn)&&!"".equals(value_N))
					{
						msgAll += "[<font color=blue>" + name_Cn + "</font>]字段值："+value_N+"</br>";
					}
				}
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgAll;

	}
	public static String getLogNoteForm(Map newObj,String tablename,String type)
	{
		String msgAll= "添加本条数据</br>";
		try
		{
			if("delete".equals(type)){
				msgAll= "删除本条数据</br>";
			}
			Hashtable<String, String> Name_cnMp = comUtil.GgbNameMp.get(tablename);
			Set<String> fieldNew=newObj.keySet();
			for (String name : fieldNew) {
				String name_Cn = Name_cnMp.get(name);//字段中文名
				String value_N = StringUtil.null2String(newObj.get(name));
				if(EmptyUtils.isNotEmpty(name_Cn)&&!"".equals(value_N))
				{
					if(name_Cn.endsWith("ID")){
						msgAll += "<div style=\"display:none;\">[<font color=blue>" + name_Cn + "</font>]字段值："+value_N+"</div>";
					}else{
						msgAll += "[<font color=blue>" + name_Cn + "</font>]字段值："+value_N+"</br>";
					}
				}
			}
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return msgAll;

	}
}
