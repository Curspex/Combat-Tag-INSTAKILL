package com.trc202.CombatTagListeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import com.earth2me.essentials.Essentials;
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
		if (e.getDamage() <= 0)
			return;
		Entity dmgr = e.getDamager();
		if (dmgr instanceof Projectile)
		{
			Projectile projectile = (Projectile) dmgr;
			if (projectile.getShooter() == null) return;
			else if (projectile.getShooter().equals(e.getEntity()))
				return;
			else if (projectile.getShooter() instanceof Entity)
				dmgr = (Entity) projectile.getShooter();
		}
		if (e.getEntity() instanceof Player)
		{
			Player tagged = (Player) e.getEntity();
			
			if ((dmgr instanceof Player)/* && plugin.settings.playerTag()*/)
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

	Essentials essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
	public void onPlayerDamageByPlayer(Player damager, Player damaged)
	{
		if (essentials != null && essentials.getUser(damager) != null && !essentials.getUser(damager).isVanished())
			return;
		
		/*if (plugin.settings.blockCreativeTagging() && damager.getGameMode() == GameMode.CREATIVE)
		{
			damager.sendMessage(ChatColor.RED + "[CombatTag] You can't tag players while in creative mode!");
			return;
		}*/

		if (/*plugin.settings.isSendMessageWhenTagged() && */!plugin.isInCombat(damager.getUniqueId()))
		{
			String tagMessage = plugin.settings.getTagMessageDamager();
			tagMessage = tagMessage.replace("[player]", "" + damaged.getName());
			damager.sendMessage(ChatColor.RED + "[CombatTag] " + tagMessage);
		}
		if (/*plugin.settings.isSendMessageWhenTagged() && */!plugin.isInCombat(damaged.getUniqueId()))
		{
			String tagMessage = plugin.settings.getTagMessageDamaged();
			tagMessage = tagMessage.replace("[player]", "" + damager.getName());
			damaged.sendMessage(ChatColor.RED + "[CombatTag] " + tagMessage);
		}
		
		plugin.addTagged(damager, damaged);
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