package de.relluem94.minecraft.server.spigot.essentials.helpers.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.Properties;
import java.util.UUID;

/**
 *
 * @author rellu
 */
@Setter
@Getter
public class OfflinePlayerEntry {

    private UUID id;
    private String name;
    private Properties properties;
}