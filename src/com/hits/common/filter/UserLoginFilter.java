package com.hits.common.filter;

import java.util.Hashtable;

import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.ServerRedirectView;

import com.hits.common.config.Globals;
import com.hits.common.util.StringUtil;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 * 
 */
public class UserLoginFilter implements ActionFilter {

	public View match(ActionContext context) {
		Sys_user user = (Sys_user) context.getRequest().getSession().getAttribute("userSession");
		if (EmptyUtils.isNotEmpty(user)) {
			

			context.getRequest().setAttribute("curuser", user);
			context.getRequest().setAttribute("isManager", 1);
		} 
		if ((user == null ||user.isValidate()==true)&& context.getRequest().getRequestURL().toString() .indexOf("doSynLogin") < 0) {

			if (context.getRequest().getHeader("x-requested-with") != null
					&& context.getRequest().getHeader("x-requested-with")
							.equalsIgnoreCase("XMLHttpRequest"))// 如果是ajax请求响应头会有，x-requested-with；
			{
				context.getResponse().setHeader("sessionstatus", "timeout");// 在响应头设置session状态

			}
			ServerRedirectView view = new ServerRedirectView(
					"/include/error/nologin.jsp");
			return view;

		}
		if (EmptyUtils.isNotEmpty(user)) {
			Hashtable<String, String> btnmap = user.getBtnmap();
			if (btnmap != null) {
				String initBtn = "";
				String bts = btnmap.get(StringUtil.null2String(context
						.getRequest().getRequestURI()
						.replace(Globals.APP_BASE_NAME, "")));
				if (bts != null && bts.indexOf(",") > 0) {
					String[] tb = bts.split(",");
					for (int i = 0; i < tb.length; i++) {
						initBtn += "$Z(\"" + tb[i] + "\").enable();\r\n";
					}

				}
				context.getRequest().setAttribute("initBtn", initBtn);
			}
		}

		return null;
	}

}
