package de.relluem94.minecraft.server.spigot.essentials.npcs;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.relluem94.minecraft.server.spigot.essentials.contexts.ServiceContext;
import de.relluem94.minecraft.server.spigot.essentials.services.SchedulerService;
import org.bukkit.entity.Mannequin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NpcMannequinAttributeApplierTest {

  @Mock
  private ServiceContext serviceContext;

  @Mock
  private SchedulerService schedulerService;

  @Mock
  private Mannequin mannequin;

  private NpcMannequinAttributeApplier applier;

  @BeforeEach
  void setUp() {
    when(serviceContext.getSchedulerService()).thenReturn(schedulerService);
    applier = new NpcMannequinAttributeApplier(serviceContext);
  }

  @Test
  void applyAttributesSchedulesTaskWithDelayOf20Ticks() {
    applier.applyAttributes(mannequin);

    verify(schedulerService).runTaskLater(any(Runnable.class), eq(20L));
  }

  @Test
  void applyAttributesWhenTaskRunsSetsMannequinInvulnerable() {
    ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);

    applier.applyAttributes(mannequin);
    verify(schedulerService).runTaskLater(taskCaptor.capture(), eq(20L));
    taskCaptor.getValue().run();

    verify(mannequin).setInvulnerable(true);
  }

  @Test
  void applyAttributesWhenTaskRunsSetsMannequinNotCollidable() {
    ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);

    applier.applyAttributes(mannequin);
    verify(schedulerService).runTaskLater(taskCaptor.capture(), eq(20L));
    taskCaptor.getValue().run();

    verify(mannequin).setCollidable(false);
  }

  @Test
  void applyAttributesWhenTaskRunsPreventsMannequinFromPickingUpItems() {
    ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);

    applier.applyAttributes(mannequin);
    verify(schedulerService).runTaskLater(taskCaptor.capture(), eq(20L));
    taskCaptor.getValue().run();

    verify(mannequin).setCanPickupItems(false);
  }

  @Test
  void applyAttributesWhenTaskRunsSetsMannequinImmovable() {
    ArgumentCaptor<Runnable> taskCaptor = ArgumentCaptor.forClass(Runnable.class);

    applier.applyAttributes(mannequin);
    verify(schedulerService).runTaskLater(taskCaptor.capture(), eq(20L));
    taskCaptor.getValue().run();

    verify(mannequin).setImmovable(true);
  }
}