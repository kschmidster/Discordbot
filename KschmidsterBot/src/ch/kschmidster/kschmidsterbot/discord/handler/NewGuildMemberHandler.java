package ch.kschmidster.kschmidsterbot.discord.handler;

import java.util.Collection;

import ch.kschmidster.kschmidsterbot.discord.core.handler.GuildMemberJoinHandler;
import ch.kschmidster.kschmidsterbot.discord.core.handler.IHandler;
import ch.kschmidster.kschmidsterbot.discord.main.Main;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.managers.GuildController;

public class NewGuildMemberHandler implements GuildMemberJoinHandler {

	@Override
	public void register(Collection<IHandler> handles) {
		handles.add(this);
	}

	@Override
	public void handleGuildMemberJoin(GuildMemberJoinEvent event) {
		Guild guild = event.getGuild();
		TextChannel channel = getTextChannel(guild.getTextChannels(), Main.TEXT_CHANNEL_UNICORN_TREFF);
		Member member = event.getMember();
		channel.sendMessage("A new unicorn is born!! Hello " + member.getAsMention()).queue();
		new GuildController(guild).modifyMemberRoles(member, getUnicornRole(guild.getRoles(), Main.ROLE_UNICORN)).queue();
	}

	private TextChannel getTextChannel(Collection<TextChannel> textChannels, String channelName) {
		return textChannels.stream()
				.filter(tc -> tc.getName().equals(channelName))
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
