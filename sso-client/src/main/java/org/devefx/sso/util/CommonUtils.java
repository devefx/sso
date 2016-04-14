package org.devefx.sso.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonUtils {
	
	public static boolean isNotEmpty(String string) {
		return !isEmpty(string);
	}
	
	public static boolean isEmpty(String string) {
		return (string == null) || (string.length() == 0);
	}
	
	public static boolean isBlank(String string) {
	    return (isEmpty(string)) || (string.trim().length() == 0);
	}
	
	public static boolean isNotBlank(String string) {
		return !isBlank(string);
	}
	
	public static String constructRedirectUrl(String serverUrl, String serviceParameterName, String serviceParameter) {
		try {
			return serverUrl + (serverUrl.indexOf("?") != -1 ? "&" : "?") + serviceParameterName + "=" + URLEncoder.encode(serviceParameter, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String urlEncode(String service, HttpServletResponse response) {
		String url = response.encodeURL(service);
		int i = url.lastIndexOf(";jsessionid");
		return i != -1 ? url.substring(0, i) : url;
	}
	
	public static String constructServiceUrl(HttpServletRequest request, HttpServletResponse response, String service, String serverName, String artifactParameterName, boolean encode) {
		if (isNotBlank(service)) {
	    	return encode ? urlEncode(service, response) : service;
	    }
	    StringBuilder buffer = new StringBuilder();
	    if ((!serverName.startsWith("https://")) && (!serverName.startsWith("http://"))) {
	    	buffer.append(request.isSecure() ? "https://" : "http://");
	    }
	    buffer.append(serverName);
	    buffer.append(request.getRequestURI());
	    if (isNotBlank(request.getQueryString())) {
	    	int location = request.getQueryString().indexOf(artifactParameterName + "=");
	    	if (location == 0) {
	    		String returnValue = encode ? urlEncode(buffer.toString(), response) : buffer.toString();
	    		return returnValue;
	    	}
	    	buffer.append("?");
	    	if (location == -1) {
	    		buffer.append(request.getQueryString());
	    	} else if (location > 0) {
	    		int actualLocation = request.getQueryString().indexOf("&" + artifactParameterName + "=");
	    		if (actualLocation == -1) {
	    			buffer.append(request.getQueryString());
	    		} else if (actualLocation > 0) {
	    			buffer.append(request.getQueryString().substring(0, actualLocation));
	    		}
	    	}
	    }
	    String returnValue = encode ? urlEncode(buffer.toString(), response) : buffer.toString();
	    return returnValue;
	}
	
	public static String safeGetParameter(HttpServletRequest request, String parameter) {
		if (("POST".equals(request.getMethod())) && ("logoutRequest".equals(parameter))) {
			return request.getParameter(parameter);
		}
		return (request.getQueryString() == null) || (request.getQueryString().indexOf(parameter) == -1) ? null : request.getParameter(parameter);
	}
	
	public static String getResponseFromServer(URL constructedUrl, String encoding) {
		return getResponseFromServer(constructedUrl, HttpsURLConnection.getDefaultHostnameVerifier(), encoding);
	}
	
	public static String getResponseFromServer(String url, String encoding) {
		try {
			return getResponseFromServer(new URL(url), encoding);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getResponseFromServer(URL constructedUrl, HostnameVerifier hostnameVerifier, String encoding) {
		URLConnection conn = null;
		try {
			conn = constructedUrl.openConnection();
			if ((conn instanceof HttpsURLConnection)) {
				((HttpsURLConnection)conn).setHostnameVerifier(hostnameVerifier);
			}
			BufferedReader in;
			if (isEmpty(encoding)) {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
			}
			StringBuilder stringBuffer = new StringBuilder(255);
			String line;
			while ((line = in.readLine()) != null) {
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}
			return stringBuffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ((conn != null) && ((conn instanceof HttpURLConnection))) {
				((HttpURLConnection)conn).disconnect();
			}
		}
		return null;
	}

}
