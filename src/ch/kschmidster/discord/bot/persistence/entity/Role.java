package ch.kschmidster.discord.bot.persistence.entity;

import org.apache.commons.lang3.StringUtils;

public class Role extends NamedEntity {
	private int position;

	public Role() {
		this(-1, StringUtils.EMPTY, -1);
	}

	protected Role(long id, String name, int position) {
		super(id, name);
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
