package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class EntityDeathListener {
    // key = 玩家UUID，value = 擊殺次數
    private static final Map<UUID, Integer> killCounts = new ConcurrentHashMap<>();
    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register((LivingEntity entity, DamageSource source) -> {
            if (source.getAttacker() instanceof ServerPlayerEntity player) {
                UUID uuid = player.getUuid();
                increment(uuid);
            }
        });
    }
    public static void increment(UUID uuid) {
        killCounts.merge(uuid, 1, Integer::sum);
//        System.out.println("Target Event EntityDeath");
    }

    public static int getCount(UUID uuid) {
        return killCounts.getOrDefault(uuid, 0);
    }

    public static void reset(UUID uuid) {
        killCounts.remove(uuid);
    }

    public static void resetAll() {
        killCounts.clear();
    }
}
