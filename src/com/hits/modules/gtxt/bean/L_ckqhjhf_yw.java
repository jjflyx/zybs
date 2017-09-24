package com.hits.modules.gtxt.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

/**
* @author 
* @time   2016-05-23 16:40:25
*/
@Table("L_CKQHJHF_YW")
public class L_ckqhjhf_yw 
{
	@Column
	private String sjlxrq_s;
	@Column
	private String sjlxx_s;
	@Column
	private String ylxrq_y;
	@Column
	private String ylxx_y;
	@Column
	@Name
	@Prev({
			@SQL(db = DB.ORACLE, value="SELECT sys_guid() FROM dual")
	})
	private String ywid;
	@Column
	private String fk_hfid;
	@Column
	private String sfwc;
	@Column
	private String add_time;
	@Column
	private int is_warn;
	@Column
	private String bz;
	@Column
	private String fkjl;
		public String getFkjl() {
		return fkjl;
	}
	public void setFkjl(String fkjl) {
		this.fkjl = fkjl;
	}
		public String getSjlxrq_s()
	{
		return sjlxrq_s;
	}
	public void setSjlxrq_s(String sjlxrq_s)
	{
		this.sjlxrq_s=sjlxrq_s;
	}
	public String getSjlxx_s()
	{
		return sjlxx_s;
	}
	public void setSjlxx_s(String sjlxx_s)
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
	public String getYlxx_y()
	{
		return ylxx_y;
	}
	public void setYlxx_y(String ylxx_y)
	{
		this.ylxx_y=ylxx_y;
	}
	public String getYwid()
	{
		return ywid;
	}
	public void setYwid(String ywid)
	{
		this.ywid=ywid;
	}
	public String getFk_hfid()
	{
		return fk_hfid;
	}
	public void setFk_hfid(String fk_hfid)
	{
		this.fk_hfid=fk_hfid;
	}
	public String getSfwc()
	{
		return sfwc;
	}
	public void setSfwc(String sfwc)
	{
		this.sfwc=sfwc;
	}
	public String getAdd_time()
	{
		return add_time;
	}
	public void setAdd_time(String add_time)
	{
		this.add_time=add_time;
	}
	public int getIs_warn()
	{
		return is_warn;
	}
	public void setIs_warn(int is_warn)
	{
		this.is_warn=is_warn;
	}
	public String getBz()
	{
		return bz;
	}
	public void setBz(String bz)
	{
		this.bz=bz;
	}

}