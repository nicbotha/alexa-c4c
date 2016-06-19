package com.sap.alexa.c4c;

import java.util.ArrayList;

import org.junit.Test;

import com.sap.alexa.shared.Account;
import static org.junit.Assert.assertNotNull;
public class C4CClientTest {

	private C4CClient c4cClient = new C4CClient();
	
	@Test
	public void testListAccountsByOwner() throws Exception{
		ArrayList<Account> results = c4cClient.listAccountsByOwner("1000");
		assertNotNull(results);		
	}
}
