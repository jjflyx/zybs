package com.hits.modules.sys.bean;

import java.util.List;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Many;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * @author Wizzer.cn
 * @time   2012-9-13 上午10:54:04
 *
 */
@Table("sys_unit")
public class Sys_unit  
{
	@Column
	@Name
	private String id;
	@Column
	private String unittype;
	@Column
	private String name;
	@Column
	private String namespell;
	@Column
	private String unitcode;
	@Column
	private String descript;
	@Column
	private String address;
	@Column
	private String telephone;
	@Column
	private String email;
	@Column
	private String website;
	@Column
	private String handler;
	@Column
	private String handlerphone;
	@Column
	private String leader;
	@Column
	private String leadphone;
	@Column
	private int location;
	@Column
	private String xy_type;
	@Column
	private String sjdw;
	@Column
	private String xzqh;
	@Many(target = Sys_user.class, field = "unitid")//一个单位下有N个用户
	private List<Sys_user> users;

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id=id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name=name;
	}
	public String getUnitcode()
	{
		return unitcode;
	}
	public void setUnitcode(String unitcode)
	{
		this.unitcode=unitcode;
	}
	public String getDescript()
	{
		return descript;
	}
	public void setDescript(String descript)
	{
		this.descript=descript;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address=address;
	}
	public String getTelephone()
	{
		return telephone;
	}
	public void setTelephone(String telephone)
	{
		this.telephone=telephone;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email=email;
	}
	public String getWebsite()
	{
		return website;
	}
	public void setWebsite(String website)
	{
		this.website=website;
	}
	public int getLocation()
	{
		return location;
	}
	public void setLocation(int location)
	{
		this.location=location;
	}
	public String getUnittype() {
		return unittype;
	}
	public void setUnittype(String unittype) {
		this.unittype = unittype;
	}
	public String getNamespell() {
		return namespell;
	}
	public void setNamespell(String namespell) {
		this.namespell = namespell;
	}
	public String getHandler() {
		return handler;
	}
	public void setHandler(String handler) {
		this.handler = handler;
	}
	public String getHandlerphone() {
		return handlerphone;
	}
	public void setHandlerphone(String handlerphone) {
		this.handlerphone = handlerphone;
	}
	public String getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = leader;
	}
	public String getLeadphone() {
		return leadphone;
	}
	public void setLeadphone(String leadphone) {
		this.leadphone = leadphone;
	}
	public List<Sys_user> getUsers() {
		return users;
	}
	public void setUsers(List<Sys_user> users) {
		this.users = users;
	}

	public String getXy_type() {
		return xy_type;
	}

	public void setXy_type(String xy_type) {
		this.xy_type = xy_type;
	}

	public String getSjdw() {
		return sjdw;
	}

	public void setSjdw(String sjdw) {
		this.sjdw = sjdw;
	}
	public String getXzqh() {
		return xzqh;
	}
	public void setXzqh(String xzqh) {
		this.xzqh = xzqh;
	}
}