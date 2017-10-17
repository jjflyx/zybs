package com.hits.modules.com;


import org.nutz.dao.Dao;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.util.EnvUtil;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;

import com.hits.common.config.Globals;
import com.hits.common.dao.ObjectCtl;
import com.hits.modules.quartz.bean.Quartz_task;
import com.hits.util.EmptyUtils;

public class KettleTask{
	
	public void init(ObjectCtl daoCtl,Dao dao ,Quartz_task task){
		String param=task.getParams();
        try {  
        	if(EmptyUtils.isNotEmpty(task.getFilepath())){//无上传文件，不执行
        		String ktrPath=Globals.APP_BASE_PATH+task.getFilepath();//kettle文件位置
        		System.out.println("ktrPath:"+ktrPath);
        		if(task.getFilepath().endsWith("ktr")){
        			String[] params=param.split(";");//相关参数
        			runTransfer(ktrPath, params);//执行kettle转换
        		}else if(task.getFilepath().endsWith("kjb")){
        			runJob(ktrPath);//执行kettle任务
        		}
        	}
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
	/**  
     * 运行转换文件方法 
     * @param params 多个参数变量值 
     * @param ktrPath 转换文件的路径，后缀ktr 
     */
	public static void runTransfer(String ktrPath,String[] params) {
		Trans trans = null;  
		try {  
            // // 初始化  
            // 转换元对象  
            KettleEnvironment.init();// 初始化  
            EnvUtil.environmentInit();  
            TransMeta transMeta = new TransMeta(ktrPath);  
            // 转换  
            trans = new Trans(transMeta);  
            // 设置日志等级(debug非常详细，对于跟踪问题有帮助)
            //trans.setLogLevel(LogLevel.DEBUG);
            trans.setLogLevel(LogLevel.ROWLEVEL);
            // 执行转换  
            trans.execute(params);  
            // 等待转换执行结束  
            trans.waitUntilFinished();  
            // 抛出异常  
            if (trans.getErrors() > 0) {  
                throw new Exception(  
                        "There are errors during transformation exception!(传输过程中发生异常)");  
            }  
            System.out.println("end");
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
	/** 
     * java 调用 kettle 的job 
     *  
     * @param jobname 
     *            如： String fName= "D:\\kettle\\informix_to_am_4.ktr"; 
     */  
    public static void runJob(String jobPath) {  
        try {  
            KettleEnvironment.init();  
            // jobname 是Job脚本的路径及名称  
            JobMeta jobMeta = new JobMeta(jobPath, null);  
            Job job = new Job(null, jobMeta);  
            // 向Job 脚本传递参数，脚本中获取参数值：${参数名}  
            // job.setVariable(paraname, paravalue);  
//            job.setVariable("id", params[0]);  
//            job.setVariable("dt", params[1]);  
            job.setLogLevel(LogLevel.ROWLEVEL);//设置日志等级
            job.start();  
            job.waitUntilFinished();  
            if (job.getErrors() > 0) {  
                throw new Exception(  
                        "There are errors during job exception!(执行job发生异常)");  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
	public static void main(String[] args) {
		try {  
			String ktrPath="C:/Users/jinjianfeng/Desktop/国土/推送任务/3.kjb";
			String datetime = "2014-12-19 23:20:45";  
			String[] params = {"707", datetime}; // 传递参数
			runJob(ktrPath);
			System.out.println("end");
		} catch (Exception e) {  
			e.printStackTrace();  
		}  
	}
}