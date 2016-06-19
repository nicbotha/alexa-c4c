package com.sap.alexa.c4c;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HttpWrapperTest {
	
	public static final String SERVICE_URL ="https://tenant001.crm.ondemand.com/sap/byd/odata/v1";
	public static final String AUTH = "Basic RGFmdDpQdW5r";

	@Test
	public void testDefaults(){
		HttpWrapper httpWrapper = new HttpWrapper(SERVICE_URL, "Daft", "Punk");
		assertEquals(HttpWrapper.RequestMethod.GET, httpWrapper.requestMethod);		
		assertEquals(SERVICE_URL, httpWrapper.serviceURL);		
		assertEquals(AUTH, httpWrapper.httpHeaders.get(HttpWrapper.AUTHORIZATION));
	}
}
