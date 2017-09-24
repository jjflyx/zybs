package com.hits.modules.gtxt.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Numbgui
* @time   2016-02-29 10:39:07
*/
@Table("XYZT_INFO")
public class Xyzt_info 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT XYZT_INFO_S.nextval FROM dual")
	})
	private int id;
	@Column
	private String name;
	@Column
	private String code;
	@Column
	private String tel;
	@Column
	private String phone;
	@Column
	private String zzjgdm;
	@Column
	private String fzr;
	@Column
	private String gszch;
	@Column
	private String swdjh;
	@Column
	private String create_date;
	@Column
	private String state;
	@Column
	private String hzrq;
	@Column
	private String company_type;
	@Column
	private String company_addr;
	@Column
	private String djjg;
	@Column
	private String fr_name;
	@Column
	private String fr_code;
	@Column
	private String actor;
	@Column
	private String acreate_time;
	@Column
	private int modetype;
	@Column
	private int local;
	@Column
	private String type;
	@Column
	private Integer sex;
	@Column
	private String home_addr;
	@Column
	private String csrq;
	@Column
	private String zc_addr;
	@Column
	private String xylx;
	@Column
	private String jggm;
	@Column
	private int status;//状态


	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getJggm() {
		return jggm;
	}

	public void setJggm(String jggm) {
		this.jggm = jggm;
	}

	public String getXylx() {
		return xylx;
	}
	public void setXylx(String xylx) {
		this.xylx = xylx;
	}

	public String getZc_addr() {
		return zc_addr;
	}

	public void setZc_addr(String zc_addr) {
		this.zc_addr = zc_addr;
	}

	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getCode()
	{
		return code;
	}
	public void setCode(String code)
	{
		this.code=code;
	}
	public String getTel()
	{
		return tel;
	}
	public void setTel(String tel)
	{
		this.tel=tel;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone=phone;
	}
	public String getZzjgdm()
	{
		return zzjgdm;
	}
	public void setZzjgdm(String zzjgdm)
	{
		this.zzjgdm=zzjgdm;
	}
	public String getFzr()
	{
		return fzr;
	}
	public void setFzr(String fzr)
	{
		this.fzr=fzr;
	}
	public String getGszch()
	{
		return gszch;
	}
	public void setGszch(String gszch)
	{
		this.gszch=gszch;
	}
	public String getSwdjh()
	{
		return swdjh;
	}
	public void setSwdjh(String swdjh)
	{
		this.swdjh=swdjh;
	}
	public String getCreate_date()
	{
		return create_date;
	}
	public void setCreate_date(String create_date)
	{
		this.create_date=create_date;
	}
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state=state;
	}
	public String getHzrq()
	{
		return hzrq;
	}
	public void setHzrq(String hzrq)
	{
		this.hzrq=hzrq;
	}
	public String getCompany_type()
	{
		return company_type;
	}
	public void setCompany_type(String company_type)
	{
		this.company_type=company_type;
	}
	public String getCompany_addr()
	{
		return company_addr;
	}
	public void setCompany_addr(String company_addr)
	{
		this.company_addr=company_addr;
	}
	public String getDjjg()
	{
		return djjg;
	}
	public void setDjjg(String djjg)
	{
		this.djjg=djjg;
	}
	public String getFr_name()
	{
		return fr_name;
	}
	public void setFr_name(String fr_name)
	{
		this.fr_name=fr_name;
	}
	public String getFr_code()
	{
		return fr_code;
	}
	public void setFr_code(String fr_code)
	{
		this.fr_code=fr_code;
	}
	public String getActor()
	{
		return actor;
	}
	public void setActor(String actor)
	{
		this.actor=actor;
	}
	public String getAcreate_time()
	{
		return acreate_time;
	}
	public void setAcreate_time(String acreate_time)
	{
		this.acreate_time=acreate_time;
	}
	public int getModetype()
	{
		return modetype;
	}
	public void setModetype(int modetype)
	{
		this.modetype=modetype;
	}
	public int getLocal()
	{
		return local;
	}
	public void setLocal(int local)
	{
		this.local=local;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type=type;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getHome_addr() {
		return home_addr;
	}

	public void setHome_addr(String home_addr) {
		this.home_addr = home_addr;
	}

	public String getCsrq() {
		return csrq;
	}

	public void setCsrq(String csrq) {
		this.csrq = csrq;
	}
}