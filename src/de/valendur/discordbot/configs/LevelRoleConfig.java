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

    public Map<Integer,Long> LEVEL_ROLE_ID = new HashMap<>();
    public long GAMES_ROLE_ID;
    public long ABOUT_ME_ROLE_ID;
    public long HOBBIES_ROLE_ID;


    @Override
    public void load() {
        JSONObject config = readConfig();

        GAMES_ROLE_ID = config.getLong("GAMES_ROLE");
        ABOUT_ME_ROLE_ID = config.getLong("ABOUT_ME_ROLE");
        HOBBIES_ROLE_ID = config.getLong("HOBBIES_ROLE");
        LEVEL_ROLE_ID.put(5,config.getLong("LEVEL_5"));
        LEVEL_ROLE_ID.put(10,config.getLong("LEVEL_10"));
        LEVEL_ROLE_ID.put(15,config.getLong("LEVEL_15"));
        LEVEL_ROLE_ID.put(25,config.getLong("LEVEL_25"));
        LEVEL_ROLE_ID.put(35,config.getLong("LEVEL_35"));
        LEVEL_ROLE_ID.put(50,config.getLong("LEVEL_50"));
        LEVEL_ROLE_ID.put(75,config.getLong("LEVEL_75"));
        LEVEL_ROLE_ID.put(100,config.getLong("LEVEL_100"));
        LEVEL_ROLE_ID.put(125,config.getLong("LEVEL_125"));
        LEVEL_ROLE_ID.put(150,config.getLong("LEVEL_150"));
        LEVEL_ROLE_ID.put(175,config.getLong("LEVEL_175"));
        LEVEL_ROLE_ID.put(200,config.getLong("LEVEL_200"));

    }

    public Role getAsRole(int level){
        Guild guild =  Bot.getGuild();
        return guild.getRoleById(LEVEL_ROLE_ID.get(level));
    }
}
