package de.relluem94.minecraft.server.spigot.essentials.wraper;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.AnnotationHelper;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import org.bukkit.command.PluginCommand;
import org.jetbrains.annotations.NotNull;

public class CommandWrapper {
    private final CommandConstruct construct;

    private boolean initialised = false;

    public CommandWrapper(@NotNull CommandConstruct construct){
        this.construct = construct;
    }

    public boolean hasSubCommands(){
        return construct.getCommands() == null;
    }

    public CommandsEnum[] getSubCommands(){
        return construct.getCommands();
    }

    public String getCommandName(){
        return AnnotationHelper.getCommandName(this.construct.getClass());
    }

    public void init(){
        if(initialised){
            return;
        }

        PluginCommand pluginCommand = RelluEssentials.getInstance().getCommand(getCommandName());

        if(pluginCommand == null){
            return;
        }

        pluginCommand.setExecutor(construct);
        initialised = true;
    }
}