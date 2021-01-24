package de.valendur.discordbot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class Utils {
	
	public static List<Member> getUsersInVoiceChannel() {
		ArrayList<Member> userList = new ArrayList<>();
		for (VoiceChannel channel : Bot2.getGuild().getVoiceChannels()) {
			if (channel != null) {
			    List<Member> connectedUsers = channel.getMembers();
			    userList.addAll(connectedUsers.stream().filter(user -> !user.getUser().isBot() && !user.getVoiceState().isDeafened())
			    		.collect(Collectors.toList()));
			}
		}
		return userList;
	}

}
