package de.relluem94.minecraft.server.spigot.essentials.contexts;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import de.relluem94.minecraft.server.spigot.essentials.registries.ItemRegistry;
import de.relluem94.minecraft.server.spigot.essentials.services.ClipboardService;
import de.relluem94.minecraft.server.spigot.essentials.services.ItemService;
import org.junit.jupiter.api.Test;

class ServiceContextTest {

  @Test
  void copyFrom_shouldCopyAllFieldValuesFromSourceToTarget() {
    ServiceContext source = new ServiceContext();
    ClipboardService clipboardService = new ClipboardService();
    ItemService itemService = new ItemService(new ItemRegistry());
    source.setClipboardService(clipboardService);
    source.setItemService(itemService);

    ServiceContext target = new ServiceContext();
    target.copyFrom(source);

    assertSame(clipboardService, target.getClipboardService());
    assertSame(itemService, target.getItemService());
  }

  @Test
  void copyFrom_shouldNotOverwriteTargetFieldsThatAreNullInSource() {
    ServiceContext source = new ServiceContext();

    ServiceContext target = new ServiceContext();
    ClipboardService existingClipboardService = new ClipboardService();
    target.setClipboardService(existingClipboardService);

    target.copyFrom(source);

    assertNull(target.getClipboardService());
  }

  @Test
  void copyFrom_shouldLeaveTargetEmptyWhenSourceIsEmpty() {
    ServiceContext source = new ServiceContext();
    ServiceContext target = new ServiceContext();

    target.copyFrom(source);

    assertNull(target.getClipboardService());
    assertNull(target.getTranslationService());
  }

  @Test
  void copyFrom_shouldNotAffectSourceAfterCopy() {
    ServiceContext source = new ServiceContext();
    ClipboardService clipboardService = new ClipboardService();
    source.setClipboardService(clipboardService);

    ServiceContext target = new ServiceContext();
    target.copyFrom(source);

    assertSame(clipboardService, source.getClipboardService());
  }

  @Test
  void copyFrom_shouldOverwriteExistingTargetFieldsWithSourceValues() {
    ServiceContext source = new ServiceContext();
    ClipboardService sourceClipboardService = new ClipboardService();
    source.setClipboardService(sourceClipboardService);

    ServiceContext target = new ServiceContext();
    ClipboardService targetClipboardService = new ClipboardService();
    target.setClipboardService(targetClipboardService);

    target.copyFrom(source);

    assertSame(sourceClipboardService, target.getClipboardService());
    assertNotSame(targetClipboardService, target.getClipboardService());
  }

  @Test
  void copyFrom_shouldWorkForSubclassInheritingServiceContext() {
    ServiceContext source = new ServiceContext();
    ClipboardService clipboardService = new ClipboardService();
    source.setClipboardService(clipboardService);

    ExtendedServiceContext extendedTarget = new ExtendedServiceContext();
    extendedTarget.copyFrom(source);

    assertSame(clipboardService, extendedTarget.getClipboardService());
  }

  private static class ExtendedServiceContext extends ServiceContext {

  }
}