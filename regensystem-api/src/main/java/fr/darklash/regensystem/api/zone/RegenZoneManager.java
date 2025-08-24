package fr.darklash.regensystem.api.zone;

import java.util.Collection;
import java.util.Set;

public interface RegenZoneManager {

    RegenZone getZone(String name);

    Collection<RegenZone> getZones();

    Set<String> getZoneNames();

    void deleteZone(String name);

    void addZone(RegenZone zone);

    void reloadZone(String name);

    void loadZones();

    int getTimeLeft(String zoneName);

    boolean isZoneRegistered(String name);
}
