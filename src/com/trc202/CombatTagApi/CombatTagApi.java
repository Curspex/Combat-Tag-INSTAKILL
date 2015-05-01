package com.trc202.CombatTagApi;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.trc202.CombatTag.CombatTag;

public class CombatTagApi {
	
	private CombatTag plugin;
	
	public CombatTagApi(CombatTag plugin)
	{
		this.plugin = plugin;
	}

	/**
	 * Checks to see if the player is in combat. The combat time can be configured by the server owner
	 * If the player has died while in combat the player is no longer considered in combat and as such will return false
	 * @param player
	 * @return true if player is in combat
	 */
	public boolean isInCombat(Player player)
	{
		return plugin.isInCombat(player.getUniqueId());
	}
	
	/**
	 * Checks to see if the player is in combat. The combat time can be configured by the server owner
	 * If the player has died while in combat the player is no longer considered in combat and as such will return false
	 * @param playerName
	 * @return true if player is online and in combat
	 */
	@SuppressWarnings("deprecation")
	public boolean isInCombat(String name)
	{
		Player player = Bukkit.getPlayerExact(name);
		if (player != null)
		{
			return plugin.isInCombat(player.getUniqueId());
		}
		return false;
	}
	
	/**
	 * Returns the time before the tag is over
	 *  -1 if the tag has expired
	 *  -2 if the player is not in combat
	 * @param player
	 */
	public long getRemainingTagTime(Player player)
	{
		if(plugin.isInCombat(player.getUniqueId()))
		{
			return plugin.getRemainingTagTime(player.getUniqueId());
		}
		else
		{
			return -1L;
		}
	}
	
	/**
	 * Returns the time before the tag is over
	 *  -1 if the tag has expired
	 *  -2 if the player is not in combat
	 * @param playerName
	 */
	@SuppressWarnings("deprecation")
	public long getRemainingTagTime(String name)
	{
		if(Bukkit.getPlayerExact(name) != null)
		{
			Player player = Bukkit.getPlayerExact(name);
			if(plugin.isInCombat(player.getUniqueId()))
			{
				return plugin.getRemainingTagTime(player.getUniqueId());
			}
			else
			{
				return -1L;
			}
		}
		return -2L;
	}
	
	/**
	 * Tags player
	 * @param player
	 * @return true if the action is successful, false if not
	 */
	public boolean tagPlayer(Player player)
	{
		return plugin.addTagged(player);
	}
	
	
	/**
	 * Untags player
	 * @param player
	 * @return nothing
	 */
	public void untagPlayer(Player player)
	{
		plugin.removeTagged(player.getUniqueId());
	}
	
	/**
	 * Returns the value of a configuration option with the specified name
	 * @param Name of config option
	 * @return String value of option
	 */
	public String getConfigOption(String configKey)
	{
		return plugin.getSettingsHelper().getProperty(configKey);
	}
}
