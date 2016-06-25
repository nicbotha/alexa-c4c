package com.sap.alexa;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataCacheParser {

	public static String toJson(AccountDataCache accountDataCache, boolean base64) throws JsonProcessingException, UnsupportedEncodingException {
		ObjectMapper mapper = new ObjectMapper();
		if (base64) {
			String json = mapper.writeValueAsString(accountDataCache);
			return Base64.encodeBase64String(json.getBytes());
		}
		return mapper.writeValueAsString(accountDataCache);
	}

	public static AccountDataCache toAccountDataCache(String json, boolean base64) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		if(base64){
			String decodedJson = new String(Base64.decodeBase64(json));
			return mapper.readValue(decodedJson, AccountDataCache.class);
		}
		return mapper.readValue(json, AccountDataCache.class);
	}
}
