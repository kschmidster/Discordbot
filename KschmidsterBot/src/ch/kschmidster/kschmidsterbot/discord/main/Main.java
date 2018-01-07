package ch.kschmidster.kschmidsterbot.discord.main;

import ch.kschmidster.kschmidsterbot.discord.listeners.GuildMemberJoinListener;
import ch.kschmidster.kschmidsterbot.discord.listeners.LinkPostedListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main extends Thread {
	// TODO maybe better handling of those consts
	public static final String TEXT_CHANNEL_UNICORN_TREFF = "unicorn-treff";
	public static final String ROLE_UNICORN = "Unicorn";
	public static final String GUILD_SPACE_UNICORN = "Space Unicorns";

	public static final int LAST_MESSAGES = 10;

	public static void main(String[] args) {
		JDA jda = getJda(AccountType.BOT, retriveToken(args));
		jda.addEventListener(new GuildMemberJoinListener());
		jda.addEventListener(new LinkPostedListener());
	}

	private static String retriveToken(String[] args) {
		if(args == null || args.length == 0) {
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

}
