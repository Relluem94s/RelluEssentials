package de.relluem94.minecraft.server.spigot.essentials.exceptions;

/**
 * Exception thrown when an operation is attempted on a Minecraft world
 * that is not currently loaded on the server.
 *
 * @author rellu
 * @version 1.0
 * @since 1.0
 */
public class WorldNotLoadedException extends Exception {

    /**
     * Constructs a new {@code WorldNotLoadedException} with the specified detail message.
     *
     * @param message the detail message describing which world is not loaded
     */
    public WorldNotLoadedException(String message) {
        super(message);
    }
}