package ch.kschmidster.discord.bot.core.handler;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class AbstractHandler<E extends Event> implements IHandler<E> {
	private final static Log log = LogFactory.getLog(AbstractHandler.class);

	private final Class<E> genericType;
	private final Configuration configuration;

	protected AbstractHandler(Class<E> clazz, Configuration config) {
		genericType = clazz;
		configuration = config;
	}

	@Override
	public final Class<E> getGenericType() {
		return genericType;
	}

	protected TextChannel getTextChannel(Guild guild, String channelName) {
		return guild.getTextChannels().stream()//
				.filter(tc -> tc.getName().equals(channelName))//
				.findFirst()//
				.get();
	}

	protected boolean hasRole(Member member, String roleName) {
		return member.getRoles().stream()//
				.filter(r -> r.getName().equals(roleName))//
				.findFirst()//
				.isPresent();
	}

	protected Role getRole(Guild guild, String roleName) {
		return guild.getRoles().stream()//
				.filter(r -> r.getName().equals(roleName))//
				.findFirst()//
				.get();
	}

	protected String getContentDisplay(MessageReceivedEvent event) {
		return event.getMessage().getContentDisplay();
	}

	protected String getEffectiveName(MessageReceivedEvent event) {
		return event.getMember().getEffectiveName();
	}

	protected List<String> getConfigStringList(String configuration) {
		return Arrays.asList(getConfiguration().getStringArray(configuration));
	}

	protected String getRandomString(List<String> list) {
		return list.get((int) (Math.random() * list.size()));
	}

	protected String getConfigString(String configuration) {
		return getConfiguration().getString(configuration);
	}

	protected int getConfigInt(String configuration) {
		return getConfiguration().getInt(configuration);
	}

	private Configuration getConfiguration() {
		if (configuration == null) {
			RuntimeException noConfigException = new IllegalStateException("The configuration is not yet set");
			log.error("Tried to access the configuration, but was not set yet", noConfigException);
			throw noConfigException;
		}
		return configuration;
	}

}
