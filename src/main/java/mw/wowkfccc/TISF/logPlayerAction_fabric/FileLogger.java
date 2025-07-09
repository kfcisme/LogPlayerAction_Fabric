package mw.wowkfccc.TISF.logPlayerAction_fabric;

import mw.wowkfccc.TISF.logPlayerAction_fabric.listener.PlayerActionManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FileLogger {
    private final PlayerActionManager actionTracker;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private Path logsDir;

    /** 人可讀時間格式 */
    private static final DateTimeFormatter TIMESTAMP_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public FileLogger(PlayerActionManager actionTracker) {
        this.actionTracker = actionTracker;
        // 只在伺服器啟動和停止時寫檔
        ServerLifecycleEvents.SERVER_STARTED.register(this::onServerStart);
        ServerLifecycleEvents.SERVER_STOPPED.register(this::onServerStop);
    }

    /** 伺服器啟動時：建立目錄並排程定時 flush */
    private void onServerStart(MinecraftServer server) {
        Path gameDir = FabricLoader.getInstance().getGameDir();
        Path modDir  = gameDir.resolve("mods").resolve("logplayeraction_fabric");
        logsDir      = modDir.resolve("logs");

        try {
            Files.createDirectories(logsDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 每 30 分鐘自動 flush 一次
        scheduler.scheduleAtFixedRate(() -> flushLogs(server),
                30, 30, TimeUnit.MINUTES);
    }

    /** 伺服器停止前：最後一次 flush 並關閉排程 */
    private void onServerStop(MinecraftServer server) {
        flushLogs(server);
        scheduler.shutdownNow();
    }

    /**
     * 玩家登入時呼叫此方法，
     * 會為該玩家建立 `<uuid>.csv`（若不存在），並寫入標題列。
     */
    public void createLog(UUID uuid) {
        if (logsDir == null) return;
        Path file = logsDir.resolve(uuid + ".csv");
        if (Files.notExists(file)) {
            try {
                Files.createFile(file);
                writeHeader(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** 在新建 CSV 檔的第一行寫入完整欄位名稱 */
    private void writeHeader(Path file) throws IOException {
        String header = String.join(",",
                "record_time",
                "pickup",
                "block_break",
                "tnt_prime",
                "multi_place",
                "chat",
                "block_damage",
                "block_place",
                "craft",
                "dmg_by_entity",
                "death",
//                "explosion",
                "furnace_extract",
                "inv_close",
                "inv_open",
                "bucket_empty",
                "bucket_fill",
                "cmd_pre",
//                "cmd_send",
                "player_death",
                "item_drop",
                "exp_change",
                "interact",
                "level_change",
                "quit",
                "respawn",
                "teleport",
                "chunk_load",
                "redstone",
                "afk_time"
        );
        try (BufferedWriter w = Files.newBufferedWriter(
                file,
                StandardOpenOption.APPEND
        )) {
            w.write(header);
            w.newLine();
        }
    }

    /** 批次將所有在線玩家的事件計數寫入檔案 */
    private void flushLogs(MinecraftServer server) {
        for (var player : server.getPlayerManager().getPlayerList()) {
            flushPlayer(player.getUuid());
        }
    }

    /** 取出單一玩家的計數、寫入並重置 */
    private void flushPlayer(UUID uuid) {
        if (logsDir == null) return;
        PlayerActionManager.EventCounts c = actionTracker.getAndResetCounts(uuid);
        writeLog(uuid, c);
    }

    /** 將一筆計數格式化成 CSV 一行並寫入 */
    private void writeLog(UUID uuid, PlayerActionManager.EventCounts c) {
        Path file = logsDir.resolve(uuid + ".csv");
        String line = formatCounts(c);
        try (BufferedWriter w = Files.newBufferedWriter(
                file,
                StandardOpenOption.APPEND
        )) {
            w.write(line);
            w.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** 格式化成「yyyy-MM-dd HH:mm:ss,數值,數值…」 */
    private String formatCounts(PlayerActionManager.EventCounts c) {
        String ts = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(System.currentTimeMillis()),
                ZoneId.systemDefault()
        ).format(TIMESTAMP_FMT);

        return String.join(",",
                ts,
                String.valueOf(c.pickup),
                String.valueOf(c.blockBreak),
                String.valueOf(c.tntPrime),
                String.valueOf(c.multiPlace),
                String.valueOf(c.chat),
                String.valueOf(c.blockDamage),
                String.valueOf(c.blockPlace),
                String.valueOf(c.craft),
                String.valueOf(c.dmgByEntity),
                String.valueOf(c.death),
//                String.valueOf(c.explosion),
                String.valueOf(c.furnaceExtract),
                String.valueOf(c.invClose),
                String.valueOf(c.invOpen),
                String.valueOf(c.bucketEmpty),
                String.valueOf(c.bucketFill),
                String.valueOf(c.cmdPre),
//                String.valueOf(c.cmdSend),
                String.valueOf(c.playerDeath),
                String.valueOf(c.itemDrop),
                String.valueOf(c.expChange),
                String.valueOf(c.interact),
                String.valueOf(c.levelChange),
                String.valueOf(c.quit),
                String.valueOf(c.respawn),
                String.valueOf(c.teleport),
                String.valueOf(c.chunkLoadCounts),
                String.valueOf(c.redstoneCounts),
                String.valueOf(c.afktime)
        );
    }
}
