package com.hits.common.filter;

import com.hits.common.config.Globals;
import com.hits.common.util.DecodeUtil;
import com.hits.common.util.DiskUtils;
import com.hits.common.util.StringUtil;
import org.nutz.mvc.ActionContext;
import org.nutz.mvc.ActionFilter;
import org.nutz.mvc.View;
import org.nutz.mvc.view.ForwardView;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author Numbgui
 * @time   2016-8-9 上午10:54:04
 *
 */
public class licenseFilter implements ActionFilter{

	public View match(ActionContext context) {
		String path=context.getPath();
		if(!checkRegInfo()){
			//未注册,获取主机Mac地址
			String number = StringUtil.null2String(DiskUtils.getSigarSequence());
			String xlh = DecodeUtil.Encrypt(number).toUpperCase();
			HttpServletRequest req = context.getRequest();
			req.setAttribute("xlh",xlh);
			ForwardView view = new ForwardView("/private/license/licenseRequest.html");
			return view;
		}
		return null;
	}

	private static boolean checkRegInfo() {
		BufferedReader bufread;
		String regPath = Globals.APP_BASE_PATH + "\\WEB-INF\\reginfo.sn";
		File regFile = new File(regPath);

		//获取产品序列号（Mac地址）
		String number = DiskUtils.getSigarSequence();
		if (!regFile.exists()) //如果没有注册文件
		{
			return false;
		} else { //有注册文件的话  读取文件中的内容
			String SN = "";
			String temp;
			FileReader fileread;
			try {
				fileread = new FileReader(regFile);
				bufread = new BufferedReader(fileread);
				while ((temp = bufread.readLine()) != null) {
					SN = temp;
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return DiskUtils.validateSN(SN);
		}
	}
}
