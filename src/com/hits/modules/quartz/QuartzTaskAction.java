package com.hits.modules.quartz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;
import org.quartz.SchedulerException;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.modules.com.comUtil;
import com.hits.modules.quartz.bean.Quartz_task;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;
import com.hits.util.PageObj;
import com.hits.util.SysLogUtil;

/**
 * 
 *  #(c) IFlytek hf_szrx <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: 任务管理类
 * 
 *  <br/>创建说明: 2015年10月25日 下午10:34:24 (☆笑死宝宝了☆)  创建文件<br/>
 * 
 *  修改历史:<br/>
 *
 */
@IocBean
@At("/private/quartz")
@Filters( { @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class QuartzTaskAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	/**
	 * 功能描述:前往任务列表
	 *
	 * @author (☆笑死宝宝了☆)  2015年10月25日 下午10:36:41
	 *
	 */
	@At
	@Ok("->:/private/quartz/quartz_list.html")
	public void toQuartzTaskList(HttpServletRequest req){
		req.setAttribute("yxMap", JSONObject.fromObject(comUtil.taskyxMap));
	}
	
	/**
	 * 功能描述:前往任务列表
	 *
	 * @author (☆笑死宝宝了☆)  2015年10月25日 下午10:36:41
	 *
	 */
	@At
	@Ok("raw")
	public String quartzTaskList(HttpServletRequest req, @Param("page") int curPage, @Param("rows") int pageSize){
		Sql sql = Sqls.create("select * from quartz_task where group_id = '"+comUtil.taskGroup+"' order by id desc");
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		return PageObj.getPageJSON(qr);
	}
	
	/**
	 * 功能描述:前往任务新增页面
	 *
	 * @author (☆笑死宝宝了☆)  2015年10月25日 下午10:36:41
	 *
	 */
	@At
	@Ok("->:/private/quartz/quartz_add.html")
	public void quartzAdd(){
		
	}
	
	/**
	 * 功能描述:新增任务
	 *
	 * @author (☆笑死宝宝了☆)  2015年10月25日 下午10:36:41
	 *
	 */
	@At
	@Ok("raw")
	public boolean saveQuartz(HttpSession session,@Param("..") final Quartz_task task) {
		final Sys_user user = (Sys_user) session.getAttribute("userSession");
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		try {
			Trans.exec(new Atom() {
				public void run() {
					if(EmptyUtils.isNotEmpty(comUtil.taskGroup)){
						task.setGroup_id(comUtil.taskGroup);
					}
					dao.insert(task);
					if (task.getStatus()==0) {
						try {
							QuartzTimeAction.newQuartzCorn(task);
						} catch (SchedulerException e) {
							e.printStackTrace();
							throw new RuntimeException("创建Quartz任务出错，手动抛出异常");
						}
					}
					SysLogUtil.addLog(dao, user, SysLogUtil.user_log_type, "新增了quartz任务，任务id为："+task.getTask_id());
					re.set(true);
				}
			});
		} catch (Exception e) {
			SysLogUtil.addLog(dao, user, SysLogUtil.user_log_type, "新增了quartz任务，出现错误："+e.getMessage());
			re.set(false);
		}
		return re.get();
	}
	
	/**
	 * 功能描述:前往任务新增页面
	 *
	 * @author (☆笑死宝宝了☆)  2015年10月25日 下午10:36:41
	 *
	 */
	@At
	@Ok("->:/private/quartz/quartz_update.html")
	public void toupdate(@Param("id")  int id,HttpServletRequest req){
		try {
			Quartz_task task=daoCtl.detailById(dao, Quartz_task.class, id);
			req.setAttribute("obj", task);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 功能描述:更新任务
	 *
	 * @author (☆笑死宝宝了☆)  2015年10月25日 下午10:36:41
	 *
	 */
	@At
	@Ok("raw")
	public boolean updateQuartz(HttpSession session,@Param("..") final Quartz_task task) {
		final Sys_user user = (Sys_user) session.getAttribute("userSession");
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		try {
			Trans.exec(new Atom() {
				public void run() {
					Quartz_task oldTask=daoCtl.detailById(dao, Quartz_task.class, task.getId());
					oldTask.setCron_expression(task.getCron_expression());
					oldTask.setRun_method(task.getRun_method());
					oldTask.setRun_class(task.getRun_class());
					oldTask.setStatus(task.getStatus());
					oldTask.setParams(task.getParams());
					oldTask.setFilepath(task.getFilepath());
					oldTask.setBz(task.getBz());
					if(EmptyUtils.isNotEmpty(comUtil.taskGroup)){
						oldTask.setGroup_id(comUtil.taskGroup);
					}else{
						oldTask.setGroup_id(task.getGroup_id());
					}
					dao.update(oldTask);
					//更新Quartz定时任务
					try {
						if (oldTask.getStatus()==0) {
							//先删除在重新添加
							QuartzTimeAction.delQuartzCorn(oldTask.getTask_id(), oldTask.getGroup_id());
							QuartzTimeAction.newQuartzCorn(oldTask);
						}else{
							//删除
							QuartzTimeAction.delQuartzCorn(oldTask.getTask_id(), oldTask.getGroup_id());
						}
						
					} catch (SchedulerException e) {
						e.printStackTrace();
						throw new RuntimeException("更新Quartz任务出错，手动抛出异常");
					}
					SysLogUtil.addLog(dao, user, SysLogUtil.user_log_type, "更新了quartz任务，任务id为："+task.getTask_id());
					re.set(true);
				}
			});
		} catch (Exception e) {
			SysLogUtil.addLog(dao, user, SysLogUtil.user_log_type, "更新了quartz任务，出现错误："+e.getMessage());
			re.set(false);
		}
		return re.get();
	}
	
	/**
	 * 功能描述:删除任务
	 *
	 * @author (☆笑死宝宝了☆)  2015年10月25日 下午10:36:41
	 *
	 */
	@At
	@Ok("raw")
	public boolean delQuartz(HttpSession session,@Param("id") final int id) {
		final Sys_user user = (Sys_user) session.getAttribute("userSession");
		final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
		try {
			Trans.exec(new Atom() {
				public void run() {
					Quartz_task task=daoCtl.detailById(dao, Quartz_task.class, id);
					//更新Quartz定时任务
					try {
						//先删除在重新添加
						QuartzTimeAction.delQuartzCorn(task.getTask_id(), task.getGroup_id());
					} catch (SchedulerException e) {
						e.printStackTrace();
						throw new RuntimeException("删除Quartz任务出错，手动抛出异常");
					}
					dao.delete(Quartz_task.class, id);
				
					SysLogUtil.addLog(dao, user, SysLogUtil.user_log_type, "删除了quartz任务");
					re.set(true);
				}
			});
		} catch (Exception e) {
			SysLogUtil.addLog(dao, user, SysLogUtil.user_log_type, "删除quartz任务，出现错误："+e.getMessage());
			re.set(false);
		}
		return re.get();
	}
	
	/**
	 * 功能描述:验证任务id是否重复
	 *
	 * @author (☆笑死宝宝了☆)  2015年10月25日 下午10:36:41
	 *
	 */
	@At
	@Ok("raw")
	public boolean checkTaskid(HttpSession session,@Param("taskid") String taskid) {
		
		try {
			String sql="select count(*) from quartz_task where task_id='"+taskid+"'";
			int count=daoCtl.getIntRowValue(dao, Sqls.create(sql));
			if (count>0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
