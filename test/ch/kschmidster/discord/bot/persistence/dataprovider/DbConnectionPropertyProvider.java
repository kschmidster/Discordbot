package ch.kschmidster.discord.bot.persistence.dataprovider;

import ch.kschmidster.discord.bot.persistence.DatabaseConnection;

public class DbConnectionPropertyProvider extends DatabaseConnection {

	public static String getJdbcDriverPropertyName() {
		return PROPERTY_JDBC_DRIVER;
	}

	public static String getDbConnectionPropertyName() {
		return PROPERTY_SYSTEM_DB_CONNECTION;
	}

	public static String getDbUserPropertyName() {
		return PROPERTY_SYSTEM_DB_USER;
	}

	public static String getDbPasswordPropertyName() {
		return PROPERTY_SYSTEM_DB_PASSWORD;
	}

}
