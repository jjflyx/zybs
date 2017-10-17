package com.hits.modules.form.bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

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
@Table("FORM_TABLE")
public class Form_table {
	@Column
	@Id
	private int tableid;
	@Column
	private int formdefid;
	@Column
	private String tablename;
	@Column
	private String tablekey;
	@Column
	private int ismain;
	@Column
	private String creater;
	@Column
	private String catetetime;
	@Column
	private String form_type;
	public static final String TABLE_PRE_NAME = "WF_";
	public static final int MAIN_TABLE = 1;

	public static final int NOT_MAIN_TABLE = 0;

	protected transient Form_def formDef;

	protected Set<Form_field> formFields = new TreeSet<Form_field>();

	protected Set<String> hashSet = new HashSet();

	protected List<Form_table> subTableList = new ArrayList();

	public Form_field getPrimaryField() {
		Iterator it = this.formFields.iterator();
		while (it.hasNext()) {
			Form_field formField = (Form_field) it.next();
			if (Form_field.PRIMARY_KEY==formField.getIsprimary()) {
				return formField;
			}
		}
		return null;
	}

	public String getEntityName() {
		return "WF_" + this.tablekey;
	}

	public Form_table() {
	}

	public Form_table(int in_tableId) {
		setTableid(in_tableId);
	}

	public Form_def getFormDef() {
		return this.formDef;
	}

	public void setFormDef(Form_def in_formDef) {
		this.formDef = in_formDef;
	}

	public Set<Form_field> getFormFields() {
		return this.formFields;
	}
	public JSONArray getFormFieldsJson() {
		JsonConfig jsonConfig = new JsonConfig();
	    jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
	    JSONArray json = JSONArray.fromObject(this.formFields, jsonConfig);
		return json;
	}
	public void setFormFields(Set<Form_field> in_formFields) {
		this.formFields = in_formFields;
	}

	public boolean equals(Object object) {
		if (!(object instanceof Form_table)) {
			return false;
		}
		Form_table rhs = (Form_table) object;
		return new EqualsBuilder().append(this.tableid, rhs.tableid)
				.append(this.tablename, rhs.tablename)
				.append(this.tablekey, rhs.tablekey).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.tableid)
				.append(this.tablename).append(this.tablekey).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("tableid", this.tableid)
				.append("tablename", this.tablename)
				.append("tablekey", this.tablekey).toString();
	}

	public Set<String> getHashSet() {
		return this.hashSet;
	}

	public void setHashSet(Set<String> hashSet) {
		this.hashSet = hashSet;
	}

	public List<Form_table> getSubTableList() {
		return this.subTableList;
	}

	public void setSubTableList(List<Form_table> subTableList) {
		this.subTableList = subTableList;
	}

	public boolean addField(Form_field formField) {
		String fieldName = formField.getFieldname().toLowerCase();
		int controlType = formField.getControltype();

		if (this.hashSet.contains(fieldName)) {
			return (controlType == 3) || (controlType == 4);
		}

		this.hashSet.add(fieldName);
		this.formFields.add(formField);
		return true;
	}
	public boolean addFields(List<Form_field> fields) {
		boolean bol=false;
		for (Form_field formField : fields) {
			bol=addField(formField);
		}
		return bol;
	}

	public int getTableid() {
		return tableid;
	}

	public void setTableid(int tableid) {
		this.tableid = tableid;
	}

	public int getFormdefid() {
		return formdefid;
	}

	public void setFormdefid(int formdefid) {
		this.formdefid = formdefid;
	}

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

	public int getIsmain() {
		return ismain;
	}

	public void setIsmain(int ismain) {
		this.ismain = ismain;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCatetetime() {
		return catetetime;
	}

	public void setCatetetime(String catetetime) {
		this.catetetime = catetetime;
	}

	public String getForm_type() {
		return form_type;
	}

	public void setForm_type(String form_type) {
		this.form_type = form_type;
	}
}
