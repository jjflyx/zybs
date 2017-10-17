package com.hits.modules.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author lxy
* @time   2015-05-22 14:58:17
*/
@Table("CS_VALUE")
public class Cs_value 
{
	@Column
	@Id
	private int id;
	@Column
	private String typeid;
	@Column
	private String name;
	@Column
	private String code;
	@Column
	private String value;
	@Column
	private int location;
	@Column
	private int state;
	@Column
	private String bz;
	@Column
	private int ext1;
	@Column
	private int ext2;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
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
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public int getExt1() {
		return ext1;
	}
	public void setExt1(int ext1) {
		this.ext1 = ext1;
	}
	public int getExt2() {
		return ext2;
	}
	public void setExt2(int ext2) {
		this.ext2 = ext2;
	}

}