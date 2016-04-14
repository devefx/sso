package org.devefx.sso.authentication.principal;

import java.io.Serializable;
import java.security.Principal;

/**
 * 简单用户信息主体
* @author： youqian.yue
* @date： 2015-9-17 上午9:35:07
 */
public class SimplePrincipal implements Principal, Serializable {
	
	private static final long serialVersionUID = -6297034670760928398L;
	private final String name;
	
	public SimplePrincipal(String name) {
		this.name = name;
	}
	
	public final String getName() {
		return this.name;
	}

	public String toString() {
		return getName();
	}

	public int hashCode() {
		return 37 * getName().hashCode();
	}

	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof SimplePrincipal)) {
			return false;
		}
		return getName().equals(((SimplePrincipal) obj).getName());
	}
}
