package de.valendur.discordbot.handlers;

import java.util.ArrayList;
import java.util.List;

import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.GenericConfig;

public class ConfigHandler {
	
	private List<GenericConfig> configs = new ArrayList<GenericConfig>();

	
	public void addConfig(GenericConfig config) {
		configs.add(config);
	}
	
	public void load() {
		for (final GenericConfig config : configs) {
			config.load();
		}
	}
	
	public GenericConfig getConfig(ConfigType type) {
		for (final GenericConfig config : configs) {
			if (config.getType() == type) {
				return config;
			}
		}
		throw new IllegalArgumentException("There is a shitty coder among us who cant define configs");
	}
	
}
