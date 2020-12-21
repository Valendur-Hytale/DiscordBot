package de.valendur.discordbot.configs;

import java.util.ArrayList;
import java.util.List;

import de.valendur.discordbot.reactionrole.ReactionMessage;
import kong.unirest.json.JSONObject;

public class ReactionRoleConfig extends GenericConfig {
	
	private List<ReactionMessage> REACTION_MESSAGES = new ArrayList<ReactionMessage>();
	
	public ReactionRoleConfig(ConfigType type) {
		super(type);
	}

	@Override
	public void load() {
		
	}
	
	public List<ReactionMessage> getReactionMessages() {
		return REACTION_MESSAGES;
	}

}
