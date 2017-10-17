package com.hits.modules.sys;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.DecodeUtil;
import com.hits.common.util.StringUtil;
import com.hits.modules.com.comUtil;
import com.hits.modules.sys.bean.Sys_templateconfig;
import com.hits.modules.sys.bean.Sys_user;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author wanfly
 * @time 2014-02-28 14:05:47
 * 
 */
@IocBean
@At("/private/sys/templateconfig")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Sys_templateconfigAction extends BaseAction {
	@Inject
	protected Dao dao;

	@At("")
	@Ok("->:/private/sys/templateconfig.html")
	public void user(HttpSession session,HttpServletRequest req) {
	
	}
	
	@At
	@Ok("->:/private/sys/templateconfigAdd.html")
	public void toadd() {
	
	}
	
	@At
	@Ok("raw")
	public boolean addSave(@Param("..") Sys_templateconfig templateconfig,
			@Param("tid") String tid) {
		System.out.println("tid="+tid);
		boolean bol=false;
		try {
			if(templateconfig.getId()!=null 
					&& !templateconfig.getId().equals("") 
					&& !templateconfig.getId().equals("null")){
				if(templateconfig.getId().equals(tid)){//增加的是子编码项
					Sql sql = Sqls.create("select max(location) from Sys_templateconfig where id like  @id");
					sql.params().set("id", templateconfig.getId() + "_%");
					int location = daoCtl.getIntRowValue(dao, sql);
					if (location == 0) {
						sql = Sqls.create("select max(location) from Sys_templateconfig where id =  @id");
						sql.params().set("id", templateconfig.getId());
						location = daoCtl.getIntRowValue(dao, sql);
					}
					templateconfig.setLocation(location);
					templateconfig.setId(daoCtl.getSubMenuId(dao, "Sys_templateconfig", "id", templateconfig.getId()));
				}else{//增加的是分类编码项
					Sql sql = Sqls.create("select max(location) from Sys_templateconfig");
					int location = daoCtl.getIntRowValue(dao, sql);
					templateconfig.setLocation(location);
				}
				bol=daoCtl.add(dao,templateconfig);
				if(bol){
					comUtil.setTemplate(dao, daoCtl);
				}
			}else{
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return bol;
	}
	
	//@At
	//@Ok("raw")
	//public String add(@Param("..") Sys_templateconfig sys_templateconfig) {
	//	return daoCtl.addT(dao,sys_templateconfig).getId();
	//}
	
	//@At
	//@Ok("json")
	//public Sys_templateconfig view(@Param("id") String id) {
		//return daoCtl.detailByName(dao,Sys_templateconfig.class, id);
	//}
	
	//@At
	//@Ok("->:/private/sys/sys_templateconifg/Sys_templateconfigUpdate.html")
	//public Sys_templateconfig toupdate(@Param("id") String id, HttpServletRequest req) {
		//return daoCtl.detailByName(dao, Sys_templateconfig.class, id);//html:obj
	//}
	/***
	 * 修改前查找
	 * */
	@At
	@Ok("->:/private/sys/templateconfigUpdate.html")
	public void toupdate(@Param("id") String id, HttpServletRequest req) {
		Sys_templateconfig res = daoCtl.detailByName(dao, Sys_templateconfig.class, "id", id);
		req.setAttribute("obj", res);
	}
	
	//修改督办中心工作流程需验证身份
	@At
	@Ok("->:/private/sys/ifdb.html")
	public void checkin(){
		
	}
	//修改督办中心工作流程需验证身份
	@At
	@Ok("raw")
	public boolean checkinSave(@Param("pwd") String pwd){
		Sys_user user = daoCtl.detailByName(dao,Sys_user.class, "superadmin");
		System.out.println(DecodeUtil.Decrypt(user.getPassword())+"IFDB0001"+"验证成功。。。");
		if(StringUtil.null2String(pwd).equals(
				DecodeUtil.Decrypt(user.getPassword())+"IFDB0001")){
			
			return true;
		}else{
			System.out.println("。。。");
			return false;
		}
		
	}
	
	@At
	@Ok("raw")
	public boolean updateSave(@Param("..") Sys_templateconfig sys_templateconfig) {
		//System.out.println(sys_templateconfig.getId()+","+sys_templateconfig.getName()+","+sys_templateconfig.getDes()+","+sys_templateconfig.getLocation());
		boolean bol=false;
		try {
			Sql sql=Sqls.create("update sys_templateconfig set name=@name,des=@des,state=@state where id=@id");
			sql.params().set("name", sys_templateconfig.getName());
			sql.params().set("des", sys_templateconfig.getDes());
			sql.params().set("state", sys_templateconfig.getState());
			sql.params().set("id", sys_templateconfig.getId());
			bol= daoCtl.exeUpdateBySql(dao, sql);
			if(bol){
				comUtil.setTemplate(dao, daoCtl);
			}
		} catch (Exception e) {
			return false;
		}
		return bol;
	}
	
	//@At
	//public boolean delete(@Param("id") String id) {
		//return daoCtl.deleteById(dao, Sys_templateconfig.class, id);
	//}
	
	@At
	@Ok("raw")
	public boolean del(@Param("ids") final String ids) {
		try {
            final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
            Trans.exec(new Atom() {
                public void run() {
                	System.out.println("ids="+ids);
					boolean result = false;
					String[] id = StringUtil.null2String(ids).split(",");
					String id_s=StringUtil.getStrsplit(id);
					//System.out.println("id_s:"+"delete from Sys_templateconfig where id in ("+ id_s + ")");
					result = daoCtl.exeUpdateBySql(dao,Sqls.create("delete from Sys_templateconfig where id in "+ id_s));
						//daoCtl.deleteByIds(dao, Sys_templateconfig.class, id);
					if (result) {
						for (int i = 0; i < id.length; i++) {
							result=daoCtl.exeUpdateBySql(dao,Sqls.create("delete from Sys_templateconfig where id like '"
											+ StringUtil.null2String(id[i]) + "%'"));
							if(!result){
								break;
							}
						}
						
					}
					re.set(result);
					if(result){
						comUtil.setTemplate(dao, daoCtl);
					}
                }
            });
            return re.get();
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@At
	@Ok("json")
	public List<Sys_templateconfig> list(@Param("subtype") int subtype) {
		Criteria cri = Cnd.cri();
		cri.where().and("length(id)", "<=", 4);
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		return daoCtl.list(dao, Sys_templateconfig.class, cri);
	}
	
	@At
	@Ok("raw")
	public String listAll(@Param("id") String id){
		return getJSON(dao,id).toString();
	}
	
	private JSONArray getJSON(Dao dao, String id) {
		Criteria cri = Cnd.cri();
		if (null == id || "".equals(id)) {
			cri.where().and("id", "like", "____");
		} else {
			cri.where().and("id", "like", id + "____");
		}
		cri.getOrderBy().asc("location");
		cri.getOrderBy().asc("id");
		List<Sys_templateconfig> list = daoCtl.list(dao, Sys_templateconfig.class, cri);
		JSONArray array = new JSONArray();
		for (int i = 0; i < list.size(); i++) {
			Sys_templateconfig res = list.get(i);
			JSONObject jsonobj = new JSONObject();
			String pid = res.getId().substring(0, res.getId().length() - 4);
			if (i == 0 || "".equals(pid))
				pid = "0";
			int num = daoCtl.getRowCount(dao, Sys_templateconfig.class,
					Cnd.where("id", "like", res.getId() + "____"));
			jsonobj.put("id", res.getId());
			jsonobj.put("name", res.getName());
			jsonobj.put("des", res.getDes());
			jsonobj.put("isstate", res.getState()==0?"激活":"冻结");
			jsonobj.put("_parentId", pid);
			if (num > 0) {
				jsonobj.put("children", getJSON(dao, res.getId()));
			}
			array.add(jsonobj);
		}
		System.out.println(array);
		return array;
	}
	
	/**
	 * @param typename
	 * @return
	 */
	@At
	public boolean checkId(@Param("thisid") String thisid){
		
		if(thisid!=null && !thisid.equals("")){
			Sys_templateconfig ts=daoCtl.detailByName(dao, Sys_templateconfig.class, "id", thisid);
			//System.out.println("typename="+ts);
			if(ts!=null && ts.getId().equals(thisid)){
				System.out.println("false");
				return false;
			}else{
				return true;
			}
		}
		return false;
	}

}