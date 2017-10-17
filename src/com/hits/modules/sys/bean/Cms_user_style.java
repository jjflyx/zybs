package com.hits.modules.sys.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author Numbgui
* @time   2014-05-20 11:25:55
*/
@Table("CMS_USER_STYLE")
public class Cms_user_style 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT CMS_USER_STYLE_S.nextval FROM dual")
	})
	private int id;
	@Column
	private long user_id;
	@Column
	private String stylename;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public long getUser_id()
	{
		return user_id;
	}
	public void setUser_id(long user_id)
	{
		this.user_id=user_id;
	}
	public String getStylename()
	{
		return stylename;
	}
	public void setStylename(String stylename)
	{
		this.stylename=stylename;
	}

}