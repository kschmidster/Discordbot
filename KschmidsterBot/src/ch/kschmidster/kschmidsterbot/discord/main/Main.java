package ch.kschmidster.kschmidsterbot.discord.main;

import ch.kschmidster.kschmidsterbot.discord.handler.LinkPostedHandler;
import ch.kschmidster.kschmidsterbot.discord.handler.NewGuildMemberHandler;
import ch.kschmidster.kschmidsterbot.discord.handler.ShutdownHandler;
import ch.kschmidster.kschmidsterbot.discord.listeners.MyListenerAdapter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main {
	// TODO maybe better handling of those consts
	public static final String ROOT = "kschmidster";

	public static final String TEXT_CHANNEL_UNICORN_TREFF = "unicorn-treff";
	public static final String ROLE_UNICORN = "Unicorn";
	public static final String GUILD_SPACE_UNICORN = "Space Unicorns";

	public static final int LAST_MESSAGES = 10;

	public static void main(String[] args) {
		addListerner(getJda(AccountType.BOT, retriveToken(args)));
	}

	private static String retriveToken(String[] args) {
		if (args == null || args.length == 0) {
			throw new IllegalArgumentException("Discord token as program argument needed!");
		}
		return args[0];
	}

	private static JDA getJda(AccountType accountType, String token) {
		try {
			return new JDABuilder(accountType).setToken(token).buildBlocking();
		} catch (Throwable t) {
			throw new IllegalStateException("Could not initialize JDA");
		}
	}

	private static void addListerner(JDA jda) {
		MyListenerAdapter myListenerAdapter = new MyListenerAdapter();
		jda.addEventListener(myListenerAdapter);
		addHandles(myListenerAdapter);
	}

	private static void addHandles(MyListenerAdapter myListenerAdapter) {
		myListenerAdapter.registerHandle(new NewGuildMemberHandler());
		myListenerAdapter.registerHandle(new LinkPostedHandler());
		myListenerAdapter.registerHandle(new ShutdownHandler());
	}

}
