package ch.kschmidster.discord.bot.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ch.kschmidster.discord.bot.persistence.exception.PropertyNotFoundException;

public class DatabaseConnection {

	protected static final String PROPERTY_JDBC_DRIVER = "jdbc.drivers";
	protected static final String PROPERTY_SYSTEM_DB_CONNECTION = "system.dbconnection";
	protected static final String PROPERTY_SYSTEM_DB_USER = "system.dbuser";
	protected static final String PROPERTY_SYSTEM_DB_PASSWORD = "system.dbpassword";

	private final String DB_URL;
	private final String DB_USER;
	private final String DB_PASSWORD;

	public DatabaseConnection() {
		// make sure the jdbc diver is set
		loadSystemProperty(PROPERTY_JDBC_DRIVER);

		DB_URL= loadSystemProperty(PROPERTY_SYSTEM_DB_CONNECTION);
		DB_USER = loadSystemProperty(PROPERTY_SYSTEM_DB_USER);
		DB_PASSWORD = loadSystemProperty(PROPERTY_SYSTEM_DB_PASSWORD);
	}

	private String loadSystemProperty(String property) {
		String prop = System.getProperty(property);
		if (prop == null) {
			throw new PropertyNotFoundException("System property " + property + " not found!");
		}
		return prop;
	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
	}

}
