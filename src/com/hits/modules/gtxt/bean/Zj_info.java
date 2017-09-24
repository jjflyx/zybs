package com.hits.modules.gtxt.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

/**
* @author Numbgui
* @time   2016-03-12 16:18:54
*/
@Table("ZJ_INFO")
public class Zj_info 
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
	private String code;
	@Column
	private String tel;
	@Column
	private String phone;
	@Column
	private String zgzh;
	@Column
	private String djh;
	@Column
	private String unit;
	@Column
	private String actor;
	@Column
	private String add_time;
	@Column
	private int type;
	@Column
	private int state;
	@Column
	private int sex;
	@Column
	private int xyzt_id;

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

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
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code=code;
	}
	public String getTel()
	{
		return tel;
	}
	public void setTel(String tel)
	{
		this.tel=tel;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone=phone;
	}
	public String getZgzh()
	{
		return zgzh;
	}
	public void setZgzh(String zgzh)
	{
		this.zgzh=zgzh;
	}
	public String getDjh()
	{
		return djh;
	}
	public void setDjh(String djh)
	{
		this.djh=djh;
	}
	public String getUnit()
	{
		return unit;
	}
	public void setUnit(String unit)
	{
		this.unit=unit;
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
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type=type;
	}

	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getXyzt_id() {
		return xyzt_id;
	}
	public void setXyzt_id(int xyzt_id) {
		this.xyzt_id = xyzt_id;
	}
}