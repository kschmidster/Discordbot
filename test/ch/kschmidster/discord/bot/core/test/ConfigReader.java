package ch.kschmidster.discord.bot.core.test;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public final class ConfigReader {

	private final List<String> linesOfFile;

	public static final String NO_VALUE = "NO_VALUE";

	private ConfigReader(List<String> lines) {
		linesOfFile = lines;
	}

	public String getConfig(String config) {
		return getConfigs(config).stream()//
				.findFirst()//
				.orElse(NO_VALUE);
	}

	public List<String> getConfigs(String answer) {
		return linesOfFile.stream()//
				.filter(s -> s.contains(answer))//
				.map(s -> s.substring(s.indexOf('=') + 1, s.length()).trim())//
				.collect(Collectors.toList());
	}

	public static ConfigReader readFile(String fileName) {
		File fileAnswers = new File(fileName);
		if (!fileAnswers.exists()) {
			fail("Config file not found");
		}
		return ConfigReader.readFile(fileAnswers);
	}

	private static ConfigReader readFile(File fileAnswers) {
		try (Scanner scanner = new Scanner(fileAnswers)) {
			List<String> answers = new ArrayList<>();
			while (scanner.hasNextLine()) {
				answers.add(scanner.nextLine());
			}
			return new ConfigReader(answers);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Failed to read config file", e);
		}
	}

}
