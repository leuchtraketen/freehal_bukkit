package net.freehal.ui.bukkit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.freehal.core.util.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private TalkAdapter adapter;

	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new ChatListener(this), this);

		System.out.println("////////////////////Test Plugin Version 0.1 is enabled!");
		adapter = new TalkAdapter();
	}

	public void onDisable() {
		// SystemUtils.runExitListeners(0);
		adapter = null;
		System.out.println("Test Plugin is disabled!");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
		} else {
			Player player = (Player) sender;
			String commandName = command.getName().toLowerCase();

			if (commandName.equalsIgnoreCase("chat")) {
				String input = StringUtils.join(" ", args);

				List<ChatEntity> npcs = findNpcs(player.getLocation());

				if (npcs.size() > 0) {
					ChatEntity npc = npcs.get(0);
					double distance = npc.getDistance();
					if (distance > ChatEntity.MAX_NPC_DISTANCE) {
						player.sendMessage("The next NPC (a " + npc.getType() + ") is " + (int) distance
								+ " blocks away!");
					} else {
						npc.talk(player, input, adapter);
					}
				}

				return true;
			}

		}
		return false;
	}

	private static List<ChatEntity> findNpcs(Location loc) {
		List<ChatEntity> npcs = new ArrayList<ChatEntity>();
		for (World w : Bukkit.getWorlds()) {
			for (LivingEntity living : w.getLivingEntities()) {
				if (living instanceof NPC || living instanceof Animals) {
					double distance = loc.distanceSquared(living.getLocation());
					npcs.add(new ChatEntity(living, distance));
				}
			}
		}

		Collections.sort(npcs);
		return npcs;
	}

	public static class ChatListener implements Listener {
		private Main main;

		public ChatListener(Main main) {
			this.main = main;
		}

		@SuppressWarnings("deprecation")
		@EventHandler
		public void onPlayerChat(PlayerChatEvent event) {
			Player player = event.getPlayer();
			String input = event.getMessage();

			List<ChatEntity> npcs = findNpcs(player.getLocation());

			if (npcs.size() > 0) {
				ChatEntity npc = npcs.get(0);
				double distance = npc.getDistance();
				if (distance <= ChatEntity.MAX_NPC_DISTANCE) {
					npc.talk(player, input, main.adapter);
					event.setCancelled(true);
				} else if (distance > ChatEntity.MAX_NPC_DISTANCE
						&& distance < 5 * ChatEntity.MAX_NPC_DISTANCE) {
					player.sendMessage("The next NPC (a " + npc.getType() + ") is " + (int) distance
							+ " blocks away!");
				}
			}
		}

	}
}
