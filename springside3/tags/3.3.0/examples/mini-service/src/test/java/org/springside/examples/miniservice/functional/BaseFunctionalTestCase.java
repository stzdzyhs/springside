package org.springside.examples.miniservice.functional;

import javax.sql.DataSource;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.mortbay.jetty.Server;
import org.springside.examples.miniservice.tools.Start;
import org.springside.modules.test.utils.DBUnitUtils;
import org.springside.modules.test.utils.JettyUtils;
import org.springside.modules.utils.SpringContextHolder;

/**
 * 功能测试基类.
 * 
 * 在整个测试期间启动一次Jetty Server, 并在每个TestCase执行前重新载入默认数据.
 * 
 * @author calvin
 */
@Ignore
public class BaseFunctionalTestCase extends Assert {

	protected static final String BASE_URL = Start.BASE_URL;

	private static Server server;

	private static DataSource dataSource;

	@BeforeClass
	public static void initAll() throws Exception {

		if (server == null) {
			startJetty();
			initDataSource();
		}

		loadDefaultData();
	}

	/**
	 * 启动Jetty服务器.
	 */
	protected static void startJetty() throws Exception {
		server = JettyUtils.buildTestServer(Start.PORT, Start.CONTEXT);
		server.start();
	}

	/**
	 * 取出Jetty Server内的DataSource.
	 */
	protected static void initDataSource() {
		dataSource = SpringContextHolder.getBean("dataSource");
	}

	/**
	 * 载入默认数据.
	 */
	protected static void loadDefaultData() throws Exception {
		DBUnitUtils.loadDbUnitData(dataSource, "/data/default-data.xml");
	}
}
