package com.sap.alexa.shared;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class AccountEntityContainer extends EntityContainer<Account> implements Serializable{

	public AccountEntityContainer(List<Account> entities, int count) {
		super(entities, count);
	}

}
