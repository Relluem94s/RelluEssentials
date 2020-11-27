package main.java.de.relluem94.minecraft.server.spigot.essentials;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Cookies;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Day;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Enderchest;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Fly;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.GameMode;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Inventory;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.More;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Nick;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Night;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.PermissionsGroup;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.PortableCraftingBench;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Repair;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Rain;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Spawn;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Storm;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Suicide;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Sun;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.BetterBlockDrop;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.BetterChatFormat;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.BetterMobs;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerJoin;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.BetterPlayerQuit;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.BetterSavety;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.BetterSoil;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.MOTD;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.NoDeathMessage;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.features.RotationTool;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.features.SelectionTool;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_AutoReplant;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_Repair;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_Salvage;
import main.java.de.relluem94.minecraft.server.spigot.essentials.events.skills.Ev_TreeFeller;
import main.java.de.relluem94.minecraft.server.spigot.essentials.helpers.Vector2Location;
import main.java.de.relluem94.minecraft.server.spigot.essentials.permissions.User;
import java.util.Objects;
import main.java.de.relluem94.minecraft.server.spigot.essentials.commands.Home;




public class RelluEssentials extends JavaPlugin{
	
	public static PluginManager pm = Bukkit.getServer().getPluginManager();
	public static ScoreboardManager sm = Bukkit.getServer().getScoreboardManager();
	public static Scoreboard board;
	public static HashMap<User, Vector2Location> selections = new HashMap<User, Vector2Location>();
	
	@Override
	public void onEnable() {
		configManager(true);
		commandManager();
		eventManager();
		featureManager();
		skillManager();
		boardManager();
		groupManager();
	}
	
	@Override
	public void onDisable() {
		configManager(false);
	}
	
	
	private void configManager(boolean enable) {
		/*  Config */
		if(enable) {
			this.saveDefaultConfig();
		}
		else {
			this.saveConfig();
		}
	}
	
	private void commandManager() {
		/*	Commands	*/
		Objects.requireNonNull(this.getCommand("0")).setExecutor(new GameMode());
		Objects.requireNonNull(this.getCommand("1")).setExecutor(new GameMode());
		Objects.requireNonNull(this.getCommand("2")).setExecutor(new GameMode());
		Objects.requireNonNull(this.getCommand("3")).setExecutor(new GameMode());
		Objects.requireNonNull(this.getCommand("fly")).setExecutor(new Fly());
		Objects.requireNonNull(this.getCommand("cookie")).setExecutor(new Cookies());
		Objects.requireNonNull(this.getCommand("craft")).setExecutor(new PortableCraftingBench());
		Objects.requireNonNull(this.getCommand("sun")).setExecutor(new Sun());
		Objects.requireNonNull(this.getCommand("rain")).setExecutor(new Rain());
		Objects.requireNonNull(this.getCommand("storm")).setExecutor(new Storm());
		Objects.requireNonNull(this.getCommand("spawn")).setExecutor(new Spawn());
		Objects.requireNonNull(this.getCommand("home")).setExecutor(new Home());
		Objects.requireNonNull(this.getCommand("day")).setExecutor(new Day());
		Objects.requireNonNull(this.getCommand("night")).setExecutor(new Night());
		Objects.requireNonNull(this.getCommand("more")).setExecutor(new More());
		Objects.requireNonNull(this.getCommand("repair")).setExecutor(new Repair());
		Objects.requireNonNull(this.getCommand("enderchest")).setExecutor(new Enderchest());
		Objects.requireNonNull(this.getCommand("inv")).setExecutor(new Inventory());
		Objects.requireNonNull(this.getCommand("setGroup")).setExecutor(new PermissionsGroup());
		Objects.requireNonNull(this.getCommand("nick")).setExecutor(new Nick());
		Objects.requireNonNull(this.getCommand("suicide")).setExecutor(new Suicide());
	}
	
	private void eventManager() {
		/*	Events	*/
		pm.registerEvents(new BetterChatFormat(), this);
		pm.registerEvents(new BetterPlayerJoin(), this);
		pm.registerEvents(new BetterPlayerQuit(), this);
		pm.registerEvents(new BetterBlockDrop(), this);
		pm.registerEvents(new BetterMobs(), this);
		pm.registerEvents(new BetterSoil(), this);
		pm.registerEvents(new BetterSavety(), this);
		pm.registerEvents(new NoDeathMessage(), this);
		pm.registerEvents(new MOTD(), this);
	}
	
	private void featureManager() {
		pm.registerEvents(new SelectionTool(), this);
		pm.registerEvents(new RotationTool(), this);
	}
	
	private void skillManager() {
		/*  Skill Events */
		pm.registerEvents(new Ev_Repair(), this);
		pm.registerEvents(new Ev_Salvage(), this);
		pm.registerEvents(new Ev_TreeFeller(), this);
		pm.registerEvents(new Ev_AutoReplant(), this);
	}
	
	private void boardManager() {
		if(sm == null) {
			sm = Bukkit.getServer().getScoreboardManager();
		}
		
		board = sm.getNewScoreboard();
		Objective o = board.registerNewObjective("player deaths", "death", "player deaths");
		o.setDisplaySlot(DisplaySlot.SIDEBAR);
	}
	
	private void groupManager() {
		for(Player p : Bukkit.getOnlinePlayers()) {
			new User(p, User.getGroup(p));
		}
	}

}
