package de.valendur.discordbot.configs;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import kong.unirest.json.JSONObject;

public abstract class GenericConfig {
	
	private ConfigType type;
	
	public GenericConfig(ConfigType type) {
		this.type = type;
	}
	
	public JSONObject readConfig() {
		try {
			return new JSONObject(new String(Files.readAllBytes(Paths.get(type.getFileName()))));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String readConfigString() {
		try {
			return new String(Files.readAllBytes(Paths.get(type.getFileName())));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public abstract void load();
	
	public void writeConfig(String configJSON) {
		try (FileWriter file = new FileWriter(type.getFileName())) {
            file.write(configJSON);
            file.flush();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public ConfigType getType() {
		return type;
	}
	
	
	
}
