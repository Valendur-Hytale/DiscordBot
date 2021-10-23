package de.valendur.discordbot.handlers;

import de.valendur.discordbot.commands.TopCommand;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class TopMessageHandler extends ListenerAdapter {

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if(event.getReactionEmote().getName().equals("arrow_backward")) {
            String description = event.getTextChannel().retrieveMessageById(event.getMessageId()).complete().getEmbeds().get(0).getDescription();
            assert description != null;
            String key = !Objects.equals(description.split(" ")[0], "Top") ? description.split(" ")[0] : "";
            String index = description.split("/")[0];
            index = index.substring(index.length()-2);
            int startIndex = Integer.parseInt(index);
            if(startIndex-10 < 10){
                startIndex = 20;
            }
            event.getTextChannel().editMessageById(event.getMessageId(), TopCommand.buildTop(key.toLowerCase() + "Exp",startIndex-10,10,key + " Top " +(startIndex-10)+"/100").build()).queue();
        }

        if(event.getReactionEmote().getName().equals("arrow_forward")){
            String description = event.getTextChannel().retrieveMessageById(event.getMessageId()).complete().getEmbeds().get(0).getDescription();
            assert description != null;
            String key = !Objects.equals(description.split(" ")[0], "Top") ? description.split(" ")[0] : "";
            String index = description.split("/")[0];
            index = index.substring(index.length()-2);
            int startIndex = Integer.parseInt(index);
            if(startIndex+10 > 90){
                startIndex = 0;
            }
            event.getTextChannel().editMessageById(event.getMessageId(), TopCommand.buildTop(key.toLowerCase() + "Exp",startIndex+10,10,key + " Top " +(startIndex+10)+"/100").build()).queue();
        }
    }
}
