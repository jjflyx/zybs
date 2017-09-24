    <%@ page language="java" pageEncoding="UTF-8"%>
    <%@ page import="com.hits.util.Uploader" %>
    <%
    	request.setCharacterEncoding("UTF-8");
    	response.setCharacterEncoding("UTF-8");
    	String url = request.getParameter("upfile");

    	 Uploader up = new Uploader(request);
    
    	 
        up.setSavePath("upload"); //保存路径
        String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"};  //允许的文件类型
        up.setUrl(url);
        up.setAllowFiles(fileType);
   	 	up.uploadRemteImage();
        
   	   	response.getWriter().print("{'url':'" + up.getUrl() + "','tip':'"+up.getState()+"','srcUrl':'" + url + "'}" );
    %>
