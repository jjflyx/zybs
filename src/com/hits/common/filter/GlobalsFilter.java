package com.hits.common.filter;

import org.nutz.mvc.*;

import com.hits.common.config.Globals;

/**
 * @author Wizzer.cn
 * @time   2012-9-13 上午10:54:04
 *
 */
public class GlobalsFilter implements ActionFilter{

	public View match(ActionContext context) {
		context.getRequest().setAttribute("app_name", Globals.APP_NAME);
		context.getRequest().setAttribute("app_base_name", Globals.APP_BASE_NAME);
		context.getRequest().setAttribute("app_base_path", Globals.APP_BASE_PATH);
		
		return null;
	}

}
