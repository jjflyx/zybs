package com.hits.core;

import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import com.hits.common.config.Globals;
import com.hits.common.config.XMLConfigFactory;
import com.hits.modules.com.comUtil;

/**
 * 类描述： 创建人：Wizzer 联系方式：www.wizzer.cn 创建时间：2013-11-26 下午2:11:13
 */
public class StartSetup implements Setup {
    private final static Log log = Logs.get();

    public void destroy(NutConfig config) {

    }

    public void init(NutConfig config) {
        try {

            Globals.APP_BASE_PATH = config.getAppRoot();
            Globals.APP_BASE_NAME = config.getServletContext().getContextPath();
            Globals.APP_NAME = XMLConfigFactory.GetName("description");
            
            //初始化公共静态类
            comUtil coms=new comUtil();
        	coms.init();
        } catch (Exception e) {
            log.error(e);
        }
    }

}
