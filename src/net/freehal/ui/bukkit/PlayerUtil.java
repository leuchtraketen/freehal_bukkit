package net.freehal.ui.bukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;

public class PlayerUtil {

	private static final ChatColor NAME_COLOR = ChatColor.YELLOW;

	private static String formatMessage(String player, String message) {
		return PlayerUtil.NAME_COLOR + player + ChatColor.WHITE + ": " + message;
	}

	public static void say(String fromPlayer, String message) {
		Bukkit.getServer().broadcastMessage(formatMessage(fromPlayer, message));
	}

	public static void say(String fromPlayer, String message, Player toPlayer) {
		toPlayer.sendMessage(PlayerUtil.NAME_COLOR + fromPlayer + ChatColor.WHITE + ": " + message);
	}

	public static void say(String fromPlayer, String message, Collection<Player> players) {
		for (Player player : players) {
			say(fromPlayer, message, player);
		}
	}

	public static List<Player> findPlayers(Location here, int distance) {
		List<Player> playersHere = new ArrayList<Player>();
		Player players[] = Bukkit.getServer().getOnlinePlayers();
		for (Player player : players) {
			if (player.getLocation().distanceSquared(here) <= distance) {
				playersHere.add(player);
			}
		}
		return playersHere;
	}

	public static List<LivingEntity> findLivingEntities(Location here, int distance) {
		List<LivingEntity> livingEntities = new ArrayList<LivingEntity>();
		for (World w : Bukkit.getWorlds()) {
			for (LivingEntity living : w.getLivingEntities()) {
				livingEntities.add(living);
			}
		}
		return livingEntities;
	}

	public static List<Animals> findAnimals(Location here, int distance) {
		List<Animals> animals = new ArrayList<Animals>();
		for (LivingEntity living : findLivingEntities(here, distance)) {
			if (living instanceof Animals) {
				animals.add((Animals) living);
			}
		}
		return animals;
	}

	public static List<NPC> findNPCs(Location here, int distance) {
		List<NPC> npcs = new ArrayList<NPC>();
		for (LivingEntity living : findLivingEntities(here, distance)) {
			if (living instanceof NPC) {
				npcs.add((NPC) living);
			}
		}
		return npcs;
	}
}
