package org.springside.examples.showcase.unit.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.sql.DataSource;

import org.hibernate.Hibernate;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springside.examples.showcase.common.dao.UserHibernateDao;
import org.springside.examples.showcase.common.entity.User;
import org.springside.modules.test.spring.SpringTxTestCase;
import org.springside.modules.test.utils.DbUnitUtils;

import com.google.common.collect.Lists;

/**
 * UserDao的集成测试用例,测试ORM映射及特殊的DAO操作.
 * 
 * @author calvin
 */
@ContextConfiguration(locations = { "/applicationContext-test.xml" })
//演示指定非默认名称的TransactionManager.
@TransactionConfiguration(transactionManager = "defaultTransactionManager")
public class UserHibernateDaoTest extends SpringTxTestCase {

	private static DataSource dataSourceHolder = null;

	@Autowired
	private UserHibernateDao userDao;

	@Before
	public void loadDefaultData() throws Exception {
		if (dataSourceHolder == null) {
			DbUnitUtils.loadData(dataSource, "/data/default-data.xml");
			dataSourceHolder = dataSource;
		}
	}

	@AfterClass
	public static void cleanDefaultData() throws Exception {
		DbUnitUtils.removeData(dataSourceHolder, "/data/default-data.xml");
	}

	@Test
	public void eagerFetchCollection() {
		int userCount = countRowsInTable("SS_USER");
		//init by hql
		List<User> userList1 = userDao.getAllUserWithRoleByDistinctHql();
		assertEquals(userCount, userList1.size());
		assertTrue(Hibernate.isInitialized(userList1.get(0).getRoleList()));

		//init by criteria
		List<User> userList2 = userDao.getAllUserWithRolesByDistinctCriteria();
		assertEquals(userCount, userList2.size());
		assertTrue(Hibernate.isInitialized(userList2.get(0).getRoleList()));
	}

	@Test
	public void batchDisableUser() {
		List<String> ids = Lists.newArrayList("1", "2");

		userDao.disableUsers(ids);

		assertEquals("disabled", userDao.get("1").getStatus());
		assertEquals("disabled", userDao.get("2").getStatus());
	}
}
