package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 監聽並記錄玩家拾取物品次數
 */
public class OnPickupItemListener {
    private static final OnPickupItemListener INSTANCE = new OnPickupItemListener();
    private static final Map<UUID, Integer> counts = new ConcurrentHashMap<>();

    public static OnPickupItemListener getInstance() {
        return INSTANCE;
    }

    public void increment(UUID player) {
        counts.merge(player, 1, Integer::sum);
    }

    public static int getCount(UUID player) {
        return counts.getOrDefault(player, 0);
    }

    public static void reset(UUID player) {
        counts.remove(player);
    }
}