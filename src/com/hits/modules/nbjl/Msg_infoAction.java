package com.hits.modules.nbjl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.nutz.dao.Chain;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.json.Json;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import com.hits.common.action.BaseAction;
import com.hits.common.config.Globals;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.util.EmptyUtils;
import com.hits.util.DateUtil;
import com.hits.common.util.StringUtil;
import com.hits.modules.bean.Msg_fj;
import com.hits.modules.bean.Msg_info;
import com.hits.modules.bean.Msg_user;
import com.hits.modules.com.YWCL;
import com.hits.modules.com.comUtil;
import com.hits.modules.sys.bean.Sys_user;

/**
 * infostate  �ݸ壺-1  �ѷ��ͣ�1  �ѳ��أ�2 
 * ɾ��״̬��� delstate ������ɾ��  1  �ռ���ɾ��   2  
 * @author
 * @time 2014-05-06 13:33:35
 * 
 */
@IocBean
@At("/private/msg/msgInfo")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class Msg_infoAction extends BaseAction {
	@Inject
	protected Dao dao;

	/**
	 * ת��(�����Լ���)����鿴�б�
	 * 
	 * @param session
	 * @param req
	 */
	@At
	@Ok("->:/private/msg/tzggList.html")
	public void msg_info(HttpSession session, HttpServletRequest req, @Param("jstate") String jstate) {
		if (jstate == null || "".equals(jstate)) {
			req.setAttribute("jstate", -1);
		} else {
			req.setAttribute("jstate", jstate);
		}
		req.setAttribute("userMap", Json.toJson(comUtil.userMap));
	}

	/**
	 * ת���Լ������Ĺ����б���
	 * 
	 * @param session
	 * @param req
	 */
	@At
	@Ok("->:/private/msg/mytzggList.html")
	public void mymsg_info(HttpSession session, HttpServletRequest req) {
		req.setAttribute("userMap", Json.toJson(comUtil.userMap));
		req.setAttribute("infostate", 1);
	}

	/**
	 * ת��(�����Լ���)վ����Ϣ�鿴�б�
	 * 
	 * @param session
	 * @param req
	 */
	@At
	@Ok("->:/private/msg/msgMessage.html")
	public void msgMessage(HttpSession session, HttpServletRequest req, @Param("jstate") String jstate) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		if (jstate == null || "".equals(jstate)) {
			req.setAttribute("jstate", -1);
		} else {
			req.setAttribute("jstate", jstate);
		}
		req.setAttribute("userMap", Json.toJson(comUtil.userMap));
		List<String> roleList = user.getRolelist();
		if (roleList.contains("146")) {
			req.setAttribute("ishjzx", true);
		} else {
			req.setAttribute("ishjzx", false);
		}
	}

	@At
	@Ok("->:/private/msg/myMsgMessage.html")
	public void myMsgMessage(HttpSession session, HttpServletRequest req) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		req.setAttribute("infostate", 1);
		req.setAttribute("userMap", Json.toJson(comUtil.userMap));

		List<String> roleList = user.getRolelist();
		if (roleList.contains("146")) {
			req.setAttribute("ishjzx", true);
		} else {
			req.setAttribute("ishjzx", false);
		}
	}

	/*
	 * �ҷ��͵�վ����Ϣ�������
	 */
	@At
	@Ok("raw")
	public JSONObject fslist(HttpServletRequest req, HttpSession session, @Param("page") int curPage,
			@Param("rows") int pageSize, @Param("infotype") Integer infotype, @Param("infostate") String infostate,
			@Param("startdate") String startdate, @Param("enddate") String enddate, @Param("keys") String keys) {
		try {
			Sys_user user = (Sys_user) session.getAttribute("userSession");
			String sql = null;
			sql = "select * from MSG_INFO t where flogin = '" + user.getLoginname() + "' and infotype = " + infotype +" and fsdelete!=1";
			if (keys != null && !keys.equals("")) {
				sql += " and (title like '%" + keys + "%')";
			}
			if (EmptyUtils.isNotEmpty(infostate)) {
				sql += " and infostate=" + infostate;
			}
			sql += " order by ctime desc";
			// System.out.println(sql);
			req.setAttribute("infotype", infotype);
			req.setAttribute("infostate", infostate);
			return daoCtl.listPageJsonSql(dao, Sqls.create(sql), curPage, pageSize);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * �ҽ��յ�վ����Ϣ�������
	 */
	@At
	@Ok("raw")
	public JSONObject jslist(HttpServletRequest req, HttpSession session, @Param("page") int curPage,
			@Param("rows") int pageSize, @Param("infotype") Integer infotype, @Param("jstate") Integer jstate,
			@Param("keys") String keys) {
		try {
			Sys_user user = (Sys_user) session.getAttribute("userSession");
			String sql = null;
			// System.out.println("infotype="+infotype);
			req.setAttribute("jstate", jstate);
			sql = "select  i.*,u.jstate  from msg_info i,msg_user u where i.id=u.msgid and i.infotype=" + infotype
					+ "  and u.jlogin = '" + user.getLoginname() + "'  and i.infostate=1 and i.jsdelete!=1";
			if (jstate == -1) {
				sql += " and u.jstate!=2";// �ų���������Ϣ
			} else {
				sql += " and u.jstate=" + jstate;// δ����Ϣ
			}
			if (keys != null && !keys.equals("")) {
				sql += " and (i.title like '%" + keys + "%')";
			}
			sql += " order by u.ftime desc";
			req.setAttribute("infotype", infotype);
			// System.out.println(sql);
			return daoCtl.listPageJsonSql(dao, Sqls.create(sql), curPage, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@At
	@Ok("->:/private/msg/msgfj.html")
	public void msgFj() {
	}

	@At
	@Ok("->:/private/msg/tzggAdd.html")
	public void toadd(HttpSession session, HttpServletRequest request, @Param("infotype") Integer infotype) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		request.setAttribute("unitname", user.getUnitname());
		request.setAttribute("flogin", user.getLoginname());
		request.setAttribute("unitid", user.getUnitid());
		request.setAttribute("infotype", infotype);
		request.setAttribute("nowDate", DateUtil.getCurDateTime().substring(0, 10));
	}

	@At
	@Ok("raw")
	public boolean add(@Param("::msginfo") final Msg_info info, @Param("::msguser") final Msg_user user,
			@Param("fileno") final String fileno, @Param("fjmc") final String fjmc, @Param("fjurl") final String fjurl,
			@Param("title") final String title) {
		try {
			final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
			Trans.exec(new Atom() {
				public void run() {
					info.setFileno(fileno);
					info.setTitle(title);
					info.setFlogin(user.getFlogin());
					if (info.getInfotype() != 0) {
						info.setCtime(DateUtil.getCurDateTime());
					}
					Msg_info msginfo = dao.insert(info);
					if (EmptyUtils.isNotEmpty(msginfo)) {
						String[] jlogin = StringUtil.null2String(user.getJlogin()).split(";");
						String[] users=new String[jlogin.length];
						for (int i = 0; i < jlogin.length; i++) {
							String jjlogin = jlogin[i].substring(jlogin[i].lastIndexOf("-") + 1, jlogin[i].length());
							users[i]=jjlogin;
						}
						String insertSql="insert into msg_user(id,msgid,flogin,ftime,jlogin,jstate,jsign,ext1) " 
								+"select msg_user_s.nextval,"+msginfo.getId()+",'"+msginfo.getFlogin()+"','"+msginfo.getCtime()+"',"
								+"loginname,0,0,0 from sys_user where loginname in "+StringUtil.getStrsplit(users);
						System.out.println(insertSql);
						dao.execute(Sqls.create(insertSql));
						/*if (EmptyUtils.isNotEmpty(fjmc) && EmptyUtils.isNotEmpty(fjurl)) {
							String[] fjmcs = StringUtil.null2String(fjmc).split("\\|");
							String[] fjurls = StringUtil.null2String(fjurl).split("\\|");
							for (int i = 0; i < fjmcs.length; i++) {
								Msg_fj msgfj = new Msg_fj();
								msgfj.setFjmc(fjmcs[i]);
								msgfj.setFjurl(fjurls[i]);
								msgfj.setMsgid(msginfo.getId());
								dao.insert(msgfj);
							}
						}*/
						re.set(true);
					} else {
						re.set(false);
					}
				}
			});
			return re.get();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@At
	@Ok("raw")
	public boolean addCg(@Param("::msginfo") final Msg_info info, @Param("::msguser") final Msg_user user,
			@Param("fileno") final String fileno, @Param("fjmc") final String fjmc, @Param("fjurl") final String fjurl,
			@Param("title") final String title) {
		info.setInfostate(-1);
		try {
			final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
			Trans.exec(new Atom() {
				public void run() {
					info.setFileno(fileno);
					info.setTitle(title);
					info.setFlogin(user.getFlogin());
					if (EmptyUtils.isNotEmpty(fjmc) && EmptyUtils.isNotEmpty(fjurl)) {
						info.setExt3(fjmc);
					}
					if (info.getInfotype() != 0) {
						info.setCtime(DateUtil.getCurDateTime());
					}
					Msg_info msginfo = dao.insert(info);
					if (EmptyUtils.isNotEmpty(msginfo)) {
						String[] jlogin = StringUtil.null2String(user.getJlogin()).split(";");
						String[] users=new String[jlogin.length];
						for (int i = 0; i < jlogin.length; i++) {
							String jjlogin = jlogin[i].substring(jlogin[i].lastIndexOf("-") + 1, jlogin[i].length());
							users[i]=jjlogin;
						}
						String insertSql="insert into msg_user(id,msgid,flogin,ftime,jlogin,jstate,jsign,ext1) " 
								+"select msg_user_s.nextval,"+msginfo.getId()+",'"+msginfo.getFlogin()+"','"+msginfo.getCtime()+"',"
								+"loginname,0,0,0 from sys_user where loginname in "+StringUtil.getStrsplit(users);
						System.out.println("cs:"+insertSql);
						dao.execute(Sqls.create(insertSql));
						/*if (EmptyUtils.isNotEmpty(fjmc) && EmptyUtils.isNotEmpty(fjurl)) {
							String[] fjmcs = StringUtil.null2String(fjmc).split("\\|");
							String[] fjurls = StringUtil.null2String(fjurl).split("\\|");
							for (int i = 0; i < fjmcs.length; i++) {
								Msg_fj msgfj = new Msg_fj();
								msgfj.setFjmc(fjmcs[i]);
								msgfj.setFjurl(fjurls[i]);
								msgfj.setMsgid(msginfo.getId());
								dao.insert(msgfj);
							}
						}*/
						re.set(true);
					} else {
						re.set(false);
					}
				}
			});
			return re.get();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ��ת����Ϣ���ҳ��
	 * 
	 * @param session
	 * @param request
	 */
	@At
	@Ok("->:/private/msg/msgAdd.html")
	public void toaddMess(HttpSession session, HttpServletRequest request) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		request.setAttribute("unitname", user.getUnitname());
		request.setAttribute("flogin", user.getLoginname());
		request.setAttribute("unitid", user.getUnitid());
		request.setAttribute("infotype", 2);

		List<String> roleList = user.getRolelist();
		if (roleList.contains("146")) {
			request.setAttribute("ishjzx", true);
		} else {
			request.setAttribute("ishjzx", false);
		}

	}
	/**
	 * ��ת����Ϣ���ҳ��
	 * 
	 * @param session
	 * @param request
	 */
	@At
	@Ok("->:/private/msg/msgHF.html")
	public void tohuifu(HttpSession session, HttpServletRequest request,@Param("id") int id) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		request.setAttribute("unitname", user.getUnitname());
		request.setAttribute("flogin", user.getLoginname());
		request.setAttribute("unitid", user.getUnitid());
		request.setAttribute("infotype", 2);
		Msg_info msg_info = daoCtl.detailById(dao, Msg_info.class, id);
		request.setAttribute("title", "�ظ���"+msg_info.getTitle());
		request.setAttribute("jlogin", comUtil.userMap.get(msg_info.getFlogin())+"-"+msg_info.getFlogin()+";");
		request.setAttribute("ext2", msg_info.getFlogin());
	}
	/**
	 * ��ת����Ϣ���ҳ��
	 * 
	 * @param session
	 * @param request
	 */
	@At
	@Ok("->:/private/msg/msgZF.html")
	public void tozhuanfa(HttpSession session, HttpServletRequest request,@Param("id") int id) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		request.setAttribute("unitname", user.getUnitname());
		request.setAttribute("flogin", user.getLoginname());
		request.setAttribute("unitid", user.getUnitid());
		request.setAttribute("infotype", 2);
		Msg_info msg_info = daoCtl.detailById(dao, Msg_info.class, id);
		request.setAttribute("title", "ת����"+msg_info.getTitle());
		request.setAttribute("note", msg_info.getContent());
		List<String> roleList = user.getRolelist();
		if (roleList.contains("146")) {
			request.setAttribute("ishjzx", true);
		} else {
			request.setAttribute("ishjzx", false);
		}
	}

	/**
	 * ��ת����Ϣ�޸�ҳ��
	 * 
	 * @param id
	 * @param req
	 * @param session
	 */
	@At
	@Ok("->:/private/msg/msgUpdate.html")
	public void toupdateMess(@Param("id") int id, HttpServletRequest req, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Msg_info msg_info = daoCtl.detailById(dao, Msg_info.class, id);
		List<Msg_user> listUser = daoCtl.list(dao, Msg_user.class,
				Sqls.create("select * from Msg_user where msgid = " + msg_info.getId()));
		List<Msg_fj> listFj = daoCtl.list(dao, Msg_fj.class,
				Sqls.create("select * from Msg_fj where msgid = " + msg_info.getId()));
		JSONArray arr = new JSONArray();
		List<String> objFj = new ArrayList<String>();
		for (int i = 0; i < listUser.size(); i++) {
			Sys_user obj = daoCtl.detailByName(dao, Sys_user.class, "loginname", listUser.get(i).getJlogin());
			if (obj == null)
				continue;
			JSONObject json = new JSONObject();
			json.put("realname", obj.getRealname());
			json.put("loginname", obj.getLoginname());
			arr.add(json);

		}
		for (Msg_fj fj : listFj) {
			objFj.add("{fjmc:'" + fj.getFjmc() + "',fjurl:'" + fj.getFjurl() + "'}");
		}
		req.setAttribute("obj", msg_info);
		req.setAttribute("listUser", arr);
		req.setAttribute("listFj", listFj);
		req.setAttribute("fjStr", objFj);
		req.setAttribute("user", user);

		List<String> roleList = user.getRolelist();
		if (roleList.contains("146")) {
			req.setAttribute("ishjzx", true);
		} else {
			req.setAttribute("ishjzx", false);
		}

	}

	@At
	@Ok("->:/private/msg/msgView.html")
	public void view(@Param("id") Integer id, HttpServletRequest request, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Msg_info info = daoCtl.detailById(dao, Msg_info.class, id);
		if (EmptyUtils.isNotEmpty(info)) {
			String note = StringUtil.null2String(info.getContent());
			String fileurl="";
//			if(YWCL.isInner()){//����
//				fileurl=comUtil.fileurl;
//			}else{//����
//				fileurl=comUtil.fileurl_ww;
//			}
//			note = note.replaceAll("/upload/", fileurl+"/");
//			note = note.replaceAll(Globals.APP_NAME+"/ueditor/", fileurl+"/ueditor/");
			info.setContent(note);
			request.setAttribute("obj", info);
			//List<Map> fjlist = Msg_fj.getFjList(daoCtl, dao, info.getId() + "");
			//request.setAttribute("fjlist", fjlist);
			Msg_user msgUser = daoCtl.detailBySql(dao, Msg_user.class, Sqls.create("select * from msg_user where msgid="
					+ info.getId() + " and jlogin='" + user.getLoginname() + "'"));
			if (msgUser != null && msgUser.getJstate() == 0) {// �����δ����Ϣ���޸���鿴״̬Ϊ���Ѷ���
				msgUser.setJstate(1);
				daoCtl.update(dao, msgUser);
			}
		}
	}

	@At
	@Ok("->:/private/msg/tzggUpdate.html")
	public void toupdate(@Param("id") int id, HttpServletRequest req, HttpSession session) {
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		Msg_info msg_info = daoCtl.detailById(dao, Msg_info.class, id);
		List<Msg_user> listUser = daoCtl.list(dao, Msg_user.class,
				Sqls.create("select * from Msg_user where msgid = " + msg_info.getId()));
		List<Msg_fj> listFj = daoCtl.list(dao, Msg_fj.class,
				Sqls.create("select * from Msg_fj where msgid = " + msg_info.getId()));
		List<String> objUser = new ArrayList<String>();
		List<String> objFj = new ArrayList<String>();
		for (int i = 0; i < listUser.size(); i++) {
			Sys_user obj = daoCtl.detailByName(dao, Sys_user.class, "loginname", listUser.get(i).getJlogin());
			objUser.add("{'realname':'" + obj.getRealname() + "','loginname':'" + obj.getLoginname() + "'}");
		}
		for (Msg_fj fj : listFj) {
			objFj.add("{fjmc:'" + fj.getFjmc() + "',fjurl:'" + fj.getFjurl() + "'}");
		}
		req.setAttribute("obj", msg_info);
		req.setAttribute("listUser", objUser);
		req.setAttribute("listFj", listFj);
		req.setAttribute("fjStr", objFj);
		req.setAttribute("user", user);
	}

	@At
	public boolean update(@Param("::msginfo") final Msg_info info, @Param("::msguser") final Msg_user user,
			@Param("fileno") final String fileno, @Param("fjmc") final String fjmc, @Param("fjurl") final String fjurl,
			@Param("title") final String title) {
		try {
			final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
			Trans.exec(new Atom() {
				public void run() {
					Integer msgid = info.getId();
					// ɾ�����ݱ���ԭ�����û���ԭ����
					dao.execute(Sqls.create("delete msg_fj where msgid = " + msgid));
					dao.execute(Sqls.create("delete msg_user where msgid = " + msgid));
					info.setTitle(title);
					info.setFileno(fileno);
					info.setFlogin(user.getFlogin());
					if (EmptyUtils.isNotEmpty(fjmc) && EmptyUtils.isNotEmpty(fjurl)) {
						info.setExt3(fjmc);
					}
					if (info.getInfotype() != 0) {
						info.setCtime(DateUtil.getCurDateTime());
					}
					int ret = dao.update(info);
					// System.out.println("ret=" + ret);
					if (ret < 0) {
						re.set(false);
						return;
					}
					if (EmptyUtils.isNotEmpty(info)) {
						String[] jlogin = StringUtil.null2String(user.getJlogin()).split(";");
						String[] users=new String[jlogin.length];
						for (int i = 0; i < jlogin.length; i++) {
							String jjlogin = jlogin[i].substring(jlogin[i].lastIndexOf("-") + 1, jlogin[i].length());
							users[i]=jjlogin;
						}
						String insertSql="insert into msg_user(id,msgid,flogin,ftime,jlogin,jstate,jsign,ext1) " 
								+"select msg_user_s.nextval,"+info.getId()+",'"+info.getFlogin()+"','"+info.getCtime()+"',"
								+"loginname,0,0,0 from sys_user where loginname in "+StringUtil.getStrsplit(users);
						dao.execute(Sqls.create(insertSql));
					}
					/*Msg_fj msgfj = new Msg_fj();
					if (EmptyUtils.isNotEmpty(fjmc) && EmptyUtils.isNotEmpty(fjurl)) {
						String[] fjmcs = StringUtil.null2String(fjmc).split(",");
						String[] fjurls = StringUtil.null2String(fjurl).split(",");
						msgfj.setMsgid(msgid);
						for (int i = 0; i < fjmcs.length; i++) {
							String fjmc_1 = fjmcs[i];
							String fjurl_1 = fjurls[i];
							msgfj.setFjmc(fjmc_1);
							msgfj.setFjurl(fjurl_1);
							dao.insert(msgfj);
						}
					}*/
					re.set(true);
				}
			});
			return re.get();
		} catch (Exception e) {
			return false;
		}
	}

	@At
	public boolean updateCg(@Param("::msginfo") final Msg_info info, @Param("::msguser") final Msg_user user,
			@Param("fileno") final String fileno, @Param("fjmc") final String fjmc, @Param("fjurl") final String fjurl,
			@Param("title") final String title) {
		try {
			final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
			Trans.exec(new Atom() {
				public void run() {
					Integer msgid = info.getId();
					// ɾ�����ݱ���ԭ�����û���ԭ����
					dao.execute(Sqls.create("delete msg_fj where msgid = " + msgid));
					dao.execute(Sqls.create("delete msg_user where msgid = " + msgid));
					info.setTitle(title);
					info.setFileno(fileno);
					info.setFlogin(user.getFlogin());
					if (info.getInfotype() != 0) {
						info.setCtime(DateUtil.getCurDateTime());
					}
					info.setInfostate(-1);
					int ret = dao.update(info);
					// System.out.println("ret=" + ret);
					if (ret < 0) {
						re.set(false);
						return;
					}
					if (EmptyUtils.isNotEmpty(info)) {
						String[] jlogin = StringUtil.null2String(user.getJlogin()).split(";");
						String[] users=new String[jlogin.length];
						for (int i = 0; i < jlogin.length; i++) {
							String jjlogin = jlogin[i].substring(jlogin[i].lastIndexOf("-") + 1, jlogin[i].length());
							users[i]=jjlogin;
						}
						String insertSql="insert into msg_user(id,msgid,flogin,ftime,jlogin,jstate,jsign,ext1) " 
								+"select msg_user_s.nextval,"+info.getId()+",'"+info.getFlogin()+"','"+info.getCtime()+"',"
								+"loginname,0,0,0 from sys_user where loginname in "+StringUtil.getStrsplit(users);
						dao.execute(Sqls.create(insertSql));
					}
					/*Msg_fj msgfj = new Msg_fj();
					if (EmptyUtils.isNotEmpty(fjmc) && EmptyUtils.isNotEmpty(fjurl)) {
						String[] fjmcs = StringUtil.null2String(fjmc).split("\\|");
						String[] fjurls = StringUtil.null2String(fjurl).split("\\|");
						msgfj.setMsgid(msgid);
						for (int i = 0; i < fjmcs.length; i++) {
							String fjmc_1 = fjmcs[i];
							String fjurl_1 = fjurls[i];
							msgfj.setFjmc(fjmc_1);
							msgfj.setFjurl(fjurl_1);
							dao.insert(msgfj);
						}
					}*/
					re.set(true);
				}
			});
			return re.get();
		} catch (Exception e) {
			return false;
		}
	}

	@At
	@Ok("raw")
	public boolean delete(@Param("ids") final String ids) {
		try {
			final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
			Trans.exec(new Atom() {
				public void run() {
					String[] id = StringUtil.null2String(ids).split(",");
					String sqll = "select * from msg_info where  id in "
							+ StringUtil.getIdsplit(id);
					int size = daoCtl.list(dao, Sqls.create(sqll)).size();
					if (size == id.length) {
						boolean result = daoCtl.deleteByIds(dao, Msg_info.class, id);
						if (result) {
							for (String pk : id) {
								String sql = "delete msg_user where msgid = " + pk + "";
								dao.execute(Sqls.create(sql));
							}
						}
						re.set(result);
					} else {
						re.set(false);
					}
				}
			});
			return re.get();
		} catch (Exception e) {
			return false;
		}
	}

	@At
	@Ok("raw")
	public boolean send(@Param("ids") final String ids) {
		try {
			// System.out.println("send:"+ids);
			final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
			Trans.exec(new Atom() {
				public void run() {
					String[] id = StringUtil.null2String(ids).split(",");
					String sqll = "select * from msg_info where infostate=-1 and id in " + StringUtil.getIdsplit(id);

					int size = daoCtl.list(dao, Sqls.create(sqll)).size();
					// System.out.println(size == id.length);
					if (size == id.length) {
						for (String pk : id) {
							String sql = "update msg_info set infostate=1 where id= " + pk + "";
							dao.execute(Sqls.create(sql));
						}

						re.set(true);
					} else {
						re.set(false);
					}
				}
			});
			return re.get();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@At
	public boolean isCg(@Param("id") String id, HttpServletRequest req) {
		Msg_info mi = daoCtl.detailBySql(dao, Msg_info.class, Sqls.create("select * from msg_info where id=" + id));
		if (mi.getInfostate() == -1 || mi.getInfostate() == 2) {
			return true;
		}
		return false;
	}

	@At
	@Ok("raw")
	public String revocation(@Param("ids") String ids) {
		try {
			String[] id = StringUtil.null2String(ids).split(",");
			String sql = "select count(1) from msg_user where msgid in " + StringUtil.getStrsplit(id)
					+ " and jstate!=0";
			int count = daoCtl.getIntRowValue(dao, Sqls.create(sql));
			if (count >= 1) {
				return "�Ѷ��Ĳ��ܽ��г���";
			}
			String sqll = "select * from msg_info where infostate=-1 and id in " + StringUtil.getIdsplit(id);
			int size = daoCtl.list(dao, Sqls.create(sqll)).size();
			if (size == 0) {
				for (String pk : id) {
					Msg_info info = daoCtl.detailById(dao, Msg_info.class, Integer.parseInt(pk));
					if (info.getInfostate() == 2) {
						return "�ѳ����Ĳ��ܽ��г���";
					}
					info.setInfostate(2);
					daoCtl.update(dao, info);
				}
				return "�����ɹ�";
			}
			return "�ݸ��޷�����";
		} catch (Exception e) {
			e.printStackTrace();
			return "�������ִ���";
		}
	}

	@At
	public boolean deleteIds(@Param("ids") String ids) {
		String[] id = StringUtil.null2String(ids).split(",");
		return daoCtl.deleteByIds(dao, Msg_info.class, id);
	}

	/**
	 * ת���Լ�������������Ϣ���ۺϲ鿴����
	 * 
	 * @param session
	 * @param req
	 */
	@At
	@Ok("->:/private/msg/notices.html")
	public void showNotices(HttpSession session, HttpServletRequest req) {
		// System.out.println("�鿴��Ϣ");
		Sys_user user = (Sys_user) session.getAttribute("userSession");
		int ggnum = daoCtl.getIntRowValue(dao,
				Sqls.create(
						"select count(i.id) from msg_info i where i.infotype=0 and i.id in (select u.msgid from MSG_USER u where u.jstate =0 and u.jlogin = '"
								+ user.getLoginname() + "' )"));
		int znxxnum = daoCtl.getIntRowValue(dao,
				Sqls.create(
						"select count(i.id) from msg_info i where i.infotype=2 and i.id in (select u.msgid from MSG_USER u where u.jstate =0 and u.jlogin = '"
								+ user.getLoginname() + "' )"));
		int wjnum = daoCtl.getIntRowValue(dao,
				Sqls.create(
						"select count(i.id) from msg_info i where i.infotype=1 and i.id in (select u.msgid from MSG_USER u where u.jstate =0 and u.jlogin = '"
								+ user.getLoginname() + "' )"));
		req.setAttribute("ggnum", ggnum);
		req.setAttribute("znxxnum", znxxnum);
		req.setAttribute("wjnum", wjnum);
		String sql = null;
		sql = "select  i.*  from msg_info i,msg_user u where i.id=u.msgid and u.jlogin = '" + user.getLoginname()
				+ "' and u.jstate=0";
		sql += " order by i.ctime";
		List<Msg_info> t = daoCtl.list(dao, Msg_info.class, Sqls.create(sql));
		req.setAttribute("list", t);
	}
	
	/**
	 * ��ɾ��  yhb
	 * @param ids
	 * @return
	 */
	@At
	@Ok("raw")
	public boolean del(@Param("ids") final String ids,@Param("flag") final String flag) {
		try {
			final ThreadLocal<Boolean> re = new ThreadLocal<Boolean>();
			Trans.exec(new Atom() {
				public void run() {
					String[] id = StringUtil.null2String(ids).split(",");
					String sqll = "select * from msg_info where infostate in (-1,2) and id in "
							+ StringUtil.getIdsplit(id);
					dao.update(Msg_info.class, Chain.make(flag.equals("fs")?"fsdelete":"jsdelete", 1), Cnd.where("id", "in", StringUtil.getIdsplit(id)));
					re.set(true);
				}
			});
			return re.get();
		} catch (Exception e) {
			return false;
		}
	}
}