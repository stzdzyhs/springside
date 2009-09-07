package org.springside.examples.showcase.functional.webservice.hessian;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springside.examples.showcase.ws.server.api.UserWebService;
import org.springside.examples.showcase.ws.server.api.result.GetAllUserResult;
import org.springside.modules.test.spring.SpringContextTestCase;

/**
 * UserService Hessian服务的功能测试, 测试主要的接口调用.
 * 
 * @author calvin
 */
@ContextConfiguration(locations = { "/webservice/applicationContext-hessian-client.xml" })
public class UserHessianServiceTest extends SpringContextTestCase {

	@Autowired
	UserWebService userHessianService;

	@Test
	public void getAllUser() {
		GetAllUserResult result = userHessianService.getAllUser();
		assertEquals(6, result.getUserList().size());
	}
}