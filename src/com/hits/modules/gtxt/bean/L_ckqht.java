package com.hits.modules.gtxt.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

import java.sql.Timestamp;
import java.util.Hashtable;

/**
* @author Numbgui
* @time   2016-05-18 10:07:10
*/
@Table("L_CKQHT")
public class L_ckqht 
{
	@Column
	@Name
	@Prev({
			@SQL(db = DB.ORACLE, value="SELECT sys_guid() FROM dual")
	})
	private String htid;
	@Column
	private String htbh;
	@Column
	private String htmc;
	@Column
	private String dlrq;
	@Column
	private String xyzt;
	@Column
	private String crr;
	@Column
	private String fkfs;
	@Column
	private Double jkje;
	@Column
	private String fj;
	@Column
	private String kqmc;
	@Column
	private float kqmj;
	@Column
	private String ksdz;
	@Column
	private String jtwz;
	@Column
	private String kz;
	@Column
	private String qsz;
	@Column
	private String qxq;
	@Column
	private String xgyy;
	@Column
	private String xkzh;
	@Column
	private String zhlxrq;
	@Column
	private String zsfwc;
	@Column
	private String actor;
	@Column
	private String unitid;
	@Column
	private String add_time;
	@Column
	private String sprq;
	@Column
	private String excelid;
	@Column
	private int modetype;
	@Column
	private int status;
	@Column
	private String actor2;
	@Column
	private String xzqh_unit;
	@Column
	private String unitid2;
	@Column
	private String wt_unit;
	@Column
	private int type;
	@Column
	private String bjh;
	@Column
	private String jf_type;
	@Column
	private int bcf_type;
	@Column
	private int syf_type;
	@Column
	private int fkfy_type;
	@Column
	private Timestamp sjc;//时间戳
	@Column
	private String xxlx;//信息类型


	public String getXxlx() {
		return xxlx;
	}

	public void setXxlx(String xxlx) {
		this.xxlx = xxlx;
	}

	public Timestamp getSjc() {
		return sjc;
	}

	public void setSjc(Timestamp sjc) {
		this.sjc = sjc;
	}

	public int getFkfy_type() {
		return fkfy_type;
	}

	public void setFkfy_type(int fkfy_type) {
		this.fkfy_type = fkfy_type;
	}

	public int getSyf_type() {
		return syf_type;
	}

	public void setSyf_type(int syf_type) {
		this.syf_type = syf_type;
	}

	public int getBcf_type() {
		return bcf_type;
	}

	public void setBcf_type(int bcf_type) {
		this.bcf_type = bcf_type;
	}

	public String getBjh() {
		return bjh;
	}

	public void setBjh(String bjh) {
		this.bjh = bjh;
	}

	public String getJf_type() {
		return jf_type;
	}

	public void setJf_type(String jf_type) {
		this.jf_type = jf_type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getHtid()
	{
		return htid;
	}
	public void setHtid(String htid)
	{
		this.htid=htid;
	}
	public String getHtbh()
	{
		return htbh;
	}
	public void setHtbh(String htbh)
	{
		this.htbh=htbh;
	}
	public String getHtmc()
	{
		return htmc;
	}
	public void setHtmc(String htmc)
	{
		this.htmc=htmc;
	}
	public String getDlrq()
	{
		return dlrq;
	}
	public void setDlrq(String dlrq)
	{
		this.dlrq=dlrq;
	}
	public String getXyzt()
	{
		return xyzt;
	}
	public void setXyzt(String xyzt)
	{
		this.xyzt=xyzt;
	}
	public String getCrr()
	{
		return crr;
	}
	public void setCrr(String crr)
	{
		this.crr=crr;
	}
	public String getFkfs()
	{
		return fkfs;
	}
	public void setFkfs(String fkfs)
	{
		this.fkfs=fkfs;
	}
	public Double getJkje()
	{
		return jkje;
	}
	public void setJkje(Double jkje) {
		this.jkje = jkje;
	}
	public String getFj()
	{
		return fj;
	}
	public void setFj(String fj)
	{
		this.fj=fj;
	}
	public String getKqmc()
	{
		return kqmc;
	}
	public void setKqmc(String kqmc)
	{
		this.kqmc=kqmc;
	}
	public float getKqmj()
	{
		return kqmj;
	}
	public void setKqmj(float kqmj)
	{
		this.kqmj=kqmj;
	}
	public String getKsdz()
	{
		return ksdz;
	}
	public void setKsdz(String ksdz)
	{
		this.ksdz=ksdz;
	}
	public String getJtwz()
	{
		return jtwz;
	}
	public void setJtwz(String jtwz)
	{
		this.jtwz=jtwz;
	}
	public String getKz()
	{
		return kz;
	}
	public void setKz(String kz)
	{
		this.kz=kz;
	}
	public String getQsz()
	{
		return qsz;
	}
	public void setQsz(String qsz)
	{
		this.qsz=qsz;
	}
	public String getQxq()
	{
		return qxq;
	}
	public void setQxq(String qxq)
	{
		this.qxq=qxq;
	}
	public String getXgyy()
	{
		return xgyy;
	}
	public void setXgyy(String xgyy)
	{
		this.xgyy=xgyy;
	}
	public String getXkzh()
	{
		return xkzh;
	}
	public void setXkzh(String xkzh)
	{
		this.xkzh=xkzh;
	}
	public String getZhlxrq()
	{
		return zhlxrq;
	}
	public void setZhlxrq(String zhlxrq)
	{
		this.zhlxrq=zhlxrq;
	}
	public String getZsfwc()
	{
		return zsfwc;
	}
	public void setZsfwc(String zsfwc)
	{
		this.zsfwc=zsfwc;
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
	public String getAdd_time()
	{
		return add_time;
	}
	public void setAdd_time(String add_time)
	{
		this.add_time=add_time;
	}
	public String getSprq()
	{
		return sprq;
	}
	public void setSprq(String sprq)
	{
		this.sprq=sprq;
	}
	public String getExcelid()
	{
		return excelid;
	}
	public void setExcelid(String excelid)
	{
		this.excelid=excelid;
	}
	public int getModetype()
	{
		return modetype;
	}
	public void setModetype(int modetype)
	{
		this.modetype=modetype;
	}
	public int getStatus()
	{
		return status;
	}
	public void setStatus(int status)
	{
		this.status=status;
	}
	public String getActor2()
	{
		return actor2;
	}
	public void setActor2(String actor2)
	{
		this.actor2=actor2;
	}
	public String getXzqh_unit()
	{
		return xzqh_unit;
	}
	public void setXzqh_unit(String xzqh_unit)
	{
		this.xzqh_unit=xzqh_unit;
	}
	public String getUnitid2()
	{
		return unitid2;
	}
	public void setUnitid2(String unitid2)
	{
		this.unitid2=unitid2;
	}
	public String getWt_unit()
	{
		return wt_unit;
	}
	public void setWt_unit(String wt_unit)
	{
		this.wt_unit=wt_unit;
	}

}