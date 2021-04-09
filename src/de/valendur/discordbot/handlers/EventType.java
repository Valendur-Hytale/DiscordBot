package de.valendur.discordbot.handlers;

public enum EventType {
	STATUS_UPDATE ("STATUS_UPDATE"),
    type2 ("Fancy Mode 2"),
    type3 ("Fancy Mode 3");

    private final String type;       

    private EventType(String s) {
        type = s;
    }

    public String toString() {
       return this.type;
    }
}
