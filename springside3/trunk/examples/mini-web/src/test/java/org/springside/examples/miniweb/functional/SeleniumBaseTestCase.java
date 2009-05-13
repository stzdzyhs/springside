package org.springside.examples.miniweb.functional;

import com.thoughtworks.selenium.SeleneseTestCase;

public abstract class SeleniumBaseTestCase extends SeleneseTestCase {

	@Override
	public void setUp() throws Exception {
		super.setUp("http://localhost:8080", "*chrome");
		super.setCaptureScreenShotOnFailure(true);
		login();
	}
	
	public void login() {
		selenium.open("/mini-web/login.action");
		selenium.type("j_username", "admin");
		selenium.type("j_password", "admin");
		selenium.click("//input[@value='登录']");
		selenium.waitForPageToLoad("30000");
		assertTrue(selenium.isTextPresent("你好,admin."));
	}


}