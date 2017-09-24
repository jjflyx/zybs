package com.hits.modules.sys.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
/**
* @author wanfly
* @time   2014-02-28 17:24:36
*/
@Table("SYS_TEMPLATECONFIG")
public class Sys_templateconfig 
{
	@Column
	private String id;
	@Column
	private String name;
	@Column
	private String des;
	@Column
	private int state;
	@Column
	private String ext2;
	@Column
	private int location;
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
	public String getDes()
	{
		return des;
	}
	public void setDes(String des)
	{
		this.des=des;
	}
	public int getState()
	{
		return state;
	}
	public void setState(int state)
	{
		this.state=state;
	}
	public String getExt2()
	{
		return ext2;
	}
	public void setExt2(String ext2)
	{
		this.ext2=ext2;
	}
	public int getLocation()
	{
		return location;
	}
	public void setLocation(int location)
	{
		this.location=location;
	}

}