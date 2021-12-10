package de.valendur.discordbot.tasks;

import java.awt.Color;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.valendur.discordbot.Bot;
import de.valendur.discordbot.dbhandlers.DBPrimeLeagueHandler;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;

public class PrimeLeagueCheckTask extends GenericRepeatedTask {


	HashMap<String,Document> gameDocs = new HashMap<String,Document>();
	
	public PrimeLeagueCheckTask(int seconds) {
		super(seconds);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() {
		System.out.println("checking for teams");
		for (Team team : DBPrimeLeagueHandler.getTeams()) {
			System.out.println("checking team " + team.teamID + " " + team.games);
			List<Game> games = getGames(team);
			for (Game game : games) {
				System.out.println("checking games " + team.teamID + " " + game.gameLink);
				Game origGame = getGameFromTeam(team, game);
				int range = origGame == null ? game.log.size() : game.log.size() - origGame.log.size();
				System.out.println("range: " + range + " origgame: " + origGame);
				for (int i = range; i >= 1; i--) {
					logEntryToChannel(game.log.get(i - 1), game, team.channelID);
				}
			}
			team.games = games;
			DBPrimeLeagueHandler.updateTeam(team);
		}
	}
	
	
	
	public LinkedHashSet<String> getGameUrls(String teamLink){
		LinkedHashSet<String> gameSet = new LinkedHashSet<>();
		Response response = null;
		Document doc = null;
		try {
			response = getSiteResponse(teamLink, null);
			doc = response.parse();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String teamName = response.url().toString().replace(teamLink + "-", "");
		
		Elements els = doc.getElementsByClass("boxed-section hybrid league-team-stage");
		Elements links = els.select("a[href]"); // Kalibrierungsphase 0, Gruppenphase 1
		//System.out.println(doc.toString());
		//System.out.println(els.get(0).toString());
		for (Element link : links) {
			Elements gameScore = link.select(".important");
			//System.out.println("got link " +gameScore.size() +  gameScore.text());
			
			if (gameScore.size() != 0 && gameScore.text().isEmpty()) {
				gameSet.add(link.attr("href"));
				//System.out.println("added link " + link.attr("href"));
			}
		}
		return gameSet;
	}
	
	public List<Game> getGames(Team team) {
		LinkedHashSet<String> gameUrls = getGameUrls("https://www.primeleague.gg/leagues/teams/" + team.teamID);
		List<Game> games = new ArrayList<Game>();
		for (String gameLink : gameUrls) {
			Game game = new Game();
			game.gameLink = gameLink;
			Document gameDoc = null;
			try {
				gameDoc = getSiteDocument(gameLink, null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			gameDocs.put(gameLink, gameDoc);
			
			Elements teamData = gameDoc.select(".content-match-head-team-titles").select("a");
			game.t1Link = teamData.get(0).attr("href");
			game.t1Name = teamData.get(0).text();
			game.t2Link = teamData.get(1).attr("href");
			game.t2Name = teamData.get(1).text();
			game.enemy = game.t1Link.contains(team.teamID) ? "Team 2" : "Team 1";
			
			Element logContainer = gameDoc.selectFirst("#league-match-content").nextElementSibling();
			Element logBody = logContainer.selectFirst("tbody");
			game.log = parseLogBody(logBody);
			
			//System.out.println(game);
			games.add(game);
			//System.out.println("-------------------------");
		}
		return games;
	}
	
	
	public List<LogEntry> parseLogBody(Element logBody) {
		List<LogEntry> logs = new ArrayList<LogEntry>();
		for (Element tableRow : logBody.select("tr")) {
			LogEntry logEntry = new LogEntry();
			Elements tableCells = tableRow.select(".table-cell-container");
			
			LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(tableCells.get(0).child(0).attr("data-time")) * 1000L ), ZoneId.systemDefault());
			logEntry.dateTime = dateTime;
			
			logEntry.playerName = tableCells.get(1).text();
			
			//System.out.println(tableCells.get(2).text());
			logEntry.type = LogType.fromString(tableCells.get(2).text());
			
			logEntry.message = tableCells.get(3).html().replace(" +0100", "").replace(" +0200", "").replace("<br>", "\n");
			logs.add(logEntry);
			//System.out.println(logEntry.toString());
		}
		return logs;
	}
	
	public void logEntryToChannel(LogEntry entry, Game game, String channelID) {
		if (entry.playerName.contains(game.enemy.equals("Team 1") ? "(Team 2)" : "(Team 1)" )) {
			return;
		}
		
		TextChannel channel = Bot.getGuild().getTextChannelById(channelID);
		EmbedBuilder base = new EmbedBuilder()
				.setAuthor(game.t1Name + " vs " + game.t2Name, game.gameLink)
				.setTitle(entry.type.getText())
				.setFooter(entry.playerName)
				.setColor(new Color(9442302));
		Document gameDoc = gameDocs.get(game.gameLink);
		String gameLinkremove = game.gameLink.replace("https://www.primeleague.gg/leagues/matches/", "");
		String gameID = gameLinkremove.substring(0, gameLinkremove.indexOf("-"));
		
		switch (entry.type) {
		case LINEUP_SUBMIT:
			System.out.println("Lineup submit");
			JSONObject jsonGame = getGame(gameID);
			System.out.println("jsongmae " + jsonGame);
			String[] players = entry.message.split(",");
			String opggLink = "https://euw.op.gg/multi/query=";
			String opggName = "";
			for (String player : players) {
				player = player.split(":")[0].replace(" ", "");
				System.out.println("get game account " + player);
				String leagueName = getGameAccount(jsonGame, player);
				System.out.println("leagueName " + leagueName);
				opggName += leagueName + ",";
				
			}
			MessageEmbed embed = base.setDescription("[op.gg](" + opggLink + URLEncoder.encode(opggName, StandardCharsets.UTF_8) + ")").build();
			channel.sendMessage(embed).queue();
			break;
		default:
			channel.sendMessage(base.setDescription(entry.message).build()).queue();
			break;
		}
		
	}
	
	public Game getGameFromTeam(Team team, Game game) {
		for (Game tGame : team.games) {
			if (tGame.gameLink.equals(game.gameLink)) {
				return tGame;
			}
		}
		return null;
	}
	
	public Response getSiteResponse(String link, String refferer) throws IOException {
		return Jsoup.connect(link)
			      .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
			      .referrer(refferer != null ? refferer : "http://www.google.com")
			      .followRedirects(true).execute();
	}
	
	
	public Document getSiteDocument(String link, String refferer) throws IOException {
		return getSiteResponse(link, refferer).parse();
	}
	
	public JSONObject getGame(String gameID) {
		HttpResponse<JsonNode> response = Unirest.get("https://www.primeleague.gg/ajax/leagues_match")
				.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:94.0) Gecko/20100101 Firefox/94.0")
				.header("Accept", "*/*")
				.header("Accept-Language", "de,en-US;q=0.7,en;q=0.3")
				.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
				.header("X-Requested-With", "XMLHttpRequest")
				.header("Sec-Fetch-Dest", "empty")
				.header("Sec-Fetch-Mode", "cors")
				.header("Sec-Fetch-Site", "same-origin")
				.header("Cache-Control", "max-age=0")
				//.header("referrer", "https://www.primeleague.gg/leagues/matches/845827-austriansenpai-vs-capital-united-esports-v2")
				.header("credentials", "include")
				.queryString("id", gameID)
				.queryString("action", "lineup_get")
				.queryString("devmode", "1")
				.queryString("language", "de")
				.asJson();
		System.out.println("response: " + response.getStatus() + " " + response.getStatusText() + " gameid: " + gameID);
		return response.getBody().getObject();
	}
	
	
	public String getGameAccount(JSONObject game, String playerID) {
		JSONObject lineups = game.getJSONObject("lineups");
		for (Object o : lineups.getJSONArray("1")) {
			JSONObject player = (JSONObject) o;
			if ( playerID.equals("" + player.getInt("id"))) {
				return (String) player.getJSONArray("gameaccounts").get(0);
			}
		}
		for (Object o : lineups.getJSONArray("2")) {
			JSONObject player = (JSONObject) o;
			if ( playerID.equals("" + player.getInt("id"))) {
				return (String) player.getJSONArray("gameaccounts").get(0);
			}
		}
		return "";
	}
}
