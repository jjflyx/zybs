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
@Table("MSG_INFO")
public class Msg_info 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT MSG_INFO_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int infotype;
	@Column
	private int imp;
	@Column
	private int infostate;
	@Column
	private String title;
	@Column
	private String subtitle;
	@Column
	private String fileno;
	@Column
	private String content;
	@Column
	private String unitid;
	@Column
	private String flogin;
	@Column
	private String ctime;
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
	public int getInfotype()
	{
		return infotype;
	}
	public void setInfotype(int infotype)
	{
		this.infotype=infotype;
	}
	public int getImp()
	{
		return imp;
	}
	public void setImp(int imp)
	{
		this.imp=imp;
	}
	public int getInfostate()
	{
		return infostate;
	}
	public void setInfostate(int infostate)
	{
		this.infostate=infostate;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title=title;
	}
	public String getSubtitle()
	{
		return subtitle;
	}
	public void setSubtitle(String subtitle)
	{
		this.subtitle=subtitle;
	}
	public String getFileno()
	{
		return fileno;
	}
	public void setFileno(String fileno)
	{
		this.fileno=fileno;
	}
	public String getContent()
	{
		return content;
	}
	public void setContent(String content)
	{
		this.content=content;
	}
	public String getUnitid()
	{
		return unitid;
	}
	public void setUnitid(String unitid)
	{
		this.unitid=unitid;
	}
	public String getFlogin()
	{
		return flogin;
	}
	public void setFlogin(String flogin)
	{
		this.flogin=flogin;
	}
	public String getCtime()
	{
		return ctime;
	}
	public void setCtime(String ctime)
	{
		this.ctime=ctime;
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