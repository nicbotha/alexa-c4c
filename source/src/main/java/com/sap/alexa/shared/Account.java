package com.sap.alexa.shared;

public class Account {
	private String AccountID;
	private String AccountName;
	private String ObjectID;
	private String OwnerID;
	private String StatusCode;
	private String UUID;

	public void set(String key, Object value) {
		if (key.equals("ObjectID")) {
			this.setObjectID(value.toString());
		} else if (key.equals("AccountID")) {
			this.setAccountID(value.toString());
		} else if (key.equals("AccountName")) {
			this.setAccountName(value.toString());
		} else if (key.equals("OwnerID")) {
			this.setOwnerID(value.toString());
		} else if (key.equals("StatusCode")) {
			this.setStatusCode(value.toString());
		}
	}

	public String getAccountID() {
		return AccountID;
	}

	public void setAccountID(String accountID) {
		AccountID = accountID;
	}

	public String getAccountName() {
		return AccountName;
	}

	public void setAccountName(String accountName) {
		AccountName = accountName;
	}

	public String getObjectID() {
		return ObjectID;
	}

	public void setObjectID(String objectID) {
		ObjectID = objectID;
	}

	public String getOwnerID() {
		return OwnerID;
	}

	public void setOwnerID(String ownerID) {
		OwnerID = ownerID;
	}

	public String getStatusCode() {
		return StatusCode;
	}

	public void setStatusCode(String statusCode) {
		StatusCode = statusCode;
	}

	public String getUUID() {
		return UUID;
	}

	public void setUUID(String uUID) {
		UUID = uUID;
	}

	@Override
	public String toString() {
		return "Account [AccountID=" + AccountID + ", AccountName=" + AccountName + ", ObjectID=" + ObjectID + ", OwnerID=" + OwnerID + ", StatusCode=" + StatusCode + ", UUID=" + UUID + "]";
	}

}
