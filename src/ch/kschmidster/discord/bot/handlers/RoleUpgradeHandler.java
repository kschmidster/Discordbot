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

	private static final int DELAY = 3000;
	// FIXME issues with the "cache" when roles get removed
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
		// seems like it contains only the new role
		Role newRole = event.getRoles().get(0);
		Role oldRole = getHighestRole(rolesOfMember.get(member.getEffectiveName()));

		if (newRole.compareTo(oldRole) > 0) {
			scheduleTimerTask(event, member, newRole);
		}
	}

	private void scheduleTimerTask(GuildMemberRoleAddEvent event, Member member, Role newRole) {
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				List<Role> currentRoles = event.getGuild().getMemberById(member.getUser().getIdLong()).getRoles();
				Role currentHighestRole = getHighestRole(currentRoles);
				if (currentHighestRole.compareTo(newRole) >= 0) {
					congratMember(event, member, newRole);
				}
				// cache update
				rolesOfMember.put(member.getEffectiveName(), getRoles(event.getGuild(), member));
			}

			private void congratMember(GuildMemberRoleAddEvent event, Member member, Role newRole) {
				TextChannel textChannel = getTextChannel(event.getGuild(),
						getConfigString("newguildmemberjoin.channel"));
				textChannel.sendMessage("Looks like " + member.getAsMention() + " got promoted!!").queue();
				textChannel.sendMessage("Welcome " + member.getAsMention() + " to the " + newRole.getAsMention() + "s")
						.queue();
			}
		}, DELAY);
	}

	private List<Role> getRoles(Guild guild, Member member) {
		return guild.getMemberById(member.getUser().getIdLong()).getRoles();
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
					.filter(g -> g.getName().equals(configuration.getString("roleupgradehandler.guild")))//
					.findFirst()//
					.get();
		}

		private static Map<String, List<Role>> cacheRolesFromMembers(Guild guild) {
			return guild.getMembers().stream()//
					.collect(Collectors.toMap(m1 -> m1.getEffectiveName(), m -> m.getRoles()));
		}

	}

}
