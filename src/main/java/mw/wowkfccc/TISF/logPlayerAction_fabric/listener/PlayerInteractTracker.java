package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Hand;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerInteractTracker {
    private static final Map<UUID, Integer> playerInteractCount = new HashMap<>();

    public static void register() {
        // 玩家與方塊互動（如點擊方塊）
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                UUID id = serverPlayer.getUuid();
                playerInteractCount.put(id, playerInteractCount.getOrDefault(id, 0) + 1);
//                player.sendMessage(
//                        net.minecraft.text.Text.literal("§6[playerInteractCount] §f你已合成 "
//                                + playerInteractCount.get(id) + " 次。"),
//                        false
//                );
            }
            return ActionResult.PASS;
        });

        // 玩家與物品互動（如右鍵空氣使用物品）
        UseItemCallback.EVENT.register((player, world, hand) -> {
            if (player instanceof ServerPlayerEntity serverPlayer) {
                UUID id = serverPlayer.getUuid();
                playerInteractCount.put(id, playerInteractCount.getOrDefault(id, 0) + 1);
//                player.sendMessage(
//                        net.minecraft.text.Text.literal("§6[playerInteractCount] §f你已合成 "
//                                + playerInteractCount.get(id) + " 次。"),
//                        false
//                );
            }
            return TypedActionResult.pass(player.getStackInHand(hand));
        });
    }

    public static int sendInsertData(UUID playerId) {
        return playerInteractCount.getOrDefault(playerId, 0);
    }

    public static void resetCounters(UUID playerId) {
        playerInteractCount.remove(playerId);
    }
}