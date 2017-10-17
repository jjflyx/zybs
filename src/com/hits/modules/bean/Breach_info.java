package com.hits.modules.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Name;
/**
* @author 
* @time   2016-01-25 11:37:11
*/
@Table("Breach_info")
public class Breach_info 
{
	@Column
	@Name
	private String id;
	@Column
	private String xylx;
	@Column
	private String sxxw;
	@Column
	private String sxqx;
	@Column
	private String cjcs;
	@Column
	private String yj;
	@Column
	private int rule;
	@Column
	private String rule_note;
	@Column
	private int is_use;
	@Column
	private int type;
	@Column
	private String cjr;
	@Column
	private String cjsj;
	@Column
	private int location;
	@Column
	private int is_xzcf;
	@Column
	private String ht_type;
	@Column
	private String gl_user;
	@Column
	private int is_fk;
	@Column
	private String xzcf_type;
	@Column
	private String ay;

	public String getHt_type() {
		return ht_type;
	}

	public void setHt_type(String ht_type) {
		this.ht_type = ht_type;
	}

	public String getGl_user() {
		return gl_user;
	}

	public void setGl_user(String gl_user) {
		this.gl_user = gl_user;
	}

	public int getIs_fk() {
		return is_fk;
	}

	public void setIs_fk(int is_fk) {
		this.is_fk = is_fk;
	}

	public String getXzcf_type() {
		return xzcf_type;
	}

	public void setXzcf_type(String xzcf_type) {
		this.xzcf_type = xzcf_type;
	}

	public String getAy() {
		return ay;
	}

	public void setAy(String ay) {
		this.ay = ay;
	}

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getXylx()
	{
		return xylx;
	}
	public void setXylx(String xylx)
	{
		this.xylx=xylx;
	}
	public String getSxxw()
	{
		return sxxw;
	}
	public void setSxxw(String sxxw)
	{
		this.sxxw=sxxw;
	}
	public String getSxqx()
	{
		return sxqx;
	}
	public void setSxqx(String sxqx)
	{
		this.sxqx=sxqx;
	}
	public String getCjcs()
	{
		return cjcs;
	}
	public void setCjcs(String cjcs)
	{
		this.cjcs=cjcs;
	}
	public String getYj()
	{
		return yj;
	}
	public void setYj(String yj)
	{
		this.yj=yj;
	}
	public int getRule()
	{
		return rule;
	}
	public void setRule(int rule)
	{
		this.rule=rule;
	}
	public String getRule_note()
	{
		return rule_note;
	}
	public void setRule_note(String rule_note)
	{
		this.rule_note=rule_note;
	}
	public int getIs_use()
	{
		return is_use;
	}
	public void setIs_use(int is_use)
	{
		this.is_use=is_use;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type=type;
	}
	public String getCjr()
	{
		return cjr;
	}
	public void setCjr(String cjr)
	{
		this.cjr=cjr;
	}
	public String getCjsj()
	{
		return cjsj;
	}
	public void setCjsj(String cjsj)
	{
		this.cjsj=cjsj;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public int getIs_xzcf() {
		return is_xzcf;
	}
	public void setIs_xzcf(int is_xzcf) {
		this.is_xzcf = is_xzcf;
	}

}