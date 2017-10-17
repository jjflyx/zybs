package com.hits.modules.cgtable.util;



import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hits.modules.cgtable.bean.FormField;
import com.hits.modules.cgtable.bean.FormTable;
import com.hits.util.DateUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 
 *  #(c) IFlytek cform <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: Java生成实体类
 * 
 *  <br/>创建说明: 2016年2月2日 上午9:51:08 (☆笑死宝宝了☆)  创建文件<br/>
 * 
 *  修改历史:<br/>
 *
 */
public class GenEntityUtil {
	
	
	/**
	 * 功能描述:创建实体.java文件
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月2日 下午4:01:53
	 * 
	 * @param table
	 * @param filepath  实体路径D:\JavaIDE\Hits\WorkSpace\cform\com\hits\modules\bean\
	 * @return
	 */
	 public static boolean CreateEntiy(FormTable table,String filepath) {
	       boolean flag=false;
	        String repath="";
	        try {
	            // 获取 模板文件
	        	Template t=getConfig("/com/hits/modules/cgtable/template").getTemplate("table_bean.ftl");

	            Map<String, Object> root = createDataModel(table);
	            
	            
	            //  合并 模板 和 数据模型
	            String className=table.getTableName();
	            className= className.substring(0, 1).toUpperCase() + className.substring(1);
	            filepath=filepath+CommonStaticUtil.PACKAGE_OUT_PATH;
	        	File javaFile=toJavaFilename(filepath, className);//创建实体类文件
	        	
	            if (javaFile != null) {
	                Writer javaWriter = new FileWriter(javaFile);
	                t.process(root, javaWriter);
	                javaWriter.flush();
	                javaWriter.close();
	            }

	            flag=true;
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (TemplateException e) {
	            e.printStackTrace();
	        }
	        finally {
	            System.out.println((flag ? "成功:" : "失败:") + repath);
	        }
	        return flag;
	    }
	 
	/**
	 * 功能描述:设置实体数据
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月2日 下午2:48:45
	 *
	 */
	public static Map<String, Object> createDataModel(FormTable table){
		Map<String, Object> root1 = new HashMap<String, Object>();
		
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("packageName", CommonStaticUtil.PACKAGE_OUT_PATH);//包名
		root.put("tableName", table.getTableName().toUpperCase());//包名(大写)
		String className=table.getTableName();
		className= className.substring(0, 1).toUpperCase() + className.substring(1);
		root.put("className", className);//包名(大写)
		root.put("createDate", DateUtil.getCurDateTime());//类创建时间
		
		
		List<FormField> listField=table.getColumns();
		if (listField.size()>0) {
			List<Map<String, Object>> list=new ArrayList<Map<String, Object>>();
			for (int i = 0; i < listField.size(); i++) {
				FormField filed=listField.get(i);
				//添加字段属性
				Map<String, Object> rootChild = new HashMap<String, Object>();
				rootChild.put("isPrimary", filed.getIsPrimary());//是否是主键（主键采用uuid方式）
				rootChild.put("filedType", getFiledType(filed.getFieldType()));//字段类型
				rootChild.put("filedName", filed.getFieldName());//字段类型
				
				list.add(rootChild);
			}
			root.put("fileds", list);
		}
 		
		root1.put("entity", root);
		return root1;
	}
	
	 /**
     * 创建.java文件所在路径 和 返回.java文件File对象
     *
     * @param outDirFile    生成文件路径
     * @param javaPackage   java包名
     * @param javaClassName java类名
     * @return
     */
    private static File toJavaFilename(String javaPackage, String javaClassName) {
        String packageSubPath = javaPackage.replace('.', '/');
        File packagePath = new File(packageSubPath);
        if (!packagePath.exists()) {
            packagePath.mkdirs();
        }
        File file = new File(packagePath, javaClassName + ".java");
       
        return file;
    }
	
	/**
	 * 模版配置
	 * @param resource
	 * @return
	 */
	private static Configuration getConfig(String resource) {

		Configuration cfg = new Configuration();
//		cfg.setDefaultEncoding("UTF-8");
		cfg.setClassForTemplateLoading(GenEntityUtil.class, resource);
		return cfg;
	}
	
	
	/**
	 * 功能描述:获取字段类型
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月1日 下午4:21:56
	 * 
	 * @param type
	 * @return
	 */
	private static String getFiledType(String type) {
		String result ="";
		
		type=type.toUpperCase();
		
		
		if ("VARCHAR2".equals(type) || "VARCHAR".equals(type)) {
			result=" String ";
		}else if("NUMBER".equals(type) || "int".equals(type)){
			result=" int ";
		}
		return result;
	}
	
}
