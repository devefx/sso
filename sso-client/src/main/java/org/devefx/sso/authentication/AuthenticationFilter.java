package org.devefx.sso.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.devefx.sso.util.AbstractFilter;
import org.devefx.sso.util.CommonUtils;
import org.devefx.sso.util.SsoHttpServletRequestWrapper;
import org.devefx.sso.validation.Assertion;

/**
 * 单点登录身份验证过滤器
 * @author： youqian.yue
 * @date： 2015-11-6 下午6:13:56
 */
public class AuthenticationFilter extends AbstractFilter {

	private String serverLoginUrl;
	
	private InterceptAddress interceptAddress;
	private int contextPathLength = -1;
	
	@Override
	protected void initInternal(FilterConfig filterConfig)
			throws ServletException {
		if (!isIgnoreInitConfiguration()) {
			super.initInternal(filterConfig);
			String interceptClass = getPropertyFromInitParams(filterConfig, "interceptClass", null);
			Object temp = null;
			try {
				temp = Class.forName(interceptClass).newInstance();
			} catch (Exception e) {
				throw new RuntimeException("Can not create instance of class: " + interceptClass, e);
			}
			if (temp instanceof InterceptAddress) {
				interceptAddress = (InterceptAddress) temp;
			} else {
				throw new RuntimeException("Can not create instance of class: " + interceptClass + ". Please check the config in web.xml");
			}
			setServerLoginUrl(getPropertyFromInitParams(filterConfig, "serverLoginUrl", null));
		}
	}
	
	public void setServerLoginUrl(String serverLoginUrl) {
		this.serverLoginUrl = serverLoginUrl;
	}
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
	    HttpServletResponse response = (HttpServletResponse)servletResponse;
	    HttpSession session = request.getSession();
	    // 是否和sso服务器保持通讯标记
	    String connStatus = getConnStatus(request);
	    // 判断Session是否已经登陆
	    Assertion assertion = (session != null ? (Assertion)session.getAttribute(CONST_SSO_ASSERTION) : null);
	    if (assertion != null) {
	    	if (!CONNECT_STATUS_LOGGED.equals(connStatus))
	    		response.addCookie(new Cookie(CONNECT_STATUS, CONNECT_STATUS_LOGGED));
	    	filterChain.doFilter(new SsoHttpServletRequestWrapper(request, assertion.getPrincipal()), response);
	    	return;
	    }
	    // 判断是否有Ticket
	    String ticket = CommonUtils.safeGetParameter(request, getArtifactParameterName());
 	    if (CommonUtils.isNotBlank(ticket)) {
	    	// 交给TicketValidationFilter验证Ticket
	    	filterChain.doFilter(request, response);
	    	return;
		}
 	    // 如果集群机器已经登陆
	    if (CONNECT_STATUS_LOGGED.equals(connStatus)) {
	    	String serviceUrl = constructServiceUrl(request, response);
 	    	String urlToRedirectTo = CommonUtils.constructRedirectUrl(serverLoginUrl.substring(0, serverLoginUrl.length() - 5) + "service", getServiceParameterName(), serviceUrl);
 	    	urlToRedirectTo = CommonUtils.constructRedirectUrl(urlToRedirectTo, "jsessionid", request.getSession().getId());
 	    	response.addCookie(new Cookie(CONNECT_STATUS, CONNECT_STATUS_NOLOGIN));
 	    	response.sendRedirect(urlToRedirectTo);
 	    	return;
		}
	    // 判断是否需要验证
	    if (interceptAddress != null) {
		    if (contextPathLength == -1) {
		    	String contextPath = request.getContextPath();
		    	contextPathLength = (contextPath == null || "/".equals(contextPath) ? 0 : contextPath.length());
			}
	    	String target = request.getRequestURI();
	 	    if (contextPathLength != 0)
	 	    	target = target.substring(contextPathLength);
	 	    int index = target.indexOf(";jsessionid");
	 	    if (index != -1)
	 	    	target = target.substring(0, index);
	 	    if (!interceptAddress.intercept(target, request.getMethod())) {
	 	    	if (connStatus != null) {
	 	    		filterChain.doFilter(request, response);
			    	return;
				}
	 	    	// 和sso server建立联系
	 	    	response.addCookie(new Cookie(CONNECT_STATUS, CONNECT_STATUS_NOLOGIN));
	 	    	// 重定向SSO服务中心
	 	    	String serviceUrl = constructServiceUrl(request, response);
	 	    	String urlToRedirectTo = CommonUtils.constructRedirectUrl(serverLoginUrl.substring(0, serverLoginUrl.length() - 5) + "service", getServiceParameterName(), serviceUrl);
	 	    	urlToRedirectTo = CommonUtils.constructRedirectUrl(urlToRedirectTo, "jsessionid", request.getSession().getId());
	 	    	response.sendRedirect(urlToRedirectTo);
	 	    	return;
			}
		}
	    // 重定向登录页面
	    String serviceUrl = constructServiceUrl(request, response);
	    String urlToRedirectTo = CommonUtils.constructRedirectUrl(serverLoginUrl, getServiceParameterName(), serviceUrl);
	    response.sendRedirect(urlToRedirectTo);
	}
	
	public String getConnStatus(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (CONNECT_STATUS.equals(cookie.getName()))
					return cookie.getValue();
			}
		}
		return null;
	}
}