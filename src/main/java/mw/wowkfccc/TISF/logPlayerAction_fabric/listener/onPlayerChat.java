package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class onPlayerChat {
    private static final Map<UUID, Integer> chatCounts = new ConcurrentHashMap<>();

    /** 每次玩家送出聊天時呼叫 */
    public static void increment(UUID uuid) {
        chatCounts.merge(uuid, 1, Integer::sum);
    }

    /** 取得玩家聊天次數 */
    public static int getCount(UUID uuid) {
        return chatCounts.getOrDefault(uuid, 0);
    }

    /** （選用）重置聊天計數 */
    public static void reset(UUID uuid) {
        chatCounts.remove(uuid);
    }
}
