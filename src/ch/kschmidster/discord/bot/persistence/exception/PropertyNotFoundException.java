package ch.kschmidster.discord.bot.persistence.exception;

public class PropertyNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 3838708710389255675L;

	public PropertyNotFoundException(String message) {
		super(message);
	}

}
