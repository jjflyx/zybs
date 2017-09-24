package com.hits.modules.sys.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author Wizzer.cn
 * @time   2012-9-13 下午1:34:26
 *
 */
@Table("sys_role_resource")
public class Sys_role_resource {
	@Column
	private int roleid;
	@Column
	private String resourceid;
	@Column
	private String button;
	public int getRoleid() {
		return roleid;
	}
	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	public String getResourceid() {
		return resourceid;
	}
	public void setResourceid(String resourceid) {
		this.resourceid = resourceid;
	}
	public String getButton() {
		return button;
	}
	public void setButton(String button) {
		this.button = button;
	}
	
}


