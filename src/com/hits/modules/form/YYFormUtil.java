package com.hits.modules.form;


import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.hits.common.config.Globals;
import com.hits.common.dao.ObjectCtl;
import com.hits.common.util.DateUtil;
import com.hits.common.util.StringUtil;
import com.hits.modules.bean.File_info;
import com.hits.modules.cgtable.util.CommonStaticUtil;
import com.hits.modules.com.comUtil;
import com.hits.modules.form.bean.Form_field;
import com.hits.modules.form.bean.Form_table;
import com.hits.util.EmptyUtils;

/**
 *
 *  #(c) IFlytek cform <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: 表单管理类
 *
 *  修改历史:<br/>
 *
 */
public class YYFormUtil {
	public static String url="/private/formyy/topreview?defid=@defid&mainid=@mainid";
	public static String TABLE_PRE_NAME = CommonStaticUtil.TABLE_NAME_SUB;
	public static String yytableid="";//主键的值
	public static Map<String,String> getOptionsMap(String options,String sp1,String sp2){
		Map<String,String> map = new HashMap<String, String>();
		if(EmptyUtils.isNotEmpty(options)){
			String[] ss=options.split(sp1);
			for (String str : ss) {
				String[] strs=str.split(sp2);
				if(strs.length==2){
					map.put(strs[1], strs[0]);
				}else{
					map.put(str, str);
				}
			}
		}
		return map;
	}
	public static Map<String,Object> getParams(HttpServletRequest req){
		Map<String, Object> params = new HashMap<String, Object>();
		Enumeration paramNames = req.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if(paramName.equals("status")){//状态保留字段
				params.put(paramName, req.getParameter(paramName));
				continue;
			}
			String[] paramValues = req.getParameterValues(paramName);
			if (paramValues.length >= 1) {
				String paramValue="";
				for (int i = 0; i < paramValues.length; i++) {
					paramValue=paramValue+paramValues[i]+"☆";
				}
				paramValue=paramValue.substring(0,paramValue.length()-1);
				params.put(paramName, paramValue);
			}
			
		}
		return params;
	}

	public static int containsBy(String str){
		int i=0;
		char[] cc=str.toCharArray();
		for (char c : cc) {
			if("☆".equals(c+"")){
				i++;
			}
		}
		return i;
	}
		/**
	 * 功能描述:保存文件到附件表
	 *
	 * @author (☆笑死宝宝了☆)  2016年3月3日 下午2:55:35
	 *
	 */
	public static void saveFile( ObjectCtl daoCtl,final Dao dao,final String fieldname,final Map<String,String> map,final String field_qz,final String tablename,final String tablekey){
		try {
			Trans.exec(new Atom() {
				public void run() {
					String name=field_qz+fieldname;
					String[] names=StringUtil.null2String(map.get(name+"_filename")).split(";");
					String[] paths=StringUtil.null2String(map.get(name+"_filepath")).split(";");
					String[] sizes=StringUtil.null2String(map.get(name+"_filesize")).split(";");
					String[] ids=StringUtil.null2String(map.get(name+"_fileid")).split(";");
					String curtime=DateUtil.getCurDateTime();
					String NDelid="";//保留的附件记录，除此之外均删除
					//附件
					for(int i=0;i<paths.length;i++){
						File_info att = new File_info();
						if(EmptyUtils.isNotEmpty(paths[i])&&EmptyUtils.isNotEmpty(names[i])){
							if(EmptyUtils.isEmpty(ids[i])||" ".equals(ids[i])){
								att.setFilename(names[i]);
								att.setFilepath(paths[i]);
								att.setTablekey(tablekey);
								att.setTablename(tablename);
								att.setCreate_time(curtime);
								att.setFilesize(sizes[i]);
								att.setFieldname(fieldname);
								dao.insert(att);
								NDelid+="'"+att.getId()+"',";
							}else if(!" ".equals(ids[i])){
								NDelid+="'"+ids[i]+"',";
							}
						}
					}
					if(NDelid.length()>1){
						NDelid=NDelid.endsWith(",")?NDelid.substring(0, NDelid.length()-1):NDelid;
						Sql delsql=Sqls.create("delete from file_info where id not in ("+NDelid+") and fieldname=@fieldname and tablekey=@tablekey and tablename=@tablename");
						delsql.params().set("tablename", tablename);
						delsql.params().set("tablekey", tablekey);
						delsql.params().set("fieldname", fieldname);
						System.out.println(delsql.toString());
						dao.execute(delsql);
					}
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
		/**
		 * 功能描述:给标签设置 value值
		 *
		 * @author (☆笑死宝宝了☆)  2016年2月26日 下午5:24:34
		 *
		 */
		public static void setTagValue(ObjectCtl daoCtl,Dao dao,Element el,String value,Form_field field,String tablename,String mainid){
			String clas=el.attr("clas");
			value=StringUtil.null2String(value);
			String nodename=el.nodeName();
			if (("select").equals(nodename)) {
				if (field.getControltype()==15) {//是附件
					List<File_info> fileList=daoCtl.list(dao,File_info.class, Sqls.create("select * from file_info where fieldname='"+field.getFieldname()+"' and tablekey='"+mainid+"' and tablename='"+tablename+"' order by create_time"));
					if (fileList.size()>0) {
						for (File_info file : fileList) {
							String varItem = file.getId()+"|"+file.getFilepath()+"|"+file.getFilesize();
							el.append("<option value='"+varItem+"' selected>"+file.getFilename()+"</option>");
						}
					}else{
						el.attr("value","");
					}
				}else{
					el.attr("selectedvalue", value);//在前台使用js做选中，在preview.js初始中运行
				}
			}else if("input".equals(nodename)){
				String type=el.attr("type").toUpperCase();
				String setvalue=el.attr("value");
				if ("CHECKBOX".equals(type)) {
					if (setvalue.equals(value)) {
						el.attr("checked", "checked");
					}
				}if ("RADIO".equals(type)) {
					if (setvalue.equals(value)) {
						el.attr("checked", "checked");
					}
				}else{
					if(field.getControltype()==7 || field.getControltype()==8){//信用主体
						if (EmptyUtils.isNotEmpty(value)) {
							String[] valueArry=value.split(",");
							String showValue="";
							for (int i = 0; i < valueArry.length; i++) {
								String xyzt=StringUtil.null2String(comUtil.xyztMap.get(valueArry[i]));
								xyzt=xyzt.substring(0, xyzt.indexOf("☆")<0?0:xyzt.indexOf("☆"));
								showValue=showValue+xyzt+",";

							}
							el.attr("clas","xyzt");
							el.attr("showxyzt",showValue.substring(0,showValue.length()-1));
						}
						el.attr("value",value);
					}else if(field.getControltype()==19){//用户所在机构
						if (EmptyUtils.isNotEmpty(value)) {
							Elements el2=el.siblingElements();
							el2.attr("value", comUtil.unitMap.get(value));
						}
						el.attr("value",value);
					}else{
						el.attr("value",value);
					}

					if (clas.equals("radio")) {
						String radiotext=field.getOptions();
						if (EmptyUtils.isNotEmpty(radiotext)) {
							String[] radioArry=radiotext.split("#newline#");
							for (int i = 0; i < radioArry.length; i++) {
								String text=radioArry[i];
								String[] textArry=text.split("/");
								if(value.equals(textArry[1])){
									value=textArry[0];
								}
							}
						}
						el.attr("selectedtext", value);//在前台使用js做选中，在preview.js初始中运行
					}

				}

			}else if("textarea".equals(nodename)){
				el.append(value);
			}
		}
		/**
	 * 功能描述:给标签设置 value值，浏览页面使用
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月26日 下午5:24:34
	 *
	 */
		public static void setValue( ObjectCtl daoCtl,Dao dao,Element el,String value,Form_field field,String tablename,String mainid){
		value=StringUtil.null2String(value);
		Element pel=el.parent();
		String pclass=pel.attr("class");
		if("hidedomain".equals(pclass)){//隐藏域
			value="";
		}else if("textfield".equals(pclass)||"textarea".equals(pclass)||"datepicker".equals(pclass)){//单行文本框||多行文本||日期
			//value=value;不做处理
		}else if("checkbox".equals(pclass)||"radioinput".equals(pclass)){//多选框||单选框
			String options=field.getOptions();
			if (EmptyUtils.isNotEmpty(options)) {
				Map<String,String> optionsMap=YYFormUtil.getOptionsMap(options, "#newline#", "/");
				value=optionsMap.get(value);
			}
		}else if("selectinput".equals(pclass)){//下拉框
			String loadselect=field.getLoadselect();
			String options=field.getOptions();
			if(EmptyUtils.isNotEmpty(loadselect)){
				String sql="select code,name from Cs_value where typeid = '"+loadselect+"'";
				Map<String,String> loadselectMap=daoCtl.getHTable(dao, Sqls.create(sql));
				sql="select code from Cs_value where value='"+value+"' and typeid = '"+loadselect+"'";
				String code=daoCtl.getStrRowValue(dao, Sqls.create(sql));
				int lg=code.length()/4;
				value="";
				for (int j = 1; j <= lg; j++) {
					value+=loadselectMap.get(code.substring(0,j*4))+"/";
				}
				int lg2=value.length()>0?value.length()-1:0;
				value=value.substring(0,lg2);
			}else{
				Map<String,String> optionsMap=YYFormUtil.getOptionsMap(options, "#newline#", "/");
				value=optionsMap.get(value);
			}
		}else if("userselector".equals(pclass)){//信用主体
			if (EmptyUtils.isNotEmpty(value)) {
				String[] valueArry=value.split(",");
				String showValue="";
				for (int i = 0; i < valueArry.length; i++) {
					String xyzt=StringUtil.null2String(comUtil.xyztMap.get(valueArry[i]));
					showValue=showValue+xyzt.split("☆")[0]+",";
					
				}
				value=showValue.substring(0,showValue.length()-1);
			}
		}else if("attachement".equals(pclass)){//附件
			List<File_info> fileList=daoCtl.list(dao,File_info.class, Sqls.create("select * from file_info where fieldname='"+field.getFieldname()+"' and tablekey='"+mainid+"' and tablename='"+tablename+"' order by create_time"));
			value="";
			for (File_info file : fileList) {
				value+="<a href=\""+Globals.APP_BASE_NAME+file.getFilepath()+"\" target='_blank' >"+file.getFilename()+"</a><br/>";
			}
		}else if("userunit".equals(pclass)){//用户所在机构
			if (EmptyUtils.isNotEmpty(value)) {
				value=comUtil.unitMap.get(value);
			}
		}
		if("span".equals(el.parent().nodeName())){
			el.parent().after(StringUtil.null2String(value));
			el.parent().remove();
			return;
		}
	}




	/**
	 * 功能描述:公用  替换页面html标签方法
	 *
	 * @author (☆笑死宝宝了☆)  2016年2月29日 上午9:44:24
	 *
	 * @param table  表实体
	 * @param doc   jsoup 
	 * @param sd    表 数据 
	 * @param fieldList  表字段 
	 * @param type  类型 view查看页面使用，需删除标签
	 * @return
	 */
	public static String getHtml(ObjectCtl daoCtl,Dao dao,Form_table table,Document doc,List<Map> sd,List<Form_field> fieldList,String filed_qz,String primarykey,String type,String ywid){
		String tablename=CommonStaticUtil.TABLE_NAME_SUB+table.getTablekey();
		if (table.getIsmain()==1) {//主表
			for (Map hashtable : sd) {
				yytableid=StringUtil.null2String(hashtable.get(primarykey));
				for (Form_field field : fieldList) {
					//当主表的status为0时，说明该条数据为草稿
					String status=StringUtil.null2String(hashtable.get("status"));
					if("0".equals(status)){
						doc.select("[id=forupdate]").remove();
					}
					doc.select("[id=fkjl]").remove();//主表不添加付款记录
					Elements els=null;
					if("makeup".equals(type)){//填报，foradd中的标签值直接显示
						els= doc.select("div#foradd [name="+field.getFieldname()+"]");
					}else{
						els= doc.select("[name="+field.getFieldname()+"]");
					}
					if (EmptyUtils.isEmpty(els)) {
						doc.body().append("<input type=\"hidden\" name=\""+field.getFieldname()+"\" value=\""+hashtable.get(field.getFieldname())+"\" />");
					}else{
						for (Element el : els) {
							String value=String.valueOf(hashtable.get(field.getFieldname()));
							if("view".equals(type)){
								setValue(daoCtl,dao,el,value,field,tablename,yytableid);
							}else{
								setTagValue(daoCtl,dao,el,value,field,tablename,yytableid);
							}
						}
					}
				}
			}
			/*=====主表解析结束=====*/
		}else{//子表
			//获取子表的数据格式
			Element subdiv=doc.select("[tablekey="+table.getTablekey()+"]").first();
			Element subtr =subdiv.getElementById("tr_"+table.getTablekey());
			if (EmptyUtils.isNotEmpty(subtr)) {//子表行级显示
				doc.getElementById("tr_"+table.getTablekey()).remove();
				//去除子表行级显示的 多选 checkbox 的disabled的属性
				Elements ckels= subtr.select("[name=ckb_"+table.getTablekey()+"]");
				for (Element ck :ckels) {
					ck.removeAttr("disabled");
				}
			}else{
				subtr =subdiv.getElementById("table_"+table.getTablekey());
				doc.getElementById("table_"+table.getTablekey()).remove();
			}
			/*====获取子表的数据格式end====*/
			if (sd.size()==0) {
				Element el= doc.getElementById("table_"+table.getTablekey());
				if (EmptyUtils.isNotEmpty(el)) {
					doc.getElementById("table_"+table.getTablekey()).child(0).append(subtr.toString());
				}else{
					doc.select("[tablekey="+table.getTablekey()+"]").first().child(0).append(subtr.toString());
				}
				/*=====子表无数据end=====*/
			}else{
				//表数据条数循环
				for (Map hashtable : sd) {
					yytableid=StringUtil.null2String(hashtable.get(primarykey));
					Element subtrtmp =new Element(subtr.tag(), "",subtr.attributes());
					subtrtmp.html(subtr.outerHtml());
					/*添加序号
					Elements ckels= subtrtmp.select("[name=ckb_"+table.getTablekey()+"]");
					for (Element ck :ckels) {
						ck.parent().html("eee");
						ck.removeAttr("disabled");
					}*/
					for (Form_field field : fieldList) {
						Elements els= subtrtmp.select("[name="+filed_qz+field.getFieldname()+"]");
						if (EmptyUtils.isEmpty(els) && field.getIsprimary()==1) {
							subtrtmp.append("<input type=\"hidden\" name=\""+filed_qz+field.getFieldname()+"\" value=\""+hashtable.get(field.getFieldname())+"\" />");
						}
						for (Element el : els) {
							String value=String.valueOf(hashtable.get(field.getFieldname()));
							if("view".equals(type)){
								setValue(daoCtl,dao,el,value,field,tablename,yytableid);
							}else{
								setTagValue(daoCtl,dao,el,value,field,tablename,yytableid);
							}
						}
					}
					//插入子表数据
					Element el = doc.getElementById("table_" +table.getTablekey());
					if(yytableid.equals(ywid)){
						subtrtmp.attr("style","background-color:red");
					}
					if (EmptyUtils.isNotEmpty(el)) {
						doc.getElementById("table_"+table.getTablekey()).child(0).append(subtrtmp.toString());
					}else{
						doc.select("[tablekey="+table.getTablekey() + "]").first().child(0).append(subtrtmp.toString());
					}
					subtrtmp.attr("style","");
				}
				/*=====子表多条数据end=====*/
			}
			/*=====子表解析end=====*/
		}
		

		return doc.body().outerHtml().replaceAll("<body>", "").replaceAll("</body>", "");

	}
}