package org.devefx.sso.authentication.principal;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户属性信息主体
* @author： youqian.yue
* @date： 2015-9-17 上午9:34:45
 */
public class AttributePrincipalImpl extends SimplePrincipal implements AttributePrincipal {
	
	private static final long serialVersionUID = 2407703815938861465L;
	private final Map<String, Object> attributes;
	
	public AttributePrincipalImpl(String name) {
		this(name, new HashMap<String, Object>());
	}
	
	public AttributePrincipalImpl(String name, Map<String, Object> attributes) {
		super(name);
		this.attributes = attributes;
	}
	
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}
}
