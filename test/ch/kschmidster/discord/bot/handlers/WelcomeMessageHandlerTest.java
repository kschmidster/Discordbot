package ch.kschmidster.discord.bot.handlers;

import static ch.kschmidster.discord.bot.core.test.DiscordBotTestLoggerHelper.logTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.kschmidster.discord.bot.core.test.ConfigReader;
import ch.kschmidster.discord.bot.core.test.DiscordBotTestLoggerHelper;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WelcomeMessageHandlerTest {
	private final static Log log = LogFactory.getLog(WelcomeMessageHandlerTest.class);

	private static final String PROPERTY_FILE = "config.properties";
	private static final String ANSWER_FILE = "test/answer.properties";

	private final Configuration configuration = Mockito.mock(Configuration.class);
	private final WelcomeMessageHandler handler = new WelcomeMessageHandler(configuration);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DiscordBotTestLoggerHelper.logBeforeClass(log, WelcomeMessageHandlerTest.class);
	}

	@Test
	public void testmessageContainsWelcomeMessage121() {
		logTest(log, "testmessageContainsWelcomeMessage");

		MessageReceivedEvent event = mock(MessageReceivedEvent.class);
		Message message = mock(Message.class);

		when(event.getMessage()).thenReturn(message);
		List<String> answers = ConfigReader.readFile(PROPERTY_FILE).getConfigs(WelcomeMessageHandler.WELCOME_MESSAGE);
		when(configuration.getStringArray(any())).thenReturn(answers.stream().toArray(String[]::new));

		answers.forEach(checkEventMessageContains(event, message));
	}

	@Test
	public void testmessageContainsWelcomeMessageWithNoise() {
		logTest(log, "testmessageContainsWelcomeMessageWithNoise");

		MessageReceivedEvent event = mock(MessageReceivedEvent.class);
		Message message = mock(Message.class);

		when(event.getMessage()).thenReturn(message);
		List<String> answers = ConfigReader.readFile(PROPERTY_FILE).getConfigs(WelcomeMessageHandler.WELCOME_MESSAGE);
		when(configuration.getStringArray(any())).thenReturn(answers.stream().toArray(String[]::new));

		List<String> welcomeMsgs = ConfigReader.readFile(ANSWER_FILE).getConfigs("welcomemessagehandler.answer");
		welcomeMsgs.forEach(checkEventMessageContains(event, message));
	}

	private Consumer<? super String> checkEventMessageContains(MessageReceivedEvent event, Message message) {
		return s -> {
			when(message.getContentDisplay()).thenReturn(s);
			assertTrue(s + " has not match", handler.messageContainsWelcomeMessage(event));
		};
	}

	@Test
	public void testGetAnswers() {
		logTest(log, "testgetWelcomeMessage");

		List<String> answers = ConfigReader.readFile(PROPERTY_FILE).getConfigs(WelcomeMessageHandler.WELCOME_MESSAGE);
		when(configuration.getStringArray(any())).thenReturn(answers.stream().toArray(String[]::new));

		Map<Pattern, String> answerMap = handler.getAnswerMap();
		assertEquals(answers.size(), answerMap.size());
		answerMap.forEach((p, s) -> assertTrue(p.matcher(s).find()));
	}

	@Test
	public void testgetWelcomeMessage() {
		logTest(log, "testgetWelcomeMessage");

		MessageReceivedEvent event = mock(MessageReceivedEvent.class);
		Message message = mock(Message.class);

		when(event.getMessage()).thenReturn(message);
		List<String> answers = ConfigReader.readFile(PROPERTY_FILE).getConfigs(WelcomeMessageHandler.WELCOME_MESSAGE);
		when(configuration.getStringArray(any())).thenReturn(answers.stream().toArray(String[]::new));

		List<String> welcomeMsgs = ConfigReader.readFile(ANSWER_FILE).getConfigs("welcomemessagehandler.answer");
		Map<Pattern, String> answerMap = handler.getAnswerMap();
		welcomeMsgs.forEach(s -> {
			when(message.getContentDisplay()).thenReturn(s);
			String answer = handler.getWelcomeMessage(event).trim();
			assertEquals(answer, getPatternMatch(answerMap, s));
		});
	}

	private String getPatternMatch(Map<Pattern, String> answerMap, String welcomeMsgs) {
		return answerMap.entrySet().stream()//
				.filter(e -> e.getKey().matcher(welcomeMsgs).find())//
				.findFirst()//
				.get()//
				.getValue();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DiscordBotTestLoggerHelper.logAfterClass(log, WelcomeMessageHandlerTest.class);
	}

}
