package main.java.de.relluem94.minecraft.server.spigot.essentials.helpers;

import org.bukkit.Location;

public class Vector2Location {
	private Location x, y;
	
	public Vector2Location(Location x, Location y) {
		this.x = x;
		this.y = y;
	}

	public Location getX() {
		return x;
	}

	public void setX(Location x) {
		this.x = x;
	}

	public Location getY() {
		return y;
	}

	public void setY(Location y) {
		this.y = y;
	}
}