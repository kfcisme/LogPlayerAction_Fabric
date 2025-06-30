package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.PlayerDropItemTracker;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    /**
     * 攔截 PlayerEntity.dropItem(ItemStack, boolean)
     * Yarn descriptor: dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;
     */
    @Inject(
            method = "dropItem(Lnet/minecraft/item/ItemStack;Z)Lnet/minecraft/entity/ItemEntity;",
            at     = @At("HEAD")
    )
    private void onDropItem(
            ItemStack stack,
            boolean throwRandomly,
            CallbackInfoReturnable<ItemEntity> cir
    ) {
        // mixin class 本身在 runtime 就是 PlayerEntity 的 instance，
        // 但 compile time 它是個獨立類，因此先 cast：
        PlayerEntity self = (PlayerEntity)(Object)this;
        if (self instanceof ServerPlayerEntity server) {
            // 累計丟物品次數
            PlayerDropItemTracker.increment(server.getUuid());
            int total = PlayerDropItemTracker.getCount(server.getUuid());
            // （可選）回饋玩家
            server.sendMessage(
                    net.minecraft.text.Text.literal(
                            "§6[丟物品追蹤] §f你已丟出 " + total + " 件物品"
                    ),
                    false
            );
        }
    }
}
