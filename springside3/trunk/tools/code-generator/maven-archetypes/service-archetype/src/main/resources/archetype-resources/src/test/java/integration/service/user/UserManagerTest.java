#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.integration.service.user;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ${package}.entity.user.Role;
import ${package}.entity.user.User;
import ${package}.service.user.UserManager;
import ${package}.unit.ws.user.UserData;
import org.springside.modules.test.spring.SpringTxTestCase;

/**
 * UserManager的集成测试用例.
 * 
 * 调用实际的DAO类进行操作,默认在操作后进行回滚.
 * 
 * @author calvin
 */
public class UserManagerTest extends SpringTxTestCase {

	@Autowired
	private UserManager userManager;

	@Test
	//如果你需要真正插入数据库,将Rollback设为false
	//@Rollback(false) 
	public void saveUser() {
		User user = UserData.getRandomUser();
		Role role = UserData.getRandomRole();
		user.getRoles().add(role);
	
		userManager.saveUser(user);
		//强制执行sql语句
		flush();
		assertNotNull(user.getId());
	}

	@Test(expected = org.hibernate.exception.ConstraintViolationException.class)
	public void savenNotUniqueUser() {
		User user = UserData.getRandomUser();
		user.setLoginName("admin");

		userManager.saveUser(user);
		flush();
	}

	@Test
	public void authUser() {
		assertEquals(true, userManager.authenticate("admin", "admin"));
		assertEquals(false, userManager.authenticate("admin", ""));
	}
}