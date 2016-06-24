package com.sap.alexa;

import com.sap.alexa.shared.Account;
import com.sap.alexa.shared.AccountEntityContainer;

@SuppressWarnings("serial")
public class AccountDataCache extends DataCache<Account> {

	public AccountDataCache(AccountEntityContainer entityContainer) {
		super(entityContainer);
	}

}
