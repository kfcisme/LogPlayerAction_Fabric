package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 統計玩家使用指令次數
 */
public class PlayerCommandPreprocessTracker {
    private static final Map<UUID, Integer> commandCounts = new ConcurrentHashMap<>();

    /** 玩家每次送出以 / 開頭的訊息時呼叫 */
    public static void increment(UUID uuid) {
        commandCounts.merge(uuid, 1, Integer::sum);
    }

    /** 取得該玩家指令使用總數 */
    public static int getCount(UUID uuid) {
        return commandCounts.getOrDefault(uuid, 0);
    }

    /** （選用）重設計數 */
    public static void reset(UUID uuid) {
        commandCounts.remove(uuid);
    }
}

