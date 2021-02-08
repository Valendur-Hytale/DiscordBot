package de.valendur.discordbot;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import kong.unirest.ContentType;
import kong.unirest.MultipartBody;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.JDAImpl;
import net.dv8tion.jda.internal.entities.EntityBuilder;
import net.dv8tion.jda.internal.requests.Requester;

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
	
	public static Message sendImage(Guild guild, TextChannel channel, BufferedImage image, String messageText)
    {
		return sendImage(guild, channel, image, new MessageBuilder().append(messageText).build());
    }
	
	public static Message sendImage(Guild guild, TextChannel channel, BufferedImage image, Message message)
    {

        JDAImpl api = (JDAImpl) guild.getJDA();
        try
        {
        	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        	ImageIO.write(image, "png", bytes);
        	bytes.flush();
            MultipartBody body = Unirest.post(Requester.DISCORD_API_PREFIX + "channels/" + channel.getId() + "/messages")
                    .header("authorization", guild.getJDA().getToken())
                    .header("user-agent", Requester.USER_AGENT)
                    .field("empty", "");
            body.field("file", bytes.toByteArray(), ContentType.create("image/png"), "imge.png");
            
            bytes.close();
            
            if (message != null)
                body.field("content", message.getContentRaw()).field("tts", message.isTTS() + "");

     
            String requestBody = body.asString().getBody();
            JSONObject messageJson = new JSONObject(requestBody);
            messageJson.put("channel_id", channel.getIdLong());
            
            return new EntityBuilder(api).createMessage(DataObject.fromJson(messageJson.toString()));
        }
        catch (UnirestException | IOException e)
        {
        	e.printStackTrace();
        }
        return null;
    }

    public static void sendImageAsync(Guild guild, TextChannel channel, BufferedImage image, String message, Consumer<Message> callback)
    {

        Thread thread = new Thread(() ->
        {
        	Message messageReturn = sendImage(guild, channel, image, new MessageBuilder().append(message).build());
			if (callback != null)
				callback.accept(messageReturn);
        });
        thread.setName("TextChannelImpl sendFileAsync Channel: " + channel.getId());
        thread.setDaemon(true);
        thread.start();
    }

}
