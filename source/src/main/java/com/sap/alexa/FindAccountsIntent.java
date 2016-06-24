package com.sap.alexa;

import java.io.IOException;

import org.apache.olingo.odata2.api.exception.ODataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.sap.alexa.c4c.C4CService;
import com.sap.alexa.shared.Account;
import com.sap.alexa.shared.AccountEntityContainer;

public class FindAccountsIntent {
	private static final Logger log = LoggerFactory.getLogger(FindAccountsIntent.class);

	public enum State {
		FIND, FIND_MORE
	};
	
	protected C4CService service = null;
	public static final String REPROMPT_LIST_MORE_ACCOUNT = "To use an account say, use account.  To list more accounts say, list more.";
	public static final String REPROMPT_USE_ACCOUNT = "Please select an account by saying, use account.";
	public static final String PROMPT_NOT_FOUND_ACCOUNT = "Could not find any accounts.";
	public static final String PROMPT_FIRST_TIME_ACCOUNT = "Found %s accounts. ";
	public static final String ATTR_CACHE = "accountCache";
	private static final String PROMPT_TECHNICAL_ERROR = "A technical error occured.  Your request could not be completed.";
	protected String ownerId = "1000";
	
	public FindAccountsIntent() {
		service = new C4CService();
	}
	
	public FindAccountsIntent(C4CService service) {
		this.service = service;
	}

	protected SpeechletResponse handleIntent(final Intent intent, final Session session) {
		log.info(">> handleIntent IntentName={}, sessionId={}", intent.getName(), session.getSessionId());
		
		State state = determineState(session);
		SpeechletResponse response = null;

		switch (state) {
		case FIND:
			response = findAccounts(session);
			break;
		case FIND_MORE:
			response = findMoreAccounts(session);
			break;
		default:
			response = emptyServiceResult();
			break;
		}

		log.info("<< handleIntent SpeechletResponse.shouldEndSession={}", response.getShouldEndSession());
		return response;
	}
	
	protected SpeechletResponse findMoreAccounts(Session session) {
		log.info(">> findMoreAccounts sessionId={}", session.getSessionId());
		
		SpeechletResponse response = null;

		try {
			AccountDataCache cache = (AccountDataCache) session.getAttribute(ATTR_CACHE);
			AccountEntityContainer accountEntityContainer = service.findAccountsByOwner(this.ownerId, String.valueOf(cache.skip()));
			
			if(accountEntityContainer.isEmpty()){
				response = emptyServiceResult();
			}else{
				cache.addEntityContainer(accountEntityContainer);
				session.setAttribute(ATTR_CACHE, cache);				
				response = accountsFoundResponse(cache);
			}
			
		} catch (IOException e) {
			log.error("Cannot find Accounts from C4C.", e);
			response = errorResponse();
		} catch (ODataException e) {
			log.error("Cannot find Accounts from C4C.", e);
			response = errorResponse();
		}
		
		log.info("<< findMoreAccounts SpeechletResponse={}", response);
		return response;

	}

	protected SpeechletResponse findAccounts(Session session) {
		log.info(">> findAccounts sessionId={}", session.getSessionId());
		SpeechletResponse response = null;

		try {
			AccountEntityContainer accountEntityContainer = service.findAccountsByOwner(this.ownerId, "0");
			
			if(accountEntityContainer.isEmpty()){
				response = emptyServiceResult();
			}else{
				AccountDataCache cache = new AccountDataCache(accountEntityContainer);
				session.setAttribute(ATTR_CACHE, cache);				
				response = accountsFoundResponse(cache);
			}
			
		} catch (IOException e) {
			log.error("Cannot find Accounts from C4C.", e);
			response = errorResponse();
		} catch (ODataException e) {
			log.error("Cannot find Accounts from C4C.", e);
			response = errorResponse();
		}
		
		log.info("<< findAccounts SpeechletResponse={}", response);
		return response;
	}

	protected SpeechletResponse accountsFoundResponse(DataCache<Account> cache) {
		log.info(">> accountsFoundResponse DataCache<Account>={}", cache);
		StringBuffer sb = new StringBuffer();
		String prompt = null;
		int index = 0;
		
		if(cache.index() == 0){
			sb.append(String.format(FindAccountsIntent.PROMPT_FIRST_TIME_ACCOUNT,  cache.getEntityContainer().getCount()));
		}
		
		for(Account account : cache.getWorkingSet()){
			sb.append(++index + ", ");
			sb.append(account.getAccountName());
			sb.append(", ");
		}
		
		prompt = sb.toString();
		prompt = prompt.substring(0, prompt.lastIndexOf(","));
		
		SpeechletResponse response = SpeechletResponseHelper.getSpeechletResponse(prompt, (cache.hasMore()? REPROMPT_LIST_MORE_ACCOUNT : REPROMPT_USE_ACCOUNT), true);
		
		log.info("<< accountsFoundResponse SpeechletResponse={}", response);
		return response;
	}
	
	protected State determineState(Session session) {
		log.info(">> determineState sessionId={}", session.getSessionId());
		State state = State.FIND;

		if (session.getAttribute(ATTR_CACHE) != null) {
			state = State.FIND_MORE;
		}
		log.info("<< determineState State={}", state.toString());
		return state;
	}

	public SpeechletResponse emptyServiceResult(){
		return SpeechletResponseHelper.getSpeechletResponse(PROMPT_NOT_FOUND_ACCOUNT, null, false);
	}
	
	private SpeechletResponse errorResponse() {
		return SpeechletResponseHelper.getSpeechletResponse(PROMPT_TECHNICAL_ERROR, null, false);
	}


}
