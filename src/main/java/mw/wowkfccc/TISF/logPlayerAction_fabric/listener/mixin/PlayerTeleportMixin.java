package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.PlayerTeleportTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerTeleportMixin {
    @Inject(
            method = "teleport(Lnet/minecraft/server/world/ServerWorld;DDDLjava/util/Set;FF)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onTeleportInject(
            ServerWorld      destination,    // ← 一定要加上這個
            double            x,
            double            y,
            double            z,
            Set<?>            movementFlags, // 泛型擦除，直接用 Set<?>
            float             yaw,
            float             pitch,
            CallbackInfoReturnable<Boolean> cir
    ) {
        ServerPlayerEntity self = (ServerPlayerEntity)(Object)this;
        PlayerTeleportTracker.increment(self.getUuid());
        int total = PlayerTeleportTracker.getCount(self.getUuid());
        self.sendMessage(
                net.minecraft.text.Text.literal(
                        "§6[傳送追蹤] §f你已傳送 " + total + " 次"
                ),
                false
        );
        // 如需取消原本傳送可呼：cir.setReturnValue(false);
    }
}
