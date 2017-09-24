package com.hits.modules.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;
/**
* @author 
* @time   2016-04-05 10:32:07
*/
@Table("SJJH_LOG")
public class Sjjh_log 
{
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT SJJH_LOG_S.nextval FROM dual")
	})
	private int id;
	@Column
	private String loginname;
	@Column
	private String type;
	@Column
	private String content;
	@Column
	private String time;
	@Column
	private String ip;
	@Column
	private String filepath;
	@Column
	private String xy_type;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public String getLoginname()
	{
		return loginname;
	}
	public void setLoginname(String loginname)
	{
		this.loginname=loginname;
	}
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type=type;
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
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip=ip;
	}
	public String getFilepath()
	{
		return filepath;
	}
	public void setFilepath(String filepath)
	{
		this.filepath=filepath;
	}
	public String getXy_type() {
		return xy_type;
	}
	public void setXy_type(String xy_type) {
		this.xy_type = xy_type;
	}

}