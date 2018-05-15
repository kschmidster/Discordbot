package ch.kschmidster.discord.bot.handlers;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.event.ConfigurationEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ch.kschmidster.discord.bot.core.handler.AbstractHandler;
import ch.kschmidster.discord.bot.core.handler.IHandler;
import net.dv8tion.jda.core.events.Event;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class WelcomeMessageHandler extends AbstractHandler<MessageReceivedEvent> {
	private final static Log log = LogFactory.getLog(WelcomeMessageHandler.class);

	private final static String PREFIX_CONFIG = "welcomemessagehandler.";
	protected final static String WELCOME_MESSAGE = PREFIX_CONFIG + "welcomemessage";
	protected final static String CHANNEL = PREFIX_CONFIG + "channel";

	private static final char MANDATORY_CHARACTER = '+';
	private static final String DOUBLED_CHARACTERS = "(.)\\1";
	private static final String SINGLE_CHARACTER = "$1";
	private static final String OPTIONAL_WHITE_SPACE = " *";
	private static final String MADATORY_WHITE_SPACE = " +";

	public WelcomeMessageHandler(Configuration configuration) {
		super(MessageReceivedEvent.class, configuration);
	}

	@Override
	public void register(Collection<IHandler<? extends Event>> handles) {
		log.info("Register " + getClass().getSimpleName());
		handles.add(this);
	}

	@Override
	public void handleEvent(MessageReceivedEvent event) {
		log.debug("Handle message " + getContentDisplay(event));

		if (isInChannel(event, getConfigString(CHANNEL)) && messageContainsWelcomeMessage(event)) {
			log.info(getContentDisplay(event) + " is a welcome message");
			String answer = getWelcomeMessage(event);
			event.getChannel().sendMessage(answer + " " + event.getMember().getAsMention()).queue();
		}
	}

	protected String getWelcomeMessage(MessageReceivedEvent event) {
		return findFirstPattern(event).get().getValue();
	}

	protected boolean isInChannel(MessageReceivedEvent event, String channel) {
		String channelName = event.getChannel().getName();
		boolean inChannel = channelName.equals(channel);
		log.debug("Check if message is in channel " + channelName + " " + inChannel);
		return inChannel;
	}

	protected boolean messageContainsWelcomeMessage(MessageReceivedEvent event) {
		boolean isWelcomeMessage = findFirstPattern(event).isPresent();
		log.debug("Check if message is a welcome message: " + isWelcomeMessage);
		return isWelcomeMessage;
	}

	private Optional<Entry<Pattern, String>> findFirstPattern(MessageReceivedEvent event) {
		return getAnswerMap().entrySet().stream()//
				.filter(e -> e.getKey().matcher(getContentDisplay(event)).find())//
				.findFirst();
	}

	protected Map<Pattern, String> getAnswerMap() {
		return getConfigStringList(WELCOME_MESSAGE).stream()//
				.collect(Collectors.toMap(createPatternFromString(), Function.identity()));//
	}

	private Function<? super String, ? extends Pattern> createPatternFromString() {
		return s -> {
			String pattern = s.replaceAll(DOUBLED_CHARACTERS, SINGLE_CHARACTER).trim().chars()//
					.collect(StringBuilder::new, (sb, i) -> sb.append((char) i).append(MANDATORY_CHARACTER),
							StringBuilder::append)//
					.toString()//
					.replaceAll(MADATORY_WHITE_SPACE, OPTIONAL_WHITE_SPACE);
			return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		};
	}

	@Override
	public void updateConfigs(ConfigurationEvent configEvent) {
		// TODO Auto-generated method stub

	}

}
