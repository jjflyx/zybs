package com.hits.modules.tasks.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

/**
* @author Numbgui
* @time   2016-03-17 17:15:06
*/
@Table("TASKS")
public class Tasks 
{
	@Column
	@Name
    @Prev({
            @SQL(db = DB.ORACLE, value="SELECT sys_guid() FROM dual")
    })
	private String id;
	@Column
	private String name;
	@Column
	private String file_path;
	@Column
	private String run_time;
	@Column
	private String actor;
	@Column
	private String add_time;
	@Column
	private int is_work;
	@Column
	private Integer is_rule;

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getFile_path()
	{
		return file_path;
	}
	public void setFile_path(String file_path)
	{
		this.file_path=file_path;
	}
	public String getRun_time()
	{
		return run_time;
	}
	public void setRun_time(String run_time)
	{
		this.run_time=run_time;
	}
	public String getActor()
	{
		return actor;
	}
	public void setActor(String actor)
	{
		this.actor=actor;
	}
	public String getAdd_time()
	{
		return add_time;
	}
	public void setAdd_time(String add_time)
	{
		this.add_time=add_time;
	}
	public int getIs_work()
	{
		return is_work;
	}
	public void setIs_work(int is_work)
	{
		this.is_work=is_work;
	}

	public Integer getIs_rule() {
		return is_rule;
	}

	public void setIs_rule(Integer is_rule) {
		this.is_rule = is_rule;
	}
}