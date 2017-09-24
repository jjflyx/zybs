package com.hits.util;

import java.sql.Timestamp;

import org.apache.commons.lang.StringUtils;

/**
 * 
 *  功能说明: SQL工具类,用于where条件查询
 * 
 *   2013-7-20 下午12:08:34 houkun  创建文件
 * 
 *  修改历史:<br/>
 *
 */
public class SQLUtils {
	
	/**
	 * 组装like hql
	 * 
	 * @param column	列名
	 *            
	 * @param param		参数
	 *            
	 * @return
	 */
	public static String popuHqlLike(String column, String param) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(param)) {
			sb.append(" and ").append(column).append(" like '%").append(param.trim()).append("%' ");
		}
		return sb.toString();
	}
	
	/**
	 * 组装 like 'xxxx%'
	 * 
	 * @param column	列名
	 *            
	 * @param param		查询值
	 *            
	 * @return hql
	 */
	public static String popuHqlRLike(String column, String param) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(param)) {
			sb.append(" and ").append(column).append(" like '").append(
					param.trim()).append("%' ");
		}
		return sb.toString();
	}
	
	/**
	 * 组装 like 'xxxx%'
	 * 
	 * @param column	列名
	 *            
	 * @param param		查询值
	 *            
	 * @return hql
	 */
	public static String popuHqlLLike(String column, String param) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(param)) {
			sb.append(" and ").append(column).append(" like '%").append(
					param.trim()).append("' ");
		}
		return sb.toString();
	}
	
	
	/**
	 * 组装equals hql eg:col='param'
	 * 
	 * @param column	列名
	 *            
	 * @param param		参数
	 *            
	 * @return
	 */
	public static String popuHqlEq(String column, String param) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(param)) {
			sb.append(" and ").append(column).append("='").append(param.trim())
					.append("' ");
		}
		return sb.toString();
	}
	
	/**
	 * 组装equals hql eg:col=param
	 * 
	 * @param column	列名
	 *            
	 * @param param		参数
	 *            
	 * @return
	 */
	public static String popuHqlEq(String column, int param) {
		StringBuilder sb = new StringBuilder();
		if (param!=0) {
			sb.append(" and ").append(column).append("=").append(param)
					.append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 组装equals hql eg:col=param
	 * 
	 * @param column	列名
	 *            
	 * @param param 	参数
	 *            
	 * @return
	 */
	public static String popuHqlEq(String column, long param) {
		StringBuilder sb = new StringBuilder();
		if (param!=0) {
			sb.append(" and ").append(column).append("=").append(param)
					.append(" ");
		}
		return sb.toString();
	}
	

	/**
	 * 组装equals hql eg:col!='param'
	 * 
	 * @param column	列名
	 * 
	 * @param param		参数
	 * 
	 * @return
	 */
	public static String popuHqlNe(String column, String param) {
		StringBuilder sb = new StringBuilder();
		if (StringUtils.isNotBlank(param)) {
			sb.append(" and ").append(column).append("!='").append(param.trim())
					.append("' ");
		}
		return sb.toString();
	}
	
	

	/**
	 * 组装equals hql
	 * 
	 * @param column	列名
	 *            
	 * @param param		参数
	 *            
	 * @return
	 */
	public static String popuHqlEq(String column, Integer param) {
		StringBuilder sb = new StringBuilder();
		if (param != null) {
			sb.append(" and ").append(column).append("=").append(param)
					.append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 组装equals hql
	 * 
	 * @param column	列名
	 *            
	 * @param param		参数
	 *            
	 * @return
	 */
	public static String popuHqlEq(String column, Long param) {
		StringBuilder sb = new StringBuilder();
		if (param != null) {
			sb.append(" and ").append(column).append("=").append(param).append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 功能描述:字段大于给定的值
	 *
	 * @author houkun  2013-7-24 下午09:44:37
	 * 
	 * @param column 	列名
	 * 
	 * @param param 	参数
	 * 
	 * @return
	 */
	public static String popuHqlMin(String column, Long param) {
		StringBuilder sb = new StringBuilder();
		if (param != null) {
			sb.append(" and ").append(column).append(">=").append(param).append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 功能描述:字段小于给定的值
	 *
	 * @author houkun  2013-7-24 下午09:45:07
	 * 
	 * @param column 	列名
	 * 
	 * @param param 	参数
	 * 
	 * @return
	 */
	public static String popuHqlMax(String column, Long param) {
		StringBuilder sb = new StringBuilder();
		if (param != null) {
			sb.append(" and ").append(column).append("<=").append(param)
					.append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 功能描述:oracle时间区间查询
	 *
	 * @author houkun  2014-5-28 下午01:49:27
	 * 
	 * @param column	列名
	 * 
	 * @param param		参数
	 * 
	 * @return
	 */
	public static String popuHqlMin(String column, Timestamp param) {
		StringBuilder sb = new StringBuilder();
		if (param != null) {
			sb.append(" and ").append(column).append(">=").append(param).append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 功能描述:oracle时间区间查询
	 *
	 * @author houkun  2014-5-28 下午01:50:25
	 * 
	 * @param column	列名
	 * 
	 * @param param		参数
	 * 
	 * @return
	 */
	public static String popuHqlMax(String column, Timestamp param) {
		StringBuilder sb = new StringBuilder();
		if (param != null) {
			sb.append(" and ").append(column).append("<=").append(param)
					.append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 功能描述:字段大于给定的值
	 *
	 * @author houkun  2013-7-24 下午09:44:37
	 * 
	 * @param column 	列名
	 * 
	 * @param param 	参数
	 * 
	 * @return
	 */
	public static String popuHqlMin(String column, Integer param) {
		StringBuilder sb = new StringBuilder();
		if (param != null) {
			sb.append(" and ").append(column).append(">=").append(param)
					.append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 功能描述:字段小于给定的值
	 *
	 * @author houkun  2013-7-24 下午09:45:07
	 * 
	 * @param column 	列名
	 * 
	 * @param param 	参数
	 * 
	 * @return
	 */
	public static String popuHqlMax(String column, Integer param) {
		StringBuilder sb = new StringBuilder();
		if (param != null) {
			sb.append(" and ").append(column).append("<=").append(param)
					.append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 功能描述:字段大于给定的值
	 *
	 * @author houkun  2013-7-24 下午09:44:37
	 * 
	 * @param column 	列名
	 * 
	 * @param param 	参数
	 * 
	 * @return
	 */
	public static String popuHqlMin(String column, Double param) {
		StringBuilder sb = new StringBuilder();
		if (param != null) {
			sb.append(" and ").append(column).append(">=").append(param).append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 功能描述:字段小于给定的值
	 *
	 * @author houkun  2013-7-24 下午09:45:07
	 * 
	 * @param column 	列名
	 * 
	 * @param param 	参数
	 * 
	 * @return
	 */
	public static String popuHqlMax(String column, Double param) {
		StringBuilder sb = new StringBuilder();
		if (param != null) {
			sb.append(" and ").append(column).append("<=").append(param)
					.append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 功能描述:把字符串时间转为to_date('"+startdate+"','yyyy-MM-dd')查询
	 *
	 * @author houkun  2014-5-28 下午01:49:27
	 * 
	 * @param column	列名
	 * 
	 * @param param		参数
	 * 
	 * @return
	 */
	public static String popuSqlTo_DateMin(String column, String param) {
		StringBuilder sb = new StringBuilder();
		if (param != null) {
			sb.append(" and ").append(column).append(">=").append("to_date('"+param+"','yyyy-MM-dd')").append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * 功能描述:把字符串时间转为to_date('"+startdate+"','yyyy-MM-dd')查询
	 *
	 * @author houkun  2014-5-28 下午01:50:25
	 * 
	 * @param column	列名
	 * 
	 * @param param		参数
	 * 
	 * @return
	 */
	public static String popuSqlTo_DateMax(String column, String param) {
		StringBuilder sb = new StringBuilder();
		if (param != null) {
			sb.append(" and ").append(column).append("<=").append("to_date('"+param+"','yyyy-MM-dd')").append(" ");
		}
		return sb.toString();
	}
}
