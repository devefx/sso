package org.devefx.sso.session;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SingleSignOutHttpSessionListener implements HttpSessionListener {

	private SessionMappingStorage sessionMappingStorage;
	
	public void sessionCreated(HttpSessionEvent event) {
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		if (sessionMappingStorage == null) {
			this.sessionMappingStorage = getSessionMappingStorage();
		}
		HttpSession session = event.getSession();
		this.sessionMappingStorage.removeBySessionById(session.getId());
	}
	
	protected static SessionMappingStorage getSessionMappingStorage() {
		return SingleSignOutFilter.getSingleSignOutHandler().getSessionMappingStorage();
	}
}
