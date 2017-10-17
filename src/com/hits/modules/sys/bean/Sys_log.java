package com.hits.modules.sys.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;
/**
 * 
 *  #(c) IFlytek 12345_new <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: 系统日志
 * 
 *  <br/>创建说明: 2015年5月28日 下午12:23:48 (☆_☆)  创建文件<br/>
 * 
 *  修改历史:<br/>
 *
 */
@Table("sys_log")
public class Sys_log 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT SYS_LOG_S.nextval FROM dual")
	})
	private int id;
	@Column
	private String loginname;
	@Column
	private int type;
	@Column
	private String content;
	@Column
	private String create_time;
	@Column
	private String login_ip;
	@Column
	private String bowser;
	@Column
	private String letter_id;
	@Column
	private String class_name;
	@Column
	private String method_name;
	@Column
	private String title;
	@Column
	private String note;
	@Column
	private String reason;
	@Column
	private String basis;
	@Column
	private String cz;
	@Column
	private String success;
	@Column
	private String url;
	@Column
	private String xgfj;

	public String getXgfj() {
		return xgfj;
	}
	public void setXgfj(String xgfj) {
		this.xgfj = xgfj;
	}
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
	public int getType()
	{
		return type;
	}
	public void setType(int type)
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
	public String getCreate_time()
	{
		return create_time;
	}
	public void setCreate_time(String create_time)
	{
		this.create_time=create_time;
	}
	public String getLogin_ip()
	{
		return login_ip;
	}
	public void setLogin_ip(String login_ip)
	{
		this.login_ip=login_ip;
	}
	public String getBowser()
	{
		return bowser;
	}
	public void setBowser(String bowser)
	{
		this.bowser=bowser;
	}
	public String getLetter_id()
	{
		return letter_id;
	}
	public void setLetter_id(String letter_id)
	{
		this.letter_id=letter_id;
	}
	public String getMethod_name()
	{
		return method_name;
	}
	public void setMethod_name(String method_name)
	{
		this.method_name=method_name;
	}
	public String getClass_name() {
		return class_name;
	}
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getBasis() {
		return basis;
	}

	public void setBasis(String basis) {
		this.basis = basis;
	}

	public String getCz() {
		return cz;
	}

	public void setCz(String cz) {
		this.cz = cz;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
}