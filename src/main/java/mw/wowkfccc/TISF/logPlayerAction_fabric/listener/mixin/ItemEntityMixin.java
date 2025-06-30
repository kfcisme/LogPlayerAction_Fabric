package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.OnPickupItemListener;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 在玩家與地上的 ItemEntity 碰撞（撿起）時注入
 * :contentReference[oaicite:0]{index=0}
 */
@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(
            method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V",
            at     = @At("HEAD")
    )
    private void onPlayerPickup(PlayerEntity player, CallbackInfo ci) {
        if (player instanceof ServerPlayerEntity serverPlayer) {
            OnPickupItemListener.increment(serverPlayer.getUuid());
            // （可選）回饋玩家
            serverPlayer.sendMessage(
                    net.minecraft.text.Text.literal(
                            "§6[撿取追蹤] §f你已撿取 " +
                                    OnPickupItemListener.getCount(serverPlayer.getUuid()) +
                                    " 件物品"
                    ),
                    false
            );
        }
    }
}
