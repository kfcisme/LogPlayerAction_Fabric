package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.mixin;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.notworked.PlayerCommandPreprocessTracker;
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
 * 攔截玩家聊天/指令封包，統計 “/” 開頭的指令次數
 */
@Mixin(ServerPlayNetworkHandler.class)
public abstract class PlayerCommandCountMixin {
    @Shadow private ServerPlayerEntity player;

    @Inject(
            method = "onChatMessage",    // 只寫方法名
            at     = @At("HEAD"),
            require=0                    // 找不到也不會 fail
    )
    private void onChatMessageInject(ChatMessageC2SPacket pkt, CallbackInfo ci) {
        String msg = pkt.chatMessage().trim();
        if (msg.startsWith("/")) {
            PlayerCommandPreprocessTracker.increment(player.getUuid());
            int total = PlayerCommandPreprocessTracker.getCount(player.getUuid());
            player.sendMessage(Text.literal("§6[指令追蹤] §f你已使用指令 "+ total +" 次"), false);
        }
    }
}
