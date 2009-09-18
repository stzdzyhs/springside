package org.springside.examples.miniservice.ws.api.dto;

import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springside.examples.miniservice.ws.api.WsConstants;

/**
 * Web Service传输Role信息的DTO.
 * 
 * 注释见{@link UserDTO}.
 *
 * @author calvin
 */
@XmlType(name = "Role", namespace = WsConstants.NS)
public class RoleDTO {

	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
