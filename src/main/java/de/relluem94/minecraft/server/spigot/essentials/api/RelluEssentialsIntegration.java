package de.relluem94.minecraft.server.spigot.essentials.api;

public interface RelluEssentialsIntegration {

    String getPluginName();
    String getPluginVersion();

    void onRelluEssentialsInit(RelluEssentialsAPI api);
    void onRelluEssentialsShutdown();
}