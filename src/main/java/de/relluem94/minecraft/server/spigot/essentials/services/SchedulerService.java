package de.relluem94.minecraft.server.spigot.essentials.services;

import java.util.List;
import java.util.function.Consumer;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scheduler.BukkitWorker;

/**
 * Service wrapper around {@link BukkitScheduler} that automatically binds all task
 * scheduling operations to the owning {@link Plugin} instance, reducing boilerplate
 * and centralizing scheduler access.
 */
public class SchedulerService {

  private final Plugin plugin;

  /**
   * Creates a new {@code SchedulerService} bound to the given plugin.
   *
   * @param plugin the plugin instance used as the owner for all scheduled tasks
   */
  public SchedulerService(Plugin plugin) {
    this.plugin = plugin;
  }

  private BukkitScheduler scheduler() {
    return plugin.getServer().getScheduler();
  }

  /**
   * Schedules a task to run on the next server tick on the main thread.
   *
   * @param task the runnable to execute
   * @return the {@link BukkitTask} representing the scheduled task
   */
  public BukkitTask runTask(Runnable task) {
    return scheduler().runTask(plugin, task);
  }

  /**
   * Schedules a task to run asynchronously on a separate thread as soon as possible.
   *
   * @param task the runnable to execute asynchronously
   * @return the {@link BukkitTask} representing the scheduled task
   */
  public BukkitTask runTaskAsynchronously(Runnable task) {
    return scheduler().runTaskAsynchronously(plugin, task);
  }

  /**
   * Schedules a task to run on the main thread after the specified delay.
   *
   * @param task  the runnable to execute
   * @param delay the delay in ticks before the task is executed
   * @return the {@link BukkitTask} representing the scheduled task
   */
  public BukkitTask runTaskLater(Runnable task, long delay) {
    return scheduler().runTaskLater(plugin, task, delay);
  }

  /**
   * Schedules a task to run asynchronously after the specified delay.
   *
   * @param task  the runnable to execute asynchronously
   * @param delay the delay in ticks before the task is executed
   * @return the {@link BukkitTask} representing the scheduled task
   */
  public BukkitTask runTaskLaterAsynchronously(Runnable task, long delay) {
    return scheduler().runTaskLaterAsynchronously(plugin, task, delay);
  }

  /**
   * Schedules a repeating task to run on the main thread at a fixed interval.
   *
   * @param task   the runnable to execute
   * @param delay  the delay in ticks before the first execution
   * @param period the interval in ticks between subsequent executions
   * @return the {@link BukkitTask} representing the scheduled task
   */
  public BukkitTask runTaskTimer(Runnable task, long delay, long period) {
    return scheduler().runTaskTimer(plugin, task, delay, period);
  }

  /**
   * Schedules a repeating task to run asynchronously at a fixed interval.
   *
   * @param task   the runnable to execute asynchronously
   * @param delay  the delay in ticks before the first execution
   * @param period the interval in ticks between subsequent executions
   * @return the {@link BukkitTask} representing the scheduled task
   */
  public BukkitTask runTaskTimerAsynchronously(Runnable task, long delay, long period) {
    return scheduler().runTaskTimerAsynchronously(plugin, task, delay, period);
  }

  /**
   * Schedules a synchronous delayed task using the legacy task ID API.
   * The task is executed on the main thread on the next available tick.
   *
   * @param task the runnable to execute
   * @return the task ID assigned to the scheduled task
   */
  public int scheduleSyncDelayedTask(Runnable task) {
    return scheduler().scheduleSyncDelayedTask(plugin, task);
  }

  /**
   * Schedules a synchronous delayed task using the legacy task ID API.
   * The task is executed on the main thread after the specified delay.
   *
   * @param task  the runnable to execute
   * @param delay the delay in ticks before the task is executed
   * @return the task ID assigned to the scheduled task
   */
  public int scheduleSyncDelayedTask(Runnable task, long delay) {
    return scheduler().scheduleSyncDelayedTask(plugin, task, delay);
  }

  /**
   * Schedules a repeating synchronous task using the legacy task ID API.
   * The task is executed on the main thread at a fixed interval.
   *
   * @param task   the runnable to execute
   * @param delay  the delay in ticks before the first execution
   * @param period the interval in ticks between subsequent executions
   * @return the task ID assigned to the scheduled task
   */
  public int scheduleSyncRepeatingTask(Runnable task, long delay, long period) {
    return scheduler().scheduleSyncRepeatingTask(plugin, task, delay, period);
  }

  /**
   * Schedules an asynchronous delayed task using the legacy task ID API.
   * The task is executed on a separate thread on the next available opportunity.
   *
   * @param task the runnable to execute asynchronously
   * @return the task ID assigned to the scheduled task
   */
  public int scheduleAsyncDelayedTask(Runnable task) {
    return scheduler().scheduleAsyncDelayedTask(plugin, task);
  }

  /**
   * Schedules an asynchronous delayed task using the legacy task ID API.
   * The task is executed on a separate thread after the specified delay.
   *
   * @param task  the runnable to execute asynchronously
   * @param delay the delay in ticks before the task is executed
   * @return the task ID assigned to the scheduled task
   */
  public int scheduleAsyncDelayedTask(Runnable task, long delay) {
    return scheduler().scheduleAsyncDelayedTask(plugin, task, delay);
  }

  /**
   * Schedules a repeating asynchronous task using the legacy task ID API.
   * The task is executed on a separate thread at a fixed interval.
   *
   * @param task   the runnable to execute asynchronously
   * @param delay  the delay in ticks before the first execution
   * @param period the interval in ticks between subsequent executions
   * @return the task ID assigned to the scheduled task
   */
  public int scheduleAsyncRepeatingTask(Runnable task, long delay, long period) {
    return scheduler().scheduleAsyncRepeatingTask(plugin, task, delay, period);
  }

  public BukkitTask runTaskTimer(Consumer<BukkitTask> task, long delay, long period) {
    return scheduler().runTaskTimer(plugin, (Runnable) task, delay, period);
  }


  public BukkitTask runTaskTimerAsynchronously(Consumer<BukkitTask> task, long delay, long period) {
    return scheduler().runTaskTimerAsynchronously(plugin, (Runnable) task, delay, period);
  }

  /**
   * Cancels the scheduled task with the given task ID.
   *
   * @param taskId the ID of the task to cancel
   */
  public void cancelTask(int taskId) {
    scheduler().cancelTask(taskId);
  }

  /**
   * Cancels all tasks that are currently scheduled or running for the owning plugin.
   */
  public void cancelTasks() {
    scheduler().cancelTasks(plugin);
  }

  /**
   * Returns whether the task with the given ID is currently being executed by a worker thread.
   *
   * @param taskId the ID of the task to check
   * @return {@code true} if the task is currently running, {@code false} otherwise
   */
  public boolean isCurrentlyRunning(int taskId) {
    return scheduler().isCurrentlyRunning(taskId);
  }

  /**
   * Returns whether the task with the given ID is queued and waiting to be executed.
   *
   * @param taskId the ID of the task to check
   * @return {@code true} if the task is queued, {@code false} otherwise
   */
  public boolean isQueued(int taskId) {
    return scheduler().isQueued(taskId);
  }

  /**
   * Returns a list of all asynchronous tasks that are currently being executed by worker threads.
   *
   * @return a list of active {@link BukkitWorker} instances
   */
  public List<BukkitWorker> getActiveWorkers() {
    return scheduler().getActiveWorkers();
  }

  /**
   * Returns a list of all tasks that are currently pending execution across all plugins.
   *
   * @return a list of pending {@link BukkitTask} instances
   */
  public List<BukkitTask> getPendingTasks() {
    return scheduler().getPendingTasks();
  }

  /**
   * Retrieves a pending {@link BukkitTask} by its task ID.
   * Returns {@code null} if no pending task with the given ID exists.
   *
   * @param taskId the ID of the task to look up
   * @return the matching {@link BukkitTask}, or {@code null} if not found
   */
  public BukkitTask getTask(int taskId) {
    return scheduler().getPendingTasks()
        .stream()
        .filter(task -> task.getTaskId() == taskId)
        .findFirst()
        .orElse(null);
  }
}