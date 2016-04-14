package org.devefx.sso.util;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;

/**
 * 配置过滤器
 * @author： youqian.yue
 * @date： 2015-9-17 上午9:35:36
 */
public abstract class AbstractConfigurationFilter implements Filter {
	
	private boolean ignoreInitConfiguration = false;

	protected final String getPropertyFromInitParams(FilterConfig filterConfig, String propertyName, String defaultValue) {
		String value = filterConfig.getInitParameter(propertyName);
		if (CommonUtils.isNotBlank(value)) {
			return value;
		}
		value = filterConfig.getServletContext().getInitParameter(propertyName);
		if (CommonUtils.isNotBlank(value)) {
			return value;
		}
		return defaultValue;
	}
	
	public boolean isIgnoreInitConfiguration() {
		return ignoreInitConfiguration;
	}

	public void setIgnoreInitConfiguration(boolean ignoreInitConfiguration) {
		this.ignoreInitConfiguration = ignoreInitConfiguration;
	}
}
