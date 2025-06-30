package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.screen.ScreenHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OnInventoryCloseListener {
    // 紀錄上一次看到的 ScreenHandler
    private static final Map<UUID, ScreenHandler> previousScreenHandlers = new HashMap<>();
    // 紀錄關閉次數
    private static final Map<UUID, Integer> inventoryCloseCounts = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register((MinecraftServer server) -> {
            // 每一個伺服器 tick（約 1/20 秒）結束時呼叫
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                UUID uuid = player.getUuid();
                ScreenHandler current = player.currentScreenHandler;

                if (previousScreenHandlers.containsKey(uuid)) {
                    ScreenHandler prev = previousScreenHandlers.get(uuid);

                    // 偵測從「非預設畫面」(prev != null) → 「預設玩家畫面」(current == player.playerScreenHandler)
                    if (prev != null && current == player.playerScreenHandler) {
                        inventoryCloseCounts.put(
                                uuid,
                                inventoryCloseCounts.getOrDefault(uuid, 0) + 1
                        );
                        //（可選）回饋玩家
                        player.sendMessage(
                                net.minecraft.text.Text.literal(
                                        "§6[介面追蹤] §f你已關閉介面 " +
                                                inventoryCloseCounts.get(uuid) + " 次"
                                ),
                                false
                        );
                    }
                }

                // 更新上一次的 handler
                previousScreenHandlers.put(uuid, current);
            }
        });
    }

    /** 外部查詢用：某位玩家關閉介面的次數 */
    public static int getCount(UUID uuid) {
        return inventoryCloseCounts.getOrDefault(uuid, 0);
    }

    /** 外部呼叫：重置某位玩家的計數器（若需要） */
    public static void reset(UUID uuid) {
        inventoryCloseCounts.remove(uuid);
        previousScreenHandlers.remove(uuid);
    }
}
