package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AFKActivityListener {
    // 用來檢測移動：記錄上一次位置
    private static final Map<UUID, Vec3d> lastPos = new ConcurrentHashMap<>();

    /** 一次性呼叫，註冊所有 Fabric 事件 callback */
    public static void register() {
        // 玩家加入
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity p = handler.player;
            AFKManager.recordActivity(p);
            lastPos.put(p.getUuid(), p.getPos());
        });

        // 攔截聊天／指令（C2S chat_message 封包）
        ServerPlayNetworking.registerGlobalReceiver(
                new Identifier("minecraft", "chat_message"),
                (server, player, handler, buf, responseSender) -> {
                    buf.readString(32767);  // 讀掉內容
                    AFKManager.recordActivity(player);
                }
        );

        // 方塊破壞
        AttackBlockCallback.EVENT.register((player, world, hand, pos, dir) -> {
            if (player instanceof ServerPlayerEntity p) {
                AFKManager.recordActivity(p);
            }
            return ActionResult.PASS;
        });

        // 方塊使用（含放置）
        UseBlockCallback.EVENT.register((player, world, hand, hit) -> {
            if (player instanceof ServerPlayerEntity p) {
                AFKManager.recordActivity(p);
            }
            return ActionResult.PASS;
        });

        // 物品使用（例如丟雪球、用水桶等）
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (player instanceof ServerPlayerEntity p) {
                AFKManager.recordActivity(p);
            }
            return TypedActionResult.pass(player.getStackInHand(hand));
        });

        // 每個伺服器 Tick：檢查移動與 AFK
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            server.getPlayerManager().getPlayerList().forEach(player -> {
                // 移動檢測
                Vec3d prev = lastPos.getOrDefault(player.getUuid(), player.getPos());
                Vec3d now  = player.getPos();
                if (!now.equals(prev)) {
                    AFKManager.recordActivity(player);
                    lastPos.put(player.getUuid(), now);
                }
                // AFK 狀態檢查
                AFKManager.checkPlayerAfk(player);
            });
        });
    }
}