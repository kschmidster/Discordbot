package ch.kschmidster.kschmidsterbot.discord.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ch.kschmidster.kschmidsterbot.discord.core.handler.IHandler;
import ch.kschmidster.kschmidsterbot.discord.core.handler.MessageReceivedHandler;
import ch.kschmidster.kschmidsterbot.discord.core.handler.PrivateMessageHandler;
import ch.kschmidster.kschmidsterbot.discord.handler.NewGuildMemberHandler;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public final class MyListenerAdapter extends ListenerAdapter {
	private Collection<IHandler> handles = new ArrayList<>();

	public void registerHandle(IHandler handle) {
		handle.register(handles);
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if (notNull(event.getGuild(), event.getChannel())) {
			getHandlers(event, MessageReceivedHandler.class)//
					.forEach(h -> h.handleMessageReceived(event));
		}
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		getHandlers(event, PrivateMessageHandler.class)//
				.forEach(h -> h.onPrivateMessageReceived(event));
	}

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		if (notNull(event.getGuild(), event.getMember())) {
			getHandlers(event, NewGuildMemberHandler.class)//
					.forEach(h -> h.handleGuildMemberJoin(event));
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
