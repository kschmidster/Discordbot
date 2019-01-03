package ch.kschmidster.discord.bot.persistence.entity;

import org.apache.commons.lang3.StringUtils;

public class Trigger extends NamedEntity {

	public Trigger() {
		this(-1, StringUtils.EMPTY);
	}

	public Trigger(long id, String name) {
		super(id, name);
	}

}
