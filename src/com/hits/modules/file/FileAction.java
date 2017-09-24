package com.hits.modules.file;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.AdaptBy;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.impl.AdaptorErrorContext;
import org.nutz.mvc.upload.TempFile;
import org.nutz.mvc.upload.UploadAdaptor;

import com.hits.common.action.BaseAction;
import com.hits.common.file.FileType;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.DateUtil;
import com.hits.common.util.StringUtil;

/**
 * 类描述： 创建人：Wizzer 联系方式：www.wizzer.cn 创建时间：2013-12-17 下午1:12:37
 *
 * @version
 */
@IocBean
@At("/private/file")
@Filters({ @By(type = GlobalsFilter.class)})
public class FileAction extends BaseAction {
	@Inject
	protected UploadAdaptor upload;
	private static final Log log=Logs.get();
	/**
	 * 打开单文件上传页面
	 * @param filetype
	 * @param req
	 */
	@At
	@Ok("->:/private/file/uploadOne.html")
	public void uploadOne(@Param("filetype") String filetype,HttpServletRequest req){
		req.setAttribute("filetype", filetype);
		req.setAttribute("allowExtensions", FileType.getSuffixname(upload, filetype));
	}
	/**
	 * 打开多文件上传页面
	 * @param filetype
	 * @param req
	 */
	@At
	@Ok("->:/private/file/uploadMore.html")
	public void uploadMore(@Param("filetype") String filetype,HttpServletRequest req){
		req.setAttribute("filetype", filetype);
		req.setAttribute("allowExtensions", FileType.getSuffixname(upload, filetype));
		req.setAttribute("maxFileSize", upload.getContext().getMaxFileSize());
	}
	/**
	 * 单个文件的保存操作
	 * @param tmpFile
	 * @param filetype
	 * @param req
	 * @param errCtx
	 * @return
	 */
	@At
	@Ok("raw")
	@AdaptBy(type = UploadAdaptor.class, args = "ioc:upload")
	public JSONObject uploadOneSave(@Param("f") TempFile tmpFile ,@Param("filetype") String filetype,HttpServletRequest req,AdaptorErrorContext errCtx) {
		JSONObject js=new JSONObject();
		if (errCtx != null) {
			Throwable t=errCtx.getAdaptorErr();
			return errorMsg(t);
		}
		if (tmpFile == null || tmpFile.getFile().length() < 10) {
			js.put("error", "错误：文件大小不可小于10B！");
			js.put("msg", "");
			return js;
		}
		String filename=tmpFile.getMeta().getFileLocalName();
		File file = tmpFile.getFile();
		String suffixname=Files.getSuffixName(file).toLowerCase();
		String ss=FileType.getSuffixname(upload, filetype);
		if(ss.indexOf(suffixname)<0){
			js.put("error", "错误：不允许的文件扩展名，允许："+ss);
			js.put("msg", "");
			System.out.println("22222222222222222222222222");
			return js;
		}
		long len=tmpFile.getFile().length();

		filename=filename.substring(0,filename.lastIndexOf("."))+"."+suffixname;
		String date = DateUtil.getToday();
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String fname= uuid+ "." + Files.getSuffixName(file).toLowerCase();
		String dest = webPath(date, fname, suffixname);
		try {
			Files.move(file, new File(dest));
		} catch (IOException e) {
			e.printStackTrace();
			js.put("error", "错误：服务器异常！");
			js.put("msg", "");
			return js;
		}
		JSONObject fs=new JSONObject();
		fs.put("filename", filename);
		fs.put("filepath", "/upload/"+FileType.getFileType(upload, suffixname)+"/"+date+"/"+fname);
		fs.put("filesize", StringUtil.getFileSize(len, 3));
		js.put("error", "");
		js.put("msg", fs);
		return js;

	}
	
	/**
	 * 多个文件上传
	 */
	@At
	@Ok("raw")
	@AdaptBy(type = UploadAdaptor.class, args = "ioc:upload")
	public JSONObject uploadMoreSave(@Param("f") TempFile[] tmpFileArr ,@Param("filetype") String filetype,HttpServletRequest req,HttpServletResponse resp,AdaptorErrorContext errCtx) {
		JSONObject js=new JSONObject();
		if (errCtx != null) {
			Throwable t=errCtx.getAdaptorErr();
			return errorMsg(t);
		}
		for(TempFile tmpFile:tmpFileArr){
			if (tmpFile == null || tmpFile.getFile().length() < 10) {
				js.put("error", "错误：文件大小不可小于10B！");
				js.put("msg", "");
				return js;
			}
			String filename=tmpFile.getMeta().getFileLocalName();
			File file = tmpFile.getFile();
			String suffixname=Files.getSuffixName(file).toLowerCase();
			String ss=FileType.getSuffixname(upload, filetype);
			if(ss.indexOf(suffixname)<0){
				js.put("error", "错误：不允许的文件扩展名，允许："+ss);
				js.put("msg", "");
				return js;
			}

			long len=tmpFile.getFile().length();

			filename=filename.substring(0,filename.lastIndexOf("."))+"."+suffixname;
			String date = DateUtil.getToday();
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			String fname= uuid+ "." + Files.getSuffixName(file).toLowerCase();
			String dest = webPath(date, fname, suffixname);
			try {
				Files.move(file, new File(dest));
			} catch (IOException e) {
				e.printStackTrace();
				js.put("error", "错误：服务器异常！");
				js.put("msg", "");
				return js;
			}
			JSONObject fs=new JSONObject();
			fs.put("filename", filename);
			fs.put("filepath", "/upload/"+FileType.getFileType(upload, suffixname)+"/"+date+"/"+fname);
			fs.put("filesize", StringUtil.getFileSize(len, 3));
			js.put("error", "");
			js.put("msg", fs);
		}
		return js;
	}
	
	/**
	 * 数据交换单个文件的保存操作
	 * @param tmpFile
	 * @param filetype
	 * @param req
	 * @param errCtx
	 * @return
	 */
	@At
	@Ok("raw")
	@AdaptBy(type = UploadAdaptor.class, args = "ioc:upload")
	public JSONObject uploadSaveForSjjh(@Param("f") TempFile tmpFile ,@Param("filetype") String filetype,HttpServletRequest req,AdaptorErrorContext errCtx) {

		JSONObject js=new JSONObject();
		if (errCtx != null) {
			Throwable t=errCtx.getAdaptorErr();
			return errorMsg(t);

		}
		if (tmpFile == null || tmpFile.getFile().length() < 10) {
			js.put("error", "错误：文件大小不可小于10B！");
			js.put("msg", "");
			return js;

		}
		String filename=tmpFile.getMeta().getFileLocalName();
		File file = tmpFile.getFile();
		String suffixname=Files.getSuffixName(file).toLowerCase();
		if("xls".indexOf(suffixname)<0){
			js.put("error", "错误：不允许的文件扩展名，允许：xls");
			js.put("msg", "");
			return js;
		}

		long len=tmpFile.getFile().length();

		filename=filename.substring(0,filename.lastIndexOf("."))+"."+suffixname;
		String date = DateUtil.getToday();
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String fname= uuid+ "." + Files.getSuffixName(file).toLowerCase();
		String dest = webPath(date, fname, suffixname).replace(FileType.getFileType(upload,suffixname), "sjjh");
		try {
			Files.move(file, new File(dest));
		} catch (IOException e) {
			e.printStackTrace();
			js.put("error", "错误：服务器异常！");
			js.put("msg", "");
			return js;
		}
		JSONObject fs=new JSONObject();
		fs.put("filename", filename);
		fs.put("filepath", "/upload/sjjh/"+date+"/"+fname);
		fs.put("filesize", StringUtil.getFileSize(len, 3));
		js.put("error", "");
		js.put("msg", fs);
		return js;

	}
	/**
	 * 获取上传路径，根据文件类型+日期生成路径
	 * @param date
	 * @param fname
	 * @param suffixname
	 * @return
	 */
	public String webPath(String date, String fname, String suffixname) {
		String newfilepath = Mvcs.getServletContext().getRealPath(
				"/upload/" + FileType.getFileType(upload,suffixname) + "/" + date
						+ "/");
		Files.createDirIfNoExists(newfilepath);
		return newfilepath + "\\" + fname;
	}
	/**
	 * 根据异常提示错误信息
	 * @param t
	 * @return
	 */
	private JSONObject errorMsg(Throwable t){
		JSONObject msg=new JSONObject();
		String className=t.getClass().getSimpleName();
		if(className.equals("UploadUnsupportedFileNameException")){
			String name=upload.getContext().getNameFilter();
			msg.put("error", "错误：无效的文件扩展名，支持的扩展名："+name.substring(name.indexOf("(")+1,name.lastIndexOf(")")).replace("|", ","));
			msg.put("msg", "");
		}else if(className.equals("UploadUnsupportedFileTypeException")){
			msg.put("error", "错误：不支持的文件类型！");
			msg.put("msg", "");
		}else if(className.equals("UploadOutOfSizeException")){
			msg.put("error", "错误：文件超出"+StringUtil.getFileSize(upload.getContext().getMaxFileSize(), 2)+"MB");
			msg.put("msg", "");
		}else if(className.equals("UploadStopException")){
			msg.put("error", "错误：上传中断！");
			msg.put("msg", "");
		}else{
			msg.put("error", "错误：未知错误！");
			msg.put("msg", "");
		}
		return msg;
	}
}
