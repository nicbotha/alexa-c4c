package com.sap.alexa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;

public class SAPSpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(SAPSpeechlet.class);
	
	private FindAccountsIntent findAccountsIntent = new FindAccountsIntent();

	public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {

	}

	public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
		log.info(">> onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		return getWelcomeResponse();
	}

	public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
		log.info(">> onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		SpeechletResponse response = null;
		
		// Get intent from the request object.
		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;

		if ("FindAccountsIntent".equals(intentName)) {
			response= findAccountsIntent.handleIntent(intent, session);
		}else if ("UseAccountIntent".equals(intentName)) {
		} else {
			throw new SpeechletException("Invalid Intent");
		}
		
		log.info("<< onIntent SpeechletResponse.shouldEndSession={}", response.getShouldEndSession());
		return response;
	}

	public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {

	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with a welcome message.
	 *
	 * @return SpeechletResponse spoken and visual welcome message
	 */
	private SpeechletResponse getWelcomeResponse() {
		String speechText = "Welcome to SAP. You can find accounts by saying, find my accounts";
		return SpeechletResponseHelper.getSpeechletResponse(speechText, null, false);
	}
}
