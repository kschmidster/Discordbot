package ch.kschmidster.kschmidsterbot.discord.core.command;

import org.apache.commons.configuration2.Configuration;

import ch.kschmidster.kschmidsterbot.discord.core.handler.AbstractHandler;
import net.dv8tion.jda.core.events.Event;

public abstract class AbstractCommand<E extends Event> extends AbstractHandler<E> {

	protected AbstractCommand(Class<E> clazz, Configuration configuration) {
		super(clazz, configuration);
	}

	@Override
	public final void handleEvent(E event) {
		handleCommand(event);
	}

	public abstract void handleCommand(E event);

}
