package org.devefx.sso.session;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.devefx.sso.util.AbstractConfigurationFilter;


public class SingleSignOutFilter extends AbstractConfigurationFilter {

	private static final SingleSignOutHandler handler = new SingleSignOutHandler();
	
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;

		if (handler.isTokenRequest(request)) {
			handler.recordSession(request);
		} else {
			if (handler.isLogoutRequest(request)) {
				handler.destroySession(request);
				return;
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	public void destroy() {
	}
	
	protected static SingleSignOutHandler getSingleSignOutHandler() {
		return handler;
	}
}
