package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 每 tick 都遍歷所有玩家，並比對「上次所在區塊」與「本次所在區塊」是否不同，
 * 只有不同才算一次新的區塊載入。
 */
public class PlayerChunkLoadListener {
    // 玩家 UUID → 累積載入新區塊次數
    private static final Map<UUID, Integer> chunkLoadCounts = new ConcurrentHashMap<>();
    // 玩家 UUID → 上次 tick 所在的 ChunkPos
    private static final Map<UUID, ChunkPos> lastChunkPos   = new ConcurrentHashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register((MinecraftServer server) -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                UUID uuid = player.getUuid();

                // 取出玩家當前位置
                BlockPos blockPos = player.getBlockPos();
                ChunkPos currentChunk = new ChunkPos(blockPos);

                // 如果是第一次，就記錄一下，不算載入
                if (!lastChunkPos.containsKey(uuid)) {
                    lastChunkPos.put(uuid, currentChunk);
                    continue;
                }

                ChunkPos prevChunk = lastChunkPos.get(uuid);
                // 只有跨到新區塊才累計
                if (!currentChunk.equals(prevChunk)) {
                    lastChunkPos.put(uuid, currentChunk);
                    int c = chunkLoadCounts.getOrDefault(uuid, 0) + 1;
                    chunkLoadCounts.put(uuid, c);

                    // （可選）即時回饋玩家
                    player.sendMessage(
                            Text.literal("§6[區塊載入追蹤] §f你已載入 " + c + " 個新區塊"),
                            false
                    );
                }
            }
        });
    }

    /** 取得玩家累積載入區塊次數 */
    public static int getCount(UUID uuid) {
        return chunkLoadCounts.getOrDefault(uuid, 0);
    }

    /** 重置某玩家的資料（如需要） */
    public static void reset(UUID uuid) {
        chunkLoadCounts.remove(uuid);
        lastChunkPos.remove(uuid);
    }
}
