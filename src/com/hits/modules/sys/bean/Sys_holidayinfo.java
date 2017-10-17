package com.hits.modules.sys.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author wanfly
* @time   2014-02-27 10:24:14
*/
@Table("SYS_HOLIDAYINFO")
public class Sys_holidayinfo 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT SYS_HOLIDAYINFO_S.nextval FROM dual")
	})
	private int id;
	/**
	 * 节假日日期
	 */
	@Column
	private String holiday;
	/**
	 * 是否是周末
	 */
	@Column
	private int isweekend;
	/**
	 * 是否工作
	 */
	@Column
	private int iswork;
	/**
	 * 节假日说明
	 */
	@Column
	private String des;
	@Column
	private int ext1;
	@Column
	private String ext2;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public String getHoliday()
	{
		return holiday;
	}
	public void setHoliday(String holiday)
	{
		this.holiday=holiday;
	}
	public int getIsweekend()
	{
		return isweekend;
	}
	public void setIsweekend(int isweekend)
	{
		this.isweekend=isweekend;
	}
	public int getIswork()
	{
		return iswork;
	}
	public void setIswork(int iswork)
	{
		this.iswork=iswork;
	}
	public String getDes()
	{
		return des;
	}
	public void setDes(String des)
	{
		this.des=des;
	}
	public int getExt1()
	{
		return ext1;
	}
	public void setExt1(int ext1)
	{
		this.ext1=ext1;
	}
	public String getExt2()
	{
		return ext2;
	}
	public void setExt2(String ext2)
	{
		this.ext2=ext2;
	}

}