package com.hits.modules.gtxt.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

/**
* @author Numbgui
* @time   2016-05-27 16:21:47
*/
@Table("L_TKQSYF")
public class L_tkqsyf 
{
	@Column
	@Name
	@Prev({
			@SQL(db = DB.ORACLE, value="SELECT sys_guid() FROM dual")
	})
	private String syid;
	@Column
	private String actor;
	@Column
	private String actor2;
	@Column
	private String add_time;
	@Column
	private String bjh;
	@Column
	private String fkfc;
	@Column
	private int jf_type;
	@Column
	private String jkje;
	@Column
	private int status;
	@Column
	private String unitid;
	@Column
	private String xkzh;
	@Column
	private String xycode;
	@Column
	private String xyname;
	@Column
	private String xyzt;
	@Column
	private String xzqh_unit;
	@Column
	private String zhlxrq;
	@Column
	private String zp_unit;
	@Column
	private int zsfws;
		public String getSyid()
	{
		return syid;
	}
	public void setSyid(String syid)
	{
		this.syid=syid;
	}
	public String getActor()
	{
		return actor;
	}
	public void setActor(String actor)
	{
		this.actor=actor;
	}
	public String getActor2()
	{
		return actor2;
	}
	public void setActor2(String actor2)
	{
		this.actor2=actor2;
	}
	public String getAdd_time()
	{
		return add_time;
	}
	public void setAdd_time(String add_time)
	{
		this.add_time=add_time;
	}
	public String getBjh()
	{
		return bjh;
	}
	public void setBjh(String bjh)
	{
		this.bjh=bjh;
	}
	public String getFkfc()
	{
		return fkfc;
	}
	public void setFkfc(String fkfc)
	{
		this.fkfc=fkfc;
	}
	public int getJf_type()
	{
		return jf_type;
	}
	public void setJf_type(int jf_type)
	{
		this.jf_type=jf_type;
	}
	public String getJkje()
	{
		return jkje;
	}
	public void setJkje(String jkje)
	{
		this.jkje=jkje;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status=status;
	}
	public String getUnitid()
	{
		return unitid;
	}
	public void setUnitid(String unitid)
	{
		this.unitid=unitid;
	}
	public String getXkzh()
	{
		return xkzh;
	}
	public void setXkzh(String xkzh)
	{
		this.xkzh=xkzh;
	}
	public String getXycode()
	{
		return xycode;
	}
	public void setXycode(String xycode)
	{
		this.xycode=xycode;
	}
	public String getXyname()
	{
		return xyname;
	}
	public void setXyname(String xyname)
	{
		this.xyname=xyname;
	}
	public String getXyzt()
	{
		return xyzt;
	}
	public void setXyzt(String xyzt)
	{
		this.xyzt=xyzt;
	}
	public String getXzqh_unit()
	{
		return xzqh_unit;
	}
	public void setXzqh_unit(String xzqh_unit)
	{
		this.xzqh_unit=xzqh_unit;
	}
	public String getZhlxrq()
	{
		return zhlxrq;
	}
	public void setZhlxrq(String zhlxrq)
	{
		this.zhlxrq=zhlxrq;
	}
	public String getZp_unit()
	{
		return zp_unit;
	}
	public void setZp_unit(String zp_unit)
	{
		this.zp_unit=zp_unit;
	}
	public int getZsfws()
	{
		return zsfws;
	}
	public void setZsfws(int zsfws)
	{
		this.zsfws=zsfws;
	}

}