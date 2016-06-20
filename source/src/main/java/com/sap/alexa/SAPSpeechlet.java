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
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class SAPSpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(SAPSpeechlet.class);
	
	private FindAccountsIntent findAccountsIntent = new FindAccountsIntent();

	public void onSessionStarted(SessionStartedRequest request, Session session) throws SpeechletException {
		// TODO Auto-generated method stub

	}

	public SpeechletResponse onLaunch(LaunchRequest request, Session session) throws SpeechletException {
		log.info(">> onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		return getWelcomeResponse();
	}

	public SpeechletResponse onIntent(IntentRequest request, Session session) throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		// Get intent from the request object.
		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;

		// Note: If the session is started with an intent, no welcome message
		// will be rendered;
		// rather, the intent specific response will be returned.
		if ("FindAccountsIntent".equals(intentName)) {
			return findAccountsIntent.handleIntent(intent, session);
		} else if ("UseAccountIntent".equals(intentName)) {
		} else {
			throw new SpeechletException("Invalid Intent");
		}
		return null;
	}

	public void onSessionEnded(SessionEndedRequest request, Session session) throws SpeechletException {
		// TODO Auto-generated method stub

	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with a welcome message.
	 *
	 * @return SpeechletResponse spoken and visual welcome message
	 */
	private SpeechletResponse getWelcomeResponse() {
		// Create the welcome message.
		String speechText = "Welcome to SAP. You can find accounts by saying, find my accounts";
		String repromptText = "Please tell me how I may be of assistance?";

		return getSpeechletResponse(speechText, repromptText, true);
	}

	/**
	 * Returns a Speechlet response for a speech and reprompt text.
	 */
	private SpeechletResponse getSpeechletResponse(String speechText, String repromptText, boolean isAskResponse) {
		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("Session");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		if (isAskResponse) {
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
