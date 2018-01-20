package ch.kschmidster.kschmidsterbot.discord.core.command;

import ch.kschmidster.kschmidsterbot.discord.core.handler.AbstractHandler;
import net.dv8tion.jda.core.events.Event;

public abstract class AbstractCommand<E extends Event> extends AbstractHandler<E> {

	protected AbstractCommand(Class<E> clazz) {
		super(clazz);
	}

	@Override
	public final void handleEvent(E event) {
		handleCommand(event);
	}

	public abstract void handleCommand(E event);

}
