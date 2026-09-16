package de.relluem94.minecraft.server.spigot.essentials.discovery.commandfixtures;

import de.relluem94.minecraft.server.spigot.essentials.annotations.CommandName;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandConstruct;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.CommandsEnum;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

@SuppressWarnings("unused")
@CommandName("AnnotatedCommandFixture")
public class AnnotatedCommandFixture implements CommandConstruct {

  @Override
  public CommandsEnum[] getCommands() {
    return new CommandsEnum[0];
  }

  @Override
  public void injectContext(ServiceContext context) {

  }

  @Override
  public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command,
      @NonNull String label, @NonNull String[] args) {
    return false;
  }

  @Override
  public @Nullable List<String> onTabComplete(@NonNull CommandSender sender,
      @NonNull Command command, @NonNull String label, @NonNull String[] args) {
    return List.of();
  }
}