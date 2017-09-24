package com.hits.modules.filter;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;

import com.google.gson.Gson;
import com.hits.useragentutil.Browser;
import com.hits.useragentutil.OperatingSystem;
import com.hits.useragentutil.UserAgent;

/**
 * 
 *  #(c) IFlytek cz_weixin <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: 微信程序请求拦截类
 * 
 *  <br/>创建说明: 2015年6月17日 下午1:52:05 (☆_☆)  创建文件<br/>
 * 
 *  修改历史:<br/>
 *
 */
public class ParmarsVerifyFilter implements ActionFilter {

	public View match(ActionContext context) {
		HttpServletRequest request=context.getRequest();
		 
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));  
		Browser browser = userAgent.getBrowser();  
	    OperatingSystem os = userAgent.getOperatingSystem();  
	    
	    //当前请求的url
	    String reqUrl=request.getRequestURI();
	    String xmName=request.getContextPath();
	    reqUrl=reqUrl.substring(xmName.length(),reqUrl.length());
	    //获取排除验证参数
		Map extraParmarsMap= readPropertiesUtil.readPropertiesMap("extraparmars.properties");
	    //获取非法字符库
		List filterCharList=readPropertiesUtil.readPropertiesList("filterchar.properties");
		Gson s=new Gson();
		//获取请求参数
		Map parameter =  request.getParameterMap();
		Iterator it = parameter.entrySet().iterator();

        for (; it.hasNext();) {
            Entry entry=(Entry) it.next();
            String key = String.valueOf(entry.getKey());
            String  a =   request.getParameter((String) entry.getKey()).replace("@","");
            request.setAttribute(key,a);
        }
        
        while(it.hasNext()){
			Entry entry=(Entry) it.next();
			//判断是否包含不验证字段
			if(!extraParmarsMap.containsValue(entry.getKey())){
				for (int i=0;i<filterCharList.size();i++) {
					//验证内容中是否有屏蔽字
					if(request.getParameter((String) entry.getKey()).contains((CharSequence) filterCharList.get(i))){
//						System.out.println(entry.getKey());
//						System.out.println(filterCharList.get(i));
					}
				}
			}

		}
        
        
//	    if (!filterCharList.contains(reqUrl)) {
//	    	System.out.println("------------------------");
//	    	 //验证是否是使用微信客户端浏览器访问
//		    if (os.getDeviceType().equals("COMPUTER") || request.getHeader("User-Agent").toLowerCase().indexOf("micromessenger")==-1) {
////		    	ServerRedirectView view = new ServerRedirectView("/private/weixin/no_wxerror.html");
//		    	request.setAttribute(ConstantsUtil.ERROR_MSG, "请使用微信客户端访问...");
//		    	ForwardView view=new ForwardView("/private/weixin/wxerror.html");
//				return view;
//			}
//		}
		return null;
	}
}
