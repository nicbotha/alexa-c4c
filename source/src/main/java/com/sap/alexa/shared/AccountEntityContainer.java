package com.sap.alexa.shared;

import java.util.List;

@SuppressWarnings("serial")
public class AccountEntityContainer extends EntityContainer<Account> {

	public AccountEntityContainer(List<Account> entities, int count) {
		super(entities, count);
	}

}
