package com.hits.common.util;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 基础加密组件
 * 
 * @author mj
 * @version 1.0
 * @since 1.0
 */
public abstract class Coder {

	// 十六进制下数字到字符的映射数组
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 转换字节数组为十六进制字符串
	 * 
	 * @param 字节数组
	 * @return 十六进制字符串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/** 将一个字节转化成十六进制形式的字符串 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * MD5加密
	 * 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	private static String encodeByMD5(String originString) {
		if (originString != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				byte[] results = md.digest(originString.getBytes());
				String resultString = byteArrayToHexString(results);
				return resultString.toUpperCase();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
	}

	public static String getKey(String userName, String jobCode) {
		String results = "";
		String key = "!@#$%^&*";
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		String oldKey = userName + key + jobCode + ft.format(new Date());
		results = encodeByMD5(oldKey);
		return results;
	}
	
	/**
	 * 功能描述:生成加密字符串
	 *
	 * 
	 * @return
	 */
	public static String getKey() {
		String results = "";
		String key = "!@#$%^&*";
		SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
		String oldKey = key +ft.format(new Date());
		results = encodeByMD5(oldKey);
		return results;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getKey("superadmin", "340102000005"));
		System.out.println(getKey());
	}
}
