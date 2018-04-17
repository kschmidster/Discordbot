package ch.kschmidster.discord.bot.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.builder.ReloadingFileBasedConfigurationBuilder;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.configuration2.event.EventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.discord.bot.core.handler.IHandler;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.events.user.UserGameUpdateEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public final class DiscordBotListenerAdapter extends ListenerAdapter implements EventListener<ConfigurationEvent> {
	private final static Log log = LogFactory.getLog(DiscordBotListenerAdapter.class);

	private static final DiscordBotListenerAdapter instance = new DiscordBotListenerAdapter();

	private static final String RECEIVED_EVENT = "Received event %s";
	private static final String DONE_INVOKING_HANDLERS = "Done invoking the handlers";
	private static final String INVOKING_HANDLERS = "Invoking the handlers ...";

	private final Collection<IHandler<? extends Event>> handles = new ArrayList<>();

	private DiscordBotListenerAdapter() {
		// no public instantiation, so it is easier for testing
	}

	public static DiscordBotListenerAdapter instance() {
		return instance;
	}

	public void registerHandle(IHandler<? extends Event> handle) {
		handle.register(handles);
	}

	public void registerTo(ReloadingFileBasedConfigurationBuilder<FileBasedConfiguration> builder) {
		// TODO also for ConfigurationEvent.ADD_PROPERTY
		builder.addEventListener(ConfigurationEvent.SET_PROPERTY, this);
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		log.debug(String.format(RECEIVED_EVENT, "MessageReceivedEvent"));
		if (notNull(event.getGuild(), event.getChannel())) {
			log.debug(INVOKING_HANDLERS);
			getHandlers(MessageReceivedEvent.class)//
					.forEach(h -> h.handleEvent(event));
			log.debug(DONE_INVOKING_HANDLERS);
		}
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		log.debug(String.format(RECEIVED_EVENT, "PrivateMessageReceivedEvent"));
		log.debug(INVOKING_HANDLERS);
		getHandlers(PrivateMessageReceivedEvent.class)//
				.forEach(h -> h.handleEvent(event));
		log.debug(DONE_INVOKING_HANDLERS);
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		log.debug(String.format(RECEIVED_EVENT, "GuildMemberJoinEvent"));
		if (notNull(event.getGuild(), event.getMember())) {
			log.debug(INVOKING_HANDLERS);
			getHandlers(GuildMemberJoinEvent.class)//
					.forEach(h -> h.handleEvent(event));
			log.debug(DONE_INVOKING_HANDLERS);
		}
	}

	@Override
	public void onUserGameUpdate(UserGameUpdateEvent event) {
		log.debug(String.format(RECEIVED_EVENT, "UserGameUpdateEvent"));
		log.debug(INVOKING_HANDLERS);
		if (notNull(event.getCurrentGame())) {
			getHandlers(UserGameUpdateEvent.class)//
					.forEach(h -> h.handleEvent(event));
		}
		log.debug(DONE_INVOKING_HANDLERS);
	}

	@Override
	public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
		log.debug(String.format(RECEIVED_EVENT, "GuildMemberRoleAddEvent"));
		log.debug(INVOKING_HANDLERS);
		if (notNull(event.getMember())) {
			getHandlers(GuildMemberRoleAddEvent.class)//
					.forEach(h -> h.handleEvent(event));
		}
		log.debug(DONE_INVOKING_HANDLERS);
	}

	private boolean notNull(Object... objects) {
		return Arrays.stream(objects)//
				.filter(o -> o == null)//
				.collect(Collectors.toList())//
				.isEmpty();
	}

	private <T extends Event> Stream<IHandler<T>> getHandlers(Class<T> clazz) {
		return handles.stream()//
				.filter(h -> clazz.equals(h.getGenericType()))//
				.map(IHandler.class::cast);
	}

	@Override
	public void onEvent(ConfigurationEvent event) {
		handles.forEach(h -> h.updateConfigs(event));
	}

}
