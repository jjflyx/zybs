package com.hits.modules.com;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import org.nutz.dao.Dao;

import com.hits.common.action.BaseAction;
import com.hits.modules.bean.Sms_log;
import com.hits.modules.bean.Sms_pjlog;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.shield.ShieldMsg;
import com.hits.util.DateUtil;
import com.hits.util.EmptyUtils;
import com.hits.util.HtmlCodeUtil;
import com.hits.util.HtmlText;


/**
 * 
 *  #(c) IFlytek 12345_new <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: 短信接口类
 * 
 *  <br/>创建说明: 2015年6月15日 下午6:14:15 (☆_☆)  创建文件<br/>
 * 
 *  修改历史:<br/>
 *
 */
public class MsgUtil extends BaseAction{
	
	/**
	 * 功能描述:
	 *
	 * @author (☆_☆)  2015年6月16日 下午5:29:46
	 * 
	 * @param dao
	 * @param letterid 信件编号
	 * @param loginname 登陆名
	 * @param tztype 短信发送类型
	 * @param phone 电话号码
	 * @param note 短信内容
	 * @return
	 */
	public static synchronized boolean SendMsg1(Dao dao,String letterid,String loginname,String tztype,String phone,String note,int type) {
		String url=comUtil.masUrl.replace("EPID", comUtil.masEpid).replace("USERNAME", comUtil.masUsername).replace("PASSWORD", comUtil.masPassword);
		boolean rs = false;
		note=HtmlText.Html2Text(note);
		if (EmptyUtils.isNotEmpty(tztype)) {
			String[] tztypear=tztype.split(",");
			for (int i = 0; i < tztypear.length; i++) {
				if (tztypear[i].equals("1")) {//发送短信
					String rex = "^1(3|4|5|7|8)[0-9]{1}\\d{8}$";
					Pattern p = Pattern.compile(rex);
					Matcher matcher = p.matcher(phone);
					boolean phonereg = matcher.find();
					System.out.println("电话:"+ phone);
					if (phonereg) {
						url=url.replace("PHONE", phone).replace("CONTENT", note);
						System.out.println("########" + url);
						String rsstr = HtmlCodeUtil.getHtmlCode(url);
						System.out.println("rsstr:" + rsstr);
						 rs=checkSendResult(dao,rsstr,letterid,loginname,phone,note,type);
					}
				}
			}
		}
		
		return rs;
	}
	
	/**
	 * 功能描述:
	 *
	 * @author (☆_☆)  2015年6月16日 下午5:29:46
	 * 
	 * @param dao
	 * @param letterid 信件编号
	 * @param loginname 登陆名
	 * @param tztype 短信发送类型
	 * @param unitid 单位id
	 * @param note 短信内容
	 * @return
	 */
	public static synchronized boolean SendMsg(Dao dao,String letterid,String loginname,String tztype,String unitid,String note) {
		String url=comUtil.masUrl.replace("EPID", comUtil.masEpid).replace("USERNAME", comUtil.masUsername).replace("PASSWORD", comUtil.masPassword);
		boolean rs = false;
		note=HtmlText.Html2Text(note);
		if (EmptyUtils.isNotEmpty(tztype)) {
			String[] tztypear=tztype.split(",");
			for (int i = 0; i < tztypear.length; i++) {
				if (tztypear[i].equals("1")) {//发送短信
					Sys_unit unit=dao.fetch(Sys_unit.class, unitid);
					String cbsj=unit.getHandlerphone();
					// 正则表达式验证是否是手机号码
					String rex = "^1(3|4|5|7|8)[0-9]{1}\\d{8}$";
					Pattern p = Pattern.compile(rex);
					Matcher matcher = p.matcher(cbsj);
					boolean phonereg = matcher.find();
					System.out.println("转办单位电话:"+ cbsj);
					if (phonereg) {
						url=url.replace("PHONE", cbsj).replace("CONTENT", note);
						System.out.println("########" + url);
						String rsstr = HtmlCodeUtil.getHtmlCode(url);
						System.out.println("rsstr:" + rsstr);
						 rs=checkSendResult(dao,rsstr,letterid,loginname,cbsj,note);
					}

				}
			}
		}
		return rs;
	}
	
	/**
	 * 功能描述:发送短信消息-phone数组形式
	 *
	 * @author (☆_☆)  2015年6月16日 上午10:35:24
	 * @return
	 */
	public static synchronized boolean SendMsg(Dao dao,String letterid,String loginname,String[] phone,String note) {
		String url=comUtil.masUrl.replace("EPID", comUtil.masEpid).replace("USERNAME", comUtil.masUsername).replace("PASSWORD", comUtil.masPassword);
		boolean rs = false;
		note=HtmlText.Html2Text(note);
		for (int i = 0; i < phone.length; i++) {
			url=url.replace("PHONE", phone[i]).replace("CONTENT", note);
			System.out.println("########" + url);
			String rsstr = HtmlCodeUtil.getHtmlCode(url);
			System.out.println("rsstr:" + rsstr);
			
			 rs=checkSendResult(dao,rsstr,letterid,loginname,phone[i],note);
		}
		return rs;
	}
	
	/**
	 * 功能描述:发送短信消息-普通形式电信发送
	 *
	 * @author (☆_☆)  2015年6月16日 上午10:35:52
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static synchronized boolean SendMsg(Dao dao,String letterid,String loginname, String phone,String note) throws UnsupportedEncodingException{
		
		String url=comUtil.masUrl.replace("EPID", comUtil.masEpid).replace("USERNAME", comUtil.masUsername).replace("PASSWORD", comUtil.masPassword);
		
		boolean rs=false;
        note=HtmlText.Html2Text(note);
        note=ShieldMsg.replaceStr(note);
        List<String> res=splitString(note, 210);
        for (String newnote:res) {
        	String sendnote= URLEncoder.encode(newnote, "utf-8");  
            url=url.replace("PHONE", phone).replace("CONTENT", sendnote);
            System.out.println("########"+url);
            String rsstr=HtmlCodeUtil.getHtmlCode(url);
            System.out.println("rsstr:"+rsstr);
            
            rs=checkSendResult(dao,rsstr,letterid,loginname,phone,note);
        }
        return rs;
    }
	
	/**
	 * 功能描述:检查短信发送结果是否成功
	 *
	 * @author (☆_☆)  2015年6月16日 下午5:51:31
	 * 
	 * @param resultCode 发送返回结果字符串
	 * @param letterid 信件编号
	 * @param loginname 登陆人
	 * @param phone 电话号码
	 * @param note 内容
	 * @return
	 */
	private static boolean checkSendResult(Dao dao,String resultCode,String letterid,String loginname,String phone,String note){
		boolean rs=false;
		// 正则表达式验证获取短信发送结果
		String rex = "(<string .*>)(.*)(</string>)";
		Pattern p = Pattern.compile(rex);
		Matcher m = p.matcher(resultCode);
		boolean result = m.find();
		String error_content = "";
		int send_result = 2;// 短信发送结果 1-发送成功；2-发送失败
		if (result) {
			String resultStr = m.group(2);
			error_content = getResultStr(resultStr);
			System.out.println("短信发送结果：" + resultStr);
			if (resultStr.equals("00")) {// 00代表发送成功
				rs = true;
				send_result = 1;
			}
		}
		
		// //保存用户发出的短信
		Sms_log sms=new Sms_log();
		sms.setMosid(letterid);
		sms.setLoginname(loginname);
		sms.setPhone(phone);
		sms.setTime(DateUtil.getCurDateTime());
		sms.setContent(note);
		sms.setSend_result(send_result);
		sms.setType(2);
		sms.setError_content(error_content);
		dao.insert(sms);
		return rs;
	}
	/**
	 * 功能描述:检查短信发送结果是否成功
	 *
	 * @author (☆_☆)  2015年6月16日 下午5:51:31
	 * 
	 * @param resultCode 发送返回结果字符串
	 * @param letterid 信件编号
	 * @param loginname 登陆人
	 * @param phone 电话号码
	 * @param note 内容
	 * @param type 1:办理结果通知
	 * @return
	 */
	private static boolean checkSendResult(Dao dao,String resultCode,String letterid,String loginname,String phone,String note,int type){
		boolean rs=false;
		// 正则表达式验证获取短信发送结果
		String rex = "(<string .*>)(.*)(</string>)";
		Pattern p = Pattern.compile(rex);
		Matcher m = p.matcher(resultCode);
		boolean result = m.find();
		String error_content = "";
		int send_result = 2;// 短信发送结果 1-发送成功；2-发送失败
		if (result) {
			String resultStr = m.group(2);
			error_content = getResultStr(resultStr);
			System.out.println("短信发送结果：" + resultStr);
			if (resultStr.equals("00")) {// 00代表发送成功
				rs = true;
				send_result = 1;
			}
		}
		
		// //保存用户发出的短信
		Sms_log sms=new Sms_log();
		sms.setMosid(letterid);
		sms.setLoginname(loginname);
		sms.setPhone(phone);
		sms.setTime(DateUtil.getCurDateTime());
		sms.setContent(note);
		sms.setSend_result(send_result);
		sms.setType(2);
		sms.setError_content(error_content);
		dao.insert(sms);
		if(type==1){//直办或转办办结短信通知时，向sms_pjlog添加纪录，便于评价匹配
			Sms_pjlog pjlog=new Sms_pjlog();
			pjlog.setFsid(sms.getId());
			pjlog.setPhone(phone);
			pjlog.setSend_time(sms.getTime());
			pjlog.setLetterid(sms.getMosid());
			dao.insert(pjlog);
		}
		return rs;
	}
	
	 /**
     * 功能描述:根据长度截取字符串，并保存
     *
     * @author (☆_☆)  2014-12-29 下午05:06:44
     * 
     * @param str
     * @param splitLength
     * @return
     */
    public static List<String> splitString(String str,int splitLength){
    	List<String> ret=new ArrayList<String>();
    	
		while(splitLength <str.length()){
			ret.add(str.substring(0, splitLength));
			str = str.substring(splitLength, str.length());
		}
		if (splitLength >=str.length()) {
			ret.add(str);
		}
    	return ret;

    }
    
    /**
     * 功能描述:根据短信发送返回标志获得说明
     *
     * @author (☆_☆)  2015-1-4 下午02:04:23
     * 
     * @param result
     * @return
     */
    public static String getResultStr(String result){
    	String str="发送失败";
    	if (EmptyUtils.isNotEmpty(result)) {
			if (result.equals("00")) {
				str="发送成功";
			}else if (result.equals("01")){
				str="号码（超过上限50个）、内容等为空或内容长度超过210";
			}else if (result.equals("02")){
				str="用户鉴权失败";
			}else if (result.equals("03")){
				str="登录IP黑名单";
			}else if (result.equals("10")){
				str="余额不足（仅针对公免账户）";
			}else if (result.equals("99")){
				str="服务器接受失败";
			}else{
				str="内容中有屏蔽字："+result;
			}
		}
    	return str;
    }

}
