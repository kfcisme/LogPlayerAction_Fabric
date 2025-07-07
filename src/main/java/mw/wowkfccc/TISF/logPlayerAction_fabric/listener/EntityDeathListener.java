package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 監聽實體死亡，當死亡來源為玩家(ServerPlayerEntity)時，統計擊殺次數。
 */
public class EntityDeathListener {
    // key = 玩家UUID，value = 擊殺次數
    private static final Map<UUID, Integer> killCounts = new ConcurrentHashMap<>();

    /**
     * 在伺服器啟動後呼叫一次，註冊死亡事件監聽。
     * e.g. 在 SERVER_STARTED callback 裡執行：
     *     OnEntityDeathListener.register();
     */
    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register((LivingEntity entity, DamageSource source) -> {
            if (source.getAttacker() instanceof ServerPlayerEntity player) {
                UUID uuid = player.getUuid();
                increment(uuid);
            }
        });
    }

    /** 每次玩家擊殺實體時將次數 +1 */
    public static void increment(UUID uuid) {
        killCounts.merge(uuid, 1, Integer::sum);
    }

    /** 取得指定玩家的累計擊殺次數 */
    public static int getCount(UUID uuid) {
        return killCounts.getOrDefault(uuid, 0);
    }

    /** 重置指定玩家的擊殺次數 */
    public static void reset(UUID uuid) {
        killCounts.remove(uuid);
    }

    /** （選用）清除所有玩家的擊殺次數 */
    public static void resetAll() {
        killCounts.clear();
    }
}
