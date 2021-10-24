package de.valendur.discordbot.configs;

import javax.annotation.Nullable;

public enum ConfigType {
	REACTION_ROLE_CONFIG("Reaction Role Config", "reactionrole.json"),
    BASE_CONFIG("Basis Config", "config.json"),
    TOKEN_CONFIG("Token Config", "token.json"),
    LEVELING_CONFIG("Level Config", "levelling.json"),
    DB_CONFIG("Backend Config", "db.json"),
    SECURITY_CONFIG("Security Config", "security.json"),
    LEVEL_ROLE_CONFIG("Level Role Config", "levelrole.json");

    private String name;
    private String fileName;

    ConfigType(String name, String fileName) {
        this.name = name;
        this.fileName = fileName;
    }

    public String getText() {
        return name;
    }
    
    public String getFileName() {
    	return fileName;
    }

    @Nullable
    public static ConfigType fromString(String text) {
        for (ConfigType b : ConfigType.values()) {
            if (b.name.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

}
