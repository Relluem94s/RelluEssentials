package de.relluem94.minecraft.server.spigot.essentials.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {

  @Mock
  private Plugin plugin;

  @Mock
  private Server server;

  @Mock
  private BukkitScheduler bukkitScheduler;

  @Mock
  private BukkitTask bukkitTask;

  @Mock
  private BukkitWorker bukkitWorker;

  private SchedulerService schedulerService;

  @BeforeEach
  void setUp() {
    when(plugin.getServer()).thenReturn(server);
    when(server.getScheduler()).thenReturn(bukkitScheduler);
    schedulerService = new SchedulerService(plugin);
  }

  @Test
  void runTaskDelegatesToSchedulerWithPlugin() {
    when(bukkitScheduler.runTask(eq(plugin), any(Runnable.class))).thenReturn(bukkitTask);

    BukkitTask result = schedulerService.runTask(() -> {
    });

    assertAll(() -> assertNotNull(result), () -> assertEquals(bukkitTask, result));
    verify(bukkitScheduler).runTask(eq(plugin), any(Runnable.class));
  }

  @Test
  void runTaskAsynchronouslyDelegatesToSchedulerWithPlugin() {
    when(bukkitScheduler.runTaskAsynchronously(eq(plugin), any(Runnable.class))).thenReturn(
        bukkitTask);

    BukkitTask result = schedulerService.runTaskAsynchronously(() -> {
    });

    assertAll(() -> assertNotNull(result), () -> assertEquals(bukkitTask, result));
    verify(bukkitScheduler).runTaskAsynchronously(eq(plugin), any(Runnable.class));
  }

  @Test
  void runTaskLaterDelegatesToSchedulerWithPluginAndDelay() {
    long delay = 20L;
    when(bukkitScheduler.runTaskLater(eq(plugin), any(Runnable.class), eq(delay))).thenReturn(
        bukkitTask);

    BukkitTask result = schedulerService.runTaskLater(() -> {
    }, delay);

    assertAll(() -> assertNotNull(result), () -> assertEquals(bukkitTask, result));
    verify(bukkitScheduler).runTaskLater(eq(plugin), any(Runnable.class), eq(delay));
  }

  @Test
  void runTaskLaterAsynchronouslyDelegatesToSchedulerWithPluginAndDelay() {
    long delay = 20L;
    when(bukkitScheduler.runTaskLaterAsynchronously(eq(plugin), any(Runnable.class),
        eq(delay))).thenReturn(bukkitTask);

    BukkitTask result = schedulerService.runTaskLaterAsynchronously(() -> {
    }, delay);

    assertAll(() -> assertNotNull(result), () -> assertEquals(bukkitTask, result));
    verify(bukkitScheduler).runTaskLaterAsynchronously(eq(plugin), any(Runnable.class), eq(delay));
  }

  @Test
  void runTaskTimerRunnableDelegatesToSchedulerWithPluginDelayAndPeriod() {
    long delay = 0L;
    long period = 20L;
    when(bukkitScheduler.runTaskTimer(eq(plugin), any(Runnable.class), eq(delay),
        eq(period))).thenReturn(bukkitTask);

    BukkitTask result = schedulerService.runTaskTimer(() -> {
    }, delay, period);

    assertAll(() -> assertNotNull(result), () -> assertEquals(bukkitTask, result));
    verify(bukkitScheduler).runTaskTimer(eq(plugin), any(Runnable.class), eq(delay), eq(period));
  }

  @Test
  void runTaskTimerAsynchronouslyRunnableDelegatesToSchedulerWithPluginDelayAndPeriod() {
    long delay = 0L;
    long period = 20L;
    when(bukkitScheduler.runTaskTimerAsynchronously(eq(plugin), any(Runnable.class), eq(delay),
        eq(period))).thenReturn(bukkitTask);

    BukkitTask result = schedulerService.runTaskTimerAsynchronously(() -> {
    }, delay, period);

    assertAll(() -> assertNotNull(result), () -> assertEquals(bukkitTask, result));
    verify(bukkitScheduler).runTaskTimerAsynchronously(eq(plugin), any(Runnable.class), eq(delay),
        eq(period));
  }

  @Test
  void scheduleSyncDelayedTaskWithoutDelayDelegatesToScheduler() {
    when(bukkitScheduler.scheduleSyncDelayedTask(eq(plugin), any(Runnable.class))).thenReturn(42);

    int taskId = schedulerService.scheduleSyncDelayedTask(() -> {
    });

    assertEquals(42, taskId);
    verify(bukkitScheduler).scheduleSyncDelayedTask(eq(plugin), any(Runnable.class));
  }

  @Test
  void scheduleSyncDelayedTaskWithDelayDelegatesToScheduler() {
    long delay = 10L;
    when(bukkitScheduler.scheduleSyncDelayedTask(eq(plugin), any(Runnable.class),
        eq(delay))).thenReturn(43);

    int taskId = schedulerService.scheduleSyncDelayedTask(() -> {
    }, delay);

    assertEquals(43, taskId);
    verify(bukkitScheduler).scheduleSyncDelayedTask(eq(plugin), any(Runnable.class), eq(delay));
  }

  @Test
  void scheduleSyncRepeatingTaskDelegatesToSchedulerWithDelayAndPeriod() {
    long delay = 0L;
    long period = 20L;
    when(bukkitScheduler.scheduleSyncRepeatingTask(eq(plugin), any(Runnable.class), eq(delay),
        eq(period))).thenReturn(44);

    int taskId = schedulerService.scheduleSyncRepeatingTask(() -> {
    }, delay, period);

    assertEquals(44, taskId);
    verify(bukkitScheduler).scheduleSyncRepeatingTask(eq(plugin), any(Runnable.class), eq(delay),
        eq(period));
  }

  @Test
  void scheduleAsyncDelayedTaskWithoutDelayDelegatesToScheduler() {
    when(bukkitScheduler.scheduleAsyncDelayedTask(eq(plugin), any(Runnable.class))).thenReturn(45);

    int taskId = schedulerService.scheduleAsyncDelayedTask(() -> {
    });

    assertEquals(45, taskId);
    verify(bukkitScheduler).scheduleAsyncDelayedTask(eq(plugin), any(Runnable.class));
  }

  @Test
  void scheduleAsyncDelayedTaskWithDelayDelegatesToScheduler() {
    long delay = 10L;
    when(bukkitScheduler.scheduleAsyncDelayedTask(eq(plugin), any(Runnable.class),
        eq(delay))).thenReturn(46);

    int taskId = schedulerService.scheduleAsyncDelayedTask(() -> {
    }, delay);

    assertEquals(46, taskId);
    verify(bukkitScheduler).scheduleAsyncDelayedTask(eq(plugin), any(Runnable.class), eq(delay));
  }

  @Test
  void scheduleAsyncRepeatingTaskDelegatesToSchedulerWithDelayAndPeriod() {
    long delay = 0L;
    long period = 20L;
    when(bukkitScheduler.scheduleAsyncRepeatingTask(eq(plugin), any(Runnable.class), eq(delay),
        eq(period))).thenReturn(47);

    int taskId = schedulerService.scheduleAsyncRepeatingTask(() -> {
    }, delay, period);

    assertEquals(47, taskId);
    verify(bukkitScheduler).scheduleAsyncRepeatingTask(eq(plugin), any(Runnable.class), eq(delay),
        eq(period));
  }

  @Test
  void runTaskTimerConsumerReturnsBukkitTaskAndPassesItToConsumer() {
    long delay = 0L;
    long period = 20L;
    when(bukkitScheduler.runTaskTimer(eq(plugin), any(Runnable.class), eq(delay),
        eq(period))).thenReturn(bukkitTask);

    BukkitTask[] capturedTask = new BukkitTask[1];
    Consumer<BukkitTask> consumer = task -> capturedTask[0] = task;

    BukkitTask result = schedulerService.runTaskTimer(consumer, delay, period);

    assertAll(() -> assertNotNull(result), () -> assertEquals(bukkitTask, result));
  }

  @Test
  void runTaskTimerAsynchronouslyConsumerReturnsBukkitTaskAndPassesItToConsumer() {
    long delay = 0L;
    long period = 20L;
    when(bukkitScheduler.runTaskTimerAsynchronously(eq(plugin), any(Runnable.class), eq(delay),
        eq(period))).thenReturn(bukkitTask);

    BukkitTask[] capturedTask = new BukkitTask[1];
    Consumer<BukkitTask> consumer = task -> capturedTask[0] = task;

    BukkitTask result = schedulerService.runTaskTimerAsynchronously(consumer, delay, period);

    assertAll(() -> assertNotNull(result), () -> assertEquals(bukkitTask, result));
  }

  @Test
  void cancelTaskDelegatesToSchedulerWithTaskId() {
    int taskId = 99;

    schedulerService.cancelTask(taskId);

    verify(bukkitScheduler).cancelTask(taskId);
  }

  @Test
  void cancelTasksDelegatesToSchedulerWithPlugin() {
    schedulerService.cancelTasks();

    verify(bukkitScheduler).cancelTasks(plugin);
  }

  @Test
  void isCurrentlyRunningReturnsTrueWhenTaskIsRunning() {
    int taskId = 10;
    when(bukkitScheduler.isCurrentlyRunning(taskId)).thenReturn(true);

    boolean result = schedulerService.isCurrentlyRunning(taskId);

    assertTrue(result);
    verify(bukkitScheduler).isCurrentlyRunning(taskId);
  }

  @Test
  void isCurrentlyRunningReturnsFalseWhenTaskIsNotRunning() {
    int taskId = 10;
    when(bukkitScheduler.isCurrentlyRunning(taskId)).thenReturn(false);

    boolean result = schedulerService.isCurrentlyRunning(taskId);

    assertFalse(result);
  }

  @Test
  void isQueuedReturnsTrueWhenTaskIsQueued() {
    int taskId = 11;
    when(bukkitScheduler.isQueued(taskId)).thenReturn(true);

    boolean result = schedulerService.isQueued(taskId);

    assertTrue(result);
    verify(bukkitScheduler).isQueued(taskId);
  }

  @Test
  void isQueuedReturnsFalseWhenTaskIsNotQueued() {
    int taskId = 11;
    when(bukkitScheduler.isQueued(taskId)).thenReturn(false);

    boolean result = schedulerService.isQueued(taskId);

    assertFalse(result);
  }

  @Test
  void getActiveWorkersReturnsListFromScheduler() {
    List<BukkitWorker> workers = List.of(bukkitWorker);
    when(bukkitScheduler.getActiveWorkers()).thenReturn(workers);

    List<BukkitWorker> result = schedulerService.getActiveWorkers();

    assertAll(() -> assertNotNull(result), () -> assertEquals(1, result.size()),
        () -> assertEquals(bukkitWorker, result.getFirst()));
    verify(bukkitScheduler).getActiveWorkers();
  }

  @Test
  void getPendingTasksReturnsListFromScheduler() {
    List<BukkitTask> tasks = List.of(bukkitTask);
    when(bukkitScheduler.getPendingTasks()).thenReturn(tasks);

    List<BukkitTask> result = schedulerService.getPendingTasks();

    assertAll(() -> assertNotNull(result), () -> assertEquals(1, result.size()),
        () -> assertEquals(bukkitTask, result.getFirst()));
    verify(bukkitScheduler).getPendingTasks();
  }

  @Test
  void getTaskReturnsMatchingTaskWhenTaskIdExists() {
    int taskId = 55;
    when(bukkitTask.getTaskId()).thenReturn(taskId);
    when(bukkitScheduler.getPendingTasks()).thenReturn(List.of(bukkitTask));

    BukkitTask result = schedulerService.getTask(taskId);

    assertAll(() -> assertNotNull(result), () -> assertEquals(bukkitTask, result));
  }

  @Test
  void getTaskReturnsNullWhenNoMatchingTaskIdExists() {
    when(bukkitTask.getTaskId()).thenReturn(1);
    when(bukkitScheduler.getPendingTasks()).thenReturn(List.of(bukkitTask));

    BukkitTask result = schedulerService.getTask(999);

    assertNull(result);
  }

  @Test
  void getTaskReturnsNullWhenPendingTasksIsEmpty() {
    when(bukkitScheduler.getPendingTasks()).thenReturn(List.of());

    BukkitTask result = schedulerService.getTask(1);

    assertNull(result);
  }

  @Test
  void runTaskPropagatesExceptionFromScheduler() {
    when(bukkitScheduler.runTask(eq(plugin), any(Runnable.class))).thenThrow(
        new IllegalStateException("scheduler error"));

    org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
        () -> schedulerService.runTask(() -> {
        }));
  }

  @Test
  void runTaskAsynchronouslyPropagatesExceptionFromScheduler() {
    when(bukkitScheduler.runTaskAsynchronously(eq(plugin), any(Runnable.class))).thenThrow(
        new IllegalStateException("scheduler error"));

    org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
        () -> schedulerService.runTaskAsynchronously(() -> {
        }));
  }

  @Test
  void runTaskLaterPropagatesExceptionFromScheduler() {
    when(bukkitScheduler.runTaskLater(eq(plugin), any(Runnable.class), anyLong())).thenThrow(
        new IllegalStateException("scheduler error"));

    org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
        () -> schedulerService.runTaskLater(() -> {
        }, 20L));
  }

  @Test
  void runTaskLaterAsynchronouslyPropagatesExceptionFromScheduler() {
    when(bukkitScheduler.runTaskLaterAsynchronously(eq(plugin), any(Runnable.class),
        anyLong())).thenThrow(new IllegalStateException("scheduler error"));

    org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
        () -> schedulerService.runTaskLaterAsynchronously(() -> {
        }, 20L));
  }

  @Test
  void runTaskTimerRunnablePropagatesExceptionFromScheduler() {
    when(bukkitScheduler.runTaskTimer(eq(plugin), any(Runnable.class), anyLong(),
        anyLong())).thenThrow(new IllegalStateException("scheduler error"));

    org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
        () -> schedulerService.runTaskTimer(() -> {
        }, 0L, 20L));
  }

  @Test
  void runTaskTimerAsynchronouslyRunnablePropagatesExceptionFromScheduler() {
    when(bukkitScheduler.runTaskTimerAsynchronously(eq(plugin), any(Runnable.class), anyLong(),
        anyLong())).thenThrow(new IllegalStateException("scheduler error"));

    org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
        () -> schedulerService.runTaskTimerAsynchronously(() -> {
        }, 0L, 20L));
  }

  @Test
  void runTaskTimerConsumerPropagatesExceptionFromScheduler() {
    when(bukkitScheduler.runTaskTimer(eq(plugin), any(Runnable.class), anyLong(),
        anyLong())).thenThrow(new IllegalStateException("scheduler error"));

    org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
        () -> schedulerService.runTaskTimer(_ -> {
        }, 0L, 20L));
  }

  @Test
  void runTaskTimerAsynchronouslyConsumerPropagatesExceptionFromScheduler() {
    when(bukkitScheduler.runTaskTimerAsynchronously(eq(plugin), any(Runnable.class), anyLong(),
        anyLong())).thenThrow(new IllegalStateException("scheduler error"));

    org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
        () -> schedulerService.runTaskTimerAsynchronously(_ -> {
        }, 0L, 20L));
  }

  @Test
  void cancelTaskPropagatesExceptionFromScheduler() {
    org.mockito.Mockito.doThrow(new IllegalStateException("cancel error")).when(bukkitScheduler)
        .cancelTask(99);

    org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
        () -> schedulerService.cancelTask(99));
  }

  @Test
  void cancelTasksPropagatesExceptionFromScheduler() {
    org.mockito.Mockito.doThrow(new IllegalStateException("cancel error")).when(bukkitScheduler)
        .cancelTasks(plugin);

    org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class,
        () -> schedulerService.cancelTasks());
  }

  @Test
  void runTaskTimerConsumerNeverReceivesNullTask() {
    long delay = 0L;
    long period = 20L;
    Runnable[] capturedRunnable = new Runnable[1];

    when(bukkitScheduler.runTaskTimer(eq(plugin), any(Runnable.class), eq(delay), eq(period)))
        .thenAnswer(invocation -> {
          capturedRunnable[0] = invocation.getArgument(1);
          return bukkitTask;
        });

    BukkitTask[] receivedTask = new BukkitTask[1];
    schedulerService.runTaskTimer(task -> receivedTask[0] = task, delay, period);
    capturedRunnable[0].run();

    assertNotNull(receivedTask[0]);
    assertEquals(bukkitTask, receivedTask[0]);
  }

  @Test
  void runTaskTimerAsynchronouslyConsumerNeverReceivesNullTask() {
    long delay = 0L;
    long period = 20L;
    Runnable[] capturedRunnable = new Runnable[1];

    when(bukkitScheduler.runTaskTimerAsynchronously(eq(plugin), any(Runnable.class), eq(delay), eq(period)))
        .thenAnswer(invocation -> {
          capturedRunnable[0] = invocation.getArgument(1);
          return bukkitTask;
        });

    BukkitTask[] receivedTask = new BukkitTask[1];
    schedulerService.runTaskTimerAsynchronously(task -> receivedTask[0] = task, delay, period);
    capturedRunnable[0].run();

    assertNotNull(receivedTask[0]);
    assertEquals(bukkitTask, receivedTask[0]);
  }

  @Test
  void runTaskTimerConsumerDoesNotInvokeConsumerWhenTaskReferenceIsNotYetSet() {
    long delay = 0L;
    long period = 20L;
    Runnable[] capturedRunnable = new Runnable[1];

    when(bukkitScheduler.runTaskTimer(eq(plugin), any(Runnable.class), eq(delay), eq(period)))
        .thenAnswer(invocation -> {
          capturedRunnable[0] = invocation.getArgument(1);
          capturedRunnable[0].run();
          return bukkitTask;
        });

    BukkitTask[] receivedTask = new BukkitTask[1];
    schedulerService.runTaskTimer(task -> receivedTask[0] = task, delay, period);

    assertNull(receivedTask[0]);
  }

  @Test
  void runTaskTimerAsynchronouslyConsumerDoesNotInvokeConsumerWhenTaskReferenceIsNotYetSet() {
    long delay = 0L;
    long period = 20L;
    Runnable[] capturedRunnable = new Runnable[1];

    when(bukkitScheduler.runTaskTimerAsynchronously(eq(plugin), any(Runnable.class), eq(delay), eq(period)))
        .thenAnswer(invocation -> {
          capturedRunnable[0] = invocation.getArgument(1);
          capturedRunnable[0].run();
          return bukkitTask;
        });

    BukkitTask[] receivedTask = new BukkitTask[1];
    schedulerService.runTaskTimerAsynchronously(task -> receivedTask[0] = task, delay, period);

    assertNull(receivedTask[0]);
  }
}