package com.trc202.CombatTagListeners;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.trc202.CombatTag.CombatTag;

public class NoPvpPlayerListener implements Listener {
	
	private final CombatTag plugin;

	public NoPvpEntityListener entityListener;

	public NoPvpPlayerListener(CombatTag instance)
	{
		plugin = instance;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		Player quitPlr = e.getPlayer();
		if (quitPlr.hasPermission("combattag.ignore.pvplog"))
		{
			return;
		}
		
		if (quitPlr.isDead())
		{
			plugin.entityListener.onPlayerDeath(quitPlr);
		}
		else if (plugin.inTagged(quitPlr.getUniqueId()))
		{
			//Player is likely in pvp
			if (plugin.isInCombat(quitPlr.getUniqueId()))
			{
				//Player has logged out before the pvp battle is considered over by the plugin
				if (plugin.isDebugEnabled())
				{
					plugin.log.info("[CombatTag] " + quitPlr.getName() + " has logged out during pvp!");
					plugin.log.info("[CombatTag] " + quitPlr.getName() + " has been instakilled!");
				}
				quitPlr.damage(1000L);
				plugin.removeTagged(quitPlr.getUniqueId());
			}
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event)
	{
		Player quitPlr = event.getPlayer();
		UUID playerUUID = quitPlr.getUniqueId();
		if (quitPlr.isDead() || quitPlr.getHealth() <= 0)
		{
			plugin.entityListener.onPlayerDeath(quitPlr);
			return;
		}
		if (plugin.isInCombat(playerUUID)) // This fucker logged out in combat
		{
			quitPlr.damage(1000L);
			plugin.removeTagged(playerUUID);
		}
	}

/*	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (event.getMaterial() == Material.ENDER_PEARL) {
				if (plugin.isInCombat(event.getPlayer().getUniqueId())) {
					if (plugin.settings.blockEnderPearl()) {
						event.getPlayer().sendMessage(ChatColor.RED + "[CombatTag] You can't ender pearl while tagged.");
						event.setCancelled(true);
					}
				}
			}
		}
	}*/


	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onTeleport(PlayerTeleportEvent event)
	{
		if (plugin.settings.blockTeleport() && plugin.isInCombat(event.getPlayer().getUniqueId()))
		{
			TeleportCause cause = event.getCause();
			switch (cause)
			{
				
				case COMMAND:
				case PLUGIN:
					if(event.getPlayer().getWorld() != event.getTo().getWorld())
					{
						event.getPlayer().sendMessage(ChatColor.RED + "[CombatTag] You can't teleport across worlds while tagged.");
						event.setCancelled(true);
					}
					else if(event.getFrom().distance(event.getTo()) > 8) //Allow through small teleports as they are inconsequential, but some plugins use these
					{
						event.getPlayer().sendMessage(ChatColor.RED + "[CombatTag] You can't teleport while tagged.");
						event.setCancelled(true);
					}
					break;
			
				case ENDER_PEARL:
					if (event.getTo().distance(event.getFrom()) > 16)
					{
						event.setCancelled(true);
					}

				default:
					break;
					
			}
		}
	}

}
