package com.hits.util;

import java.util.List;

/**
 * 
 *  cspWeb
 *
 *  功能说明: 验证对象是否为空
 * 
 *  创建说明: 2014-5-20 上午10:36:02 houkun 
 * 
 *
 */
public class EmptyUtils {
	
	
	@SuppressWarnings("unchecked")
	public static boolean isEmpty(List list){
		return (list==null || list.size()==0);
	}
	
	
	@SuppressWarnings("unchecked")
	public static boolean isNotEmpty(List list){
		return (list!=null && list.size()>0);
	}
	
	
	public static boolean isEmpty(String str){
		return (str==null || "".equals(str));
	}
	
	
	public static boolean isNotEmpty(String str){
		return (str!=null && !str.equals(""));
	}
	
	
	public static boolean isNotEmpty(Object obj){
		return obj!=null;
	}
	
	
	public static boolean isEmpty(Object obj){
		return obj==null;
	}
	
	
	public static boolean isEmpty(Object[] strings){
		return (strings==null || strings.length==0);
	}
	
	
	public static boolean isNotEmpty(Integer num){
		return num!=null;
	}
	
	
	public static boolean isEmpty(Integer num){
		return num==null;
	}
	
	
	public static boolean isNotEmpty(Object[] strings){
		return (strings!=null && strings.length>0);
	}
	
	


}
