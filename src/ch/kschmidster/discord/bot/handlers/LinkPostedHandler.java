package ch.kschmidster.discord.bot.handlers;

import java.util.Collection;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.UrlValidator;

import ch.kschmidster.discord.bot.core.handler.AbstractHandler;
import ch.kschmidster.discord.bot.core.handler.IHandler;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LinkPostedHandler extends AbstractHandler<MessageReceivedEvent> {
	private final static Log log = LogFactory.getLog(LinkPostedHandler.class);

	private final static String PREFIX_CONFIG = "linkposted.";
	private static final String NOT_ALLOWED_ROLE = PREFIX_CONFIG + "notallowedrole";
	private static final String TEMPORARLY_ALLOWED = PREFIX_CONFIG + "temporarlyallowed";
	private static final String LAST_MESSAGES = PREFIX_CONFIG + "lastmessagestocheck";
	private final static String REPORT_CHANNEL = PREFIX_CONFIG + "reportchannel";
	private final static String ACCEPTED_CHANNEL = PREFIX_CONFIG + "acceptedchannel";

	public LinkPostedHandler(Configuration configuration) {
		super(MessageReceivedEvent.class, configuration);
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		log.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleEvent(MessageReceivedEvent event) {
		String message = getContentDisplay(event);
		log.debug("Handle Message from " + getEffectiveName(event) + ": " + message);
		if (isNotInChannel(event.getChannel(), getConfigString(ACCEPTED_CHANNEL))
				&& (hasRole(event.getMember(), getConfigString(NOT_ALLOWED_ROLE))
						&& !hasRole(event.getMember(), getConfigString(TEMPORARLY_ALLOWED))
						|| hasNoRole(event.getMember()))) {
			log.info("Handle message");
			String content[] = message.split(" ");
			for (String s : content) {
				if (UrlValidator.getInstance().isValid(s)) {
					log.info("Link posted from a Unicorn detected!!! Member: " + event.getMember().getEffectiveName()
							+ " link: " + s);
					log.info("Send the violation to the violation channel");
					sendLinkToViolationChannel(event, s);
					deleteMessage(event, s);
				}
			}
		}
	}

	private boolean isNotInChannel(MessageChannel channel, String channelName) {
		return !channelName.equals(channel.getName());
	}

	private boolean hasNoRole(Member member) {
		return member.getRoles().isEmpty();
	}

	private void sendLinkToViolationChannel(MessageReceivedEvent event, String link) {
		TextChannel channel = getTextChannel(event.getGuild(), getConfigString(REPORT_CHANNEL));
		channel.sendMessage("@everyone " + event.getAuthor() + " just posted this link: " + link).queue();
	}

	private Message getToDeleteMessage(Collection<Message> messages, String link) {
		return messages.stream()//
				.filter(m -> m.getContentDisplay().contains(link))//
				.findFirst()//
				.get();
	}

	private void deleteMessage(MessageReceivedEvent event, String link) {
		log.debug("Deleting message containing link");
		MessageChannel channel = event.getChannel();
		MessageHistory history = new MessageHistory(channel);
		Message message = getToDeleteMessage(history.retrievePast(getConfigInt(LAST_MESSAGES)).complete(), link);
		if (message != null) {
			log.info("Inform user that he is not allowed to post links");
			channel.sendMessage(event.getMember().getAsMention()
					+ " Bitte keine Links posten danke! Als Unicorn hast du noch keine Berechtigung dazu. "
					+ "Frage zuerst einer der Space Mods oder die Space Queen.").queue();
			channel.deleteMessageById(message.getId()).queue();
		}
	}

	@Override
	public void updateConfigs(ConfigurationEvent configEvent) {
		// TODO Auto-generated method stub

	}

}
