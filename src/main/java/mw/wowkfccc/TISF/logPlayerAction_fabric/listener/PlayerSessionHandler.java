//package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;
//
//import mw.wowkfccc.TISF.logPlayerAction_fabric.LogPlayerAction_fabric;
//import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
//import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.network.ServerPlayerEntity;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.UUID;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * 負責：
// *  1. 玩家 JOIN/DISCONNECT 時初始化或移除其統計
// *  2. 伺服器啟動後每 30 分鐘，將 PlayerActionManager.getAndResetCounts() 回傳的
// *     各項事件計數，格式化成一行 CSV，附加寫入 logs/<uuid>.log
// *  3. 伺服器停止前再 flush 一次並關閉排程
// */
//public class PlayerSessionHandler {
//    private final LogPlayerAction_fabric     plugin;
//    private final PlayerActionManager        actionTracker;
//    private final ScheduledExecutorService   scheduler = Executors.newSingleThreadScheduledExecutor();
//    private File                              logsDir;
//
//    public PlayerSessionHandler(LogPlayerAction_fabric plugin,
//                                PlayerActionManager actionTracker) {
//        this.plugin        = plugin;
//        this.actionTracker = actionTracker;
//
//        // 1) 玩家加入：初始化統計
//        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
//            UUID uuid = handler.getPlayer().getUuid();
//            actionTracker.initializePlayer(uuid);
//        });
//
//        // 2) 玩家離線：移除統計
//        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
//            UUID uuid = handler.getPlayer().getUuid();
//            actionTracker.removePlayer(uuid);
//        });
//
//        // 3) 伺服器啟動：建立 logs 目錄並啟動排程
//        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStart);
//
//        // 4) 伺服器停止：先 flush 再關閉排程
//        ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStop);
//    }
//
//    private void onServerStart(MinecraftServer server) {
//        // logs 資料夾放在 plugins/<mod-id>/logs
//        logsDir = new File(plugin.getDataFolder(), "logs");
//        if (!logsDir.exists() && !logsDir.mkdirs()) {
//            System.out.println("無法建立 logs 資料夾: " + logsDir.getAbsolutePath());
//        }
//
//        // 延遲 30 分鐘後開始，每 30 分鐘執行一次 flushLogs
//        scheduler.scheduleAtFixedRate(() -> flushLogs(server),
//                30, 30, TimeUnit.MINUTES);
//    }
//
//    private void onServerStop(MinecraftServer server) {
//        // 伺服器關閉前再 flush 一次
//        flushLogs(server);
//        scheduler.shutdownNow();
//    }
//
//    /** 取出所有在線玩家的統計並寫入各自的 log 檔 */
//    private void flushLogs(MinecraftServer server) {
//        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
//            UUID uuid = player.getUuid();
//            PlayerActionManager.EventCounts c = actionTracker.getAndResetCounts(uuid);
//            writeLog(uuid, c);
//        }
//    }
//
//    /** 寫入單位玩家的統計到 logs/<uuid>.log（CSV） */
//    private void writeLog(UUID uuid, PlayerActionManager.EventCounts c) {
//        File file = new File(logsDir, uuid.toString() + ".log");
//        String line = formatCounts(c);
//        try (FileWriter w = new FileWriter(file, true)) {
//            w.write(line);
//            w.write(System.lineSeparator());
//        } catch (IOException e) {
//            System.err.println("寫入玩家 " + uuid + " 日誌失敗");
//        }
//    }
//
//    /**
//     * 依照你原本 mySQLInsertData.insertEventCounts() 中 ps.setXXX(...) 的欄位順序：
//     * 1 pickup, 2 blockBreak, 3 tntPrime, 4 multiPlace, 5 chat,
//     * 6 blockDamage, 7 blockPlace, 8 craft, 9 dmgByEntity, 10 death,
//     * 12 furnaceExtract, 13 invClose, 14 invOpen, 15 bucketEmpty, 16 bucketFill,
//     * 17 cmdPre, 19 playerDeath, 20 itemDrop, 21 expChange,
//     * 22 interact, 23 levelChange, 24 quit, 25 respawn, 26 teleport,
//     * 27 chunkLoadCounts, 28 redstoneCounts
//     */
//    private String formatCounts(PlayerActionManager.EventCounts c) {
//        long ts = System.currentTimeMillis();
//        return ts + "," +
//                c.pickup + "," +
//                c.blockBreak + "," +
//                c.tntPrime + "," +
//                c.multiPlace + "," +
//                c.chat + "," +
//                c.blockDamage + "," +
//                c.blockPlace + "," +
//                c.craft + "," +
//                c.dmgByEntity + "," +
//                c.death + "," +
//                /* skip explosion */ "" + "," +
//                c.furnaceExtract + "," +
//                c.invClose + "," +
//                c.invOpen + "," +
//                c.bucketEmpty + "," +
//                c.bucketFill + "," +
//                c.cmdPre + "," +
//                /* skip cmdSend */ "" + "," +
//                c.playerDeath + "," +
//                c.itemDrop + "," +
//                c.expChange + "," +
//                c.interact + "," +
//                c.levelChange + "," +
//                c.quit + "," +
//                c.respawn + "," +
//                c.teleport + "," +
//                c.chunkLoadCounts + "," +
//                c.redstoneCounts;
//    }
//}

package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import mw.wowkfccc.TISF.logPlayerAction_fabric.FileLogger;
import mw.wowkfccc.TISF.logPlayerAction_fabric.LogPlayerAction_fabric;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import java.util.UUID;

/**
 * 負責：
 *  1. 玩家 JOIN 時，初始化統計並建立 log 檔
 *  2. 玩家 DISCONNECT 時，移除統計
 *  3. log 寫入與排程由 FileLogger 處理
 */
public class PlayerSessionHandler {
    private final LogPlayerAction_fabric plugin;
    private final PlayerActionManager actionTracker;
    private final FileLogger fileLogger;

    public PlayerSessionHandler(LogPlayerAction_fabric plugin, PlayerActionManager actionTracker, FileLogger fileLogger) {
        this.plugin        = plugin;
        this.actionTracker = actionTracker;
        // 建構子中初始化 FileLogger（它已在內部註冊 SERVER_STARTED/STOPPED 並排程寫檔）
        this.fileLogger    = new FileLogger(actionTracker);

        // 1) 玩家加入：初始化統計、建立 log 檔
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            UUID uuid = handler.getPlayer().getUuid();
            actionTracker.initializePlayer(uuid);
            this.fileLogger.createLog(uuid);
        });

        // 2) 玩家離線：移除統計
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            UUID uuid = handler.getPlayer().getUuid();
            actionTracker.removePlayer(uuid);
        });
    }
}

