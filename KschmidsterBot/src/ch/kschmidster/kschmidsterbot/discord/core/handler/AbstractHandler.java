package ch.kschmidster.kschmidsterbot.discord.core.handler;

import net.dv8tion.jda.core.events.Event;

public abstract class AbstractHandler<E extends Event> implements IHandler<E> {

	private final Class<E> genericType;

	protected AbstractHandler(Class<E> clazz) {
		genericType = clazz;
	}

	@Override
	public final Class<E> getGenericType() {
		return genericType;
	}

}
