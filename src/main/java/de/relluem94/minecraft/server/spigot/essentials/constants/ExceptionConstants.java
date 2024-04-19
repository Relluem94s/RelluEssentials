package de.relluem94.minecraft.server.spigot.essentials.constants;

/**
 *
 * @author rellu
 */
public class ExceptionConstants {

    private ExceptionConstants() {
        throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
    }

    //==============================================================================//
    //                         EXCEPTION   STUFF                                    //
    //==============================================================================//
    public static final String PLUGIN_EXCEPTION_WORLD_NOT_LOADED = "Can't unload a World (%s) that is not loaded.";
    public static final String PLUGIN_EXCEPTION_WORLD_NOT_FOUND = "No World found with name: %s";
}
