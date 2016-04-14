package org.devefx.sso.validation;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.devefx.sso.util.AbstractFilter;
import org.devefx.sso.util.CommonUtils;

/**
 * ticket验证过滤器
 * @author： youqian.yue
 * @date： 2015-9-17 上午9:37:48
 */
public class TicketValidationFilter extends AbstractFilter {
	
	private TicketValidator ticketValidator;
	
	public void setTicketValidator(TicketValidator ticketValidator) {
		this.ticketValidator = ticketValidator;
	}
	
	@Override
	protected void initInternal(FilterConfig filterConfig) throws ServletException {
		
		// 设置Ticket验证器
		setTicketValidator(getTicketValidator(filterConfig));
		super.initInternal(filterConfig);
	}
	
	protected TicketValidator getTicketValidator(FilterConfig filterConfig) {
		// 读取配置
		String serverUrlPrefix = getPropertyFromInitParams(filterConfig, "serverUrlPrefix", null);
		String encoding = getPropertyFromInitParams(filterConfig, "encoding", null);
		
		// 创建Ticket验证器
		ServiceTicketValidator validator = new ServiceTicketValidator(serverUrlPrefix);
		validator.setEncoding(encoding);
		
		return validator;
	}
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		String ticket = CommonUtils.safeGetParameter(request, getArtifactParameterName());
		if (CommonUtils.isNotBlank(ticket)) {
			try {
				String serviceUrl = constructServiceUrl(request, response);
				Assertion assertion = this.ticketValidator.validate(ticket, serviceUrl);
				request.setAttribute(CONST_SSO_ASSERTION, assertion);
				request.getSession().setAttribute(CONST_SSO_ASSERTION, assertion);
				response.addCookie(new Cookie(CONNECT_STATUS, CONNECT_STATUS_LOGGED));
				response.sendRedirect(serviceUrl);
				return;
			} catch (TicketValidationException e) {
				response.setStatus(403);
				throw new ServletException(e);
			}
		}
		filterChain.doFilter(request, response);
	}
}
