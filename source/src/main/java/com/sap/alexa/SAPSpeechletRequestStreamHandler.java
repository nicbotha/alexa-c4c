package com.sap.alexa;

import java.util.HashSet;
import java.util.Set;

import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public class SAPSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
   	private static final Set<String> supportedApplicationIds;

    static {
        /*
         * This Id can be found on https://developer.amazon.com/edw/home.html#/ "Edit" the relevant
         * Alexa Skill and put the relevant Application Ids in this Set.
         */
        supportedApplicationIds = new HashSet<String>();
        supportedApplicationIds.add("amzn1.echo-sdk-ams.app.26e2c935-24ae-46c9-846a-9e7b790290d5");
    }

    public SAPSpeechletRequestStreamHandler() {
        super(new SAPSpeechlet(), supportedApplicationIds);
    }
}
