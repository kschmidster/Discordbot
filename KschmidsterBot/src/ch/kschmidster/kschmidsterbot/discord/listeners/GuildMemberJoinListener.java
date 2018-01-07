package ch.kschmidster.kschmidsterbot.discord.listeners;

import java.util.Collection;

import ch.kschmidster.kschmidsterbot.discord.main.Main;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.GuildController;

public class GuildMemberJoinListener extends ListenerAdapter {

	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		JDA jda = event.getJDA();
		Guild guild = getGuild(jda.getGuilds(), Main.GUILD_SPACE_UNICORN);
		TextChannel channel = getTextChannel(jda.getTextChannels(), Main.TEXT_CHANNEL_UNICORN_TREFF);
		Member member = getNewMember(guild);
		channel.sendMessage("A new unicorn is born!! Hello " + member.getAsMention()).queue();
		new GuildController(guild).modifyMemberRoles(member, getUnicornRole(guild.getRoles(), Main.ROLE_UNICORN)).queue();
	}

	private Guild getGuild(Collection<Guild> guilds, String guildName) {
		return guilds.stream()
				.filter(g -> g.getName().equals(guildName))
				.findFirst()
				.get();
	}

	private TextChannel getTextChannel(Collection<TextChannel> textChannels, String channelName) {
		return textChannels.stream()
				.filter(tc -> tc.getName().equals(channelName))
				.findFirst()
				.get();
	}

	private Member getNewMember(Guild guild) {
		return guild.getMembers().stream()
				.filter(m -> m.getRoles().isEmpty())
				.findFirst()
				.get();
	}

	private Role getUnicornRole(Collection<Role> roles, String roleName) {
		return roles.stream()
				.filter(r -> r.getName().equals(roleName))
				.findFirst()
				.get();
	}

}
