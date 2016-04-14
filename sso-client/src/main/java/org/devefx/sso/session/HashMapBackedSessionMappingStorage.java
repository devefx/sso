package org.devefx.sso.session;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

public class HashMapBackedSessionMappingStorage implements SessionMappingStorage {

	private final Map<String, HttpSession> MANAGED_SESSIONS = new HashMap<String, HttpSession>();
	private final Map<String, String> ID_TO_SESSION_KEY_MAPPING = new HashMap<String, String>();
	
	public synchronized void addSessionById(String mappingId, HttpSession session) {
		this.ID_TO_SESSION_KEY_MAPPING.put(session.getId(), mappingId);
	    this.MANAGED_SESSIONS.put(mappingId, session);
	}
	
	public synchronized void removeBySessionById(String sessionId) {
		String key = this.ID_TO_SESSION_KEY_MAPPING.get(sessionId);
		this.MANAGED_SESSIONS.remove(key);
		this.ID_TO_SESSION_KEY_MAPPING.remove(sessionId);
	}
	
	public synchronized HttpSession removeSessionByMappingId(String mappingId) {
		HttpSession session = this.MANAGED_SESSIONS.get(mappingId);
		if (session != null) {
			removeBySessionById(session.getId());
		}
		return session;
	}
}
