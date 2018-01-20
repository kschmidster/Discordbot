package ch.kschmidster.kschmidsterbot.discord.core.commands;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.kschmidsterbot.discord.core.command.AbstractCommand;
import ch.kschmidster.kschmidsterbot.discord.core.command.Command;
import ch.kschmidster.kschmidsterbot.discord.core.handler.IHandler;
import ch.kschmidster.kschmidsterbot.discord.main.Main;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class ShutdownCommand extends AbstractCommand<PrivateMessageReceivedEvent> {
	private static final Log LOG = LogFactory.getLog(ShutdownCommand.class);

	public ShutdownCommand() {
		super(PrivateMessageReceivedEvent.class);
	}

	private static final String PROPERTY_PASSWORD = "password";

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		LOG.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleCommand(PrivateMessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		LOG.info("Received private message from " + event.getAuthor().getName() + ": " + message);
		// TODO maybe clean up a little..
		String[] split = message.split(" ");
		if (split.length == 2 && Command.SHUTDOWN.isCommand(split[0])//
				&& userIsPermitted(event.getAuthor()) && passIsCorrect(split[1])) {
			LOG.info("Handle message");
			LOG.info("Going to shut me down ...");
			event.getJDA().shutdown();
			LOG.info("Kschmidsterbot says bye bye");
			return;
		}
		LOG.info("Handled private message");
	}

	private boolean userIsPermitted(User user) {
		LOG.debug("Checking if user: " + user.getName() + " is permitted to shut me down");
		return user.getName().equals(Main.ROOT);
	}

	private boolean passIsCorrect(String password) {
		LOG.debug("Checking if the password is correct");
		return password.equals(System.getProperty(PROPERTY_PASSWORD));
	}

}