package com.hits.modules.quartz;

import java.util.Collection;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.ioc.Ioc;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.modules.com.comUtil;
import com.hits.modules.quartz.bean.Quartz_task;
import com.hits.util.EmptyUtils;
@IocBean
@Filters({ @By(type = GlobalsFilter.class)})
public class QuartzTimeAction extends BaseAction{
//	private static Logger logger = Logger.getLogger(QuartzTimeAction.class);
	private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
	@Inject
	protected Dao dao;
	
	/**
	 * 功能描述:初始化执行定时任务
	 *
	 * @author (☆笑死宝宝了☆)  2015年10月25日 下午9:58:40
	 * 
	 * @throws SchedulerException
	 */
	public void initQuartzTask() throws SchedulerException {
		
		Ioc ioc=Mvcs.getIoc();
		
		Scheduler scheduler = schedulerFactory.getScheduler();
		// 可执行的任务列表
		String sqlstr="select * from quartz_task where status=0 ";
		if(EmptyUtils.isNotEmpty(comUtil.taskGroup)){
			sqlstr+=" and group_id = '"+comUtil.taskGroup+"'";
		}
		Collection<Quartz_task> taskList =daoCtl.list(dao,Quartz_task.class, Sqls.create(sqlstr));
		System.out.println("★★★★★Quartz在项目启动时候初始化了[共有"+taskList.size()+"个任务需要重新加载]★★★★★");
		for (Quartz_task task : taskList) {
			// 任务名称和任务组设置规则：
			TriggerKey triggerKey = TriggerKey.triggerKey(task.getTask_id(), task.getGroup_id());
			
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			// 不存在，创建一个
			if (null == trigger) {
				
				System.out.println("★★★★★我是Quarztz:["+task.getTask_id()+" - "+ task.getGroup_id()+"],我在初始化时被重新构建了★★★★★");
				JobDetail jobDetail = JobBuilder.newJob(QuartzExecute.class).withIdentity(task.getTask_id(), task.getGroup_id()).build();
				jobDetail.getJobDataMap().put("scheduleJob", task);
//				jobDetail.getJobDataMap().put("ioc", ioc);
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCron_expression());
				// 按新的表达式构建一个新的trigger
				trigger = TriggerBuilder.newTrigger().withIdentity(task.getTask_id(), task.getGroup_id()).withSchedule(scheduleBuilder).build();
				scheduler.scheduleJob(jobDetail, trigger);
				scheduler.start();
			} else {
				System.out.println("★★★★★我是Quarztz:["+task.getTask_id()+" - "+ task.getGroup_id()+"],我在初始化时被重新✈更新了表达式★★★★★");
				// trigger已存在，则更新相应的定时设置
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCron_expression());
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
				scheduler.start();
			}
		}
	}
	
	/**
	 * 功能描述:手动创建新的 定时任务
	 *
	 * @author (☆笑死宝宝了☆)  2015年10月25日 下午9:58:52
	 * 
	 * @param task
	 * @return
	 * @throws SchedulerException 
	 */
	public static void newQuartzCorn(Quartz_task task ) throws SchedulerException{
		Scheduler scheduler;
			Ioc ioc=Mvcs.getIoc();
			scheduler = schedulerFactory.getScheduler();
			
			System.out.println("★★★★★我是手动新建Quarztz:["+task.getTask_id()+" - "+ task.getGroup_id()+"]★★★★★");
			JobDetail jobDetail = JobBuilder.newJob(QuartzExecute.class).withIdentity(task.getTask_id(), task.getGroup_id()).build();
			jobDetail.getJobDataMap().put("scheduleJob", task);
//			jobDetail.getJobDataMap().put("ioc", ioc);
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCron_expression());
			// 按新的表达式构建一个新的trigger
			CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(task.getTask_id(), task.getGroup_id()).withSchedule(scheduleBuilder).build();
			scheduler.scheduleJob(jobDetail, trigger);
			scheduler.start();
			
	}
	
	/**
	 * 功能描述:手动删除定时任务
	 *
	 * @author (☆笑死宝宝了☆)  2015年10月25日 下午9:59:00
	 * 
	 * @param taskid
	 * @param groupid
	 * @return
	 * @throws SchedulerException 
	 */
	public static void delQuartzCorn(String taskid,String groupid) throws SchedulerException{
		Scheduler scheduler = schedulerFactory.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(taskid, groupid);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (trigger!=null) {
				System.out.println("★★★★★手动删除了Quarztz:["+taskid+" - "+ groupid+"]★★★★★");
				JobKey jobKey = JobKey.jobKey(taskid, groupid);
				scheduler.deleteJob(jobKey);
			}else{
				System.out.println("★★★★★手动删除定时任务-Quarztz:["+taskid+" - "+ groupid+"],不存在★★★★★");
			}
			
	}

}
