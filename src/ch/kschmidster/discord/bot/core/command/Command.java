package ch.kschmidster.discord.bot.core.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public enum Command {

	SHUTDOWN("shutdown"), //
	PERMIT("permit"), //
	SOURCE("source");

	private final static Log log = LogFactory.getLog(Command.class);

	private final static String PREFIX = "!";
	private final String name;

	private Command(String commandName) {
		name = commandName;
	}

	public boolean isCommand(String commandName) {
		log.debug("Checking if " + name + " is same as " + commandName);
		if (commandName != null && commandName.startsWith(PREFIX)) {
			log.debug("Is command " + commandName);
			return commandName.substring(1, commandName.length()).equals(name);
		}
		return false;
	}

}
