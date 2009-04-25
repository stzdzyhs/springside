#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.entity.security;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import ${package}.entity.IdEntity;
import org.springside.modules.utils.ReflectionUtils;

/**
 * 角色.
 * 
 * 使用JPA annotation定义ORM关系.
 * 使用Hibernate annotation定义二级缓存. 
 * 
 * @author calvin
 */
@Entity
@Table(name = "ROLES")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends IdEntity {

	private String name;

	private Set<Authority> auths = new LinkedHashSet<Authority>(); //有序的关联对象集合
	private Set<User> users = new LinkedHashSet<User>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(joinColumns = { @JoinColumn(name = "ROLE_ID") }, inverseJoinColumns = { @JoinColumn(name = "AUTHORITY_ID") })
	@Fetch(FetchMode.SUBSELECT)
	@OrderBy("id")
	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public Set<Authority> getAuthorities() {
		return auths;
	}

	public void setAuthorities(Set<Authority> auths) {
		this.auths = auths;
	}
	
	@ManyToMany(mappedBy = "roles")
	public Set<User> getUsers() {
		return users;
	}
	
	public void setUsers(Set<User> users) {
		this.users = users;
	}

	@Transient
	public String getAuthNames() throws Exception {
		return ReflectionUtils.fetchElementPropertyToString(auths, "displayName", ", ");
	}

	@Transient
	@SuppressWarnings("unchecked")
	public List<Long> getAuthIds() throws Exception {
		return ReflectionUtils.fetchElementPropertyToList(auths, "id");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
