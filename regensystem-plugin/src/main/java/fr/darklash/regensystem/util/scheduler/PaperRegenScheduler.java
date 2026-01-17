package fr.darklash.regensystem.util.scheduler;

import fr.darklash.regensystem.RegenSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class PaperRegenScheduler implements RegenScheduler {

    private final RegenSystem plugin;

    public PaperRegenScheduler(RegenSystem plugin) {
        this.plugin = plugin;
    }

    /* ---------- ASYNC ---------- */

    @Override
    public void runAsync(Runnable task) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
    }

    @Override
    public RegenTask runRepeatingAsync(long delayTicks, long periodTicks, Runnable task) {
        var bukkitTask = Bukkit.getScheduler()
                .runTaskTimerAsynchronously(plugin, task, delayTicks, periodTicks);
        return bukkitTask::cancel;
    }

    /* ---------- SYNC ---------- */

    @Override
    public void runSync(Runnable task) {
        Bukkit.getScheduler().runTask(plugin, task);
    }

    @Override
    public void runLater(long delayTicks, Runnable task) {
        Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks);
    }

    @Override
    public RegenTask runRepeating(long delayTicks, long periodTicks, Runnable task) {
        var bukkitTask = Bukkit.getScheduler()
                .runTaskTimer(plugin, task, delayTicks, periodTicks);
        return bukkitTask::cancel;
    }

    /* ---------- REGION ---------- */

    @Override
    public void runLater(Location location, long delayTicks, Runnable task) {
        runLater(delayTicks, task); // Paper n’a pas de régions
    }

    @Override
    public RegenTask runRepeating(Location location, long delayTicks, long periodTicks, Runnable task) {
        return runRepeating(delayTicks, periodTicks, task);
    }
}
