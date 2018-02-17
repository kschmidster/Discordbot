package ch.kschmidster.kschmidsterbot.discord.core.handler;

import java.util.Collection;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.Event;

public abstract class AbstractHandler<E extends Event> implements IHandler<E> {
	private final static Log LOG = LogFactory.getLog(AbstractHandler.class);

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

	protected TextChannel getTextChannel(Collection<TextChannel> textChannels, String channelName) {
		return textChannels.stream()//
				.filter(tc -> tc.getName().equals(channelName))//
				.findFirst()//
				.get();
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
			LOG.error("Tried to access the configuration, but was not set yet", noConfigException);
			throw noConfigException;
		}
		return configuration;
	}

}
