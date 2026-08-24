package de.relluem94.minecraft.server.spigot.essentials.constants;

/**
 *
 * @author rellu
 */
public class ExceptionConstants {

  private ExceptionConstants() {
    throw new IllegalStateException(Constants.PLUGIN_INTERNAL_UTILITY_CLASS);
  }

  public static final String PLUGIN_EXCEPTION_WORLD_NOT_LOADED = "Can't unload a World (%s) that is not loaded.";
  public static final String PLUGIN_EXCEPTION_WORLD_NOT_FOUND = "No World found with name: %s";
  public static final String PLUGIN_EXCEPTION_SIGNHELPER_SIGN_MISSING_CUSTOM_INPUT = "Sign is missing custom input";
  public static final String PLUGIN_EXCEPTION_ITEMHELPER_UNABLE_TO_SAVE_ITEMSTACK = "Unable to save itemstack.";
  public static final String PLUGIN_EXCEPTION_ITEMHELPER_UNABLE_TO_DECODE_CLASS_TYPE = "Unable to decode class type.";
  public static final String PLUGIN_EXCEPTION_ITEMHELPER_INVALID_BASE64_DATA = "Invalid Base64 data: %s";
  public static final String PLUGIN_EXCEPTION_ITEMHELPER_NAME_NOT_FOUND = "ERROR_404_NAME_NOT_FOUND_EXCEPTION";
  public static final String PLUGIN_EXCEPTION_NPC_UNIMPLEMENTED_METHOD = "Unimplemented method 'getMainGUI'";
  public static final String PLUGIN_EXCEPTION_INVENTORY_REGISTRY = "Inventory already registered: %s";
  public static final String PLUGIN_EXCEPTION_ITEM_REGISTRY = "Item already registered: %s";
  public static final String PLUGIN_EXCEPTION_PLAYERSERVICE_ALREADY_INITIALIZED = "PlayerService is already initialized";
  public static final String PLUGIN_EXCEPTION_LOCATION_TYPE_NOT_FOUND = "LocationType WARP not found in DB";


}
