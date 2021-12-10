package de.valendur.discordbot.tasks;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Team implements Serializable {
	private final static long serialVersionUID = 1000;
	
	public String teamID, channelID;
	public List<Game> games = new ArrayList<Game>();
	
	public Team(String teamID, String channelID) {
		this.teamID = teamID;
		this.channelID = channelID;
	}
		
}

class Game implements Serializable {
	private final static long serialVersionUID = 2000;
	
	public List<LogEntry> log = new ArrayList<LogEntry>();
	public String t1Name, t2Name, t1Link, t2Link;
	public String gameLink, enemy;
	
	
	@Override
	public String toString() {
		return gameLink + " " + t1Name + " " + t1Link + " vs " + t2Name + " " + t2Link + "\n" +
				String.join("\n", log.stream().map(Object::toString).collect(Collectors.toList()));
	}
	
	
}

class LogEntry implements Serializable {
	private final static long serialVersionUID = 3000;
	
	public String playerName, message;
	public LocalDateTime dateTime;
	public LogType type;
	
	@Override
	public String toString() {
		return dateTime.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")) + " " + type.getText() + " Nachricht: " + message + " Von: " + playerName;  
	}
}

enum LogType {
    SCHED_SUGGESTION("scheduling_suggest", "Spieldatumvorschlag"),
    SCHED_AUTOCONFIRM("scheduling_autoconfirm", "Spieldatumsannahme"),
    SCHED_CONFIRM("scheduling_confirm", "Spieldatumsannahme"),
    LINEUP_SUBMIT("lineup_submit", "Lineupbestätigung"),
    LINEUP_PLAYER_READY("lineup_player_ready", "Spieler Bereit"),
    HOSTING_REQUEST("hosting_request", "Tournament Code"),
    GAME_END("report", "Spielende"),
    GAME_END_MANUALLY("score_report", "Matchende"),
    MATCH_END("played", "Matchende");

    private String action, friendlyName;
    private static List<LogType> notiList = Arrays.asList(SCHED_SUGGESTION, SCHED_AUTOCONFIRM, SCHED_CONFIRM, 
    												LINEUP_SUBMIT);

    LogType(String action, String friendlyName) {
        this.action = action;
        this.friendlyName = friendlyName;
    }

    public String getText() {
        return this.friendlyName;
    }

    public static LogType fromString(String action) {
        for (LogType t : LogType.values()) {
            if (t.action.equalsIgnoreCase(action)) {
                return t;
            }
        }
        return null;
    }
    
    public static boolean shouldNotify(LogType t) {
    	return notiList.contains(t);	
    }
    
}
