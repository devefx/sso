package org.devefx.sso.authentication;

/**
 * 需要拦截地址
 * @author： youqian.yue
 * @date： 2015-11-5 下午4:35:32
 */
public interface InterceptAddress {
	public boolean intercept(String name, String method);
}
