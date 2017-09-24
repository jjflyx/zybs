package com.hits.modules.zcxx.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
/**
* @author L H T
* @time   2016-03-31 13:55:57
*/
@Table("ZC_ZX_INFO")
public class Zc_zx_info 
{
	@Column
	@Name
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT sys_guid() FROM dual")
	})
	private String id;
	@Column("zc_id")
	private String zc_id;
	@Column
	private String zx_time;
	@Column
	private String zx_yx;
	@Column
	private String zx_unit;
	@Column
	private String zx_loginname;
	@Column
	private int zx_state;
		public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getZc_id()
	{
		return zc_id;
	}
	public void setZc_id(String zc_id)
	{
		this.zc_id=zc_id;
	}
	public String getZx_time()
	{
		return zx_time;
	}
	public void setZx_time(String zx_time)
	{
		this.zx_time=zx_time;
	}
	public String getZx_yx()
	{
		return zx_yx;
	}
	public void setZx_yx(String zx_yx)
	{
		this.zx_yx=zx_yx;
	}
	public String getZx_unit()
	{
		return zx_unit;
	}
	public void setZx_unit(String zx_unit)
	{
		this.zx_unit=zx_unit;
	}
	public String getZx_loginname()
	{
		return zx_loginname;
	}
	public void setZx_loginname(String zx_loginname)
	{
		this.zx_loginname=zx_loginname;
	}
	public int getZx_state() {
		return zx_state;
	}
	public void setZx_state(int zx_state) {
		this.zx_state = zx_state;
	}
	
	

}