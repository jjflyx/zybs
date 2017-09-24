package com.hits.modules.zzxx.bean;

import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Wizzer
* @time   2016-03-10 17:23:03
*/
@Table("ZZ_INFO")
public class Zz_info 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT ZZ_INFO_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int xyzt_id;
	@Column
	private String zz_name;
	@Column
	private String zz_code;
	@Column
	private String zz_jb;
	@Column
	private String zz_lb;
	@Column
	private String qt_code;
	@Column
	private String zz_unit;
	@Column
	private String zyfw;
	@Column
	private String gxry;
	@Column
	private int count;
	@Column
	private String ssjg;
	@Column
	private int if_sn;
	@Column
	private String expiry_date;
	@Column
	private String njqx;
	@Column
	private String qyzz;
	@Column
	private String xy_type;
	@Column
	private String actor;
	@Column
	private String unitid;
	@Column
	private String create_date;
	@Column
	private String start_date;
	@Column
	private String end_date;
	@Column
	private String jsfzr;
	@Column
	private String fr_zcdj;
	@Column
	private String jsfzr_zcdj;
	@Column
	private String staffSize;
	@Column
	private String apparatus;
	@Column
	private String major;
	@Column
	private String changeRecord;
	@Column
	private String ext;
	@Column
	private int status;
	@Column
	private int cancellation;//资质状态：1/正常；0/已注销
	@Column
	private Timestamp sjc;//时间戳

	public Timestamp getSjc() {
		return sjc;
	}

	public void setSjc(Timestamp sjc) {
		this.sjc = sjc;
	}

	public int getCancellation() {
		return cancellation;
	}

	public void setCancellation(int cancellation) {
		this.cancellation = cancellation;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

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
	public String getZz_name()
	{
		return zz_name;
	}
	public void setZz_name(String zz_name)
	{
		this.zz_name=zz_name;
	}
	public String getZz_code()
	{
		return zz_code;
	}
	public void setZz_code(String zz_code)
	{
		this.zz_code=zz_code;
	}
	public String getZz_jb()
	{
		return zz_jb;
	}
	public void setZz_jb(String zz_jb)
	{
		this.zz_jb=zz_jb;
	}
	public String getZz_lb()
	{
		return zz_lb;
	}
	public void setZz_lb(String zz_lb)
	{
		this.zz_lb=zz_lb;
	}
	public String getQt_code()
	{
		return qt_code;
	}
	public void setQt_code(String qt_code)
	{
		this.qt_code=qt_code;
	}
	public String getZz_unit()
	{
		return zz_unit;
	}
	public void setZz_unit(String zz_unit)
	{
		this.zz_unit=zz_unit;
	}
	public String getZyfw()
	{
		return zyfw;
	}
	public void setZyfw(String zyfw)
	{
		this.zyfw=zyfw;
	}
	public String getGxry()
	{
		return gxry;
	}
	public void setGxry(String gxry)
	{
		this.gxry=gxry;
	}
	public int getCount()
	{
		return count;
	}
	public void setCount(int count)
	{
		this.count=count;
	}
	public String getSsjg()
	{
		return ssjg;
	}
	public void setSsjg(String ssjg)
	{
		this.ssjg=ssjg;
	}
	public int getIf_sn()
	{
		return if_sn;
	}
	public void setIf_sn(int if_sn)
	{
		this.if_sn=if_sn;
	}
	public String getExpiry_date()
	{
		return expiry_date;
	}
	public void setExpiry_date(String expiry_date)
	{
		this.expiry_date=expiry_date;
	}
	public String getNjqx()
	{
		return njqx;
	}
	public void setNjqx(String njqx)
	{
		this.njqx=njqx;
	}
	public String getQyzz()
	{
		return qyzz;
	}
	public void setQyzz(String qyzz)
	{
		this.qyzz=qyzz;
	}
	public String getXy_type()
	{
		return xy_type;
	}
	public void setXy_type(String xy_type)
	{
		this.xy_type=xy_type;
	}
	public String getActor()
	{
		return actor;
	}
	public void setActor(String actor)
	{
		this.actor=actor;
	}
	public String getUnitid()
	{
		return unitid;
	}
	public void setUnitid(String unitid)
	{
		this.unitid=unitid;
	}
	public String getCreate_date()
	{
		return create_date;
	}
	public void setCreate_date(String create_date)
	{
		this.create_date=create_date;
	}
	public String getStart_date()
	{
		return start_date;
	}
	public void setStart_date(String start_date)
	{
		this.start_date=start_date;
	}
	public String getEnd_date()
	{
		return end_date;
	}
	public void setEnd_date(String end_date)
	{
		this.end_date=end_date;
	}
	public String getJsfzr() {
		return jsfzr;
	}
	public void setJsfzr(String jsfzr) {
		this.jsfzr = jsfzr;
	}
	public String getFr_zcdj() {
		return fr_zcdj;
	}
	public void setFr_zcdj(String frZcdj) {
		fr_zcdj = frZcdj;
	}
	public String getJsfzr_zcdj() {
		return jsfzr_zcdj;
	}
	public void setJsfzr_zcdj(String jsfzrZcdj) {
		jsfzr_zcdj = jsfzrZcdj;
	}
	public String getStaffSize() {
		return staffSize;
	}
	public void setStaffSize(String staffSize) {
		this.staffSize = staffSize;
	}
	public String getApparatus() {
		return apparatus;
	}
	public void setApparatus(String apparatus) {
		this.apparatus = apparatus;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getChangeRecord() {
		return changeRecord;
	}
	public void setChangeRecord(String changeRecord) {
		this.changeRecord = changeRecord;
	}

}