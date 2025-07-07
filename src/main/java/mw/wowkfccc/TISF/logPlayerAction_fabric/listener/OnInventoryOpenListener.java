//package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;
//
//import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
//import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.network.ServerPlayerEntity;
//import net.minecraft.screen.ScreenHandler;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * 透過每個 tick 比對玩家的 currentScreenHandler，
// * 來判斷「從遊戲畫面(playerScreenHandler)→介面(container)」是開啟，
// * 「從介面→遊戲畫面」是關閉，
// * 並且只各+1次。
// */
//public class InventoryOpenCloseListener {
//    // 存玩家上一次的 handler
//    private static final Map<UUID, ScreenHandler> previousHandler = new HashMap<>();
//    // 記開啟次數
//    private static final Map<UUID, Integer> openCounts  = new HashMap<>();
//    // 記關閉次數
//    private static final Map<UUID, Integer> closeCounts = new HashMap<>();
//
//    /**
//     * 在伺服器啟動時呼叫一次，註冊 JOIN、DISCONNECT、END_SERVER_TICK
//     */
//    public static void register() {
//        // 玩家進入時初始化
//        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
//            UUID uuid = handler.getPlayer().getUuid();
//            ScreenHandler base = handler.getPlayer().playerScreenHandler;
//            previousHandler.put(uuid, base);
//            openCounts.put(uuid, 0);
//            closeCounts.put(uuid, 0);
//        });
//
//        // 玩家離開時清除
//        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
//            UUID uuid = handler.getPlayer().getUuid();
//            previousHandler.remove(uuid);
//            openCounts.remove(uuid);
//            closeCounts.remove(uuid);
//        });
//
//        // 每個伺服器 tick 結束時，比對 handler
//        ServerTickEvents.END_SERVER_TICK.register((MinecraftServer server) -> {
//            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
//                UUID uuid = player.getUuid();
//                ScreenHandler prev = previousHandler.get(uuid);
//                ScreenHandler curr = player.currentScreenHandler;
//
//                // 從「遊戲畫面」跳到「任一 Container」：開啟
//                if (prev == player.playerScreenHandler && curr != player.playerScreenHandler) {
//                    openCounts.put(uuid, openCounts.get(uuid) + 1);
//                }
//                // 從「Container」回到「遊戲畫面」：關閉
//                if (prev != player.playerScreenHandler && curr == player.playerScreenHandler) {
//                    closeCounts.put(uuid, closeCounts.get(uuid) + 1);
//                }
//
//                // 更新上一次的 handler
//                previousHandler.put(uuid, curr);
//            }
//        });
//    }
//
//    /** 取得玩家開啟介面的次數 */
//    public static int getOpenCount(UUID uuid) {
//        return openCounts.getOrDefault(uuid, 0);
//    }
//
//    /** 取得玩家關閉介面的次數 */
//    public static int getCloseCount(UUID uuid) {
//        return closeCounts.getOrDefault(uuid, 0);
//    }
//
//    /** 重置玩家計數（若需要） */
//    public static void resetCounts(UUID uuid) {
//        openCounts.put(uuid, 0);
//        closeCounts.put(uuid, 0);
//    }
//}
