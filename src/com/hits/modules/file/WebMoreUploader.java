package com.hits.modules.file;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.nutz.lang.Files;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.upload.UploadAdaptor;

import com.hits.modules.com.comUtil;
import com.hits.util.EmptyUtils;

import sun.misc.BASE64Decoder;

/**
 * UEditor文件上传辅助类
 * 
 */
public class WebMoreUploader {
	
	// 文件大小常量, 单位kb
	private static final int MAX_SIZE = 500 * 1024;

	private HttpServletRequest request = null;

	// 保存路径
	private String savePath = "";
	// 文件允许格式
	private String[] allowFiles = { ".rar", ".doc", ".docx", ".zip", ".pdf",".txt", ".swf", ".wmv", ".gif", ".png", ".jpg", ".jpeg", ".bmp" };
	// 文件大小限制，单位Byte
	private long maxSize = 0;

	private HashMap<String, String> errorInfo = new HashMap<String, String>();
	
	private Map<String, String> params = null;

	public static final String ENCODEING = System.getProperties().getProperty("file.encoding");
	
	public  List<UploadParam> uploadlist=null;
	

	public WebMoreUploader(HttpServletRequest request) {
		this.params = new HashMap<String, String>();
		this.request = request;
		uploadlist=new ArrayList<UploadParam>();
		this.setMaxSize(WebMoreUploader.MAX_SIZE);

		HashMap<String, String> tmp = this.errorInfo;
		tmp.put("SUCCESS", "SUCCESS"); // 默认成功
		// 未包含文件上传域
		tmp.put("NOFILE","\\u672a\\u5305\\u542b\\u6587\\u4ef6\\u4e0a\\u4f20\\u57df");
		// 不允许的文件格式
		tmp.put("TYPE","\\u4e0d\\u5141\\u8bb8\\u7684\\u6587\\u4ef6\\u683c\\u5f0f");
		// 文件大小超出限制
		tmp.put("SIZE","\\u6587\\u4ef6\\u5927\\u5c0f\\u8d85\\u51fa\\u9650\\u5236");
		// 请求类型错误
		tmp.put("ENTYPE", "\\u8bf7\\u6c42\\u7c7b\\u578b\\u9519\\u8bef");
		// 上传请求异常
		tmp.put("REQUEST", "\\u4e0a\\u4f20\\u8bf7\\u6c42\\u5f02\\u5e38");
		// 未找到上传文件
		tmp.put("FILE", "\\u672a\\u627e\\u5230\\u4e0a\\u4f20\\u6587\\u4ef6");
		// IO异常
		tmp.put("IO", "IO\\u5f02\\u5e38");
		// 目录创建失败
		tmp.put("DIR", "\\u76ee\\u5f55\\u521b\\u5efa\\u5931\\u8d25");
		// 未知错误
		tmp.put("UNKNOWN", "\\u672a\\u77e5\\u9519\\u8bef");

		this.parseParams();

	}

	public void upload() throws Exception{
		
		boolean isMultipart = ServletFileUpload.isMultipartContent(this.request);
		if (!isMultipart) {
			return;
		}
		for (UploadParam param : uploadlist) {
			if (EmptyUtils.isEmpty(param.getFileItem()) || EmptyUtils.isEmpty(param.getFileItem().getInputStream())) {
				param.setState(this.errorInfo.get("FILE"));
				continue;
			}
			// 存储title
			param.setTitle(param.getParams().get("pictitle"));
			try {
				String originalName=param.getOriginalName();
				this.savePath=FileAction.getFiletype(originalName.substring(originalName.lastIndexOf(".")+1));
				String savePath = this.getFolder(this.savePath);
				
				if (savePath.equals("DIR")) {
					param.setState(this.errorInfo.get("DIR"));
					continue;
				}

				if (!this.checkFileType(param.getOriginalName())) {
					param.setState(this.errorInfo.get("TYPE"));
					continue;
				}
				param.setFileName(this.getName(param.getOriginalName()));
				param.setType(this.getFileExt(param.getFileName()));
				param.setUrl("/"+savePath + "/" + param.getFileName());
				
				param.getFileItem().write(new File(this.getPhysicalPath(param.getUrl())));

				param.setState(this.errorInfo.get("SUCCESS"));
			} catch (Exception e) {
				e.printStackTrace();
				param.setState(this.errorInfo.get("IO"));
			}
		}
		

	}


	
	/**
	 * 文件类型判断
	 * 
	 * @param fileName
	 * @return
	 */
	private boolean checkFileType(String fileName) {
		Iterator<String> type = Arrays.asList(this.allowFiles).iterator();
		while (type.hasNext()) {
			String ext = type.next();
			if (fileName.toLowerCase().endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @return string
	 */
	private String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	
	private void parseParams() {

		DiskFileItemFactory dff = new DiskFileItemFactory();
		
		try {
			ServletFileUpload sfu = new ServletFileUpload(dff);
			sfu.setSizeMax(this.maxSize);
			sfu.setHeaderEncoding(WebMoreUploader.ENCODEING);

			List<FileItem> list = sfu.parseRequest(request);
			Iterator<FileItem> iter = list.iterator(); 
			while (iter.hasNext()) {
				 FileItem item = iter.next();  
				 //保存当前文件信息
				 UploadParam uploadParam=new UploadParam();
				 uploadParam.setFileItem(item);
					// 普通参数存储
					if (item.isFormField()) {
						
						params.put(item.getFieldName(),this.getParameterValue( item.getInputStream()));
						//uploadParam.setParams(params);
					} else {
						
						//获取文件大小
					 	uploadParam.setSize(item.getSize()+"");
					 	//获取文件
						uploadParam.setOriginalName(item.getName());
						System.out.println("fieldNmae:"+item.getFieldName());
						
						uploadlist.add(uploadParam);
					}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public String getParameter(String name) {

		return this.params.get(name);

	}

	/**
	 * 依据原始文件名生成新文件名
	 * 
	 * @return
	 */
	private String getName(String fileName) {
//		Random random = new Random();
//		return this.fileName = "" + random.nextInt(10000)
//				+ System.currentTimeMillis() + this.getFileExt(fileName);
		return UUID.randomUUID().toString().replaceAll("-", "")+ this.getFileExt(fileName);
	}

	/**
	 * 根据字符串创建本地目录 并按照日期建立子目录返回
	 * 
	 * @param path
	 * @return
	 */
	private String getFolder(String path) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
		path += "/" + formater.format(new Date());
		File dir = new File(this.getPhysicalPath(path));
		if (!dir.exists()) {
			try {
				dir.mkdirs();
			} catch (Exception e) {
				return "DIR";
			}
		}
		return path;
	}

	/**
	 * 根据传入的虚拟路径获取物理路径
	 * 
	 * @param path
	 * @return
	 */
	private String getPhysicalPath(String path) {
		/*String servletPath = this.request.getServletPath();
		String realPath = this.request.getSession().getServletContext()
				.getRealPath(servletPath);
		return new File(realPath).getParent() + "/" + path;*/
		return comUtil.filepath + "/" + path;
	}

	/**
	 * 从输入流中获取字符串数据
	 * 
	 * @param in
	 *            给定的输入流， 结果字符串将从该流中读取
	 * @return 从流中读取到的字符串
	 */
	private String getParameterValue(InputStream in) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String result = "";
		String tmpString = null;

		try {

			while ((tmpString = reader.readLine()) != null) {
				result += tmpString;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;

	}

	private byte[] getFileOutputStream(InputStream in) {

		try {
			return IOUtils.toByteArray(in);
		} catch (IOException e) {
			return null;
		}

	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public void setAllowFiles(String[] allowFiles) {
		this.allowFiles = allowFiles;
	}

	public void setMaxSize(long size) {
		this.maxSize = size * 1024;
	}

	
}