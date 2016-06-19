package com.sap.alexa.c4c;

import org.apache.olingo.odata2.api.edm.Edm;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class C4CODataHandlerTest {

	@Test
	public void testReadEdm() throws Exception{
		C4CODataHandler odata = new C4CODataHandler();
		Edm edm = odata.readEdm();
		
		assertNotNull(edm);
		assertNotNull("CSRF Token not fetched", odata.csrfToken);
	}
}
