package de.relluem94.minecraft.server.spigot.essentials;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.relluem94.minecraft.server.spigot.essentials.commands.Fly;
import de.relluem94.minecraft.server.spigot.essentials.commands.GameMode;

public class RelluEssentials extends JavaPlugin{
	
	@SuppressWarnings("unused")
	private PluginManager pm = this.getServer().getPluginManager();
	
	@Override
	public void onEnable() {
		this.getCommand("0").setExecutor(new GameMode());
		this.getCommand("1").setExecutor(new GameMode());
		this.getCommand("2").setExecutor(new GameMode());
		this.getCommand("3").setExecutor(new GameMode());

		this.getCommand("fly").setExecutor(new Fly());
		//pm.registerEvent();
	}
	
	@Override
	public void onDisable() {
		
	}
}
