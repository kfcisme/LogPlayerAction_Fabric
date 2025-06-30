package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

//import net.fabricmc.fabric.api.entity.event.v1.LivingEntityEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OnEntityDeathListener {
    // key: 玩家 UUID, value: 殺敵總次數
    private static final Map<UUID, Integer> KILL_COUNTS = new ConcurrentHashMap<>();

    /** 玩家擊殺一隻生物後呼叫 */
    public static void increment(ServerPlayerEntity player, LivingEntity target) {
        UUID uuid = player.getUuid();
        KILL_COUNTS.merge(uuid, 1, Integer::sum);

        // 選項：發送訊息給玩家，顯示總擊殺次數
        int total = KILL_COUNTS.get(uuid);
//        player.sendMessage(Text.literal(
//                String.format("§c[擊殺追蹤] §f你已擊殺生物 %d 次（最後一隻：%s）",
//                        total, target.getEntityName())
//        ), false);
    }

    public static int getCount(UUID uuid) {
        return KILL_COUNTS.getOrDefault(uuid, 0);
    }

    public static void reset(UUID uuid) {
        KILL_COUNTS.remove(uuid);
    }
}
