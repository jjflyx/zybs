package com.hits.modules.form;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.nutz.mvc.Mvcs;

import com.hits.common.util.DecodeUtil;
import com.hits.common.util.StringUtil;
import com.hits.modules.com.comUtil;
import com.hits.modules.form.bean.Form_field;
import com.hits.modules.form.bean.Form_table;
import com.hits.modules.form.bean.Pair;
import com.hits.modules.form.bean.ParseReult;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;

import net.sf.json.JSONObject;

public class FormUtil
{
  private static Log logger = LogFactory.getLog(FormUtil.class);

  public static Map<String, Short> controlTypeMap = new HashMap();
  public static Hashtable<String, String> pxmap = new Hashtable<String, String>();

  static { 
	controlTypeMap.put("hidedomain", Short.valueOf("0"));//隐藏域
    controlTypeMap.put("textfield", Short.valueOf("1"));//单行文本框
    controlTypeMap.put("textarea", Short.valueOf("2"));//多行文本
    controlTypeMap.put("checkbox", Short.valueOf("3"));//多选框
    controlTypeMap.put("radioinput", Short.valueOf("4"));//单选框
    controlTypeMap.put("selectinput", Short.valueOf("5"));//下拉框
    controlTypeMap.put("dictionary", Short.valueOf("6"));//用户所在省厅或市县局或行业协会

    controlTypeMap.put("userselector", Short.valueOf("7"));//信用主体
    controlTypeMap.put("singleuserselector", Short.valueOf("8"));
    controlTypeMap.put("roleselector", Short.valueOf("9"));
    controlTypeMap.put("singleroleselector", Short.valueOf("10"));
    controlTypeMap.put("positionselector", Short.valueOf("11"));
    controlTypeMap.put("singlepositionselector", Short.valueOf("12"));
    controlTypeMap.put("depselector", Short.valueOf("13"));
    controlTypeMap.put("singledepselector", Short.valueOf("14"));

    controlTypeMap.put("attachement", Short.valueOf("15"));
    controlTypeMap.put("datepicker", Short.valueOf("16"));
    controlTypeMap.put("officecontrol", Short.valueOf("17"));
    controlTypeMap.put("ckeditor", Short.valueOf("18"));
    
    controlTypeMap.put("userunit", Short.valueOf("19"));
  }
  private static Element genHtmlBytable(Element el,String tableKey){
	  String external = el.attr("external").replace("&#39;", "\"");
	try {
		JSONObject jsonObject = JSONObject.fromObject(external);
		String fieldName = jsonObject.getString("name");
		if(EmptyUtils.isNotEmpty(tableKey)){
			fieldName = tableKey+"."+fieldName;
		}
		Elements cels = el.children();
		for (Element cel : cels) {
			if("checkbox".equals(el.className())||"userselector".equals(el.className())){
				String readonly = StringUtil.null2String(jsonObject.get("isReadonly"));
				if("input".equals(cel.nodeName())){
					if("hidden".equals(cel.attr("type"))){
						cel.attr("name", fieldName);
					}else{
						if("1".equals(readonly)){
							cel.attr("readonly", "readonly");
							cel.attr("class", "readonlyText");
						}else if("userselector".equals(el.className())){
							cel.attr("readonly", "readonly");
						}
						cel.attr("name", fieldName+"_name");
					}
				}else if("button".equals(cel.nodeName())){
					if("1".equals(readonly)){
						cel.remove();
					}else{
						String isSingle = jsonObject.getString("isSingle");
						cel.attr("onclick", "userselector('"+fieldName+"',"+isSingle+")");
					}
				}
			}else if("radioinput".equals(el.className())){
				if("input".equals(cel.nodeName())){
					if("hidden".equals(cel.attr("type"))){
						cel.attr("name", fieldName);
					}else{
						cel.attr("name", el.className()+"_"+fieldName);
					}
				}
			}else if("datepicker".equals(el.className())){
				cel.attr("name", fieldName);
				JSONObject dbType = (JSONObject)jsonObject.get("dbType");
				String type = dbType.getString("format");
				String method="dateFmt:'"+type+"'";
				//String defaultDate = dbType.getString("defaultDate");
				String maxDate = StringUtil.null2String(dbType.get("maxDate"));
				String minDate = StringUtil.null2String(dbType.get("minDate"));
				if("1".equals(maxDate)||"1".equals(minDate)){
					method+=",maxDate:'%y-%M-%d'";
				}
				cel.attr("onclick", "WdatePicker({"+method+"})");
				cel.attr("readonly", "readonly");
			}else if("attachement".equals(el.className())){
				String nodename=cel.nodeName();
				if("button".equals(nodename)){
					cel.attr("id", fieldName+"_"+nodename);
					cel.attr("fileinputname", fieldName);
				}else{
					if("hidden".equals(cel.attr("type"))){
						String is_name=cel.attr("name");
						if(EmptyUtils.isEmpty(is_name)){
							cel.attr("name", fieldName);
						}else if(EmptyUtils.isNotEmpty(tableKey)){
							cel.attr("name", tableKey+"."+is_name);
						}
					}else{
						cel.empty();
						cel.attr("name", fieldName);
					}
				}
			}else if("hidedomain".equals(el.className())){
				cel.attr("name", fieldName);
				cel.attr("type", "hidden");
			}else if("userunit".equals(el.className())){
				if("input".equals(cel.nodeName())){
					if("hidden".equals(cel.attr("type"))){
						cel.attr("name", fieldName);
					}else{
						cel.attr("name", el.className()+"_"+fieldName);
					}
				}
			}else{
				if("input".equals(cel.nodeName())||"textarea".equals(cel.nodeName())
						||"select".equals(cel.nodeName())){ 
					cel.attr("name", fieldName);
				}
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	  return el;
  }
  private static Document getSubTableHtml(Document doc)
  {
    Elements list = doc.select("div[type=subtable]");

    for (Iterator it = list.iterator(); it.hasNext(); ) {
      Element subTable = (Element)it.next();

      if (subTable.hasAttr("external")) {
        subTable.removeAttr("external");
        subTable.removeClass("subtable");
      }
      String tableKey = subTable.attr("tablekey").toLowerCase();
      if (EmptyUtils.isNotEmpty(tableKey)) {
    	  Elements rows = subTable.select("[external]");
    	  for (Element el : rows) {
			  el=genHtmlBytable(el, tableKey);
			  el.removeAttr("external");
		}
      }
    }
    return doc;
  }
  public static String generateHtml(Document doc){
	  String genHtml="";
	  try {
		  Elements mainFields = doc.select("[external]");
		  for (Element el : mainFields) {
			  if("subtable".equals(el.className())){
				  el.removeAttr("external");
				  continue;
			  }
			  el=genHtmlBytable(el, "");
			  el.removeAttr("external");
		}
		  Elements preels = doc.select("pre");
		  for (Element el : preels) {
			  String clas = el.attr("class").replace("&#39;", "\"");
			  if(clas.startsWith("brush:js")){
				  doc.body().append("<script type=\"text/javascript\">\n"+el.html().replace("&lt;", "<").replace("&gt;", ">").replace("&amp;", "&")+"\n</script>");
			  }
			  el.remove();
		  }
		  genHtml=doc.body().outerHtml();
	  } catch (Exception e) {
		  // TODO: handle exception
	  }
	  return genHtml;
  }
  public static String generateHtml(String formhtml,String type){
	  String genHtml="";
	  try {
		  Document doc = Jsoup.parseBodyFragment(formhtml);
		  Elements removeElets=null;
		  if("add".equals(type)){
			  removeElets= doc.select("[id=formakeup],[id=forupdate]");
		  }else if("makeup".equals(type)){
			  removeElets = doc.select("[id=forupdate]");
		  }else if("view".equals(type)){
			  removeElets = doc.select("[id=forupdate]");
		  }/*else if("0".equals(type)){
			  removeElets = doc.select("[id=forupdate]");
		  }*/
		  if(EmptyUtils.isNotEmpty(removeElets)){
			  for (Element el : removeElets) {
				  el.remove();
			  }
		  }
		  doc=getSubTableHtml(doc);
		  genHtml=generateHtml(doc);
	  } catch (Exception e) {
		  // TODO: handle exception
	  }
	  return genHtml;
  }
  public static String generateQueryHtml(String formhtml){
	  String genHtml="<p id=\"searchForm\">";
	  try {
		  Document doc = Jsoup.parseBodyFragment(formhtml);
		  Elements mainFields = doc.select("[external]");
		  for (Element el : mainFields) {
			  if("subtable".equals(el.className())){
				  el.removeAttr("external");
				  continue;
			  }
			  String external = el.attr("external").replace("&#39;", "\"");
			  JSONObject jsonObject = JSONObject.fromObject(external);
			  String comment = jsonObject.getString("comment");
			  JSONObject search = (JSONObject)jsonObject.get("search");
			  if (search != null) {
				  genHtml+=comment+":"+el.outerHtml();
			  }
			  el.removeAttr("external");
		  }
		  genHtml+="</p>";
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
	  return genHtml;
  }
  public static String getQueryHtml(String formhtml){
	  String genHtml="";
	  String sql="";
	  try {
		  Document doc = Jsoup.parseBodyFragment(formhtml);
		  Elements mainFields = doc.select("[external]");
		  for (Element el : mainFields) {
			  String external = el.attr("external").replace("&#39;", "\"");
			  JSONObject jsonObject = JSONObject.fromObject(external);
			  String fieldName = jsonObject.getString("name");
			  JSONObject search = (JSONObject)jsonObject.get("search");
			  if(EmptyUtils.isEmpty(search)){
				  break;
			  }
			  String condition = search.getString("condition");//like equal likeend
			    String searchFrom = search.getString("searchFrom");//fromForm fromStatic fromScript
			    if ("fromStatic".equals(searchFrom)) {//固定值
			    	String searchValue = search.getString("searchValue");
			    	if("equal".equals(condition)){
			    		if("\"null\"".equals(searchValue)){
			    			sql+=fieldName+" is null and ";
			    		}else{
			    			sql+=fieldName+"='"+searchValue+"' and ";
			    		}
			    	}else if("notequal".equals(condition)){
			    		if("\"null\"".equals(searchValue)){
			    			sql+=fieldName+" is not null and ";
			    		}else{
			    			sql+=fieldName+"!='"+searchValue+"' and ";
			    		}
			    	}else if("like".equals(condition)){
			    		sql+=fieldName+" like '%"+searchValue+"%' and ";
			    	}else if("likeend".equals(condition)){
			    		sql+=fieldName+" like '"+searchValue+"%' and ";
			    	}else if("likeend".equals(condition)){
			    		sql+=fieldName+" like '"+searchValue+"%' and ";
			    	}else if("greaterequal".equals(condition)){
			    		sql+=fieldName+" >= '"+searchValue+"' and ";
			    	}else if("lessequal".equals(condition)){
			    		sql+=fieldName+" <= '"+searchValue+"' and ";
			    	}
			    	Elements cels = el.children();
			    	for (Element cel : cels) {
			    		if("hidedomain".equals(el.className())){
			    			cel.attr("name", fieldName);
			    			cel.attr("type", "hidden");
			    		}
			    	}
			    }else if ("fromForm".equals(searchFrom)) {//表单
			    	Elements cels = el.children();
					  for (Element cel : cels) {
						  if("userselector".equals(el.className())){
							  if("input".equals(cel.nodeName())){
								  if("hidden".equals(cel.attr("type"))){
									  cel.attr("name", fieldName+"/"+condition);
								  }else{
									  cel.attr("name", fieldName+"/"+condition+"_name");
								  }
							  }else if("button".equals(cel.nodeName())){
								  String isSingle = jsonObject.getString("isSingle");
								  cel.attr("onclick", "userselector('"+fieldName+"/"+condition+"',"+isSingle+")");
							  }
						  }else if("datepicker".equals(el.className())){
							  cel.attr("name", fieldName+"/"+condition);
							  JSONObject dbType = (JSONObject)jsonObject.get("dbType");
							  String type = dbType.getString("format");
							  cel.attr("onclick", "WdatePicker({dateFmt:'"+type+"'})");
						  }else{
							  if("input".equals(cel.nodeName())||"textarea".equals(cel.nodeName())
									  ||"select".equals(cel.nodeName())){
								  cel.attr("name", fieldName+"/"+condition);
							  }
						  }
							  cel.removeAttr("verify");
					}
			    }
			  el.removeAttr("external");
		  }
		  String bodyhtml=doc.body().html();
		  String regex="<p id=\"searchForm\">(.*?)</p>";
		  Pattern p =Pattern.compile(regex);
		  Matcher m=p.matcher(bodyhtml);
		  while(m.find()){
			  genHtml+=m.group(1);
		  }
		  if(EmptyUtils.isNotEmpty(sql)){
			  genHtml+="<input type=\"hidden\" name=\"querySql\" value=\""+DecodeUtil.Encrypt(sql)+"\"/>"; 
		  }
	  } catch (Exception e) {
		  e.printStackTrace();
	  }
	  return genHtml;
  }
  
  public static ParseReult parseHtmlNoTable(Map<String, Object> params)
  {
    String defHtml = StringUtil.null2String(params.get("defHtml"));
    String formtype = StringUtil.null2String(params.get("formtype"));
    pxmap=(Hashtable<String, String>)params.get("pxmap");
    ParseReult result = new ParseReult();
    Document doc = Jsoup.parseBodyFragment(defHtml);
    List subTableList=new ArrayList();
    if("2".equals(formtype)){//多表模式
    	subTableList = parseSubTableHtml(doc, result);
    }
    Form_table mainTable = parseMainTable(doc, params, result);
    mainTable.setSubTableList(subTableList);

    result.setFormTable(mainTable);
    return result;
  }

  private static Form_table parseMainTable(Document doc, Map<String, Object> params, ParseReult result)
  {
    String tableKey = (String)params.get("tableKey");
    String tableName = (String)params.get("tableName");
    Form_table formTable = new Form_table();

    formTable.setTablekey(tableKey);
    formTable.setTablename(tableName);
    formTable.setIsmain(Form_table.MAIN_TABLE);

    Elements mainFields = doc.select("[external]");

    if (mainFields.size() == 0) {
      result.addError("主表【" + (tableKey == null ? "" : tableKey) + "," + (
        tableName == null ? "主表" : tableName) + "】尚未定义任何字段");
      return formTable;
    }

    for (Iterator it = mainFields.iterator(); it.hasNext(); ) {
      Element el = (Element)it.next();
      if("subtable".equals(el.className())){
    	  continue;
      }
      Form_field formField = parseExternal(el, result);

      if (formField == null)
      {
        continue;
      }
      String controlName = "m:" + tableKey + ":" + 
        formField.getFieldname();
      if(EmptyUtils.isNotEmpty(pxmap)){
    	  formField.setLocation(StringUtil.StringToInt(pxmap.get(tableKey+"_"+formField.getFieldname()),0));
      }

      boolean rtn = formTable.addField(formField);
      if (!rtn) {
        result.addError("表单中主表:【" + tableKey + "】，字段:【" + 
          formField.getFieldname() + "】重复");
      }
      else
      {
        List list = parseChilds(doc, el, controlName, formField);

        for (Iterator tmpIt = list.iterator(); tmpIt.hasNext(); ) {
          Pair pair = (Pair)tmpIt.next();
          String error = parseMainField(pair.getEl(), pair.getFieldName());
          if (EmptyUtils.isNotEmpty(error)) {
            result.addError(error);
          }
        }

        removeWrap(el);
      }
    }
    return formTable;
  }

  private static void removeWrap(Element parentEl)
  {
    for (Iterator it = parentEl.children().iterator(); it
      .hasNext(); )
    {
      Node elClone = (Node)it.next();
      parentEl.before(elClone);
    }
    parentEl.remove();
  }

  private static List<Pair> parseChilds(Document doc, Element parentEl, String controlName, Form_field formField)
  {
    List list = new ArrayList();
    Elements elList = parentEl.select("input,select,textarea");
    for (Iterator it = elList.iterator(); it.hasNext(); ) {
      Element el = (Element)it.next();
      Pair pair = new Pair(el, formField.getFieldname());
      list.add(pair);
    }
    return list;
  }

  private static String parseMainField(Element el, String fieldName)
  {
    String controltype = el.attr("controltype");

    String type = el.attr("type").toLowerCase();

    if ("attachment".equalsIgnoreCase(controltype)) {
      Element parent = getContainer(el, "div_attachment_container");
      parent.attr("right", "${service.getFieldRight('" + fieldName + 
        "',  permission)}");

      el.val("${service.getFieldValue('" + fieldName + "',model)}");
    }
    else if ("office".equalsIgnoreCase(controltype)) {
      el.attr("value", "${service.getFieldValue('" + fieldName + 
        "',model)}");

      el.attr("right", "${service.getFieldRight('" + fieldName + 
        "',  permission)}");
    }
    else if (("checkbox".equalsIgnoreCase(type)) || 
      ("radio".equalsIgnoreCase(type))) {
      String value = el.attr("value");

      el.attr("chk", "1").attr("disabled", "disabled");
      Element elParent = el.parent();
      String parentNodeName = elParent.nodeName();
      if (!parentNodeName.equals("label")) {
        return fieldName + 
          "的html代码必须为<label><input type='checkbox|radio' value='是'/>是</label>的形式";
      }

      String tmp = parentNodeName.equals("label") ? elParent.toString() : 
        el.toString();

      String str = "<span>&lt;#assign " + fieldName + "Html&gt;" + tmp + 
        " &lt;/#assign&gt;" + "\r\n${service.getRdoChkBox('" + 
        fieldName + "', " + fieldName + "Html,'" + value + 
        "', model, permission)}</span>";
      elParent.before(str);
      elParent.remove();
    }
    else if (el.nodeName().equalsIgnoreCase("textarea")) {
      el.append("#value");
      String str = "<span>&lt;#assign " + fieldName + "Html&gt;" + 
        el.toString() + " &lt;/#assign&gt;" + 
        "\r\n${service.getField('" + fieldName + "'," + fieldName + 
        "Html, model, permission)}</span>";
      el.before(str);
      el.remove();
    }
    else if (el.nodeName().equalsIgnoreCase("input")) {
      el.attr("value", "#value");
      String str = "&lt;#assign " + fieldName + "Html&gt;" + 
        el.toString() + " &lt;/#assign&gt;" + 
        "\r\n${service.getField('" + fieldName + "'," + fieldName + 
        "Html, model, permission)}";

      if ("hidden".equalsIgnoreCase(type)) {
        str = "&lt;#assign " + fieldName + "Html&gt;" + el.toString() + 
          " &lt;/#assign&gt;" + 
          "\r\n${service.getHiddenField('" + fieldName + "'," + 
          fieldName + "Html, model, permission)}";
      }
      el.before(str);
      el.remove();
    }
    else if (el.nodeName().equalsIgnoreCase("select")) {
      el.attr("val", "#value");
      String str = "&lt;#assign " + fieldName + "Html&gt;" + 
        el.toString() + " &lt;/#assign&gt;" + 
        "\r\n${service.getField('" + fieldName + "'," + fieldName + 
        "Html, model, permission)}";
      el.before(str);
      el.remove();
    }
    else if (el.nodeName().equalsIgnoreCase("a")) {
      String str = "&lt;#assign " + fieldName + "Html&gt;" + 
        el.toString() + " &lt;/#assign&gt;" + 
        "\r\n${service.getLink('" + fieldName + "'," + fieldName + 
        "Html, model, permission)}";
      el.before(str);
      el.remove();
    }
    return "";
  }

  private static Element getContainer(Element node, String containerName)
  {
    Element parent = node;
    while ((parent = parent.parent()) != null) {
      String name = parent.attr("name");
      if (containerName.equals(name)) {
        return parent;
      }
    }
    return node;
  }

  private static Form_field parseExternal(Element el, ParseReult result)
  {
    Form_field formField = new Form_field();
    String external = el.attr("external").replace("&#39;", "\"");

    el.removeAttr("external");
    JSONObject jsonObject = null;
    try {
      jsonObject = JSONObject.fromObject(external);
    } catch (Exception ex) {
      result.addError(external + "错误的JSON格式!");
      return null;
    }

    String fieldName = jsonObject.getString("name");

    JSONObject dbType = (JSONObject)jsonObject.get("dbType");

    String fieldLabel = jsonObject.getString("comment");
    if (EmptyUtils.isEmpty(fieldName)) {
      result.addError(external + "没有定义字段名");
      return null;
    }
    if (dbType == null) {
      result.addError(dbType + ",没有定义字段类型。");
      return null;
    }
    formField.setIsdesignshow(1);
    formField.setFieldname(fieldName);
    formField.setFieldlabel(fieldLabel == null ? fieldName : fieldLabel);
    if(jsonObject.containsKey("options")){
    	String options = StringUtil.null2String(jsonObject.getString("options"));
    	formField.setOptions(options);
    }
    if(jsonObject.containsKey("loadselect")){
    	String loadselect = StringUtil.null2String(jsonObject.getString("loadselect"));
    	formField.setLoadselect(loadselect);
    }
    handFieldType(dbType, formField, result);

    handOption(jsonObject, formField);

    handControlType(el, jsonObject, formField);

    return formField;
  }

  private static void handOption(JSONObject jsonObject, Form_field formField)
  {
    Object isRequired = jsonObject.get("isRequired");
    if (isRequired == null)
      formField.setIsrequired(Short.valueOf("0"));
    else {
      formField.setIsrequired(Short.valueOf(Short.parseShort(isRequired.toString())));
    }

    Object isList = jsonObject.get("isList");
    if (isList == null)
      formField.setIslist(Short.valueOf("0"));
    else {
      formField.setIslist(Short.valueOf(Short.parseShort(isList.toString())));
    }

    Object isQuery = jsonObject.get("isQuery");
    if (isQuery == null)
      formField.setIsquery(Short.valueOf("0"));
    else {
      formField.setIsquery(Short.valueOf(Short.parseShort(isQuery.toString())));
    }
    
    Object isPublic = jsonObject.get("isPublic");
    if (isPublic == null)
      formField.setIspublic(Short.valueOf("0"));
    else {
      formField.setIspublic(Short.valueOf(Short.parseShort(isPublic.toString())));
    }

    Object isPrimary = jsonObject.get("isPrimary");
    if (isPrimary == null)
      formField.setIsprimary(Short.valueOf("0"));
    else
      formField.setIsprimary(Short.valueOf(Short.parseShort(isPrimary.toString())));
  }

  private static void handControlType(Element el, JSONObject jsonObject, Form_field formField)
  {
    Short controlType = (Short)controlTypeMap.get("textfield");
    String clsName = el.attr("class").toLowerCase();

    if (controlTypeMap.containsKey(clsName)) {
      controlType = (Short)controlTypeMap.get(clsName);
    }
    if ((clsName.equals("userselector")) || (clsName.equals("roleselector")) || 
      (clsName.equals("positionselector")) || 
      (clsName.equals("depselector")))
    {
      if ((jsonObject.containsKey("isSingle")) && 
        (jsonObject.getInt("isSingle") == 1)) {
        controlType = Short.valueOf((short)(controlType.shortValue() + 1));
      }
    }

    formField.setControltype(controlType);
  }

  private static void handFieldType(JSONObject dbType, Form_field formField, ParseReult result)
  {
    if (!dbType.containsKey("type")) {
      result.addError("字段:" + formField.getFieldname() + "," + 
        formField.getFieldlabel() + ",没有设置数据类型!");
      return;
    }

    String type = dbType.getString("type");
    if (EmptyUtils.isEmpty(type)) {
      result.addError("字段:" + formField.getFieldname() + "," + 
        formField.getFieldlabel() + ",没有设置数据类型!");
      return;
    }
    if (!isValidType(type)) {
      result.addError("字段:" + formField.getFieldname() + "," + 
        formField.getFieldlabel() + ",数据类型设置错误:" + type);
      return;
    }

    formField.setFieldsize(Integer.valueOf(0));
    formField.setFieldtype(type);
    formField.setShowformat(null);
    if ("varchar".equals(type)) {
      if (!dbType.containsKey("length")) {
        result.addError("字段:" + formField.getFieldname() + "," + 
          formField.getFieldlabel() + ",数据类型(“文本”)长度未设置。");
        return;
      }

      int length = dbType.getInt("length");
      formField.setFieldsize(Integer.valueOf(length));
    }
    else if ("number".equals(type)) {
      if (!dbType.containsKey("intLen")) {
        result.addError("字段:" + formField.getFieldname() + "," + 
          formField.getFieldlabel() + ",数据类型(“数字”)数据长度未设置。");
        return;
      }
      int intLen = dbType.getInt("intLen");
      int decimalLen = 0;
      if (dbType.containsKey("decimalLen")) {
        decimalLen = dbType.getInt("decimalLen");
      }

      formField.setFieldsize(Integer.valueOf(intLen));
    } else if ("date".equals(type))
    {
      setDateFormat(dbType, formField);
    }
  }

  private static boolean isValidType(String type)
  {
    return (type.equals("varchar")) || (type.equals("number")) || 
      (type.equals("date")) || (type.equals("clob"));
  }

  private static void setDateFormat(JSONObject dbType, Form_field formField)
  {
    JSONObject jsonObj = JSONObject.fromObject("{}");

    String format = (String)dbType.get("format");
    if (format == null)
      jsonObj.element("format", "yyyy-MM-dd");
    else {
      jsonObj.element("format", format);
    }

    Object displayDate = dbType.get("displayDate");
    if (displayDate == null)
      jsonObj.element("displayDate", 0);
    else {
      jsonObj.element("displayDate", 
        Integer.parseInt(displayDate.toString()));
    }
    formField.setShowformat(format);
  }

  private static List<Form_table> parseSubTableHtml(Document doc, ParseReult result)
  {
    List subList = new ArrayList();
    Elements list = doc.select("div[type=subtable]");

    for (Iterator it = list.iterator(); it.hasNext(); ) {
      Element subTable = (Element)it.next();

      if (subTable.hasAttr("external")) {
        subTable.removeAttr("external");
        subTable.removeClass("subtable");
      }

      Form_table table = new Form_table();

      String tableKey = subTable.attr("tablekey").toLowerCase();
      if (EmptyUtils.isEmpty(tableKey)) {
        result.addError("有子表对象没有设置表名。");
      }
      else {
        String tableName = subTable.attr("tablename");
        if (EmptyUtils.isEmpty(tableName)) {
          result.addError("有子表描述未设置。");
        }
        else {
          table.setTablekey(tableKey);
          table.setTablename(tableName);
          table.setIsmain(Short.valueOf("0"));
          subList.add(table);

          subTable.attr("right", "${service.getSubTablePermission('" + 
            tableKey + "', permission)}");

          Elements rows = subTable.select("[formtype=edit],[formtype=form]");
          if (rows.size() == 0) {
            logger.debug("no formtype row defined");
            result.addError("子表【" + tableKey + "】没有定义属性【formtype】。");
          }
          else
          {
            Element row = rows.get(0);

            String mode = row.attr("formtype");

            if ("edit".equals(mode)) {
              parseSubTableEditField(doc, row, table, result);
            }

            Element newRow = row.clone().attr("formtype", "newrow");

            row.after(newRow);

            newRow.before("&lt;#if model.sub." + tableKey + 
              " != null&gt; &lt;#list model.sub." + tableKey + 
              ".dataList as table&gt;");
            newRow.after("&lt;/#list> &lt;/#if&gt;");

            if ("edit".equals(mode)) {
              parseSubTableEditField(newRow);
            }
            else if ("form".equals(mode)) {
              Elements windowRows = subTable.select("[formtype=window]");
              if (windowRows.size() != 1) {
                logger.debug("window mode hasn't window defined");
                result.addError("在弹出窗口模式下，子表【" + tableKey + 
                  "】没有设置window属性的对象。");
              }
              else {
                Element window = windowRows.get(0);
                parseSubTableFormField(doc, row, newRow, window, table, result);
              }
            }
          }
        }
      }
    }
    return subList;
  }
  

  private static void parseSubTableEditField(Document doc, Element newRow, Form_table table, ParseReult result)
  {
    Elements fields = newRow.select("[external]");
    String tableKey = table.getTablekey();
    String tableName = table.getTablename();
    if (fields.size() == 0) {
      result.addError("子表【" + tableKey + "," + (
        tableName.equals("") ? "子表" : tableName) + "】尚未定义任何字段");
      return;
    }
    for (Iterator it = fields.iterator(); it.hasNext(); ) {
      Element el = (Element)it.next();

      Form_field formField = parseExternal(el, result);
      if(EmptyUtils.isNotEmpty(pxmap)){
    	  formField.setLocation(StringUtil.StringToInt(pxmap.get(tableKey+"_"+formField.getFieldname()),0));
      }

      boolean rtn = table.addField(formField);

      String fieldName = formField.getFieldname();

      if (!rtn) {
        result.addError("表单中子表:【" + tableKey + "】，字段:【" + fieldName + 
          "】重复!");
      }
      else
      {
        String controlName = "s:" + tableKey + ":" + fieldName;
        parseChilds(doc, el, controlName, formField);

        removeWrap(el);
      }
    }
  }

  private static void parseSubTableEditField(Element newRow)
  {
    Elements fields = newRow.select("[name^=s:]");
    for (Iterator it = fields.iterator(); it.hasNext(); ) {
      Element el = (Element)it.next();
      handSubFieldValuePermission(el);
    }
  }

  private static void handSubFieldValuePermission(Element el)
  {
    String nodeName = el.nodeName();
    String name = el.attr("name");
    String type = el.attr("type").toLowerCase();

    String fieldName = name.replaceAll("^.*:", "").toLowerCase();

    if ("textarea".equals(nodeName)) {
      el.append("${table." + fieldName + "}");
    }
    else if (("checkbox".equals(type)) || ("radio".equals(type))) {
      el.attr("chk", "1");
      String value = el.attr("value");
      el.before("${service.getRdoChkBox('" + fieldName + "', '" + 
        el.toString() + "','" + value + "', table)}");
      el.remove();
    }
    else if (("select".equals(nodeName)) || ("input".equals(nodeName))) {
      el.attr("value", "${table." + fieldName + "}");
    }
  }

  private static void parseSubTableFormField(Document doc, Element row, Element newRow, Element window, Form_table table, ParseReult result)
  {
    String tableKey = table.getTablekey();
    String tableName = table.getTablename();

    Elements rowFields = row.select("[fieldname]");
    if (rowFields.size() == 0) {
      result.addError("表:" + tableKey + "," + tableName + 
        ",弹窗编辑模式，显示行没有定义任何字段");
      return;
    }

    for (Iterator it = rowFields.iterator(); it.hasNext(); ) {
      Element el = (Element)it.next();
      String fieldname = el.attr("fieldname").toLowerCase();

      el.attr("fieldname", "s:" + tableKey + ":" + fieldname);
    }

    Elements newRowfields = newRow.select("[fieldname]");
    for (Iterator it = newRowfields.iterator(); it.hasNext(); ) {
      Element el = (Element)it.next();
      String fieldname = el.attr("fieldname").toLowerCase();

      el.attr("fieldname", "s:" + tableKey + ":" + fieldname);

      el.append("${table." + fieldname + "}");
    }

    Elements windowFields = window.select("[external]");

    if (rowFields.size() == 0) {
      result.addError("表:" + tableKey + "," + tableName + 
        ",弹窗编辑模式，窗口没有定义任何字段");
      return;
    }
    for (Iterator it = windowFields.iterator(); it.hasNext(); ) {
      Element el = (Element)it.next();

      Form_field formField = parseExternal(el, result);
      if (formField == null) {
        continue;
      }
      boolean rtn = table.addField(formField);

      String fieldName = formField.getFieldname();

      if (!rtn) {
        result.addError("表单中子表表:【" + tableKey + "】，字段:【" + fieldName + 
          "】重复!");
      }
      else
      {
        String name = "s:" + tableKey + ":" + fieldName;
        Element appendTag = doc.createElement("input")
          .attr("type", "hidden").attr("name", name)
          .attr("value", "${table." + fieldName + "}");
        newRow.children().last().after(appendTag);

        parseChilds(doc, el, name, formField);

        removeWrap(el);
      }
    }
  }
}