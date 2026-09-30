package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import de.relluem94.minecraft.server.spigot.essentials.models.Selection;
import de.relluem94.minecraft.server.spigot.essentials.models.pojo.ModifyClipboardEntry;
import de.relluem94.rellulib.stores.DoubleStore;
import java.util.List;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClipboardServiceTest {

  @Mock
  private Player player;

  @Mock
  private DoubleStore<Selection, List<ModifyClipboardEntry>> clipboard;

  private ClipboardService clipboardService;

  @BeforeEach
  void setUp() {
    clipboardService = new ClipboardService();
  }

  @Test
  void getClipboardReturnsNullWhenNoClipboardAssigned() {
    assertNull(clipboardService.getClipboard(player));
  }

  @Test
  void setClipboardAssignsClipboardToPlayer() {
    clipboardService.setClipboard(player, clipboard);

    assertAll(
        () -> assertSame(clipboard, clipboardService.getClipboard(player))
    );
  }

  @Test
  void getClipboardReturnsCorrectClipboardAfterSet() {
    clipboardService.setClipboard(player, clipboard);

    assertSame(clipboard, clipboardService.getClipboard(player));
  }

  @Test
  void removeClipboardRemovesAssignedClipboard() {
    clipboardService.setClipboard(player, clipboard);
    clipboardService.removeClipboard(player);

    assertNull(clipboardService.getClipboard(player));
  }

  @Test
  void removeClipboardOnPlayerWithNoClipboardDoesNotThrow() {
    clipboardService.removeClipboard(player);

    assertNull(clipboardService.getClipboard(player));
  }

  @Test
  void setClipboardOverwritesPreviousClipboard() {
    @SuppressWarnings("unchecked")
    DoubleStore<Selection, List<ModifyClipboardEntry>> secondClipboard =
        org.mockito.Mockito.mock(DoubleStore.class);

    clipboardService.setClipboard(player, clipboard);
    clipboardService.setClipboard(player, secondClipboard);

    assertAll(
        () -> assertSame(secondClipboard, clipboardService.getClipboard(player))
    );
  }

  @Test
  void setClipboardWithNullClipboardStoresNull() {
    clipboardService.setClipboard(player, null);

    assertNull(clipboardService.getClipboard(player));
  }
}