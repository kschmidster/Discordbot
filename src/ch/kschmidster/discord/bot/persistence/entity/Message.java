package ch.kschmidster.discord.bot.persistence.entity;

import org.apache.commons.lang3.StringUtils;

public class Message extends NamedEntity {
	private TextChannel textChannel;

	public Message() {
		this(-1, StringUtils.EMPTY, null);
	}

	public Message(long id, String name, TextChannel channel) {
		super(id, name);
		this.textChannel = channel;
	}

	public TextChannel getTextChannel() {
		return textChannel;
	}

	public void setTextChannel(TextChannel textChannel) {
		this.textChannel = textChannel;
	}

}
