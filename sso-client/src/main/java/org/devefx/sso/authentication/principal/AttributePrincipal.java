package org.devefx.sso.authentication.principal;

import java.io.Serializable;
import java.security.Principal;
import java.util.Map;

/**
 * 用户属性信息主体接口
 * @author： youqian.yue
 * @date： 2015-9-17 上午9:34:19
 */
public interface AttributePrincipal extends Principal, Serializable {
	
	public abstract Map<String, Object> getAttributes();
}
