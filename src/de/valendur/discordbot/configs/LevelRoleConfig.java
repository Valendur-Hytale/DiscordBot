package de.valendur.discordbot.configs;

import de.valendur.discordbot.Bot;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;

import java.util.HashMap;
import java.util.Map;

public class LevelRoleConfig extends GenericConfig{

    public LevelRoleConfig(ConfigType type) {
        super(type);
    }
    public Map<String,Long> LEVEL_ROLES = new HashMap<>();


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
}
