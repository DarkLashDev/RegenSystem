package fr.darklash.regensystem.api;

import java.util.HashMap;
import java.util.Map;

public class RegenSystemImpl implements RegenSystemAPI {

    private final Map<String, Long> zoneTimers = new HashMap<>();

    @Override
    public long getRemainingTime(String zoneId) {
        return zoneTimers.getOrDefault(zoneId, 0L);
    }

    @Override
    public void forceRegenerate(String zoneId) {
        // logique ici
    }

    @Override
    public void registerZone(String zoneId) {
        zoneTimers.put(zoneId, System.currentTimeMillis() + 60000L); // 1 minute
    }
}
