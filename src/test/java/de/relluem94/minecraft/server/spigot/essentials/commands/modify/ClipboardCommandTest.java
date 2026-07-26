package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.helpers.LanguageHelper;
import de.relluem94.minecraft.server.spigot.essentials.helpers.objects.Selection;
import de.relluem94.minecraft.server.spigot.essentials.helpers.pojo.ModifyClipboardEntry;
import de.relluem94.rellulib.stores.DoubleStore;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.HashMap;
import java.util.List;

import static org.mockito.Mockito.*;

class ClipboardCommandTest {

    private Player player;
    private ClipboardCommand clipboardCommand;
    private RelluEssentials relluEssentialsMock;

    private MockedStatic<RelluEssentials> mockedRelluEssentials;

    @BeforeEach
    void setUp() {
        player = mock(Player.class);

        relluEssentialsMock = mock(RelluEssentials.class);
        LanguageHelper languageHelperMock = mock(LanguageHelper.class);

        mockedRelluEssentials = mockStatic(RelluEssentials.class);
        mockedRelluEssentials.when(RelluEssentials::getInstance).thenReturn(relluEssentialsMock);
        RelluEssentials.languageHelper = languageHelperMock;

        when(languageHelperMock.getWithPrefix(any())).thenReturn("msg");

        clipboardCommand = new ClipboardCommand();
    }

    @AfterEach
    void tearDown() {
        mockedRelluEssentials.close();
    }

    @Test
    void execute_withNoClipboardEntry_sendsNoClipboardMessage() {
        relluEssentialsMock.clipboard = new HashMap<>();

        clipboardCommand.execute(player, new String[]{"clipboard", "rotate"});

        verify(player).sendMessage(anyString());
    }

    @Test
    void execute_withNullClipboardList_sendsNoClipboardMessage() {
        relluEssentialsMock.clipboard = new HashMap<>();
        Selection selectionMock = mock(Selection.class);
        relluEssentialsMock.clipboard.put(player, new DoubleStore<>(selectionMock, null));

        clipboardCommand.execute(player, new String[]{"clipboard", "rotate"});

        verify(player).sendMessage(anyString());
    }

    @Test
    void execute_withEmptyClipboardList_sendsNoClipboardMessage() {
        relluEssentialsMock.clipboard = new HashMap<>();
        Selection selectionMock = mock(Selection.class);
        relluEssentialsMock.clipboard.put(player, new DoubleStore<>(selectionMock, List.of()));

        clipboardCommand.execute(player, new String[]{"clipboard", "rotate"});

        verify(player).sendMessage(anyString());
    }

    @Test
    void execute_withValidClipboard_rotatesAndUpdatesClipboard() {
        relluEssentialsMock.clipboard = new HashMap<>();
        Selection selectionMock = mock(Selection.class);
        ModifyClipboardEntry entryMock = mock(ModifyClipboardEntry.class);
        List<ModifyClipboardEntry> clipboardList = List.of(entryMock);
        relluEssentialsMock.clipboard.put(player, new DoubleStore<>(selectionMock, clipboardList));

        try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
                     mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

            DoubleStore<Selection, List<ModifyClipboardEntry>> rotatedStore =
                    new DoubleStore<>(selectionMock, List.of(entryMock));

            modifyHelper.when(() -> de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.rotate(eq(clipboardList), eq(selectionMock)))
                    .thenReturn(rotatedStore);

            clipboardCommand.execute(player, new String[]{"clipboard", "rotate"});

            verify(player).sendMessage(anyString());
        }
    }

    @Test
    void matches_withCorrectArgs_returnsTrue() {
        assert clipboardCommand.matches(new String[]{"clipboard", "rotate"});
    }

    @Test
    void matches_withWrongSubCommand_returnsFalse() {
        assert !clipboardCommand.matches(new String[]{"clipboard", "flip"});
    }

    @Test
    void matches_withWrongCommand_returnsFalse() {
        assert !clipboardCommand.matches(new String[]{"set", "rotate"});
    }

    @Test
    void matches_withTooFewArgs_returnsFalse() {
        assert !clipboardCommand.matches(new String[]{"clipboard"});
    }

    @Test
    void matches_withTooManyArgs_returnsFalse() {
        assert !clipboardCommand.matches(new String[]{"clipboard", "rotate", "extra"});
    }
}