\echo Start setting the constraints

\echo Setting constraints for table Reaction
ALTER TABLE Reaction
ADD CONSTRAINT fk_Role
	FOREIGN KEY (Min_Permit_Role) REFERENCES Role (Id);

\echo Done setting constraints for Reaction


\echo Setting constraints for table Message
ALTER TABLE Message
ADD CONSTRAINT fk_Reaction
	FOREIGN KEY (ReactionId) REFERENCES Reaction (Id);

ALTER TABLE Message
ADD CONSTRAINT fk_TextChannel
	FOREIGN KEY (ChannelId) REFERENCES TextChannel (Id);

\echo Done setting constraints for table Message


\echo No settings constraints for table TextChannel

\echo Done setting constraints for table TextChannel


\echo Setting constraints for table Trigger
ALTER TABLE Trigger
ADD CONSTRAINT fk_Reaction
	FOREIGN KEY (ReactionId) REFERENCES Reaction (Id);

\echo Done setting constraints for table Trigger


\echo No settings constraints for table Role

\echo Done setting constraints for table Role


\echo Constraits successfuly created!