package com.hits.common.file;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.nutz.mvc.upload.UploadAdaptor;

import com.hits.common.util.StringUtil;

/** 
 * 类描述： 
 * 创建人：Wizzer 
 * 联系方式：www.wizzer.cn
 * 创建时间：2013-12-25 下午1:13:30 
 * @version 
 */
public class FileType { 
	/**
	 * 根据文件名获取文件类型，默认返回other
	 * @param upload
	 * @param suffixname
	 * @return
	 */
	public static String getFileType(UploadAdaptor upload,String suffixname) {
		Map<String,String> hm=upload.getContext().getExtOption();
		String str = StringUtil.null2String(suffixname).toLowerCase();
		Set<Map.Entry<String, String>> set = hm.entrySet();
        for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
            Map.Entry<String, String> entry = it.next();
            if(entry.getValue().toLowerCase().contains(str))
            	return entry.getKey();
        }
		return "other";
	}
	/**
	 * 根据文件类型获取文件后缀名，默认返回nameFilter配置
	 * @param upload
	 * @param filetype
	 * @return
	 */
	public static String getSuffixname(UploadAdaptor upload,String filetype) {
		Map<String,String> hm=upload.getContext().getExtOption();
		String str = StringUtil.null2String(filetype).toLowerCase();
		if(!"".equals(str)){
			Set<Map.Entry<String, String>> set = hm.entrySet();
	        for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
	            Map.Entry<String, String> entry = it.next();
	            if(entry.getKey().toLowerCase().equals(str))
	            	return entry.getValue();
	        }
		}
        String name=upload.getContext().getNameFilter(); 
		return name.substring(name.indexOf("(")+1,name.lastIndexOf(")")).replace("|", ",");
	}
}
