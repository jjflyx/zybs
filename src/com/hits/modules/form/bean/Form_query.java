package com.hits.modules.form.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author 
* @time   2016-02-22 10:04:13
*/
@Table("FORM_QUERY")
public class Form_query 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT FORM_QUERY_S.nextval FROM dual")
	})
	private int id;
	@Column
	private int formdefid;
	@Column
	private String queryhtml;
	@Column
	private int foradd;
	@Column
	private int forupdate;
	@Column
	private int fordelete;
	@Column
	private int formakeup;
	@Column
	private int forview;
	@Column
	private String addurl;
	@Column
	private String updateurl;
	@Column
	private String makeupurl;
	@Column
	private String viewurl;
	@Column
	private int width;
	@Column
	private int height;
		public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id=id;
	}
	public int getFormdefid()
	{
		return formdefid;
	}
	public void setFormdefid(int formdefid)
	{
		this.formdefid=formdefid;
	}
	public String getQueryhtml() {
		return queryhtml;
	}
	public void setQueryhtml(String queryhtml) {
		this.queryhtml = queryhtml;
	}
	public int getForadd() {
		return foradd;
	}
	public void setForadd(int foradd) {
		this.foradd = foradd;
	}
	public int getForupdate() {
		return forupdate;
	}
	public void setForupdate(int forupdate) {
		this.forupdate = forupdate;
	}
	public int getFordelete() {
		return fordelete;
	}
	public void setFordelete(int fordelete) {
		this.fordelete = fordelete;
	}
	public int getFormakeup() {
		return formakeup;
	}
	public void setFormakeup(int formakeup) {
		this.formakeup = formakeup;
	}
	public int getForview() {
		return forview;
	}
	public void setForview(int forview) {
		this.forview = forview;
	}
	public String getAddurl() {
		return addurl;
	}
	public void setAddurl(String addurl) {
		this.addurl = addurl;
	}
	public String getUpdateurl() {
		return updateurl;
	}
	public void setUpdateurl(String updateurl) {
		this.updateurl = updateurl;
	}
	public String getMakeupurl() {
		return makeupurl;
	}
	public void setMakeupurl(String makeupurl) {
		this.makeupurl = makeupurl;
	}
	public String getViewurl() {
		return viewurl;
	}
	public void setViewurl(String viewurl) {
		this.viewurl = viewurl;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}