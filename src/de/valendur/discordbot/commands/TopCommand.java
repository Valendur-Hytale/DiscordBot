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
        e.getMessage().addReaction("✅").queue();
        switch (commandParams.toLowerCase().trim()) {
            case "daily":
                e.getChannel().sendMessage(buildTop("dailyExp", 0,5, "Daily Top 5/100").build()).queue(message -> {
                    message.addReaction("◀️").queue(message1 -> {
                        message.addReaction("▶️").queue();
                    });

                });
                break;
            case "weekly":
                e.getChannel().sendMessage(buildTop("weeklyExp", 0,5, "Weekly Top 5/100").build()).queue(message -> {
                    message.addReaction("◀️").queue(message1 -> {
                        message.addReaction("▶️").queue();
                    });

                });
                break;
            case "monthly":
                e.getChannel().sendMessage(buildTop("monthlyExp", 0,5, "Monthly Top 5/100").build()).queue(message -> {
                    message.addReaction("◀️").queue(message1 -> {
                        message.addReaction("▶️").queue();
                    });

                });
                break;
            case "all":
            default:
                e.getChannel().sendMessage(buildTop("currentExp",0, 5, "Top 5/100").build()).queue(message -> {
                    message.addReaction("◀️").queue(message1 -> {
                        message.addReaction("▶️").queue();
                    });

                });
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
        for (int i = startIndex; i < (startIndex+amount); i++) {
            JSONObject sortedUser = sortedMember.getJSONObject(i);
            JSONObject user = DBLevelingHandler.getUser(sortedUser.getLong("userID"));
            int currentExp = user.getInt("currentExp") - user.getInt("expForCurrentLevel");
            int currentLevel = user.getInt("currentLevel");
            String profileName = user.getString("profileName") != null ? user.getString("profileName") : "";

            String desc = "\n" + (i+1) + ". " + profileName +
                    "\nLevel: " + currentLevel +
                    "\nXP: " + currentExp +
                    "\n";
            builder.appendDescription(desc);
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
                int valA = 0;
                int valB = 0;

                try {
                    valA = a.getInt(KEY_NAME);
                    valB = b.getInt(KEY_NAME);
                } catch (JSONException e) {
                    //do something
                }

                return -Integer.compare(valA,valB);
            }
        });

        for (int i = 0; i < input.length(); i++) {
            sortedJsonArray.put(jsonValues.get(i));
        }
        return sortedJsonArray;
    }
}
