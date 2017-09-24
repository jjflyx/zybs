package com.hits.modules.gtxt.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

/**
* @author Numbgui
* @time   2016-05-18 10:07:11
*/
@Table("L_CKQHT_YW")
public class L_ckqht_yw 
{
	@Column
	private String sfwc;
	@Column
	private String sjlxrq_s;
	@Column
	private Float sjlxx_s;
	@Column
	private String ylxrq_y;
	@Column
	private Double ylxx_y;
	@Column
	private String fk_htid;
	@Column
	@Name
	@Prev({
			@SQL(db = DB.ORACLE, value="SELECT sys_guid() FROM dual")
	})
	private String ywid;
	@Column
	private int is_warn;
	@Column
	private String add_time;
		public String getSfwc()
	{
		return sfwc;
	}
	public void setSfwc(String sfwc)
	{
		this.sfwc=sfwc;
	}
	public String getSjlxrq_s()
	{
		return sjlxrq_s;
	}
	public void setSjlxrq_s(String sjlxrq_s)
	{
		this.sjlxrq_s=sjlxrq_s;
	}
	public Float getSjlxx_s()
	{
		return sjlxx_s;
	}
	public void setSjlxx_s(Float sjlxx_s)
	{
		this.sjlxx_s=sjlxx_s;
	}
	public String getYlxrq_y()
	{
		return ylxrq_y;
	}
	public void setYlxrq_y(String ylxrq_y)
	{
		this.ylxrq_y=ylxrq_y;
	}
	public double getYlxx_y()
	{
		return ylxx_y;
	}
	public void setYlxx_y(Double ylxx_y)
	{
		this.ylxx_y=ylxx_y;
	}
	public String getFk_htid()
	{
		return fk_htid;
	}
	public void setFk_htid(String fk_htid)
	{
		this.fk_htid=fk_htid;
	}
	public String getYwid()
	{
		return ywid;
	}
	public void setYwid(String ywid)
	{
		this.ywid=ywid;
	}
	public int getIs_warn()
	{
		return is_warn;
	}
	public void setIs_warn(int is_warn)
	{
		this.is_warn=is_warn;
	}
	public String getAdd_time()
	{
		return add_time;
	}
	public void setAdd_time(String add_time)
	{
		this.add_time=add_time;
	}

}