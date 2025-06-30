package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.PlayerTeleportTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerTeleportMixin {
    @Inject(method = "teleport", at = @At("HEAD"))
    private void onTeleportInject(
            double x, double y, double z,
            float yaw, float pitch,
            CallbackInfo ci
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
    }
}