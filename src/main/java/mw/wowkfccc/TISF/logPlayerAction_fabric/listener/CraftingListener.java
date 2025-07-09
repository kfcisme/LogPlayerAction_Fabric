package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class CraftingListener {
    // 使用 ConcurrentHashMap 保證線程安全
    private static final Map<UUID, AtomicInteger> counts = new ConcurrentHashMap<>();

    public static void increment(UUID playerId) {
        counts.computeIfAbsent(playerId, id -> new AtomicInteger(0)).incrementAndGet();
    }

    public static int getCount(UUID playerId) {
        AtomicInteger count = counts.get(playerId);
        return count != null ? count.get() : 0;
    }


    public static void reset(UUID playerId) {
        counts.remove(playerId);
    }


    public static void resetAll() {
        counts.clear();
    }
}
