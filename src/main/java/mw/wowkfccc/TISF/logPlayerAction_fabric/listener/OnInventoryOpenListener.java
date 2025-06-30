package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OnInventoryOpenListener {
    // 紀錄上一次的 ScreenHandler
    private static final Map<UUID, ScreenHandler> previousScreenHandlers = new HashMap<>();
    // 紀錄開啟介面次數
    private static final Map<UUID, Integer> inventoryOpenCounts   = new HashMap<>();

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register((MinecraftServer server) -> {
            // 每個伺服器 tick 結束時遍歷所有玩家
            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                UUID uuid     = player.getUuid();
                ScreenHandler current  = player.currentScreenHandler;
                ScreenHandler previous = previousScreenHandlers.get(uuid);

                // 偵測從「主畫面(playerScreenHandler)」→「其他 GUI(current != playerScreenHandler)」
                if (previous != null
                        && previous == player.playerScreenHandler
                        && current  != player.playerScreenHandler)
                {
                    int c = inventoryOpenCounts.getOrDefault(uuid, 0) + 1;
                    inventoryOpenCounts.put(uuid, c);

                    // （可選）告訴玩家他打開了第幾次介面
                    player.sendMessage(
                            Text.literal("§6[介面追蹤] §f你已打開介面 " + c + " 次"),
                            false
                    );
                }

                // 更新上一次的 handler
                previousScreenHandlers.put(uuid, current);
            }
        });
    }

    /** 查詢玩家開啟介面次數 */
    public static int getCount(UUID uuid) {
        return inventoryOpenCounts.getOrDefault(uuid, 0);
    }

    /** 重置玩家資料（如果需要） */
    public static void reset(UUID uuid) {
        inventoryOpenCounts.remove(uuid);
        previousScreenHandlers.remove(uuid);
    }
}
