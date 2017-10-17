package com.hits.modules.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author lxy
* @time   2015-05-22 14:58:17
*/
@Table("CS_TYPE")
public class Cs_type 
{
	@Column
	@Name
	private String id;
	@Column
	private String name;
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
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}

}