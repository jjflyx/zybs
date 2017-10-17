package com.hits.modules.form.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class ParseReult
{
  private Form_table formTable = new Form_table();

  private String template = "";

  private List<String> errors = new ArrayList();

  private Map<String, String> opinionMap = new HashMap();

  public void addError(String error)
  {
    this.errors.add(error);
  }

  public Form_table getFormTable() {
    return this.formTable;
  }

  public void setFormTable(Form_table formTable) {
    this.formTable = formTable;
  }

  public String getTemplate()
  {
    return this.template;
  }

  public void setTemplate(String template)
  {
    this.template = template;
  }

  public List<String> getErrors()
  {
    return this.errors;
  }

  public String getError()
  {
    String error = "";
    for (Iterator it = this.errors.iterator(); it.hasNext(); ) {
      error = error + (String)it.next() + "<br>";
    }
    return error;
  }

  public boolean hasErrors()
  {
    return this.errors.size() > 0;
  }
  public void setErrors(List<String> errors) {
    this.errors = errors;
  }

  public Map<String, String> getOpinionMap()
  {
    return this.opinionMap;
  }

  public void setOpinionMap(Map<String, String> opinionMap) {
    this.opinionMap = opinionMap;
  }

  public void addOpinion(String name, String memo)
  {
    this.opinionMap.put(name, memo);
  }
}