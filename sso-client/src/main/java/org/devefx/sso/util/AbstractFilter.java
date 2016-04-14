package org.devefx.sso.util;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 抽象过滤器
 * @author： youqian.yue
 * @date： 2015-9-17 上午9:36:10
 */
public abstract class AbstractFilter extends AbstractConfigurationFilter {
	public static final String CONNECT_STATUS = "conn_status";
	public static final String CONNECT_STATUS_NOLOGIN = "1";
	public static final String CONNECT_STATUS_LOGGED = "2";
	
	public static final String CONST_SSO_ASSERTION = "_const_sso_assertion_";
	private String artifactParameterName = "ticket";
	private String serviceParameterName = "service";
	private boolean encodeServiceUrl = true;
	private String serverName;
	private String service;
	
	public void init(FilterConfig filterConfig) throws ServletException {
		if (!isIgnoreInitConfiguration()) {
			setServerName(getPropertyFromInitParams(filterConfig, "serverName", null));
			 
			
			initInternal(filterConfig);
		}
	}

	public void destroy() {
		
	}
	
	public final void setServerName(String serverName) {
		if ((serverName != null) && (serverName.endsWith("/"))) {
			this.serverName = serverName.substring(0, serverName.length() - 1);
	    } else {
	    	this.serverName = serverName;
	    }
	}
	
	protected final String constructServiceUrl(HttpServletRequest request, HttpServletResponse response) {
		return CommonUtils.constructServiceUrl(request, response, this.service, this.serverName, this.artifactParameterName, this.encodeServiceUrl);
	}

	public String getArtifactParameterName() {
		return artifactParameterName;
	}

	public void setArtifactParameterName(String artifactParameterName) {
		this.artifactParameterName = artifactParameterName;
	}

	public String getServiceParameterName() {
		return serviceParameterName;
	}

	public void setServiceParameterName(String serviceParameterName) {
		this.serviceParameterName = serviceParameterName;
	}
	
	public final void setEncodeServiceUrl(boolean encodeServiceUrl) {
		this.encodeServiceUrl = encodeServiceUrl;
	}
	
	protected void initInternal(FilterConfig filterConfig) throws ServletException {
		
	}
}
