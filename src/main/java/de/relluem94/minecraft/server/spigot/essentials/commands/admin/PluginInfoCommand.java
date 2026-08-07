package de.relluem94.minecraft.server.spigot.essentials.commands.admin;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Admin;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.RelluEssentialsIntegration;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.registries.RelluEssentialsRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import java.util.List;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

public class PluginInfoCommand implements SubCommand {

  private final TranslationService translationService;

  public PluginInfoCommand(ServiceContext context) {
    this.translationService = context.getTranslationService();
  }

  @Override
  public void execute(Player player, String[] args) {
    player.sendMessage(translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_INFO_VERSION,
        RelluEssentials.getInstance().getDescription().getVersion()));

    List<RelluEssentialsIntegration> integrations = RelluEssentialsRegistry.getInstance()
        .getIntegrations();
    if (integrations.isEmpty()) {
      player.sendMessage(
          translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_INFO_INTEGRATIONS_NONE));
    } else {
      player.sendMessage(
          translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_INFO_INTEGRATIONS_HEADER,
              integrations.size()));
      for (RelluEssentialsIntegration integration : integrations) {
        player.sendMessage(
            translationService.getWithPrefix(MessageKey.COMMAND_ADMIN_INFO_INTEGRATIONS_ENTRY,
                integration.getPluginName(), integration.getPluginVersion()));
      }
    }
  }

  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 1 && Admin.Commands.INFO.getName().equalsIgnoreCase(args[0]);
  }
}