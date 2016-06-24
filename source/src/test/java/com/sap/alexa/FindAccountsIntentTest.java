package com.sap.alexa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.olingo.odata2.api.exception.ODataException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.sap.alexa.FindAccountsIntent.State;
import com.sap.alexa.c4c.C4CService;
import com.sap.alexa.shared.Account;
import com.sap.alexa.shared.AccountEntityContainer;
import com.sap.alexa.shared.EntityContainer;

public class FindAccountsIntentTest {
	private FindAccountsIntent findAccountsIntent;
	private static final String PROMPT_FIRST_TIME_EXPECTED = "Found 50 accounts.";

	@Before
	public void setup() {
		findAccountsIntent = new FindAccountsIntent();
	}

	@After
	public void tearDown() {
		findAccountsIntent = null;
	}

	@Test
	public void determineState_FIND_Test() throws Exception {
		State state = findAccountsIntent.determineState(Session.builder().withSessionId(UUID.randomUUID().toString()).withIsNew(true).build());
		assertEquals(State.FIND, state);
	}

	@Test
	public void determineState_FINDMORE_Test() throws Exception {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put(FindAccountsIntent.ATTR_CACHE, new DataCache<Account>(null));
		State state = findAccountsIntent.determineState(Session.builder().withSessionId(UUID.randomUUID().toString()).withAttributes(attributes).build());
		assertEquals(State.FIND_MORE, state);
	}

	/**
	 * 5 Accounts are read, then user is prompted to either use one of the 5 or
	 * list more
	 */
	@Test
	public void accountsFoundResponse_ListMore_Test() throws Exception {
		EntityContainer<Account> entityContainer = new EntityContainer<Account>(Arrays.asList(generateAccount(), generateAccount(), generateAccount(), generateAccount(), generateAccount()), 12);
		DataCache<Account> cache = new DataCache<Account>(entityContainer);
		SpeechletResponse speechletResponse = findAccountsIntent.accountsFoundResponse(cache);

		assertFalse(speechletResponse.getShouldEndSession());
		assertNotNull(speechletResponse.getReprompt());
		assertNotNull(speechletResponse.getReprompt().getOutputSpeech());
		assertEquals(FindAccountsIntent.REPROMPT_LIST_MORE_ACCOUNT, ((PlainTextOutputSpeech) speechletResponse.getReprompt().getOutputSpeech()).getText());
	}

	/**
	 * Available accounts are read, then user is prompted use one of the
	 * accounts
	 */
	@Test
	public void accountsFoundResponse_UseAccount_Test() throws Exception {
		EntityContainer<Account> entityContainer = new EntityContainer<Account>(Arrays.asList(generateAccount(), generateAccount(), generateAccount(), generateAccount(), generateAccount()), 5);
		DataCache<Account> cache = new DataCache<Account>(entityContainer);
		SpeechletResponse speechletResponse = findAccountsIntent.accountsFoundResponse(cache);

		assertFalse(speechletResponse.getShouldEndSession());
		assertNotNull(speechletResponse.getReprompt());
		assertNotNull(speechletResponse.getReprompt().getOutputSpeech());
		assertEquals(FindAccountsIntent.REPROMPT_USE_ACCOUNT, ((PlainTextOutputSpeech) speechletResponse.getReprompt().getOutputSpeech()).getText());
	}

	/**
	 * No accounts could be found response
	 */
	@Test
	public void emptyServiceResultTest() throws Exception {
		SpeechletResponse speechletResponse = findAccountsIntent.emptyServiceResult();

		assertTrue(speechletResponse.getShouldEndSession());
		assertNull(speechletResponse.getReprompt());
		assertEquals(FindAccountsIntent.PROMPT_NOT_FOUND_ACCOUNT, ((PlainTextOutputSpeech) speechletResponse.getOutputSpeech()).getText());
	}
	
	/**
	 * No accounts could be found reponse
	 * */
	@Test
	public void findAccounts_NOT_FOUND_Test() throws Exception{
		this.findAccountsIntent.service = createMockService();
		this.findAccountsIntent.ownerId = "0";
		SpeechletResponse speechletResponse = findAccountsIntent.findAccounts(Session.builder().withSessionId(UUID.randomUUID().toString()).build());
		
		assertTrue(speechletResponse.getShouldEndSession());
		assertNull(speechletResponse.getReprompt());
		assertEquals(FindAccountsIntent.PROMPT_NOT_FOUND_ACCOUNT, ((PlainTextOutputSpeech) speechletResponse.getOutputSpeech()).getText());
	}
	
	/**
	 * Only found 5 accounts, so user should choose which one to use
	 * */
	@Test
	public void findAccounts_FOUND_5_Test() throws Exception{
		this.findAccountsIntent.service = createMockService();
		this.findAccountsIntent.ownerId = "5";
		SpeechletResponse speechletResponse = findAccountsIntent.findAccounts(Session.builder().withSessionId(UUID.randomUUID().toString()).build());
		
		assertFalse(speechletResponse.getShouldEndSession());
		assertNotNull(speechletResponse.getReprompt());
		assertNotNull(speechletResponse.getReprompt().getOutputSpeech());
		assertEquals(FindAccountsIntent.REPROMPT_USE_ACCOUNT, ((PlainTextOutputSpeech) speechletResponse.getReprompt().getOutputSpeech()).getText());
	}
	
	/**
	 * Found more than 5 accounts, so user should either choose which one to use or request to list more
	 * */
	@Test
	public void findAccounts_Many_Test() throws Exception{
		this.findAccountsIntent.service = createMockService();
		this.findAccountsIntent.ownerId = "12";
		SpeechletResponse speechletResponse = findAccountsIntent.findAccounts(Session.builder().withSessionId(UUID.randomUUID().toString()).build());
		
		assertFalse(speechletResponse.getShouldEndSession());
		assertNotNull(speechletResponse.getReprompt());
		assertNotNull(speechletResponse.getReprompt().getOutputSpeech());
		assertEquals(FindAccountsIntent.REPROMPT_LIST_MORE_ACCOUNT, ((PlainTextOutputSpeech) speechletResponse.getReprompt().getOutputSpeech()).getText());
	}
	
	/**
	 * No accounts could be found reponse
	 * */
	@Test
	public void handleIntent_FIND_NOT_FOUND_Test() throws Exception{
		this.findAccountsIntent.service = createMockService();
		this.findAccountsIntent.ownerId = "0";
		this.findAccountsIntent.handleIntent(Intent.builder().withName("FindAccountsIntent").build(), Session.builder().withSessionId(UUID.randomUUID().toString()).withIsNew(true).build());
		SpeechletResponse speechletResponse = findAccountsIntent.findAccounts(Session.builder().withSessionId(UUID.randomUUID().toString()).build());
		
		assertTrue(speechletResponse.getShouldEndSession());
		assertNull(speechletResponse.getReprompt());
		assertEquals(FindAccountsIntent.PROMPT_NOT_FOUND_ACCOUNT, ((PlainTextOutputSpeech) speechletResponse.getOutputSpeech()).getText());
	}
	
	/**
	 * Only found 5 accounts, so user should choose which one to use
	 * */
	@Test
	public void handleIntent_FIND_5_Test() throws Exception{
		this.findAccountsIntent.service = createMockService();
		this.findAccountsIntent.ownerId = "5";
		this.findAccountsIntent.handleIntent(Intent.builder().withName("FindAccountsIntent").build(), Session.builder().withSessionId(UUID.randomUUID().toString()).withIsNew(true).build());
		SpeechletResponse speechletResponse = findAccountsIntent.findAccounts(Session.builder().withSessionId(UUID.randomUUID().toString()).build());
		
		assertFalse(speechletResponse.getShouldEndSession());
		assertNotNull(speechletResponse.getReprompt());
		assertNotNull(speechletResponse.getReprompt().getOutputSpeech());
		assertEquals(FindAccountsIntent.REPROMPT_USE_ACCOUNT, ((PlainTextOutputSpeech) speechletResponse.getReprompt().getOutputSpeech()).getText());
	}
	
	/**
	 * Only found 5 accounts, so user should choose which one to use
	 * */
	@Test
	public void handleIntent_Many_Test() throws Exception{
		this.findAccountsIntent.service = createMockService();
		this.findAccountsIntent.ownerId = "12";
		this.findAccountsIntent.handleIntent(Intent.builder().withName("FindAccountsIntent").build(), Session.builder().withSessionId(UUID.randomUUID().toString()).withIsNew(true).build());
		SpeechletResponse speechletResponse = findAccountsIntent.findAccounts(Session.builder().withSessionId(UUID.randomUUID().toString()).build());
		
		assertFalse(speechletResponse.getShouldEndSession());
		assertNotNull(speechletResponse.getReprompt());
		assertNotNull(speechletResponse.getReprompt().getOutputSpeech());
		assertEquals(FindAccountsIntent.REPROMPT_LIST_MORE_ACCOUNT, ((PlainTextOutputSpeech) speechletResponse.getReprompt().getOutputSpeech()).getText());
	}

	@Test
	public void formatPrompt() throws Exception {
		assertEquals(PROMPT_FIRST_TIME_EXPECTED, String.format(FindAccountsIntent.PROMPT_FIRST_TIME_ACCOUNT, 50));
	}

	private Account generateAccount() {
		Account acc = new Account();
		acc.setAccountID(UUID.randomUUID().toString());
		acc.setAccountName(UUID.randomUUID().toString());
		acc.setObjectID(UUID.randomUUID().toString());
		acc.setOwnerID(UUID.randomUUID().toString());
		acc.setStatusCode(UUID.randomUUID().toString());
		return acc;
	}

	private C4CService createMockService() {
		return new C4CService() {
			@Override
			public AccountEntityContainer findAccountsByOwner(String ownerID, String skip) throws IOException, ODataException {
				if (ownerID == "0") {
					return new AccountEntityContainer(null, 0);
				} else if (ownerID == "5") {
					return new AccountEntityContainer(Arrays.asList(generateAccount(), generateAccount(), generateAccount(), generateAccount(), generateAccount()), 5);
				} else {
					return new AccountEntityContainer(Arrays.asList(generateAccount(), generateAccount(), generateAccount(), generateAccount(), generateAccount(), generateAccount(), generateAccount(), generateAccount(), generateAccount(), generateAccount(), generateAccount(), generateAccount()), 12);
				}
			}
		};
	}

}
