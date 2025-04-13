package de.relluem94.minecraft.server.spigot.essentials.helpers.types;

import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.Location;

public class Vector2Location extends DoubleStore<Location, Location> {

    public Vector2Location(Location first, Location second) {
        super(first, second);
    }

    public Location getX() {
        return getValue();
    }

    public void setX(Location x) {
        setValue(x);
    }

    public Location getY() {
        return getSecondValue();
    }

    public void setY(Location y) {
        setSecondValue(y);
    }
}
