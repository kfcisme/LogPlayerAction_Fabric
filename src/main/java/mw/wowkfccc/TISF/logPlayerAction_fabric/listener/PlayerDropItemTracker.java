package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

//import net.fabricmc.fabric.api.event.player.PlayerDropItemCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDropItemTracker {
    private static final Map<UUID, Integer> dropCounts = new ConcurrentHashMap<>();

    /** 每次玩家丟物品時呼叫 */
    public static void increment(UUID uuid) {
        dropCounts.merge(uuid, 1, Integer::sum);
    }

    /** 取得玩家丟物品總次數 */
    public static int getCount(UUID uuid) {
        return dropCounts.getOrDefault(uuid, 0);
    }

    /** （選用）重置計數 */
    public static void reset(UUID uuid) {
        dropCounts.remove(uuid);
    }
}