package ch.kschmidster.kschmidsterbot.discord.handlers;

import java.util.Collection;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.kschmidsterbot.discord.core.handler.AbstractHandler;
import ch.kschmidster.kschmidsterbot.discord.core.handler.IHandler;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.managers.GuildController;

public class NewGuildMemberJoinHandler extends AbstractHandler<GuildMemberJoinEvent> {
	private final static Log log = LogFactory.getLog(NewGuildMemberJoinHandler.class);

	private final static String PREFIX_CONFIG = "newguildmemberjoin.";
	private final static String INITIAL_ROLE = PREFIX_CONFIG + "initialrole";
	private final static String CHANNEL = PREFIX_CONFIG + "channel";

	public NewGuildMemberJoinHandler(Configuration configuration) {
		super(GuildMemberJoinEvent.class, configuration);
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		log.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleEvent(GuildMemberJoinEvent event) {
		log.info("Handle new member join");
		Guild guild = event.getGuild();
		TextChannel channel = getTextChannel(guild, getConfigString(CHANNEL));
		Member member = event.getMember();
		log.info("Welcome new member: " + member.getEffectiveName());
		channel.sendMessage("A new unicorn is born!! Hello " + member.getAsMention()).queue();
		log.info("Add unicorn role to user");
		new GuildController(guild).addRolesToMember(member, getRole(guild, getConfigString(INITIAL_ROLE))).queue();
	}

	@Override
	public void updateConfigs(ConfigurationEvent configEvent) {
		// TODO Auto-generated method stub

	}

}
