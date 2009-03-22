package org.springside.modules.security.jcaptcha;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.octo.captcha.service.CaptchaService;
import com.octo.captcha.service.CaptchaServiceException;

/**
 * 集成JCaptcha的Filter.
 * 
 * 可通过配置与SpringSecurity相同的登录表单处理URL与身份验证失败URL,实现与SpringSecurity的集成.
 * 
 * 在web.xml中配置的参数包括：
 * 1.failureUrl--身份验证失败后跳转的URL,与SpringSecurity中的配置保持一致,无默认值必须配置.
 * 2.filterProcessesUrl--登录表单处理URL,与SpringSecurity中的配置一致,默认为/j_spring_security_check.
 * 3.captchaServiceId--captchaService在Spring ApplicationContext中的bean id,默认为captchaService.
 * 4.captchaParamterName--登录表单中验证码Input框的名称,默认为j_captcha.
 * 
 * 具体应用参考showcase示例的web.xml与login.jsp.
 * 
 * @author calvin
 */
public class JCaptchaFilter implements Filter {

	public static final String DEFAULT_FILTER_PROCESSES_URL = "/j_spring_security_check";
	public static final String DEFAULT_CAPTCHA_SERVICE_ID = "captchaService";
	public static final String DEFAULT_CAPTCHA_PARAMTER_NAME = "j_captcha";

	private static Logger logger = LoggerFactory.getLogger(JCaptchaFilter.class);

	private String failureUrl;
	private String filterProcessesUrl = DEFAULT_FILTER_PROCESSES_URL;;
	private String captchaServiceId = DEFAULT_CAPTCHA_SERVICE_ID;
	private String captchaParamterName = DEFAULT_CAPTCHA_PARAMTER_NAME;

	private CaptchaService captchaService = null;

	public void init(FilterConfig fConfig) throws ServletException {
		initParameters(fConfig);
		initCaptchaService(fConfig);
	}

	/**
	 * 初始化web.xml中定义的filter init-param.
	 */
	protected void initParameters(FilterConfig fConfig) {
		if (StringUtils.isBlank(fConfig.getInitParameter("failureUrl")))
			throw new IllegalArgumentException("CaptchaFilter缺少failureUrl参数");
		failureUrl = fConfig.getInitParameter("failureUrl");

		if (StringUtils.isNotBlank(fConfig.getInitParameter("filterProcessesUrl"))) {
			filterProcessesUrl = fConfig.getInitParameter("filterProcessesUrl");
		}

		if (StringUtils.isNotBlank(fConfig.getInitParameter("captchaServiceId"))) {
			captchaServiceId = fConfig.getInitParameter("captchaServiceId");
		}

		if (StringUtils.isNotBlank(fConfig.getInitParameter("captchaParamterName"))) {
			captchaParamterName = fConfig.getInitParameter("captchaParamterName");
		}
	}

	/**
	 * 从ApplicatonContext获取CaptchaService实例.
	 */
	private void initCaptchaService(FilterConfig fConfig) {
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(fConfig.getServletContext());
		captchaService = (CaptchaService) context.getBean(captchaServiceId);
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest theRequest, ServletResponse theResponse, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest request = (HttpServletRequest) theRequest;
		HttpServletResponse response = (HttpServletResponse) theResponse;
		String servletPath = request.getServletPath();

		//符合filterProcessesUrl为验证处理请求,其余为生成验证图片请求.
		if (StringUtils.startsWith(servletPath, filterProcessesUrl)) {
			boolean validated = validateCaptchaChallenge(request, response);
			if (validated) {
				chain.doFilter(request, response);
			} else {
				redirectFailureUrl(request, response);
			}
		} else {
			genernateCaptchaImage(request, response);
		}
	}

	/**
	 * 生成验证码图片.
	 */
	private void genernateCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws IOException {

		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		ServletOutputStream out = response.getOutputStream();
		try {
			String captchaId = request.getSession(true).getId();
			BufferedImage challenge = (BufferedImage) captchaService.getChallengeForID(captchaId, request.getLocale());
			ImageIO.write(challenge, "jpg", out);
			out.flush();
		} catch (CaptchaServiceException e) {
			logger.error(e.getMessage(), e);
		} finally {
			out.close();
		}
	}

	/**
	 * 验证验证码. 
	 */
	private boolean validateCaptchaChallenge(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		if (request.getSession(false) == null)
			return false;

		try {
			String captchaID = request.getSession().getId();
			String challengeResponse = request.getParameter(captchaParamterName);
			return captchaService.validateResponseForID(captchaID, challengeResponse);
		} catch (CaptchaServiceException e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

	protected void redirectFailureUrl(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.sendRedirect(request.getContextPath() + failureUrl);
	}
}