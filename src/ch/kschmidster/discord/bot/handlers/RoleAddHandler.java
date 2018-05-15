package ch.kschmidster.discord.bot.handlers;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.discord.bot.core.handler.AbstractHandler;
import ch.kschmidster.discord.bot.core.handler.IHandler;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;

public class RoleAddHandler extends AbstractHandler<GuildMemberRoleAddEvent> {
	private final static Log log = LogFactory.getLog(RoleAddHandler.class);

	private static final String CHANNEL = "newguildmemberjoin.channel";

	private static final int DELAY = 3000;

	public RoleAddHandler(Configuration configuration) {
		super(GuildMemberRoleAddEvent.class, configuration);
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		log.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleEvent(GuildMemberRoleAddEvent event) {
		log.debug("Handle message new role added event");
		// seems like it contains only the new role
		Role newRole = event.getRoles().get(0);
		Role highestRole = getHighestRole(event.getMember().getRoles());

		if (newRole.compareTo(highestRole) == 0) {
			log.info("New role: " + newRole.getName() + " added to " + event.getMember().getEffectiveName());
			log.info("Schedule task to gratulate for the new role");
			scheduleTimerTask(event, newRole);
		}
	}

	private void scheduleTimerTask(GuildMemberRoleAddEvent event, Role newRole) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				Member member = event.getMember();
				List<Role> currentRoles = event.getGuild().getMemberById(member.getUser().getIdLong()).getRoles();
				Role currentHighestRole = getHighestRole(currentRoles);
				if (currentHighestRole.compareTo(newRole) >= 0) {
					congratMember(event, member, newRole);
				}
			}

			private void congratMember(GuildMemberRoleAddEvent event, Member member, Role newRole) {
				TextChannel textChannel = getTextChannel(event.getGuild(), getConfigString(CHANNEL));
				textChannel.sendMessage("Looks like " + member.getAsMention() + " got promoted!!").queue();
				textChannel.sendMessage(member.getAsMention() + " is now a " + newRole.getAsMention()).queue();
			}
		}, DELAY);
	}

	private Role getHighestRole(List<Role> roles) {
		return roles.stream()//
				.max(Comparator.naturalOrder())//
				.get();
	}

	@Override
	public void updateConfigs(ConfigurationEvent configEvent) {
		// TODO Auto-generated method stub

	}

}
