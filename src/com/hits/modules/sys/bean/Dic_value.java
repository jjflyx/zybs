package com.hits.modules.sys.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Name;
/**
* @author frigyes
* @time   2015-06-30 19:29:25
*/
@Table("DIC_VALUE")
public class Dic_value 
{
	@Column
	@Name
	private String id;
	@Column
	private String typeid;
	@Column
	private String flcode;
	@Column
	private String name;
	@Column
	private String code;
	@Column
	private String value;
	@Column
	private String pic;
	@Column
	private int state;
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
	public String getTypeid()
	{
		return typeid;
	}
	public void setTypeid(String typeid)
	{
		this.typeid=typeid;
	}
	public String getFlcode()
	{
		return flcode;
	}
	public void setFlcode(String flcode)
	{
		this.flcode=flcode;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code=code;
	}
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value=value;
	}
	public String getPic()
	{
		return pic;
	}
	public void setPic(String pic)
	{
		this.pic=pic;
	}
	public int getState()
	{
		return state;
	}
	public void setState(int state)
	{
		this.state=state;
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