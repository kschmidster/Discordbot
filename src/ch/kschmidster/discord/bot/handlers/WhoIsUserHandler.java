package ch.kschmidster.discord.bot.handlers;

import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.discord.bot.core.command.AbstractCommand;
import ch.kschmidster.discord.bot.core.handler.IHandler;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WhoIsUserHandler extends AbstractCommand<MessageReceivedEvent> {
	private final static Log log = LogFactory.getLog(WhoIsUserHandler.class);

	private static final String PREFIX_CONFIG = "whoisuser.";
	private static final String USER = PREFIX_CONFIG + "user";
	private static final String ANSWERS = PREFIX_CONFIG + "answers";

	public WhoIsUserHandler(Configuration configuration) {
		super(MessageReceivedEvent.class, configuration);
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		log.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleCommand(MessageReceivedEvent event) {
		String message = getContentDisplay(event);
		log.debug("Handle message " + message);
		if (getPattern().matcher(message).find()) {
			log.info("Contains regex, answer with random predefined answer");
			List<String> answers = getConfigStringList(ANSWERS);
			event.getChannel().sendMessage(getRandomString(answers)).queue();
		}
	}

	private Pattern getPattern() {
		String regex = getConfigString(USER).trim().chars()//
				.collect(StringBuilder::new, (sb, i) -> sb.append((char) i).append('+'), StringBuilder::append)//
				.toString();
		return Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
	}

	@Override
	public void updateConfigs(ConfigurationEvent configEvent) {
		// TODO Auto-generated method stub

	}

}
