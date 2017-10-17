<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter,com.hits.modules.com.comUtil"
    pageEncoding="UTF-8"%>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = application.getRealPath( "/" );
	String filePath=comUtil.filepath;
	String action = request.getParameter("action");
	String result = new ActionEnter( request, rootPath ,filePath).exec();
	if( action!=null && 
	   (action.equals("listfile") || action.equals("listimage") ) ){
		System.out.println(rootPath);
	    rootPath = rootPath.replace("\\", "/");
	    result = result.replaceAll(rootPath, "/");//把返回路径中的物理路径替换为 '/'
	}
	out.write(result);
	
%>