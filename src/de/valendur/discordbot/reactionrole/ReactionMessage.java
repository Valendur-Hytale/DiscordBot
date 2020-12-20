package de.valendur.discordbot.reactionrole;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;

public class ReactionMessage {
	
	private String channelID;
	private String messageID;
	private List<ReactionEmoteRole> emoteRoles = new ArrayList<ReactionEmoteRole>();
	
	public ReactionMessage (String channelID, String messageID) {
		this.channelID = channelID;
		this.messageID = messageID;
	}
	
	
	public void addEmoteRole(ReactionEmoteRole emoteRole) {
		this.emoteRoles.add(emoteRole);
	}
	
	public void addedReaction(final Member member, final MessageReaction messageReaction) {
		final String emoteID = getIDfromReaction(messageReaction);
		for (ReactionEmoteRole emoteRole : emoteRoles) {
			if (emoteRole.getEmote().equalsIgnoreCase(emoteID)) {
				emoteRole.addedReaction(member);
			}
		} 
	}
	
	public void removedReaction(final Member member, final MessageReaction messageReaction) {
		final String emoteID = getIDfromReaction(messageReaction);
		for (ReactionEmoteRole emoteRole : emoteRoles) {
			if (emoteRole.getEmote().equalsIgnoreCase(emoteID)) {
				emoteRole.removedReaction(member);
			}
		} 
	}
	
	public void setup(Guild guild) {
		TextChannel channel = guild.getTextChannelById(channelID);
		Message message = channel.retrieveMessageById(messageID).complete();
		List<String> emotes = new ArrayList<String>();
		for (ReactionEmoteRole emoteRole : emoteRoles) {
			message.addReaction(emoteRole.getEmote()).queue();
			emotes.add(emoteRole.getEmote());
		}
		for (MessageReaction reaction : message.getReactions()) {
			final String emoteString = getIDfromReaction(reaction);
			if (!emotes.contains(emoteString)) {
				message.clearReactions(emoteString).queue();
			}
		}
		
		
	}
	
	
	private String getIDfromReaction(MessageReaction reaction) {
		if (reaction.getReactionEmote().isEmoji()) {
			return reaction.getReactionEmote().getEmoji().toLowerCase();
		} else {
			return reaction.getReactionEmote().getEmote().getId();
		}
	}
	
	
	public String getMessageID() {
		return messageID;
	}

}
