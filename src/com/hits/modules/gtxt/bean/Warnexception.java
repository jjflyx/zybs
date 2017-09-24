package com.hits.modules.gtxt.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Numbgui
* @time   2016-03-08 14:34:37
*/
@Table("WARNEXCEPTION")
public class Warnexception 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT WARNEXCEPTION_S.nextval FROM dual")
	})
	private int id;
	@Column
	private String applicant;
	@Column
	private String add_time;
	@Column
	private String note;
	@Column
	private String file_html;
	@Column
	private String xyzt_id;
	@Column
	private int sxcj_id;
	@Column
	private String unit;
	@Column
	private String username;
	@Column
	private String acceptance_time;
	@Column
	private int cz;
	@Column
	private String cz_note;
	@Column
	private String cz_why;
	@Column
	private String cz_yj;
	@Column
	private int state;
	@Column
	private String tel;
	@Column
	private String code;
	@Column
	private String contract_id;
	@Column
	private String contract_name;
	@Column
	private String xyzt_name;
	@Column
	private String xzqh;
	@Column
	private String xy_type;
	@Column
	private Integer source;
	@Column
	private String clsx;//处理时限
	

	public String getClsx() {
		return clsx;
	}
	public void setClsx(String clsx) {
		this.clsx = clsx;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public String getApplicant()
	{
		return applicant;
	}
	public void setApplicant(String applicant)
	{
		this.applicant=applicant;
	}
	public String getAdd_time()
	{
		return add_time;
	}
	public void setAdd_time(String add_time)
	{
		this.add_time=add_time;
	}
	public String getNote()
	{
		return note;
	}
	public void setNote(String note)
	{
		this.note=note;
	}
	public String getFile_html()
	{
		return file_html;
	}
	public void setFile_html(String file_html)
	{
		this.file_html=file_html;
	}
	public String getXyzt_id()
	{
		return xyzt_id;
	}
	public void setXyzt_id(String xyzt_id)
	{
		this.xyzt_id=xyzt_id;
	}
	public int getSxcj_id()
	{
		return sxcj_id;
	}
	public void setSxcj_id(int sxcj_id)
	{
		this.sxcj_id=sxcj_id;
	}
	public String getUnit()
	{
		return unit;
	}
	public void setUnit(String unit)
	{
		this.unit=unit;
	}
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username=username;
	}
	public String getAcceptance_time()
	{
		return acceptance_time;
	}
	public void setAcceptance_time(String acceptance_time)
	{
		this.acceptance_time=acceptance_time;
	}
	public int getCz()
	{
		return cz;
	}
	public void setCz(int cz)
	{
		this.cz=cz;
	}
	public String getCz_note()
	{
		return cz_note;
	}
	public void setCz_note(String cz_note)
	{
		this.cz_note=cz_note;
	}
	public String getCz_why()
	{
		return cz_why;
	}
	public void setCz_why(String cz_why)
	{
		this.cz_why=cz_why;
	}
	public String getCz_yj()
	{
		return cz_yj;
	}
	public void setCz_yj(String cz_yj)
	{
		this.cz_yj=cz_yj;
	}
	public int getState()
	{
		return state;
	}
	public void setState(int state)
	{
		this.state=state;
	}
	public String getTel()
	{
		return tel;
	}
	public void setTel(String tel)
	{
		this.tel=tel;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContract_id() {
		return contract_id;
	}

	public void setContract_id(String contract_id) {
		this.contract_id = contract_id;
	}

	public String getContract_name() {
		return contract_name;
	}

	public void setContract_name(String contract_name) {
		this.contract_name = contract_name;
	}

	public String getXyzt_name() {
		return xyzt_name;
	}

	public void setXyzt_name(String xyzt_name) {
		this.xyzt_name = xyzt_name;
	}

	public String getXzqh() {
		return xzqh;
	}

	public void setXzqh(String xzqh) {
		this.xzqh = xzqh;
	}

	public String getXy_type() {
		return xy_type;
	}

	public void setXy_type(String xy_type) {
		this.xy_type = xy_type;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}
}