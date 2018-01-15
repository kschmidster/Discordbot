package ch.kschmidster.kschmidsterbot.discord.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.kschmidsterbot.discord.core.handler.IHandler;
import ch.kschmidster.kschmidsterbot.discord.handler.LinkPostedHandler;
import ch.kschmidster.kschmidsterbot.discord.handler.NewGuildMemberJoinHandler;
import ch.kschmidster.kschmidsterbot.discord.handler.ShutdownHandler;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public final class MyListenerAdapter extends ListenerAdapter {
	private final static Log LOG = LogFactory.getLog(MyListenerAdapter.class);

	private static final String RECEIVED_EVENT = "Received event %s";
	private static final String DONE_INVOKING_HANDLERS = "Done invoking the handlers";
	private static final String INVOKING_HANDLERS = "Invoking the handlers ...";

	private Collection<IHandler> handles = new ArrayList<>();

	public void registerHandle(IHandler handle) {
		handle.register(handles);
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		LOG.debug(String.format(RECEIVED_EVENT, "MessageReceivedEvent"));
		if (notNull(event.getGuild(), event.getChannel())) {
			LOG.debug(INVOKING_HANDLERS);
			getHandlers(event, LinkPostedHandler.class)//
					.forEach(h -> h.handleEvent(event));
			LOG.debug(DONE_INVOKING_HANDLERS);
		}
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		LOG.debug(String.format(RECEIVED_EVENT, "PrivateMessageReceivedEvent"));
		LOG.debug(INVOKING_HANDLERS);
		getHandlers(event, ShutdownHandler.class)//
				.forEach(h -> h.handleEvent(event));
		LOG.debug(DONE_INVOKING_HANDLERS);
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		LOG.debug(String.format(RECEIVED_EVENT, "GuildMemberJoinEvent"));
		if (notNull(event.getGuild(), event.getMember())) {
			LOG.debug(INVOKING_HANDLERS);
			getHandlers(event, NewGuildMemberJoinHandler.class)//
					.forEach(h -> h.handleEvent(event));
			LOG.debug(DONE_INVOKING_HANDLERS);
		}
	}

	private boolean notNull(Object... objects) {
		return Arrays.stream(objects)//
				.filter(o -> o == null)//
				.collect(Collectors.toList())//
				.isEmpty();
	}

	private <T extends IHandler> Stream<T> getHandlers(Event event, Class<T> clazz) {
		return handles.stream()//
				.filter(clazz::isInstance)//
				.map(clazz::cast);
	}

}
