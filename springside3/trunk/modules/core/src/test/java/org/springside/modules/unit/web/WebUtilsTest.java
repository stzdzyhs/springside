package org.springside.modules.unit.web;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springside.modules.web.WebUtils;

public class WebUtilsTest extends Assert {

	@Test
	public void checkIfModified() {
		//未设Header,返回true
		MockHttpServletRequest request = new MockHttpServletRequest();
		assertEquals(true, WebUtils.checkIfModified(request, (new Date().getTime() - 2000)));

		//设置If-Modified-Since Header
		request.addHeader("If-Modified-Since", new Date().getTime());
		//文件修改时间比Header时间小,文件未修改, 返回false
		assertEquals(false, WebUtils.checkIfModified(request, (new Date().getTime() - 2000)));
		//文件修改时间比Header时间大,文件已修改, 返回false
		assertEquals(true, WebUtils.checkIfModified(request, (new Date().getTime() + 2000)));
	}

	@Test
	public void checkAccetptGzip() {
		MockHttpServletRequest request = new MockHttpServletRequest();

		request.addHeader("Accept-Encoding", "gzip,deflate");
		assertTrue(WebUtils.checkAccetptGzip(request));

		request = new MockHttpServletRequest();
		request.addHeader("Accept-Encoding", "deflate");
		assertFalse(WebUtils.checkAccetptGzip(request));

		request = new MockHttpServletRequest();
		assertFalse(WebUtils.checkAccetptGzip(request));
	}

	@Test
	public void setHeader() {
		MockHttpServletResponse response = new MockHttpServletResponse();
		WebUtils.setNotModified(response);
		assertEquals(304, response.getStatus());
	}
}