package de.valendur.discordbot.configs;

import java.util.ArrayList;
import java.util.List;

import de.valendur.discordbot.reactionrole.ReactionEmoteRole;
import de.valendur.discordbot.reactionrole.ReactionMessage;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;

public class ReactionEmoteRoleConfig extends GenericConfig {
	
	private List<ReactionMessage> REACTION_MESSAGES = new ArrayList<ReactionMessage>();
	
	public ReactionEmoteRoleConfig(ConfigType type) {
		super(type);
	}

	@Override
	public void load() {
		final JSONObject config = readConfig();
		JSONArray messages = config.getJSONArray("MESSAGES");
		
		for (int i = 0; i < messages.length(); i++) {
			JSONObject message = messages.getJSONObject(i);
			
			ReactionMessage reactionMessage = new ReactionMessage(message.getString("CHANNEL_ID"), message.getString("MESSAGE_ID"), message.getBoolean("EXCLUSIVE"));
					
			JSONArray emoteRoles = message.getJSONArray("EMOTE_ROLES");
			for (int ii = 0; ii < emoteRoles.length(); ii++) {
				JSONObject emoteRole = emoteRoles.getJSONObject(ii);
				
				@SuppressWarnings("unchecked")
				ReactionEmoteRole reactionEmoteRole = new ReactionEmoteRole(emoteRole.getString("EMOTE_ID"), (List<String>) emoteRole.getJSONArray("ROLE_IDS").toList());
				
				reactionMessage.addEmoteRole(reactionEmoteRole);
			}
			
			REACTION_MESSAGES.add(reactionMessage);
		}
	}
	
	public List<ReactionMessage> getReactionMessages() {
		return REACTION_MESSAGES;
	}

}
