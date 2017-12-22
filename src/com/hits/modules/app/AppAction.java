package com.hits.modules.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.QueryResult;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Criteria;
import org.nutz.dao.sql.Sql;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Strings;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.trans.Molecule;
import org.nutz.trans.Trans;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.util.DecodeUtil;
import com.hits.modules.bean.AppCommonModel;
import com.hits.modules.bean.Cs_value;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.CommonUtil;
import com.hits.util.DateUtil;
import com.hits.util.EmptyUtils;
import com.hits.util.FileUtil;
import com.hits.util.StringUtil;
import com.hits.util.SysLogUtil;

/**
 * 
 *  #(c) IFlytek hnsj_jk <br/>
 *
 *  版本说明: $id:$ <br/>
 *
 *  功能说明: app接口
 * 
 *  <br/>创建说明: 2016年9月2日 下午4:37:51 L H T  创建文件<br/>
 * 
 *  修改历史:<br/>
 *
 */
@IocBean
@At("/public/app")
@Filters({ @By(type = GlobalsFilter.class)})
public class AppAction extends BaseAction {
	
	@Inject
	protected Dao dao;
	
	@Inject("refer:appService")
	protected AppService appService;
	
	/**
	 * 功能描述:性质颜色
	 *
	 * @author L H T  2016年8月15日 上午10:28:30
	 * 
	 * @return
	 */
	public static Map<String,String> getXzColor(){
		Map<String,String> map=new HashMap<String,String>();
		map.put("1","f60");//投诉
		map.put("2","33cc99");//咨询
		map.put("3","007AFF");//建议
		map.put("4","f00");//举报
		return map;
		
	}
	
	@At
	@Ok("json")
	public AppCommonModel getHeadTitle() {
		AppCommonModel appModel = new AppCommonModel();
		try {
			Sql sql = Sqls.create("select value from cs_value where name='app_name'");
			String title=daoCtl.getStrRowValue(dao, sql);
			title=EmptyUtils.isNotEmpty(title) ? title : "";
			appModel.setObj(title);
		} catch (Exception e) {
			e.printStackTrace();
			appModel.setResult(-1);
		}
		return appModel;
	}
	
	/**
	 * 功能描述:用户登录 
	 *
	 * @author L H T  2016年7月21日 上午10:57:09
	 * 
	 * @param loginname
	 * 
	 * @param password
	 * 
	 * @return
	 */
	@At
	@Ok("json:full")
	public AppCommonModel doLogin(@Param("userName") final String userName,@Param("password") final String password) {
		AppCommonModel appModel = new AppCommonModel();
		try {
			appModel=Trans.exec(new Molecule<AppCommonModel>(){

				@Override
				public void run() {
					AppCommonModel appModel = new AppCommonModel();
					if (Strings.isBlank(userName) || Strings.isBlank(password)) {
						appModel.setResult(-1);
						appModel.setMsg("用户名或密码为空！");
					}else{
						Sys_user user = daoCtl.detailByName(dao, Sys_user.class,"loginname", userName);
						if (user == null) {
							// 增加日志
							SysLogUtil.addLog(dao, userName, SysLogUtil.login_log_type,"用户登陆用户名错误："+userName);
							appModel.setResult(-1);
							appModel.setMsg("用户名不存在");
						}  else if (!StringUtil.null2String(password).equals(DecodeUtil.Decrypt(user.getPassword()))) {
							// 增加日志
							SysLogUtil.addLog(dao, userName, SysLogUtil.login_log_type,"用户登陆密码错误");
							appModel.setResult(-1);
							appModel.setMsg("密码错误");
						} else {
							user.setAddress(user.getAddress().trim());
							dao.update(user);
							
							Map<String,String> map=new HashMap<String,String>();
							map.put("realname", StringUtil.null2String(user.getRealname()));
							map.put("address", StringUtil.null2String(user.getAddress()));//用户地址
							
							/*String touxiang="";
							if (EmptyUtils.isNotEmpty(user.getTouxiang())) {
								if(YWCL.isInner()){//内网
									touxiang=comUtil.fileurl+user.getTouxiang();
								}else{//外网
									touxiang=comUtil.fileurl_ww+user.getTouxiang();
								}
							}*/
							
							//map.put("touxiang", StringUtil.null2String(touxiang));//用户头像
							map.put("email", StringUtil.null2String(user.getEmail()));//用户邮箱
							appModel.setObj(map);
							appModel.setMsg("登录成功");
						}
					}
					setObj(appModel);
				}
				
			});
			
		} catch (Exception e) {
			appModel.setMsg("登录失败");
			appModel.setResult(-1);
			e.printStackTrace();
		}
		return appModel;
	}

	/**
	 * 获取所有单位
	 * @param loginname
	 * @return
	 */
	@At
	@Ok("json:full")
	public AppCommonModel getUnit(HttpServletRequest req) {
		AppCommonModel appModel = new AppCommonModel();
		appModel=Trans.exec(new Molecule<AppCommonModel>(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				AppCommonModel appModel = new AppCommonModel();
				Sql sql=Sqls.create("select id,name from sys_unit where unittype=88");
				List<Map> unitMap = new ArrayList<Map>();
				unitMap=daoCtl.list(dao, sql);
				appModel.setResult(1);
				appModel.setObj(unitMap);
				setObj(appModel);
			}
		});
		return appModel;
	}
	
	/**
	 * 获取单位下负责人的信息
	 * @param loginname
	 * @return
	 */
	@At
	@Ok("json:full")
	public AppCommonModel getKhxx(@Param("unitid") String unitid) {
		AppCommonModel appModel = new AppCommonModel();
		try {
			if(EmptyUtils.isNotEmpty(unitid)){
				Sql sql=Sqls.create("select * from sys_unit where id = "+unitid);
				Sys_unit unit = daoCtl.detailByName(dao, Sys_unit.class, unitid);
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("khxm", unit.getHandler());
				map.put("lxfs", unit.getHandlerphone());
				appModel.setResult(1);
				appModel.setObj(map);
			}else{
				appModel.setResult(-1);
			}
			
		} catch (Exception e) {
			appModel.setResult(-1);
		}
		return appModel;
	}
	
	/**异步查询指定分类的信息
	 *
	 * @author lht  2015年10月11日 下午8:40:50
	 * 
	 * @param typeid 配置所属分类id
	 * @param csvalue 代码值
	 * @return
	 */
	@At
	@Ok("json:full")
	public AppCommonModel getAjaxCsValue(HttpSession session,@Param("typeid") String typeid,@Param("cascade") int cascade) {
		AppCommonModel appModel = new AppCommonModel();
		try {
			Sql sql=null;
			String sqlstr="select typeid,name from Cs_value where state=0 and ";
			sqlstr+=" typeid='"+typeid+"' ";
			if (cascade==1) {
				sqlstr+="  and length(code)=4 ";
			}
			sqlstr+=" order by location asc,id asc";
			sql=Sqls.create(sqlstr);
			List<Map> list=daoCtl.list(dao, sql);
			appModel.setObj(list);
		} catch (Exception e) {
			appModel.setResult(-1);
		}
		return appModel;
	}
	
	/**异步查询级联信息
	 *
	 * @author lht  2015年10月11日 下午8:40:50
	 * 
	 * @param typeid 配置所属分类id
	 * @param csvalue 代码值
	 * @return
	 */
	@At
	@Ok("json:full")
	public AppCommonModel getAjaxCascadeCsValue(@Param("typeid") String typeid,@Param("value") String value) {
		AppCommonModel appModel = new AppCommonModel();
		List<Cs_value> list =new ArrayList<Cs_value>();
		Sql sql=Sqls.create("select * from Cs_value where typeid=@typeid and value=@value");
		sql.params().set("typeid", typeid);
		sql.params().set("value", value);
		Cs_value csvalue= daoCtl.detailBySql(dao, Cs_value.class, sql);
		
		if (EmptyUtils.isNotEmpty(csvalue)) {
			Sql cascadesql=Sqls.create("select * from Cs_value where typeid =@typeid and code like '"+csvalue.getCode()+"____' order by location asc,id asc");
			cascadesql.params().set("typeid", typeid);
			list=daoCtl.list(dao, Cs_value.class, cascadesql);
			
		}
		appModel.setObj(list);
		return appModel;
	}
}
	