package com.hits.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

public class HtmlCodeUtil {
	/**
	 * 按给定的网址得到页面代码
	 *
	 * @param httpUrl
	 */
	public static String getHtmlCode(String httpUrl) {
		String htmlCode = "";
		try {
			httpUrl = getGBKFromISO(httpUrl);
			InputStream in;
			URL url = new java.net.URL(httpUrl);
			// System.out.println("url="+url.toString());
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/4.0");
			connection.connect();
			in = connection.getInputStream();
			java.io.BufferedReader breader = new BufferedReader(
					new InputStreamReader(in, "utf-8"));
			String currentLine;
			while ((currentLine = breader.readLine()) != null) {
				htmlCode += currentLine + "\n";
			}
			in.close();
			breader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlCode;
	}

	/**
	 * 设置返回页面的URL所有参数
	 *
	 * @param request
	 */
	public static String getParamUrl(HttpServletRequest request) {
		String urlparam = request.getRequestURI();
		Enumeration enu = request.getParameterNames();
		String paramenters = "";
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String query = name + "=" + request.getParameter(name);
			if (paramenters.equals(""))
				paramenters = query;
			else
				paramenters = paramenters + "&" + query;
		}
		if (paramenters.length() != 0) {
			urlparam = urlparam + "?" + paramenters;// "?randof=" +
													// String.valueOf(Math.random());
		}
		return urlparam;
	}

	/**
	 * 返回页面中的参数
	 *
	 * @param request
	 */
	public static String getParam(HttpServletRequest request) {
		Enumeration enu = request.getParameterNames();
		String paramenters = "";
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String query = name + "=" + request.getParameter(name);
			if (paramenters.equals(""))
				paramenters = query;
			else
				paramenters = paramenters + "&" + query;
		}
		return paramenters;
	}

	/**
	 * 将ISO字符转化为GBK字符
	 *
	 * @param str
	 */
	public static String getGBKFromISO(String str) {
		try {
			if (str == null)
				str = "";
			byte[] buf = str.getBytes("iso-8859-1");
			byte[] buf2 = str.getBytes("gbk");
			if (!str.equals(new String(buf2, "gbk"))) {
				str = new String(buf, "gbk");
			}

			return str;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}
}
