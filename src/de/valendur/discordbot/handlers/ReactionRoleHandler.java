package de.valendur.discordbot.handlers;

import de.valendur.discordbot.Config;
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
		for (ReactionMessage reactionMessage : Config.REACTION_MESSAGES) {
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
		for (ReactionMessage reactionMessage : Config.REACTION_MESSAGES) {
			if (reactionMessage.getMessageID().equalsIgnoreCase(e.getMessageId())) {
				reactionMessage.removedReaction(e.retrieveMember().complete(), e.getReaction());
			}
		}
	}
	
	
	public void setup(Guild guild) {
		for (ReactionMessage reactionMessage : Config.REACTION_MESSAGES) {
			reactionMessage.setup(guild);
		}
	}
}
