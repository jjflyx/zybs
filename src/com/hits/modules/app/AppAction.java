package com.hits.modules.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
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
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.POST;
import org.nutz.mvc.annotation.Param;
import org.nutz.trans.Atom;
import org.nutz.trans.Molecule;
import org.nutz.trans.Trans;

import com.hits.common.action.BaseAction;
import com.hits.common.dao.ObjectCtl;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.util.DecodeUtil;
import com.hits.modules.bean.AppCommonModel;
import com.hits.modules.bean.Cs_value;
import com.hits.modules.bean.File_info;
import com.hits.modules.com.YWCL;
import com.hits.modules.com.comUtil;
import com.hits.modules.file.UploadParam;
import com.hits.modules.file.WebMoreUploader;
import com.hits.modules.order.HkzdAction;
import com.hits.modules.order.OrderAction;
import com.hits.modules.order.bean.HkzdBean;
import com.hits.modules.order.bean.OrderBean;
import com.hits.modules.sys.bean.Sys_unit;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.CommonUtil;
import com.hits.util.DateUtil;
import com.hits.util.EmptyUtils;
import com.hits.util.FileUtil;
import com.hits.util.IpUtil;
import com.hits.util.StringUtil;
import com.hits.util.SysLogUtil;

/**
 * 
 * #(c) IFlytek hnsj_jk <br/>
 * 
 * 版本说明: $id:$ <br/>
 * 
 * 功能说明: app接口
 * 
 * <br/>
 * 创建说明: 2016年9月2日 下午4:37:51 L H T 创建文件<br/>
 * 
 * 修改历史:<br/>
 * 
 */
@IocBean
@At("/public/app")
@Filters({ @By(type = GlobalsFilter.class) })
public class AppAction extends BaseAction {

	@Inject
	protected Dao dao;

	@Inject("refer:appService")
	protected AppService appService;

	/**
	 * 功能描述:性质颜色
	 * 
	 * @author L H T 2016年8月15日 上午10:28:30
	 * 
	 * @return
	 */
	public static Map<String, String> getXzColor() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "f60");// 投诉
		map.put("2", "33cc99");// 咨询
		map.put("3", "007AFF");// 建议
		map.put("4", "f00");// 举报
		return map;

	}

	@At
	@Ok("json:full")
	public AppCommonModel getHeadTitle() {
		AppCommonModel appModel = new AppCommonModel();
		try {
			Sql sql = Sqls
					.create("select typevalue from SYS_USERPARAMCONFIG where typename='app_name'");
			String title = daoCtl.getStrRowValue(dao, sql);
			System.out.println("----" + title);
			title = EmptyUtils.isNotEmpty(title) ? title : "";

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
	 * @author L H T 2016年7月21日 上午10:57:09
	 * 
	 * @param loginname
	 * 
	 * @param password
	 * 
	 * @return
	 */
	@At
	@Ok("json:full")
	public AppCommonModel doLogin(@Param("userName") final String userName,
			@Param("password") final String password) {
		AppCommonModel appModel = new AppCommonModel();
		try {
			appModel = Trans.exec(new Molecule<AppCommonModel>() {
				@Override
				public void run() {
					AppCommonModel appModel = new AppCommonModel();
					if (Strings.isBlank(userName) || Strings.isBlank(password)) {
						appModel.setResult(-1);
						appModel.setMsg("用户名或密码为空！");
					} else {
						Sys_user user = daoCtl.detailByName(dao,
								Sys_user.class, "loginname", userName);
						if (user == null) {
							// 增加日志
							SysLogUtil.addLog(dao, userName,
									SysLogUtil.login_log_type, "用户登陆用户名错误："
											+ userName);
							appModel.setResult(-1);
							appModel.setMsg("用户名不存在");
						} else if (!StringUtil.null2String(password).equals(
								DecodeUtil.Decrypt(user.getPassword()))) {
							// 增加日志
							SysLogUtil.addLog(dao, userName,
									SysLogUtil.login_log_type, "用户登陆密码错误");
							appModel.setResult(-1);
							appModel.setMsg("密码错误");
						} else {
							user.setAddress(user.getAddress().trim());
							dao.update(user);
							Map<String, String> map = new HashMap<String, String>();
							map.put("loginname",StringUtil.null2String(user.getLoginname()));
							map.put("realname",StringUtil.null2String(user.getRealname()));
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
	 * 
	 * @param loginname
	 * @return
	 */
	@At
	@Ok("json:full")
	public AppCommonModel getUnit() {
		AppCommonModel appModel = new AppCommonModel();
		appModel = Trans.exec(new Molecule<AppCommonModel>() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				AppCommonModel appModel = new AppCommonModel();
				Sql sql = Sqls
						.create("select id,name from sys_unit where unittype=88");
				List<Map> unitMap = new ArrayList<Map>();
				unitMap = daoCtl.list(dao, sql);
				appModel.setResult(1);
				appModel.setObj(unitMap);
				setObj(appModel);
			}
		});
		return appModel;
	}

	/**
	 * 获取单位下负责人的信息
	 * 
	 * @param loginname
	 * @return
	 */
	@At
	@Ok("json:full")
	public AppCommonModel getKhxx(@Param("unitid") String unitid) {
		AppCommonModel appModel = new AppCommonModel();
		try {
			if (EmptyUtils.isNotEmpty(unitid)) {
				Sql sql = Sqls.create("select * from sys_unit where id = "
						+ unitid);
				Sys_unit unit = daoCtl
						.detailByName(dao, Sys_unit.class, unitid);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("khxm", unit.getHandler());
				map.put("lxfs", unit.getHandlerphone());
				appModel.setResult(1);
				appModel.setObj(map);
			} else {
				appModel.setResult(-1);
			}

		} catch (Exception e) {
			appModel.setResult(-1);
		}
		return appModel;
	}

	/**
	 * 获取单位下负责人的信息
	 * 
	 * @param loginname
	 * @return
	 */
	@At
	@Ok("json:full")
	public AppCommonModel getFkr() {
		AppCommonModel appModel = new AppCommonModel();
		try {
			Sql sql = Sqls
					.create("select userid,realname from sys_user where unitid ='0016' ");
			List<Map> unitMap = new ArrayList<Map>();
			unitMap = daoCtl.list(dao, sql);
			appModel.setResult(1);
			appModel.setObj(unitMap);
		} catch (Exception e) {
			appModel.setResult(-1);
		}
		return appModel;
	}

	@At
	@Ok("json")
	public AppCommonModel toaddZdLoad() {
		AppCommonModel appModel = new AppCommonModel();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("gmtj", comUtil.gmtjList);
			map.put("yt", comUtil.ytList);
			appModel.setObj(map);
		} catch (Exception e) {
			e.printStackTrace();
			appModel.setResult(-1);
			appModel.setMsg("请求错误");
		}
		return appModel;
	}

	/**
	 * 根据条件查询订单信息
	 * 
	 * @author lht 2015年10月11日 下午8:40:50
	 * 
	 * @param typeid
	 *            配置所属分类id
	 * @param csvalue
	 *            代码值
	 * @return
	 */
	@At
	@Ok("json:full")
	public AppCommonModel getOrderList(@Param("loginname") String loginname,
			@Param("searchtype") int searchtype, @Param("page") int curPage,
			@Param("rows") int pageSize) {
		AppCommonModel appModel = new AppCommonModel();
		try {
			Criteria cri = Cnd.cri();
			String sql = "select htid,ddmc,unit.name,yfjk,add_time,isfh from l_jsgg gg,sys_unit unit where gg.unitid=unit.id";
			if (searchtype == 0) {
				sql += " and isfh=0002 ";// 未付款
			} else {
				sql += " and isfh=0001 ";// 已付款
			}
			sql += " order by add_time desc";
			QueryResult qr = daoCtl.listPage(dao, OrderBean.class, curPage,
					pageSize, Sqls.create(sql), cri);
			appModel.setObj(qr);
		} catch (Exception e) {
			appModel.setResult(-1);
		}
		return appModel;
	}

	/**
	 * 根据条件查询订单信息
	 * 
	 * @author lht 2015年10月11日 下午8:40:50
	 * 
	 * @param typeid
	 *            配置所属分类id
	 * @param csvalue
	 *            代码值
	 * @return
	 */
	@At
	@Ok("json:full")
	public AppCommonModel getHkzdList(@Param("loginname") String loginname,
			@Param("searchtype") int searchtype, @Param("page") int curPage,
			@Param("rows") int pageSize) {
		AppCommonModel appModel = new AppCommonModel();
		try {
			Criteria cri = Cnd.cri();
			String sql = "select zdid,zdmc,user.realname,fkrq,isfk,sfjk from l_hkzd gg,sys_user user where gg.userid=user.userid";
			if (searchtype == 0) {
				sql += " and isfk=0002 ";// 未付款
			} else {
				sql += " and isfk=0001 ";// 已付款
			}
			sql += " order by fkrq desc";
			QueryResult qr = daoCtl.listPage(dao, HkzdBean.class, curPage,
					pageSize, Sqls.create(sql), cri);
			appModel.setObj(qr);
		} catch (Exception e) {
			appModel.setResult(-1);
		}
		return appModel;
	}
	
	// 新增账单
	@At
	@Ok("json")
	public AppCommonModel hkzdAdd(final HttpServletRequest request, HttpServletResponse response,@Param("..") final HkzdBean hkzd){
		final ThreadLocal<AppCommonModel> re = new ThreadLocal<AppCommonModel>();
		final AppCommonModel appModel = new AppCommonModel();
		Trans.exec(new Atom(){
			@Override
			public void run() {
				try {
					//获取参数
					WebMoreUploader upload = new WebMoreUploader(request);
					//因为app传参为GBK格式，需转码
					/**
					 * 银行的UTF-8变为本地的GBk，但仔细一想，既然本地应用得到了post的数据，本地java文件的编码格式又为GBk，
					 * 显然此时本地应用将银行post过来的中文编码格式不论是什么格式，都认为是GBK，按照GBK解码，所以就出现了乱码。
					 * 找到问题的根源，就好办了，既然乱码的原因是将UTF-8编码的中文，解码时用了GBk来解码，
					 * 那么解决办法就应该是将乱码重新用GBK编码，再用UTF-8解码。
					 */
					String zdmc=upload.getParameter("zdmc");
					 URL url = new URL(zdmc);
				        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
				        conn.setDoInput(true);
				        conn.setDoOutput(true);
				        conn.setRequestMethod("POST");
				        conn.setUseCaches(false);
				        conn.setInstanceFollowRedirects(true);
				        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
				        conn.connect();

				        DataOutputStream out = new DataOutputStream( conn.getOutputStream());
				        out.writeBytes(zdmc);
				        out.flush();
				        out.close();
				        BufferedReader bfReader= new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

				        String lines;
				        StringBuffer sb = new StringBuffer();
				        while ((lines = bfReader.readLine()) != null) {
				            // utf-8 无需再次转码
				            //lines = new String(lines.getBytes(), "UTF-8");
				            sb.append(lines);
				        }
				        bfReader.close();

				        // 断开连接
				        conn.disconnect();
				        System.out.println(sb.toString()+"------------------------");
					
					URLEncoder.encode(zdmc,"UTF-8");
					System.out.println("====112=====>"+zdmc);
					String gbk=URLEncoder.encode(zdmc,"GBK");
					System.out.println("========3====>"+gbk);
					 new String(gbk.getBytes("UTF-8"),"ISO-8859-1");   
					//zdmc = new String(zdmc.getBytes("GBk"),"UTF-8");
					//URLEncoder.encode(zdmc,"gbk");
					String bz = upload.getParameter("bz");
					bz = new String(bz.getBytes("GBK"),"UTF-8");
					for(int i = 0;i <= bz.length();i++){
						bz=java.net.URLDecoder.decode(bz, "UTF-8");
						System.out.println("==============>"+i);
					}
					hkzd.setZdmc(zdmc);
					hkzd.setUserid(upload.getParameter("fkr"));
					hkzd.setFkrq(upload.getParameter("fkrq"));
					hkzd.setGmtj(upload.getParameter("gmtj"));
					hkzd.setSfjk(upload.getParameter("yfjk"));
					hkzd.setIsfk(upload.getParameter("isfk"));
					hkzd.setYt(upload.getParameter("yt"));
					hkzd.setBz(bz);
					hkzd.setActor(upload.getParameter("loginname"));
					hkzd.setZdid(HkzdAction.getHtid());
					hkzd.setCreate_time(DateUtil.getCurDateTime());
					dao.insert(hkzd);
				    //保存图片
					String[] fileType = {".gif" , ".png" , ".jpg" , ".jpeg" , ".bmp"};
					upload.setAllowFiles(fileType);
					upload.upload();
					for (UploadParam param :upload.uploadlist) {
						if (param.getState().equals("SUCCESS")) {
							File_info fj=File_info.getFj("l_hkzd", hkzd.getZdid()+"", param.getOriginalName(), "/index"+param.getUrl(),param.getSize());
							dao.insert(fj);
							appModel.setResult(1);
							appModel.setMsg("保存成功");
						}else{
							appModel.setResult(-1);
							appModel.setMsg("图片上传出现错误");
							throw new RuntimeException("图片上传出现错误");
						}
					}
				} catch (Exception e) {
					appModel.setResult(-1);
					appModel.setMsg("数据提交失败");
					e.printStackTrace();
				}
				re.set(appModel);
			}
		});
		return appModel;
	}
	
}
