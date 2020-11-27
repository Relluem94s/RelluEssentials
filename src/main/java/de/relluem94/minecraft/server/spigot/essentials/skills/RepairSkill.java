package de.relluem94.minecraft.server.spigot.essentials.skills;

import org.bukkit.scoreboard.Scoreboard;

public class RepairSkill extends Skill{
	
	private int id = 1;
	private float level;
	private float maxLevel = 100.0f;
	private float minLevel = 0.0f;
	//TODO Class for User Skills  Control
	
	public RepairSkill(Scoreboard board) {
		super(board, "Repair", "Repair Skill");
	}
}
