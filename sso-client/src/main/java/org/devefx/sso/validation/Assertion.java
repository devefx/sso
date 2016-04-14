package org.devefx.sso.validation;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.devefx.sso.authentication.principal.AttributePrincipal;

/**
 * 断定接口
 * @author： youqian.yue
 * @date： 2015-9-17 上午9:36:50
 */
public interface Assertion extends Serializable {
	
	public abstract Date getValidFromDate();
	  
	public abstract Date getValidUntilDate();
	
	public abstract Map<String, Object> getAttributes();
	
	public abstract AttributePrincipal getPrincipal();
}
