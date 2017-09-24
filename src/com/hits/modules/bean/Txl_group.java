package com.hits.modules.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;

@Table("TXL_GROUP")
public class Txl_group {
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT TXL_GROUP_S.nextval FROM dual")
	})
	private int id;
	@Column
	private String creater;
	@Column
	private String name;
	public int getIsInsider() {
		return isInsider;
	}
	public void setIsInsider(int isInsider) {
		this.isInsider = isInsider;
	}
	@Column
	private String bz;
	@Column
	private int isInsider;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	
	
}
