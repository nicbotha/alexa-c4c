package com.sap.alexa;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.Test;

import com.sap.alexa.shared.Account;
import com.sap.alexa.shared.EntityContainer;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class DataCacheTest {

	@Test
	public void testBehaviour() throws Exception{
		List<Account> expectedAccounts1 = generateAccounts(5);
		DataCache<Account> cache = new DataCache<Account>(new EntityContainer<Account>(expectedAccounts1, 12));
		
		assertEquals(0, cache.index());
		assertEquals(5, cache.skip());
		assertTrue(cache.getWorkingSet().containsAll(expectedAccounts1));	
		
		List<Account> expectedAccounts2 = generateAccounts(5);
		cache.addEntityContainer(new EntityContainer<Account>(expectedAccounts2, 12));
		
		assertFalse(cache.getWorkingSet().containsAll(expectedAccounts1));
		assertTrue(cache.getWorkingSet().containsAll(expectedAccounts2));
		assertEquals(1, cache.index());
		assertEquals(10, cache.skip());
	}
	
	private List<Account> generateAccounts(int count){
		List<Account> accounts = new ArrayList<Account>();
		
		for(int i = 0; i < count; i++){
			accounts.add(generateAccount());
		}
		
		return accounts;
	}
	
	private Account generateAccount(){
		Account acc = new Account();
		acc.setAccountID(UUID.randomUUID().toString());
		acc.setAccountName(UUID.randomUUID().toString());
		acc.setObjectID(UUID.randomUUID().toString());
		acc.setOwnerID(UUID.randomUUID().toString());
		acc.setStatusCode(UUID.randomUUID().toString());
		return acc;
	}
	
}
