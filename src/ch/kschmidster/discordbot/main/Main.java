package ch.kschmidster.discordbot.main;

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

import ch.kschmidster.discordbot.core.commands.PermitUserCommand;
import ch.kschmidster.discordbot.core.commands.ShutdownCommand;
import ch.kschmidster.discordbot.core.commands.SourceCommand;
import ch.kschmidster.discordbot.core.commands.WerIstBodoCommand;
import ch.kschmidster.discordbot.handlers.BotGotMentionedHandler;
import ch.kschmidster.discordbot.handlers.DiscordOwnerIsStreamingHandler;
import ch.kschmidster.discordbot.handlers.LinkPostedHandler;
import ch.kschmidster.discordbot.handlers.NewGuildMemberJoinHandler;
import ch.kschmidster.discordbot.listeners.MyListenerAdapter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main {
	private static final Log log = LogFactory.getLog(Main.class);

	private static final String CONFIG_PROPERTIES = "config.properties";
	private static final String PREFIX_CONFIG = "bot.";
	private static final String TOKEN = PREFIX_CONFIG + "token";

	public static void main(String[] args) {
		log.info("Initializing automatic config file reloader...");
		ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder = initializeConfigurationLoader();
		Configuration configuration = getConfiguration(builder);
		log.info("Done!");

		log.info("Starting up kschmidsterbot for discord ...");
		initializeDiscordBot(setUpJda(AccountType.BOT, getToken(configuration)), builder, configuration);
		log.info("Kschmidsterbot for discord is up and running :D");
	}

	private static ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> initializeConfigurationLoader() {
		File propertiesFile = getPropertiesFile();
		ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder = //
				new ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)//
						.configure(new Parameters()//
								.fileBased()//
								.setFile(propertiesFile));
		new PeriodicReloadingTrigger(builder.getReloadingController(), null, 1, TimeUnit.MINUTES).start();
		log.debug("Reloader is set");
		return builder;
	}

	private static File getPropertiesFile() {
		log.debug("Checking if config.properties file exists");
		File propertiesFile = new File(CONFIG_PROPERTIES);
		if (!propertiesFile.exists()) {
			RuntimeException noConfigFileException = new IllegalStateException("Discord bot needs a config file!");
			log.fatal("There is no config file!", noConfigFileException);
			throw noConfigFileException;
		}
		log.debug("Found config.properties file!");
		return propertiesFile;
	}

	private static Configuration getConfiguration(
			ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder) {
		try {
			return builder.getConfiguration();
		} catch (ConfigurationException e) {
			RuntimeException noConfigException = new IllegalStateException("Failed to load the configuration");
			log.fatal("Could not load the configuration", noConfigException);
			throw noConfigException;
		}
	}

	private static String getToken(Configuration configuration) {
		log.debug("Retrieving discord token ...");
		String token = configuration.getString(TOKEN);
		if (token == null) {
			IllegalArgumentException exception = new IllegalArgumentException("Discord token as configuration needed!");
			log.fatal("Token not found!", exception);
			throw exception;
		}
		log.debug("Retrieved discord token");
		return token;
	}

	private static JDA setUpJda(AccountType accountType, String token) {
		log.info("Setting up the JDA ...");
		try {
			JDA jda = new JDABuilder(accountType).setToken(token).buildBlocking();
			log.info("Done setting up JDA");
			return jda;
		} catch (Throwable t) {
			String message = "Could not initialize JDA";
			IllegalStateException exception = new IllegalStateException(message);
			log.fatal(message, exception);
			throw exception;
		}
	}

	private static void initializeDiscordBot(JDA jda,
			ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder, Configuration configuration) {
		log.info("Add Listener");
		MyListenerAdapter myListenerAdapter = new MyListenerAdapter();
		myListenerAdapter.registerTo(builder);
		jda.addEventListener(myListenerAdapter);
		registerHandles(myListenerAdapter, configuration);
		registerCommands(myListenerAdapter, configuration);
	}

	private static void registerHandles(MyListenerAdapter myListenerAdapter, Configuration configuration) {
		log.info("Register all handles");
		myListenerAdapter.registerHandle(new NewGuildMemberJoinHandler(configuration));
		myListenerAdapter.registerHandle(new LinkPostedHandler(configuration));
		myListenerAdapter.registerHandle(new DiscordOwnerIsStreamingHandler(configuration));
		myListenerAdapter.registerHandle(new BotGotMentionedHandler(configuration));
	}

	private static void registerCommands(MyListenerAdapter myListenerAdapter, Configuration configuration) {
		log.info("Register all commands");
		myListenerAdapter.registerHandle(new ShutdownCommand(configuration));
		myListenerAdapter.registerHandle(new WerIstBodoCommand(configuration));
		myListenerAdapter.registerHandle(new PermitUserCommand(configuration));
		myListenerAdapter.registerHandle(new SourceCommand(configuration));
	}

}
