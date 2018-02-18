package ch.kschmidster.kschmidsterbot.discord.core.commands;

import java.util.Collection;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.kschmidsterbot.discord.core.command.AbstractCommand;
import ch.kschmidster.kschmidsterbot.discord.core.command.Command;
import ch.kschmidster.kschmidsterbot.discord.core.handler.IHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.GuildController;

public class PermitUserCommand extends AbstractCommand<MessageReceivedEvent> {
	private static final Log log = LogFactory.getLog(PermitUserCommand.class);

	private static final int MILLISECONDS = 1000;

	private static final String PREFIX_CONFIG = "permituser.";
	private static final String PERMITTED_USER = PREFIX_CONFIG + "permitteduser";
	private static final String PERMITTED_ROLE = PREFIX_CONFIG + "permitrole";
	private static final String PERMITTED_TIME = PREFIX_CONFIG + "permittime";

	public PermitUserCommand(Configuration configuration) {
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
		Member member = event.getMember();
		if (split.length == 2 && Command.PERMIT.isCommand(split[0])) {
			if (hasRole(member, getConfigString(PERMITTED_USER))) {
				log.info("Going to add the permit role to user: " + split[1]);
				Guild guild = event.getGuild();
				GuildController guildController = new GuildController(guild);

				Member memberToPermit = getMemberToPermit(split[1], guild);
				if (memberToPermit != null) {
					giveUserPermitRole(event, guildController, memberToPermit);
				} else {
					log.info("Could not find user: " + split[1]);
					event.getChannel().sendMessage("Sorry, " + member.getAsMention() + " aber ich konnte " + split[1]
							+ " nicht in der Datenbank finden!").queue();
				}
			} else {
				event.getChannel().sendMessage(event.getMember().getAsMention()
						+ " du bist nicht berechtig für diesen Command, das können nur Mods.").queue();
			}
		}
	}

	private Member getMemberToPermit(String member, Guild guild) {
		String username = member.contains("@") ? member.replace("@", "") : member;
		Optional<Member> memberOptional = guild.getMembers().stream()//
				.filter(m -> m.getEffectiveName().contains(username))//
				.findFirst();
		return memberOptional.isPresent() ? memberOptional.get() : null;
	}

	private void giveUserPermitRole(MessageReceivedEvent event, GuildController guildController,
			Member memberToPermit) {
		Role permittedRole = getRole(event.getGuild(), getConfigString(PERMITTED_ROLE));
		guildController.addRolesToMember(memberToPermit, permittedRole).queue();

		MessageChannel channel = event.getChannel();
		scheduleRemovePermitRole(guildController, memberToPermit, permittedRole, channel);
		log.info("User: " + memberToPermit.getEffectiveName() + " is now permitted to post links or pictures for "
				+ getConfigInt(PERMITTED_TIME) + "s");
		channel.sendMessage(memberToPermit.getAsMention() + " du kannst jetzt innerhalb von "
				+ getConfigInt(PERMITTED_TIME) + "s einen Link oder Bild posten!").queue();
	}

	private void scheduleRemovePermitRole(GuildController guildController, Member tempPermittedMember,
			Role permittedRole, MessageChannel channel) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				guildController.removeSingleRoleFromMember(tempPermittedMember, permittedRole).queue();
				channel.sendMessage(tempPermittedMember.getAsMention()
						+ " du deine Berechtigung um Links zu posten ist abgelaufen.").queue();
			}
		}, getConfigInt(PERMITTED_TIME) * MILLISECONDS);
	}

	@Override
	public void updateConfigs(ConfigurationEvent configEvent) {
		// TODO Auto-generated method stub

	}

}
