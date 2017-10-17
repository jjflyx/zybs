package com.hits.modules.sys.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Name;
/**
* @author frigyes
* @time   2015-06-30 19:29:25
*/
@Table("DIC_TYPE")
public class Dic_type 
{
	@Column
	@Name
	private String id;
	@Column
	private String pid;
	@Column
	private String code;
	@Column
	private String name;
	@Column
	private String bz;
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
	public String getPid()
	{
		return pid;
	}
	public void setPid(String pid)
	{
		this.pid=pid;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code=code;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getBz()
	{
		return bz;
	}
	public void setBz(String bz)
	{
		this.bz=bz;
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