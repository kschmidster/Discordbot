package ch.kschmidster.discord.bot.persistence.entity;

import org.apache.commons.lang3.StringUtils;

public class TextChannel extends NamedEntity {

	public TextChannel() {
		this(-1, StringUtils.EMPTY);
	}

	public TextChannel(long id, String name) {
		super(id, name);
	}

}
