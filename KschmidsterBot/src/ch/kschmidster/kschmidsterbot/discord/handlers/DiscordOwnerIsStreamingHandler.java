package ch.kschmidster.kschmidsterbot.discord.handlers;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.kschmidsterbot.discord.core.handler.AbstractHandler;
import ch.kschmidster.kschmidsterbot.discord.core.handler.IHandler;
import ch.kschmidster.kschmidsterbot.discord.main.Main;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Game.GameType;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.user.UserGameUpdateEvent;

public class DiscordOwnerIsStreamingHandler extends AbstractHandler<UserGameUpdateEvent> {
	private final static Log LOG = LogFactory.getLog(DiscordOwnerIsStreamingHandler.class);

	public DiscordOwnerIsStreamingHandler() {
		super(UserGameUpdateEvent.class);
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		LOG.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleEvent(UserGameUpdateEvent event) {
		LOG.debug("Handle game update");
		Game currentGame = event.getCurrentGame();

		if (GameType.STREAMING.equals(currentGame.getType())//
				&& isDiscordOwner(event)) {
			LOG.info("Discord owner is streaming");
			TextChannel channel = getTextChannel(event.getGuild().getTextChannels(), Main.TEXT_ANKUENDIGUNGEN);

			channel.sendMessage("OMG!!!").queue();
			channel.sendMessage(event.getMember().getEffectiveName() + " hat gerade ihren Stream gestartet!!!").queue();
			channel.sendMessage("Sie spielt " + event.getCurrentGame().getName()).queue();
		}
	}

	private boolean isDiscordOwner(UserGameUpdateEvent event) {
		LOG.debug("Check if user: " + event.getMember().getEffectiveName() + " is discord owner");
		return System.getProperty("discordOwner")//
				.equals(event.getMember().getEffectiveName());
	}

}
