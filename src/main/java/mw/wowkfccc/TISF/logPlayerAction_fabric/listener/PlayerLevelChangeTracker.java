package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerLevelChangeTracker {
    private static final Map<UUID, Integer> playerLevelChangeCount = new HashMap<>();

    public static void register() {
        ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
            UUID id = newPlayer.getUuid();
            // 當玩家重生時更新數據（選擇性）
            if (alive) {
                playerLevelChangeCount.putIfAbsent(id, 0);
            }
        });
    }

    public static void onLevelChange(ServerPlayerEntity player, int oldLevel, int newLevel) {
        UUID id = player.getUuid();
        if (oldLevel != newLevel) {
            playerLevelChangeCount.put(id, playerLevelChangeCount.getOrDefault(id, 0) + 1);
        }
    }

    public static int sendInsertData(UUID playerId) {
        return playerLevelChangeCount.getOrDefault(playerId, 0);
    }

    public static void resetCounters(UUID playerId) {
        playerLevelChangeCount.remove(playerId);
    }
}