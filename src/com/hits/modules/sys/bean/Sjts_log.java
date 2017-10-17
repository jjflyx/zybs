package com.hits.modules.sys.bean;

import java.util.Date;

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
@Table("log_stsjjh")
public class Sjts_log 
{
	@Column
	@Id
	private int id_batch;
	@Column
	private String channel_id;
	@Column
	private String transname;
	@Column
	private String status;
	@Column
	private String LINES_READ;
	@Column
	private String LINES_WRITTEN;
	@Column
	private String LINES_UPDATED;
	@Column
	private String LINES_INPUT;
	@Column
	private String LINES_OUTPUT;
	@Column
	private String LINES_REJECTED;
	@Column
	private int errors;
	@Column
	private Date startdate;
	@Column
	private Date enddate;
	@Column
	private Date LOGDATE;
	@Column
	private Date DEPDATE;
	@Column
	private Date REPLAYDATE;
	@Column
	private String log_field;
	public int getId_batch() {
		return id_batch;
	}
	public void setId_batch(int id_batch) {
		this.id_batch = id_batch;
	}
	public String getChannel_id() {
		return channel_id;
	}
	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}
	public String getTransname() {
		return transname;
	}
	public void setTransname(String transname) {
		this.transname = transname;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLINES_READ() {
		return LINES_READ;
	}
	public void setLINES_READ(String lINES_READ) {
		LINES_READ = lINES_READ;
	}
	public String getLINES_WRITTEN() {
		return LINES_WRITTEN;
	}
	public void setLINES_WRITTEN(String lINES_WRITTEN) {
		LINES_WRITTEN = lINES_WRITTEN;
	}
	public String getLINES_UPDATED() {
		return LINES_UPDATED;
	}
	public void setLINES_UPDATED(String lINES_UPDATED) {
		LINES_UPDATED = lINES_UPDATED;
	}
	public String getLINES_INPUT() {
		return LINES_INPUT;
	}
	public void setLINES_INPUT(String lINES_INPUT) {
		LINES_INPUT = lINES_INPUT;
	}
	public String getLINES_OUTPUT() {
		return LINES_OUTPUT;
	}
	public void setLINES_OUTPUT(String lINES_OUTPUT) {
		LINES_OUTPUT = lINES_OUTPUT;
	}
	public String getLINES_REJECTED() {
		return LINES_REJECTED;
	}
	public void setLINES_REJECTED(String lINES_REJECTED) {
		LINES_REJECTED = lINES_REJECTED;
	}
	public int getErrors() {
		return errors;
	}
	public void setErrors(int errors) {
		this.errors = errors;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	public Date getLOGDATE() {
		return LOGDATE;
	}
	public void setLOGDATE(Date lOGDATE) {
		LOGDATE = lOGDATE;
	}
	public Date getDEPDATE() {
		return DEPDATE;
	}
	public void setDEPDATE(Date dEPDATE) {
		DEPDATE = dEPDATE;
	}
	public Date getREPLAYDATE() {
		return REPLAYDATE;
	}
	public void setREPLAYDATE(Date rEPLAYDATE) {
		REPLAYDATE = rEPLAYDATE;
	}
	public String getLog_field() {
		return log_field;
	}
	public void setLog_field(String log_field) {
		this.log_field = log_field;
	}
}