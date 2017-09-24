package com.hits.modules.cjzdx;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.modules.bean.Breach_info;
import com.hits.modules.com.YWCL;
import com.hits.modules.com.comUtil;
import com.hits.modules.gtxt.bean.Xyzt_info;
import com.hits.modules.sxcj.bean.Discredit_info;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.EmptyUtils;
import com.hits.util.PageObj;
import com.hits.util.StringUtil;

import net.sf.json.JSONObject;

/**
 * @author hzg
 * @time 2016-03-09 10:38:44
 * 
 */
@IocBean
@At("/private/cjzdx")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class cjzdxAction extends BaseAction{
	@Inject
	protected Dao dao;
	
	//惩戒中对象列表
	@At("/tolist")
	@Ok("->:/private/cjzdx/cjzdxlist.html")
	public void user(HttpServletRequest req,HttpSession session) {
		Sql sql = Sqls.create("select value,name from cs_value where typeid = 00010008");
		List<Map> cstype = daoCtl.list(dao, sql);
		Hashtable<String, String> csTable = daoCtl.getHTable(dao, sql);
		req.setAttribute("xyztMap", comUtil.xyztlxMap);
		req.setAttribute("ztTable", JSONObject.fromObject(comUtil.xyztlx));
		req.setAttribute("srcMap", cstype);
		req.setAttribute("csTable", JSONObject.fromObject(csTable));
		req.setAttribute("xytype", comUtil.sxxwtypeList);
		req.setAttribute("csvalueList", comUtil.sxxwtypeList);
		req.setAttribute("sxxwtypeMap", JSONObject.fromObject(comUtil.sxxwtypeMap));
	}
	
	//惩戒中对象列表页查询
	@At
	@Ok("raw")
	public String cjzdxList(HttpServletRequest req,HttpSession session,@Param("xyzt_type") String xyzt_type,
			@Param("src") String src,@Param("xyzt_name") String xyzt_name,@Param("xy_type") String xy_type,@Param("page") int curPage, @Param("rows") int pageSize) {
		Sql sql = null;
		String sqlstr = "select a.id as sxcjid,a.xy_type,a.start_date,a.end_date,a.src,b.name,b.type as xyzt_type,"+
		"c.sxxw from discredit_info a,xyzt_info b,breach_info c where a.xyzt_id = b.id and a.sxxw_id = c.id and a.type ='0'";
		//信用主体类型
		if(EmptyUtils.isNotEmpty(xyzt_type)){
			sqlstr += " and b.type = '"+xyzt_type+"'";
		}
		//信用类型
		if(EmptyUtils.isNotEmpty(xy_type)){
			sqlstr += " and a.xy_type like '"+xy_type+"%'";
		}
		//产生方式
		if(EmptyUtils.isNotEmpty(src)){
			sqlstr += " and a.src = '"+StringUtil.StringToInt(src)+"'";
		}
		//信用主体名称
		if(EmptyUtils.isNotEmpty(xyzt_name)){
			sqlstr += " and b.name like '%"+xyzt_name+"%'";
		}
		sqlstr += " order by a.id desc";
		System.out.println(sqlstr);
		sql = Sqls.create(sqlstr);
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map<String,String>> list = (List<Map<String, String>>) qr.getList();
		for(int i=0;i<list.size();i++){
			String start_date = list.get(i).get("start_date");
			String end_date = list.get(i).get("end_date");
			if(end_date==null||"".equals(end_date)){
				list.get(i).put("startDend", start_date+" 起 ");
			}else{
				list.get(i).put("startDend", start_date+" 至 "+(null == end_date?"":end_date));
			}
			list.get(i).put("xyzt_type", list.get(i).get("xyzt_type").toString().trim());
		}
		return PageObj.getPageJSON(qr);
	}
	
	//惩戒中对象浏览操作
	@At
	@Ok("->:/private/cjzdx/cjzdxDetail.html")
	public void showDetail(@Param("sxcjid") String sxcjid,HttpServletRequest req){
		int id = StringUtil.StringToInt(sxcjid);
		//失信惩戒信息
		Discredit_info sxcj = daoCtl.detailById(dao, Discredit_info.class, id);
		//信用主体信息
		Xyzt_info xyzt = daoCtl.detailById(dao, Xyzt_info.class, StringUtil.StringToInt(sxcj.getXyzt_id()));
		xyzt.setType(xyzt.getType().trim());
		//获取合同信息
		if(EmptyUtils.isNotEmpty(sxcj.getContract_id())&&EmptyUtils.isNotEmpty(sxcj.getTablekey())){
			Sql sqlht = Sqls.create("select htbh,htmc from "+sxcj.getTablekey()+" where htid = '"+sxcj.getContract_id()+"' ");
			if("00030002".equals(sxcj.getXy_type())||"l_tkqsyf".equals(sxcj.getTablekey())){//采矿权或探矿权使用费
				String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(sxcj.getTablekey())));
				String xkzhSql="select xkzh from "+sxcj.getTablekey()+" where "+tablePK+" = '"+sxcj.getContract_id()+"' ";
				sqlht=Sqls.create(xkzhSql);
			}else if("l_tkht".equals(sxcj.getTablekey())){//探矿权合同
				String tablePK=daoCtl.getStrRowValue(dao, Sqls.create(YWCL.getPKSql(sxcj.getTablekey())));
				String xkzhSql="select kcxkz from "+sxcj.getTablekey()+" where "+tablePK+" = '"+sxcj.getContract_id()+"' ";
				sqlht=Sqls.create(xkzhSql);
			} 
			req.setAttribute("glxm", daoCtl.getStrRowValue(dao, sqlht));
			String xylx = comUtil.sxxwtypeMap.get(sxcj.getXy_type());
			req.setAttribute("xylx", xylx);
		}
		if(sxcj.getXy_type().equals("00070001")){//农村土地整治
			String year=sxcj.getDiscipline_date().substring(0,4);
			String sqlstr="select count(1) from discredit_info where discipline_date like '"+year+"%' and type not in (2,3) and xyzt_id='"+sxcj.getXyzt_id()+"' and sxqx_id='"+sxcj.getSxqx_id()+"'";
			req.setAttribute("syfss", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr)));
			if(EmptyUtils.isNotEmpty(sxcj.getContract_id())){
				sqlstr="select count(1) from discredit_info where discipline_date like '"+year+"%' and contract_id='"+StringUtil.null2String(sxcj.getContract_id())+"' and type not in (2,3) and xyzt_id='"+sxcj.getXyzt_id()+"' and sxqx_id='"+sxcj.getSxqx_id()+"'";
				req.setAttribute("txmfss", daoCtl.getIntRowValue(dao, Sqls.create(sqlstr)));
			}
			req.setAttribute("year", year);
		}
		//失信行为
		Sql sqlxw = Sqls.create("select sxxw from breach_info where id = '"+sxcj.getSxxw_id()+"'");
		String sxxw = daoCtl.getStrRowValue(dao, sqlxw);
		//失信情形
		Breach_info binfo = daoCtl.detailBySql(dao,Breach_info.class, Sqls.create("select * from breach_info where id ='"+sxcj.getSxqx_id()+"'"));
		req.setAttribute("sxxw", sxxw);
		req.setAttribute("binfo", binfo);
		req.setAttribute("xyzt", xyzt);
		req.setAttribute("sxcj", sxcj);
		req.setAttribute("ztMap", comUtil.xyztlx);
		//惩戒状态
		Sql sql = Sqls.create("SELECT value,name FROM CS_VALUE  WHERE typeid='00010007' AND length(code)=4 ORDER BY location ASC ");
		req.setAttribute("cjztTable", daoCtl.getHTable(dao, sql));
	}
		
}
