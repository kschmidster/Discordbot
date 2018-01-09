package ch.kschmidster.kschmidsterbot.discord.handler;

import java.util.Collection;

import ch.kschmidster.kschmidsterbot.discord.core.command.Command;
import ch.kschmidster.kschmidsterbot.discord.core.handler.IHandler;
import ch.kschmidster.kschmidsterbot.discord.core.handler.PrivateMessageHandler;
import ch.kschmidster.kschmidsterbot.discord.main.Main;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public class ShutdownHandler implements PrivateMessageHandler {

	private static final String PROPERTY_PASSWORD = "password";

	@Override
	public void register(Collection<IHandler> handles) {
		handles.add(this);
	}

	@Override
	public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
		// TODO maybe clean up a little..
		String[] split = event.getMessage().getContentDisplay().split(" ");
		if (split.length == 2 && isCommand(split[0], Command.SHUTDOWN)//
				&& userIsPermitted(event.getAuthor()) && passIsCorrect(split[1])) {
			event.getJDA().shutdown();
			System.out.println("Shutting down...");
		}
	}

	private boolean userIsPermitted(User user) {
		return user.getName().equals(Main.ROOT);
	}

	private boolean passIsCorrect(String password) {
		return password.equals(System.getProperty(PROPERTY_PASSWORD));
	}

}
