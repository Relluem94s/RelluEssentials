package de.relluem94.minecraft.server.spigot.essentials.commands.modify;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.RelluEssentials;
import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.minecraft.server.spigot.essentials.services.TranslationService;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.List;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class ClipboardCommandTest {

  private Player player;
  private ClipboardCommand clipboardCommand;
  private RelluEssentials relluEssentialsMock;

  private MockedStatic<RelluEssentials> mockedRelluEssentials;

  @BeforeEach
  void setUp() {
    player = mock(Player.class);

    relluEssentialsMock = mock(RelluEssentials.class);

    mockedRelluEssentials = mockStatic(RelluEssentials.class);
    mockedRelluEssentials.when(RelluEssentials::getInstance).thenReturn(relluEssentialsMock);

    TranslationService translationServiceMock = mock(TranslationService.class);
    when(translationServiceMock.getWithPrefix(any())).thenReturn("msg");

    ServiceContext serviceContext = mock(ServiceContext.class);
    when(serviceContext.getTranslationService()).thenReturn(translationServiceMock);

    clipboardCommand = new ClipboardCommand(serviceContext);
  }

  @AfterEach
  void tearDown() {
    mockedRelluEssentials.close();
  }

  @Test
  void execute_withNoClipboardEntry_sendsNoClipboardMessage() {

    clipboardCommand.execute(player, new String[]{"clipboard", "rotate"});

    verify(player).sendMessage(anyString());
  }

  @Test
  void execute_withNullClipboardList_sendsNoClipboardMessage() {
    Selection selectionMock = mock(Selection.class);
    relluEssentialsMock.getClipboard().put(player, new DoubleStore<>(selectionMock, null));

    clipboardCommand.execute(player, new String[]{"clipboard", "rotate"});

    verify(player).sendMessage(anyString());
  }

  @Test
  void execute_withEmptyClipboardList_sendsNoClipboardMessage() {
    Selection selectionMock = mock(Selection.class);
    relluEssentialsMock.getClipboard().put(player, new DoubleStore<>(selectionMock, List.of()));

    clipboardCommand.execute(player, new String[]{"clipboard", "rotate"});

    verify(player).sendMessage(anyString());
  }

  @Test
  void execute_withValidClipboard_rotatesAndUpdatesClipboard() {
    Selection selectionMock = mock(Selection.class);
    ModifyClipboardEntry entryMock = mock(ModifyClipboardEntry.class);
    List<ModifyClipboardEntry> clipboardList = List.of(entryMock);
    relluEssentialsMock.getClipboard().put(player, new DoubleStore<>(selectionMock, clipboardList));

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