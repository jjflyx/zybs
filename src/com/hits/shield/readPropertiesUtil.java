package com.hits.shield;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;


public class readPropertiesUtil {
	public static List readPropertiesList (String fielpath){
		List list=new ArrayList();
		InputStream inputStream =new  readPropertiesUtil().getClass().getClassLoader().getResourceAsStream(fielpath);   
		Properties p = new Properties();   
		try {   
			p.load(inputStream);   
		} catch (IOException e1) {   
			e1.printStackTrace();   
		}   
		Iterator pit=p.entrySet().iterator();
		while(pit.hasNext()){
			Entry entry=(Entry) pit.next();
			list.add(entry.getValue());
		}
		return list;
	}
	
	public static Map readPropertiesMap (String fielpath){
		Map map=new HashMap();
		InputStream inputStream =new  readPropertiesUtil().getClass().getClassLoader().getResourceAsStream(fielpath);   
		Properties p = new Properties();   
		try {   
			p.load(inputStream);   
		} catch (IOException e1) {   
			e1.printStackTrace();   
		}   
		Iterator pit=p.entrySet().iterator();
		while(pit.hasNext()){
			Entry entry=(Entry) pit.next();
			map.put(entry.getValue(), entry.getValue());
		}
		return map;
	}
	public static List<CorChar> readPropertiesList2 (String fielpath){
		List<CorChar> list=new ArrayList<CorChar>();
		InputStream inputStream =new  readPropertiesUtil().getClass().getClassLoader().getResourceAsStream(fielpath);   
		Properties p = new Properties();   
		try {   
			p.load(inputStream);   
		} catch (IOException e1) {   
			e1.printStackTrace();   
		}   
		Iterator pit=p.entrySet().iterator();
		while(pit.hasNext()){
			CorChar corc=new CorChar();
			Entry entry=(Entry) pit.next();
			corc.setYchar((String)entry.getKey());
			corc.setRepchar((String)entry.getValue());
			list.add(corc);
		}
		return list;
	}
}
