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
 *  �汾˵��: $id:$ <br/>
 *
 *  ����˵��: ���Žӿ���
 * 
 *  <br/>����˵��: 2015��6��15�� ����6:14:15 (��_��)  �����ļ�<br/>
 * 
 *  �޸���ʷ:<br/>
 *
 */
public class MsgUtil extends BaseAction{
	
	/**
	 * ��������:
	 *
	 * @author (��_��)  2015��6��16�� ����5:29:46
	 * 
	 * @param dao
	 * @param letterid �ż����
	 * @param loginname ��½��
	 * @param tztype ���ŷ�������
	 * @param phone �绰����
	 * @param note ��������
	 * @return
	 */
	public static synchronized boolean SendMsg1(Dao dao,String letterid,String loginname,String tztype,String phone,String note,int type) {
		String url=comUtil.masUrl.replace("EPID", comUtil.masEpid).replace("USERNAME", comUtil.masUsername).replace("PASSWORD", comUtil.masPassword);
		boolean rs = false;
		note=HtmlText.Html2Text(note);
		if (EmptyUtils.isNotEmpty(tztype)) {
			String[] tztypear=tztype.split(",");
			for (int i = 0; i < tztypear.length; i++) {
				if (tztypear[i].equals("1")) {//���Ͷ���
					String rex = "^1(3|4|5|7|8)[0-9]{1}\\d{8}$";
					Pattern p = Pattern.compile(rex);
					Matcher matcher = p.matcher(phone);
					boolean phonereg = matcher.find();
					System.out.println("�绰:"+ phone);
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
	 * ��������:
	 *
	 * @author (��_��)  2015��6��16�� ����5:29:46
	 * 
	 * @param dao
	 * @param letterid �ż����
	 * @param loginname ��½��
	 * @param tztype ���ŷ�������
	 * @param unitid ��λid
	 * @param note ��������
	 * @return
	 */
	public static synchronized boolean SendMsg(Dao dao,String letterid,String loginname,String tztype,String unitid,String note) {
		String url=comUtil.masUrl.replace("EPID", comUtil.masEpid).replace("USERNAME", comUtil.masUsername).replace("PASSWORD", comUtil.masPassword);
		boolean rs = false;
		note=HtmlText.Html2Text(note);
		if (EmptyUtils.isNotEmpty(tztype)) {
			String[] tztypear=tztype.split(",");
			for (int i = 0; i < tztypear.length; i++) {
				if (tztypear[i].equals("1")) {//���Ͷ���
					Sys_unit unit=dao.fetch(Sys_unit.class, unitid);
					String cbsj=unit.getHandlerphone();
					// ������ʽ��֤�Ƿ����ֻ�����
					String rex = "^1(3|4|5|7|8)[0-9]{1}\\d{8}$";
					Pattern p = Pattern.compile(rex);
					Matcher matcher = p.matcher(cbsj);
					boolean phonereg = matcher.find();
					System.out.println("ת�쵥λ�绰:"+ cbsj);
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
	 * ��������:���Ͷ�����Ϣ-phone������ʽ
	 *
	 * @author (��_��)  2015��6��16�� ����10:35:24
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
	 * ��������:���Ͷ�����Ϣ-��ͨ��ʽ���ŷ���
	 *
	 * @author (��_��)  2015��6��16�� ����10:35:52
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
	 * ��������:�����ŷ��ͽ���Ƿ�ɹ�
	 *
	 * @author (��_��)  2015��6��16�� ����5:51:31
	 * 
	 * @param resultCode ���ͷ��ؽ���ַ���
	 * @param letterid �ż����
	 * @param loginname ��½��
	 * @param phone �绰����
	 * @param note ����
	 * @return
	 */
	private static boolean checkSendResult(Dao dao,String resultCode,String letterid,String loginname,String phone,String note){
		boolean rs=false;
		// ������ʽ��֤��ȡ���ŷ��ͽ��
		String rex = "(<string .*>)(.*)(</string>)";
		Pattern p = Pattern.compile(rex);
		Matcher m = p.matcher(resultCode);
		boolean result = m.find();
		String error_content = "";
		int send_result = 2;// ���ŷ��ͽ�� 1-���ͳɹ���2-����ʧ��
		if (result) {
			String resultStr = m.group(2);
			error_content = getResultStr(resultStr);
			System.out.println("���ŷ��ͽ����" + resultStr);
			if (resultStr.equals("00")) {// 00�����ͳɹ�
				rs = true;
				send_result = 1;
			}
		}
		
		// //�����û������Ķ���
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
	 * ��������:�����ŷ��ͽ���Ƿ�ɹ�
	 *
	 * @author (��_��)  2015��6��16�� ����5:51:31
	 * 
	 * @param resultCode ���ͷ��ؽ���ַ���
	 * @param letterid �ż����
	 * @param loginname ��½��
	 * @param phone �绰����
	 * @param note ����
	 * @param type 1:������֪ͨ
	 * @return
	 */
	private static boolean checkSendResult(Dao dao,String resultCode,String letterid,String loginname,String phone,String note,int type){
		boolean rs=false;
		// ������ʽ��֤��ȡ���ŷ��ͽ��
		String rex = "(<string .*>)(.*)(</string>)";
		Pattern p = Pattern.compile(rex);
		Matcher m = p.matcher(resultCode);
		boolean result = m.find();
		String error_content = "";
		int send_result = 2;// ���ŷ��ͽ�� 1-���ͳɹ���2-����ʧ��
		if (result) {
			String resultStr = m.group(2);
			error_content = getResultStr(resultStr);
			System.out.println("���ŷ��ͽ����" + resultStr);
			if (resultStr.equals("00")) {// 00�����ͳɹ�
				rs = true;
				send_result = 1;
			}
		}
		
		// //�����û������Ķ���
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
		if(type==1){//ֱ���ת�������֪ͨʱ����sms_pjlog��Ӽ�¼����������ƥ��
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
     * ��������:���ݳ��Ƚ�ȡ�ַ�����������
     *
     * @author (��_��)  2014-12-29 ����05:06:44
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
     * ��������:���ݶ��ŷ��ͷ��ر�־���˵��
     *
     * @author (��_��)  2015-1-4 ����02:04:23
     * 
     * @param result
     * @return
     */
    public static String getResultStr(String result){
    	String str="����ʧ��";
    	if (EmptyUtils.isNotEmpty(result)) {
			if (result.equals("00")) {
				str="���ͳɹ�";
			}else if (result.equals("01")){
				str="���루��������50���������ݵ�Ϊ�ջ����ݳ��ȳ���210";
			}else if (result.equals("02")){
				str="�û���Ȩʧ��";
			}else if (result.equals("03")){
				str="��¼IP������";
			}else if (result.equals("10")){
				str="���㣨����Թ����˻���";
			}else if (result.equals("99")){
				str="����������ʧ��";
			}else{
				str="�������������֣�"+result;
			}
		}
    	return str;
    }

}
