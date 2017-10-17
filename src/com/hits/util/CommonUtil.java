package com.hits.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 *  #(c) IFlytek xmdd <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: 公用方法类
 * 
 *  <br/>创建说明:  L H T   创建文件<br/>
 * 
 *  修改历史:<br/>
 *
 */
public class CommonUtil {
	
	/**
	 * 功能描述:验证loginname 是否符合规则，验证基本条件是 4位 数字的登陆名称
	 *
	 * @author (☆_☆)  2015年5月28日 下午1:50:38
	 * 
	 * @param loginname
	 * 
	 * @return  true/false
	 */
	public static boolean checLoginnameIsHeFa(String loginname){
		String rex = "^\\d{1,4}$";
		Pattern p = Pattern.compile(rex);
		Matcher m = p.matcher(loginname);
		return m.find();
	}
	
	public static void main(String[] args) {
		System.out.println(checLoginnameIsHeFa("e8"));
	}
	
	
}
