package ch.kschmidster.discord.bot.main;

import static ch.kschmidster.discord.bot.core.test.DiscordBotTestLoggerHelper.logAfterClass;
import static ch.kschmidster.discord.bot.core.test.DiscordBotTestLoggerHelper.logBeforeClass;
import static ch.kschmidster.discord.bot.core.test.DiscordBotTestLoggerHelper.logTest;
import static org.junit.Assert.fail;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.io.File;
import java.nio.file.Paths;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.kschmidster.discord.bot.listeners.DiscordBotListenerAdapter;
import net.dv8tion.jda.core.JDA;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DiscordBotListenerAdapter.class)
public class MainTest {
	private final static Log log = LogFactory.getLog(MainTest.class);

	private static final String WORK_DIR = "user.dir";
	private static final String PATH_IHANDLERS = "src/ch/kschmidster/discord/bot/";
	private static final String PATH_HANDLERS = PATH_IHANDLERS + "handlers";
	private static final String PATH_COMMANDS = PATH_IHANDLERS + "commands";

	private static int NO_OF_HANDLES;
	private static int NO_OF_COMMANDS;

	@BeforeClass
	public static void setUpClass() {
		logBeforeClass(log, MainTest.class);

		NO_OF_HANDLES = getDirectory(PATH_HANDLERS).listFiles().length;
		NO_OF_COMMANDS = getDirectory(PATH_COMMANDS).listFiles().length;
	}

	private static File getDirectory(String path) {
		File file = Paths.get(System.getProperty(WORK_DIR))//
				.resolve(path)//
				.toFile();
		if (!file.exists()) {
			fail();
		}
		return file;
	}

	@Test
	public void testRegisterMyListenerAdapter() {
		logTest(log, "testRegisterMyListenerAdapter");

		JDA jda = mock(JDA.class);
		@SuppressWarnings("unchecked")
		ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder = mock(
				ReloadingFileBasedConfigurationBuilder.class);
		Configuration configuration = mock(Configuration.class);

		Main.initializeDiscordBot(jda, builder, configuration);

		verify(jda).addEventListener(any(DiscordBotListenerAdapter.class));
	}

	@Test
	public void testRegisterHandles() throws Exception {
		logTest(log, "testRegisterHandles");

		DiscordBotListenerAdapter listener = mock(DiscordBotListenerAdapter.class);
		Configuration configuration = mock(Configuration.class);

		Main.registerHandles(listener, configuration);

		verify(listener, times(NO_OF_HANDLES)).registerHandle(any());
	}

	@Test
	public void testRegisterCommands() {
		logTest(log, "testRegisterCommands");

		DiscordBotListenerAdapter listener = mock(DiscordBotListenerAdapter.class);
		Configuration configuration = mock(Configuration.class);

		Main.registerCommands(listener, configuration);

		verify(listener, times(NO_OF_COMMANDS)).registerHandle(any());
	}

	@Test
	public void testCallRegisterHandlesAndCommands() {
		logTest(log, "testCallRegisterHandlesAndCommands");

		mockStatic(DiscordBotListenerAdapter.class);

		JDA jda = mock(JDA.class);
		@SuppressWarnings("unchecked")
		ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder = mock(
				ReloadingFileBasedConfigurationBuilder.class);
		Configuration configuration = mock(Configuration.class);
		DiscordBotListenerAdapter listener = mock(DiscordBotListenerAdapter.class);

		when(DiscordBotListenerAdapter.instance()).thenReturn(listener);

		Main.initializeDiscordBot(jda, builder, configuration);

		verify(listener, times(NO_OF_HANDLES + NO_OF_COMMANDS)).registerHandle(any());
	}

	@AfterClass
	public static void classTearDown() {
		logAfterClass(log, MainTest.class);
	}

}
