package com.hits.common.dao;

import java.io.BufferedReader;
import java.sql.Clob;
import java.sql.ResultSet;

import org.nutz.log.Log;
import org.nutz.log.Logs;
 

/**
 * @author Wizzer.cn
 * @time   2012-9-13 下午6:13:55
 *
 */
public class DBObject {
	private static final Log log = Logs.get();
	/*
	 * 获取CLOB字段值
	 */
	/*public static String getClobBody(ResultSet rs, String colnumName)
	{
		String result = "";
		try
		{
			String str_Clob = "";
			StringBuffer strBuffer_CLob = new StringBuffer();
			strBuffer_CLob.append("");
			oracle.sql.CLOB clob = (oracle.sql.CLOB) rs.getClob(colnumName);
			BufferedReader in = new BufferedReader(clob.getCharacterStream());
			while ((str_Clob = in.readLine()) != null)
			{
				strBuffer_CLob.append(str_Clob + "\n");
			}
			in.close();
			result = strBuffer_CLob.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.debug(e.getMessage());
		}
		return result;
	}*/
	
	public static String getClobBody(ResultSet rs, String colnumName)
	{
		String result = "";
		try
		{
			String str_Clob = "";
			StringBuffer strBuffer_CLob = new StringBuffer();
			strBuffer_CLob.append("");
			Clob clob = rs.getClob(colnumName);
			BufferedReader in = new BufferedReader(clob.getCharacterStream());
			while ((str_Clob = in.readLine()) != null)
			{
				strBuffer_CLob.append(str_Clob + "\n");
			}
			in.close();
			result = strBuffer_CLob.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.debug(e.getMessage());
		}
		return result;
	}
	
	/*
	 * 获取CLOB字段值
	 * 解决：java.lang.ClassCastException: com.alibaba.druid.proxy.jdbc.ClobProxyImpl cannot be cast to oracle.sql.CLOB
	 */
	public static String getClobBody1(ResultSet rs, String colnumName)
	{
		String result = "";
		try
		{
			String str_Clob = "";
			StringBuffer strBuffer_CLob = new StringBuffer();
			strBuffer_CLob.append("");
			Clob clob = rs.getClob(colnumName);
			BufferedReader in = new BufferedReader(clob.getCharacterStream());
			while ((str_Clob = in.readLine()) != null)
			{
				strBuffer_CLob.append(str_Clob + "\n");
			}
			in.close();
			result = strBuffer_CLob.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			log.debug(e.getMessage());
		}
		return result;
	}
}


