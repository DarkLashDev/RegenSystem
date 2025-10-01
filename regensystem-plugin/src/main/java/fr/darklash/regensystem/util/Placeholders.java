package fr.darklash.regensystem.util;

import java.util.HashMap;
import java.util.Map;

public class Placeholders {

    private final Map<String, String> values = new HashMap<>();

    private Placeholders(String key, Object value) {
        values.put(key, String.valueOf(value));
    }

    public static Placeholders of(String key, Object value) {
        return new Placeholders(key, value);
    }

    public Placeholders add(String key, Object value) {
        values.put(key, String.valueOf(value));
        return this;
    }

    public Map<String, String> asMap() {
        return values;
    }
}
