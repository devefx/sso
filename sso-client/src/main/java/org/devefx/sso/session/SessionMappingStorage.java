package org.devefx.sso.session;

import javax.servlet.http.HttpSession;

public interface SessionMappingStorage {
	public void addSessionById(String mappingId, HttpSession session);
	
	public void removeBySessionById(String sessionId);
	
	public HttpSession removeSessionByMappingId(String mappingId);
}
