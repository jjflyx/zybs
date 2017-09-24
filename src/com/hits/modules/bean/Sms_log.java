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
@Table("SMS_LOG")
public class Sms_log 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT SMS_LOG_S.nextval FROM dual")
	})
	private int id;
	@Column
	private String mosid;
	@Column
	private String phone;
	@Column
	private String content;
	@Column
	private String time;
	@Column
	private String loginname;
	@Column
	private int type;
	@Column
	private int send_result;
	@Column
	private String error_content;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public String getMosid()
	{
		return mosid;
	}
	public void setMosid(String mosid)
	{
		this.mosid=mosid;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone=phone;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content=content;
	}
	public String getTime()
	{
		return time;
	}
	public void setTime(String time)
	{
		this.time=time;
	}
	public String getLoginname()
	{
		return loginname;
	}
	public void setLoginname(String loginname)
	{
		this.loginname=loginname;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type=type;
	}
	public int getSend_result()
	{
		return send_result;
	}
	public void setSend_result(int send_result)
	{
		this.send_result=send_result;
	}
	public String getError_content()
	{
		return error_content;
	}
	public void setError_content(String error_content)
	{
		this.error_content=error_content;
	}

}