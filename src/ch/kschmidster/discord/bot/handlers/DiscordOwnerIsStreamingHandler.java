package ch.kschmidster.discord.bot.handlers;

import java.util.Collection;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.discord.bot.core.handler.AbstractHandler;
import ch.kschmidster.discord.bot.core.handler.IHandler;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.user.UserGameUpdateEvent;

public class DiscordOwnerIsStreamingHandler extends AbstractHandler<UserGameUpdateEvent> {
	private final static Log log = LogFactory.getLog(DiscordOwnerIsStreamingHandler.class);

	private final static String PREFIX_CONFIG = "discordownerisstreaming.";
	private static final String DISCORD_OWNER = PREFIX_CONFIG + "discordOwner";
	private final static String CHANNEL = PREFIX_CONFIG + "channel";

	private static GameType state = GameType.DEFAULT;

	public DiscordOwnerIsStreamingHandler(Configuration configuration) {
		super(UserGameUpdateEvent.class, configuration);
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		log.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleEvent(UserGameUpdateEvent event) {
		log.debug("Handle game update");
		Game currentGame = event.getCurrentGame();
		if (!alreadyStreaming() && GameType.STREAMING == currentGame.getType()//
				&& isDiscordOwner(event)) {
			log.info("Discord owner is streaming");
			TextChannel channel = getTextChannel(event.getGuild(), getConfigString(CHANNEL));

			channel.sendMessage("OMG!!!").queue();
			channel.sendMessage(
					"@everyone " + event.getMember().getEffectiveName() + " hat gerade ihren Stream gestartet!!!")
					.queue();
		}
		if (isDiscordOwner(event) && state != currentGame.getType()) {
			log.debug(
					"Set new discord owner is streaming state, was: " + state + " change to: " + currentGame.getType());
			state = currentGame.getType();
		}
	}

	private boolean alreadyStreaming() {
		return GameType.STREAMING == state;
	}

	private boolean isDiscordOwner(UserGameUpdateEvent event) {
		log.debug("Check if user: " + event.getMember().getEffectiveName() + " is discord owner");
		return getConfigString(DISCORD_OWNER)//
				.equals(event.getMember().getEffectiveName());
	}

	@Override
	public void updateConfigs(ConfigurationEvent configEvent) {
		// TODO Auto-generated method stub

	}

}
