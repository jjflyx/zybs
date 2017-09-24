package com.hits.modules.zcxx.bean;

import java.sql.Timestamp;
import java.util.List;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;
/**
* @author L H T
* @time   2016-03-31 13:55:57
*/
@Table("ZC_INFO")
public class Zc_info 
{
	@Column
	@Name
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT sys_guid() FROM dual")
	})
	private String id;
	@Column
	private String name;
	@Column
	private String idcard;
	@Column
	private String zc_name;
	@Column
	private String zc_card;
	@Column
	private String zc_level;
	@Column
	private String zc_time;
	@Column
	private String sp_unit;
	@Column
	private int zc_state;
	@Column
	private String create_time;
	@Column
	private String create_loginname;
	@Column
	private String zc_zxsj;
	@Column
	private String zc_zxjg;
	@Column
	private String zc_zxyy;
	@Column
	private Timestamp sjc;
	
	public Timestamp getSjc() {
		return sjc;
	}
	public void setSjc(Timestamp sjc) {
		this.sjc = sjc;
	}
	public String getZc_zxsj() {
		return zc_zxsj;
	}
	public void setZc_zxsj(String zc_zxsj) {
		this.zc_zxsj = zc_zxsj;
	}
	public String getZc_zxjg() {
		return zc_zxjg;
	}
	public void setZc_zxjg(String zc_zxjg) {
		this.zc_zxjg = zc_zxjg;
	}
	public String getZc_zxyy() {
		return zc_zxyy;
	}
	public void setZc_zxyy(String zc_zxyy) {
		this.zc_zxyy = zc_zxyy;
	}
	@Many(target = Zc_zx_info.class, field ="zc_id")
	private List<Zc_zx_info> zczxlist;
	
		public String getId()
	{
		return id;
	}
	public void setId(String id)
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
	public String getIdcard()
	{
		return idcard;
	}
	public void setIdcard(String idcard)
	{
		this.idcard=idcard;
	}
	public String getZc_name()
	{
		return zc_name;
	}
	public void setZc_name(String zc_name)
	{
		this.zc_name=zc_name;
	}
	public String getZc_card()
	{
		return zc_card;
	}
	public void setZc_card(String zc_card)
	{
		this.zc_card=zc_card;
	}
	public String getZc_level()
	{
		return zc_level;
	}
	public void setZc_level(String zc_level)
	{
		this.zc_level=zc_level;
	}
	public String getZc_time()
	{
		return zc_time;
	}
	public void setZc_time(String zc_time)
	{
		this.zc_time=zc_time;
	}
	public String getSp_unit()
	{
		return sp_unit;
	}
	public void setSp_unit(String sp_unit)
	{
		this.sp_unit=sp_unit;
	}
	public int getZc_state()
	{
		return zc_state;
	}
	public void setZc_state(int zc_state)
	{
		this.zc_state=zc_state;
	}
	public String getCreate_time()
	{
		return create_time;
	}
	public void setCreate_time(String create_time)
	{
		this.create_time=create_time;
	}
	public String getCreate_loginname()
	{
		return create_loginname;
	}
	public void setCreate_loginname(String create_loginname)
	{
		this.create_loginname=create_loginname;
	}
	public List<Zc_zx_info> getZczxlist() {
		return zczxlist;
	}
	public void setZczxlist(List<Zc_zx_info> zczxlist) {
		this.zczxlist = zczxlist;
	}

}