package net.freehal.ui.bukkit;

import java.util.regex.Pattern;

import net.freehal.core.util.RegexUtils;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class ChatEntity implements Comparable<ChatEntity> {

	public static int MAX_NPC_DISTANCE = 20;
	public static int MAX_AUDIBLE_RANGE = 50;

	@SuppressWarnings("unused")
	private LivingEntity npc;
	private Double distance;

	public LivingEntity getNpc() {
		return npc;
	}

	public Double getDistance() {
		return distance;
	}

	public String getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	private String type;
	private String id;
	private String name;

	public ChatEntity(LivingEntity npc, double distance) {
		this.npc = npc;
		this.distance = distance;
		this.type = npc.toString().replaceAll("Craft", "");
		this.id = Integer.toString(npc.getEntityId());
		this.name = type + "#" + id;

		System.out.println("NPC/Animal: " + name + ": distance=" + (int) distance);

		if (distance <= MAX_NPC_DISTANCE) {}
	}

	@Override
	public int compareTo(ChatEntity arg0) {
		return distance.compareTo(arg0.distance);
	}

	public void talk(Player player, String input, TalkAdapter adapter) {
		Location here = player.getLocation();
		input = prepareInput(input);
		PlayerUtil.say(player.getDisplayName(), input, PlayerUtil.findPlayers(here, MAX_NPC_DISTANCE));

		String output = adapter.answer(input);
		output = prepareOutput(output);
		PlayerUtil.say(name, output, PlayerUtil.findPlayers(here, MAX_NPC_DISTANCE));
	}

	private String prepareOutput(String output) {
		output = RegexUtils.ireplace(output, "Freehal", name);
		return output;
	}

	private String prepareInput(String input) {
		input = RegexUtils.ireplace(input, Pattern.quote(name), "Freehal");
		input = RegexUtils.ireplace(input, Pattern.quote(type), "Freehal");
		return input;
	}
}
