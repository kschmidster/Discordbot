package ch.kschmidster.kschmidsterbot.discord.core.command;

public enum Command {

	SHUTDOWN("shutdown");

	private final static String PREFIX = "!";
	private final String name;

	private Command(String commandName) {
		name = commandName;
	}

	public boolean isCommand(String commandName) {
		if (commandName != null && commandName.startsWith(PREFIX)) {
			return commandName.substring(1, commandName.length()).equals(name);
		}
		return false;
	}

}
