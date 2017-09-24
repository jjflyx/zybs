package com.hits.modules.ratifyFollow;

import com.hits.common.action.BaseAction;
import com.hits.common.config.Globals;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.StringUtil;
import com.hits.modules.ratifyFollow.bean.Dir_collect;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by numbgui on 16-1-8.
 */

@IocBean
@At("/private/ratifyFollow")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class contractManageAction extends BaseAction {
    @Inject
    protected Dao dao;

    @At("/contract/fulfil")
    @Ok("->:/private/ratifyFollow/contractMange.html")
    public void contractManage(HttpServletRequest req,HttpSession session) {
        System.out.println("In contractManage()...");
    }

    @At("/contract/warning")
    @Ok("->:/private/ratifyFollow/earlyWarning.html")
    public void earlyWarning(@Param("type")String type,HttpServletRequest req){
        req.setAttribute("type",type);
    }

    @At
    @Ok("raw")
    public String warnList(){
        System.out.println("In warnList()...");
        JSONObject jsonobj = new JSONObject();
        JSONArray array = new JSONArray();
        jsonobj.put("a", "科大讯飞");
        jsonobj.put("d", "肥东县土地整改合同");
        jsonobj.put("e", "超过合同约定期限未支付出让金。");
        jsonobj.put("f", "2018-01-01");
        jsonobj.put("g", "2016-01-01");
        jsonobj.put("ee","人工录入");
        for(int i=0; i< 3 ;i++){
            jsonobj.put("d", "肥东县土地整改合同-"+i);
            jsonobj.put("id", i+"");
            jsonobj.put("name","王"+i+"麻子");
            if(i==0){
                jsonobj.put("img","red");
                jsonobj.put("a", "合肥地质矿业有限公司");
            }else if(i==1){
                jsonobj.put("a", "芜湖地质矿业有限公司");
                jsonobj.put("img","yellow");
                jsonobj.put("ee","系统生成");
            }else{
                jsonobj.put("a", "巢湖地质矿业有限公司");
                jsonobj.put("img","green");
            }
            array.add(jsonobj);
        }
        return array.toString();
    }

    @At
    @Ok("raw")
    public String frList(){
        System.out.println("In frList()...");
        JSONObject jsonobj = new JSONObject();
        JSONArray array = new JSONArray();
        jsonobj.put("b", "肥东县土地整改合同");
        jsonobj.put("c", "科大讯飞");
        jsonobj.put("e", "10001000");
        jsonobj.put("f", "安徽省合肥市");
        jsonobj.put("g", "2016-01-08 12:43:12");
        int total = 0;
        for(int i=0; i< 1 ;i++) {
            jsonobj.put("d", "王"+i+"麻");
            jsonobj.put("a", "10002016010"+i);
            jsonobj.put("id", i+"");
            array.add(jsonobj);
            total = i+1;
        }
        return "{\"total\":"+total+",\"rows\":"+array.toString()+"}";
    }

    @At
    @Ok("->:/private/ratifyFollow/earlyWarning_Add.html")
    public void warnAdd(){
        System.out.println("In warnAdd()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/earlyWarning_Detail.html")
    public void warnDetail(){
        System.out.println("In warnDetail()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/earlyWarning_Update.html")
    public void warnUpdate(){
        System.out.println("In warnUpdate()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/contract_Add.html")
    public void contractAdd(){
        System.out.println("In contractAdd()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/contract_Update.html")
    public void contractUpdate(){
        System.out.println("In contractUpdate()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/contract_Detail.html")
    public void contractDetail(){
        System.out.println("In contractDetail()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/BlackList.html")
    public void blackListPage(){
        System.out.println("In blackPage()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/blackAdd.html")
    public void blackAdd(){
        System.out.println("In blackAdd()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/blackUpdate.html")
    public void blackUpdate(){
        System.out.println("In blackUpdate()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/blackDetail.html")
    public void blackDetail(){
        System.out.println("In blackDetail()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/warnRuleList.html")
    public void warnRulePage(){
        System.out.println("In warnRulePage()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/warnRule_Add.html")
    public void warnRuleAdd(){
        System.out.println("In warnRuleAdd()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/warnRule_Detail.html")
    public void warnRuleDetail(){
        System.out.println("In warnRuleDetail()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/warnRule_Update.html")
    public void warnRuleUpdate(){
        System.out.println("In warnRuleUpdate()...");
    }

    @At
    @Ok("raw")
    public String warnRuleList(){
        System.out.println("In warnRuleList()...");
        JSONObject jsonobj = new JSONObject();
        JSONArray array = new JSONArray();
        jsonobj.put("a", "土地市场信用");
        jsonobj.put("b", "未按照建设用地使用权出让合同约定支付建设用地出让金");
        jsonobj.put("e", "超过合同约定期限未支付出让金");
        jsonobj.put("f", "2016-01-08 12:43:12");
        for(int i=0; i< 1 ;i++){
            jsonobj.put("id", i+"");
            jsonobj.put("name","土地市场规则-"+(i+1));
            array.add(jsonobj);
        }
        return array.toString();
    }

    @At("/reality/miningPrice")
    @Ok("->:/private/ratifyFollow/miningPrice_reality.html")
    public void miningPrice_rel(@Param("type")String type,HttpServletRequest req){
        req.setAttribute("type",type);
        System.out.println("In miningPrice_rel()...");
    }

    @At("/reality/miningPriceUpdate")
    @Ok("->:/private/ratifyFollow/miningPrice_reality_Update.html")
    public void miningPriceUpdate_rel(@Param("type")String type,@Param("yj")String yj,HttpServletRequest req){
        req.setAttribute("type",type);
        req.setAttribute("yj",yj);
        System.out.println("In miningPriceUpdate_rel()...");
    }

    @At("/reality/miningPriceShow")
    @Ok("->:/private/ratifyFollow/miningPrice_reality_Detail.html")
    public void miningPriceShow_rel(@Param("type")String type,HttpServletRequest req){
        req.setAttribute("type",type);
        System.out.println("In miningPriceShow_rel()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/miningPrice.html")
    public void miningPrice(@Param("type")String type,HttpServletRequest req){
        req.setAttribute("type",type);
        System.out.println("In miningPriceAdd()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/miningPrice_Add.html")
    public void miningPriceAdd(@Param("type")String type,HttpServletRequest req){
        req.setAttribute("type",type);
        System.out.println("In miningPriceAdd()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/miningPrice_Detail.html")
    public void miningPriceShow(@Param("type")String type,HttpServletRequest req){
        req.setAttribute("type",type);
        System.out.println("In miningPriceShow()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/miningPrice_Update.html")
    public void miningPriceUpdate(@Param("type")String type,@Param("yj")String yj,HttpServletRequest req){
        req.setAttribute("type",type);
        req.setAttribute("yj",yj);
        System.out.println("In miningPriceUpdate()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/warnExceptionList.html")
    public void warnExceptionPage(){
        System.out.println("In warnRulePage()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/warnExceptionDetail.html")
    public void warnExceptionDetail(@Param("type")String type,HttpServletRequest req){
        System.out.println("In warnExceptionDetail()..."+type);
        req.setAttribute("type",type);
    }


    @At("/collect/dir")
    @Ok("->:/private/ratifyFollow/collectDirectory.html")
    public void collectDirPage(){
        System.out.println("In collectDirPage()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/dateListPage.html")
    public void dataListPage(){
        System.out.println("In dataListPage()...");
    }

    @At
    @Ok("->:/private/ratifyFollow/dateDetail.html")
    public void dataDetail(){
        System.out.println("In dataDetail()...");
    }

    @At
    @Ok("raw")
    public String dataList(){
        System.out.println("In dataList()...");
        JSONObject jsonobj = new JSONObject();
        JSONArray array = new JSONArray();
        jsonobj.put("a", "土地市场信用");
        jsonobj.put("d", "credit_ground_mart");
        jsonobj.put("e", "根据土地市场信用规则，定义出的数据库表单。");
        jsonobj.put("f", "2016-01-08 12:43:12");
        jsonobj.put("g", "2016-01-17 12:43:12");
        jsonobj.put("ee","系统管理员");
        for(int i=0; i< 1 ;i++){
            jsonobj.put("id", i+"");
            array.add(jsonobj);
        }
        return array.toString();
    }


    @At
    @Ok("json")
    public List<Dir_collect> list(@Param("subtype") int subtype) {
        Criteria cri = Cnd.cri();
        cri.where().and("subtype", "=", subtype);
        cri.getOrderBy().asc("location");
        cri.getOrderBy().asc("id");
        return daoCtl.list(dao, Dir_collect.class, cri);
    }

    @At("/collect/listAll")
    @Ok("raw")
    public String listAll(@Param("id") String id, @Param("subtype") int subtype) {
        return getJSON(dao, id, subtype).toString();
    }

    private JSONArray getJSON(Dao dao, String id, int subtype) {
        Criteria cri = Cnd.cri();
        if (null == id || "".equals(id)) {
            cri.where().and("id", "like", "____");
        } else {
            cri.where().and("id", "like", id + "____");
        }
        if (subtype >= 0) {
            cri.where().and("subtype", "=", subtype);
        }
        cri.getOrderBy().asc("location");
        cri.getOrderBy().asc("id");
        List<Dir_collect> list = daoCtl.list(dao, Dir_collect.class, cri);
        JSONArray array = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            Dir_collect res = list.get(i);
            JSONObject jsonobj = new JSONObject();
            String pid = res.getId().substring(0, res.getId().length() - 4);
            if (i == 0 || "".equals(pid))
                pid = "0";
            int num = daoCtl.getRowCount(dao, Dir_collect.class,
                    Cnd.where("id", "like", res.getId() + "____"));
            jsonobj.put("id", res.getId());
            jsonobj.put("name", res.getName());
            jsonobj.put("descript", res.getDescript());
            jsonobj.put("url", res.getState());
            jsonobj.put("state",res.getState()+"");
            jsonobj.put("_parentId", pid);
            String bts = StringUtil.null2String(res.getButton());
            String[] bt;
            String temp = "";
            if (!"".equals(bts)) {
                bt = bts.split(";");
                for (int j = 0; j < bt.length; j++)
                    temp += bt[j].substring(0, bt[j].indexOf("/")) + ";";
            }
            jsonobj.put("bts", temp);
            if (num > 0) {
                jsonobj.put("children", getJSON(dao, res.getId(), subtype));
            }
            array.add(jsonobj);
        }
        return array;
    }

    /***
     * 修改前查找
     * */
    @At
    @Ok("->:/private/ratifyFollow/resourceUpdate.html")
    public void toupdate(@Param("id") String id, HttpServletRequest req) {
        Dir_collect res = daoCtl.detailByName(dao, Dir_collect.class, id);
        req.setAttribute("obj", res);
    }

    /****
     * 修改
     * */
    @At
    @Ok("raw")
    public String updateSave(@Param("..") Dir_collect res,
                             @Param("button2") String button2, HttpServletRequest req) {

        res.setButton(button2);
        return daoCtl.update(dao, res) == true ? res.getId() : "";
    }

    /****
     * 新建菜单，查找单位。
     * */
    @At
    @Ok("->:/private/ratifyFollow/resourceAdd.html")
    public void toAdd() {

    }

    /***
     * 新建资源
     * */
    @At
    @Ok("raw")
    public boolean addSave(@Param("..") Dir_collect res,
                           @Param("button2") String button2) {

        Sql sql = Sqls
                .create("select max(location) from Dir_collect where id like  @id");
        sql.params().set("id", res.getId() + "_%");
        int location = daoCtl.getIntRowValue(dao, sql);
        if (location == 0) {
            sql = Sqls
                    .create("select max(location) from Dir_collect where id =  @id");
            sql.params().set("id", res.getId());
            location = daoCtl.getIntRowValue(dao, sql);
        }
        res.setLocation(location);
        res.setId(daoCtl.getSubMenuId(dao, "Dir_collect", "id", res.getId()));
        res.setButton(button2);
        return daoCtl.add(dao, res);
    }

    /**
     * 删除
     * */
    @At
    @Ok("raw")
    public boolean del(@Param("id") String ids) {
        System.out.println("id"+ids);
        boolean result = false;
        String[] id = StringUtil.null2String(ids).split(",");
        result = daoCtl.deleteByNames(dao, Dir_collect.class, id);
        if (result) {
            for (int i = 0; i < id.length; i++) {
                daoCtl.exeUpdateBySql(dao, Sqls.create("delete from Dir_collect where id like '"
                        + StringUtil.null2String(id[i]) + "%'"));
            }
        }
        return result;
    }

    /**
     * 转到排序页面
     * */
    @At
    @Ok("->:/private/ratifyFollow/resourceSort.html")
    public void toSort(HttpServletRequest req) throws Exception {
        JSONArray array = new JSONArray();
        Criteria cri = Cnd.cri();
        cri.getOrderBy().asc("location");
        cri.getOrderBy().asc("id");
        List<Dir_collect> list = daoCtl.list(dao, Dir_collect.class, cri);
        JSONObject jsonroot = new JSONObject();
        jsonroot.put("id", "");
        jsonroot.put("pId", "0");
        jsonroot.put("name", "目录列表");
        jsonroot.put("open", true);
        jsonroot.put("childOuter", false);
        jsonroot.put("icon", Globals.APP_BASE_NAME
                + "/images/icons/icon042a1.gif");
        array.add(jsonroot);
        for (int i = 0; i < list.size(); i++) {
            JSONObject jsonobj = new JSONObject();
            Dir_collect obj = list.get(i);
            jsonobj.put("id", obj.getId());
            String p = obj.getId().substring(0, obj.getId().length() - 4);
            jsonobj.put("pId", p == "" ? "0" : p);
            String name = obj.getName();
            jsonobj.put("name", name);
            jsonobj.put("childOuter", false);
            if (obj.getId().length() < 12) {
                jsonobj.put("open", true);
            } else {
                jsonobj.put("open", false);
            }
            array.add(jsonobj);
        }
        req.setAttribute("str", array.toString());
    }

    /***
     * 确认排序
     * */
    @At
    @Ok("raw")
    public boolean sort(@Param("checkids") String[] checkids) {
        boolean rs = daoCtl.updateSortRow(dao, "Dir_collect", checkids, "location", 0);
        return rs;
    }
}
