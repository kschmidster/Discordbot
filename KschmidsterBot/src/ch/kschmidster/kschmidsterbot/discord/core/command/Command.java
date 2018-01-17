package ch.kschmidster.kschmidsterbot.discord.core.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public enum Command {

	SHUTDOWN("shutdown");

	private final static Log LOG = LogFactory.getLog(Command.class);

	private final static String PREFIX = "!";
	private final String name;

	private Command(String commandName) {
		name = commandName;
	}

	public boolean isCommand(String commandName) {
		LOG.debug("Checking if " + name + " is same as " + commandName);
		if (commandName != null && commandName.startsWith(PREFIX)) {
			return commandName.substring(1, commandName.length()).equals(name);
		}
		return false;
	}

}