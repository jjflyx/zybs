package com.hits.modules.gtxt;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.modules.bean.Breach_info;
import com.hits.modules.com.comUtil;
import com.hits.modules.gtxt.bean.Warnexception;
import com.hits.modules.gtxt.bean.Xyzt_info;
import com.hits.modules.sxcj.bean.Discredit_info;
import com.hits.util.DateUtil;
import com.hits.util.EmptyUtils;
import com.hits.util.StringUtil;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by numbgui on 2016/3/7.
 */

@IocBean
@At("/public/info")
@Filters({ @By(type = GlobalsFilter.class)})
public class xyzt_infoPublicAction extends BaseAction {
    @Inject
    protected Dao dao;


    @At("/index")
    @Ok("->:/public/gtxy/xyztIndex.html")
    @Fail("->:/public/search.html")
    public void xyztInfo(@Param("id")Integer id,HttpServletRequest req) {
        Xyzt_info xyzt_info = daoCtl.detailById(dao,Xyzt_info.class,id);
        List<Discredit_info> discreditInfoList = discreditInfoList = daoCtl.list(dao, Discredit_info.class, Cnd.where("xyzt_id","=",id).and("type", "=", 0));
        Hashtable<String, String> cjtype = daoCtl.getHTable(dao,  Sqls.create("select value,name from cs_value where typeid = 00010011"));
        List<Breach_info> breach_infos = daoCtl.list(dao, Breach_info.class, Cnd.orderBy().asc("id"));
        Map<String,Breach_info> breach_infoMap = new HashMap<String,Breach_info>();
        for(Breach_info breach_info : breach_infos ){
            breach_infoMap.put(breach_info.getId(),breach_info);
        }
        Map<String,String> contractNameMap = new HashMap<String,String>();
        for(Discredit_info discredit_info : discreditInfoList){
            //判断是否涉及合同
            if(StringUtil.isNull(discredit_info.getContract_id(),"").length()!=0){
                discredit_info.setContract_id(daoCtl.getStrRowValue(dao,Sqls.create(" select htbh from " +
                        comUtil.htTableMap.get(breach_infoMap.get(discredit_info.getSxxw_id()).getXylx()) + " where htid = '" + discredit_info.getContract_id() + "' ")));
                contractNameMap.put(discredit_info.getContract_id(),daoCtl.getStrRowValue(dao, Sqls.create(" select htmc from " +
                        comUtil.htTableMap.get(breach_infoMap.get(discredit_info.getSxxw_id()).getXylx()) + " where htbh = '" + discredit_info.getContract_id() + "' ")));
            }
            discredit_info.setSxxw_id(breach_infoMap.get(discredit_info.getSxxw_id()).getSxxw());
            if(discredit_info.getSxqx_id().indexOf(",") > 0){

            }else{
                discredit_info.setSxqx_id(breach_infoMap.get(discredit_info.getSxqx_id()).getSxqx());
            }
            if(discredit_info.getCjcs_id().indexOf(",") > 0){
                String[] str = discredit_info.getCjcs_id().split(",");
                String cjcs = "";
                for(String str_id : str){
                    cjcs += breach_infoMap.get(str_id).getCjcs()+"\n";
                }
                discredit_info.setCjcs_id(cjcs);
            }else{
                discredit_info.setCjcs_id(breach_infoMap.get(discredit_info.getCjcs_id()).getCjcs());
            }
            //惩戒机构替换为中文
            discredit_info.setUnit(cjtype.get(discredit_info.getUnit()));
        }
        req.setAttribute("info",xyzt_info);
        req.setAttribute("size",discreditInfoList.size());
        req.setAttribute("discreditInfoList",discreditInfoList);
        req.setAttribute("contractNameMap",contractNameMap);
    }

    @At
    @Ok("->:/public/gtxy/warnExceptionPage.html")
    public void warnExceptionPage(@Param("id")String id,@Param("sxxw")String sxxw,@Param("sxqx")String sxqx,@Param("cjcs")String cjcs,
                                  @Param("xylx")String xy_type,@Param("start")String start,@Param("end_date")String end_date,HttpServletRequest req){
        req.setAttribute("id",id);
        req.setAttribute("sxxw",sxxw);
        req.setAttribute("sxqx",sxqx);
        req.setAttribute("cjcs",cjcs);
        req.setAttribute("start",start);
        req.setAttribute("end_date",end_date);
        req.setAttribute("xy_type",xy_type);
        req.setAttribute("xyzt_id",daoCtl.getStrRowValue(dao,Sqls.create("select xyzt_id from DISCREDIT_INFO where type = 0 and id = '"+id+"' ")));
    }

    @At
    @Ok("raw")
    public Boolean addWarnException(@Param("id") Integer id,@Param("applicant") String applicant,@Param("tel") String tel,@Param("note") String note,
                                   @Param("file_html") String file_html,@Param("code")String code,@Param("contract_id")String contract_id,@Param("xy_type")String xy_type){
        Discredit_info discredit_info = daoCtl.detailByCnd(dao,Discredit_info.class,Cnd.where("id","=",id));
        Warnexception warnexceptionInfo = new Warnexception();
        warnexceptionInfo.setApplicant(applicant);
        warnexceptionInfo.setNote(note);
        warnexceptionInfo.setTel(tel);
        warnexceptionInfo.setFile_html(file_html);
        warnexceptionInfo.setSxcj_id(id);
        warnexceptionInfo.setCode(code);
        warnexceptionInfo.setUnit(discredit_info.getUnitid());
        warnexceptionInfo.setXzqh(daoCtl.getStrRowValue(dao,Sqls.create(" select xzqh from sys_unit where id = '"+discredit_info.getUnitid()+"' ")));
        warnexceptionInfo.setXyzt_id(discredit_info.getXyzt_id());
        warnexceptionInfo.setAdd_time(DateUtil.getCurDateTime());
        warnexceptionInfo.setXy_type(xy_type);
        warnexceptionInfo.setUsername(discredit_info.getActor());
        // source == 0 表示为线上申请
        warnexceptionInfo.setSource(0);
        if(EmptyUtils.isNotEmpty(contract_id)){
            warnexceptionInfo.setContract_id(contract_id);
            warnexceptionInfo.setContract_name(daoCtl.getStrRowValue(dao,Sqls.create(" select htmc from " +
                    comUtil.htTableMap.get(xy_type) + " where htid = '" + discredit_info.getContract_id() + "' ")));
        }
        warnexceptionInfo.setXyzt_name(daoCtl.getStrRowValue(dao,Sqls.create(" select name from XYZT_INFO where id = '"+warnexceptionInfo.getXyzt_id()+"' ")));
        return daoCtl.add(dao,warnexceptionInfo);
    }
}
