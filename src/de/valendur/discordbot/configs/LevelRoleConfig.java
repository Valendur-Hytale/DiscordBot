package de.valendur.discordbot.configs;

import de.valendur.discordbot.Bot;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class LevelRoleConfig extends GenericConfig{

    public LevelRoleConfig(ConfigType type) {
        super(type);
    }
    public Map<String,Long> LEVEL_ROLES = new LinkedHashMap<>();


    @Override
    public void load() {
        JSONObject config = readConfig();
        JSONObject level = config.getJSONObject("ROLES");

        //If somebody messes it up everything will work like intended
        if(level == null) {
            return;
        }

        //Goes through every key within the Array and puts it with its Value in the List
        level.keySet().forEach(lvl -> {
            LEVEL_ROLES.put(lvl, level.getLong(lvl));
        });
    }

    /**
     * @param role The Role name like set in the Config
     * @return The Role which corespondents to the role/level
     */
    public Role getAsRole(String role) {
        Guild guild = Bot.getGuild();
        if(LEVEL_ROLES.get(role) != null) {
            return guild.getRoleById(LEVEL_ROLES.get(role));
        }
        return null;
    }

    /**
     *
     * @param user The user Object who gets the Role Assigned
     */
    public void assignRole(@NotNull JSONObject user) {
        Guild guild =  Bot.getGuild();
        long userId = user.getInt("userID");
        int currentLvl = user.getInt("currentLevel");

        try {
            if(currentLvl >= 5) {
                guild.addRoleToMember(userId,getAsRole("HOBBIES")).queue();
                guild.addRoleToMember(userId,getAsRole("ABOUT_ME")).queue();
                guild.addRoleToMember(userId,getAsRole("GAMES")).queue();
            }
            int prevRoleIndex = getIndexRoleBefore(""+currentLvl);
            if(prevRoleIndex != -1){
                Role rem = getAsRole("" + (long) LEVEL_ROLES.values().toArray()[prevRoleIndex]);
                guild.removeRoleFromMember(userId, rem).queue(unused -> System.out.println("Removed Role " + rem.getName() + " from " + userId));
            }
            Role add = getAsRole(""+ currentLvl);
            guild.addRoleToMember(userId,add).queue(unused -> System.out.println("Added Role " + add.getName() + " to " + userId));

            //Just that the console will not be Spammed if Nullpointer would fire its just bc the Level doesn't exist
        } catch (NullPointerException ignored){
        }
    }

    /**
     *
     * @param role The Role that should be checked as a Level
     * @return The Index of the Previous Role - Returns -1 if the Role doesn't exists or if it is the First role
     */
    public int getIndexRoleBefore(String role){
        if(Objects.equals(role, "HOBBIES") || Objects.equals(role, "ABOUT_ME") || Objects.equals(role, "GAMES")) {
            return -1;
        }
        for (int i = 0; i < LEVEL_ROLES.keySet().size(); i++) {
            if (LEVEL_ROLES.keySet().toArray()[i] == role) {
                return i - 1;
            }
        }
        return -1;
    }
}
