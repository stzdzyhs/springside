package org.springside.examples.miniservice.ws.api;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.springside.examples.miniservice.ws.api.dto.UserDTO;
import org.springside.examples.miniservice.ws.api.result.CreateUserResult;
import org.springside.examples.miniservice.ws.api.result.GetAllUserResult;
import org.springside.examples.miniservice.ws.api.result.WSResult;

/**
 * JAX-WS2.0的WebService接口定义类.
 * 
 * 使用JAX-WS2.0 annotation设置WSDL中的定义.
 * 使用WSResult及其子类类包裹返回结果.
 * 使用DTO传输对象隔绝系统内部领域对象的修改对外系统的影响.
 * 
 * @author sky
 * @author calvin
 */
@WebService(name = "UserService", targetNamespace = WsConstants.NS)
public interface UserWebService {
	/**
	 * 显示所有用户.
	 */
	public GetAllUserResult getAllUser();

	/**
	 * 新建用户.
	 */
	public CreateUserResult createUser(@WebParam(name = "user") UserDTO user);

	/**
	 * 验证用户名密码.
	 */
	public WSResult authUser(@WebParam(name = "loginName") String loginName,
			@WebParam(name = "password") String password);
}
