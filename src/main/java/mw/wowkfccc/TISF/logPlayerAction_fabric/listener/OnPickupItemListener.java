package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理玩家撿取物品次數
 */
public class OnPickupItemListener {
    private static final Map<UUID, Integer> pickupCounts = new ConcurrentHashMap<>();

    /** 每次玩家撿起物品時呼叫 */
    public static void increment(UUID uuid) {
        pickupCounts.merge(uuid, 1, Integer::sum);
    }

    /** 取得玩家累計撿取次數 */
    public static int getCount(UUID uuid) {
        return pickupCounts.getOrDefault(uuid, 0);
    }

    /** 重置玩家計數（如需要） */
    public static void reset(UUID uuid) {
        pickupCounts.remove(uuid);
    }
}
