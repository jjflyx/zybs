package com.hits.modules.form.bean;

import java.util.Comparator;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;
import org.nutz.dao.DB;

/**
 * @author frigyes
 * @time 2015-06-30 19:29:25
 */
@Table("FORM_FIELD")
public class Form_field implements Comparable<Form_field>{
	@Column
	@Id
	private int fieldid;
	@Column
	private int tableid;
	@Column
	private String fieldname;
	@Column
	private String fieldlabel;
	@Column
	private String fieldtype;
	@Column
	private int fieldsize;
	@Column
	private int isprimary;
	@Column
	private int isrequired;
	@Column
	private String fielddscp;
	@Column
	private String foreignkey;
	@Column
	private String foreigntable;
	@Column
	private int islist;
	@Column
	private int isquery;
	@Column
	private String showformat;
	@Column
	private int isdesignshow;//1=设计的可视化2=设计的不可视化0=手工加上
	@Column
	private int status;
	@Column
	private int controltype;
	@Column
	private String regularexp;
	@Column
	private int ispublic;//0-不公开；1-公开
	@Column
	private int location;//排序位置
	@Column
	private String options;//固定选项值
	@Column
	private String loadselect;//select自动加载的参数
	public static final Short FLOW_TITLE = 1;
	public static final Short NOT_FLOW_TITLE = 0;
	public static final Short PRIMARY_KEY = 1;
	public static final Short NOT_PRIMARY_KEY = 0;
	public static final Short IS_SHOW = 1;
	public static final Short UN_SHOW = 2;
	public static final Short HAND_IN = 3;
	public static final Short STATUS_DEL = 1;
	public static final Short STATUS_NOTDEL = 0;
	public static final Short TYPE_MAIN_TABLE = 0;
	public static final Short TYPE_SUB_TABLE = 1;
	public static final Short TYPE_MAIN_FIELD = 2;
	public static final Short TYPE_SUB_FIELD = 3;

	protected transient Form_table formTable;

	public Form_field() {
	}
	public Form_field(int in_fieldId) {
		setFieldid(in_fieldId);
	}

	public Form_table getFormTable() {
		return this.formTable;
	}

	public void setFormTable(Form_table in_formTable) {
		this.formTable = in_formTable;
	}

	public boolean equals(Object object) {
		if (!(object instanceof Form_field)) {
			return false;
		}
		Form_field rhs = (Form_field) object;
		return new EqualsBuilder().append(this.fieldid, rhs.fieldid)
				.append(this.fieldname, rhs.fieldname)
				.append(this.fieldtype, rhs.fieldtype)
				.append(this.isrequired, rhs.isrequired)
				.append(this.fieldsize, rhs.fieldsize)
				.append(this.fielddscp, rhs.fielddscp)
				.append(this.isprimary, rhs.isprimary)
				.append(this.foreignkey, rhs.foreignkey)
				.append(this.foreigntable, rhs.foreigntable)
				.append(this.islist, rhs.islist)
				.append(this.isquery, rhs.isquery).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.fieldid)
				.append(this.fieldname).append(this.fieldtype)
				.append(this.isrequired).append(this.fieldsize)
				.append(this.fielddscp)
				.append(this.isprimary).append(this.foreignkey)
				.append(this.foreigntable).append(this.islist)
				.append(this.isquery).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("fieldid", this.fieldid)
				.append("fieldname", this.fieldname)
				.append("fieldtype", this.fieldtype)
				.append("isrequired", this.isrequired)
				.append("fieldsize", this.fieldsize)
				.append("fielddscp", this.fielddscp)
				.append("isprimary", this.isprimary)
				.append("foreignkey", this.foreignkey)
				.append("foreigntable", this.foreigntable)
				.append("islist", this.islist).append("isquery", this.isquery)
				.toString();
	}

	public int getFieldid() {
		return fieldid;
	}

	public void setFieldid(int fieldid) {
		this.fieldid = fieldid;
	}

	public int getTableid() {
		return tableid;
	}

	public void setTableid(int tableid) {
		this.tableid = tableid;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getFieldlabel() {
		return fieldlabel;
	}

	public void setFieldlabel(String fieldlabel) {
		this.fieldlabel = fieldlabel;
	}

	public String getFieldtype() {
		return fieldtype;
	}

	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}

	public int getFieldsize() {
		return fieldsize;
	}

	public void setFieldsize(int fieldsize) {
		this.fieldsize = fieldsize;
	}

	public int getIsprimary() {
		return isprimary;
	}

	public void setIsprimary(int isprimary) {
		this.isprimary = isprimary;
	}

	public int getIsrequired() {
		return isrequired;
	}

	public void setIsrequired(int isrequired) {
		this.isrequired = isrequired;
	}

	public String getFielddscp() {
		return fielddscp;
	}

	public void setFielddscp(String fielddscp) {
		this.fielddscp = fielddscp;
	}

	public String getForeignkey() {
		return foreignkey;
	}

	public void setForeignkey(String foreignkey) {
		this.foreignkey = foreignkey;
	}

	public String getForeigntable() {
		return foreigntable;
	}

	public void setForeigntable(String foreigntable) {
		this.foreigntable = foreigntable;
	}

	public int getIslist() {
		return islist;
	}

	public void setIslist(int islist) {
		this.islist = islist;
	}

	public int getIsquery() {
		return isquery;
	}

	public void setIsquery(int isquery) {
		this.isquery = isquery;
	}

	public String getShowformat() {
		return showformat;
	}

	public void setShowformat(String showformat) {
		this.showformat = showformat;
	}

	public int getIsdesignshow() {
		return isdesignshow;
	}

	public void setIsdesignshow(int isdesignshow) {
		this.isdesignshow = isdesignshow;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getControltype() {
		return controltype;
	}

	public void setControltype(int controltype) {
		this.controltype = controltype;
	}

	public String getRegularexp() {
		return regularexp;
	}

	public void setRegularexp(String regularexp) {
		this.regularexp = regularexp;
	}
	public int getIspublic() {
		return ispublic;
	}
	public void setIspublic(int ispublic) {
		this.ispublic = ispublic;
	}
	public int getLocation() {
		return location;
	}
	public void setLocation(int location) {
		this.location = location;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public String getLoadselect() {
		return loadselect;
	}
	public void setLoadselect(String loadselect) {
		this.loadselect = loadselect;
	}
	//定义一个内部类来实现比较器
    static class compareToStudent implements Comparator<Form_field>{        

    	public int compare(Form_field f1, Form_field f2) {
    		// TODO Auto-generated method stub
    		int rulst= f1.getLocation() > f2.getLocation()? 1 : (f1.getLocation()==f2.getLocation() ? 0 :-1);
            if(rulst==0)
            {
                    rulst=f1.getFieldname().compareTo(f2.getFieldname());
            }                
            return rulst;
    	}             
    }

//写具体的比较方法
    public int compareTo(Form_field f)                

    {
            int result;
            result=this.location > f.getLocation()? 1 : (this.location==f.getLocation() ? 0 :-1);
            if(result==0)
            {
                    result=this.fieldname.compareTo(f.getFieldname());
            }
            return result;
    }
}
