package ch.kschmidster.kschmidsterbot.discord.core.handler;

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;

public interface GuildMemberJoinHandler extends IHandler {

	void handleGuildMemberJoin(GuildMemberJoinEvent event);

}
