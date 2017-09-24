package com.hits.modules.sys.bean;

import org.nutz.dao.DB;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author Wizzer.cn
 * @time   2012-9-20 下午1:33:32
 *
 */
@Table("sys_role")
public class Sys_role {
	@Column 
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT SYS_ROLE_S.nextval FROM dual")
	})
	private int id;
	@Column
	private String name;
	@Column
	private String unitid;
	@Column
	private String descript;
	@Column
	private int location;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUnitid() {
		return unitid;
	}
	public void setUnitid(String unitid) {
		this.unitid = unitid;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	

}


