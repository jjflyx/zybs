package com.hits.util;

import java.util.List;

import net.sf.json.JSONObject;

import org.nutz.dao.QueryResult;

public class PageObj {
	private int total;
	private List rows;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List getRows() {
		return rows;
	}
	public void setRows(List rows) {
		this.rows = rows;
	}
	
	public static String getPageJSON(QueryResult qr){
		if(qr!=null){
			PageObj pb=new PageObj();
			pb.setTotal((qr.getPager().getRecordCount()));
			pb.setRows(qr.getList());
			return JSONObject.fromObject(pb).toString();
		}else
			return null;
	}
	
	public static JSONObject getPageJSONObj(QueryResult qr){
		if(qr!=null){
			PageObj pb=new PageObj();
			pb.setTotal((qr.getPager().getRecordCount()));
			pb.setRows(qr.getList());
			return JSONObject.fromObject(pb);
		}else
			return null;
	}
}
