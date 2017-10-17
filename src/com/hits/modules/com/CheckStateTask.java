package com.hits.modules.com;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

import com.hits.common.dao.ObjectCtl;
import com.hits.common.util.DateUtil;
import com.hits.common.util.StringUtil;
import com.hits.modules.quartz.bean.Quartz_task;
import com.hits.util.EmptyUtils;

public class CheckStateTask{
	
	public void init(ObjectCtl daoCtl,Dao dao ,Quartz_task task){
		try {
			Sql sql = null;
			String sqlstr = "select a.id as sxcjid,a.start_date,a.end_date,a.type,a.issue,a.src,b.name,b.type as xyzt_type,"+
					"c.sxxw,a.perform_date from discredit_info a,xyzt_info b,breach_info c where a.xyzt_id = b.id and a.sxxw_id = c.id "+
					" and (a.sxxw_id in (select id from breach_info)) and a.type=0 ";
			sqlstr += " order by a.id desc";
			System.out.println("sqlstr:"+ sqlstr);
			sql = Sqls.create(sqlstr);
			List<Map> list = daoCtl.list(dao, sql);
			for(int i=0;i<list.size();i++){
				String start_date = StringUtil.null2String(list.get(i).get("start_date"));
				String end_date = StringUtil.null2String(list.get(i).get("end_date"));
				String perform_date = StringUtil.null2String(list.get(i).get("perform_date"));
				//判断是否为已撤销    2表示已撤销
				if(EmptyUtils.isNotEmpty(perform_date)){
					int sxcjid = ((BigDecimal)list.get(i).get("sxcjid")).intValue();
					daoCtl.exeUpdateBySql(dao, Sqls.create("update discredit_info set type = '1' where id = '"+sxcjid+"'"));
				}else if(EmptyUtils.isNotEmpty(end_date)&&DateUtil.getDifferDays(start_date, DateUtil.getToday()) > 0 &&
						DateUtil.getDifferDays(end_date, DateUtil.getToday()) > 0){
					//跟现在时间对比是否惩戒结束
					int sxcjid = ((BigDecimal)list.get(i).get("sxcjid")).intValue();
					daoCtl.exeUpdateBySql(dao, Sqls.create("update discredit_info set type = 1 where id = '"+sxcjid+"'"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}