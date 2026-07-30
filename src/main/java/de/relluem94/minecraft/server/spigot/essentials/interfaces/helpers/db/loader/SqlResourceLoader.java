package de.relluem94.minecraft.server.spigot.essentials.interfaces.helpers.db.loader;

import java.io.FileNotFoundException;

public interface SqlResourceLoader {
    String load(String fileName) throws FileNotFoundException;
}