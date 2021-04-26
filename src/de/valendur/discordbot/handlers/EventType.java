package de.valendur.discordbot.handlers;

public enum EventType {
	STATUS_UPDATE ("STATUS_UPDATE"),
    MESSAGE_RECEIVED ("MESSAGE_RECEIVED"),
    MESSAGE_CONTENT_RECEIVED ("MESSAGE_CONTENT_RECEIVED"),
    GUILD_JOIN_LEAVE ("GUILD_JOIN_LEAVE"),
    ACTIVITY_UPDATE ("ACTIVITY_UPDATE");

    private final String type;       

    private EventType(String s) {
        type = s;
    }

    public String toString() {
       return this.type;
    }
}
