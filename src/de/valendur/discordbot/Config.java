package de.valendur.discordbot;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import kong.unirest.json.JSONObject;



public class Config {
	
	private static final String config_file = "config.json";
	private static final String tokenFile = "token.json";
	private static JSONObject config;
	
	public static String BOT_TOKEN;
	public static String GUILD_ID;
	public static String COMMAND_PREFIX;
	public static String SECURITY_CHANNEL;
	public static String MESSAGE_ID;
	public static boolean MIGRATE;
	public static List<String> RELOAD_ROLES;
	
	//ROLE MANAGEMENT
	
	public static List<String> rolesOnJoin = new ArrayList<String>(); 
	public static LinkedHashMap<String, LinkedHashMap<String, String>> messageEmoteRole = new LinkedHashMap<String, LinkedHashMap<String,String>>();
	public static List<MessageLocation> mlList = new ArrayList<MessageLocation>();
	
	
	@SuppressWarnings("unchecked")
	public static String parseConfig() {
		 String configString = getConfig();
		 
		 if (configString == null) {
			 return "Failed to read config";
		 }
		 
		 config = new JSONObject(configString);
		 
		 BOT_TOKEN = readToken(); //config.getString("BOT_TOKEN");
		 COMMAND_PREFIX = config.getString("COMMAND_PREFIX");
		 SECURITY_CHANNEL = config.getString("SECURITY_CHANNEL");
/*		 GUILD_ID = config.getString("GUILD_ID");
		 MIGRATE = (boolean) config.get("MIGRATE");
		 RELOAD_ROLES = (List<String>) config.getJSONArray("ROLE_ALLOWED_TO_RELOAD").toList();
		 
		 
		 
			/*try {
				kong.unirest.json.JSONObject object;
				new JSONObject
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
				}
				
				
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
			}*/
			
		return "Reloaded!";
	 }
	
	public static String getConfig() {
		try {
			return new String(Files.readAllBytes(Paths.get(config_file)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String readToken() {
		try {
			 return new JSONObject( new String(Files.readAllBytes(Paths.get(tokenFile)))).getString("BOT_TOKEN");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
