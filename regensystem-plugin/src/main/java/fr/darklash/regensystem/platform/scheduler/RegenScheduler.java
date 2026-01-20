package fr.darklash.regensystem.platform.scheduler;

import org.bukkit.Location;

public interface RegenScheduler {

    /* ---------- ASYNC ---------- */
    void runAsync(Runnable task);

    RegenTask runRepeatingAsync(long delayTicks, long periodTicks, Runnable task);

    /* ---------- SYNC / GLOBAL ---------- */
    void runSync(Runnable task);

    void runLater(long delayTicks, Runnable task);

    RegenTask runRepeating(long delayTicks, long periodTicks, Runnable task);

    /* ---------- REGION (Folia) ---------- */
    void runLater(Location location, long delayTicks, Runnable task);

    RegenTask runRepeating(Location location, long delayTicks, long periodTicks, Runnable task);
}
