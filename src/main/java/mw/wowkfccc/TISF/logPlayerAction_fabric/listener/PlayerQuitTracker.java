package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerQuitTracker {
    public static final Map<UUID, Integer> playerQuitCounts = new HashMap<>();

    public static void register() {
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.player;
            UUID playerId = player.getUuid();

            // 累加離線次數
            playerQuitCounts.put(playerId,
                    playerQuitCounts.getOrDefault(playerId, 0) + 1);
            player.sendMessage(
                    net.minecraft.text.Text.literal("§6[playerQuitCounts] §f你已合成 "
                            + playerQuitCounts.get(playerId) + " 次。"),
                    false
            );

        });
    }

    public static int sendInsertData(UUID playerId) {
        return playerQuitCounts.getOrDefault(playerId, 0);
    }

    public static void resetCounters(UUID playerId) {
        playerQuitCounts.remove(playerId);
    }
}
