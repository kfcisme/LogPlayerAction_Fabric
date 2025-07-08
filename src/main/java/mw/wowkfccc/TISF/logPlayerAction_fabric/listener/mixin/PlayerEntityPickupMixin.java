package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;
import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.OnPickupItemListener;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(ItemEntity.class)
public abstract class PlayerEntityPickupMixin {
    // 存取 private pickupDelay 欄位，避免把剛丟出的又當成撿起
    @Shadow private int pickupDelay;

    @Inject(
            method = "onPlayerCollision",    // 只用名稱，不帶 descriptor
            at = @At("HEAD"),
            require = 0                      // 確保找不到也不會編譯錯
    )
    private void onPlayerCollisionInject(PlayerEntity player, CallbackInfo ci) {
        // 只有 pickupDelay == 0 時才算真撿起
        if (this.pickupDelay == 0) {
            OnPickupItemListener.getInstance().increment(player.getUuid());
            player.sendMessage(
                    Text.literal("[Pickup]"),
                    false
            );
        }
    }
}