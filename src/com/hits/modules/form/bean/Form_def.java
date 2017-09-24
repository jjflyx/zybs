package com.hits.modules.form.bean;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Prev;
import org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;
/**
* @author frigyes
* @time   2015-06-30 19:29:25
*/
@Table("FORM_DEF")
public class Form_def 
{
	@Column
	@Id
	@Prev({
		@SQL(db = DB.ORACLE, value="SELECT FORM_DEF_S.nextval FROM dual")
	})
	private int defid;
	@Column
	private String formtitle;
	@Column
	private String formdes;
	@Column
	private String formhtml;
	@Column
	private int status;
	@Column
	private int formtype;
	@Column
	private int isdefalut;
	@Column
	private int isgen;
	@Column
	private int version;
	@Column
	private String templateid;
	@Column
	private String creater;
	@Column
	private String createtime;
	@Column
	private int isvalidate;
	@Column
	private String xyml;
	@Column
	private String ywtype;
	@Column
	private String table_type;
	@Column
	private String viewhtml;
  public static final Long DEFAULT_FLOW_FORMID = Long.valueOf(1L);

  public static final Short NOT_GEN = 0;

  public static final Short HAS_GEN = 1;

  public static final Short HAS_Pub = 1;

  public static final Short NOT_Pub = 0;
  public static final String EDIT_INLINE = "edit";
  public static final String EDIT_FORM = "form";
  public static final String EDIT_WINDOW = "window";

  protected Set<Form_table> formTables = new HashSet();

  public Form_table getMainTable()
  {
    Iterator it = this.formTables.iterator();
    while (it.hasNext()) {
      Form_table formTable = (Form_table)it.next();
      if (Form_table.MAIN_TABLE==formTable.getIsmain()) {
        return formTable;
      }
    }
    return null;
  }

  public Form_table getSubTable()
  {
    Iterator it = this.formTables.iterator();
    while (it.hasNext()) {
      Form_table formTable = (Form_table)it.next();
      if (Form_table.MAIN_TABLE!=formTable.getIsmain()) {
        return formTable;
      }
    }
    return null;
  }

  public List<Form_table> getSubTables()
  {
    Iterator it = this.formTables.iterator();
    List formTables = new ArrayList();
    while (it.hasNext()) {
      Form_table formTable = (Form_table)it.next();
      if (Form_table.MAIN_TABLE!=formTable.getIsmain()) {
        formTables.add(formTable);
      }
    }
    return formTables;
  }

  public Form_def()
  {
  }

  public Form_def(int in_formDefId)
  {
    setDefid(in_formDefId);
  }

  public Set<Form_table> getFormTables() {
    return this.formTables;
  }

  public void setFormTables(Set<Form_table> in_formTables) {
    this.formTables = in_formTables;
  }

  public int getStatus()
  {
    return this.status;
  }

  public boolean equals(Object object)
  {
    if (!(object instanceof Form_def)) {
      return false;
    }
    Form_def rhs = (Form_def)object;
    return new EqualsBuilder().append(this.defid, rhs.defid)
      .append(this.formtitle, rhs.formtitle)
      .append(this.formdes, rhs.formdes)
      .append(this.formhtml, rhs.formhtml)
      .append(this.status, rhs.status).isEquals();
  }

  public int hashCode()
  {
    return new HashCodeBuilder(-82280557, -700257973)
      .append(this.defid).append(this.formtitle)
      .append(this.formdes).append(this.formhtml).append(this.status)
      .toHashCode();
  }

  public String toString()
  {
    return new ToStringBuilder(this).append("defid", this.defid)
      .append("formtitle", this.formtitle)
      .append("formdes", this.formdes)
      .append("formhtml", this.formhtml).append("status", this.status)
      .toString();
	}

	public int getDefid() {
		return defid;
	}

	public void setDefid(int defid) {
		this.defid = defid;
	}

	public String getFormtitle() {
		return formtitle;
	}

	public void setFormtitle(String formtitle) {
		this.formtitle = formtitle;
	}

	public String getFormdes() {
		return formdes;
	}

	public void setFormdes(String formdes) {
		this.formdes = formdes;
	}

	public String getFormhtml() {
		return formhtml;
	}

	public void setFormhtml(String formhtml) {
		this.formhtml = formhtml;
	}

	public int getFormtype() {
		return formtype;
	}

	public void setFormtype(int formtype) {
		this.formtype = formtype;
	}

	public int getIsdefalut() {
		return isdefalut;
	}

	public void setIsdefalut(int isdefalut) {
		this.isdefalut = isdefalut;
	}

	public int getIsgen() {
		return isgen;
	}

	public void setIsgen(int isgen) {
		this.isgen = isgen;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getTemplateid() {
		return templateid;
	}

	public void setTemplateid(String templateid) {
		this.templateid = templateid;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public int getIsvalidate() {
		return isvalidate;
	}

	public void setIsvalidate(int isvalidate) {
		this.isvalidate = isvalidate;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getXyml() {
		return xyml;
	}
	public void setXyml(String xyml) {
		this.xyml = xyml;
	}
	public String getYwtype() {
		return ywtype;
	}
	public void setYwtype(String ywtype) {
		this.ywtype = ywtype;
	}
	public String getTable_type() {
		return table_type;
	}
	public void setTable_type(String table_type) {
		this.table_type = table_type;
	}
	public String getViewhtml() {
		return viewhtml;
	}
	public void setViewhtml(String viewhtml) {
		this.viewhtml = viewhtml;
	}
}
