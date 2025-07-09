package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AFKManager {
    // 最後一次活動時間
    private static final Map<UUID, Long> lastActivity  = new ConcurrentHashMap<>();
    // 是否目前處於 AFK
    private static final Map<UUID, Boolean> isAfk      = new ConcurrentHashMap<>();
    // AFK 開始時間
    private static final Map<UUID, Long> afkStartTime  = new ConcurrentHashMap<>();
    // 累積 AFK 時長（秒）
    private static final Map<UUID, Long> afkTotalSec   = new ConcurrentHashMap<>();

    // 閥值：300秒（5 分鐘）
    private static final long THRESHOLD_MS = 30L * 1000L;

    /** 記錄玩家有活動（任何行為都要呼） */
    public static void recordActivity(ServerPlayerEntity p) {
        UUID id = p.getUuid();
        long now = System.currentTimeMillis();

        // 如果原本是 AFK，就結束並累積
        if (Boolean.TRUE.equals(isAfk.get(id))) {
            Long start = afkStartTime.remove(id);
            if (start != null) {
                long durSec = (now - start) / 1000;
                afkTotalSec.merge(id, durSec, Long::sum);
                 p.sendMessage(Text.of("§a你已從 AFK 回來，本次持續 " + durSec + " 秒"));
            }
            isAfk.put(id, false);
        }

        lastActivity.put(id, now);
    }

    /** 在玩家 Tick 時檢查單一玩家是否進入 AFK */
    public static void checkPlayerAfk(ServerPlayerEntity p) {
        UUID id = p.getUuid();
        long now  = System.currentTimeMillis();
        long last = lastActivity.getOrDefault(id, now);

        if (!Boolean.TRUE.equals(isAfk.get(id)) && now - last >= THRESHOLD_MS) {
            isAfk.put(id, true);
            afkStartTime.put(id, now);
             p.sendMessage(Text.of("§7你已進入 §eAFK §7狀態"));
        }
    }

    /** 取得累積 AFK 時間（秒） */
    public static int getAfkTotalSeconds(UUID id) {
        return afkTotalSec.getOrDefault(id, 0L).intValue();
    }

    /** 重置單一玩家 AFK 資料 */
    public static void resetPlayer(UUID id) {
        lastActivity.remove(id);
        isAfk.remove(id);
        afkStartTime.remove(id);
        afkTotalSec.remove(id);
    }

    /** 伺服器關閉時清空所有資料 */
    public static void clearAll() {
        lastActivity.clear();
        isAfk.clear();
        afkStartTime.clear();
        afkTotalSec.clear();
    }
}