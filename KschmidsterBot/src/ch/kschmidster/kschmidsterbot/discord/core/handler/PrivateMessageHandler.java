package ch.kschmidster.kschmidsterbot.discord.core.handler;

import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;

public interface PrivateMessageHandler extends IHandler {

	void onPrivateMessageReceived(PrivateMessageReceivedEvent event);

}
