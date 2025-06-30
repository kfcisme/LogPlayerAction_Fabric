package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/** 管理玩家傳送次數 */
public class PlayerTeleportTracker {
    private static final Map<UUID, Integer> teleportCounts = new ConcurrentHashMap<>();

    public static void increment(UUID uuid) {
        teleportCounts.merge(uuid, 1, Integer::sum);
    }

    public static int getCount(UUID uuid) {
        return teleportCounts.getOrDefault(uuid, 0);
    }

    public static void reset(UUID uuid) {
        teleportCounts.remove(uuid);
    }
}
