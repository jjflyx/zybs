package com.hits.modules.com;

import java.util.List;
import java.util.Map;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;

import com.hits.common.dao.ObjectCtl;
import com.hits.common.util.DateUtil;
import com.hits.common.util.FrUtil;
import com.hits.common.util.StringUtil;
import com.hits.modules.gtxt.bean.Xyzt_info;
import com.hits.modules.quartz.bean.Quartz_task;
import com.hits.util.EmptyUtils;

public class ProxyTask {
	public void init(ObjectCtl daoCtl,Dao dao ,Quartz_task task){
		try {
			System.out.println("***************start****************");
			String sqlstr="select * from xyzt_info where modetype=2 /*and (code is null or zzjgdm is null)*/";
			List<Map> xyztList=daoCtl.list(dao, Sqls.create(sqlstr));
			int i=0,j=0;
			for (Map map : xyztList) {
				Map<String, String> fr=FrUtil.getFrByName(StringUtil.null2String(map.get("name")));
				if(EmptyUtils.isNotEmpty(fr)&&EmptyUtils.isNotEmpty(fr.get("FRMC"))){
					//String state=comUtil.qyzt_nv.get(StringUtil.null2String(fr.get("QYZT")));
					map.put("state", StringUtil.null2String(fr.get("QYZT")));
					map.put("hzrq", StringUtil.null2String(fr.get("HZRQ")));
					map.put("create_date", StringUtil.null2String(fr.get("SLRQ")));
					//map.put("company_addr", fr.get("qydz")));
					map.put("djjg", StringUtil.null2String(fr.get("DJJG")));
					map.put("swdjh", StringUtil.null2String(fr.get("SWDJH")));
					map.put("tel", StringUtil.null2String(fr.get("LXDH")));
					map.put("zzjgdm", StringUtil.null2String(fr.get("ZZJGDM")));
					map.put("gszch", StringUtil.null2String(fr.get("GSZCH")));
					map.put("code", StringUtil.null2String(fr.get("TYSHXYDM")));
					map.put("update_date", StringUtil.null2String(fr.get("XYPTMODIFY_DATE")));
					//map.put(".table", "xyzt_info");
					String id=StringUtil.null2String(map.get("id"));
					dao.update("xyzt_info", Chain.from(map), Cnd.where("id","=",id));
					comUtil.xyztMap.put(id,StringUtil.null2String(map.get("name"))+"☆"
					+StringUtil.null2String(map.get("code"))+"☆"+StringUtil.null2String(map.get("zzjgdm")));
					i++;
				}else{
					j++;
				}
			}
			System.out.println("共完成"+i+"条更新，法人名称未匹配"+j+"条。");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void test(ObjectCtl daoCtl,Dao dao ,Quartz_task task){
		try {
			System.out.println("***************start****************");
			System.out.println(task.getParams());
			String[] params=task.getParams().replace("；", ";").split(";");
			String sqlstr="select * from xyzt_info where modetype=2 /*and (code is null or zzjgdm is null)*/";
			List<Map> xyztList=daoCtl.list(dao, Sqls.create(sqlstr));
			int i=0,j=0;
			for (String param : params) {
				Map<String, String> fr=FrUtil.getFrByName(param);
				System.out.println(fr.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
