package ch.kschmidster.kschmidsterbot.discord.listeners;

import java.util.Collection;

import org.apache.commons.validator.routines.UrlValidator;

import ch.kschmidster.kschmidsterbot.discord.main.Main;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageHistory;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class LinkPostedListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if(hasRole(event.getMember(), Main.ROLE_UNICORN) || hasNoRole(event.getMember())) {
			String contentDisplay = event.getMessage().getContentDisplay();
			String content[] = contentDisplay.split(" ");
			UrlValidator validator = new UrlValidator();
			for (String s : content) {
				if (validator.isValid(s)) {
					System.out.println("Link posted from a Unicorn detected!!! Member: " + event.getMember().getEffectiveName() + " link: " + s);
					deleteMessage(event, s);
				}
			}
		}
	}

	private boolean hasRole(Member member, String roleName) {
		return member.getRoles().stream()
				.filter(r -> r.getName().equals(roleName))
				.findFirst()
				.isPresent();
	}

	private boolean hasNoRole(Member member) {
		return member.getRoles().isEmpty();
	}

	private Message getToDeleteMessage(Collection<Message> messages, String link) {
		 return messages.stream()
				 .filter(m -> m.getContentDisplay().contains(link))
				 .findFirst()
				 .get();
	}

	private void deleteMessage(MessageReceivedEvent event, String link) {
		MessageChannel channel = event.getChannel();
		MessageHistory history = new MessageHistory(channel);
		Message message = getToDeleteMessage(history.retrievePast(Main.LAST_MESSAGES).complete(), link);
		if (message != null) {
			channel.sendMessage(event.getMember().getAsMention()
					+ " Bitte keine Links posten danke! Als Unicorn hast du noch keine Berechtigung dazu. "
					+ "Frage zuerst einer der Space Mods oder die Space Queen.").queue();
			channel.deleteMessageById(message.getId()).queue();
		}
	}

}
