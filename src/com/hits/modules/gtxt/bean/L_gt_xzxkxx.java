package com.hits.modules.gtxt.bean;

import java.sql.Timestamp;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.*;

/**
* @author 
* @time   2016-05-23 16:40:25
*/
@Table("L_GT_XZXKXX")
public class L_gt_xzxkxx 
{
	@Column
	@Name
	private String id;
	@Column
	private String xk_wsh;
	@Column
	private String xk_xmmc;
	@Column
	private String xk_splb;
	@Column
	private String xk_nr_ah;
	@Column
	private String xk_xdr;
	@Column
	private String xk_xdr_shxym;
	@Column
	private String xk_xdr_zdm;
	@Column
	private String xk_xdr_gsdj;
	@Column
	private String xk_xdr_swdj;
	@Column
	private String xk_xdr_sfz;
	@Column
	private String xk_fr;
	@Column
	private Timestamp xk_jdrq;
	@Column
	private Timestamp xk_jzq;
	@Column
	private String xk_xzjg;
	@Column
	private String xk_zt;
	@Column
	private String dfbm;
	@Column
	private Timestamp sjc;
	@Column
	private String bz;
	@Column
	private String source;
	@Column
	private String area;
	@Column
	private int xyptbatchno;
	@Column
	private String xyptstate;
	@Column
	private String department_id;
	@Column
	private String data_state;
	@Column
	private Timestamp xyptmodify_date;
	@Column
	private Timestamp xypt_time_dxp;
	@Column
	private String xk_xmmc_ah;
	@Column
	private String xk_nr;
	@Column
	private String xy_type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getXk_wsh() {
		return xk_wsh;
	}
	public void setXk_wsh(String xk_wsh) {
		this.xk_wsh = xk_wsh;
	}
	public String getXk_xmmc() {
		return xk_xmmc;
	}
	public void setXk_xmmc(String xk_xmmc) {
		this.xk_xmmc = xk_xmmc;
	}
	public String getXk_splb() {
		return xk_splb;
	}
	public void setXk_splb(String xk_splb) {
		this.xk_splb = xk_splb;
	}
	public String getXk_nr_ah() {
		return xk_nr_ah;
	}
	public void setXk_nr_ah(String xk_nr_ah) {
		this.xk_nr_ah = xk_nr_ah;
	}
	public String getXk_xdr() {
		return xk_xdr;
	}
	public void setXk_xdr(String xk_xdr) {
		this.xk_xdr = xk_xdr;
	}
	public String getXk_xdr_shxym() {
		return xk_xdr_shxym;
	}
	public void setXk_xdr_shxym(String xk_xdr_shxym) {
		this.xk_xdr_shxym = xk_xdr_shxym;
	}
	public String getXk_xdr_zdm() {
		return xk_xdr_zdm;
	}
	public void setXk_xdr_zdm(String xk_xdr_zdm) {
		this.xk_xdr_zdm = xk_xdr_zdm;
	}
	public String getXk_xdr_gsdj() {
		return xk_xdr_gsdj;
	}
	public void setXk_xdr_gsdj(String xk_xdr_gsdj) {
		this.xk_xdr_gsdj = xk_xdr_gsdj;
	}
	public String getXk_xdr_swdj() {
		return xk_xdr_swdj;
	}
	public void setXk_xdr_swdj(String xk_xdr_swdj) {
		this.xk_xdr_swdj = xk_xdr_swdj;
	}
	public String getXk_xdr_sfz() {
		return xk_xdr_sfz;
	}
	public void setXk_xdr_sfz(String xk_xdr_sfz) {
		this.xk_xdr_sfz = xk_xdr_sfz;
	}
	public String getXk_fr() {
		return xk_fr;
	}
	public void setXk_fr(String xk_fr) {
		this.xk_fr = xk_fr;
	}
	public Timestamp getXk_jdrq() {
		return xk_jdrq;
	}
	public void setXk_jdrq(Timestamp xk_jdrq) {
		this.xk_jdrq = xk_jdrq;
	}
	public Timestamp getXk_jzq() {
		return xk_jzq;
	}
	public void setXk_jzq(Timestamp xk_jzq) {
		this.xk_jzq = xk_jzq;
	}
	public String getXk_xzjg() {
		return xk_xzjg;
	}
	public void setXk_xzjg(String xk_xzjg) {
		this.xk_xzjg = xk_xzjg;
	}
	public String getXk_zt() {
		return xk_zt;
	}
	public void setXk_zt(String xk_zt) {
		this.xk_zt = xk_zt;
	}
	public String getDfbm() {
		return dfbm;
	}
	public void setDfbm(String dfbm) {
		this.dfbm = dfbm;
	}
	public Timestamp getSjc() {
		return sjc;
	}
	public void setSjc(Timestamp sjc) {
		this.sjc = sjc;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public int getXyptbatchno() {
		return xyptbatchno;
	}
	public void setXyptbatchno(int xyptbatchno) {
		this.xyptbatchno = xyptbatchno;
	}
	public String getXyptstate() {
		return xyptstate;
	}
	public void setXyptstate(String xyptstate) {
		this.xyptstate = xyptstate;
	}
	public String getDepartment_id() {
		return department_id;
	}
	public void setDepartment_id(String department_id) {
		this.department_id = department_id;
	}
	public String getData_state() {
		return data_state;
	}
	public void setData_state(String data_state) {
		this.data_state = data_state;
	}
	public Timestamp getXyptmodify_date() {
		return xyptmodify_date;
	}
	public void setXyptmodify_date(Timestamp xyptmodify_date) {
		this.xyptmodify_date = xyptmodify_date;
	}
	public Timestamp getXypt_time_dxp() {
		return xypt_time_dxp;
	}
	public void setXypt_time_dxp(Timestamp xypt_time_dxp) {
		this.xypt_time_dxp = xypt_time_dxp;
	}
	public String getXk_xmmc_ah() {
		return xk_xmmc_ah;
	}
	public void setXk_xmmc_ah(String xk_xmmc_ah) {
		this.xk_xmmc_ah = xk_xmmc_ah;
	}
	public String getXk_nr() {
		return xk_nr;
	}
	public void setXk_nr(String xk_nr) {
		this.xk_nr = xk_nr;
	}
	public String getXy_type() {
		return xy_type;
	}
	public void setXy_type(String xy_type) {
		this.xy_type = xy_type;
	}
	
}