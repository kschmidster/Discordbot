package ch.kschmidster.discord.bot.persistence.entity;

public class Entity {
	private long id;

	protected Entity(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
