package de.valendur.discordbot.handlers;

import de.valendur.discordbot.Bot2;
import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.ReactionRoleConfig;
import de.valendur.discordbot.reactionrole.ReactionMessage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionRoleHandler extends ListenerAdapter {

	
	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent e) {	
		if (e.retrieveUser().complete().isBot()) {
			return;
		} 
		for (final ReactionMessage reactionMessage : getConfig().getReactionMessages()) {
			if (reactionMessage.getMessageID().equalsIgnoreCase(e.getMessageId())) {
				reactionMessage.addedReaction(e.retrieveMember().complete(), e.getReaction());
			}
		}
	}
	
	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent e) {	
		if (e.retrieveUser().complete().isBot()) {
			return;
		} 
		for (final ReactionMessage reactionMessage : getConfig().getReactionMessages()) {
			if (reactionMessage.getMessageID().equalsIgnoreCase(e.getMessageId())) {
				reactionMessage.removedReaction(e.retrieveMember().complete(), e.getReaction());
			}
		}
	}
	
	public void setup(Guild guild) {
		for (final ReactionMessage reactionMessage : getConfig().getReactionMessages()) {
			reactionMessage.setup(guild);
		}
	}
	
	public ReactionRoleConfig getConfig() {
		return (ReactionRoleConfig) Bot2.configHandler.getConfig(ConfigType.REACTION_ROLE_CONFIG);
	}
}
