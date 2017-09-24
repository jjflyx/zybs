package com.hits.modules.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author 
* @time   2014-05-06 13:33:35
*/
@Table("MSG_USER")
public class Msg_user 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT MSG_USER_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int msgid;
	@Column
	private String flogin;
	@Column
	private String ftime;
	@Column
	private String jlogin;
	@Column
	private String jtime;
	@Column
	private int jstate;
	@Column
	private int jsign;
	@Column
	private int ext1;
	@Column
	private String ext2;
	@Column
	private String ext3;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public int getMsgid()
	{
		return msgid;
	}
	public void setMsgid(int msgid)
	{
		this.msgid=msgid;
	}
	public String getFlogin()
	{
		return flogin;
	}
	public void setFlogin(String flogin)
	{
		this.flogin=flogin;
	}
	public String getFtime()
	{
		return ftime;
	}
	public void setFtime(String ftime)
	{
		this.ftime=ftime;
	}
	public String getJlogin()
	{
		return jlogin;
	}
	public void setJlogin(String jlogin)
	{
		this.jlogin=jlogin;
	}
	public String getJtime()
	{
		return jtime;
	}
	public void setJtime(String jtime)
	{
		this.jtime=jtime;
	}
	public int getJstate()
	{
		return jstate;
	}
	public void setJstate(int jstate)
	{
		this.jstate=jstate;
	}
	public int getJsign()
	{
		return jsign;
	}
	public void setJsign(int jsign)
	{
		this.jsign=jsign;
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