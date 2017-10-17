package com.hits.modules.tasks;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hits.common.util.DateUtil;
import com.hits.modules.sys.bean.Sys_user;
import com.hits.util.DeCompressUtil;
import com.hits.util.EmptyUtils;
import net.sf.json.JSONObject;

import org.nutz.dao.*;
import org.nutz.dao.sql.Criteria;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.lang.Files;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;

import com.hits.common.action.BaseAction;
import com.hits.common.filter.GlobalsFilter;
import com.hits.common.filter.UserLoginFilter;
import com.hits.common.util.StringUtil;
import com.hits.modules.tasks.bean.Tasks;
import org.nutz.trans.Atom;
import org.nutz.trans.Trans;

import java.io.File;
import java.io.IOException;

/**
 * @author Numbgui
 * @time 2016-03-17 17:15:06
 *
 */
@IocBean
@At("/private/tasks")
@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })
public class TasksAction extends BaseAction {
	private static String findFilePath = "";
	@Inject
	protected Dao dao;

	@At("/tolist")
	@Ok("->:/private/tasks/tasksList.html")
	public void tasks(HttpSession session,HttpServletRequest req) {
		System.out.println("In tasks()...");
	}

	@At
	@Ok("->:/private/tasks/tasksAdd.html")
	public void toAdd(HttpServletRequest req) {
		req.setAttribute("rule",daoCtl.getIntRowValue(dao,Sqls.create(" select count(id) from TASKS where is_rule = 1  ")));
	}

	@At
	@Ok("raw")
	public String add(@Param("..") Tasks tasks,@Param("fj_filepath")String filePath,@Param("fj_filename")String fileName,@Param("rule")Integer rule,
					  HttpSession session) {
		Sys_user user=(Sys_user) session.getAttribute("userSession");
		String fileRealPath = Mvcs.getServletContext().getRealPath("/tasks/" + fileName);
		String fileRealPathDir = fileRealPath.substring(0, fileRealPath.lastIndexOf("."));
		tasks.setActor(user.getLoginname());
		tasks.setAdd_time(DateUtil.getCurDateTime());
		tasks.setFile_path(fileRealPathDir);
		tasks.setIs_work(0);
		tasks.setIs_rule(EmptyUtils.isEmpty(rule)?0:rule);
		try {
			File oldfile = new File(Mvcs.getServletContext().getRealPath(filePath));
			File newFile = new File(fileRealPath);
			if (newFile.exists()) {
				newFile.delete();
			}
			Files.move(oldfile, newFile);
			//判断是否有同名目录存在
			if(new File(fileRealPathDir).exists()) {
				return "HaveTheSameDirectory";
			}else{
				DeCompressUtil.deCompress(fileRealPath,Mvcs.getServletContext().getRealPath("/tasks/"+fileName.substring(0,fileName.lastIndexOf("."))+"/"));
			}
			//删除上传的压缩文件
			if(newFile.exists()){
				newFile.delete();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return daoCtl.add(dao,tasks) ? "true" : "false";
	}

	@At
	@Ok("raw")
	public String ExecuteImmediately(@Param("file_id")String file_id){
		Tasks tasks = daoCtl.detailByCnd(dao,Tasks.class,Cnd.where("id","=",file_id));
		findFile(new File(tasks.getFile_path()),".jar");
		Runtime rt = Runtime.getRuntime();
		boolean flag = false;
		try{
			Process ps = rt.exec("cmd.exe /c start java -jar " + findFilePath +"");
			if (ps.waitFor() != 0) {
				//p.exitValue()==0表示正常结束，1：非正常结束
				if (ps.exitValue() == 1){
					System.err.println("命令执行失败!"+ps.exitValue());
				}
			}else{
				System.out.println("命令执行成功!");
				flag = true;
			}
			//批处理执行完后，根据cmd.exe进程名称 kill掉cmd窗口(这个方法是好不容易才找到了，网上很多介绍的都无效，csdn废我3分才找到这个方法)
//			killProcess();
		}catch(Exception e){
			e.printStackTrace();
		}
		return flag?"true":"false";
	}

	private static void killProcess(){
		Runtime rt = Runtime.getRuntime();
		Process p = null;
		try {
			rt.exec("cmd.exe /C start wmic process where name='cmd.exe' call terminate");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void findFile(File root, String name) {
		if (root.exists() && root.isDirectory()) {
			for (File file : root.listFiles()) {
				if (file.isFile() && file.getName().indexOf(name) >-1) {
					findFilePath = file.getPath();//这里输出文件名！
				}
			}
		}
	}

	@At
	@Ok("json")
	public Tasks view(@Param("id") String id) {
		return daoCtl.detailByName(dao,Tasks.class, id);
	}

	@At
	@Ok("->:/private/tasks/TasksUpdate.html")
	public void toupdate(@Param("id") String id, HttpServletRequest req) {
		Tasks tasks = daoCtl.detailByName(dao, Tasks.class, id);
		req.setAttribute("obj", tasks);
	}

	@At
	public boolean update(@Param("..") Tasks tasks) {

		return daoCtl.update(dao, tasks);
	}

	@At
	public boolean delete(@Param("id") String id) {
		return daoCtl.deleteByName(dao, Tasks.class, id);
	}

	@At
	public boolean deleteIds(@Param("ids") String ids) {
		final String[] idArr = StringUtil.null2String(ids).split(",");
		try {
			Trans.exec(new Atom() {
				@Override
				public void run() {
					Tasks tasks = null;
					for (String id : idArr) {
						tasks = dao.fetch(Tasks.class, Cnd.where("id", "=", id));
						System.out.println("tasks.getFile_path() : "+tasks.getFile_path());
						File file = new File(tasks.getFile_path());
						if (file.exists()) {
							if(file.listFiles().length==0) {
								file.delete();
							}else{
								deleteDir(file);
							}
						}
						dao.clear(Tasks.class, Cnd.where("id", "=", id));
					}
				}
			});
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//递归删除目录中的所有文件
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i=0; i<children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}


	@At
	@Ok("raw")
	public JSONObject list(@Param("keyword") String keyword, @Param("is_work") String is_work, @Param("page") int curPage, @Param("rows") int pageSize) {
		Criteria cri = Cnd.cri();
		cri.where().and("1", "=", "1");
		if (!"99".equals(is_work)) {
			cri.where().and("is_work", "=", is_work);
		}
		if (EmptyUtils.isNotEmpty(keyword)) {
			cri.where().andLike("name", keyword);
		}
		cri.getOrderBy().desc("is_work").desc("add_time");
		return daoCtl.listPageJson(dao, Tasks.class, curPage, pageSize, cri);
	}

}