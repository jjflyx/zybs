package com.hits.modules.xzqz.bean;

import java.sql.Timestamp;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Wizzer
* @time   2016-03-18 16:19:18
*/
@Table("XZQZ_INFO")
public class Xzqz_info 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT XZQZ_INFO_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int xyzt_id;
	@Column
	private String jg_unitid;
	@Column
	private String jds_code;
	@Column
	private String aj_name;
	@Column
	private String reason;
	@Column
	private String reply;
	@Column
	private String conslusion;
	@Column
	private String cfsx_date;
	@Column
	private String start_date;
	@Column
	private String end_date;
	@Column
	private String zxqk;
	@Column
	private String file_html;
	@Column
	private String actor;
	@Column
	private String unitid;
	@Column
	private String create_date;
	@Column
	private String is_warn;
	@Column
	private String xzcf_zt;
	@Column
	private int issue;
	@Column
	private int ext1;
	@Column
	private String ext2;
	@Column
	private String ext3;
	@Column
	private String xzcf_id;
	@Column
	private String slqk;
	@Column
	private String qzzxqk;
	@Column
	private Timestamp sjc;//时间戳

	public Timestamp getSjc() {
		return sjc;
	}

	public void setSjc(Timestamp sjc) {
		this.sjc = sjc;
	}

	public String getSlqk() {
		return slqk;
	}

	public void setSlqk(String slqk) {
		this.slqk = slqk;
	}

	public String getQzzxqk() {
		return qzzxqk;
	}

	public void setQzzxqk(String qzzxqk) {
		this.qzzxqk = qzzxqk;
	}

	public String getXzcf_id() {
		return xzcf_id;
	}

	public void setXzcf_id(String xzcf_id) {
		this.xzcf_id = xzcf_id;
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
	public String getJg_unitid()
	{
		return jg_unitid;
	}
	public void setJg_unitid(String jg_unitid)
	{
		this.jg_unitid=jg_unitid;
	}
	public String getJds_code()
	{
		return jds_code;
	}
	public void setJds_code(String jds_code)
	{
		this.jds_code=jds_code;
	}
	public String getAj_name()
	{
		return aj_name;
	}
	public void setAj_name(String aj_name)
	{
		this.aj_name=aj_name;
	}
	public String getReason()
	{
		return reason;
	}
	public void setReason(String reason)
	{
		this.reason=reason;
	}
	public String getReply()
	{
		return reply;
	}
	public void setReply(String reply)
	{
		this.reply=reply;
	}
	public String getConslusion()
	{
		return conslusion;
	}
	public void setConslusion(String conslusion)
	{
		this.conslusion=conslusion;
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
	public String getZxqk()
	{
		return zxqk;
	}
	public void setZxqk(String zxqk)
	{
		this.zxqk=zxqk;
	}
	public String getFile_html()
	{
		return file_html;
	}
	public void setFile_html(String file_html)
	{
		this.file_html=file_html;
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
	public String getIs_warn()
	{
		return is_warn;
	}
	public void setIs_warn(String is_warn)
	{
		this.is_warn=is_warn;
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

}