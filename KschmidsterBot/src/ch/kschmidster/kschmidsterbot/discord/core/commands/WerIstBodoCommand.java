package ch.kschmidster.kschmidsterbot.discord.core.commands;

import java.util.Collection;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.kschmidsterbot.discord.core.command.AbstractCommand;
import ch.kschmidster.kschmidsterbot.discord.core.handler.IHandler;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WerIstBodoCommand extends AbstractCommand<MessageReceivedEvent> {
	private final static Log LOG = LogFactory.getLog(WerIstBodoCommand.class);

	private final static String REGEX = "b+o+d+o+";
	private final static Pattern PATTERN = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);

	public WerIstBodoCommand() {
		super(MessageReceivedEvent.class);
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		LOG.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleCommand(MessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		LOG.info("Handle message " + message);
		if (PATTERN.matcher(message).find() && !message.equals("WER IST BODOO?!?!?!")) {
			LOG.info("Contains bodo");
			// TODO use a variety of responses
			event.getChannel().sendMessage("WER IST BODOO?!?!?!").queue();
		}
	}

}
