package de.valendur.discordbot.commands;

import de.valendur.discordbot.dbhandlers.DBLevelingHandler;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONException;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TopCommand extends GenericCommand {

    public TopCommand(String commandText) {
        super(commandText);
    }

    @Override
    public void execute(GuildMessageReceivedEvent e, String commandParams) {
        switch (commandParams) {
            case "daily":
                e.getChannel().sendMessage(buildTop("dailyExp", 0,10, "Daily Top 10/100").build()).queue();
                break;
            case "weekly":
                e.getChannel().sendMessage(buildTop("weeklyExp", 0,10, "Weekly Top 10/100").build()).queue();
                break;
            case "monthly":
                e.getChannel().sendMessage(buildTop("monthlyExp", 0,10, "Monthly Top 10/100").build()).queue();
                break;
            case "all":
                e.getChannel().sendMessage(buildTop("exp",0, 10, "Top 10/100").build()).queue(message -> {
                    message.addReaction("◀️").queueAfter(2, TimeUnit.MILLISECONDS);
                    message.addReaction("▶️").queueAfter(2, TimeUnit.MILLISECONDS);

                });
                break;
            default:
                break;
        }
    }

    /**
     * Builds an Embed in following Style:
     * <ul>
     * <li>1. [name] [level] [exp]</li>
     * <li>2. [name] [level] [exp]</li>
     * <li>...</li>
     * <li>amount. [name] [level] [exp]</li>
     * </ul>
     * @param key - The Sorting key
     * @param startIndex - The Starting index from the list
     * @param amount - How many Entry's there are in the List
     * @param description - That what stands above the list
     * @return The EmbedBuilder
     */
    public static EmbedBuilder buildTop(String key, int startIndex,int amount, String description) {
        JSONArray users = DBLevelingHandler.getAllUsers();
        JSONArray sortedMember = sortArray(key, users);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription(description);
        builder.setTimestamp(Instant.now());
        for (int i = startIndex; i < amount; i++) {
            JSONObject user = sortedMember.getJSONObject(i);
            builder.appendDescription("\n" + i + ". " + user.getString("profileName") + " " + user.getString("level") + " " + user.getString("exp"));
        }
        return builder;
    }


    public static JSONArray sortArray(String key, JSONArray input) {
        JSONArray sortedJsonArray = new JSONArray();

        List<JSONObject> jsonValues = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            jsonValues.add(input.getJSONObject(i));
        }
        jsonValues.sort(new Comparator<>() {
            private final String KEY_NAME = key;

            @Override
            public int compare(JSONObject a, JSONObject b) {
                String valA = "";
                String valB = "";

                try {
                    valA = (String) a.get(KEY_NAME);
                    valB = (String) b.get(KEY_NAME);
                } catch (JSONException e) {
                    //do something
                }

                return valA.compareTo(valB);
                //if you want to change the sort order, simply use the following:
                //return -valA.compareTo(valB);
            }
        });

        for (int i = 0; i < input.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }
}
