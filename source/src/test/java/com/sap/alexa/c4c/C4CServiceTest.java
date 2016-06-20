package com.sap.alexa.c4c;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.sap.alexa.shared.AccountEntityContainer;
public class C4CServiceTest {

	private C4CService c4cClient = new C4CService();
	
	@Test
	public void testFindAccountsByOwner() throws Exception{
		AccountEntityContainer results = c4cClient.findAccountsByOwner("1000", "0");
		assertNotNull(results);	
		assertNotNull(results.getEntities());
		assertNotEquals(0, results.getCount());
	}
	
	@Test
	public void testFindAccountsByName() throws Exception{
		AccountEntityContainer results = c4cClient.findAccountByName("Lowes", "0");
		assertNotNull(results);	
		assertNotNull(results.getEntities());
		assertNotEquals(0, results.getCount());	
	}
}
