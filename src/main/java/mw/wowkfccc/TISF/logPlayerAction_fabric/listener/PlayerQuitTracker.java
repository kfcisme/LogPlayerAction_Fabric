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

            // 這裡可選擇紀錄日誌或傳送到資料庫
            // System.out.println("玩家離線：" + player.getName().getString() + " 次數: " + playerQuitCounts.get(playerId));
        });
    }

    public static int sendInsertData(UUID playerId) {
        return playerQuitCounts.getOrDefault(playerId, 0);
    }

    public static void resetCounters(UUID playerId) {
        playerQuitCounts.remove(playerId);
    }
}
