package fr.darklash.regensystem.api;

import fr.darklash.regensystem.api.zone.RegenZoneManager;

public class RegenSystemProvider {

    private static RegenZoneManager api;

    public static RegenZoneManager get() {
        if (api == null) {
            throw new IllegalStateException("RegenSystem API not loaded.");
        }
        return api;
    }

    public static void set(RegenZoneManager implementation) {
        if (api != null) return;
        api = implementation;
    }

    private RegenSystemProvider() {}
}
