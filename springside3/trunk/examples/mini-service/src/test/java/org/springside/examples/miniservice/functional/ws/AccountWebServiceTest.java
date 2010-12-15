package org.springside.examples.miniservice.functional.ws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.xml.ws.BindingProvider;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springside.examples.miniservice.data.AccountData;
import org.springside.examples.miniservice.entity.account.User;
import org.springside.examples.miniservice.functional.BaseFunctionalTestCase;
import org.springside.examples.miniservice.ws.AccountWebService;
import org.springside.examples.miniservice.ws.dto.UserDTO;
import org.springside.examples.miniservice.ws.result.CreateUserResult;
import org.springside.examples.miniservice.ws.result.SearchUserResult;
import org.springside.examples.miniservice.ws.result.WSResult;

/**
 * UserService Web服务的功能测试, 测试主要的接口调用.
 * 
 * 一般使用在Spring applicaitonContext.xml中用<jaxws:client/>创建的Client, 也可以用JAXWS的API自行创建.
 * 
 * @author calvin
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@ContextConfiguration(locations = { "/applicationContext-ws-client.xml" })
public class AccountWebServiceTest extends BaseFunctionalTestCase {

	@Autowired
	private AccountWebService accountWebService;

	/**
	 * 测试创建用户,在Spring applicaitonContext.xml中用<jaxws:client/>创建Client.
	 */
	@Test
	public void createUser() {
		User user = AccountData.getRandomUser();

		UserDTO userDTO = new UserDTO();
		userDTO.setLoginName(user.getLoginName());
		userDTO.setName(user.getName());
		userDTO.setEmail(user.getEmail());

		CreateUserResult result = accountWebService.createUser(userDTO);

		assertEquals(WSResult.SUCCESS, result.getCode());
		assertNotNull(result.getUserId());
	}

	/**
	 * 测试创建用户,使用错误的登录名, 在Spring applicaitonContext.xml中用<jaxws:client/>创建Client.
	 */
	@Test
	public void createUserWithInvalidLoginName() {
		User user = AccountData.getRandomUser();
		UserDTO userDTO = new DozerBeanMapper().map(user, UserDTO.class);

		//登录名为空
		userDTO.setLoginName(null);
		CreateUserResult result = accountWebService.createUser(userDTO);
		assertEquals(WSResult.PARAMETER_ERROR, result.getCode());

		//登录名重复
		userDTO.setLoginName("user1");
		result = accountWebService.createUser(userDTO);
		assertEquals(WSResult.PARAMETER_ERROR, result.getCode());
	}

	/**
	 * 测试搜索用户,使用CXF的API自行动态创建Client.
	 */
	@Test
	public void searchUser() {
		String address = BASE_URL + "/ws/accountservice";

		JaxWsProxyFactoryBean proxyFactory = new JaxWsProxyFactoryBean();
		proxyFactory.setAddress(address);
		proxyFactory.setServiceClass(AccountWebService.class);
		AccountWebService accountWebServiceCreated = (AccountWebService) proxyFactory.create();

		//(可选)重新设定endpoint address.
		((BindingProvider) accountWebServiceCreated).getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
				address);

		SearchUserResult result = accountWebServiceCreated.searchUser(null, null);

		assertTrue(result.getUserList().size() >= 4);
		assertEquals("Jack", result.getUserList().get(0).getName());
	}
}
