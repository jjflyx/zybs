package com.hits.modules.quartz;

import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.hits.common.action.BaseAction;
import com.hits.common.dao.ObjectCtl;
import com.hits.modules.com.comUtil;
import com.hits.modules.quartz.bean.Quartz_task;
import com.hits.util.DateUtil;
import com.hits.util.EmptyUtils;
@IocBean
public class QuartzExecute extends BaseAction implements Job {
	
	protected Dao dao=Mvcs.ctx.getDefaultIoc().get(Dao.class);
	
//	private static Logger logger = Logger.getLogger(QuartzExecute.class);
	
	public synchronized void execute(JobExecutionContext context)throws JobExecutionException {
		String taskid="";
		String id="";
		try {
//			System.out.println("获取dao对象："+Mvcs.ctx.getDefaultIoc().get(Dao.class));
			Quartz_task task = (Quartz_task) context.getMergedJobDataMap().get("scheduleJob");
			
			//TODO 在这里执行你的任务...
			
			if (EmptyUtils.isNotEmpty(task)) {
//				Ioc ioc=(Ioc) context.getMergedJobDataMap().get("ioc");
//				Dao dao = ioc.get(Dao.class);
				
				taskid=task.getTask_id();
				id=task.getId()+"";
				String runclass=task.getRun_class();
				String method=task.getRun_method();
				System.out.println("★★★★★定时任务: [" + task.getTask_id() + "]，开始运行,时间["+DateUtil.getCurDateTime()+"]★★★★★");
				Object taskClass=Class.forName(runclass).newInstance();
				
				taskClass.getClass().getMethod(method, ObjectCtl.class,Dao.class,Quartz_task.class).invoke(taskClass,new Object[]{daoCtl,dao,task});
				
			}else{
				System.out.println("★★★★★定时任务:["+context.getJobDetail().getKey() +"] [scheduleJob]，为空★★★★★");
			}
		} catch (Exception e) {
			System.out.println("★★★★★定时任务: ["+taskid+ "]，运行出现错误:"+e.getMessage()+"★★★★★");
			e.printStackTrace();
		}finally{
			System.out.println("★★★★★定时任务: [" + taskid + "]，结束运行,时间["+DateUtil.getCurDateTime()+"]★★★★★");
			comUtil.taskyxMap.put(id, DateUtil.getCurDateTime());
		}

	}

}
