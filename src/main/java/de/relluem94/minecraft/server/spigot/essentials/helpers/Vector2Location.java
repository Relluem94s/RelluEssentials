package de.relluem94.minecraft.server.spigot.essentials.helpers;

import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.Location;

public class Vector2Location extends DoubleStore {

    public Vector2Location(Location first, Location second) {
        super(first, second);
    }

    public Location getX() {
        return (Location) getValue();
    }

    public void setX(Location x) {
        setValue(x);
    }

    public Location getY() {
        return (Location) getSecondValue();
    }

    public void setY(Location y) {
        setSecondValue(y);
    }
}
