package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.PlayerDropItemTracker;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 攔截 PlayerEntity 的 dropItem 方法以監聽物品丟出事件
 */
@Mixin(ServerPlayerEntity .class)
public abstract class PlayerEntityDropMixin {
    @Inject(
            method = "dropSelectedItem(Z)Z",
            at = @At("HEAD"),
            require = 1
    )
    private void onDropSelectedItemInject(boolean dropAll, CallbackInfoReturnable<ItemEntity> cir) {
        ServerPlayerEntity  player = (ServerPlayerEntity)(Object)this;
        PlayerDropItemTracker.getInstance().increment(player.getUuid());
//        player.sendMessage(
//                Text.literal("[Drop]"),
//                false
//        );
    }
}

