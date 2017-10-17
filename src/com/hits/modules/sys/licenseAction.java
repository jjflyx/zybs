package com.hits.modules.sys;

import com.hits.common.action.BaseAction;
import com.hits.common.config.Globals;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.util.*;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Wizzer.cn
 * @time 2012-9-13 上午10:54:04
 * 
 */
@IocBean
@At("/license")
@Filters({ @By(type = GlobalsFilter.class)})
public class licenseAction extends BaseAction {
	@At
	@Ok("->:/private/license/licenseRequest.html")
	public void doLicense(@Param("sqm") String sqm, HttpServletRequest req) {
		String msg = "授权失败，请重新注册！";
		String regPath = Globals.APP_BASE_PATH + "\\WEB-INF\\reginfo.sn";
		File regFile = new File(regPath);
		if(!"".equals(sqm) && DiskUtils.validateSN(sqm)){
			RandomAccessFile mm = null;
			try {
				mm = new RandomAccessFile(regFile, "rw");
				mm.writeBytes(sqm);
				msg = "授权成功!";
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				if (mm != null) {
					try {
						mm.close();
					} catch (IOException e2) {
						e2.printStackTrace();
					}
				}
			}
		}
		req.setAttribute("msg", msg);
	}
}
