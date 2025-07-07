package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;


import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.OnPickupItemListener;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    /**
     * 在伺服器端玩家撿到這個 ItemEntity 時觸發一次
     * method 名稱對應 Yarn mappings 1.20.4：onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V
     */
    @Inject(
            method = "onPlayerCollision",
            at     = @At("HEAD")
    )
    private void onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        // 只在伺服器端計數
        World world = ((ItemEntity)(Object)this).getWorld();
        if (!world.isClient) {
            OnPickupItemListener.increment(player.getUuid());
        }
    }
}
