package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用 ScreenHandlerSlotClickCallback 監聽玩家從工作檯結果槽拉取合成物品的動作。
 *   - register(): 註冊事件。
 *   - getCount(UUID): 查詢當前合成次數。
 *   - reset(UUID): 重置合成計數。
 */
public class CraftingListener {
    // 使用 ConcurrentHashMap 保證線程安全
    private static final Map<UUID, AtomicInteger> counts = new ConcurrentHashMap<>();

    /**
     * 合成次數累加
     *
     * @param playerId 玩家 UUID
     */
    public static void increment(UUID playerId) {
        counts.computeIfAbsent(playerId, id -> new AtomicInteger(0)).incrementAndGet();
    }

    /**
     * 獲取玩家已合成的次數
     *
     * @param playerId 玩家 UUID
     * @return 合成次數，若不存在則回傳 0
     */
    public static int getCount(UUID playerId) {
        AtomicInteger count = counts.get(playerId);
        return count != null ? count.get() : 0;
    }

    /**
     * 重置玩家合成次數（移除紀錄）
     *
     * @param playerId 玩家 UUID
     */
    public static void reset(UUID playerId) {
        counts.remove(playerId);
    }

    /**
     * 若需重置所有玩家記錄，可呼叫此方法
     */
    public static void resetAll() {
        counts.clear();
    }
}
