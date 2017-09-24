package com.hits.modules.gtxt.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Numbgui
* @time   2016-03-11 13:17:18
*/
@Table("EARLY_WARNING_INFO")
public class Early_warning_info 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT EARLY_WARNING_INFO_S.nextval FROM dual")
	})
	private int id;
	@Column
	private String rule_id;
	@Column
	private String rule_name;
	@Column
	private String xyzt_id;
	@Column
	private String xyzt_name;
	@Column
	private String end_date;
	@Column
	private int day;
	@Column
	private int state;
	@Column
	private String note;
	@Column
	private String create_time;
	@Column
	private String htid;
	@Column
	private String tablename;
	@Column
	private String formdefid_y;
	@Column
	private String formdefid_s;
	@Column
	private String loginname;
	@Column
	private String unitid;

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public String getRule_id()
	{
		return rule_id;
	}
	public void setRule_id(String rule_id)
	{
		this.rule_id=rule_id;
	}
	public String getRule_name()
	{
		return rule_name;
	}
	public void setRule_name(String rule_name)
	{
		this.rule_name=rule_name;
	}
	public String getXyzt_id()
	{
		return xyzt_id;
	}
	public void setXyzt_id(String xyzt_id)
	{
		this.xyzt_id=xyzt_id;
	}
	public String getXyzt_name()
	{
		return xyzt_name;
	}
	public void setXyzt_name(String xyzt_name)
	{
		this.xyzt_name=xyzt_name;
	}
	public String getEnd_date()
	{
		return end_date;
	}
	public void setEnd_date(String end_date)
	{
		this.end_date=end_date;
	}
	public int getDay()
	{
		return day;
	}
	public void setDay(int day)
	{
		this.day=day;
	}
	public int getState()
	{
		return state;
	}
	public void setState(int state)
	{
		this.state=state;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note=note;
	}
	public String getCreate_time()
	{
		return create_time;
	}
	public void setCreate_time(String create_time)
	{
		this.create_time=create_time;
	}

	public String getHtid() {
		return htid;
	}

	public void setHtid(String htid) {
		this.htid = htid;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getFormdefid_y() {
		return formdefid_y;
	}

	public void setFormdefid_y(String formdefid_y) {
		this.formdefid_y = formdefid_y;
	}

	public String getFormdefid_s() {
		return formdefid_s;
	}

	public void setFormdefid_s(String formdefid_s) {
		this.formdefid_s = formdefid_s;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public String getUnitid() {
		return unitid;
	}
	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}
}