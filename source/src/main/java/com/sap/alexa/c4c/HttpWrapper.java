package com.sap.alexa.c4c;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpWrapper {
	enum RequestMethod {
		GET, POST, DELETE, HEAD, PUT
	}

	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_XML = "application/xml";
	public static final String APPLICATION_ATOM_XML = "application/atom+xml";
	
	public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HTTP_HEADER_CSRF_TOKEN = "X-CSRF-Token";

	public static final String AUTHORIZATION = "Authorization";
	public static final String CSRF_TOKEN_FETCH = "Fetch";
	public static final String SEPARATOR = "/";
	protected RequestMethod requestMethod = RequestMethod.GET;
	protected HashMap<String, String> httpHeaders = new HashMap<String, String>();
	protected String pathNameURL;
	protected final String serviceURL;

	private HttpClient httpClient = null;

	public HttpWrapper(String serviceURL) {
		this.serviceURL = serviceURL;
	}
	
	public HttpWrapper(String serviceURL, String username, String password) {
		this.serviceURL = serviceURL;
		this.addBasicCredentials(username, password);
	}

	public HttpResponse execute() throws IllegalStateException, IOException {
		final String absoluteUrl = new StringBuilder(serviceURL).append(SEPARATOR).append(pathNameURL).toString();
		if (this.requestMethod.equals(RequestMethod.GET)) {
			return executeGet(absoluteUrl);
		} else if (this.requestMethod.equals(RequestMethod.POST)) {
			return executePost(absoluteUrl);
		} else {
			return null;
		}
	}

	private HttpResponse executeGet(String URL) throws IllegalStateException, IOException {
		final HttpGet get = new HttpGet(URL);
		
		if(httpHeaders != null && !httpHeaders.isEmpty()){
			for (String key : httpHeaders.keySet()){
				get.setHeader(key, httpHeaders.get(key));
			}
		}

		return getHttpClient().execute(get);
	}

	private HttpResponse executePost(String URL) {
		return null;
	}
	
	public void setPathNameURL(String pathNameURL) {
		this.pathNameURL = pathNameURL;
	}

	public void setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
	}

	public void addHeader(String key, String value) {
		this.httpHeaders.put(key, value);
	}

	/**
	 * Not a good idea to use basic authentication for production.  Use certs or OAuth instead.
	 * */
	public void addBasicCredentials(String username, String password) {
		String stringBuilder = new StringBuilder(username).append(":").append(password).toString();
		String encoded = "Basic " + new String(Base64.encodeBase64(stringBuilder.getBytes()));
		this.addHeader(HttpWrapper.AUTHORIZATION, encoded);
	}

	private HttpClient getHttpClient() {
		if (this.httpClient == null) {
			this.httpClient = HttpClientBuilder.create().build();
		}
		return this.httpClient;
	}

}
