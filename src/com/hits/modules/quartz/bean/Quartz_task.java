package com.hits.modules.quartz.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author 
* @time   2015-10-25 20:59:22
*/
@Table("QUARTZ_TASK")
public class Quartz_task 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT QUARTZ_TASK_S.nextval FROM dual")
	})
	private int id;
	@Column
	private String task_id;
	@Column
	private String group_id;
	@Column
	private String cron_expression;
	@Column
	private String run_class;
	@Column
	private String run_method;
	@Column
	private int status;

	@Column
	private String params;
	@Column
	private String bz;
	@Column
	private String filepath;

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public String getTask_id()
	{
		return task_id;
	}
	public void setTask_id(String task_id)
	{
		this.task_id=task_id;
	}
	public String getGroup_id()
	{
		return group_id;
	}
	public void setGroup_id(String group_id)
	{
		this.group_id=group_id;
	}
	public String getCron_expression()
	{
		return cron_expression;
	}
	public void setCron_expression(String cron_expression)
	{
		this.cron_expression=cron_expression;
	}
	public String getRun_method()
	{
		return run_method;
	}
	public void setRun_method(String run_method)
	{
		this.run_method=run_method;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRun_class() {
		return run_class;
	}
	public void setRun_class(String run_class) {
		this.run_class = run_class;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getBz() {
		return bz;
	}

	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
}