package de.valendur.discordbot.handlers;

import java.awt.Color;
import java.util.Arrays;

import com.github.philippheuer.events4j.core.EventManager;
import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.philippheuer.events4j.simple.domain.EventSubscriber;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.events.ChannelGoLiveEvent;

import de.valendur.discordbot.Bot;
import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.TokenConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;

public class TwitchHandler {
	
	public static TwitchClient twitchClient;
	public static EventManager eventManager;
	public Guild guild;
	
	public void setup(Guild guild) {
		this.guild = guild;
		twitchClient = TwitchClientBuilder.builder()
			    .withClientId(getToken().TWITCH_CLIENT_ID).withClientSecret(getToken().TWITCH_CLIENT_SECRET)
			    .withEnableHelix(true)
			    .withDefaultEventHandler(SimpleEventHandler.class)
			    .build();
		twitchClient.getClientHelper().enableStreamEventListener(Bot.getBaseConfig().TWITCH_CHANNEL_NAME);
		eventManager = twitchClient.getEventManager();
		
		eventManager.getEventHandler(SimpleEventHandler.class).registerListener(this);
		System.out.println("registered");
	}

	
	public TokenConfig getToken() {
		return (TokenConfig) Bot.configHandler.getConfig(ConfigType.TOKEN_CONFIG);
	}
	
	
	@EventSubscriber
    public void printChannelMessage(ChannelGoLiveEvent event) {
        System.out.println("[" + event.getChannel().getName() + "] is now live " + event.getStream().getThumbnailUrl(640,480));
        Role role = guild.getRoleById(Bot.getBaseConfig().SM_ROLE_ID);
        String name = event.getChannel().getName();
        String logo = twitchClient.getHelix().getUsers(null, null, Arrays.asList(Bot.getBaseConfig().TWITCH_CHANNEL_NAME)).execute().getUsers().get(0).getProfileImageUrl();
        Message message = new MessageBuilder()
        .append(role.getAsMention())
        .setEmbed(new EmbedBuilder()
          .setTitle(event.getStream().getTitle(), "https://twitch.tv/" + Bot.getBaseConfig().TWITCH_CHANNEL_NAME)
          .setDescription("spielt " + event.getStream().getGameName())
          .setColor(new Color(9442302))
//          .setFooter("footer text")
          .setImage(event.getStream().getThumbnailUrl(320,180))
          .setAuthor(name + " ist nun Live auf Twitch!", "https://twitch.tv/" + Bot.getBaseConfig().TWITCH_CHANNEL_NAME, logo)
          .build())
        .build();
        //guild.getTextChannelById(Bot.getBaseConfig().TWITCH_ANNOUNCEMENT_CHANNEL).sendMessage(event.getStream().getTitle() + "\n\n https://twitch.tv/" + Bot.getBaseConfig().TWITCH_CHANNEL_NAME + "\n" + role.getAsMention()).queue();
        guild.getTextChannelById(Bot.getBaseConfig().TWITCH_ANNOUNCEMENT_CHANNEL).sendMessage(message).queue();
        
    }

}
