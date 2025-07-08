package mw.wowkfccc.TISF.logPlayerAction_fabric;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.PlayerActionManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
//import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.server.MinecraftServer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.nio.file.Path;

public class FileLogger {
    private final LogPlayerAction_fabric plugin;
    private final PlayerActionManager actionTracker;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    //    private File logsDir;
    private Path logsDir;

    public FileLogger(LogPlayerAction_fabric plugin, PlayerActionManager actionTracker) {
        this.plugin        = plugin;
        this.actionTracker = actionTracker;

        // 伺服器啟動／停止事件
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStart);
        ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStop);
    }

    /** 伺服器啟動時被呼叫 */
    private void onServerStart(MinecraftServer server) {
        // 1) 伺服器根目錄
        Path gameDir = FabricLoader.getInstance().getGameDir();
        // 2) mods/<mod-id>/logs
        Path modDir  = gameDir.resolve("mods").resolve("logplayeraction_fabric");
        logsDir       = modDir.resolve("logs");

        try {
            Files.createDirectories(logsDir);
        } catch (IOException e) {
            System.err.println(e);
        }

        // 3) 排程每 30 分鐘寫一次
        scheduler.scheduleAtFixedRate(() -> flushLogs(server),
                30, 30, TimeUnit.MINUTES);
    }

    /** 伺服器停止前被呼叫 */
    private void onServerStop(MinecraftServer server) {
        // 停服前再 flush 一次
        flushLogs(server);
        scheduler.shutdownNow();
    }

    /** 玩家 JOIN 時，建立一個空的 log 檔（若檔案已存在則不改） */
    public void createLog(UUID uuid) {
        if (logsDir == null) return; // 尚未啟動
        Path file = logsDir.resolve(uuid.toString() + ".log");
        if (Files.notExists(file)) {
            try {
                Files.createFile(file);
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    /** 取出所有在線玩家的統計並寫入各自的 log 檔 */
    public void flushLogs(MinecraftServer server) {
        for (var player : server.getPlayerManager().getPlayerList()) {
            UUID uuid = player.getUuid();
            var counts = actionTracker.getAndResetCounts(uuid);
            writeLog(uuid, counts);
        }
    }

    /** 寫入單一玩家的統計到 CSV 行 */
    private void writeLog(UUID uuid, PlayerActionManager.EventCounts c) {
        Path file = logsDir.resolve(uuid.toString() + ".log");
        String line = formatCounts(c);
        try (BufferedWriter w = Files.newBufferedWriter(
                file,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            w.write(line);
            w.newLine();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    /**
     * 格式化事件計數為 CSV，欄位順序請對應你原本 insertData() 的 ps.setXXX()：
     * timestamp, pickup, blockBreak, tntPrime, multiPlace, chat, blockDamage, blockPlace,
     * craft, dmgByEntity, death, furnaceExtract, invClose, invOpen, bucketEmpty, bucketFill,
     * cmdPre, playerDeath, itemDrop, expChange, interact, levelChange, quit, respawn,
     * teleport, chunkLoadCounts, redstoneCounts
     */
    private String formatCounts(PlayerActionManager.EventCounts c) {
        long ts = System.currentTimeMillis();
        return ts + "," +
                c.pickup + "," +
                c.blockBreak + "," +
                c.tntPrime + "," +
//                c.multiPlace + "," +
                c.chat + "," +
                c.blockDamage + "," +
                c.blockPlace + "," +
                c.craft + "," +
                c.dmgByEntity + "," +
                c.death + "," +
                c.furnaceExtract + "," +
                c.invClose + "," +
                c.invOpen + "," +
                c.bucketEmpty + "," +
                c.bucketFill + "," +
                c.cmdPre + "," +
                c.playerDeath + "," +
                c.itemDrop + "," +
                c.expChange + "," +
                c.interact + "," +
                c.levelChange + "," +
                c.quit + "," +
                c.respawn + "," +
                c.teleport + "," +
                c.chunkLoadCounts + "," +
                c.redstoneCounts;
    }
}


