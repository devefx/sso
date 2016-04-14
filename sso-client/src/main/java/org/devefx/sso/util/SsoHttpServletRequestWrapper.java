package org.devefx.sso.util;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.devefx.sso.authentication.principal.AttributePrincipal;

public class SsoHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private final AttributePrincipal principal;
	
	public SsoHttpServletRequestWrapper(HttpServletRequest request, AttributePrincipal principal) {
		super(request);
		this.principal = principal;
	}
	
	@Override
	public Principal getUserPrincipal() {
		return principal;
	}
	
	@Override
	public String getRemoteUser() {
		if (principal != null) {
			return principal.getName();
		}
		return null;
	}
}
