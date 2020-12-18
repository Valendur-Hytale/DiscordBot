package de.valendur.discordbot;

import java.io.FileReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.security.auth.login.LoginException;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Bot extends ListenerAdapter {
	
	public static String prefix = "!";
	
	public static JDA jda;
	
	
	
	public static String USER_ID;
	
	public static List<String> rolesOnJoin = new ArrayList<String>(); 
	
	public static LinkedHashMap<String, LinkedHashMap<String, String>> messageEmoteRole = new LinkedHashMap<String, LinkedHashMap<String,String>>();
	
	public static List<MessageLocation> mlList = new ArrayList<MessageLocation>();
	
	public static boolean migrate;
	
	//public static HashMap<String, String> emoteToRole = new HashMap<String, String>();
	
	 public static void main(String[] args ) {
		// parseConfig();
	        try {
	        	
	            jda = new JDABuilder(Config.BOT_TOKEN)
	                    .addEventListeners(new Bot())  
	                    .build();
	            jda.awaitReady(); 
	            System.out.println("Finished Building JDA!");
	            reactToMessage();
	            //migrateRoles();
	            
	        }
	        catch (LoginException e){

	            e.printStackTrace();
	        }
	        catch (InterruptedException e) {

	            e.printStackTrace();
	        } catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	 
	 
	 public static void migrateRoles() throws InterruptedException {
		 Guild guild = jda.getGuildById(Config.GUILD_ID);
		 
		 for (Member member : guild.getMembers()) {
			 System.out.println(member.getEffectiveName());
			 System.out.println(member.getId());
			 for (String role : rolesOnJoin) {
				 
				 Thread.sleep(50);
				 Role test = guild.getRoleById(role);
				 System.out.println("adding role: " + test.getName());
				 guild.addRoleToMember(member.getId(), test).complete();System.out.println("adding role");
			 }
		 }
	 }
	
	 
	 
	 @Override
	 public void onMessageReactionAdd(MessageReactionAddEvent e) {
		 if (e.getUser().isBot()) {
			 return;
		 }
		 
		 
		 if (messageEmoteRole.containsKey(e.getMessageId())) {
			 HashMap<String, String> emoteRole = messageEmoteRole.get(e.getMessageId());
			 
			 String id = "";
				boolean isUnicode = false;
				if (e.getReactionEmote().isEmoji()) {
					id = e.getReactionEmote().getEmoji();
					//id = unicodeToHex(id);
					isUnicode = true;
				} else {
					id = e.getReactionEmote().getEmote().getId();
				}
			 
				System.out.println("added reaciton " + id);
				System.out.println("added " + id.toUpperCase());
			 if (emoteRole.containsKey(id)) {
				 System.out.println("is contained");
				 e.getGuild().addRoleToMember(e.getMember(), e.getGuild().getRoleById(emoteRole.get(id.toUpperCase()))).queue();
			 }
		 }
		 
	 }
	 
	 
	 public static String unicodeToHex(String s) {
		 StringBuilder sb = new StringBuilder();

		 for (int i = 0; i < s.length(); i++) {
		   if (Character.isSurrogate(s.charAt(i))) {
		     Integer res = Character.codePointAt(s, i);
		     i++;
		     sb.append("U+" + Integer.toHexString(res).toUpperCase());
		   } else {
		     sb.append(s.charAt(i));
		   }
		 }

		 System.out.println(sb.toString());
		 return sb.toString();
	 }
	 
	 
	 @Override
	 public void onMessageReactionRemove(MessageReactionRemoveEvent e) {		 
		 if (e.getUser().isBot()) {
			 return;
		 }
		 
		 if (messageEmoteRole.containsKey(e.getMessageId())) {
			 HashMap<String, String> emoteRole = messageEmoteRole.get(e.getMessageId());
			 
			 String id = "";
				boolean isUnicode = false;
				if (e.getReactionEmote().isEmoji()) {
					id = e.getReactionEmote().getEmoji().toLowerCase();
					//id = unicodeToHex(id);
					isUnicode = true;
				} else {
					id = e.getReactionEmote().getEmote().getId();
				}
				System.out.println("reaction remove id + " + id);
			 if (emoteRole.containsKey(id)) {
				 e.getGuild().removeRoleFromMember(e.getMember(), e.getGuild().getRoleById(emoteRole.get(id.toUpperCase()))).queue();
			 }
		 }
	 }
	 
	 
	 @Override
	 public void onGuildMemberJoin(GuildMemberJoinEvent e) {
		 Guild guild = jda.getGuildById(Config.GUILD_ID);
		 
		 for (String role : rolesOnJoin) {
			 guild.addRoleToMember(e.getMember(), guild.getRoleById(role)).queue();
		 }
		 
	 }
	 
	 @Override
	 public void onPrivateMessageReceived(PrivateMessageReceivedEvent e) {
		 if (!e.getAuthor().getId().equals(USER_ID)) {return;}
		 
		 e.getChannel().sendMessage(Config.parseConfig()).queue();
		 try {
			reactToMessage();
		} catch (InterruptedException | ExecutionException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 
		 
	 }
	 
	 
	 public static String checkForSpecificShit(String id) {
		 
		 if (id.contains("✅")) {
			 id = id.replace("✅", "");
		 }
		 
		 
		 return id;
	 }
	 
	 public static void reactToMessage() throws InterruptedException, ExecutionException {
		 
		 int i = 0;
		 
		 for (String messageID : messageEmoteRole.keySet()) {
			 MessageLocation location = mlList.get(i);
			 
			 LinkedHashMap<String,String> emoteRole = messageEmoteRole.get(messageID);
			 
			 System.out.println("guild " + location.guildID + " channel " + location.channelID + " message " + messageID);
			 
			 TextChannel channel = (TextChannel) jda.getGuildById(location.guildID).getGuildChannelById(location.channelID);
			 
			 Message message = channel.retrieveMessageById(messageID).submit().get();
			 for (MessageReaction reaction : message.getReactions()) {
				ReactionEmote emote = reaction.getReactionEmote();
				String id = "";
				boolean isUnicode = false;
				if (emote.isEmoji()) {
					id = emote.getEmoji();
					//id = unicodeToHex(id);
					isUnicode = true;
				} else {
					id = emote.getEmote().getId();
				}
				
				boolean exists = false;
				
				for (String emojiID : emoteRole.keySet()) {
					if (emojiID.equalsIgnoreCase(id)) {
						exists = true;
					}
				}
				
				if (!exists) {
					if (isUnicode) {
						message.removeReaction(id).queue();
					} else {
						message.removeReaction(emote.getEmote()).queue();
					}
				}
				
				
				
				
			 }
			 
			 
			 Guild guild = jda.getGuildById(location.guildID);
			 int after = 0;
			 for (String emote : emoteRole.keySet()) {
				 
				 System.out.println(emote);
				 if (emote.startsWith("u") || emote.startsWith("U") || emote.length() <= 4) {
					 message.addReaction(emote).queueAfter(after * 500, TimeUnit.MILLISECONDS);
				 } else {
					 message.addReaction(guild.getEmoteById(emote)).queueAfter(after * 500, TimeUnit.MILLISECONDS);
				 }
				 after++;
			 }
			 
			 
			 i++;
		 }
		 
		 
	 }
	 
	 /*public static String parseConfig() {
		 JSONParser parser = new JSONParser();
			try {
				Object obj = parser.parse(new FileReader("config.json"));
	 
				// A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
				JSONObject config = (JSONObject) obj;
				
				System.out.println(config.toJSONString());
				
				BOT_TOKEN = (String) config.get("BOT_TOKEN");
				//MESSAGE_ID = (String) config.get("MESSAGE_ID");
				USER_ID = (String) config.get("USER_ID");
				GUILD_ID = (String) config.get("GUILD_ID");
				
				migrate = (boolean) config.get("MIGRATE");
				
				
				
				// A JSON array. JSONObject supports java.util.List interface.
				/*JSONArray emoteRoleArray = (JSONArray) config.get("EMOTE_ROLE");
				
				for (Object object : emoteRoleArray) {
					JSONObject emoteRole = (JSONObject) object;
					emoteToRole.put((String) emoteRole.get("EMOTE_ID"), (String) emoteRole.get("ROLE_ID"));
				}
				
				JSONArray joinRoleArray = (JSONArray) config.get("JOIN_ROLES");
				for (Object object : joinRoleArray) {
					String role = (String) object;
					rolesOnJoin.add(role);
				}//
				
				
				JSONArray messagesArray = (JSONArray) config.get("MESSAGES");
				
				
				
				for (Object object : messagesArray) {
					JSONObject messages = (JSONObject) object;
					
					String messageID = (String) messages.get("MESSAGE_ID");
					String guildID = (String) messages.get("GUILD_ID");
					String channelID = (String) messages.get("CHANNEL_ID");
					
					JSONArray emoteRoleArray = (JSONArray) messages.get("EMOTE_ROLE");
					
					LinkedHashMap<String,String> emoteRoleMap = new LinkedHashMap<String, String>();
					
					for (Object object2 : emoteRoleArray) {
						JSONObject emoteRole = (JSONObject) object2;
						emoteRoleMap.put(((String) emoteRole.get("EMOTE_ID")), (String) emoteRole.get("ROLE_ID"));
					

					}
					
					System.out.println("added " + guildID + " " + channelID + " " + messageID);
					
					messageEmoteRole.put(messageID, emoteRoleMap);
					mlList.add(new MessageLocation(guildID, channelID));
				}
				
	 

			} catch (Exception e) {
				StringWriter sw = new StringWriter();
	            e.printStackTrace(new PrintWriter(sw));
	            String exceptionAsString = sw.toString();
	            e.printStackTrace();
	            return exceptionAsString;
			}
			
		return "Reloaded!";
	 }*/

}
