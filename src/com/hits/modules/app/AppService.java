package com.hits.modules.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.aop.Aop;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.modules.bean.AppCommonModel;
import com.hits.modules.com.YWCL;
import com.hits.modules.com.comUtil;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.modules.sys.bean.Sys_userparamconfig;
import com.hits.util.CommonUtil;
import com.hits.util.DateUtil;
import com.hits.util.EmptyUtils;
import com.hits.util.StringUtil;

/**
 * 
 * #(c) IFlytek hnsj_jk <br/>
 * 
 * 版本说明: $id:$ <br/>
 * 
 * 功能说明: 接口业务处理类
 * 
 * <br/>
 * 创建说明: 2016年9月5日 上午9:39:46 L H T 创建文件<br/>
 * 
 * 修改历史:<br/>
 * 
 */
@IocBean
@Filters({ @By(type = GlobalsFilter.class) })
public class AppService extends BaseAction {
	@Inject
	protected Dao dao;

	/**
	 * 功能描述:获取拟办意见
	 * 
	 * @author L H T 2016年7月29日 上午9:39:08
	 * 
	 * @return
	 */
	private List<String> getNibanNote() {

		String zbnote1 = "请按领导批示认真调查处理并答复来电人，并将结果在<span id='showlimitedate'></span>前通过网络反馈";
		String zbnote2 = "请认真调查处理并答复来电人，并将结果在<span id='showlimitedate'></span>前通过网络反馈";
		String zbnote3 = "请认真调查处理，并将结果在<span id='showlimitedate'></span>前通过网络反馈";
		String zbnote4 = "请按市领导批示精神认真查处，并将结果报经分管领导审阅后在<span id='showlimitedate'></span>前通过网络反馈";

		// 转办意见
		List<String> list = new ArrayList<>();
		list.add(zbnote1);
		list.add(zbnote2);
		list.add(zbnote3);
		list.add(zbnote4);
		return list;
	}

	/**
	 * 功能描述:获取系统 名称
	 * 
	 * @author L H T 2016年9月5日 上午10:05:23
	 * 
	 * @return
	 */
	public String getHeadTitle() throws Exception {
		Sql sql = Sqls
				.create("select value from cs_value where name='app_name'");
		String title = daoCtl.getStrRowValue(dao, sql);
		title = EmptyUtils.isNotEmpty(title) ? title : "";
		return title;
	}

	public List<Map> getIndexNotice(int curPage, int pageSize) throws Exception {
		Sql sql = Sqls
				.create("select  a.content_id as id , b.title,b.pub_time audittime from cms_content a left join cms_content_ext b on a.content_id=b.content_id  left join cms_content_txt c  on b.content_id=c.content_id  where    a.channel_id='00010002' and a.site_id='0004' and a.status=3 order by a.sort_time desc");
		QueryResult qr = daoCtl.listPageSql(dao, sql, curPage, pageSize);
		List<Map> qrList = (List<Map>) qr.getList();
		for (Map<String, String> map : qrList) {
			String audittime = StringUtil.null2String(map.get("audittime"));
			if (EmptyUtils.isNotEmpty(audittime)) {
				map.put("audittime", audittime.substring(5, 10));
			}
		}
		return qrList;
	}

	public List<Object> getGongDanCount() throws Exception {
		Sql sql = null;
		Map<String, String> unitMap = comUtil.unitMap;

		List<Object> list = new ArrayList<Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		// 订单未付款
		sql = Sqls.create(" select sum(yfjk) from l_jsgg where isfh='0002'");
		map.put("count", daoCtl.getIntRowValue(dao, sql));
		map.put("name", "订单未付款");
		map.put("icon", "menudcl");
		map.put("index", "0");
		list.add(map);

		// 订单已付款
		sql = Sqls.create("select sum(yfjk) from l_jsgg where isfh='0001'");
		map = new HashMap<String, Object>();
		map.put("count", daoCtl.getIntRowValue(dao, sql));
		map.put("name", "订单已付款");
		map.put("icon", "menuyfk");
		map.put("index", "2");
		list.add(map);

		// 账单未付款
		sql = Sqls.create(" select sum(sfjk) from l_hkzd where isfk='0002'");
		map = new HashMap<String, Object>();
		map.put("count", daoCtl.getIntRowValue(dao, sql));
		map.put("name", "账单未付款");
		map.put("icon", "menuclz");
		map.put("index", "1");
		list.add(map);

		// 已付款
		sql = Sqls.create("select sum(sfjk) from l_hkzd where isfk='0001'");
		map = new HashMap<String, Object>();
		map.put("count", daoCtl.getIntRowValue(dao, sql));
		map.put("name", "账单已付款");
		map.put("icon", "menuhtsq");
		map.put("index", "3");
		list.add(map);
		return list;
	}
}