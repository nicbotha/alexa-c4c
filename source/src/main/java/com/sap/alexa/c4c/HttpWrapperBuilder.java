package com.sap.alexa.c4c;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.alexa.c4c.HttpWrapper.RequestMethod;

public class HttpWrapperBuilder {
	
	protected final Logger log = LoggerFactory.getLogger(HttpWrapperBuilder.class);
	protected RequestMethod requestMethod;
	protected HashMap<String, String> httpHeaders = new HashMap<String, String>();
	protected String serviceURL;

	public HttpWrapperBuilder() {
		requestMethod = RequestMethod.GET;
		setContentTypeXml();
		loadConfig();
	}

	public HttpWrapperBuilder setC4CTenantName(String node) {
		this.serviceURL = HttpWrapper.SERVICE_URL.replaceFirst("tenant", node);
		return this;
	}

	public HttpWrapperBuilder setRequestMethod(RequestMethod requestMethod) {
		this.requestMethod = requestMethod;
		return this;
	}

	public HttpWrapperBuilder setContentTypeXml() {
		this.httpHeaders.put(HttpWrapper.HTTP_HEADER_CONTENT_TYPE, HttpWrapper.APPLICATION_XML);
		return this;
	}

	public HttpWrapperBuilder addHeader(String key, String value) {
		this.httpHeaders.put(key, value);
		return this;
	}

	/**
	 * Not a good idea to use basic authentication for production.  Use certs or OAuth instead.
	 * */
	public HttpWrapperBuilder encodeCredentials(String username, String password) {
		String stringBuilder = new StringBuilder(username).append(":").append(password).toString();
		String encoded = "Basic " + new String(Base64.encodeBase64(stringBuilder.getBytes()));
		return this.addHeader(HttpWrapper.AUTHORIZATION_HEADER, encoded);
	}

	public HttpWrapper build() {
		return new HttpWrapper(this.requestMethod, this.httpHeaders, this.serviceURL);
	}

	protected void loadConfig() {
		InputStream inputStream = null;
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";

			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}

			encodeCredentials(prop.getProperty("username"), prop.getProperty("password"));
			setC4CTenantName(prop.getProperty("tenant"));

		} catch (Exception e) {
			log.error("Error while opening config.", e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
				}
			}
		}
	}

}
