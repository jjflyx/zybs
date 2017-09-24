package com.hits.modules.xzcf.bean;

import java.sql.Date;
import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Wizzer
* @time   2016-03-18 08:40:21
*/
@Table("XZCF_INFO")
public class Xzcf_info 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT XZCF_INFO_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int xyzt_id;
	@Column
	private String contract_id;
	@Column
	private String xzcf_type;
	@Column
	private String cf_unit;
	@Column
	private String au;
	@Column
	private String gjz;
	@Column
	private String xzcf_code;
	@Column
	private String cflx_date;
	@Column
	private String cfsx_date;
	@Column
	private String start_date;
	@Column
	private String end_date;
	@Column
	private String xzcf_note;
	@Column
	private String file_html;
	@Column
	private int if_lx;
	@Column
	private int sxcj_id;
	@Column
	private String actor;
	@Column
	private String unitid;
	@Column
	private String create_date;
	@Column
	private int ext1;
	@Column
	private String ext2;
	@Column
	private String ext3;
	@Column
	private String is_warn;
	@Column
	private String sxxw_id;
	@Column
	private String sxqx_id;
	@Column
	private String cjcs_id;
	@Column
	private String xzcf_zt;
	@Column
	private int issue;
	@Column
	private String xy_type;
	@Column
	private int is_qz;
	@Column
	private String sjlxqk;//实际履行情况
	@Column
	private String biaozhu;//标注
	@Column
	private int status;//状态
	@Column
	private String xzcf_yiju;//行政处罚依据
	@Column
	private Timestamp sjc;//时间戳
	@Column
	private int xyptbatchno;//批号
	@Column
	private String data_state;//数据状态




	public Timestamp getSjc() {
		return sjc;
	}

	public void setSjc(Timestamp sjc) {
		this.sjc = sjc;
	}

	public int getXyptbatchno() {
		return xyptbatchno;
	}

	public void setXyptbatchno(int xyptbatchno) {
		this.xyptbatchno = xyptbatchno;
	}

	public String getData_state() {
		return data_state;
	}

	public void setData_state(String data_state) {
		this.data_state = data_state;
	}

	public String getXzcf_yiju() {
		return xzcf_yiju;
	}

	public void setXzcf_yiju(String xzcf_yiju) {
		this.xzcf_yiju = xzcf_yiju;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getBiaozhu() {
		return biaozhu;
	}

	public void setBiaozhu(String biaozhu) {
		this.biaozhu = biaozhu;
	}

	public String getSjlxqk() {
		return sjlxqk;
	}

	public void setSjlxqk(String sjlxqk) {
		this.sjlxqk = sjlxqk;
	}

	public String getXy_type() {
		return xy_type;
	}

	public void setXy_type(String xy_type) {
		this.xy_type = xy_type;
	}

	public int getIs_qz() {
		return is_qz;
	}

	public void setIs_qz(int is_qz) {
		this.is_qz = is_qz;
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
	public String getContract_id()
	{
		return contract_id;
	}
	public void setContract_id(String contract_id)
	{
		this.contract_id=contract_id;
	}
	public String getXzcf_type()
	{
		return xzcf_type;
	}
	public void setXzcf_type(String xzcf_type)
	{
		this.xzcf_type=xzcf_type;
	}
	public String getCf_unit()
	{
		return cf_unit;
	}
	public void setCf_unit(String cf_unit)
	{
		this.cf_unit=cf_unit;
	}
	public String getAu()
	{
		return au;
	}
	public void setAu(String au)
	{
		this.au=au;
	}
	public String getGjz()
	{
		return gjz;
	}
	public void setGjz(String gjz)
	{
		this.gjz=gjz;
	}
	public String getXzcf_code()
	{
		return xzcf_code;
	}
	public void setXzcf_code(String xzcf_code)
	{
		this.xzcf_code=xzcf_code;
	}
	public String getCflx_date()
	{
		return cflx_date;
	}
	public void setCflx_date(String cflx_date)
	{
		this.cflx_date=cflx_date;
	}
	public String getCfsx_date()
	{
		return cfsx_date;
	}
	public void setCfsx_date(String cfsx_date)
	{
		this.cfsx_date=cfsx_date;
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
	public String getXzcf_note()
	{
		return xzcf_note;
	}
	public void setXzcf_note(String xzcf_note)
	{
		this.xzcf_note=xzcf_note;
	}
	public String getFile_html()
	{
		return file_html;
	}
	public void setFile_html(String file_html)
	{
		this.file_html=file_html;
	}
	public int getIf_lx()
	{
		return if_lx;
	}
	public void setIf_lx(int if_lx)
	{
		this.if_lx=if_lx;
	}
	public int getSxcj_id()
	{
		return sxcj_id;
	}
	public void setSxcj_id(int sxcj_id)
	{
		this.sxcj_id=sxcj_id;
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
	public int getExt1()
	{
		return ext1;
	}
	public void setExt1(int ext1)
	{
		this.ext1=ext1;
	}
	public String getExt2()
	{
		return ext2;
	}
	public void setExt2(String ext2)
	{
		this.ext2=ext2;
	}
	public String getExt3()
	{
		return ext3;
	}
	public void setExt3(String ext3)
	{
		this.ext3=ext3;
	}
	public String getIs_warn()
	{
		return is_warn;
	}
	public void setIs_warn(String is_warn)
	{
		this.is_warn=is_warn;
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
	public String getXzcf_zt()
	{
		return xzcf_zt;
	}
	public void setXzcf_zt(String xzcf_zt)
	{
		this.xzcf_zt=xzcf_zt;
	}
	public int getIssue()
	{
		return issue;
	}
	public void setIssue(int issue)
	{
		this.issue=issue;
	}

}