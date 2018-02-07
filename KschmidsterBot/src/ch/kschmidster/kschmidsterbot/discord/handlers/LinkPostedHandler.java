package ch.kschmidster.kschmidsterbot.discord.handlers;

import java.util.Collection;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.routines.UrlValidator;

import ch.kschmidster.kschmidsterbot.discord.core.handler.AbstractHandler;
import ch.kschmidster.kschmidsterbot.discord.core.handler.IHandler;
import ch.kschmidster.kschmidsterbot.discord.main.Main;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class LinkPostedHandler extends AbstractHandler<MessageReceivedEvent> {
	private final static Log LOG = LogFactory.getLog(LinkPostedHandler.class);

	public LinkPostedHandler(Configuration configuration) {
		super(MessageReceivedEvent.class, configuration);
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		LOG.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleEvent(MessageReceivedEvent event) {
		String message = event.getMessage().getContentDisplay();
		LOG.info("Handle Message from " + event.getAuthor().getName() + ": " + message);
		if (isNotInChannel(event.getChannel(), Main.TEXT_CHANNEL_ZEIT_FUER_CLIPS)
				&& (hasRole(event.getMember(), Main.ROLE_UNICORN) || hasNoRole(event.getMember()))) {
			LOG.info("Handle message");
			String content[] = message.split(" ");
			UrlValidator validator = new UrlValidator();
			for (String s : content) {
				if (validator.isValid(s)) {
					LOG.info("Link posted from a Unicorn detected!!! Member: " + event.getMember().getEffectiveName()
							+ " link: " + s);
					User root = getRootUser(event.getJDA().getUsers());
					LOG.info("Send the violation to root");
					sendLinkToRoot(event, root, s);
					deleteMessage(event, s);
				}
			}
		}
	}

	private boolean isNotInChannel(MessageChannel channel, String channelName) {
		return !channelName.equals(channel.getName());
	}

	private boolean hasRole(Member member, String roleName) {
		return member.getRoles().stream()//
				.filter(r -> r.getName().equals(roleName))//
				.findFirst()//
				.isPresent();
	}

	private boolean hasNoRole(Member member) {
		return member.getRoles().isEmpty();
	}

	private Message getToDeleteMessage(Collection<Message> messages, String link) {
		return messages.stream()//
				.filter(m -> m.getContentDisplay().contains(link))//
				.findFirst()//
				.get();
	}

	private void deleteMessage(MessageReceivedEvent event, String link) {
		LOG.info("Deleting message containing link");
		MessageChannel channel = event.getChannel();
		MessageHistory history = new MessageHistory(channel);
		Message message = getToDeleteMessage(history.retrievePast(Main.LAST_MESSAGES).complete(), link);
		if (message != null) {
			LOG.info("Inform user that he is not allowed to post links");
			channel.sendMessage(event.getMember().getAsMention()
					+ " Bitte keine Links posten danke! Als Unicorn hast du noch keine Berechtigung dazu. "
					+ "Frage zuerst einer der Space Mods oder die Space Queen.").queue();
			channel.deleteMessageById(message.getId()).queue();
		}
	}

	private User getRootUser(Collection<User> users) {
		return users.stream()//
				.filter(u -> u.getName().equals(Main.ROOT))//
				.findFirst()//
				.get();
	}

	private void sendLinkToRoot(MessageReceivedEvent event, User root, String link) {
		root.openPrivateChannel()
		.queue(c -> c.sendMessage(event.getAuthor() + " just posted this link: " + link).queue());
	}

	@Override
	public void updateConfigs(ConfigurationEvent configEvent) {
		// TODO Auto-generated method stub

	}

}
