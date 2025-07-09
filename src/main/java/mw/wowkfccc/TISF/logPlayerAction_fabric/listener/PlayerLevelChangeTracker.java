package mw.wowkfccc.TISF.logPlayerAction_fabric.listener;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PlayerLevelChangeTracker {
    @Unique
    private static final Map<UUID, Integer> changeCount = new HashMap<>();


    public static void recordChange(ServerPlayerEntity player) {
        UUID id = player.getUuid();
        int cnt = changeCount.merge(id, 1, Integer::sum);
//        player.sendMessage(
//                Text.literal("[等級改變] 你已改變等級 " + cnt + " 次。"),
//                false
//        );
    }

    public static int sendInsertData(UUID playerId) {
        return changeCount.getOrDefault(playerId, 0);
    }

    public static void resetCounters(UUID playerId) {
        changeCount.remove(playerId);
    }
}