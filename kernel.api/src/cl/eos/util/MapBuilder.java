package cl.eos.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class MapBuilder<K, V> {
    private final Map<K, V> map;

    private MapBuilder(Map<K, V> map) {
        this.map = map;
    }

    public MapBuilder<K, V> put(K key, V value) {
        if(map == null)
            throw new IllegalStateException();
        map.put(key, value);
        return this;
    }

    public MapBuilder<K, V> put(Map<? extends K, ? extends V> other) {
        if(map == null)
            throw new IllegalStateException();
        map.putAll(other);
        return this;
    }

    public Map<K, V> build() {
        Map<K, V> m = map;
        return Collections.unmodifiableMap(m);
    }

    public static <K, V> MapBuilder<K, V> unordered() {
        return new MapBuilder<>(new HashMap<>());
    }

    public static <K, V> MapBuilder<K, V> ordered() {
        return new MapBuilder<>(new LinkedHashMap<>());
    }

    public static <K extends Comparable<K>, V> MapBuilder<K, V> sorted() {
        return new MapBuilder<>(new TreeMap<>());
    }

    public static <K, V> MapBuilder<K, V> sorted(Comparator<K> comparator) {
        return new MapBuilder<>(new TreeMap<>(comparator));
    }
}