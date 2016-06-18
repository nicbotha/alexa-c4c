package com.sap.alexa.c4c;

import java.util.HashMap;
import java.util.HashSet;

public class HttpWrapper {
	enum RequestMethod {
		GET, POST, DELETE, HEAD, PUT
	}
	
	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String CSRF_TOKEN_HEADER = "X-CSRF-Token";
	public static final String CSRF_TOKEN_FETCH = "Fetch";
	public static final String APPLICATION_XML = "application/xml";
	public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	public static final String SERVICE_URL ="https://tenant.crm.ondemand.com/sap/byd/odata/v1/servicerequest";
	
	private final RequestMethod requestMethod;
	private final HashMap<String, String> httpHeaders;
	private final String serviceURL;
		
	public HttpWrapper(RequestMethod requestMethod, HashMap<String, String> httpHeaders, String serviceURL) {
		this.requestMethod = requestMethod;
		this.httpHeaders = httpHeaders;
		this.serviceURL = serviceURL;
	}
	
		
	
}
