package org.devefx.sso.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.devefx.sso.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleSignOutHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(SingleSignOutHandler.class);
	private SessionMappingStorage sessionMappingStorage = new HashMapBackedSessionMappingStorage();
	private String artifactParameterName = "ticket";
	private String logoutParameterName = "logoutRequest";
	
	public SessionMappingStorage getSessionMappingStorage() {
		return this.sessionMappingStorage;
	}
	  
	public boolean isTokenRequest(HttpServletRequest request) {
		return CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request,
				this.artifactParameterName));
	}

	public boolean isLogoutRequest(HttpServletRequest request) {
		return ("POST".equals(request.getMethod())) && (!isMultipartRequest(request))
				&& (CommonUtils.isNotBlank(CommonUtils.safeGetParameter(request, this.logoutParameterName)));
	}

	private boolean isMultipartRequest(HttpServletRequest request) {
		return (request.getContentType() != null) && (request.getContentType().toLowerCase().startsWith("multipart"));
	}
	
	public void recordSession(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String token = CommonUtils.safeGetParameter(request, this.artifactParameterName);
		try {
			this.sessionMappingStorage.removeBySessionById(session.getId());
		}
	    catch (Exception e) {}
		this.sessionMappingStorage.addSessionById(token, session);
	}
	
	public void destroySession(HttpServletRequest request) {
		String token = CommonUtils.safeGetParameter(request, this.logoutParameterName);
		if (CommonUtils.isNotBlank(token)) {
			HttpSession session = sessionMappingStorage.removeSessionByMappingId(token);
			if (session != null) {
				try {
					session.invalidate();
				} catch (IllegalStateException e) {
					LOGGER.error(e.getMessage());
				}
			}
		}
	}
}
