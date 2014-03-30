package com.trc202.CombatTagListeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import com.trc202.CombatTag.CombatTagInstakill;


public class CombatTagCommandPrevention implements Listener{
	
	CombatTagInstakill plugin;
	
	public CombatTagCommandPrevention(CombatTagInstakill plugin){
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event)
    {
		Player player = event.getPlayer();
		if(plugin.isInCombat(player.getName())){
			String command = event.getMessage();
			for(String disabledCommand : plugin.settings.getDisabledCommands()){
				if (disabledCommand.equalsIgnoreCase("all") && !command.equalsIgnoreCase("/ct") && !command.equalsIgnoreCase("/combattag")){
					player.sendMessage(ChatColor.RED + "[CombatTag] All commands are disabled while in combat");
					event.setCancelled(true);
					return;
				}
				if(command.indexOf(" ") == disabledCommand.length()){
					if(command.substring(0, command.indexOf(" ")).equalsIgnoreCase(disabledCommand)){
						if(plugin.isDebugEnabled()){plugin.log.info("[CombatTag] Combat Tag has blocked the command: " + disabledCommand + " .");}
						player.sendMessage(ChatColor.RED + "[CombatTag] This command is disabled while in combat");
						event.setCancelled(true);
						return;
					}
				} else if(disabledCommand.indexOf(" ") > 0){
					if(command.toLowerCase().startsWith(disabledCommand.toLowerCase())){
						if(plugin.isDebugEnabled()){plugin.log.info("[CombatTag] Combat Tag has blocked the command: " + disabledCommand + " .");}
						player.sendMessage(ChatColor.RED + "[CombatTag] This command is disabled while in combat");
						event.setCancelled(true);
						return;
					}
				} else if(command.indexOf(" ") == -1 && command.equalsIgnoreCase(disabledCommand)){
					if(plugin.isDebugEnabled()){plugin.log.info("[CombatTag] Combat Tag has blocked the command: " + disabledCommand + " .");}
					player.sendMessage(ChatColor.RED + "[CombatTag] This command is disabled while in combat");
					event.setCancelled(true);
					return;
				}
			}
		}
	}
}

