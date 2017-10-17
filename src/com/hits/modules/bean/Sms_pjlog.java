package com.hits.modules.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author lxy
* @time   2015-06-16 11:13:34
*/
@Table("SMS_PJLOG")
public class Sms_pjlog 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT SMS_PJLOG_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int fsid;
	@Column
	private int jsid;
	@Column
	private String letterid;
	@Column
	private String phone;
	@Column
	private String send_time;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public int getFsid()
	{
		return fsid;
	}
	public void setFsid(int fsid)
	{
		this.fsid=fsid;
	}
	public int getJsid()
	{
		return jsid;
	}
	public void setJsid(int jsid)
	{
		this.jsid=jsid;
	}
	public String getLetterid()
	{
		return letterid;
	}
	public void setLetterid(String letterid)
	{
		this.letterid=letterid;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone=phone;
	}
	public String getSend_time()
	{
		return send_time;
	}
	public void setSend_time(String send_time)
	{
		this.send_time=send_time;
	}

}