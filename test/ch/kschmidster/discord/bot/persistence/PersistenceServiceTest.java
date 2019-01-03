package ch.kschmidster.discord.bot.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.kschmidster.discord.bot.persistence.DatabaseConnection;
import ch.kschmidster.discord.bot.persistence.EntityBuilder;
import ch.kschmidster.discord.bot.persistence.PersistenceService;
import ch.kschmidster.discord.bot.persistence.entity.Reaction;

public class PersistenceServiceTest {

	private final static String TRIGGER = "Some trigger";
	private final static List<String> TRIGGERS = Arrays.asList("Some", "realistic", "triggers");
	
	private DatabaseConnection dbConnectionMock;
	private EntityBuilder entityBuilderMock;
	private ResultSet resultSetMock;

	@Before
	public void setUp() throws SQLException {
		// Mocks
		dbConnectionMock = mock(DatabaseConnection.class);
		Connection connectionMock = mock(Connection.class);
		PreparedStatement statementMock = mock(PreparedStatement.class);
		resultSetMock = mock(ResultSet.class);
		entityBuilderMock = mock(EntityBuilder.class);

		// General behavior of the mocks
		when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
		when(connectionMock.prepareStatement(Mockito.anyString())).thenReturn(statementMock);
		when(statementMock.executeQuery()).thenReturn(resultSetMock);
	}

	@Test
	public void testGetZeroReactions() throws Exception {
		// Test case specific behavior
		when(resultSetMock.next()).thenReturn(false);

		PersistenceService service = new PersistenceService(dbConnectionMock, entityBuilderMock);
		Optional<List<Reaction>> reactionOptional = service.reactionFor(TRIGGER);

		validateReactions(reactionOptional, 0);
	}

	@Test
	public void testGetOneReaction() throws Exception {
		// Test case specific behavior
		when(resultSetMock.next()).thenReturn(true).thenReturn(false);
		when(entityBuilderMock.buildReaction(Mockito.same(resultSetMock))).thenReturn(new Reaction());

		PersistenceService service = new PersistenceService(dbConnectionMock, entityBuilderMock);
		Optional<List<Reaction>> reactionOptional = service.reactionFor(TRIGGER);

		validateReactions(reactionOptional, 1);
	}

	@Test
	public void testGetMultipleReactions() throws Exception {
		// Test case specific behavior
		when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
		Reaction reaction = new Reaction();
		when(entityBuilderMock.buildReaction(Mockito.same(resultSetMock))).thenReturn(reaction).thenReturn(reaction)
				.thenReturn(reaction);

		PersistenceService service = new PersistenceService(dbConnectionMock, entityBuilderMock);
		Optional<List<Reaction>> reactionOptional = service.reactionFor(TRIGGER);

		validateReactions(reactionOptional, 3);
	}

	@Test
	public void testGetZeroReactionsForMultipleTriggers() throws Exception {
		// Test case specific behavior
		when(resultSetMock.next()).thenReturn(false);

		PersistenceService service = new PersistenceService(dbConnectionMock, entityBuilderMock);
		Optional<List<Reaction>> reactionOptional = service.reactionFor(TRIGGERS);

		validateReactions(reactionOptional, 0);
	}

	@Test
	public void testGetOneReactionForMultipleTriggers() throws Exception {
		// Test case specific behavior
		when(resultSetMock.next()).thenReturn(true).thenReturn(false);
		when(entityBuilderMock.buildReaction(Mockito.same(resultSetMock))).thenReturn(new Reaction());

		PersistenceService service = new PersistenceService(dbConnectionMock, entityBuilderMock);
		Optional<List<Reaction>> reactionOptional = service.reactionFor(TRIGGERS);

		validateReactions(reactionOptional, 1);
	}
	
	@Test
	public void testGetMultipleReactionsForMultipleTriggers() throws Exception {
		// Test case specific behavior
		when(resultSetMock.next()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
		Reaction reaction = new Reaction();
		when(entityBuilderMock.buildReaction(Mockito.same(resultSetMock))).thenReturn(reaction).thenReturn(reaction)
				.thenReturn(reaction);

		PersistenceService service = new PersistenceService(dbConnectionMock, entityBuilderMock);
		Optional<List<Reaction>> reactionOptional = service.reactionFor(TRIGGERS);

		validateReactions(reactionOptional, 3);
	}
	
	private void validateReactions(Optional<List<Reaction>> reactions, int size) {
		// Must not throw
		List<Reaction> reaction = reactions.get();
		assertNotNull(reaction);
		assertEquals(size, reaction.size());
	}
	
}
