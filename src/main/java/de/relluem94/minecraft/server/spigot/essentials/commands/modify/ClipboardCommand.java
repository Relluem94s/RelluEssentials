package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.rotate;

import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.List;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

/**
 * Sub-command handler for clipboard operations within the modify command.
 * Handles rotation of the player's current clipboard content.
 *
 * @author rellu
 */
public class ClipboardCommand implements SubCommand {

  private static final String ROTATE_SUB_COMMAND = Modify.Commands.CLIPBOARD.getSubCommands()[0];
  private final ServiceContext serviceContext;

  /**
   * Creates a new ClipboardCommand with the given service context.
   *
   * @param context the service context providing access to clipboard and translation services
   */
  public ClipboardCommand(ServiceContext context) {
    this.serviceContext = context;
  }

  /**
   * Rotates the clipboard content of the given player.
   * Sends a failure message if the player has no clipboard content.
   *
   * @param player the player whose clipboard will be rotated
   * @param args   the command arguments passed by the player
   */
  @Override
  public void execute(Player player, String[] args) {
    DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardEntry =
        serviceContext.getClipboardService().getClipboard(player);
    if (clipboardEntry == null || clipboardEntry.getSecondValue() == null
        || clipboardEntry.getSecondValue().isEmpty()) {
      player.sendMessage(serviceContext.getTranslationService()
          .getWithPrefix(MessageKey.COMMAND_MODIFY_NO_CLIPBOARD));
      return;
    }

    serviceContext.getClipboardService().setClipboard(player,
        rotate(clipboardEntry.getSecondValue(), clipboardEntry.getValue()));
    player.sendMessage(serviceContext.getTranslationService()
            .getWithPrefix(MessageKey.COMMAND_MODIFY_CLIPBOARD_ROTATE_SUCCESS));
  }

  /**
   * Checks whether the given arguments match the clipboard rotate sub-command.
   * Expects exactly two arguments where the first matches the clipboard command name
   * and the second matches the rotate sub-command name.
   *
   * @param args the command arguments to evaluate
   * @return {@code true} if the arguments match the clipboard rotate sub-command,
   *     {@code false} otherwise
   */
  @Override
  public boolean matches(String @NonNull [] args) {
    return args.length == 2
        && Modify.Commands.CLIPBOARD.getName().equalsIgnoreCase(args[0])
        && ROTATE_SUB_COMMAND.equalsIgnoreCase(args[1]);
  }
}