package com.hits.modules.sxcj.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Wizzer
* @time   2016-03-21 16:36:20
*/
@Table("DISCREDIT_INFO_BF")
public class Discredit_info_bf 
{
	@Column
	private int id;
	@Column
	private String xyzt_id;
	@Column
	private String contract_id;
	@Column
	private String sxxw_id;
	@Column
	private String sxqx_id;
	@Column
	private String cjcs_id;
	@Column
	private String sxxw_other;
	@Column
	private String sxqx_other;
	@Column
	private String cjcs_other;
	@Column
	private String perform_date;
	@Column
	private String unit;
	@Column
	private String start_date;
	@Column
	private String measures;
	@Column
	private String type;
	@Column
	private String discipline_date;
	@Column
	private String end_date;
	@Column
	private int src;
	@Column
	private String actor;
	@Column
	private String create_date;
	@Column
	private int issue;
	@Column
	private String yt_date;
	@Column
	private String unitid;
	@Column
	private String xy_type;
	@Column
	private String xzqh;
	@Column
	private String tablekey;//合同表表名
	@Column
	private String ywid;
	@Column
	private int src_type;
	@Column
	private String xzcf_id;
	@Column
	private String biaozhu;
	@Column
	private String warnid;

	public String getWarnid() {
		return warnid;
	}

	public void setWarnid(String warnid) {
		this.warnid = warnid;
	}

	public String getBiaozhu() {
		return biaozhu;
	}

	public void setBiaozhu(String biaozhu) {
		this.biaozhu = biaozhu;
	}

	public String getXzcf_id() {
		return xzcf_id;
	}

	public void setXzcf_id(String xzcf_id) {
		this.xzcf_id = xzcf_id;
	}

	public int getSrc_type() {
		return src_type;
	}

	public void setSrc_type(int src_type) {
		this.src_type = src_type;
	}

	public String getYwid() {
		return ywid;
	}

	public void setYwid(String ywid) {
		this.ywid = ywid;
	}

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public String getXyzt_id()
	{
		return xyzt_id;
	}
	public void setXyzt_id(String xyzt_id)
	{
		this.xyzt_id=xyzt_id;
	}
	public String getContract_id()
	{
		return contract_id;
	}
	public void setContract_id(String contract_id)
	{
		this.contract_id=contract_id;
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
	public String getCjcs_id()
	{
		return cjcs_id;
	}
	public void setCjcs_id(String cjcs_id)
	{
		this.cjcs_id=cjcs_id;
	}
	public String getUnit()
	{
		return unit;
	}
	public void setUnit(String unit)
	{
		this.unit=unit;
	}
	public String getStart_date()
	{
		return start_date;
	}
	public void setStart_date(String start_date)
	{
		this.start_date=start_date;
	}
	public String getMeasures()
	{
		return measures;
	}
	public void setMeasures(String measures)
	{
		this.measures=measures;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type=type;
	}
	public String getDiscipline_date()
	{
		return discipline_date;
	}
	public void setDiscipline_date(String discipline_date)
	{
		this.discipline_date=discipline_date;
	}
	public String getEnd_date()
	{
		return end_date;
	}
	public void setEnd_date(String end_date)
	{
		this.end_date=end_date;
	}
	public int getSrc()
	{
		return src;
	}
	public void setSrc(int src)
	{
		this.src=src;
	}
	public String getActor()
	{
		return actor;
	}
	public void setActor(String actor)
	{
		this.actor=actor;
	}
	public String getCreate_date()
	{
		return create_date;
	}
	public void setCreate_date(String create_date)
	{
		this.create_date=create_date;
	}
	public int getIssue()
	{
		return issue;
	}
	public void setIssue(int issue)
	{
		this.issue=issue;
	}
	public String getYt_date()
	{
		return yt_date;
	}
	public void setYt_date(String yt_date)
	{
		this.yt_date=yt_date;
	}
	public String getUnitid()
	{
		return unitid;
	}
	public void setUnitid(String unitid)
	{
		this.unitid=unitid;
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
	public String getTablekey() {
		return tablekey;
	}
	public void setTablekey(String tablekey) {
		this.tablekey = tablekey;
	}

	public String getSxxw_other() {
		return sxxw_other;
	}

	public void setSxxw_other(String sxxwOther) {
		sxxw_other = sxxwOther;
	}

	public String getSxqx_other() {
		return sxqx_other;
	}

	public void setSxqx_other(String sxqxOther) {
		sxqx_other = sxqxOther;
	}

	public String getCjcs_other() {
		return cjcs_other;
	}

	public void setCjcs_other(String cjcsOther) {
		cjcs_other = cjcsOther;
	}

	public String getPerform_date() {
		return perform_date;
	}

	public void setPerform_date(String performDate) {
		perform_date = performDate;
	}
}