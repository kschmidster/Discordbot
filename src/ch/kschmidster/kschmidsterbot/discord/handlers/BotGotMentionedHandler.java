package ch.kschmidster.kschmidsterbot.discord.handlers;

import java.util.Collection;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.kschmidsterbot.discord.core.handler.AbstractHandler;
import ch.kschmidster.kschmidsterbot.discord.core.handler.IHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BotGotMentionedHandler extends AbstractHandler<MessageReceivedEvent> {
	private static final Log log = LogFactory.getLog(BotGotMentionedHandler.class);

	private static final String PREFIX_CONFIG = "bot.";
	private static final String BOT_NAME = PREFIX_CONFIG + "username";
	private static final String ANSWERS = PREFIX_CONFIG + "answers";

	public BotGotMentionedHandler(Configuration configuration) {
		super(MessageReceivedEvent.class, configuration);
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		log.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleEvent(MessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		log.info("Handle message " + message);

		if (message.contains(getMember(event.getGuild(), getConfigString(BOT_NAME)).getEffectiveName())) {
			event.getChannel().sendMessage(getPreparedRandomAnswer(event)).queue();
		}
	}

	private Member getMember(Guild guild, String member) {
		return guild.getMembers().stream()//
				.filter(m -> m.getEffectiveName().contains(member))//
				.findFirst()//
				.get();
	}

	private String getPreparedRandomAnswer(MessageReceivedEvent event) {
		String answer = getRandomString(getConfigStringList(ANSWERS));
		if (answer.contains("$")) {
			return answer.replace("$", event.getMember().getAsMention());
		}
		return answer;
	}

	@Override
	public void updateConfigs(ConfigurationEvent configEvent) {
		// TODO Auto-generated method stub

	}

}
