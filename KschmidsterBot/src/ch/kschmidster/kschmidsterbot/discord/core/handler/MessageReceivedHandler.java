package ch.kschmidster.kschmidsterbot.discord.core.handler;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface MessageReceivedHandler extends IHandler {

	void handleMessageReceived(MessageReceivedEvent event);

}
