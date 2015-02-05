package com.trc202.CombatTagListeners;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.trc202.CombatTag.CombatTag;

public class NoPvpEntityListener implements Listener {

	CombatTag plugin;

	public NoPvpEntityListener(CombatTag combatTag)
	{
		this.plugin = combatTag;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageByEntityEvent e)
	{
		if (e.getDamage() == 0)
			return;
		Entity dmgr = e.getDamager();
		if (dmgr instanceof Projectile)
		{
			if (((Projectile)dmgr).getShooter() instanceof Entity)
				dmgr = (Entity) ((Projectile)dmgr).getShooter();
		}
		if (e.getEntity() instanceof Player)
		{
			Player tagged = (Player) e.getEntity();
			
			if ((dmgr instanceof Player) && plugin.settings.playerTag())
			{
				Player damagerPlayer = (Player) dmgr;
				if(!(damagerPlayer == null && damagerPlayer == tagged))
				{
					onPlayerDamageByPlayer(damagerPlayer,tagged);
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDeath(EntityDeathEvent event)
	{
		if (event.getEntityType().equals(EntityType.PLAYER))
			onPlayerDeath((Player) event.getEntity());
	}

	public void onPlayerDeath(Player deadPlayer)
	{
		plugin.removeTagged(deadPlayer.getUniqueId());
	}

	public void onPlayerDamageByPlayer(Player damager, Player damaged)
	{

			if(!damager.hasPermission("combattag.ignore"))
			{	
				if (plugin.settings.blockCreativeTagging() && damager.getGameMode() == GameMode.CREATIVE)
				{
					damager.sendMessage(ChatColor.RED + "[CombatTag] You can't tag players while in creative mode!");
					return;
				}

				if (plugin.settings.isSendMessageWhenTagged() && !plugin.isInCombat(damager.getUniqueId()))
				{
					String tagMessage = plugin.settings.getTagMessageDamager();
					tagMessage = tagMessage.replace("[player]", "" + damaged.getName());
					damager.sendMessage(ChatColor.RED + "[CombatTag] " + tagMessage);
				}
				plugin.addTagged(damager);

			}
			if(!damaged.hasPermission("combattag.ignore") && !plugin.settings.onlyDamagerTagged())
			{	
				if(!plugin.isInCombat(damaged.getUniqueId()))
				{
					if(plugin.settings.isSendMessageWhenTagged())
					{
						String tagMessage = plugin.settings.getTagMessageDamaged();
						tagMessage = tagMessage.replace("[player]", damager.getName());
						damaged.sendMessage(ChatColor.RED + "[CombatTag] " + tagMessage);
					}
				}
				plugin.addTagged(damaged);
				
			}
	}

	
	public boolean disallowedWorld(String worldName)
	{
		for(String disallowedWorld : plugin.settings.getDisallowedWorlds())
		{
			if(worldName.equalsIgnoreCase(disallowedWorld))
			{
				//Skip this tag the world they are in is not to be tracked by combat tag
				return true;
			}
		}
		return false;
	}
}