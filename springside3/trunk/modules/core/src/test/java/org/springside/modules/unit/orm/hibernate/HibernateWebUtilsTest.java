package org.springside.modules.unit.orm.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springside.modules.orm.PropertyFilter;
import org.springside.modules.orm.hibernate.HibernateWebUtils;

public class HibernateWebUtilsTest extends Assert {

	@Test
	public void mergeByCheckedIds() {
		List<TestBean> srcList = new ArrayList<TestBean>();
		srcList.add(new TestBean("A"));
		srcList.add(new TestBean("B"));

		List<String> idList = new ArrayList<String>();
		idList.add("A");
		idList.add("C");

		HibernateWebUtils.cleanByCheckedResult(srcList, idList);

		assertEquals(1, srcList.size());
		assertEquals("A", srcList.get(0).getId());
		assertEquals(1, idList.size());
		assertEquals("C", idList.get(0));
	}

	@Test
	public void buildPropertyFilters() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setParameter("filter_EQ_loginName", "abcd");
		request.setParameter("filter_LIKE_name_OR_email", "efg");

		List<PropertyFilter> filters = HibernateWebUtils.buildPropertyFilters(request);

		assertEquals(2, filters.size());

		PropertyFilter filter1 = filters.get(0);
		assertEquals(PropertyFilter.MatchType.EQ, filter1.getMatchType());
		assertEquals("loginName", filter1.getPropertyName());
		assertEquals("abcd", filter1.getValue());

		PropertyFilter filter2 = filters.get(1);
		assertEquals(PropertyFilter.MatchType.LIKE, filter2.getMatchType());
		assertEquals(true, filter2.isMultiProperty());
		assertEquals("efg", filter2.getValue());
	}

	public static class TestBean {
		private String id;

		public TestBean() {
		}

		public TestBean(String id) {
			this.id = id;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}
	}

}
