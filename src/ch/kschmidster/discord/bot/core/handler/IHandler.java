package ch.kschmidster.discord.bot.core.handler;

import java.util.Collection;

import org.apache.commons.configuration2.event.ConfigurationEvent;

import net.dv8tion.jda.core.events.Event;

public interface IHandler<E extends Event> {

	void register(Collection<IHandler<? extends Event>> handles);

	Class<E> getGenericType();

	void handleEvent(E event);

	void updateConfigs(ConfigurationEvent configEvent);

}
