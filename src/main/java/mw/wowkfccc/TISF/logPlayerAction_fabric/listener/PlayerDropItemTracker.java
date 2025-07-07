package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

////import net.fabricmc.fabric.api.event.player.PlayerDropItemCallback;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.text.Text;
//import net.minecraft.util.ActionResult;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//import java.util.concurrent.ConcurrentHashMap;
//
//public class PlayerDropItemTracker {
//    private static final Map<UUID, Integer> dropCounts = new ConcurrentHashMap<>();
//
//    /** 每次玩家丟物品時呼叫 */
//    public static void increment(UUID uuid) {
//        dropCounts.merge(uuid, 1, Integer::sum);
//        System.out.println(Text.literal("§a[pick item] "));
//    }
//
//    /** 取得玩家丟物品總次數 */
//    public static int getCount(UUID uuid) {
//        return dropCounts.getOrDefault(uuid, 0);
//    }
//
//    /** （選用）重置計數 */
//    public static void reset(UUID uuid) {
//        dropCounts.remove(uuid);
//    }
//}


import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.fabric.api.entity.EntityPickInteractionAware;

/**
 * 管理玩家丟棄物品次數，並即時通知玩家
 */
public class PlayerDropItemTracker {
    private static final Map<UUID, Integer> counts = new ConcurrentHashMap<>();

    /** 每次丟棄時由 Mixin 調用 */
    public static void onPlayerDrop(ServerPlayerEntity player) {
        UUID uuid = player.getUuid();
        int c = counts.merge(uuid, 1, Integer::sum);
        player.sendMessage(Text.literal("§6[丟棄物品] 你已丟棄第 " + c + " 次"), false);
    }

    public static int getCount(UUID uuid) {
        return counts.getOrDefault(uuid, 0);
    }

    public static void reset(UUID uuid) {
        counts.remove(uuid);
    }
}
