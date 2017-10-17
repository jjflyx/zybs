package com.hits.common.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Numbgui on 2016/4/6.
 */
public class FrUtil {

    public static void main(String[] args){
        try {
            getFrObj("淮政办秘[1995]121号");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Map<String,String> getFrObj(String keyword) throws IOException{
        Map<String,String> frMap = new HashMap<String, String>();
        try {
        	BasicCookieStore cookieStore = new BasicCookieStore();
        	String url="http://59.203.19.239/proxy?uscc="+keyword;
        	CloseableHttpClient httpclient = HttpClients.createDefault();
        	HttpGet httpget = new HttpGet(url);
        	httpget.addHeader("esb_service_id","21940");
        	httpget.addHeader("esb_token","90c2a024-1d26-4d5d-a09a-e6342b6a1edf");
        	CloseableHttpResponse response = httpclient.execute(httpget);
        	HttpEntity entity = response.getEntity();
        	String result = EntityUtils.toString(response.getEntity(), "utf-8");
        	JSONObject jsonObj = new JSONObject(result);
        	JSONArray ja = jsonObj.getJSONArray("data");
        	if(ja.length() > 0) {
        		for (int i = 0; i < 1; i++) {
        			JSONObject jobj = (JSONObject) ja.get(i);
        			Iterator it = jobj.keys();
        			while (it.hasNext()) {
        				String key = String.valueOf(it.next());
        				String value = StringUtil.isNull(StringUtil.null2String(jobj.get(key)), "");
        				if(("SLRQ".equals(key.toUpperCase())||"HZRQ".equals(key.toUpperCase()))&&value.length()>10){
        					value=value.substring(0,10);
        				}
        				frMap.put(key, value);
        			}
        		}
        	}
        	response.close();
        	httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return frMap;
    }
    public static Map<String,String> getFrByName(String keyword) throws IOException{
        Map<String,String> frMap = new HashMap<String, String>();
        try {
        	BasicCookieStore cookieStore = new BasicCookieStore();
        	String url="http://59.203.19.239/proxy?uscc="+keyword;
        	CloseableHttpClient httpclient = HttpClients.createDefault();
        	HttpGet httpget = new HttpGet(url);
        	httpget.addHeader("esb_service_id","21940");
        	httpget.addHeader("esb_token","90c2a024-1d26-4d5d-a09a-e6342b6a1edf");
        	CloseableHttpResponse response = httpclient.execute(httpget);
        	HttpEntity entity = response.getEntity();
        	String result = EntityUtils.toString(response.getEntity(), "utf-8");
        	JSONObject jsonObj = new JSONObject(result);
        	JSONArray ja = jsonObj.getJSONArray("data");
        	if(ja.length() > 0) {
        		for (int i = 0; i < 1; i++) {
        			JSONObject jobj = (JSONObject) ja.get(i);
        			if(StringUtil.null2String(jobj.get("FRMC")).equals(keyword)){//完全匹配法人名称
	        			Iterator it = jobj.keys();
	        			while (it.hasNext()) {
	        				String key = String.valueOf(it.next());
	        				String value = StringUtil.isNull(StringUtil.null2String(jobj.get(key)), "");
	        				if(("SLRQ".equals(key.toUpperCase())||"HZRQ".equals(key.toUpperCase()))&&value.length()>10){
	        					value=value.substring(0,10);
	        				}
	        				frMap.put(key, value);
	        			}
	        			break;
        			}
        		}
        	}
        	response.close();
        	httpclient.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return frMap;
    }
}
