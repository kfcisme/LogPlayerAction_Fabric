package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 管理玩家撿取物品次數。
 * ItemEntityMixin 會在玩家撿取物品時呼叫 increment(uuid)。
 */
public class OnPickupItemListener {
    // 用 ConcurrentHashMap 保證執行緒安全
    private static final Map<UUID, Integer> pickupCounts = new ConcurrentHashMap<>();

    /**
     * 每次玩家撿起物品時由 mixin 調用
     * @param uuid 玩家 UUID
     */
    public static void increment(UUID uuid) {
        pickupCounts.merge(uuid, 1, Integer::sum);
        System.out.println(Text.literal("§a[pick item] "));
    }

    /**
     * 取得指定玩家的累計撿取次數
     * @param uuid 玩家 UUID
     * @return 撿取次數
     */
    public static int getCount(UUID uuid) {
        return pickupCounts.getOrDefault(uuid, 0);
    }

    /**
     * 重置指定玩家的撿取計數
     * @param uuid 玩家 UUID
     */
    public static void reset(UUID uuid) {
        pickupCounts.remove(uuid);
    }

    /**
     * （選用）清除所有玩家的撿取計數
     */
    public static void resetAll() {
        pickupCounts.clear();
    }
}
