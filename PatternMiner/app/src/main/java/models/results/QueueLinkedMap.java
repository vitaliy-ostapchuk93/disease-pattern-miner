package models.results;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

public class QueueLinkedMap<K, V> extends LinkedHashMap<K, V> {

    private int maxSize;

    public QueueLinkedMap(int maxSize) {
        super();
        this.maxSize = maxSize;
    }

    private static <K, V> Map.Entry<K, V> getLast(Map<K, V> map) {
        try {
            if (map instanceof LinkedHashMap) return getLastViaReflection(map);
        } catch (Exception ignore) {
        }
        return getLastByIterating(map);
    }

    private static <K, V> Map.Entry<K, V> getLastByIterating(Map<K, V> map) {
        Map.Entry<K, V> last = null;
        for (Map.Entry<K, V> e : map.entrySet()) last = e;
        return last;
    }

    private static <K, V> Map.Entry<K, V> getLastViaReflection(Map<K, V> map) throws NoSuchFieldException, IllegalAccessException {
        Field tail = map.getClass().getDeclaredField("tail");
        tail.setAccessible(true);
        return (Map.Entry<K, V>) tail.get(map);
    }

    public Map.Entry<K, V> getFirst() {
        return getFirst(this);
    }

    public Map.Entry<K, V> getLast() {
        return getLast(this);
    }

    public Map.Entry<K, V> removeHead() {
        Map.Entry head = getFirst();
        this.remove(head);
        return head;
    }

    private <K, V> Map.Entry<K, V> getFirst(Map<K, V> map) {
        if (map.isEmpty()) return null;
        return map.entrySet().iterator().next();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
