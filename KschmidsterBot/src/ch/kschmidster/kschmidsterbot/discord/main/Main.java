package ch.kschmidster.kschmidsterbot.discord.main;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.kschmidsterbot.discord.core.commands.ShutdownCommand;
import ch.kschmidster.kschmidsterbot.discord.core.commands.WerIstBodoCommand;
import ch.kschmidster.kschmidsterbot.discord.handlers.LinkPostedHandler;
import ch.kschmidster.kschmidsterbot.discord.handlers.DiscordOwnerIsStreamingHandler;
import ch.kschmidster.kschmidsterbot.discord.handlers.NewGuildMemberJoinHandler;
import ch.kschmidster.kschmidsterbot.discord.listeners.MyListenerAdapter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main {
	private static final Log LOG = LogFactory.getLog(Main.class);

	// TODO maybe better handling of those consts
	public static final String ROOT = "kschmidster";

	public static final String TEXT_CHANNEL_UNICORN_TREFF = "unicorn-treff";
	public static final String TEXT_CHANNEL_ZEIT_FUER_CLIPS = "zeitfuerclips";
	public static final String TEXT_ANKUENDIGUNGEN = "ankuendigungen";
	public static final String ROLE_UNICORN = "Unicorn";
	public static final String GUILD_SPACE_UNICORN = "Space Unicorns";

	public static final int LAST_MESSAGES = 10;

	public static void main(String[] args) {
		LOG.info("Starting up kschmidsterbot for discord ...");
		addListerner(setUpJda(AccountType.BOT, retriveToken(args)));
		LOG.info("Kschmidsterbot for discord is up and running :D");
	}

	private static String retriveToken(String[] args) {
		LOG.debug("Retrieving discord token ...");
		if (args == null || args.length == 0) {
			IllegalArgumentException exception = new IllegalArgumentException("Discord token as program argument needed!");
			LOG.fatal("Token not found!", exception);
			throw exception;
		}
		LOG.debug("Retrieved discord token");
		return args[0];
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

	private static void addListerner(JDA jda) {
		LOG.info("Add Listener");
		MyListenerAdapter myListenerAdapter = new MyListenerAdapter();
		jda.addEventListener(myListenerAdapter);
		registerHandles(myListenerAdapter);
		registerCommands(myListenerAdapter);
	}

	private static void registerHandles(MyListenerAdapter myListenerAdapter) {
		LOG.info("Register all handles");
		myListenerAdapter.registerHandle(new NewGuildMemberJoinHandler());
		myListenerAdapter.registerHandle(new LinkPostedHandler());
		myListenerAdapter.registerHandle(new DiscordOwnerIsStreamingHandler());
	}

	private static void registerCommands(MyListenerAdapter myListenerAdapter) {
		LOG.info("Register all commands");
		myListenerAdapter.registerHandle(new ShutdownCommand());
		myListenerAdapter.registerHandle(new WerIstBodoCommand());
	}

}
