package ch.kschmidster.discord.bot.persistence.entity;

import java.util.Collections;
import java.util.List;

public class Reaction extends Entity {
	private ReactionKind kind;
	private Role minPermitRole;
	private List<Trigger> triggers;
	private List<Message> messages;
	private long cooldown;

	public Reaction() {
		this(-1, ReactionKind.NONE, null, Collections.emptyList(), Collections.emptyList(), -1);
	}

	public Reaction(long id, ReactionKind kind, Role minPermitRolem, List<Trigger> triggers, List<Message> messages,
			long cooldown) {
		super(id);
		this.kind = kind;
		this.minPermitRole = minPermitRolem;
		this.triggers = triggers;
		this.messages = messages;
		this.cooldown = cooldown;
	}

	public ReactionKind getKind() {
		return kind;
	}

	public void setKind(ReactionKind kind) {
		this.kind = kind;
	}

	public Role getMinPermitRole() {
		return minPermitRole;
	}

	public void setMinPermitRole(Role minPermitRole) {
		this.minPermitRole = minPermitRole;
	}

	public List<Trigger> getTriggers() {
		return triggers;
	}

	public void setTriggers(List<Trigger> triggers) {
		this.triggers = triggers;
	}

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}

	public long getCooldown() {
		return cooldown;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}

}
