package ch.kschmidster.kschmidsterbot.discord.core.handler;

import ch.kschmidster.kschmidsterbot.discord.core.command.Command;
import net.dv8tion.jda.core.events.Event;

public abstract class AbstractHandler<E extends Event> implements IHandler {

	public abstract void handleEvent(E event);

	protected boolean isCommand(String commandName, Command command) {
		return command.isCommand(commandName);
	}

}
