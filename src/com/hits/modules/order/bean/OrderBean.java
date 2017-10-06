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
@Table("l_jsgg")
public class OrderBean {
	@Column
	@Name
	/*@Prev({
		@SQL(db = DB.MYSQL, value="SELECT LAST_INSERT_ID()")
	})*/
	private String  htid;//合同id
	@Column
	private String  actor;//创建人
	@Column
	private String  bz;//备注
	@Column
	private String  hhgg;//货号规格
	@Column
	private String  unitid;//
	@Column
	private String  xzqh_unit;
	@Column
	private String  yfhrq;//应发货日期
	@Column
	private String  yfjk;//应付价款
	@Column
	private String  create_time;//状态
	@Column
	private String  add_time;//添加时间
	@Column
	private String  ddmc;//订单名称
	@Column
	private String  fj;//附件
	@Column
	private String  shdz;//收货地址
	@Column
	private String  lxfs;//联系方式
	@Column
	private String  khxm;//客户名称
	@Column
	private String  isfh;//是否发货
	@Column
	private String  ccdx;//尺寸大小
	
	
	public String getCcdx() {
		return ccdx;
	}
	public void setCcdx(String ccdx) {
		this.ccdx = ccdx;
	}
	public String getIsfh() {
		return isfh;
	}
	public void setIsfh(String isfh) {
		this.isfh = isfh;
	}
	public String getKhxm() {
		return khxm;
	}
	public void setKhxm(String khxm) {
		this.khxm = khxm;
	}
	public String getActor() {
		return actor;
	}
	public void setActor(String actor) {
		this.actor = actor;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
	public String getHhgg() {
		return hhgg;
	}
	public void setHhgg(String hhgg) {
		this.hhgg = hhgg;
	}

	public String getHtid() {
		return htid;
	}
	public void setHtid(String htid) {
		this.htid = htid;
	}
	public String getXzqh_unit() {
		return xzqh_unit;
	}
	public void setXzqh_unit(String xzqh_unit) {
		this.xzqh_unit = xzqh_unit;
	}
	public String getYfhrq() {
		return yfhrq;
	}
	public void setYfhrq(String yfhrq) {
		this.yfhrq = yfhrq;
	}
	public String getYfjk() {
		return yfjk;
	}
	public void setYfjk(String yfjk) {
		this.yfjk = yfjk;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getAdd_time() {
		return add_time;
	}
	public void setAdd_time(String add_time) {
		this.add_time = add_time;
	}
	public String getDdmc() {
		return ddmc;
	}
	public void setDdmc(String ddmc) {
		this.ddmc = ddmc;
	}
	public String getFj() {
		return fj;
	}
	public void setFj(String fj) {
		this.fj = fj;
	}
	public String getShdz() {
		return shdz;
	}
	public void setShdz(String shdz) {
		this.shdz = shdz;
	}
	public String getLxfs() {
		return lxfs;
	}
	public void setLxfs(String lxfs) {
		this.lxfs = lxfs;
	}
	public String getUnitid() {
		return unitid;
	}
	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}
}
