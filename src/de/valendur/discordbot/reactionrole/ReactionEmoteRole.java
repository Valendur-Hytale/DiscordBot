package de.valendur.discordbot.reactionrole;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;

public class ReactionEmoteRole {
	
	private String emote;
	private List<String> roles = new ArrayList<String>();
	
	public ReactionEmoteRole(String emote, List<String> roles) {
		this.emote = emote;
		this.roles = roles;
	}
	
	public ReactionEmoteRole(String emote, String roleID) {
		this.emote = emote;
		addRole(roleID);
	}
	
	public void addRole(String roleID) {
		this.roles.add(roleID);
	}
	
	
	public String getEmote() {
		return emote;
	}
	
	public void addEmoteToMessage(Message message) {
		if (emote.startsWith("u") || emote.startsWith("U") || emote.length() <= 4) {
			 message.addReaction(emote).queue();
		 } else {
			 message.addReaction(message.getGuild().getEmoteById(emote)).queue();
		 }
	}
	
	public void addedReaction(Member member) {
		for (String roleID : roles) {
			member.getGuild().addRoleToMember(member, member.getGuild().getRoleById(roleID)).queue();
		} 
	}
	
	public void removedReaction(Member member) {
		for (String roleID : roles) {
			member.getGuild().removeRoleFromMember(member, member.getGuild().getRoleById(roleID)).queue();;
		} 
	}
}
