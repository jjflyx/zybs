package com.hits.modules.jlry.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Wizzer
* @time   2016-03-22 10:14:21
*/
@Table("REWARD_INFO")
public class Reward_info 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT REWARD_INFO_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int xyzt_id;
	@Column
	private int contract_id;
	@Column
	private String create_date;
	@Column
	private String unit;
	@Column
	private String jl_date;
	@Column
	private String note;
	@Column
	private int type;
	@Column
	private String unitid;
	@Column
	private String actor;
	@Column
	private int issue;
	@Column
	private String xy_type;
	@Column
	private String xzqh;
	@Column
	private String file_html;

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public int getXyzt_id()
	{
		return xyzt_id;
	}
	public void setXyzt_id(int xyzt_id)
	{
		this.xyzt_id=xyzt_id;
	}
	public int getContract_id()
	{
		return contract_id;
	}
	public void setContract_id(int contract_id)
	{
		this.contract_id=contract_id;
	}
	public String getCreate_date()
	{
		return create_date;
	}
	public void setCreate_date(String create_date)
	{
		this.create_date=create_date;
	}
	public String getUnit()
	{
		return unit;
	}
	public void setUnit(String unit)
	{
		this.unit=unit;
	}
	public String getJl_date()
	{
		return jl_date;
	}
	public void setJl_date(String jl_date)
	{
		this.jl_date=jl_date;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note=note;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type=type;
	}
	public String getUnitid()
	{
		return unitid;
	}
	public void setUnitid(String unitid)
	{
		this.unitid=unitid;
	}
	public String getActor()
	{
		return actor;
	}
	public void setActor(String actor)
	{
		this.actor=actor;
	}
	public int getIssue()
	{
		return issue;
	}
	public void setIssue(int issue)
	{
		this.issue=issue;
	}
	public String getXy_type()
	{
		return xy_type;
	}
	public void setXy_type(String xy_type)
	{
		this.xy_type=xy_type;
	}

	public String getXzqh() {
		return xzqh;
	}

	public void setXzqh(String xzqh) {
		this.xzqh = xzqh;
	}
	public String getFile_html() {
		return file_html;
	}
	public void setFile_html(String file_html) {
		this.file_html = file_html;
	}
}