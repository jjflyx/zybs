package com.hits.modules.com;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.util.EmptyUtils;

import net.sf.json.JSONObject;

/**
 * 
 *  #(c) IFlytek gtxt <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: 公用数据类
 * 
 *  <br/>创建说明: 2016年3月30日 下午8:27:01 L H T  创建文件<br/>
 * 
 *  修改历史:<br/>
 *
 */
@IocBean
@At("/private/common")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class CommonAction extends BaseAction{
	
	@Inject
	protected Dao dao;
	
	/**
	 * 功能描述:查询合同信息
	 *
	 * @author L H T  2016年3月30日 下午8:27:54
	 * 
	 * @param req
	 * @param xy_type 信用类型
	 * @param xyzt_id 信用主体id
	 */
	@At
	@Ok("->:/private/com/htlist.html")
	public void findHt(HttpServletRequest req,@Param("xy_type") String xy_type,@Param("xyzt_id") String xyzt_id){
		try {
			req.setAttribute("xy_type", xy_type);
			req.setAttribute("xyzt_id", xyzt_id);
			
			//合同浏览id
			String htbm = comUtil.htTableMap.get(xy_type);
			if (EmptyUtils.isNotEmpty(htbm)) {
				Sql sql=Sqls.create("select formdefid from form_table where tablekey=@tablekey and form_type=1");
				sql.params().set("tablekey", htbm.replace("l_", ""));
				String defid=daoCtl.getStrRowValue(dao, sql);
				req.setAttribute("defid", defid);
			}
			
			Sql ztsql=Sqls.create("select id,name from xyzt_info ");
			Map<String,String> ztMap=daoCtl.getHTable(dao, ztsql);
			Gson gson=new Gson();
			req.setAttribute("ztMap", gson.toJson(ztMap));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 功能描述:显示合同列表
	 *
	 * @author L H T  2016年3月30日 下午8:29:11
	 * 
	 * @param xyzt_id 信用主体
	 * @param xy_type 信用类型
	 * @return
	 */
	@At
	@Ok("raw")
	public JSONObject htlist(@Param("xyzt_id") String xyzt_id,@Param("xy_type") String xy_type,@Param("htmc") String htmc,@Param("page") int curPage, @Param("rows") int pageSize){
		try {
			System.out.println("信用主体id："+xyzt_id+"     信用主体类型："+xy_type);
			String htbm = comUtil.htTableMap.get(xy_type);
			if (EmptyUtils.isNotEmpty(htbm)) {
				Sql sql = Sqls.create("select * from $httablename where xyzt =@xyzt $whrersql");
				sql.vars().set("httablename", htbm);
				sql.params().set("xyzt", xyzt_id);
				
				String wheresql="";
				if (EmptyUtils.isNotEmpty(htmc)) {
					wheresql +=" and htmc like '%"+htmc+"%' ";
				}
				wheresql +=" order by add_time";
				sql.vars().set("whrersql", wheresql);
				return daoCtl.listPageJsonSql(dao, sql, curPage, pageSize);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
