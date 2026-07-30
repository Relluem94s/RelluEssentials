package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.commands.Modify;
import de.relluem94.minecraft.server.spigot.essentials.enums.MessageKey;
import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import de.relluem94.minecraft.server.spigot.essentials.model.Selection;
import de.relluem94.minecraft.server.spigot.essentials.model.pojo.ModifyClipboardEntry;
import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import java.util.List;

import static de.relluem94.minecraft.server.spigot.essentials.RelluEssentials.languageHelper;
import static de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.rotate;

public class ClipboardCommand implements SubCommand {

    private static final String ROTATE_SUB_COMMAND = Modify.Commands.CLIPBOARD.getSubCommands()[0];

    @Override
    public void execute(Player player, String[] args) {
        DoubleStore<Selection, List<ModifyClipboardEntry>> clipboardEntry = RelluEssentials.getInstance().clipboard.get(player);
        if (clipboardEntry == null || clipboardEntry.getSecondValue() == null || clipboardEntry.getSecondValue().isEmpty()) {
            player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_NO_CLIPBOARD));
            return;
        }

        RelluEssentials.getInstance().clipboard.put(player, rotate(clipboardEntry.getSecondValue(), clipboardEntry.getValue()));
        player.sendMessage(languageHelper.getWithPrefix(MessageKey.COMMAND_MODIFY_CLIPBOARD_ROTATE_SUCCESS));
    }

    @Override
    public boolean matches(String @NonNull [] args) {
        return args.length == 2
                && Modify.Commands.CLIPBOARD.getName().equalsIgnoreCase(args[0])
                && ROTATE_SUB_COMMAND.equalsIgnoreCase(args[1]);
    }
}