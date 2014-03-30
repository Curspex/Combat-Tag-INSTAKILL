package com.trc202.CombatTagListeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.trc202.CombatTag.CombatTagInstakill;

public class NoPvpBlockListener implements Listener{

	CombatTagInstakill plugin;
	
	public NoPvpBlockListener(CombatTagInstakill combatTag){
		this.plugin = combatTag;
	}
	
	@EventHandler
	public void onBlockBreakEvent(BlockBreakEvent event){
		if(event.isCancelled()){return;}
		Player player = event.getPlayer();
		if(plugin.isInCombat(player.getName())){
			if(!isBlockEditWhileTagged()){
				player.sendMessage(ChatColor.RED + "[Combat Tag] You can't break blocks while tagged.");
				event.setCancelled(true);
				
			}
		}
	}
	@EventHandler
	public void onBlockPlaceEvent(BlockPlaceEvent event){
		if(event.isCancelled()){return;}
		Player player = event.getPlayer();
		if(plugin.isInCombat(player.getName())){
			if(!isBlockEditWhileTagged()){
				player.sendMessage(ChatColor.RED + "[Combat Tag] You can't place blocks while tagged.");
				event.setCancelled(true);
			}
		}
	}
	
	public boolean isBlockEditWhileTagged(){
		return plugin.settings.isBlockEditWhileTagged();
	}
}
