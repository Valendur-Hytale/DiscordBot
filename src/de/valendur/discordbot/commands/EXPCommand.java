package de.valendur.discordbot.commands;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

import de.valendur.discordbot.Bot2;
import de.valendur.discordbot.Utils;
import de.valendur.discordbot.configs.BaseConfig;
import de.valendur.discordbot.configs.ConfigType;
import de.valendur.discordbot.configs.LevelingConfig;
import de.valendur.discordbot.dbhandlers.DBLevelingHandler;
import de.valendur.discordbot.handlers.RandomHandler;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.AttachmentOption;

public class EXPCommand extends GenericCommand{

	public EXPCommand(String commandText) {
		super(commandText);
	}
	
	@Override
	public void execute(GuildMessageReceivedEvent e, String commandParams){
	
		/*Instant preRead = Instant.now();
		BufferedImage image = readFile();
		Instant afterRead = Instant.now();
		
		
		Graphics2D g = image.createGraphics();
		g.setColor(new Color(255,0,0));
		g.fillRoundRect(252, 830, 1412, 38, 38, 38);
		g.dispose();
		byte[] array = bufferedImageToByteArray(image);
		Instant afterArray = Instant.now();
		e.getChannel().sendFile(array, "ay.png").complete();
		Instant afterSend = Instant.now();
		
		Duration reading = Duration.between(preRead, afterRead);
		Duration toArray = Duration.between(afterRead, afterArray);
		Duration sending = Duration.between(afterArray, afterSend);
		
		System.out.println("reading duration: " + reading.toMillis());
		System.out.println("toArray duration: " + toArray.toMillis());
		System.out.println("sending duration: " + sending.toMillis());*/
		
		JSONObject user = DBLevelingHandler.getUser(e.getMember().getIdLong());
		
		
		
		e.getChannel().sendMessage("<@" + e.getMember().getId() + ">").addFile(generateImageBytes(e.getMember(), user), "profileCard.png").queue();
	}
	
	
	private byte[] generateImageBytes(Member member, JSONObject user) {
		BufferedImage image = readFile();
		
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		        			RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		
		
		BufferedImage profilePic = readImage(member.getUser().getEffectiveAvatarUrl());
		
		
		g.drawImage(makeRoundedCorner(profilePic, 375), 261, 221, 375, 375, null);
		
		g.setColor(Color.WHITE);
		
		Font bigFont = new Font("Arial Black", Font.PLAIN, 75);
		Font middleFont = new Font("Arial Black", Font.PLAIN, 57);
		Font smallFont = new Font("Arial Black", Font.PLAIN, 40);
		
		g.setFont(bigFont);
		g.drawString(member.getEffectiveName(), 720, 280);
		g.setFont(smallFont);
		g.drawString(member.getUser().getAsTag(), 720, 340);
		
		
		g.setFont(smallFont);
		g.drawString("Beitrittsdatum", 720, 440);
		g.setFont(middleFont);
		g.drawString(member.getTimeJoined().format(DateTimeFormatter.ISO_LOCAL_DATE), 720, 495);
		
		g.setFont(smallFont);
		g.drawString("Erstellungsdatum", 720, 545);
		g.setFont(middleFont);
		g.drawString(member.getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE), 720, 600);
		System.out.println(user.toString());
		
		
		g.setFont(smallFont);
		g.drawString("Rank", 1320, 470);
		//FontMetrics fm = g.getFontMetrics();
		//System.out.println("Rank width: " + fm.stringWidth("Rank"));
		g.setFont(bigFont);
		FontMetrics fm = g.getFontMetrics();
		String stringToDraw = "#" + user.getInt("rank");
		int strgWidth = fm.stringWidth(stringToDraw);
		g.drawString(stringToDraw, 1376 - strgWidth/2, 540);
		
		
		double currentExp = user.getInt("currentExp") - user.getInt("expForCurrentLevel");
		double nextExp = user.getInt("expForNextLevel") - user.getInt("expForCurrentLevel");
		
		
		
		System.out.println(user.getInt("currentExp") + " " + user.getInt("expForCurrentLevel") + " " + user.getInt("currentExp"));
		
		g.setFont(smallFont);
		g.drawString(currentExp + " / " + nextExp, 270, 810);
		g.drawString("Level: " + user.getInt("currentLevel"), 1480, 810);
		
		g.setColor(new Color(255,0,0));
		g.fillRoundRect(252, 830, (int) (1412.0 * (currentExp / nextExp)), 38, 38, 38);
		
		g.dispose();
		return bufferedImageToByteArray(image);
	}
	
	private BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
	    int w = image.getWidth();
	    int h = image.getHeight();
	    BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

	    Graphics2D g2 = output.createGraphics();
	    
	    // This is what we want, but it only does hard-clipping, i.e. aliasing
	    // g2.setClip(new RoundRectangle2D ...)

	    // so instead fake soft-clipping by first drawing the desired clip shape
	    // in fully opaque white with antialiasing enabled...
	    g2.setComposite(AlphaComposite.Src);
	    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2.setColor(Color.WHITE);
	    g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
	    
	    // ... then compositing the image on top,
	    // using the white shape from above as alpha source
	    g2.setComposite(AlphaComposite.SrcAtop);
	    g2.drawImage(image, 0, 0, null);
	    
	    g2.dispose();
	    
	    return output;
	}
	
	
	
	private BufferedImage readFile() {
		BufferedImage in = null;
		try {
			in = ImageIO.read(new File("profile.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		BufferedImage newImage = new BufferedImage(
		    in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = newImage.createGraphics();
		g.drawImage(in, 0, 0, null);
		g.dispose();
		
		return newImage;
	}
	
	private BufferedImage readImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private byte[] bufferedImageToByteArray(final BufferedImage image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(image, "png", baos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		byte[] array = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return array;
		
	}	
	
	
}
