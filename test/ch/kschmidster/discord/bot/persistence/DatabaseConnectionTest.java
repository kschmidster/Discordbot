package ch.kschmidster.discord.bot.persistence;

import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import ch.kschmidster.discord.bot.persistence.DatabaseConnection;
import ch.kschmidster.discord.bot.persistence.dataprovider.DbConnectionPropertyProvider;
import ch.kschmidster.discord.bot.persistence.exception.PropertyNotFoundException;

public class DatabaseConnectionTest {

	@Rule
	public ExpectedException exception = ExpectedException.none();

	private static final String JDBC_DRIVER = DbConnectionPropertyProvider.getJdbcDriverPropertyName();
	private static final String SYSTEM_DB_CONNECTION = DbConnectionPropertyProvider
			.getDbConnectionPropertyName();
	private static final String SYSTEM_DB_USER = DbConnectionPropertyProvider.getDbUserPropertyName();
	private static final String SYSTEM_DB_PASSWORD = DbConnectionPropertyProvider.getDbPasswordPropertyName();

	private static final String PROPERTY_SET = "Property set";

	@Test
	public void testConstructDatabaseConnection() {
		setProperties(Arrays.asList(JDBC_DRIVER, SYSTEM_DB_CONNECTION, SYSTEM_DB_USER, SYSTEM_DB_PASSWORD));

		assertNotNull(new DatabaseConnection());
	}

	@Test
	public void testConstructDbConnectionWithoutJdbcDriver() {
		setProperties(Arrays.asList(SYSTEM_DB_CONNECTION, SYSTEM_DB_USER, SYSTEM_DB_PASSWORD));

		exception.expect(PropertyNotFoundException.class);
		exception.expectMessage(JDBC_DRIVER);

		assertNotNull(new DatabaseConnection());
	}

	@Test
	public void testConstructDbConnectionWithoutDbConnection() {
		setProperties(Arrays.asList(JDBC_DRIVER, SYSTEM_DB_USER, SYSTEM_DB_PASSWORD));

		exception.expect(PropertyNotFoundException.class);
		exception.expectMessage(SYSTEM_DB_CONNECTION);

		assertNotNull(new DatabaseConnection());
	}

	@Test
	public void testConstructDbConnectionWithoutDbUser() {
		setProperties(Arrays.asList(JDBC_DRIVER, SYSTEM_DB_CONNECTION, SYSTEM_DB_PASSWORD));

		exception.expect(PropertyNotFoundException.class);
		exception.expectMessage(SYSTEM_DB_USER);

		assertNotNull(new DatabaseConnection());
	}

	@Test
	public void testConstructDbConnectionWithoutDbPassword() {
		setProperties(Arrays.asList(JDBC_DRIVER, SYSTEM_DB_CONNECTION, SYSTEM_DB_USER));

		exception.expect(PropertyNotFoundException.class);
		exception.expectMessage(SYSTEM_DB_PASSWORD);

		assertNotNull(new DatabaseConnection());
	}

	private void setProperties(List<String> properties) {
		properties.forEach(property -> System.setProperty(property, PROPERTY_SET));
	}

	@After
	public void cleanUp() {
		Arrays.asList(JDBC_DRIVER, SYSTEM_DB_CONNECTION, SYSTEM_DB_USER, SYSTEM_DB_PASSWORD)//
				.forEach(System::clearProperty);
	}

}
