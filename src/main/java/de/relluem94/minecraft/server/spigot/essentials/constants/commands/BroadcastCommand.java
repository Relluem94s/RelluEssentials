
package de.relluem94.minecraft.server.spigot.essentials.constants.commands;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.Getter;

@CommandName("broadcast")
public class BroadcastCommand implements CommandConstruct {

    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }

    @Getter
    public enum Commands implements CommandsEnum {
        title("title"),
        CHAT("chat");


        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }
}

