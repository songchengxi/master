package org.jeecgframework.web.system.service.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.scx.system.entity.CompanyRoleUser;
import org.jeecgframework.web.system.pojo.base.TSLog;
import org.jeecgframework.web.system.pojo.base.TSRoleUser;
import org.jeecgframework.web.system.pojo.base.TSUser;
import org.jeecgframework.web.system.service.UserService;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jeecgframework.core.common.service.impl.CommonServiceImpl;
import org.jeecgframework.core.constant.Globals;
import org.jeecgframework.core.util.BrowserUtils;
import org.jeecgframework.core.util.ContextHolderUtils;
import org.jeecgframework.core.util.DateUtils;
import org.jeecgframework.core.util.ResourceUtil;
import org.jeecgframework.core.util.oConvertUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author  张代浩
 *
 */
@Service("userService")
@Transactional
public class UserServiceImpl extends CommonServiceImpl implements UserService {

	public TSUser checkUserExits(TSUser user){
		return this.commonDao.getUserByUserIdAndUserNameExits(user);
	}
	public String getUserRole(TSUser user){
		return this.commonDao.getUserRole(user);
	}
	
	public void pwdInit(TSUser user,String newPwd) {
			this.commonDao.pwdInit(user,newPwd);
	}
	
	public int getUsersOfThisRole(String id) {
		Criteria criteria = getSession().createCriteria(TSRoleUser.class);
		criteria.add(Restrictions.eq("TSRole.id", id));
		int allCounts = ((Long) criteria.setProjection(
				Projections.rowCount()).uniqueResult()).intValue();
		return allCounts;
	}
	
	@Override
	public String trueDel(TSUser user) {
		String message;
		List<CompanyRoleUser> roleUser = this.commonDao.findByProperty(CompanyRoleUser.class, "TSUser.id", user.getId());
		if (!user.getStatus().equals(Globals.User_ADMIN)) {
			if (roleUser.size() > 0) {
				// 删除用户时先删除用户和角色关系表
				delRoleUser(user);
				this.commonDao.executeSql("delete from t_s_user_org where user_id=?", user.getId()); // 删除 用户-机构 数据
                this.commonDao.delete(user);
				message = "用户：" + user.getUserName() + "删除成功";
				this.addLog(message, Globals.Log_Type_DEL, Globals.Log_Leavel_INFO);
			} else {
				this.commonDao.delete(user);
				message = "用户：" + user.getUserName() + "删除成功";
			}
		} else {
			message = "超级管理员不可删除";
		}
		return message;
	}
	
	private void delRoleUser(TSUser user) {
		// 同步删除用户角色关联表
		List<CompanyRoleUser> roleUserList = this.commonDao.findByProperty(CompanyRoleUser.class, "TSUser.id", user.getId());
		if (roleUserList.size() >= 1) {
			for (CompanyRoleUser roleUser : roleUserList) {
				this.commonDao.delete(roleUser);
			}
		}
	}
	
	/**
	 * 添加日志
	 */
	private void addLog(String logcontent, Short loglevel, Short operatetype) {
		HttpServletRequest request = ContextHolderUtils.getRequest();
		String broswer = BrowserUtils.checkBrowse(request);
		TSLog log = new TSLog();
		log.setLogcontent(logcontent);
		log.setLoglevel(loglevel);
		log.setOperatetype(operatetype);
		log.setNote(oConvertUtils.getIp());
		log.setBroswer(broswer);
		log.setOperatetime(DateUtils.gettimestamp());
//		log.setTSUser(ResourceUtil.getSessionUser());
		/*start chenqian 201708031TASK #2317 【改造】系统日志表，增加两个字段，避免关联查询 [操作人账号] [操作人名字]*/
		TSUser u = ResourceUtil.getSessionUser();
		log.setUserid(u.getId());
		log.setUsername(u.getUserName());
		log.setRealname(u.getRealName());
		/*update-end--Author chenqian 201708031TASK #2317 【改造】系统日志表，增加两个字段，避免关联查询 [操作人账号] [操作人名字]*/
		commonDao.save(log);
	}
}
