package de.relluem94.minecraft.server.spigot.essentials.registries;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import de.relluem94.minecraft.server.spigot.essentials.interfaces.SubCommand;
import java.util.List;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.Test;

class SubCommandRegistryTest {

    private record FixedMatchSubCommand(boolean shouldMatch) implements SubCommand {

        @Override
            public void execute(Player player, String[] args) {
        }

            @Override
            public boolean matches(String[] args) {
                return shouldMatch;
            }
        }

    @Test
    void find_returnsFirstMatchingSubCommand() {
        SubCommand nonMatching = new FixedMatchSubCommand(false);
        SubCommand matching = new FixedMatchSubCommand(true);
        SubCommandRegistry<SubCommand> registry = new SubCommandRegistry<>(List.of(nonMatching, matching));

        SubCommand result = registry.find(new String[]{"anything"});

        assertSame(matching, result);
    }

    @Test
    void find_returnsNullWhenNoSubCommandMatches() {
        SubCommand nonMatching = new FixedMatchSubCommand(false);
        SubCommandRegistry<SubCommand> registry = new SubCommandRegistry<>(List.of(nonMatching));

        SubCommand result = registry.find(new String[]{"anything"});

        assertNull(result);
    }

    @Test
    void find_returnsNullForEmptyRegistry() {
        SubCommandRegistry<SubCommand> registry = new SubCommandRegistry<>(List.of());

        SubCommand result = registry.find(new String[]{"anything"});

        assertNull(result);
    }

    @Test
    void find_returnsFirstWhenMultipleSubCommandsMatch() {
        SubCommand firstMatch = new FixedMatchSubCommand(true);
        SubCommand secondMatch = new FixedMatchSubCommand(true);
        SubCommandRegistry<SubCommand> registry = new SubCommandRegistry<>(List.of(firstMatch, secondMatch));

        SubCommand result = registry.find(new String[]{"anything"});

        assertSame(firstMatch, result);
    }
}