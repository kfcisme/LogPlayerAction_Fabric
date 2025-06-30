package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.onPlayerChat;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 在伺服器收到玩家聊天封包時，注入統計邏輯
 */
@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    // Shadow 出原類裡的玩家物件
    @Shadow public ServerPlayerEntity player;

    @Inject(
            method = "onChatMessage(Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;)V",
            at     = @At("HEAD")
    )
    private void onChatMessageInject(ChatMessageC2SPacket packet, CallbackInfo ci) {
        // 純伺服器端，累計一次聊天
        onPlayerChat.increment(player.getUuid());

        // （可選）可即時顯示給玩家
        int total = onPlayerChat.getCount(player.getUuid());
        player.sendMessage(
                Text.literal("§6[聊天追蹤] §f你已發言 " + total + " 次"),
                false
        );
    }
}