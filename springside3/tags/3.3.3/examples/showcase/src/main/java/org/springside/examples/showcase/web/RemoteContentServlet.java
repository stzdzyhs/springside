package org.springside.examples.showcase.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取远程静态内容并进行展示的Servlet.
 * 
 * 演示使用多线程安全的Apache HttpClient获取远程静态内容.
 * 
 * 演示访问地址如下(contentUrl已经过URL编码):
 * remote-content?contentUrl=http%3A%2F%2Flocalhost%3A8080%2Fshowcase%2Fimg%2Flogo.jpg
 * 
 * @author calvin
 */
public class RemoteContentServlet extends HttpServlet {

	private static final long serialVersionUID = -8483811141908827663L;

	private static Logger logger = LoggerFactory.getLogger(RemoteContentServlet.class);

	private HttpClient httpClient = null;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//获取内容
		String contentUrl = request.getParameter("contentUrl");
		HttpEntity entity = fetchContent(contentUrl);

		if (entity == null) {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		//设置Header
		response.setContentType(entity.getContentType().getValue());
		if (entity.getContentLength() > 0) {
			response.setContentLength((int) entity.getContentLength());
		}

		//输出内容
		InputStream input = entity.getContent();
		OutputStream output = response.getOutputStream();

		try {
			//基于byte数组读取InputStream并直接写入OutputStream, 数组默认大小为4k.
			IOUtils.copy(input, output);
			output.flush();
		} finally {
			//保证Input/Output Stream的关闭.
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}

	/**
	 * 使用HttpClient取得内容.
	 */
	private HttpEntity fetchContent(String targetUrl) {
		HttpGet httpGet = new HttpGet(targetUrl);
		HttpContext context = new BasicHttpContext();
		try {
			HttpResponse remoteResponse = httpClient.execute(httpGet, context);
			return remoteResponse.getEntity();
		} catch (Exception e) {
			logger.error("fetch remote content" + targetUrl + "  error", e);
			httpGet.abort();
			return null;
		}
	}

	/**
	 * 创建多线程安全的HttpClient实例.
	 */
	@Override
	public void init() throws ServletException {
		// Create and initialize HTTP parameters
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 50);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		// Create and initialize scheme registry 
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
		// Create an HttpClient with the ThreadSafeClientConnManager.
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
		httpClient = new DefaultHttpClient(cm, params);
	}

	/**
	 * 销毁HttpClient实例.
	 */
	@Override
	public void destroy() {
		if (httpClient != null) {
			httpClient.getConnectionManager().shutdown();
		}
	}
}