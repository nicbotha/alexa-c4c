package com.sap.alexa.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Account implements Serializable{
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
		}else if (key.equals("UUID")) {
			this.setUUID(value.toString());
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((AccountID == null) ? 0 : AccountID.hashCode());
		result = prime * result + ((AccountName == null) ? 0 : AccountName.hashCode());
		result = prime * result + ((ObjectID == null) ? 0 : ObjectID.hashCode());
		result = prime * result + ((OwnerID == null) ? 0 : OwnerID.hashCode());
		result = prime * result + ((StatusCode == null) ? 0 : StatusCode.hashCode());
		result = prime * result + ((UUID == null) ? 0 : UUID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (AccountID == null) {
			if (other.AccountID != null)
				return false;
		} else if (!AccountID.equals(other.AccountID))
			return false;
		if (AccountName == null) {
			if (other.AccountName != null)
				return false;
		} else if (!AccountName.equals(other.AccountName))
			return false;
		if (ObjectID == null) {
			if (other.ObjectID != null)
				return false;
		} else if (!ObjectID.equals(other.ObjectID))
			return false;
		if (OwnerID == null) {
			if (other.OwnerID != null)
				return false;
		} else if (!OwnerID.equals(other.OwnerID))
			return false;
		if (StatusCode == null) {
			if (other.StatusCode != null)
				return false;
		} else if (!StatusCode.equals(other.StatusCode))
			return false;
		if (UUID == null) {
			if (other.UUID != null)
				return false;
		} else if (!UUID.equals(other.UUID))
			return false;
		return true;
	}

}
