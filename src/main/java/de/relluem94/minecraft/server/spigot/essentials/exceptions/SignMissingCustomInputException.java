package de.relluem94.minecraft.server.spigot.essentials.exceptions;

/**
 * Exception thrown when a sign is missing required custom input.
 * <p>
 * This exception is used to indicate that a sign interaction or creation
 * failed because expected custom input was not provided or found.
 * </p>
 *
 * @author rellu
 */
public class SignMissingCustomInputException extends Exception {
    /**
     * Constructs a new {@code SignMissingCustomInputException} with the specified detail message.
     *
     * @param message the detail message describing the missing custom input
     */
    public SignMissingCustomInputException(String message) {
        super(message);
    }
}