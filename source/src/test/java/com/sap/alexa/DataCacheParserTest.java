package com.sap.alexa;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;

import com.sap.alexa.shared.Account;
import com.sap.alexa.shared.AccountEntityContainer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DataCacheParserTest {

	@Test
	public void parse_AccountDataCache_Test() throws Exception{
		AccountEntityContainer entityContainer = new AccountEntityContainer(Arrays.asList(generateAccount("1","testAccount", "2", "3", "t")), 1);
		AccountDataCache expectedAccountDataCache = new AccountDataCache(entityContainer);
		
		String json = DataCacheParser.toJson(expectedAccountDataCache, true);
		assertNotNull(json);
		
		AccountDataCache accountDataCache = DataCacheParser.toAccountDataCache(json, true);
		assertNotNull(accountDataCache);
		assertNotNull(accountDataCache.getEntityContainer());
		assertEquals(expectedAccountDataCache.getEntityContainer().getCount(), accountDataCache.getEntityContainer().getCount());
	}
	
	private Account generateAccount(String id, String name, String objId, String ownerId, String status){
		Account acc = new Account();
		acc.setAccountID(id==null? UUID.randomUUID().toString(): id);
		acc.setAccountName(name==null?UUID.randomUUID().toString():name);
		acc.setObjectID(objId==null?UUID.randomUUID().toString():objId);
		acc.setOwnerID(ownerId==null?UUID.randomUUID().toString():ownerId);
		acc.setStatusCode(status==null?UUID.randomUUID().toString():status);
		return acc;
	}
}
