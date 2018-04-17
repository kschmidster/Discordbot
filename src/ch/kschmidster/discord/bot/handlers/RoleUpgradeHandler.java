package ch.kschmidster.discord.bot.handlers;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.discord.bot.core.handler.AbstractHandler;
import ch.kschmidster.discord.bot.core.handler.IHandler;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberRoleAddEvent;

public class RoleUpgradeHandler extends AbstractHandler<GuildMemberRoleAddEvent> {
	private final static Log log = LogFactory.getLog(RoleUpgradeHandler.class);

	private final Map<String, List<Role>> rolesOfMember;

	private RoleUpgradeHandler(Map<String, List<Role>> memberRoles, Configuration configuration) {
		super(GuildMemberRoleAddEvent.class, configuration);
		rolesOfMember = memberRoles;
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		log.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleEvent(GuildMemberRoleAddEvent event) {
		Member member = event.getMember();
		List<Role> roles = event.getRoles();
		List<Role> newRoles = rolesOfMember.get(member.getEffectiveName());

		if (newRoles.size() > roles.size() && addedRoleIsBetter(roles, newRoles)) {
			scheduleTimerTask(event, member, newRoles);
		}
	}

	private void scheduleTimerTask(GuildMemberRoleAddEvent event, Member member, List<Role> newRoles) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				List<Role> currentRoles = event.getGuild().getMemberById(member.getUser().getIdLong()).getRoles();
				Role currentHighestRole = getHighestRole(currentRoles);
				Role newHighestRole = getHighestRole(newRoles);
				if (currentHighestRole.compareTo(newHighestRole) >= 0) {
					TextChannel textChannel = getTextChannel(event.getGuild(),
							getConfigString("newguildmemberjoin.channel"));
					textChannel.sendMessage("Looks like " + member.getAsMention() + " got promoted!!").queue();
					textChannel
							.sendMessage(
									"Welcome " + member.getAsMention() + " to the " + newHighestRole.getAsMention())
							.queue();
				}
			}
		}, 3000);
	}

	private boolean addedRoleIsBetter(List<Role> roles, List<Role> newRoles) {
		return getHighestRole(newRoles).compareTo(getHighestRole(roles)) > 0;
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

	public static class RoleUpgradeHandlerCreator {

		private RoleUpgradeHandlerCreator() {
			// no public instantiation
		}

		public static RoleUpgradeHandler create(JDA jda, Configuration configuration) {
			Map<String, List<Role>> memberRoles = cacheRolesFromMembers(getGuild(jda, configuration));
			return new RoleUpgradeHandler(memberRoles, configuration);
		}

		private static Guild getGuild(JDA jda, Configuration configuration) {
			return jda.getGuilds().stream()//
					.filter(g -> g.getId().equals(configuration.getString("newguildmemberjoin.channel")))//
					.findFirst()//
					.get();
		}

		private static Map<String, List<Role>> cacheRolesFromMembers(Guild guild) {
			return guild.getMembers().stream()//
					.collect(Collectors.toMap(m1 -> m1.getEffectiveName(), m -> m.getRoles()));
		}

	}

}
