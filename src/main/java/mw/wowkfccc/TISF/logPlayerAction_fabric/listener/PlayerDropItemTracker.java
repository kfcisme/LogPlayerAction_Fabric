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


import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDropItemTracker {
    // 1. 單例 instance
    private static final PlayerDropItemTracker INSTANCE = new PlayerDropItemTracker();

    // 2. 私有化 constructor
    private PlayerDropItemTracker() {}

    // 3. 提供靜態存取點
    public static PlayerDropItemTracker getInstance() {
        return INSTANCE;
    }

    // 你的原本欄位與方法
    private static final Map<UUID, Integer> counts = new ConcurrentHashMap<>();

    public void increment(UUID uuid) {
        counts.merge(uuid, 1, Integer::sum);
    }

    public static int getCount(UUID uuid) {
        return counts.getOrDefault(uuid, 0);
    }

    public static void reset(UUID uuid) {
        counts.remove(uuid);
    }
}
