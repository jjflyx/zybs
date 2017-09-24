package com.hits.modules.gtxt.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

/**
* @author Numbgui
* @time   2016-03-09 13:49:53
*/
@Table("GTXT_RULE")
public class Gtxt_rule 
{
	@Column
	@Name
	@Prev({
			@SQL(db = DB.ORACLE, value="SELECT sys_guid() FROM dual")
	})
	private String id;
	@Column
	private String rule_name;
	@Column
	private String xy_type;
	@Column
	private String note;
	@Column
	private String sxxw_id;
	@Column
	private String sxqx_id;
	@Column
	private int type;
	@Column
	private String rule;
	@Column
	private int is_work;
	@Column
	private String actor;
	@Column
	private String add_time;
	@Column
	private String tableid;
	@Column
	private Integer cjqx;
	@Column
	private Integer state;
	@Column
	private String sms_model;

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getRule_name()
	{
		return rule_name;
	}
	public void setRule_name(String rule_name)
	{
		this.rule_name=rule_name;
	}
	public String getXy_type()
	{
		return xy_type;
	}
	public void setXy_type(String xy_type)
	{
		this.xy_type=xy_type;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note=note;
	}
	public String getSxxw_id()
	{
		return sxxw_id;
	}
	public void setSxxw_id(String sxxw_id)
	{
		this.sxxw_id=sxxw_id;
	}
	public String getSxqx_id()
	{
		return sxqx_id;
	}
	public void setSxqx_id(String sxqx_id)
	{
		this.sxqx_id=sxqx_id;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type=type;
	}
	public String getRule()
	{
		return rule;
	}
	public void setRule(String rule)
	{
		this.rule=rule;
	}
	public int getIs_work()
	{
		return is_work;
	}
	public void setIs_work(int is_work)
	{
		this.is_work=is_work;
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

	public String getTableid() {
		return tableid;
	}

	public void setTableid(String tableid) {
		this.tableid = tableid;
	}

	public Integer getCjqx() {
		return cjqx;
	}

	public void setCjqx(Integer cjqx) {
		this.cjqx = cjqx;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}
	public String getSms_model() {
		return sms_model;
	}
	public void setSms_model(String sms_model) {
		this.sms_model = sms_model;
	}
}