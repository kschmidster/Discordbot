package ch.kschmidster.discord.bot.persistence.exception;

public class DatabaseException extends RuntimeException {

	private static final long serialVersionUID = 5671568300431536580L;

	public DatabaseException(String message, Throwable t) {
		super(message, t);
	}

}
