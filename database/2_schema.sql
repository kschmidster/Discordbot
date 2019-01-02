\echo Start creating Tables

CREATE TABLE Reaction (
	Id INTEGER PRIMARY KEY,
	Kind VARCHAR(10) NOT NULL,
	Min_Permit_Role INTEGER,
	Cooldown_Caller INTEGER NOT NULL
);

CREATE TABLE Message (
	Id INTEGER NOT NULL,
	ReactionId INTEGER NOT NULL,
	Message VARCHAR(255) NOT NULL,
	ChannelId INTEGER NOT NULL,
	PRIMARY KEY (Id, ReactionId)
);

CREATE TABLE TextChannel (
	Id INTEGER PRIMARY KEY,
	Name VARCHAR(25) NOT NULL
);

CREATE TABLE Trigger (
	Id INTEGER NOT NULL,
	ReactionId INTEGER NOT NULL,
	Name VARCHAR(25) NOT NULL,
	PRIMARY KEY (Id, ReactionId)
);

CREATE TABLE Role (
	Id INTEGER PRIMARY KEY,
	Name VARCHAR(25) NOT NULL,
	Position INTEGER NOT NULL
);

\echo Tables successfully created!