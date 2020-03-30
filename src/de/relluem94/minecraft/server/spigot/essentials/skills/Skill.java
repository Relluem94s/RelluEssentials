package de.relluem94.minecraft.server.spigot.essentials.skills;

import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;

public class Skill{
	
	private int id;
	private float level;
	private float maxLevel;
	private float minLevel = 0.0f;
	//TODO Skill Tree etc
	
	
	private Objective objective;
	
	public Skill(Scoreboard board, String name, String displayName) {
		if(board.getObjective(name) == null) {
			objective = board.registerNewObjective(name, "dummy", displayName);
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setRenderType(RenderType.INTEGER);
		}
	}
	
	public Objective getObjective() {
		return objective;
	}
}
