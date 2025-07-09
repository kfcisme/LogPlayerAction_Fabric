package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.CommandCountManager;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(ServerPlayNetworkHandler.class)
public class PlayerCommandCountMixin {
    // shadow 出來方便拿到 player
    @Shadow public ServerPlayerEntity player;

    @Inject(
            method = "onCommandExecution(Lnet/minecraft/network/packet/c2s/play/CommandExecutionC2SPacket;)V",
            at = @At("HEAD")
    )
    private void onCommandPacket(CommandExecutionC2SPacket packet, CallbackInfo ci) {
        // packet.getCommand() 可以拿到原始字串（包含 "/" 或不含）
        CommandCountManager.increment(player.getUuid());
    }
}