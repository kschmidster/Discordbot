package ch.kschmidster.kschmidsterbot.discord.main;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.reloading.PeriodicReloadingTrigger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.kschmidsterbot.discord.core.commands.ShutdownCommand;
import ch.kschmidster.kschmidsterbot.discord.core.commands.WerIstBodoCommand;
import ch.kschmidster.kschmidsterbot.discord.handlers.DiscordOwnerIsStreamingHandler;
import ch.kschmidster.kschmidsterbot.discord.handlers.LinkPostedHandler;
import ch.kschmidster.kschmidsterbot.discord.handlers.NewGuildMemberJoinHandler;
import ch.kschmidster.kschmidsterbot.discord.listeners.MyListenerAdapter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main {
	private static final Log LOG = LogFactory.getLog(Main.class);

	private static final String CONFIG_PROPERTIES = "config.properties";
	private static final String PREFIX_CONFIG = "bot.";
	private static final String TOKEN = PREFIX_CONFIG + "token";

	public static void main(String[] args) {
		LOG.info("Initializing automatic config file reloader...");
		ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder = initializeConfigurationLoader();
		Configuration configuration = getConfiguration(builder);
		LOG.info("Done!");

		LOG.info("Starting up kschmidsterbot for discord ...");
		initializeDiscordBot(setUpJda(AccountType.BOT, getToken(configuration)), builder, configuration);
		LOG.info("Kschmidsterbot for discord is up and running :D");
	}

	private static ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> initializeConfigurationLoader() {
		File propertiesFile = getPropertiesFile();
		ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder = //
				new ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)//
						.configure(new Parameters()//
								.fileBased()//
								.setFile(propertiesFile));
		new PeriodicReloadingTrigger(builder.getReloadingController(), null, 1, TimeUnit.MINUTES).start();
		LOG.debug("Reloader is set");
		return builder;
	}

	private static File getPropertiesFile() {
		LOG.debug("Checking if config.properties file exists");
		File propertiesFile = new File(CONFIG_PROPERTIES);
		if (!propertiesFile.exists()) {
			RuntimeException noConfigFileException = new IllegalStateException("Discord bot needs a config file!");
			LOG.fatal("There is no config file!", noConfigFileException);
			throw noConfigFileException;
		}
		LOG.debug("Found config.properties file!");
		return propertiesFile;
	}

	private static Configuration getConfiguration(
			ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder) {
		try {
			return builder.getConfiguration();
		} catch (ConfigurationException e) {
			RuntimeException noConfigException = new IllegalStateException("Failed to load the configuration");
			LOG.fatal("Could not load the configuration", noConfigException);
			throw noConfigException;
		}
	}

	private static String getToken(Configuration configuration) {
		LOG.debug("Retrieving discord token ...");
		String token = configuration.getString(TOKEN);
		if (token == null) {
			IllegalArgumentException exception = new IllegalArgumentException("Discord token as configuration needed!");
			LOG.fatal("Token not found!", exception);
			throw exception;
		}
		LOG.debug("Retrieved discord token");
		return token;
	}

	private static JDA setUpJda(AccountType accountType, String token) {
		LOG.info("Setting up the JDA ...");
		try {
			JDA jda = new JDABuilder(accountType).setToken(token).buildBlocking();
			LOG.info("Done setting up JDA");
			return jda;
		} catch (Throwable t) {
			String message = "Could not initialize JDA";
			IllegalStateException exception = new IllegalStateException(message);
			LOG.fatal(message, exception);
			throw exception;
		}
	}

	private static void initializeDiscordBot(JDA jda,
			ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder, Configuration configuration) {
		LOG.info("Add Listener");
		MyListenerAdapter myListenerAdapter = new MyListenerAdapter();
		myListenerAdapter.registerTo(builder);
		jda.addEventListener(myListenerAdapter);
		registerHandles(myListenerAdapter, configuration);
		registerCommands(myListenerAdapter, configuration);
	}

	private static void registerHandles(MyListenerAdapter myListenerAdapter, Configuration configuration) {
		LOG.info("Register all handles");
		myListenerAdapter.registerHandle(new NewGuildMemberJoinHandler(configuration));
		myListenerAdapter.registerHandle(new LinkPostedHandler(configuration));
		myListenerAdapter.registerHandle(new DiscordOwnerIsStreamingHandler(configuration));
	}

	private static void registerCommands(MyListenerAdapter myListenerAdapter, Configuration configuration) {
		LOG.info("Register all commands");
		myListenerAdapter.registerHandle(new ShutdownCommand(configuration));
		myListenerAdapter.registerHandle(new WerIstBodoCommand(configuration));
	}

}
