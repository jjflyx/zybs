package com.hits.common.config;

import java.io.File;

import javax.xml.parsers.*;

import org.w3c.dom.*;

/**
 * @author Wizzer.cn
 * @time   2012-9-13 上午10:54:04
 *
 */
public class XMLConfigFactory {

	private static Document doc = null;
	public static void init(String xmlFile) throws Exception {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(new File(xmlFile));
	} 
	public static String GetName(String name) throws Exception {
		init(Globals.APP_BASE_PATH+"/WEB-INF/web.xml");
		NodeList nodeList = doc.getElementsByTagName(name);	
		String str=""; 
		try{
			Node fatherNode = nodeList.item(0);
			str=fatherNode.getFirstChild().getNodeValue();
		}catch(Exception e){
			return "";
		}
		return str;

	}
}
