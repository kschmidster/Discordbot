package ch.kschmidster.discord.bot.commands;

import java.util.Collection;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.discord.bot.core.command.AbstractCommand;
import ch.kschmidster.discord.bot.core.command.Command;
import ch.kschmidster.discord.bot.core.handler.IHandler;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class ShutdownCommand extends AbstractCommand<PrivateMessageReceivedEvent> {
	private static final Log log = LogFactory.getLog(ShutdownCommand.class);

	private static final String PREFIX_CONFIG = "bot.";
	private static final String ROOT = PREFIX_CONFIG + "root";
	private static final String PASSWORD = PREFIX_CONFIG + "password";

	public ShutdownCommand(Configuration configuration) {
		super(PrivateMessageReceivedEvent.class, configuration);
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		log.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleCommand(PrivateMessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		log.debug("Received private message from " + event.getAuthor().getName() + ": " + message);

		String[] split = message.split(" ");
		if (split.length > 1 && Command.SHUTDOWN.isCommand(split[0])//
				&& userIsPermitted(event.getAuthor()) && passIsCorrect(split[1])) {
			log.info("Going to shut me down ...");
			event.getJDA().shutdown();
			log.info("Kschmidsterbot says bye bye");
			return;
		}
	}

	private boolean userIsPermitted(User user) {
		log.debug("Checking if user: " + user.getName() + " is permitted to shut me down");
		return user.getName().equals(getConfigString(ROOT));
	}

	private boolean passIsCorrect(String password) {
		log.debug("Checking if the password is correct");
		return password.equals(getConfigString(PASSWORD));
	}

	@Override
	public void updateConfigs(ConfigurationEvent configEvent) {
		// TODO Auto-generated method stub

	}

}
