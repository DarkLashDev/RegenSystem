package fr.darklash.regensystem.platform.scheduler;

import fr.darklash.regensystem.RegenSystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.concurrent.TimeUnit;

public class FoliaRegenScheduler implements RegenScheduler {

    private final RegenSystem plugin;
    private static final long TICK_DURATION_MS = 50L;

    public FoliaRegenScheduler(RegenSystem plugin) {
        this.plugin = plugin;
    }

    /* ---------- ASYNC ---------- */

    @Override
    public void runAsync(Runnable task) {
        Bukkit.getAsyncScheduler().runNow(plugin, t -> task.run());
    }

    @Override
    public RegenTask runRepeatingAsync(long delayTicks, long periodTicks, Runnable task) {
        var handle = Bukkit.getAsyncScheduler().runAtFixedRate(
                plugin,
                t -> task.run(),
                Math.max(1, delayTicks) * TICK_DURATION_MS,
                Math.max(1, periodTicks) * TICK_DURATION_MS,
                TimeUnit.MILLISECONDS
        );
        return handle::cancel;
    }

    /* ---------- GLOBAL ---------- */

    @Override
    public void runSync(Runnable task) {
        Bukkit.getGlobalRegionScheduler().run(plugin, t -> task.run());
    }

    @Override
    public void runLater(long delayTicks, Runnable task) {
        if (delayTicks <= 0) {
            task.run();
            return;
        }

        Bukkit.getGlobalRegionScheduler().runDelayed(
                plugin,
                t -> task.run(),
                delayTicks
        );
    }

    @Override
    public RegenTask runRepeating(long delayTicks, long periodTicks, Runnable task) {
        var handle = Bukkit.getGlobalRegionScheduler().runAtFixedRate(
                plugin,
                t -> task.run(),
                Math.max(1, delayTicks),
                Math.max(1, periodTicks)
        );
        return handle::cancel;
    }

    /* ---------- REGION ---------- */

    @Override
    public void runLater(Location location, long delayTicks, Runnable task) {
        if (delayTicks <= 0) {
            task.run();
            return;
        }

        Bukkit.getRegionScheduler().runDelayed(
                plugin,
                location,
                t -> task.run(),
                delayTicks
        );
    }

    @Override
    public RegenTask runRepeating(Location location, long delayTicks, long periodTicks, Runnable task) {
        var handle = Bukkit.getRegionScheduler().runAtFixedRate(
                plugin,
                location,
                t -> task.run(),
                Math.max(1, delayTicks),
                Math.max(1, periodTicks)
        );
        return handle::cancel;
    }
}
