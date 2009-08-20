package org.springside.examples.showcase.common.web;

import java.util.List;

import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.hibernate.StaleStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springside.examples.showcase.common.entity.User;
import org.springside.examples.showcase.common.service.UserManager;

/**
 * 用户管理Action.
 * 
 * @author calvin
 */
//因为没有按Convention Plugin默认的Pacakge命名规则, 因此用annotation重新指定Namespace.
@Namespace("/common")
@InterceptorRefs( { @InterceptorRef("paramsPrepareParamsStack") })
@Results( { @Result(name = CrudActionSupport.RELOAD, location = "user.action", type = "redirect") })
public class UserAction extends CrudActionSupport<User> {

	private static final long serialVersionUID = 4384919647659925184L;

	@Autowired
	private UserManager userManager;

	// 页面属性  //
	private User entity;
	private Long id;
	private List<User> allUsers;
	private Integer workingVersion;
	private List<Long> checkedUserIds;

	// ModelDriven 与 Preparable函数 //
	public User getModel() {
		return entity;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	protected void prepareModel() throws Exception {
		if (id != null) {
			entity = userManager.getUser(id);
		} else {
			entity = new User();
		}
	}

	// CRUD Action 函数 //
	@Override
	public String list() throws Exception {
		allUsers = userManager.getAllUser();
		return SUCCESS;
	}

	@Override
	public String input() throws Exception {
		return INPUT;
	}

	/**
	 * 保存用户时,演示Hibernate的version字段使用.
	 */
	@Override
	public String save() throws Exception {
		if (workingVersion < entity.getVersion())
			throw new StaleStateException("对象已有新的版本");

		userManager.saveUser(entity);
		return RELOAD;
	}

	@Override
	public String delete() throws Exception {
		throw new UnsupportedOperationException("delete操作暂时未支持");
	}

	// 其他Action函数 //
	public String disableUsers() {
		userManager.disableUsers(checkedUserIds);
		return RELOAD;
	}

	// 页面属性访问函数 //
	public List<User> getAllUsers() {
		return allUsers;
	}

	public void setCheckedUserIds(List<Long> checkedUserIds) {
		this.checkedUserIds = checkedUserIds;
	}

	public void setWorkingVersion(Integer workingVersion) {
		this.workingVersion = workingVersion;
	}
}
