package com.sap.alexa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class SpeechletResponseHelper {
	private static final Logger log = LoggerFactory.getLogger(SpeechletResponseHelper.class);

	public static SpeechletResponse getSpeechletResponse(String speechText, String repromptText, boolean isAskResponse) {
		log.info(">> getSpeechletResponse speechText={}, repromptText={}, isAskResponse={}", speechText, repromptText, isAskResponse);
		
		SpeechletResponse response = null;
		
		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("SAP");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		if (isAskResponse) {
			log.info(">>> getSpeechletResponse repromptText={}, isAskResponse={}", repromptText, isAskResponse);
			PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
			repromptSpeech.setText(repromptText);
			Reprompt reprompt = new Reprompt();
			reprompt.setOutputSpeech(repromptSpeech);
			response = SpeechletResponse.newAskResponse(speech, reprompt, card);
		} else {
			log.info(">>> getSpeechletResponse isAskResponse={}", isAskResponse);
			response = SpeechletResponse.newTellResponse(speech, card);
		}
		
		log.info("<< getSpeechletResponse SpeechletResponse.shouldEndSession={}", response.getShouldEndSession());
		return response;
	}
}
