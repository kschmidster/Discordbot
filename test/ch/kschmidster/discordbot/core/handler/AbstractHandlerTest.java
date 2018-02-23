package ch.kschmidster.discordbot.core.handler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static ch.kschmidster.discordbot.core.test.DiscordbotTest.logAfterClass;
import static ch.kschmidster.discordbot.core.test.DiscordbotTest.logBeforeClass;
import static ch.kschmidster.discordbot.core.test.DiscordbotTest.logTest;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;

public class AbstractHandlerTest {
	private final static Log log = LogFactory.getLog(AbstractHandlerTest.class);

	private static final String ANSWER_STRING = "42";
	private static final String CONFIGURATION = "config";
	private static final String[] ARRAY = new String[] { "1", "2", "3", "4", "5" };

	private static final int THRESHOLD = 10;

	private final Configuration configuration = mock(Configuration.class);

	// Class to test
	private final AbstractHandler<Event> handler = createAbstractHandler(configuration);

	@BeforeClass
	public static void classSetup() {
		logBeforeClass(log, AbstractHandlerTest.class);
	}

	@Test
	public void testGetTextChannel1() {
		logTest(log, "testGetTextChannel1");

		Guild guild = mock(Guild.class);
		TextChannel tc1 = mockTextChannel("TextChannel1");
		TextChannel tc2 = mockTextChannel("TextChannel2");
		TextChannel tc3 = mockTextChannel("TextChannel3");
		List<TextChannel> channels = Arrays.asList(tc1, tc2, tc3);
		when(guild.getTextChannels()).thenReturn(channels);

		assertEquals(handler.getTextChannel(guild, tc1.getName()), tc1);
		assertEquals(handler.getTextChannel(guild, tc2.getName()), tc2);
		assertEquals(handler.getTextChannel(guild, tc3.getName()), tc3);
	}

	private TextChannel mockTextChannel(String name) {
		TextChannel textChannel = mock(TextChannel.class);
		when(textChannel.getName()).thenReturn(name);
		return textChannel;
	}

	@Test(expected = NoSuchElementException.class)
	public void testGetTextChannel2() {
		logTest(log, "testGetTextChannel2");

		Guild guild = mock(Guild.class);
		TextChannel tc1 = mockTextChannel("TextChannel1");
		TextChannel tc2 = mockTextChannel("TextChannel2");
		TextChannel tc3 = mockTextChannel("TextChannel3");
		List<TextChannel> channels = Arrays.asList(tc1, tc2, tc3);
		when(guild.getTextChannels()).thenReturn(channels);

		handler.getTextChannel(guild, ANSWER_STRING);
	}

	@Test
	public void testHasRole1() {
		logTest(log, "testHasRole1");

		Member member = mock(Member.class);
		Role role1 = mockRole("Role1");
		Role role2 = mockRole("Role2");
		Role role3 = mockRole("Role3");
		List<Role> list = Arrays.asList(role1, role2, role3);
		when(member.getRoles()).thenReturn(list);

		assertTrue(handler.hasRole(member, role1.getName()));
		assertTrue(handler.hasRole(member, role2.getName()));
		assertTrue(handler.hasRole(member, role3.getName()));
	}

	private Role mockRole(String name) {
		Role role = mock(Role.class);
		when(role.getName()).thenReturn(name);
		return role;
	}

	@Test
	public void testHasRole2() {
		logTest(log, "testHasRole2");

		Member member = mock(Member.class);
		Role role1 = mockRole("Role1");
		Role role2 = mockRole("Role2");
		Role role3 = mockRole("Role3");
		List<Role> list = Arrays.asList(role1, role2, role3);
		when(member.getRoles()).thenReturn(list);

		assertFalse(handler.hasRole(member, ANSWER_STRING));
	}

	@Test
	public void testGetRole1() {
		logTest(log, "testGetRole1");

		Guild guild = mock(Guild.class);
		Role role1 = mockRole("Role1");
		Role role2 = mockRole("Role2");
		Role role3 = mockRole("Role3");
		when(guild.getRoles()).thenReturn(Arrays.asList(role1, role2, role3));

		assertEquals(handler.getRole(guild, role1.getName()), role1);
		assertEquals(handler.getRole(guild, role2.getName()), role2);
		assertEquals(handler.getRole(guild, role3.getName()), role3);
	}

	@Test(expected = NoSuchElementException.class)
	public void testGetRole2() {
		logTest(log, "testGetRole2");

		Guild guild = mock(Guild.class);
		Role role1 = mockRole("Role1");
		Role role2 = mockRole("Role2");
		Role role3 = mockRole("Role3");
		when(guild.getRoles()).thenReturn(Arrays.asList(role1, role2, role3));

		handler.getRole(guild, ANSWER_STRING);
	}

	@Test
	public void testGetConfigStringList() {
		logTest(log, "testGetConfigStringList");

		when(configuration.getStringArray(any(String.class))).thenReturn(ARRAY);

		List<String> config = handler.getConfigStringList(CONFIGURATION);
		assertEquals(config, Arrays.asList(ARRAY));
	}

	@Test
	public void testGetRandomString() {
		logTest(log, "testGetRandomString");

		List<String> list = Arrays.asList("1", "2");
		String config = handler.getRandomString(list);

		assertTrue(list.contains(config));
		for (int i = 0; i < THRESHOLD; i++) {
			if (!config.equals(handler.getRandomString(list))) {
				return;
			}
		}
		fail();
	}

	@Test
	public void testGetConfigString() throws Exception {
		logTest(log, "testGetConfigString");

		when(configuration.getString(any(String.class))).thenReturn(ANSWER_STRING);
		String config = handler.getConfigString(CONFIGURATION);
		assertEquals(config, ANSWER_STRING);
	}

	@Test
	public void testGetConfigInt() throws Exception {
		logTest(log, "testGetConfigInt");

		when(configuration.getInt(any(String.class))).thenReturn(42);
		int config = handler.getConfigInt(CONFIGURATION);
		assertEquals(config, 42);
	}

	private static AbstractHandler<Event> createAbstractHandler(Configuration config) {
		// Just an empty implementation which gets tested in the concrete class
		return new AbstractHandler<Event>(Event.class, config) {
			@Override
			public void updateConfigs(ConfigurationEvent configEvent) {
			}

			@Override
			public void register(Collection<IHandler<? extends Event>> handles) {
			}

			@Override
			public void handleEvent(Event event) {
			}
		};
	}

	@AfterClass
	public static void classTearDown() {
		logAfterClass(log, AbstractHandlerTest.class);
	}

}
