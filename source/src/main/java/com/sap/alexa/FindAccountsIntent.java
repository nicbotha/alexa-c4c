package com.sap.alexa;

import java.io.IOException;

import org.apache.olingo.odata2.api.exception.ODataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.sap.alexa.c4c.C4CService;
import com.sap.alexa.shared.Account;
import com.sap.alexa.shared.AccountEntityContainer;

public class FindAccountsIntent {
	private static final Logger log = LoggerFactory.getLogger(FindAccountsIntent.class);
	private C4CService service = new C4CService();

	private enum State {
		FIND, FIND_MORE
	};

	private static final String ATTR_CACHE = "accountCache";

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

		return response;
	}
	
	@SuppressWarnings("unchecked")
	private SpeechletResponse findMoreAccounts(Session session) {
		log.info(">> findAccounts sessionId={}", session.getSessionId());
		SpeechletResponse response = null;

		try {
			DataCache<Account> cache = (DataCache<Account>) session.getAttribute(ATTR_CACHE);
			AccountEntityContainer accountEntityContainer = service.findAccountsByOwner("1000", String.valueOf(cache.skip()));
			
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
		
		return response;

	}

	private SpeechletResponse findAccounts(Session session) {
		log.info(">> findAccounts sessionId={}", session.getSessionId());
		SpeechletResponse response = null;

		try {
			AccountEntityContainer accountEntityContainer = service.findAccountsByOwner("1000", "0");
			
			if(accountEntityContainer.isEmpty()){
				response = emptyServiceResult();
			}else{
				DataCache<Account> cache = new DataCache<Account>(accountEntityContainer);
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
		
		return response;
	}

	private SpeechletResponse accountsFoundResponse(DataCache<Account> cache) {
		StringBuffer sb = new StringBuffer();
		if(cache.index() == 0){
			sb.append("Found "+ cache.getEntityContainer().getCount() +" accounts.");
		}
		for(Account account : cache.getWorkingSet()){
			sb.append(account.getAccountName());
			sb.append(",");
		}
		
		if(cache.hasMore()){
			sb.append("Would you like to hear more?");
		}
		
		return getSpeechletResponse(sb.toString(), null, false);
	}

	

	private State determineState(Session session) {
		State state = State.FIND;

		if (session.getAttribute(ATTR_CACHE) != null) {
			state = State.FIND_MORE;
		}

		return state;
	}

	private SpeechletResponse emptyServiceResult(){
		return getSpeechletResponse("No account could be found.", null, false);
	}
	
	private SpeechletResponse errorResponse() {
		return getSpeechletResponse("A technical error occured.  Your request could not be completed.", null, false);
	}

	/**
	 * Returns a Speechlet response for a speech and reprompt text.
	 */
	private SpeechletResponse getSpeechletResponse(String speechText, String repromptText, boolean isAskResponse) {
		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("SAP Accounts");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		if (isAskResponse) {
			// Create reprompt
			PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
			repromptSpeech.setText(repromptText);
			Reprompt reprompt = new Reprompt();
			reprompt.setOutputSpeech(repromptSpeech);

			return SpeechletResponse.newAskResponse(speech, reprompt, card);

		} else {
			return SpeechletResponse.newTellResponse(speech, card);
		}
	}
}
