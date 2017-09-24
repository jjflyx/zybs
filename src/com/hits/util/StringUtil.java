package com.hits.util;

import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.beanutils.BeanUtilsBean;

/**
 * 
 * #(c) IFlytek 12345_new <br/>
 *
 * 版本说明: $id:$ <br/>
 *
 * 功能说明: 字符串操作类
 * 
 * <br/>
 * 创建说明: 2015年5月27日 下午4:57:39 (☆_☆) 创建文件<br/>
 * 
 * 修改历史:<br/>
 *
 */
public class StringUtil {

	public static boolean blnTextBox = true;

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


	/***
	 * 将列表转换为字符串输出
	 * 
	 * @param arr
	 * @param split
	 */
	public static String getStrByArray(ArrayList arr, String split) {
		String outstr = "";
		for (int i = 0; i < arr.size(); i++) {
			outstr += arr.get(i) + split;
		}
		if (outstr.length() > 0) {
			outstr = outstr.substring(0, outstr.length() - 1);
		}
		return outstr;
	}

	/**
	 * 将GBK字符转化为ISO字符
	 *
	 * @param str
	 */
	public static String getISOFromGBK(String str) {
		try {
			if (str == null)
				str = "";
			byte[] buf = str.getBytes("gbk");
			byte[] buf2 = str.getBytes("iso-8859-1");
			if (!str.equals(new String(buf2, "iso-8859-1"))) {
				str = new String(buf, "iso-8859-1");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 将字符串handleStr中以pointStr以分隔的每个字符串存放在向量中返回
	 *
	 * @param handleStr
	 * @param pointStr
	 */
	public static Vector explode(String handleStr, String pointStr) {
		Vector v = new Vector();
		int pos1, pos2;
		try {
			if (handleStr.length() > 0) {
				pos1 = handleStr.indexOf(pointStr);
				pos2 = 0;
				while (pos1 != -1) {
					v.addElement(handleStr.substring(pos2, pos1));
					pos2 = pos1 + pointStr.length();
					pos1 = handleStr.indexOf(pointStr, pos2);
				}
				v.addElement(handleStr.substring(pos2));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}

	/**
	 * 在字符串handleStr中的字符串pointStr以repStr代替
	 *
	 * @param handleStr
	 * @param pointStr
	 * @param repStr
	 */
	public static String replace(String handleStr, String pointStr,
			String repStr) {
		String str = new String();
		int pos1, pos2;
		try {
			if (handleStr.length() > 0) {
				pos1 = handleStr.indexOf(pointStr);
				pos2 = 0;
				while (pos1 != -1) {
					str += handleStr.substring(pos2, pos1);
					str += repStr;
					pos2 = pos1 + pointStr.length();
					pos1 = handleStr.indexOf(pointStr, pos2);
				}
				str += handleStr.substring(pos2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	

	/**
	 * 将字符串转换为html字符串
	 *
	 * @param handleStr
	 */
	public static String htmlSpecialChars(String handleStr) {
		return htmlSpecialChars(handleStr, true);
	}

	/**
	 * html字符串和字符串之间的互换。当seq为true时转换到html字符串
	 *
	 * @param handleStr
	 * @param seq
	 */
	public static String htmlSpecialChars(String handleStr, boolean seq) {
		String str = handleStr;

		if (seq) {
			str = replace(str, "&", "&amp;");
			str = replace(str, "\"", "&quot;");
			str = replace(str, "<", "&lt;");
			str = replace(str, ">", "&gt;");
		} else {
			str = replace(str, "&amp;", "&");
			str = replace(str, "&quot;", "\"");
			str = replace(str, "&lt;", "<");
			str = replace(str, "&gt;", ">");
		}

		if (!blnTextBox)
			if (seq)
				str = replace(str, "\n", "<br>");
			else
				str = replace(str, "<br>", "\n");

		return str;
	}

	/**
	 * 将字符串中的"\n"以"<br>
	 * &nbsp;&nbsp;" 替换后返回
	 *
	 * @param handleStr
	 */
	public static String returnChar2BR(String handleStr) {
		String str = handleStr;
		str = replace(str, "\n", "<br>&nbsp;&nbsp;");
		return str;
	}

	/**
	 * 将字符串中的"\n"以"<br>
	 * &nbsp;&nbsp;" 替换后返回
	 *
	 * @param handleStr
	 */
	public static String returnChar2BRno(String handleStr) {
		String str = handleStr;
		if (str != null) {
			str = replace(str, "\n", "<br>");
		} else {
			str = "";
		}
		return str;
	}

	/**
	 * 将字符串中的"\n"以"<br>
	 * &nbsp;&nbsp;" 替换后返回
	 *
	 * @param handleStr
	 */
	public static String returnChar2BRno2(String handleStr) {
		String str = handleStr;
		if (str != null && !str.equals("null") && !str.equals("NULL")) {
			str.trim();
			str = replace(str, "\n", "<br>");
		} else {
			str = "";
		}
		// System.out.println("str="+str);
		if (str.indexOf("　　") == 0 || str.indexOf("&nbsp;") == 0)
			return str;
		return "&nbsp;&nbsp;&nbsp;&nbsp;" + str;
	}

	/**
	 * 将handler中的内容取出转换为字符串并将其每个以separator分割开后返回。
	 *
	 * @param handler
	 * @param separator
	 */
	public static String implode(Vector handler, String separator) {
		StringBuffer strbuf = new StringBuffer();
		try {
			if (!handler.isEmpty()) {
				int len = handler.size();
				for (int loopi = 0; loopi < len; loopi++) {
					strbuf.append((String) handler.get(loopi));
					if (loopi != len - 1)
						strbuf.append(separator);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strbuf.toString();
	}

	/**
	 * Return appointed String from a String Vector param1: String Vector
	 * param2: appointed Index param3: include Excel CSV process.
	 */
	public static String getField(Vector vt, int i, boolean isExcel) {
		String str = "";
		try {
			str = (String) vt.get(i);
			if (str != null && str.length() > 2 && isExcel) {
				if (str.substring(0, 1).compareTo("\"") == 0) {
					str = str.substring(1, str.length() - 1);
					str = StringUtil.replace(str, "\"\"", "\"");
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			return "";
		}
		return str;
	}

	/**
	 * 当字符串长度小于Len时，有字符InsChar填冲到str的左边或右边，当intDirect为0时为左，1时为右 param1: father
	 * string param2: need fill in char param3: 0 is left fill in 1 is right
	 * fill in param4: total string length after fill in char
	 */
	public static String insStr(String str, String InsChar, int intDirect,
			int Len) {
		int intLen = str.length();
		StringBuffer strBuffer = new StringBuffer(str);

		if (intLen < Len) {
			int inttmpLen = Len - intLen;
			for (int i = 0; i < inttmpLen; i++) {
				if (intDirect == 1) {
					str = str.concat(InsChar);
				} else if (intDirect == 0) {
					strBuffer.insert(0, InsChar);
					str = strBuffer.toString();
				}
			}
		}
		return str;
	}

	/**
	 * 返回在字符串str中，首次出现字符串divided的位置。若没有找到返回-1
	 *
	 * @param str
	 * @param divided
	 */
	public static int searchDiv(String str, String divided) {
		try {
			divided = divided.trim();
			int divpos = -1;

			if (str.length() > 0) {
				divpos = str.indexOf(divided);

				return divpos;
			} else
				return divpos;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * 在字符串str中取首次出现startdiv到首次出现enddiv之间的字符串并返回，如果没有找到返回“”
	 *
	 * @param str
	 * @param startdiv
	 * @param enddiv
	 */
	public static String extractStr(String str, String startdiv, String enddiv) {
		int startdivlen = startdiv.length();
		str = str.trim();

		int startpos = -1;
		int endpos = -1;

		startdiv = startdiv.trim();
		enddiv = enddiv.trim();
		startpos = searchDiv(str, startdiv);
		if (str.length() > 0) {
			if (startpos >= 0) {
				str = str.substring(startpos + startdivlen);
				str = str.trim();
			}
			endpos = searchDiv(str, enddiv);
			if (endpos == -1)
				return "";
			str = str.substring(0, endpos);
			str = str.trim();
		}
		return str;
	}

	/**
	 * 返回一个不为空的字符串
	 *
	 * @param str
	 */
	public static String isNull(String str) {
		return isNull(str, "&nbsp;");
	}

	/**
	 * 返回一个不为空的字符串，当为空时返回def
	 *
	 * @param str
	 * @param def
	 */
	public static String isNull(String str, String def) {
		if (str == null)
			return def;
		else if (str.length() == 0)
			return def;
		else
			return str;
	}

	/**
	 * 将字符串类型转换为整数类型
	 *
	 * @param str
	 */
	public static int StringToInt(String str) {
		return StringToInt(str, 0);
	}

	/**
	 * 将字符串类型转换为整数类型，出错时有def值返回
	 *
	 * @param str
	 * @param def
	 */
	public static int StringToInt(String str, int def) {
		int intRet = def;
		try {
			if (str == null || str.trim().equals(""))
				str = def + "";
			intRet = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return intRet;
	}

	/**
	 * 将字符串类型转换为浮点类型
	 *
	 * @param str
	 */
	public static float StringToFloat(String str) {
		return StringToFloat(str, 0);
	}

	/**
	 * 将字符串类型转换为浮点类型，出错时有def值返回
	 *
	 * @param str
	 * @param def
	 */
	public static float StringToFloat(String str, float def) {
		float fRet = def;
		try {
			if (str == null || str.trim().equals(""))
				str = def + "";
			fRet = Float.parseFloat(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return fRet;
	}

	/**
	 * 将字符串类型转换为双精度类型
	 *
	 * @param str
	 */
	public static double StringToDouble(String str) {
		return StringToDouble(str, (double) 0);
	}

	/**
	 * 将字符串类型转换为双精度类型，出错时有def值返回
	 *
	 * @param str
	 * @param def
	 */
	public static double StringToDouble(String str, double def) {
		double dRet = (double) def;
		try {
			if (str == null || str.trim().equals(""))
				str = def + "";
			dRet = Double.parseDouble(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return dRet;
	}

	/**
	 * 将字符串类型转换为双精度类型
	 *
	 * @param str
	 */
	public static long StringToLong(String str) {
		return StringToLong(str, (long) 0);
	}

	/**
	 * 将字符串类型转换为双精度类型，出错时有def值返回
	 *
	 * @param str
	 * @param def
	 */
	public static long StringToLong(String str, long def) {
		long dRet = (long) def;
		try {
			if (str == null || str.trim().equals(""))
				str = def + "";
			dRet = Long.parseLong(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return dRet;
	}

	/**
	 * 将字符串类型转换为时间类型，出错时有def值返回
	 *
	 * @param str
	 */
	public static String StringToDate(String str) {
		String intRet = DateUtil.date2str(new java.util.Date());
		try {
			if (str == null || str.trim().equals(""))
				str = intRet;
			intRet = str;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return intRet;
	}

	/**
	 * 返回正确的日期格式
	 * 
	 * @param str
	 */
	public static String StringToStrDate(String str) {
		String init = "";
		if (str == null || str.trim().equals("")) {
			init = DateUtil.getDateStr(new Date());
		} else {
			init = str;
		}
		return init;
	}

	/**
	 * 获得安全字符串，使得字符串不为空，并去掉前后的空格
	 *
	 * @param str
	 */
	public static String getSafeString(String str) {
		if (str == null)
			return "";
		else
			return str.trim();
	}

	/**
	 * 将字符串在指定的长度内显示，超出后以..代替
	 *
	 * @param str
	 *            in string
	 * @param iLen
	 *            specify length out string
	 */
	public static String substr(String str, int iLen) {
		if (str == null)
			return "";
		if (iLen > 2) {
			str = toHtml(str);
			if (str.length() > iLen - 2) {
				str = str.substring(0, iLen - 2) + "..";
			}

		}
		str = str.replace("\"", "");
		return str;
	}

	public static String substr(String oldString, int length, String add) {
		int oldL = oldString.length();
		if (oldL < length) {
			for (int i = 0; i < (length - oldL); i++)
				oldString += add;
		} else if (oldL > length)
			oldString = oldString.substring(0, 8);
		return oldString;
	}

	/**
	 * 将字符串转换为UTF-8
	 *
	 * @param str
	 *            handle string str
	 */
	public static String getJpString(String str) {
		if (str == null) {
			return null;
		}
		try {
			return new String(str.getBytes("ISO-8859-1"), "UTF-8");
		} catch (java.io.UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 将字符串数组转换为UTF-8
	 *
	 * @param str
	 *            handle string str[]
	 */

	public static String[] getJpString(String[] str) {
		if (str == null) {
			return null;
		}
		String[] ret = new String[str.length];
		for (int i = 0; i < str.length; i++) {
			ret[i] = getJpString(str[i]);
		}
		return ret;
	}

	/**
	 * 返回以UTF-8编码的URL
	 *
	 * @param str
	 *            handle string str[]
	 */

	public static String getUrlString(String str) {
		if (str == null) {
			return null;
		}
		try {
			return URLEncoder.encode(str, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void Obj2Map(Object obj, Map map) {
		if (map == null)
			map = new java.util.HashMap();
		PropertyDescriptor descriptors[] = BeanUtilsBean.getInstance()
				.getPropertyUtils().getPropertyDescriptors(obj);
		for (int i = 0; i < descriptors.length; i++) {
			String name = descriptors[i].getName();
			try {
				if (descriptors[i].getReadMethod() != null) {
					map.put(name, BeanUtilsBean.getInstance()
							.getPropertyUtils().getProperty(obj, name));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将对象转换了字符型
	 * 
	 * @param s
	 */
	public static String null2String(Object s) {
		return s == null ? "" : ("null".equals(s) ? "" : s.toString());

	}

	/*
	 * public static String null2String2(Object s,String _default) { if(s
	 * ==null||"".equals(s)) return _default; return s.toString(); }
	 */

	/**
	 * 运行可执行文件
	 * 
	 * @param cmd
	 * @return String
	 */
	public static synchronized boolean executeCmd(String cmd) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmd);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		process.destroy();
		return true;
	}

	/**
	 * 将字符串数组转换为（'a','b'）的格式后返回，来方便数据库的操作
	 * 
	 * @param names
	 * @return String
	 */
	public static String getStrsplit(String[] names) {
		if (names == null || names.length == 0)
			return "()";
		String result = "(";
		for (int i = 0; i < names.length; i++) {
			if (i == names.length - 1)
				result = result + "'" + names[i] + "'";
			else
				result = result + "'" + names[i] + "',";
		}
		result = result + ")";
		return result;
	}

	/**
	 * 将字符串数组转换为（'a','b'）的格式后返回，来方便数据库的操作
	 * 
	 * @param names
	 * @return String
	 */
	public static String getStrsplit(List names) {
		if (names == null || names.size() == 0)
			return "('')";
		String result = "(";
		for (int i = 0; i < names.size(); i++) {
			if (i == names.size() - 1)
				result = result + "'" + (String) names.get(i) + "'";
			else
				result = result + "'" + (String) names.get(i) + "',";
		}
		result = result + ")";
		return result;
	}

	/**
	 * 将整型数组转换为（1，2）的格式后返回，来方便数据库的操作
	 * 
	 * @param ids
	 * @return String
	 */
	public static String getIdsplit(String[] ids) {
		if (ids == null || ids.length == 0)
			return "('')";
		String result = "(";
		for (int i = 0; i < ids.length; i++) {
			if (i == ids.length - 1)
				result = result + ids[i];
			else
				result = result + ids[i] + ",";
		}
		result = result + ")";
		return result;
	}

	/**
	 * 将向量转换为（1，2）的格式后返回，来方便数据库的操作
	 * 
	 * @param ids
	 * @return String
	 */
	public static String getIdsplit(List ids) {
		if (ids == null || ids.size() == 0)
			return "()";
		String result = "(";
		for (int i = 0; i < ids.size(); i++) {
			if (i == ids.size() - 1)
				result = result + (String) ids.get(i);
			else
				result = result + (String) ids.get(i) + ",";
		}
		result = result + ")";
		return result;
	}

	/**
	 * 将url转换为图片的HTML代码格式
	 * 
	 * @param url
	 * @return String
	 */
	public static String toImage(String url) {
		return "<img src='" + url + "' border=0>";
	}

	/**
	 * 获取字符串长度
	 * 
	 * @param url
	 * @return String
	 */
	public static int getHeight(String sr) {
		if (sr != null)
			return sr.length();
		else
			return 0;
	}

	/**
	 * 将url转换为图片的HTML代码格式
	 * 
	 * @param url
	 * @return String
	 */
	public static String toImage(String url, String attribute) {
		return "<img src='" + url + "' border=0 " + attribute + " >";
	}

	/**
	 * 返回树形结构的字符串为├来分级
	 * 
	 * @param level
	 * @param flag
	 * @return String
	 */
	public static String getLevelFlag(int level, String flag) {
		String temp = "";
		for (int i = 0; i < level; i++) {
			temp = temp + flag;
		}
		return temp + "├";
	}

	/**
	 * 返回树形结构的字符串为├来分级
	 * 
	 * @param level
	 * @param flag
	 * @return String
	 */
	public static String getLevelFlag(String level, String flag) {
		System.out.println("level.length()/4=" + level.length() / 4);
		switch (level.length() / 4) {
		case 1:
			return "&nbsp;├" + flag;
		case 2:
			return "&nbsp;&nbsp;├" + flag;
		case 3:
			return "&nbsp;&nbsp;&nbsp;├" + flag;
		case 4:
			return "&nbsp;&nbsp;&nbsp;&nbsp;├" + flag;
		case 5:
			return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;├" + flag;
		default:
			return "&nbsp;├" + flag;
		}
	}

	/**
	 * @param str
	 * @return 防止sql注入对系统产生破坏；
	 */
	public static boolean isSqlSafe(String str) {
		if (str == null)
			return true;
		String[] init_str = { "'", "or", "-", "and", "union", "count", "exec",
				"select", "delete", "update" };
		for (int i = 0; i < init_str.length; i++) {
			if (str.indexOf(init_str[i]) >= 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 访问站点得到站点的访问数量同时访问数量+1;刷新不增加
	 */
	public static String getImgs(String mapurl, int num, int len) {

		String img = "";
		String temp = num + "";
		if (len > temp.length()) {
			for (int i = temp.length(); i < len; i++) {
				img += "<img src='" + mapurl + "0.gif'>";
			}
		}
		for (int i = 0; i < temp.length(); i++) {
			img += "<img src='" + mapurl + temp.substring(i, i + 1) + ".gif'>";
		}

		return img;
	}

	public static String substrNo(String str, int iLen) {
		str = str.trim();
		if (str == null)
			return "";
		if (iLen > 2) {
			if (str.length() > iLen - 2) {
				str = str.substring(0, iLen - 2) + "";
			}

		}
		return str;
	}

	/**
	 * 过滤str中的htnl敏感字符
	 *
	 * @param str
	 * @return String
	 */
	public static String toHtml(String str) {
		if (str == null)
			return "";
		if (str.indexOf("来电人反映：") > 0) {
			int flag = str.indexOf("来电人反映：");
			str = str.substring(flag);
		}
		StringBuffer sb = new StringBuffer();
		int len = str.length();
		for (int i = 0; i < len; i++) {
			char c = str.charAt(i);
			switch (c) {
			case ' ':
				sb.append("&nbsp;");
				break;
			case '\n':
				sb.append("<br>");
				break;
			case '\r':
				break;
			case '\'':
				sb.append("&#39;");
				break;

			case '<':
				if (str.length() >= i + 4) {
					if ("<br>".equals(str.substring(i, i + 4))) {
						sb.append(c);
						break;
					}
				}
				sb.append("&lt;");
				break;
			case '>':
				if (i - 3 >= 0) {
					if ("<br>".equals(str.substring(i - 3, i + 1))) {
						sb.append(c);
						break;
					}
				}
				sb.append("&gt;");
				break;
			case '&':
				sb.append("&amp;");
				break;
			case '"':
				sb.append("&#34;");
				break;
			case '\\':
				sb.append("&#92;");
				break;
			default:
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * 对上传文件的路径进行处理。因为论坛上传图片发帖子，在业务系统后台审核。部署在2个tomcat 端口不一样导致后台审核图片挂掉。 现对路径进行处理
	 * create by yhb web 项目名 替换IP ipweb格式如下 http://ip:port/web
	 * 
	 */

	public static String doSrc(String str, String ipweb, String web) {

		int index = str.indexOf("src=\"/" + web);
		if (index != -1) {
			String s = str.substring(index, index + 6 + web.length());
			str = str.replace(s, "src=\"" + ipweb);
		}
		return str;
	}
}
