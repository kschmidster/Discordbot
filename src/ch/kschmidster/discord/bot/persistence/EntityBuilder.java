package ch.kschmidster.discord.bot.persistence;

import java.sql.ResultSet;

import ch.kschmidster.discord.bot.persistence.entity.Reaction;

public class EntityBuilder {

	public Reaction buildReaction(ResultSet resultSet) {
		// TODO implementation
		// Result set should cols, starting at 1
		// (reaction) id, kind, , min_per_role, cooldown,
		// (role) id, name, position,
		// (message) id, reactid, message, channelid,
		// (channel) id, name,
		// (trigger) id, reactid, name
		return null;
	}

}
