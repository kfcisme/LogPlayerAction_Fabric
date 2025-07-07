package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** 放置方塊次數 */
public class BlockPlaceTracker {
    private static final Map<UUID, Integer> counts = new ConcurrentHashMap<>();
    public static int increment(UUID uuid) {
        int c = counts.merge(uuid, 1, Integer::sum);
        return c;
    }
    public static int get(UUID uuid) { return counts.getOrDefault(uuid, 0); }
    public static void reset(UUID uuid) { counts.remove(uuid); }
}