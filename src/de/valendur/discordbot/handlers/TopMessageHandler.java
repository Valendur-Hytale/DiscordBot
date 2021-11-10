package de.valendur.discordbot.handlers;

import de.valendur.discordbot.commands.TopCommand;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class TopMessageHandler extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.retrieveUser().complete().isBot()) {
            return;
        }
        if(event.getReactionEmote().getName().equals("◀️")) {
            String description = event.getTextChannel().retrieveMessageById(event.getMessageId()).complete().getEmbeds().get(0).getDescription();
            if(description != null) {
                String key = !Objects.equals(description.split(" ")[0], "Top") ? description.split(" ")[0] : "current";
                String index = description.split("/")[0];
                String indexx = index.substring(index.length() - 2);
                int startIndex = Integer.parseInt(indexx.trim());
                if (startIndex - 5 < 5) {
                    startIndex = 5;
                }
                if (description.contains("/100")) {
                    String desc = "";
                    if(key.toLowerCase().equals("current")){
                        desc = "Top ";
                    } else {
                        desc = key + " Top ";
                    }
                    event.getTextChannel().editMessageById(event.getMessageId(), TopCommand.buildTop(key.toLowerCase() + "Exp", (startIndex - 10), 5, desc + (startIndex-5) + "/100").build()).queue();
                }
            }
        }

        if(event.getReactionEmote().getName().equals("▶️")){
            String description = event.getTextChannel().retrieveMessageById(event.getMessageId()).complete().getEmbeds().get(0).getDescription();
            if(description != null) {
                String key = !Objects.equals(description.split(" ")[0], "Top") ? description.split(" ")[0] : "current";
                String index = description.split("/")[0];
                String indexx = index.substring(index.length() - 2);
                int startIndex = Integer.parseInt(indexx.trim());
                if (startIndex + 5 > 100) {
                    startIndex = 0;
                }
                if (description.contains("/100")) {
                    String desc = "";
                    if(key.toLowerCase().equals("current")){
                        desc = "Top ";
                    } else {
                        desc = key + " Top ";
                    }
                    event.getTextChannel().editMessageById(event.getMessageId(), TopCommand.buildTop(key.toLowerCase() + "Exp", (startIndex), 5, desc + (startIndex + 5) + "/100").build()).queue();
                }
            }
        }
    }
}
