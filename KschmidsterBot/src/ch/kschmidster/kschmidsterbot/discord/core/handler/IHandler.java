package ch.kschmidster.kschmidsterbot.discord.core.handler;

import java.util.Collection;

import net.dv8tion.jda.core.events.Event;

public interface IHandler<E extends Event> {

	void register(Collection<IHandler<? extends Event>> handles);

	Class<E> getGenericType();

	void handleEvent(E event);

}
