<%@ page language="java" pageEncoding="UTF-8"%>
 <%@ page import="com.hits.util.Uploader" %>
<% 
	 Uploader up = new Uploader(request);
	 
     up.setSavePath("upload"); //保存路径
     String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"};  //允许的文件类型
     up.setAllowFiles(fileType);
     String imgStr = up.getImageManager();
    
	out.print(imgStr);		
%>
