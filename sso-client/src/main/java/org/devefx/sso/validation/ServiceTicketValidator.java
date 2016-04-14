package org.devefx.sso.validation;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.devefx.sso.authentication.principal.AttributePrincipalImpl;
import org.devefx.sso.util.CommonUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * ticket 验证器
 * @author： youqian.yue
 * @date： 2015-9-17 上午9:37:27
 */
public class ServiceTicketValidator implements TicketValidator {

	private final static String URL_SUFFIX = "serviceValidate";
	private final String serverUrlPrefix;
	private String encoding;
	
	public ServiceTicketValidator(String serverUrlPrefix) {
		this.serverUrlPrefix = serverUrlPrefix;
	}
	
	private final String constructValidationUrl(String ticket, String serviceUrl) {
		Map<String, String> urlParameters = new HashMap<String, String>();
		urlParameters.put("ticket", ticket);
	    urlParameters.put("service", encodeUrl(serviceUrl));
		
	    StringBuffer buffer = new StringBuffer();
	    buffer.append(this.serverUrlPrefix);
	    if (!this.serverUrlPrefix.endsWith("/")) {
	    	buffer.append("/");
		}
	    buffer.append(URL_SUFFIX);
	    int i = 0;
	    for (Map.Entry<String, String> entry : urlParameters.entrySet()) {
	    	String key = (String)entry.getKey();
	    	String value = (String)entry.getValue();
	    	if (value != null) {
	    		buffer.append(i++ == 0 ? "?" : "&");
	    		buffer.append(key);
	            buffer.append("=");
	            buffer.append(value);
	    	}
		}
		return buffer.toString();
	}
	
	protected final String encodeUrl(String url) {
		if (url == null) {
			return null;
		}
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (Exception e) {
		}
		return url;
	}

	private final String retrieveResponseFromServer(URL validationUrl, String ticket) {
		return CommonUtils.getResponseFromServer(validationUrl, encoding);
	}
	
	private Assertion parseResponseFromServer(String response) throws TicketValidationException {
		
		JSONObject jsonObject = JSON.parseObject(response);
		
		String principal = jsonObject.getString("id");
		
		if (CommonUtils.isEmpty(principal)) {
			throw new TicketValidationException("No principal was found in the response from the SSO server.");
		}
		Map<String, Object> attributes = new HashMap<String, Object>();
		JSONObject attributesJson = jsonObject.getJSONObject("attributes");
		for (Map.Entry<String, Object> entry : attributesJson.entrySet()) {
			attributes.put(entry.getKey(), entry.getValue());
		}
		Assertion assertion = new AssertionImpl(new AttributePrincipalImpl(principal, attributes));
		
		return assertion;
	}
	
	public Assertion validate(String ticket, String service) throws TicketValidationException {
		
		String validationUrl = constructValidationUrl(ticket, service);
		
		try {
			String serverResponse = retrieveResponseFromServer(new URL(validationUrl), ticket);
			if (serverResponse == null) {
				throw new TicketValidationException("The SSO server returned no response.");
			}
			return parseResponseFromServer(serverResponse);
		} catch (MalformedURLException e) {
			throw new TicketValidationException(e);
		}
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
}
