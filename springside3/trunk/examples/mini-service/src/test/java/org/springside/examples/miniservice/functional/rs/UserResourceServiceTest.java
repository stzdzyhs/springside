package org.springside.examples.miniservice.functional.rs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.dozer.DozerBeanMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springside.examples.miniservice.data.UserData;
import org.springside.examples.miniservice.entity.user.User;
import org.springside.examples.miniservice.rs.dto.UserDTO;

public class UserResourceServiceTest extends Assert {

	private WebClient client;

	@Before
	public void setUp() {
		client = WebClient.create("http://localhost:8080/mini-service/services/rs");
	}

	@Test
	public void getUser() {
		UserDTO user = client.path("/users/1").accept("application/json").get(UserDTO.class);
		assertEquals("admin", user.getLoginName());
	}

	@Test
	public void getUserWithInvalidId() {
		try {
			client.path("/users/999").accept("application/json").get(UserDTO.class);
			fail("Should thrown exception while invalid id");
		} catch (WebApplicationException e) {
			assertEquals(HttpServletResponse.SC_NOT_FOUND, e.getResponse().getStatus());
		}
	}

	@Test
	public void getAllUser() {
		Collection<? extends UserDTO> userList = client.path("/users").accept("application/xml").getCollection(
				UserDTO.class);

		assertTrue(userList.size() >= 6);
		UserDTO admin = userList.iterator().next();
		assertEquals("admin", admin.getLoginName());
	}

	@Test
	public void createUser() throws IOException {
		User user = UserData.getRandomUser();
		UserDTO dto = new DozerBeanMapper().map(user, UserDTO.class);
		Response response = client.path("/users").accept("application/json").type("application/json").post(dto);

		assertEquals(HttpServletResponse.SC_OK, response.getStatus());
		InputStream is = (InputStream) response.getEntity();
		System.out.println("Created user id:" + IOUtils.toString(is));
	}

}