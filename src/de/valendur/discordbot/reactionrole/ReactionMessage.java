package de.valendur.discordbot.reactionrole;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

public class ReactionMessage {
	
	private String channelID;
	private String messageID;
	private boolean exclusive;
	private List<ReactionEmoteRole> emoteRoles = new ArrayList<ReactionEmoteRole>();
	
	public ReactionMessage (String channelID, String messageID, boolean exclusive) {
		this.channelID = channelID;
		this.messageID = messageID;
		this.exclusive = exclusive;
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
	
	public void addedReaction(final Member member, final MessageReaction messageReaction, final MessageReactionAddEvent e) {
		final String emoteID = getIDfromReaction(messageReaction);
		for (ReactionEmoteRole emoteRole : emoteRoles) {
			System.out.println(emoteID + " : " + emoteRole.getEmote());
			if (emoteRole.getEmote().equalsIgnoreCase(emoteID)) {
				emoteRole.addedReaction(member);
			} else {
				System.out.println("Starting removal");
				e.retrieveMessage().queue(message -> {
					System.out.println("Queing removal");
					message.removeReaction(emoteRole.getEmote(), member.getUser()).queue();
				});
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
		message.addReaction("😄").queue();
		for (ReactionEmoteRole emoteRole : emoteRoles) {
			
			message.addReaction(emoteRole.getEmote()).queue();
			emotes.add(emoteRole.getEmote());
			System.out.println("EmOJI: " + emoteRole.getEmote());
		}
		for (MessageReaction reaction : message.getReactions()) {
			final String emoteString = getIDfromReaction(reaction);
			if (!emotes.contains(emoteString)) {
				
				if (reaction.getReactionEmote().isEmoji()) {
					
					message.clearReactions(reaction.getReactionEmote().getEmoji()).queue();
				} else {
					message.clearReactions(reaction.getReactionEmote().getEmote()).queue();
				}
				
			}
		}
		
		
	}
	
	
	private String getIDfromReaction(MessageReaction reaction) {
		if (reaction.getReactionEmote().isEmoji()) {
			System.out.println("is emoji");
			return reaction.getReactionEmote().getEmoji().toLowerCase();
		} else {
			return reaction.getReactionEmote().getEmote().getId();
		}
	}
	
	
	public String getMessageID() {
		return messageID;
	}
	
	public boolean isExclusive() {
		return exclusive;
	}

}
