package ch.kschmidster.kschmidsterbot.discord.handlers;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.kschmidsterbot.discord.core.handler.AbstractHandler;
import ch.kschmidster.kschmidsterbot.discord.core.handler.IHandler;
import ch.kschmidster.kschmidsterbot.discord.main.Main;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.managers.GuildController;

public class NewGuildMemberJoinHandler extends AbstractHandler<GuildMemberJoinEvent> {
	private final static Log LOG = LogFactory.getLog(NewGuildMemberJoinHandler.class);

	public NewGuildMemberJoinHandler() {
		super(GuildMemberJoinEvent.class);
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		LOG.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleEvent(GuildMemberJoinEvent event) {
		LOG.info("Handle new member join");
		Guild guild = event.getGuild();
		TextChannel channel = getTextChannel(guild.getTextChannels(), Main.TEXT_CHANNEL_UNICORN_TREFF);
		Member member = event.getMember();
		LOG.info("Welcome new member: " + member.getEffectiveName());
		channel.sendMessage("A new unicorn is born!! Hello " + member.getAsMention()).queue();
		LOG.info("Add unicorn role to user");
		new GuildController(guild).modifyMemberRoles(member, getUnicornRole(guild.getRoles(), Main.ROLE_UNICORN))
				.queue();
	}

	private Role getUnicornRole(Collection<Role> roles, String roleName) {
		return roles.stream()//
				.filter(r -> r.getName().equals(roleName))//
				.findFirst()//
				.get();
	}

}
