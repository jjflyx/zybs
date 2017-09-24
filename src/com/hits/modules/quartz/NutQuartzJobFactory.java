package com.hits.modules.quartz;

import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.quartz.Job;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.simpl.SimpleJobFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
@IocBean
public class NutQuartzJobFactory implements JobFactory {

	private static final Log log = Logs.get();

	protected SimpleJobFactory simple = new SimpleJobFactory();

	public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
		try {
			
			return Mvcs.ctx.getDefaultIoc().get(bundle.getJobDetail().getJobClass());
		}
		catch (Exception e) {
			log.warn("Not ioc bean? fallback to SimpleJobFactory", e);
		}
		return simple.newJob(bundle, scheduler);
	}

}