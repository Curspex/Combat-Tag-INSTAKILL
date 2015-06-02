package com.trc202.CombatTagListeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.trc202.CombatTag.CombatTag;

public class NoPvpBlockListener implements Listener {

	CombatTag plugin;
	
	public NoPvpBlockListener(CombatTag combatTag)
	{
		this.plugin = combatTag;
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onBlockBreakEvent(BlockBreakEvent event)
	{
		Player player = event.getPlayer();
		if(plugin.isInCombat(player.getUniqueId()) && !player.hasMetadata(CombatTag.IGNORE_META))
		{
			if(!isBlockEditWhileTagged())
			{
				player.sendMessage(ChatColor.RED + "[Combat Tag] You can't break blocks while tagged.");
				event.setCancelled(true);
			}
		}
	}
	@EventHandler(ignoreCancelled = true)
	public void onBlockPlaceEvent(BlockPlaceEvent event)
	{
		Player player = event.getPlayer();
		if(plugin.isInCombat(player.getUniqueId()) && !player.hasMetadata(CombatTag.IGNORE_META))
		{
			if(!isBlockEditWhileTagged())
			{
				player.sendMessage(ChatColor.RED + "[Combat Tag] You can't place blocks while tagged.");
				event.setCancelled(true);
			}
		}
	}
	
	public boolean isBlockEditWhileTagged()
	{
		return plugin.settings.isBlockEditWhileTagged();
	}
}
