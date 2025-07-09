package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CommandCountManager {
    private static final Map<UUID, AtomicInteger> COUNTS = new ConcurrentHashMap<>();

    public static void increment(UUID uuid) {
        COUNTS.computeIfAbsent(uuid, k -> new AtomicInteger(0))
                .incrementAndGet();
    }

    public static int getCount(UUID uuid) {
        return COUNTS.getOrDefault(uuid, new AtomicInteger(0)).get();
    }
    public static void reset(UUID uuid) {
        COUNTS.remove(uuid);
    }
}