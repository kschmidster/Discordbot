package ch.kschmidster.discord.bot.listeners;

import static ch.kschmidster.discord.bot.core.test.DiscordBotTestLoggerHelper.logAfterClass;
import static ch.kschmidster.discord.bot.core.test.DiscordBotTestLoggerHelper.logBeforeClass;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import ch.kschmidster.discord.bot.core.handler.IHandler;;

public class DiscordBotListenerAdapterTest {
	private final static Log log = LogFactory.getLog(DiscordBotListenerAdapterTest.class);

	private final DiscordBotListenerAdapter listener = DiscordBotListenerAdapter.instance();

	@BeforeClass
	public static void setUpClass() {
		logBeforeClass(log, DiscordBotListenerAdapterTest.class);
	}

	@Test
	public void testRegisterHandle() {
		IHandler<?> handler = mock(IHandler.class);

		listener.registerHandle(handler);

		verify(handler).register(any());
	}

	@AfterClass
	public static void tearDownClass() {
		logAfterClass(log, DiscordBotListenerAdapterTest.class);
	}

}
