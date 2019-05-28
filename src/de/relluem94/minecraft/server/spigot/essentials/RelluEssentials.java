package de.relluem94.minecraft.server.spigot.essentials;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import de.relluem94.minecraft.server.spigot.essentials.commands.Cookies;
import de.relluem94.minecraft.server.spigot.essentials.commands.Fly;
import de.relluem94.minecraft.server.spigot.essentials.commands.GameMode;
import de.relluem94.minecraft.server.spigot.essentials.commands.PortableCraftingBench;
import de.relluem94.minecraft.server.spigot.essentials.commands.Rain;
import de.relluem94.minecraft.server.spigot.essentials.commands.Storm;
import de.relluem94.minecraft.server.spigot.essentials.commands.Sun;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterBlockDrop;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterChatFormat;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerJoin;
import de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerQuit;




public class RelluEssentials extends JavaPlugin{
	
	private PluginManager pm = this.getServer().getPluginManager();
	
	@Override
	public void onEnable() {
		/*	Commands	*/
		this.getCommand("0").setExecutor(new GameMode());
		this.getCommand("1").setExecutor(new GameMode());
		this.getCommand("2").setExecutor(new GameMode());
		this.getCommand("3").setExecutor(new GameMode());
		this.getCommand("fly").setExecutor(new Fly());
		this.getCommand("cookie").setExecutor(new Cookies());
		this.getCommand("craft").setExecutor(new PortableCraftingBench());
		this.getCommand("sun").setExecutor(new Sun());
		this.getCommand("rain").setExecutor(new Rain());
		this.getCommand("storm").setExecutor(new Storm());
		
		
		/*	Events	*/
		pm.registerEvents(new BetterChatFormat(), this);
		pm.registerEvents(new BetterPlayerJoin(), this);
		pm.registerEvents(new BetterPlayerQuit(), this);
		pm.registerEvents(new BetterBlockDrop(), this);
	}
	
	@Override
	public void onDisable() {
		
	}
}
