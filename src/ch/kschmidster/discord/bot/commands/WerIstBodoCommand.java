package ch.kschmidster.discord.bot.commands;

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

public class WerIstBodoCommand extends AbstractCommand<MessageReceivedEvent> {
	private final static Log log = LogFactory.getLog(WerIstBodoCommand.class);

	private static final String PREFIX_CONFIG = "weristbodo.";
	private static final String ANSWERS = PREFIX_CONFIG + "answers";

	private final static String REGEX = "b+o+d+o+";
	private final static Pattern PATTERN = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);

	public WerIstBodoCommand(Configuration configuration) {
		super(MessageReceivedEvent.class, configuration);
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		log.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleCommand(MessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		log.info("Handle message " + message);
		if (PATTERN.matcher(message).find() && isNotAnswer(message, getConfigStringList(ANSWERS))) {
			log.info("Contains bodo");
			List<String> answers = getConfigStringList(ANSWERS);
			event.getChannel().sendMessage(getRandomString(answers)).queue();
		}
	}

	@Override
	public void updateConfigs(ConfigurationEvent configEvent) {
		// TODO Auto-generated method stub

	}

}
