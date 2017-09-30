package com.hits.modules.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Name;
/**
* @author 
* @time   2016-03-03 17:54:28
*/
@Table("FILE_INFO")
public class File_info 
{
	@Column
	@Name
	@Prev({
		@SQL(db = DB.MYSQL, value="SELECT LAST_INSERT_ID()")
	})
	private String id;
	@Column
	private String filename;
	@Column
	private String filepath;
	@Column
	private String filesize;
	@Column
	private String loginname;
	@Column
	private String create_time;
	@Column
	private String tablename;
	@Column
	private String tablekey;
	@Column
	private String fieldname;
	
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getTablekey() {
		return tablekey;
	}
	public void setTablekey(String tablekey) {
		this.tablekey = tablekey;
	}
		public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getFilename()
	{
		return filename;
	}
	public void setFilename(String filename)
	{
		this.filename=filename;
	}
	public String getFilepath()
	{
		return filepath;
	}
	public void setFilepath(String filepath)
	{
		this.filepath=filepath;
	}
	public String getFilesize()
	{
		return filesize;
	}
	public void setFilesize(String filesize)
	{
		this.filesize=filesize;
	}
	public String getLoginname()
	{
		return loginname;
	}
	public void setLoginname(String loginname)
	{
		this.loginname=loginname;
	}
	public String getCreate_time()
	{
		return create_time;
	}
	public void setCreate_time(String create_time)
	{
		this.create_time=create_time;
	}
	public String getFieldname() {
		return fieldname;
	}
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

}