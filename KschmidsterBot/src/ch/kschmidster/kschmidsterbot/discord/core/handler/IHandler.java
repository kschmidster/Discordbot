package ch.kschmidster.kschmidsterbot.discord.core.handler;

import java.util.Collection;

import ch.kschmidster.kschmidsterbot.discord.core.command.Command;

public interface IHandler {

	void register(Collection<IHandler> handles);

	default boolean isCommand(String commandName, Command command) {
		return command.isCommand(commandName);
	}

}
