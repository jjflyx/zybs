package com.hits.modules.order.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author JJF E-mail:
 * @version 创建时间：2017年8月8日 下午8:05:13
 * 类说明
 */
@Table("l_hkzd")
public class HkzdBean {
	@Column
	@Name
	private String  zdid;//账单id
	@Column
	private String  actor;//创建人
	@Column
	private String  userid;//付款人
	@Column
	private String  isfk;//是否付款
	@Column
	private String  zdmc;//账单名称
	@Column
	private String  fkrq;//付款日期
	@Column
	private String  sfjk;//所付价款
	@Column
	private String  gmtj;//购买途径
	@Column
	private String  yt;//用途
	@Column
	private String  create_time;//创建时间
	
	
	public String getIsfk() {
		return isfk;
	}
	public void setIsfk(String isfk) {
		this.isfk = isfk;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getZdid() {
		return zdid;
	}
	public void setZdid(String zdid) {
		this.zdid = zdid;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getZdmc() {
		return zdmc;
	}
	public void setZdmc(String zdmc) {
		this.zdmc = zdmc;
	}
	public String getFkrq() {
		return fkrq;
	}
	public void setFkrq(String fkrq) {
		this.fkrq = fkrq;
	}
	public String getSfjk() {
		return sfjk;
	}
	public void setSfjk(String sfjk) {
		this.sfjk = sfjk;
	}
	public String getGmtj() {
		return gmtj;
	}
	public void setGmtj(String gmtj) {
		this.gmtj = gmtj;
	}
	public String getYt() {
		return yt;
	}
	public void setYt(String yt) {
		this.yt = yt;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
}
