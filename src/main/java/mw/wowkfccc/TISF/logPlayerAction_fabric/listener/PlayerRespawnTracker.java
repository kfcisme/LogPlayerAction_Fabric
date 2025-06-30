package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRespawnTracker {
    // UUID -> 重生次數
    private static final Map<UUID, Integer> playerRespawnCount = new HashMap<>();

    public static void register() {
        // 注意改成 ServerPlayerEvents.AFTER_RESPAWN
        ServerPlayerEvents.AFTER_RESPAWN.register((oldPlayer, newPlayer, alive) -> {
            // newPlayer 一定是 ServerPlayerEntity
            UUID playerId = newPlayer.getUuid();
            playerRespawnCount.put(
                    playerId,
                    playerRespawnCount.getOrDefault(playerId, 0) + 1
            );
            // （可選）回饋玩家
            newPlayer.sendMessage(
                    net.minecraft.text.Text.literal(
                            "§6[重生追蹤] §f你已重生 " +
                                    playerRespawnCount.get(playerId) +
                                    " 次"
                    ),
                    false
            );
        });
    }

    /** 外部查詢用 */
    public static int getCount(UUID playerId) {
        return playerRespawnCount.getOrDefault(playerId, 0);
    }

    /** 如果需要，重置計數 */
    public static void resetCounters(UUID playerId) {
        playerRespawnCount.remove(playerId);
    }
}
