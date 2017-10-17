package com.hits.modules.sys.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author wanfly
* @time   2014-02-26 13:50:16
*/
@Table("SYS_USERPARAMCONFIG")
public class Sys_userparamconfig 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT SYS_USERPARAMCONFIG_S.nextval FROM dual")
	})
	private int id;
	@Column
	private String typename;
	@Column
	private String typevalue;
	@Column
	private String desnote;
	@Column
	private int ext1;
	@Column
	private String ext2;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public String getTypename()
	{
		return typename;
	}
	public void setTypename(String typename)
	{
		this.typename=typename;
	}
	public String getTypevalue()
	{
		return typevalue;
	}
	public void setTypevalue(String typevalue)
	{
		this.typevalue=typevalue;
	}
	public String getDesnote()
	{
		return desnote;
	}
	public void setDesnote(String desnote)
	{
		this.desnote=desnote;
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

}