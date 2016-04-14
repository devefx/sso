package org.devefx.sso.validation;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.devefx.sso.authentication.principal.AttributePrincipal;
import org.devefx.sso.authentication.principal.AttributePrincipalImpl;

/**
 * 断定
 * @author： youqian.yue
 * @date： 2015-9-17 上午9:37:03
 */
public class AssertionImpl implements Assertion {
	
	private static final long serialVersionUID = -7966975111191678719L;
	private final Date validFromDate;
	private final Date validUntilDate;
	private final Map<String, Object> attributes;
	private final AttributePrincipal principal;
	
	public AssertionImpl(String name) {
		this(new AttributePrincipalImpl(name));
	}
	
	public AssertionImpl(AttributePrincipal principal) {
		this(principal, new HashMap<String, Object>());
	}
	
	public AssertionImpl(AttributePrincipal principal, Map<String, Object> attributes) {
		this(principal, new Date(), null, attributes);
	}
	
	public AssertionImpl(AttributePrincipal principal, Date validFromDate, Date validUntilDate, Map<String, Object> attributes) {
		this.principal = principal;
		this.validFromDate = validFromDate;
		this.validUntilDate = validUntilDate;
		this.attributes = attributes;
	}
	
	public Date getValidFromDate() {
		return this.validFromDate;
	}

	public Date getValidUntilDate() {
		return this.validUntilDate;
	}

	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	public AttributePrincipal getPrincipal() {
		return this.principal;
	}

}
