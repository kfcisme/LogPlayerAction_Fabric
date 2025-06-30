package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.PlayerExpChangeTracker;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 攔截經驗球(ExperienceOrbEntity)與玩家碰撞(onPlayerCollision)，
 * 每次撿取時呼叫 increment()
 */
@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {
    @Inject(
            method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V",
            at     = @At("HEAD")
    )
    private void onPlayerPickupXP(PlayerEntity player, CallbackInfo ci) {
        if (player instanceof ServerPlayerEntity server) {
            PlayerExpChangeTracker.increment(server.getUuid());
            // （可選）即時通知玩家
            int total = PlayerExpChangeTracker.getCount(server.getUuid());
            server.sendMessage(
                    net.minecraft.text.Text.literal(
                            "§6[經驗球追蹤] §f你已撿取經驗球 " + total + " 次"
                    ),
                    false
            );
        }
    }
}
