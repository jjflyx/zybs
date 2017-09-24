package com.hits.modules.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;
/**
* @author 
* @time   2014-05-06 13:33:35
*/
@Table("MSG_FJ")
public class Msg_fj 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT MSG_FJ_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int msgid;
	@Column
	private String fjmc;
	@Column
	private String fjurl;
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
	public String getFjmc()
	{
		return fjmc;
	}
	public void setFjmc(String fjmc)
	{
		this.fjmc=fjmc;
	}
	public String getFjurl()
	{
		return fjurl;
	}
	public void setFjurl(String fjurl)
	{
		this.fjurl=fjurl;
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