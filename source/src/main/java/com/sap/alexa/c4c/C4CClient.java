package com.sap.alexa.c4c;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.alexa.shared.Account;

public class C4CClient {

	protected final Logger log = LoggerFactory.getLogger(C4CClient.class);
	private C4CODataHandler odata;
	public static final String QRY_ACCOUNTSBYOWNER = "?$format=json&$filter=OwnerID%20eq%20'@'%20and%20StatusCode%20eq%20'2'&$top=5&$inlinecount=allpages";

	public C4CClient() {
	}

	public ArrayList<Account> listAccountsByOwner(String ownerID) throws IOException, ODataException {
		ArrayList<Account> result = new ArrayList<Account>();

		String queryString = QRY_ACCOUNTSBYOWNER.replaceFirst("@", ownerID);

		ODataFeed feed = getOData().readFeed(HttpWrapper.APPLICATION_JSON, "AccountCollection", queryString);

		for (ODataEntry entry : feed.getEntries()) {
			result.add(mapEntryToAccount(entry));
		}

		return result;
	}

	public Account mapEntryToAccount(ODataEntry entry) {
		Account account = new Account();

		Map<String, Object> propMap = entry.getProperties();

		for (Map.Entry<String, Object> e : propMap.entrySet()) {
			Object value = e.getValue();
			String propName = e.getKey();

			if (value != null) {
				account.set(propName, value);
			}

		}

		return account;
	}

	protected C4CODataHandler getOData() {
		if (this.odata == null) {
			this.odata = new C4CODataHandler();
		}

		return this.odata;
	}
}
