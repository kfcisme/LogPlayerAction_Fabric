package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.screen.ScreenHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class InventoryOpenCloseListener {
    // 存玩家上一次的 handler
    private static final Map<UUID, ScreenHandler> previousHandler = new HashMap<>();
    // 記開啟次數
    private static final Map<UUID, Integer> openCounts  = new HashMap<>();
    // 記關閉次數
    private static final Map<UUID, Integer> closeCounts = new HashMap<>();


    public static void register() {
        // 玩家進入時初始化
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            UUID uuid = handler.getPlayer().getUuid();
            ScreenHandler base = handler.getPlayer().playerScreenHandler;
            previousHandler.put(uuid, base);
            openCounts.put(uuid, 0);
            closeCounts.put(uuid, 0);
        });

        // 玩家離開時清除
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            UUID uuid = handler.getPlayer().getUuid();
            previousHandler.remove(uuid);
            openCounts.remove(uuid);
            closeCounts.remove(uuid);
        });

        // 每個伺服器 tick 結束時，比對 handler
        ServerTickEvents.END_SERVER_TICK.register((MinecraftServer server) -> {
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                UUID uuid = player.getUuid();
                ScreenHandler prev = previousHandler.get(uuid);
                ScreenHandler curr = player.currentScreenHandler;

                // 從「遊戲畫面」跳到「任一 Container」：開啟
                if (prev == player.playerScreenHandler && curr != player.playerScreenHandler) {
                    openCounts.put(uuid, openCounts.get(uuid) + 1);
//                    player.sendMessage(
//                            net.minecraft.text.Text.literal("§6[inv open] §f你已合成 "
//                                    + openCounts.get(uuid) + " 次。"),
//                            false
//                    );
                }
                // 從「Container」回到「遊戲畫面」：關閉
                if (prev != player.playerScreenHandler && curr == player.playerScreenHandler) {
                    closeCounts.put(uuid, closeCounts.get(uuid) + 1);
//                    player.sendMessage(
//                            net.minecraft.text.Text.literal("§6[inv close] §f你已合成 "
//                                    + closeCounts.get(uuid) + " 次。"),
//                            false
//                    );
                }

                // 更新上一次的 handler
                previousHandler.put(uuid, curr);
            }
        });
    }

    /** 取得玩家開啟介面的次數 */
    public static int getOpenCount(UUID uuid) {
        return openCounts.getOrDefault(uuid, 0);
    }

    /** 取得玩家關閉介面的次數 */
    public static int getCloseCount(UUID uuid) {
        return closeCounts.getOrDefault(uuid, 0);
    }

    /** 重置玩家計數（若需要） */
    public static void resetCounts(UUID uuid) {
        openCounts.put(uuid, 0);
        closeCounts.put(uuid, 0);
    }
}
