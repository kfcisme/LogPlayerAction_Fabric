package mw.wowkfccc.TISF.logPlayerAction_fabric.listener.notworked;

import net.minecraft.text.Text;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class PlayerCommandPreprocessTracker {
    private static final Map<UUID, Integer> commandCounts = new ConcurrentHashMap<>();

    public static void increment(UUID uuid) {
        commandCounts.merge(uuid, 1, Integer::sum
        );
        System.out.println(Text.literal("§a[commandCounts] §f第 " + commandCounts + " 次"));
    }

    public static int getCount(UUID uuid) {
        return commandCounts.getOrDefault(uuid, 0);
    }

    public static void reset(UUID uuid) {
        commandCounts.remove(uuid);
    }
}

