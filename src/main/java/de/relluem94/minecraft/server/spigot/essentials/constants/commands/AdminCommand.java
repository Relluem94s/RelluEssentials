package de.relluem94.minecraft.server.spigot.essentials.constants.commands;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.Getter;

@Getter
@CommandName("admin")
public class AdminCommand implements CommandConstruct {
    @Override
    public CommandsEnum[] getCommands() {
        return Commands.values();
    }


    @Getter
    public enum Commands implements CommandsEnum {
        AFK("afk"),
        CLEAN_PROTECTIONS("cleanProtections"),
        CHAT("chat"),
        LIGHT("light"),
        NPC("npc"),
        PING("ping"),
        TOP("top");


        private final String name;
        private final String[] subCommands;

        Commands(String name, String... subCommands) {
            this.name = name;
            this.subCommands = subCommands;
        }
    }
}

