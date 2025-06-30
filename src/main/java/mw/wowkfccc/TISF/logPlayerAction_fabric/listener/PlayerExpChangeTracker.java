package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** 管理玩家撿取經驗球次數 */
public class PlayerExpChangeTracker {
    private static final Map<UUID, Integer> expChangeCounts = new ConcurrentHashMap<>();

    public static void increment(UUID uuid) {
        expChangeCounts.merge(uuid, 1, Integer::sum);
    }

    public static int getCount(UUID uuid) {
        return expChangeCounts.getOrDefault(uuid, 0);
    }

    public static void reset(UUID uuid) {
        expChangeCounts.remove(uuid);
    }
}
