package org.springside.examples.miniservice.rs.client;

import java.net.URI;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Required;
import org.springside.examples.miniservice.rs.dto.UserDTO;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * 使用Jersey Client的User REST客户端.
 * 
 * @author calvin
 */
public class UserResourceClient {

	private WebResource client;

	@Required
	public void setBaseUrl(String baseUrl) {
		Client jerseyClient = Client.create(new DefaultClientConfig());
		client = jerseyClient.resource(baseUrl);
	}

	public URI createUser(UserDTO user) {
		ClientResponse response = client.path("/users").entity(user, MediaType.APPLICATION_JSON)
				.post(ClientResponse.class);
		if (201 == response.getStatus()) {
			return response.getLocation();
		} else {
			throw new UniformInterfaceException(response);
		}
	}
}
