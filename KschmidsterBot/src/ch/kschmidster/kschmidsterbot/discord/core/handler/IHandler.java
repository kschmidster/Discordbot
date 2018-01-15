package ch.kschmidster.kschmidsterbot.discord.core.handler;

import java.util.Collection;

public interface IHandler {

	void register(Collection<IHandler> handles);

}
