package com.sap.alexa.c4c;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.alexa.c4c.HttpWrapper.RequestMethod;

public class C4CODataHandler {

	protected String username;
	protected String password;
	protected String C4CServiceNameURL;
	protected String csrfToken = null;
	protected Edm edm = null;
	public static final String METADATA = "$metadata";
	protected HttpWrapper httpWrapper = null;
	protected final Logger log = LoggerFactory.getLogger(C4CODataHandler.class);

	public C4CODataHandler() {
		loadConfig();
		httpWrapper = new HttpWrapper(C4CServiceNameURL, username, password);
	}

	public ODataFeed readFeed(String contentType, String entitySetName, String options) throws IllegalStateException, IOException, EntityProviderException, EdmException {
		log.info(">> readFeed contentType={}, entitySetName={}, options={}", contentType, entitySetName, options);
		HttpResponse response = null;
		Edm edm = readEdm();
		EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		String pathNameURL = createURLPathName(entitySetName, null, options);

		httpWrapper.setRequestMethod(RequestMethod.GET);
		httpWrapper.setPathNameURL(pathNameURL);

		response = httpWrapper.execute();

		InputStream content = response.getEntity().getContent();
		return EntityProvider.readFeed(contentType, entityContainer.getEntitySet(entitySetName), content, EntityProviderReadProperties.init().build());
	}

	public ODataEntry readEntry(String contentType, String entitySetName, String keyValue, String options) throws IllegalStateException, IOException, EdmException, EntityProviderException {
		log.info(">> readEntry contentType={}, entitySetName={}, keyValue={}, options={}", contentType, entitySetName, keyValue, options);
		HttpResponse response = null;
		EdmEntityContainer entityContainer = readEdm().getDefaultEntityContainer();
		String pathNameURL = createURLPathName(entitySetName, keyValue, options);

		httpWrapper.setRequestMethod(RequestMethod.GET);
		httpWrapper.setPathNameURL(pathNameURL);

		response = httpWrapper.execute();

		InputStream content = response.getEntity().getContent();

		return EntityProvider.readEntry(contentType, entityContainer.getEntitySet(entitySetName), content, EntityProviderReadProperties.init().build());
	}

	protected Edm readEdm() throws EntityProviderException, IllegalStateException, IOException {
		log.info(">> readEdm");
		HttpResponse response = null;

		if (edm != null) {
			return edm;
		}

		httpWrapper.addHeader(HttpWrapper.HTTP_HEADER_CONTENT_TYPE, HttpWrapper.APPLICATION_XML);
		httpWrapper.addHeader(HttpWrapper.HTTP_HEADER_CSRF_TOKEN, HttpWrapper.CSRF_TOKEN_FETCH);
		httpWrapper.setRequestMethod(RequestMethod.GET);
		httpWrapper.setPathNameURL(METADATA);

		response = httpWrapper.execute();
		csrfToken = response.getFirstHeader(HttpWrapper.HTTP_HEADER_CSRF_TOKEN).getValue();
		edm = EntityProvider.readMetadata(response.getEntity().getContent(), false);

		log.debug("<< readEdm() Edm=" + edm);
		return edm;
	}

	/**
	 * Utility method to create the path for the url. Path name to contain any
	 * paths, ids or queries. e.g. /peopleCollection(100)?format=json
	 * 
	 */
	private String createURLPathName(String entitySetName, String id, String options) {
		log.info(">> createURLPathName entitySetName={}, id={}, options={}", entitySetName, id, options);
		
		String response = null;
		final StringBuilder pathUri = new StringBuilder(entitySetName);
		if (id != null) {
			pathUri.append("('").append(id).append("')");
		}

		if (options != null) {
			if (options != null) {
				pathUri.append(options);
			}
		}
		
		response = pathUri.toString();

		log.info("<< createURLPathName URL={}", response);
		return response;
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

			this.username = prop.getProperty("username");
			this.password = prop.getProperty("password");
			this.C4CServiceNameURL = prop.getProperty("C4CNameServiceURL");

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
