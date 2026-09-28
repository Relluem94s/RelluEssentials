package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.ClipboardService;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.List;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class ClipboardCommandTest {

  private Player player;
  private ClipboardCommand clipboardCommand;
  private ClipboardService clipboardService;

  @BeforeEach
  void setUp() {
    player = mock(Player.class);
    clipboardService = new ClipboardService();

    TranslationService translationServiceMock = mock(TranslationService.class);
    when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");

    ServiceContext serviceContext = mock(ServiceContext.class);
    when(serviceContext.getTranslationService()).thenReturn(translationServiceMock);
    when(serviceContext.getClipboardService()).thenReturn(clipboardService);

    clipboardCommand = new ClipboardCommand(serviceContext);
  }

  @Test
  void executeWithNoClipboardEntrySendsNoClipboardMessage() {
    clipboardCommand.execute(player, new String[]{"clipboard", "rotate"});

    verify(player).sendMessage(anyString());
  }

  @Test
  void executeWithNullClipboardListSendsNoClipboardMessage() {
    Selection selectionMock = mock(Selection.class);
    clipboardService.setClipboard(player, new DoubleStore<>(selectionMock, null));

    clipboardCommand.execute(player, new String[]{"clipboard", "rotate"});

    verify(player).sendMessage(anyString());
  }

  @Test
  void executeWithEmptyClipboardListSendsNoClipboardMessage() {
    Selection selectionMock = mock(Selection.class);
    clipboardService.setClipboard(player, new DoubleStore<>(selectionMock, List.of()));

    clipboardCommand.execute(player, new String[]{"clipboard", "rotate"});

    verify(player).sendMessage(anyString());
  }

  @Test
  void executeWithValidClipboardRotatesAndUpdatesClipboard() {
    Selection selectionMock = mock(Selection.class);
    ModifyClipboardEntry entryMock = mock(ModifyClipboardEntry.class);
    List<ModifyClipboardEntry> clipboardList = List.of(entryMock);
    clipboardService.setClipboard(player, new DoubleStore<>(selectionMock, clipboardList));

    try (MockedStatic<de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper> modifyHelper =
        mockStatic(de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.class)) {

      DoubleStore<Selection, List<ModifyClipboardEntry>> rotatedStore =
          new DoubleStore<>(selectionMock, List.of(entryMock));

      modifyHelper.when(
              () -> de.relluem94.minecraft.server.spigot.essentials.helpers.ModifyHelper.rotate(
                  eq(clipboardList), eq(selectionMock)))
          .thenReturn(rotatedStore);

      clipboardCommand.execute(player, new String[]{"clipboard", "rotate"});

      verify(player).sendMessage(anyString());
    }
  }

  @Test
  void matchesWithCorrectArgsReturnsTrue() {
    assert clipboardCommand.matches(new String[]{"clipboard", "rotate"});
  }

  @Test
  void matchesWithWrongSubCommandReturnsFalse() {
    assert !clipboardCommand.matches(new String[]{"clipboard", "flip"});
  }

  @Test
  void matchesWithWrongCommandReturnsFalse() {
    assert !clipboardCommand.matches(new String[]{"set", "rotate"});
  }

  @Test
  void matchesWithTooFewArgsReturnsFalse() {
    assert !clipboardCommand.matches(new String[]{"clipboard"});
  }

  @Test
  void matchesWithTooManyArgsReturnsFalse() {
    assert !clipboardCommand.matches(new String[]{"clipboard", "rotate", "extra"});
  }
}