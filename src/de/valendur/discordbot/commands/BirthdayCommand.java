package de.valendur.discordbot.commands;

import de.valendur.discordbot.dbhandlers.DBBirthdayHandler;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command.Subcommand;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.Button;

public class BirthdayCommand extends ListenerAdapter {

	public static void command(SlashCommandEvent event, String subName) {
		event.getHook();
		switch (subName) {
		case "set": {
			set(event);
			return;
		}
		case "delete": {
			delete(event);
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + subName);
		}
	}

	@Override
	public void onButtonClick(ButtonClickEvent event) {
		event.deferEdit().queue();
		System.out.println("button click");
		String[] id = event.getComponentId().split(":");
		String type = id[0];
		System.out.println("afet button");
		switch (type) {
		case "setBirthday":
			//	 revalidate bc spoofable
			int month = (int) Math.min(12, Math.max(1, Integer.parseInt(id[2])));
			int day = (int) Math.min(31, Math.max(1, Integer.parseInt(id[1])));
			System.out.println(day + " " + (month - 1));
			DBBirthdayHandler.setBirthdayForUser(event.getUser().getIdLong(), day, month - 1);
			break;
		case "dontBirthday":
			System.out.println("dont");
			System.out.println("hook id: " + event.getHook().retrieveOriginal().complete().getId());
			//event.getHook().deleteOriginal().queue();
			break;
		}
		System.out.println("to the end");
		event.getHook().editOriginal("Du kannst diese Nachricht nun verwerfen").queue();
		
	}

	public static void set(SlashCommandEvent event) { // This is configured to be optional so check for null
		int month = (int) Math.min(12, Math.max(1, event.getOption("month").getAsLong()));
		int day = (int) Math.min(31, Math.max(1, event.getOption("day").getAsLong()));
		event.reply("Bist du dir sicher dass du deinen Geburtstag auf den " + day + "." + month + " setzen möchtest?") 
				.addActionRow(// this means "<style>(<id>, <label>)" the id can be spoofed by the user so
								// setup some kinda verification system
						Button.danger("dontBirthday", "Ich hab mich vertippt."),
						Button.success("setBirthday:" + day + ":" + month, "Ja das passt."))
				.setEphemeral(true).queue();
	}

	public static void delete(SlashCommandEvent event) {
		// set birthday db handler
		event.reply("You do not have permissions to kick me.").setEphemeral(true).queue();
	}

}
