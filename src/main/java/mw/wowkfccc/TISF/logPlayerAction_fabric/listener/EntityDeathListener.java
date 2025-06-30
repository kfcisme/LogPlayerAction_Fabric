package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * 註冊生物死亡後的 callback，當且僅當是玩家擊殺時，呼叫 DeathCountTracker.increment()
 */
public class EntityDeathListener {
    public static void register() {
        ServerLivingEntityEvents.AFTER_DEATH.register(
                (LivingEntity entity, DamageSource source) -> {
                    // 只有在死亡來源是玩家的情況下才統計
                    if (source.getAttacker() instanceof ServerPlayerEntity player) {
                        OnEntityDeathListener.increment(player, entity);
                    }
                }
        );
    }
}