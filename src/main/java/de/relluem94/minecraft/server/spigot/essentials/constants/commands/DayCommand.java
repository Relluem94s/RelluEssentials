
package de.relluem94.minecraft.server.spigot.essentials.constants.commands;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import lombok.Getter;

@Getter
@CommandName("day")
public class DayCommand implements CommandConstruct {
    @Override
    public CommandsEnum[] getCommands() {
        return null;
    }
}

