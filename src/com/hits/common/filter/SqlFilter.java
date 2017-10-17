package com.hits.common.filter;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.mvc.*;
import org.nutz.mvc.view.ServerRedirectView;

import com.hits.common.config.Globals;
import com.hits.modules.filter.readPropertiesUtil;

/**
 * @author Wizzer.cn
 * @time   2012-9-13 上午10:54:04
 *
 */
public class SqlFilter implements ActionFilter{

	public View match(ActionContext context) {
		HttpServletRequest req = (HttpServletRequest) context.getRequest();
        HttpServletResponse res = (HttpServletResponse) context.getResponse();
        //获得所有请求参数名
        Enumeration params = req.getParameterNames();

        String sql = "";
        while (params.hasMoreElements()) {
            //得到参数名
            String name = params.nextElement().toString();
          //获取排除验证参数
			Map extraParmarsMap= readPropertiesUtil.readPropertiesMap("extraparmars.properties");
            if(!extraParmarsMap.containsValue(name)){
            	//得到参数对应值
            	String[] value = req.getParameterValues(name);
            	for (int i = 0; i < value.length; i++) {
            		sql = sql + value[i];
            	}
            }
        }


        //有sql关键字，跳转到error.html
        if (sqlValidate(sql)) {
			ServerRedirectView view = new ServerRedirectView("/include/error/404.html");
			return view;
        } else{
        	return null;
        }
		
	}
	//效验
    protected static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr = "and|exec|execute|insert|create|drop|table|from|grant|use|" +
                "group_concat|column_name|information_schema.columns|table_schema|" +
                "where|select|delete|update|order|by|*|chr|char" +
                "|declare|--";//|#|+|,|%|;|过滤掉的sql关键字，可以手动添加  qxmore000400010009
        String[] badStrs = badStr.split("\\|");

        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) != -1) {
                return true;
            }
        }
        return false;
    }

}
