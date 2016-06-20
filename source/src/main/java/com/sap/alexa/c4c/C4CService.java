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
import com.sap.alexa.shared.AccountEntityContainer;

public class C4CService {

	protected final Logger log = LoggerFactory.getLogger(C4CService.class);
	
	private C4CODataHandler odata;
	public static final String QRY_ACCOUNTSBYOWNER = "?$select=AccountID,AccountName,ObjectID,OwnerID,StatusCode,UUID"
			+ "&$format=json"
			+ "&$filter=OwnerID%20eq%20'@1'%20and%20StatusCode%20eq%20'2'"
			+ "&$top=5"
			+ "&$skip=@2"
			+ "&$orderby=AccountName"
			+ "&$inlinecount=allpages";
	public static final String QRY_ACCOUNTBYNAME = "??$select=AccountID,AccountName,ObjectID,OwnerID,StatusCode,UUID"
			+ "&$format=json"
			+ "&$filter=startswith(AccountName,'@1')%20and%20StatusCode%20eq%20'2'"
			+ "&$top=5"
			+ "&$skip=@2"
			+ "&$orderby=AccountName"
			+ "&$inlinecount=allpages";

	public C4CService() {
		getOData();
	}

	/**
	 * Find all Accounts for owner that is active.
	 */
	public AccountEntityContainer findAccountsByOwner(String ownerID, String skip) throws IOException, ODataException {
		log.info(">> findAccountsByOwner ownerId={}, skip={}", ownerID, skip);
		ArrayList<Account> result = new ArrayList<Account>();

		String queryString = QRY_ACCOUNTSBYOWNER.replaceFirst("@1", ownerID);
		queryString = queryString.replaceFirst("@2", skip);

		ODataFeed feed = getOData().readFeed(HttpWrapper.APPLICATION_JSON, "AccountCollection", queryString);

		Integer inlineCount = feed.getFeedMetadata().getInlineCount();
		
		for (ODataEntry entry : feed.getEntries()) {
			result.add(mapEntryToAccount(entry));
		}

		return new AccountEntityContainer(result, inlineCount == null ? 0 : inlineCount.intValue());
	}

	/**
	 * Find all Acounts with name starting with search criteria.
	 */
	public AccountEntityContainer findAccountByName(String accountName, String skip) throws IOException, ODataException {
		log.info(">> findAccountByName accountName={}, skip={}", accountName, skip);
		ArrayList<Account> result = new ArrayList<Account>();

		String queryString = QRY_ACCOUNTBYNAME.replaceFirst("@1", accountName);
		queryString = queryString.replaceFirst("@2", skip);

		ODataFeed feed = getOData().readFeed(HttpWrapper.APPLICATION_JSON, "AccountCollection", queryString);

		Integer inlineCount = feed.getFeedMetadata().getInlineCount();
		
		for (ODataEntry entry : feed.getEntries()) {
			result.add(mapEntryToAccount(entry));
		}

		return new AccountEntityContainer(result, inlineCount == null ? 0 : inlineCount.intValue());
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
