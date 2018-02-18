package ch.kschmidster.kschmidsterbot.discord.core.commands;

import java.util.Collection;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.kschmidsterbot.discord.core.command.AbstractCommand;
import ch.kschmidster.kschmidsterbot.discord.core.command.Command;
import ch.kschmidster.kschmidsterbot.discord.core.handler.IHandler;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SourceCommand extends AbstractCommand<MessageReceivedEvent> {
	private static final Log log = LogFactory.getLog(SourceCommand.class);

	private static final String PREFIX_CONFIG = "bot.";
	private static final String SOURCE = PREFIX_CONFIG + "source";

	public SourceCommand(Configuration configuration) {
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

		String[] split = message.split(" ");
		if (split.length > 0 && Command.SOURCE.isCommand(split[0])) {
			event.getChannel().sendMessage("Mein Source Code findet ihr auf " + getConfigString(SOURCE)).queue();
		}
	}

	@Override
	public void updateConfigs(ConfigurationEvent configEvent) {
		// TODO Auto-generated method stub

	}

}
