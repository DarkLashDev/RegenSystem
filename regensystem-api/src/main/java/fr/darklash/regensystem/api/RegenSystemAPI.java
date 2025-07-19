package fr.darklash.regensystem.api;

public interface RegenSystemAPI {

    /**
     * Renvoie le temps restant de régénération d'une zone donnée.
     */
    long getRemainingTime(String zoneId);

    /**
     * Force la régénération d'une zone.
     */
    void forceRegenerate(String zoneId);

    /**
     * Enregistre une nouvelle zone de régénération.
     */
    void registerZone(String zoneId);
}
