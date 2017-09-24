package com.hits.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import com.hits.common.config.Globals;
import com.hits.common.util.DecodeUtil;



public class DownFileUtil extends javax.servlet.GenericServlet {
    /**
     * 下载文件，默认为电子表格，其内容要在htmlbody文本域中指定。其文件存放在服务器c:下。文件名为temp.xls若是其它类型文件要在type中指定
     *
     * @param req
     * @param rep
     */
    public synchronized void service(ServletRequest req, ServletResponse rep) {
        try {
            HttpServletRequest request = (HttpServletRequest) req;
            request.setCharacterEncoding("utf-8");
            String htmlbody =request.getParameter("htmlbody");
            String fileName = StringUtil.isNull(request.getParameter("filename"), "统计报表");
            String type = request.getParameter("type");
            if (type == null) type = "xls";
            htmlbody = htmlbody.replaceAll("border=\"0\"", "border=\"1\"");
            htmlbody = htmlbody.replaceAll("border=0", "border=1");
            htmlbody = htmlbody.replaceAll("border='0'", "border='1'");
            StringBuffer temp = new StringBuffer();
            temp.append("<html>\n");
            temp.append("<meta http-equiv=\"Content-Type\" content=\"APPLICATION/OCTET-STREAM; charset=gbk\">");
            temp.append("<body>\n");
            temp.append(htmlbody + "\n");
            temp.append("</body>\n");
            temp.append("</html>\n");
            String content = temp.toString();
            String filepath = Globals.APP_BASE_PATH+ "\\WEB-INF\\temp\\temp." + type;
            FileUtil.setFileCnt(filepath, content);
            javax.servlet.http.HttpServletResponse response = (HttpServletResponse) rep;
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("APPLICATION/OCTET-STREAM;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=\""+ URLEncoder.encode(fileName, "utf-8").replace("+", " ")+"." + type + "\"");
            File f = new File(filepath);
            BufferedInputStream bufferedInput = new BufferedInputStream(new FileInputStream(f));
            byte[] buffer = new byte[1024];
            int i;
            while ((i = bufferedInput.read(buffer)) != -1) {
                out.write(buffer, 0, i);
            }
            bufferedInput.close();
            out.flush();
            out.close();
        }
        catch (Exception e) {
           
            e.printStackTrace();
        }
    }

   

}

