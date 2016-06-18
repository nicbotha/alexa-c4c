package com.sap.alexa.c4c;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HttpWrapperBuilderTest {
	
	public static final String SERVICE_URL ="https://tenant001.crm.ondemand.com/sap/byd/odata/v1/servicerequest";
	public static final String AUTH = "Basic RGFmdDpQdW5r";

	@Test
	public void testDefaults(){
		HttpWrapperBuilder builder = new HttpWrapperBuilder();
		assertEquals(HttpWrapper.RequestMethod.GET, builder.requestMethod);		
		assertEquals(SERVICE_URL, builder.serviceURL);
		
		String content_type = builder.httpHeaders.get(HttpWrapper.HTTP_HEADER_CONTENT_TYPE);
		String auth = builder.httpHeaders.get(HttpWrapper.AUTHORIZATION_HEADER);
		
		assertEquals(HttpWrapper.APPLICATION_XML, content_type);
		assertEquals(AUTH, auth);
	}
}
