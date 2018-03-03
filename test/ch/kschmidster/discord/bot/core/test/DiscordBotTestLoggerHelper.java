package ch.kschmidster.discord.bot.core.test;

import org.apache.commons.logging.Log;

public class DiscordBotTestLoggerHelper {

	public static void logBeforeClass(Log log, Class<?> clazz) {
		log.info("Starting tests of " + clazz.getSimpleName());
	}

	public static void logTest(Log log, String methodName) {
		log.info("Running test " + methodName);
	}

	public static void logAfterClass(Log log, Class<?> clazz) {
		log.info("Finished tests of " + clazz.getSimpleName());
	}

}
