package ch.kschmidster.discord.bot.persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import ch.kschmidster.discord.bot.persistence.entity.Reaction;
import ch.kschmidster.discord.bot.persistence.exception.DatabaseException;

public class PersistenceService {

	private static final int TRIGGER_PARAMETER = 1;

	private DatabaseConnection dbConnection;
	private EntityBuilder entityBuilder;

	public PersistenceService(DatabaseConnection dbConnection, EntityBuilder entityBuilder) {
		this.dbConnection = dbConnection;
		this.entityBuilder = entityBuilder;
	}

	public static final String SQL_GET_REACTION_SINGLE_TRIGGER;
	public static final String SQL_GET_REACTION_MULTIPLE_TRIGGER;
	static {
		StringBuilder select = new StringBuilder();

		select.append(" SELECT * FROM Reaction reaction ");
		select.append("  LEFT OUTER JOIN Role role ");
		select.append("   ON role.id = reaction.min_permit_role ");
		select.append("  LEFT OUTER JOIN Message message ");
		select.append("   ON message.reactionid = reaction.id ");
		select.append("  LEFT OUTER JOIN TextChannel textchannel ");
		select.append("   ON textchannel.id = message.channelid ");
		select.append("  LEFT OUTER JOIN Trigger trigger ");
		select.append("   ON trigger.reactionid = reaction.id ");

		StringBuilder whereSingle = new StringBuilder(select.toString());
		whereSingle.append(" WHERE trigger.name = ? ");
		SQL_GET_REACTION_SINGLE_TRIGGER = whereSingle.toString();

		StringBuilder whereMultiple = new StringBuilder(select.toString());
		whereMultiple.append(" WHERE trigger.name IN ( ? ) ");
		SQL_GET_REACTION_MULTIPLE_TRIGGER = whereMultiple.toString();
	}

	public Optional<List<Reaction>> reactionFor(String trigger) {
		return executeQuery(Arrays.asList(trigger), SQL_GET_REACTION_SINGLE_TRIGGER);
	}

	public Optional<List<Reaction>> reactionFor(List<String> triggers) {
		return executeQuery(triggers, SQL_GET_REACTION_MULTIPLE_TRIGGER);
	}

	private Optional<List<Reaction>> executeQuery(List<String> triggers, String query) {
		try (Connection connection = dbConnection.getConnection();
				PreparedStatement statement = connection.prepareStatement(query)) {
			setParameter(triggers, statement);
			ResultSet resultSet = statement.executeQuery();

			List<Reaction> reactions = new ArrayList<>();
			while (resultSet.next()) {
				Reaction reaction = entityBuilder.buildReaction(resultSet);
				reactions.add(reaction);
			}

			return Optional.of(reactions);
		} catch (SQLException e) {
			throw new DatabaseException(e.getMessage(), e);
		}
	}

	private void setParameter(List<String> triggers, PreparedStatement statement) throws SQLException {
		if (triggers.size() == 1) {
			statement.setString(TRIGGER_PARAMETER, triggers.get(0));
		}
		statement.setString(TRIGGER_PARAMETER, createTriggersParameter(triggers));
	}

	private String createTriggersParameter(List<String> triggers) {
		StringBuilder sb = new StringBuilder();
		for (String s : triggers) {
			sb.append('\'');
			sb.append(s);
			sb.append('\'');
		}
		return sb.toString();
	}

}
